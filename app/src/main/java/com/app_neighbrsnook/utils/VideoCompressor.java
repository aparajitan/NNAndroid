package com.app_neighbrsnook.utils;

import android.content.Context;
import android.media.MediaCodec;
import android.media.MediaExtractor;
import android.media.MediaFormat;
import android.media.MediaMetadataRetriever;
import android.media.MediaMuxer;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

public class VideoCompressor {

    private static final String TAG = "VideoCompressor";

    public interface CompressionListener {
        void onCompressionSuccess(Uri compressedUri);
        void onCompressionFailure(String error);
        void onProgressUpdate(int progress);
    }

    public static void compressVideo(Context context, Uri videoUri, CompressionListener listener) {
        new Thread(() -> {
            try {
                File outputFile = createOutputFile();
                boolean success = compressVideoWithMediaCodec(context, videoUri, outputFile.getAbsolutePath(), listener);

                if (success) {
                    Uri compressedUri = Uri.fromFile(outputFile);
                    if (listener != null) {
                        listener.onCompressionSuccess(compressedUri);
                    }
                } else {
                    if (listener != null) {
                        listener.onCompressionFailure("Video compression failed");
                    }
                }
            } catch (Exception e) {
                Log.e(TAG, "Compression error: " + e.getMessage());
                if (listener != null) {
                    listener.onCompressionFailure("Compression error: " + e.getMessage());
                }
            }
        }).start();
    }

    private static File createOutputFile() {
        File movieDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES);
        if (!movieDirectory.exists()) {
            movieDirectory.mkdirs();
        }
        return new File(movieDirectory, "compressed_video_" + System.currentTimeMillis() + ".mp4");
    }

    private static boolean compressVideoWithMediaCodec(Context context, Uri videoUri, String outputPath, CompressionListener listener) {
        MediaExtractor extractor = null;
        MediaMuxer muxer = null;

        try {
            extractor = new MediaExtractor();
            extractor.setDataSource(context, videoUri, null);

            muxer = new MediaMuxer(outputPath, MediaMuxer.OutputFormat.MUXER_OUTPUT_MPEG_4);

            // Detect and preserve original rotation
            int originalRotation = getVideoRotation(context, videoUri);
            Log.d(TAG, "Original video rotation: " + originalRotation);

            // Rotation metadata preserve karo
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                muxer.setOrientationHint(originalRotation);
            }

            int videoTrackIndex = -1;
            int audioTrackIndex = -1;
            int videoTrackId = -1;
            int audioTrackId = -1;

            // Find video and audio tracks
            for (int i = 0; i < extractor.getTrackCount(); i++) {
                MediaFormat format = extractor.getTrackFormat(i);
                String mime = format.getString(MediaFormat.KEY_MIME);

                if (mime.startsWith("video/")) {
                    // Optimized video compression settings
                    int originalBitrate = 1500000; // 1.5 Mbps

                    // Adjust bitrate based on video resolution
                    int videoWidth = format.getInteger(MediaFormat.KEY_WIDTH);
                    int videoHeight = format.getInteger(MediaFormat.KEY_HEIGHT);

                    if (videoWidth >= 1920 || videoHeight >= 1080) {
                        // 1080p or higher - use 2 Mbps
                        originalBitrate = 2000000;
                    } else if (videoWidth >= 1280 || videoHeight >= 720) {
                        // 720p - use 1.2 Mbps
                        originalBitrate = 1200000;
                    } else {
                        // Lower resolutions - use 800 kbps
                        originalBitrate = 800000;
                    }

                    format.setInteger(MediaFormat.KEY_BIT_RATE, originalBitrate);
                    format.setInteger(MediaFormat.KEY_FRAME_RATE, 24); // Reduced from 30 to 24
                    format.setInteger(MediaFormat.KEY_I_FRAME_INTERVAL, 3); // Increased from 2 to 3

                    Log.d(TAG, "Video resolution: " + videoWidth + "x" + videoHeight);
                    Log.d(TAG, "Using bitrate: " + originalBitrate);

                    videoTrackIndex = i;
                    videoTrackId = muxer.addTrack(format);
                } else if (mime.startsWith("audio/")) {
                    // Optimized audio compression
                    format.setInteger(MediaFormat.KEY_BIT_RATE, 64000); // Reduced from 96k to 64k
                    audioTrackIndex = i;
                    audioTrackId = muxer.addTrack(format);
                }
            }

            if (videoTrackIndex == -1) {
                return false; // No video track found
            }

            muxer.start();

            // Process video track
            if (videoTrackIndex != -1) {
                extractor.selectTrack(videoTrackIndex);
                MediaCodec.BufferInfo bufferInfo = new MediaCodec.BufferInfo();
                ByteBuffer buffer = ByteBuffer.allocate(1024 * 1024);

                while (true) {
                    int sampleSize = extractor.readSampleData(buffer, 0);
                    if (sampleSize < 0) {
                        break;
                    }

                    bufferInfo.offset = 0;
                    bufferInfo.size = sampleSize;
                    bufferInfo.flags = extractor.getSampleFlags();
                    bufferInfo.presentationTimeUs = extractor.getSampleTime();

                    muxer.writeSampleData(videoTrackId, buffer, bufferInfo);
                    extractor.advance();
                }
                extractor.unselectTrack(videoTrackIndex);
            }

            // Process audio track if available
            if (audioTrackIndex != -1) {
                extractor.selectTrack(audioTrackIndex);
                MediaCodec.BufferInfo bufferInfo = new MediaCodec.BufferInfo();
                ByteBuffer buffer = ByteBuffer.allocate(64 * 1024);

                while (true) {
                    int sampleSize = extractor.readSampleData(buffer, 0);
                    if (sampleSize < 0) {
                        break;
                    }

                    bufferInfo.offset = 0;
                    bufferInfo.size = sampleSize;
                    bufferInfo.flags = extractor.getSampleFlags();
                    bufferInfo.presentationTimeUs = extractor.getSampleTime();

                    muxer.writeSampleData(audioTrackId, buffer, bufferInfo);
                    extractor.advance();
                }
            }

            return true;

        } catch (Exception e) {
            Log.e(TAG, "Compression error: " + e.getMessage());
            return false;
        } finally {
            if (extractor != null) {
                extractor.release();
            }
            if (muxer != null) {
                try {
                    muxer.stop();
                    muxer.release();
                } catch (Exception e) {
                    Log.e(TAG, "Error releasing muxer: " + e.getMessage());
                }
            }
        }
    }

    private static int getVideoRotation(Context context, Uri videoUri) throws IOException {
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        try {
            retriever.setDataSource(context, videoUri);
            String rotationStr = retriever.extractMetadata(
                    MediaMetadataRetriever.METADATA_KEY_VIDEO_ROTATION
            );
            return rotationStr != null ? Integer.parseInt(rotationStr) : 0;
        } catch (Exception e) {
            Log.e(TAG, "Error getting video rotation: " + e.getMessage());
            return 0;
        } finally {
            retriever.release();
        }
    }
}