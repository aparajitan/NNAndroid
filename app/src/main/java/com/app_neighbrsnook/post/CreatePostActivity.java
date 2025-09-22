package com.app_neighbrsnook.post;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaCodec;
import android.media.MediaExtractor;
import android.media.MediaFormat;
import android.media.MediaMuxer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.app_neighbrsnook.MainActivity;
import com.app_neighbrsnook.R;
import com.app_neighbrsnook.abusive.BadWordFilter;
import com.app_neighbrsnook.adapter.BusinessCategoryAdapter;
import com.app_neighbrsnook.adapter.ImageUploadAdapter;
import com.app_neighbrsnook.adapter.ImageUploadAdapter1;
import com.app_neighbrsnook.adapter.MediaViewAdapter;
import com.app_neighbrsnook.adapter.SelectPostTypeAdapter;
import com.app_neighbrsnook.apiService.UrlClass;
import com.app_neighbrsnook.model.ImagePOJO;
import com.app_neighbrsnook.network.APIClient;
import com.app_neighbrsnook.network.APIInterface;
import com.app_neighbrsnook.pojo.Nbdatum;
import com.app_neighbrsnook.pojo.PostPojo;
import com.app_neighbrsnook.pojo.StatePojo;
import com.app_neighbrsnook.utils.GlobalMethods;
import com.app_neighbrsnook.utils.SharedPrefsManager;
import com.app_neighbrsnook.libraries.cropper.CropImage;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreatePostActivity extends AppCompatActivity implements BusinessCategoryAdapter.CategoryInterface, ImageUploadAdapter1.ImageItemClick, SelectPostTypeAdapter.CategoryInterface, ImageUploadAdapter.ImageRequest, View.OnClickListener, MediaViewAdapter.OnItemClickListener {
    ImageView img_back, add_imageview1;
    Dialog mail_dialog, image_dialog;
    SharedPrefsManager sm;
    TextView tv_post, image_textview, doc_textview, cancle_tv, image_count_textview;
    RelativeLayout rl_upload_layout;
    RecyclerView photo_recycler_view;
    ArrayList<ImagePOJO> bitmapList = new ArrayList<>();
    ImageUploadAdapter1 uploadAdapter;
    Context context;
    Activity activity;
    TextView tv_take_photo, tv_choose_photo, tv_take_video, tv_choose_video, tv_post_btn;
    private final int TAKE_VIDEO_FROM_GALLERY = 1;
    private final int TAKE_VIDEO_FROM_CAMERA = 2;
    private final int TAKE_PIC_FROM_GALLERY = 3;
    private final int TAKE_PIC_FROM_CAMERA = 4;
    FrameLayout frm_layout;
    String currentPhotoPath;
    RelativeLayout rl_laout;
    EditText edt;
    int categoryid;
    Uri imageUri, videoUri;
    File imageFile;
    ArrayList<Bitmap> mArrayGalleryPhoto = new ArrayList<>();
    ImageUploadAdapter imageUploadAdapter;
    MediaViewAdapter mediaViewAdapter;
    ArrayList<Uri> mArrayUri = new ArrayList();
    ArrayList<Uri> imageUris = new ArrayList<>();
    ArrayList<Uri> videoUris = new ArrayList<>();
    List<Nbdatum> categorylist = new ArrayList<>();
    ProgressDialog progressDialog;
    int totalImageCount = 2, totalVideoCount = 1, itemCount, postVideoSize;
    LinearLayout root;
    String apiStatus;
    private ArrayList<String> videoFilePaths = new ArrayList<>();
    private static final int PERMISSION_REQUEST_CODE = 123;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = activity = this;
        setContentView(R.layout.activity_create_post);
        sm = new SharedPrefsManager(this);


        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);

        idInitialize();
        postTypeApi();

        uploadAdapter = new ImageUploadAdapter1(this, bitmapList, this);
        photo_recycler_view.setLayoutManager(new GridLayoutManager(context, 4, RecyclerView.VERTICAL, false));
        photo_recycler_view.setAdapter(uploadAdapter);
        rl_upload_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rl_upload_layout.setVisibility(View.GONE);
            }
        });
        rl_laout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edt.setVisibility(View.GONE);
            }
        });
        add_imageview1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ( GlobalMethods.checkCameraAndGalleryPermission(CreatePostActivity.this)) {
                    if (mArrayUri.size() < itemCount) {
                        rl_upload_layout.setVisibility(View.VISIBLE);
                    } else {
                        rl_upload_layout.setVisibility(View.GONE);
                        GlobalMethods.getInstance(CreatePostActivity.this).globalDialog(CreatePostActivity.this, "You can only add up to " + itemCount + " media items (" + totalVideoCount + " video and " + totalImageCount + " images)");
                    }
                }
            }
        });
        tv_take_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                rl_upload_layout.setVisibility(View.GONE);
                String fileName = "photo";
                File storageDirectory = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                try {
                    imageFile = File.createTempFile(fileName, "jpg", storageDirectory);
                    currentPhotoPath = imageFile.getAbsolutePath();
                    imageUri = FileProvider.getUriForFile(CreatePostActivity.this, "com.app_neighbrsnook.fileprovider", imageFile);

                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                    startActivityForResult(intent, TAKE_PIC_FROM_CAMERA);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        tv_choose_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rl_upload_layout.setVisibility(View.GONE);
                try {
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent, "Select Picture"), TAKE_PIC_FROM_GALLERY);
                } catch (Exception e) {

                }
            }
        });
        tv_take_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rl_upload_layout.setVisibility(View.GONE);
                Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, videoUri);
                startActivityForResult(intent, TAKE_VIDEO_FROM_CAMERA);
            }
        });
        tv_choose_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rl_upload_layout.setVisibility(View.GONE);
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("video/*");
                startActivityForResult(Intent.createChooser(intent, "Select Video"), TAKE_VIDEO_FROM_GALLERY);
            }
        });
        cancle_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                rl_upload_layout.setVisibility(View.GONE);
            }
        });
        image_textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                imageDialog(mArrayUri);
            }
        });
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        tv_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectCategory();
            }
        });
    }

    private void idInitialize() {
        tv_post_btn = findViewById(R.id.post_tv);
        tv_post_btn.setOnClickListener(this);
        root = findViewById(R.id.root);
        rl_laout = findViewById(R.id.rl_laout);
        img_back = findViewById(R.id.img_back);
        rl_laout = findViewById(R.id.rl_laout);
        tv_post = findViewById(R.id.business_category);
        add_imageview1 = findViewById(R.id.add_imageview1);
        rl_upload_layout = findViewById(R.id.upload_options_rl);
        tv_choose_photo = findViewById(R.id.choose_photo);
        tv_take_photo = findViewById(R.id.take_photo);
        image_count_textview = findViewById(R.id.image_count_textview);
        tv_choose_video = findViewById(R.id.choose_video);
        tv_take_video = findViewById(R.id.take_video);
        image_textview = findViewById(R.id.image_textview);
        doc_textview = findViewById(R.id.doc_textview);
        cancle_tv = findViewById(R.id.cancle_tv);
        photo_recycler_view = findViewById(R.id.photos_recycler_view);
        edt = findViewById(R.id.edt_mind);
        frm_layout = findViewById(R.id.frm_layout);
    }

    private RequestBody createPartFromString(String data) {
        return RequestBody.create(MediaType.parse("text/plain"), data);
    }

    private void postTypeApi() {
        APIInterface service = APIClient.getRetrofit().create(APIInterface.class);
        Call<StatePojo> call = service.postTpypeList("posttype");
        call.enqueue(new Callback<StatePojo>() {
            @Override
            public void onResponse(Call<StatePojo> call, Response<StatePojo> response) {
                categorylist = response.body().getNbdata();
                totalImageCount = Integer.parseInt(response.body().getPost_img_limit());
                totalVideoCount = Integer.parseInt(response.body().getPost_video_limit());
                postVideoSize = Integer.parseInt(response.body().getPost_video_size());
                itemCount = totalImageCount + totalVideoCount;
                image_count_textview.setText("Max : " + totalImageCount + " Images " + totalVideoCount + " Video");
            }

            @Override
            public void onFailure(Call<StatePojo> call, Throwable t) {
                Toast.makeText(CreatePostActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("res---", t.getMessage());
            }
        });
    }
    private void selectCategory() {
        RecyclerView rv;
        ImageView cancel;
        mail_dialog = new Dialog(this);
        mail_dialog.setContentView(R.layout.open_create_post_list);
        rv = mail_dialog.findViewById(R.id.rv_category);
        cancel = mail_dialog.findViewById(R.id.cross_imageview);
        rv.setLayoutManager(new LinearLayoutManager(this));
        SelectPostTypeAdapter emailListAdapter = new SelectPostTypeAdapter(categorylist, mail_dialog, CreatePostActivity.this);
        rv.setAdapter(emailListAdapter);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mail_dialog.cancel();
            }
        });
        mail_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(CreatePostActivity.this, android.R.color.transparent)));
        mail_dialog.setCancelable(true);
        mail_dialog.show();
    }
    public File bitmapToFile(Bitmap bitmap, String fileNameToSave) { // File name like "image.png"
        File file = null;
        try {
            file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + File.separator + fileNameToSave);
            file.createNewFile();
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 0, bos); // YOU can also save it in JPEG
            byte[] bitmapdata = bos.toByteArray();
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(bitmapdata);
            fos.flush();
            fos.close();
            return file;
        } catch (Exception e) {
            e.printStackTrace();
            return file;
        }
    }
    @Override
    protected void onActivityResult(int req, int result, Intent data) {
        super.onActivityResult(req, result, data);
        if (result == RESULT_OK) {
            if (req == TAKE_PIC_FROM_GALLERY) {
                handleImageResult(data, false);
            } else if (req == TAKE_PIC_FROM_CAMERA) {
                handleImageResult(data, true);
            } else if (req == TAKE_VIDEO_FROM_GALLERY || req == TAKE_VIDEO_FROM_CAMERA) {
                handleVideoResult(data);
            } else if (req == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                handleCropResult(data);
            }
        }
    }

    private void handleImageResult(Intent data, boolean isCamera) {
        // Check if total image count is exceeded
        if (imageUris.size() >= totalImageCount) {
            Toast.makeText(this, "You can only add up to " + totalImageCount + " images", Toast.LENGTH_LONG).show();
            return;
        }

        Uri imageUri;

        try {
            if (isCamera) {
                // Camera case: Use currentPhotoPath
                if (currentPhotoPath != null) {
                    imageUri = Uri.fromFile(new File(currentPhotoPath));
                    performCrop(imageUri);
                } else {
                    Toast.makeText(this, "Error capturing image. Please try again.", Toast.LENGTH_SHORT).show();
                    // Automatically reopen the camera
                    reopenCamera();
                }
            } else if (data != null) {
                // Gallery case with multiple selections
                if (data.getClipData() != null) {
                    ClipData mClipData = data.getClipData();
                    int count = Math.min(mClipData.getItemCount(), itemCount - (imageUris.size() + videoUris.size()));
                    for (int i = 0; i < count; i++) {
                        imageUri = mClipData.getItemAt(i).getUri();
                        if (imageUri != null) {
                            performCrop(imageUri);
                            if (imageUris.size() + videoUris.size() >= itemCount) break;
                        }
                    }
                } else {
                    // Gallery case with single selection
                    imageUri = data.getData();
                    if (imageUri != null) {
                        performCrop(imageUri);
                    } else {
                        Toast.makeText(this, "Error selecting image. Please try again.", Toast.LENGTH_SHORT).show();
                    }
                }
            } else {
                // No data received
                Toast.makeText(this, "No image selected. Please try again.", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            // Catch any unexpected exceptions
            e.printStackTrace();
            Toast.makeText(this, "An error occurred while handling the image.", Toast.LENGTH_SHORT).show();
        }
    }

    // Method to reopen the camera
    private void reopenCamera() {
        String fileName = "photo";
        File storageDirectory = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        try {
            imageFile = File.createTempFile(fileName, ".jpg", storageDirectory);
            currentPhotoPath = imageFile.getAbsolutePath();
            imageUri = FileProvider.getUriForFile(CreatePostActivity.this, "com.app_neighbrsnook.fileprovider", imageFile);

            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            startActivityForResult(intent, TAKE_PIC_FROM_CAMERA);
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Failed to reopen camera. Please try manually.", Toast.LENGTH_SHORT).show();
        }
    }

    private void performCrop(Uri fileUri) {
        CropImage.activity(fileUri)
                .setCropMenuCropButtonTitle("Set Image")
                .setAspectRatio(1, 1)
                .setFixAspectRatio(false)
                .start(this);
    }

    private void handleCropResult(Intent data) {
        CropImage.ActivityResult result = CropImage.getActivityResult(data);
        if (result != null && result.getUri() != null) {
            Uri croppedUri = result.getUri();
            imageUris.add(croppedUri);
            mArrayUri.add(croppedUri);

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), croppedUri);
                mArrayGalleryPhoto.add(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }

            updateImageTextView();
        } else {
            Toast.makeText(this, "Crop failed", Toast.LENGTH_SHORT).show();
        }
    }

    private void handleVideoResult(Intent data) {
        if (data == null || data.getData() == null) {
            Toast.makeText(this, "Failed to get video. Please try again.", Toast.LENGTH_LONG).show();
            return;
        }
        if (videoUris.size() >= totalVideoCount) {
            Toast.makeText(this, "You can only add up to " + totalVideoCount + " video", Toast.LENGTH_LONG).show();
            return;
        }
        Uri videoUri = data.getData();
        String videoPath = getPath(this, videoUri);
        if (videoPath == null) {
            Toast.makeText(this, "Unable to get the video path", Toast.LENGTH_SHORT).show();
            return;
        }
        long fileSize = new File(videoPath).length();
        if (fileSize > postVideoSize * 1024 * 1024) {
            Toast.makeText(this, "Video size exceeds " + postVideoSize + "MB limit", Toast.LENGTH_LONG).show();
            return;
        }
        compressVideo(videoUri);
    }
    private void compressVideo(Uri videoUri) {
        File movieDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES);
        if (!movieDirectory.exists()) {
            movieDirectory.mkdirs();
        }

        File compressedVideoFile = new File(movieDirectory, "compressed_video_" + System.currentTimeMillis() + ".mp4");
        String outputPath = compressedVideoFile.getAbsolutePath();

        videoFilePaths.add(outputPath);
        progressDialog.show();

        // Execute compression in background thread
        new Thread(() -> {
            try {
                boolean success = compressVideoWithMediaCodec(videoUri, outputPath);

                runOnUiThread(() -> {
                    progressDialog.dismiss();
                    if (success) {
                        Uri compressedUri = Uri.fromFile(compressedVideoFile);
                        videoUris.add(compressedUri);
                        if (mediaViewAdapter != null) {
                            mediaViewAdapter.notifyDataSetChanged();
                        }
                        updateVideoTextView();
                        Toast.makeText(this, "Video compressed successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Video compression failed", Toast.LENGTH_SHORT).show();
                    }
                });
            } catch (Exception e) {
                runOnUiThread(() -> {
                    progressDialog.dismiss();
                    Toast.makeText(this, "Video compression error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
            }
        }).start();
    }

    private boolean compressVideoWithMediaCodec(Uri videoUri, String outputPath) {
        MediaExtractor extractor = null;
        MediaMuxer muxer = null;

        try {
            extractor = new MediaExtractor();
            extractor.setDataSource(this, videoUri, null);

            muxer = new MediaMuxer(outputPath, MediaMuxer.OutputFormat.MUXER_OUTPUT_MPEG_4);

            int videoTrackIndex = -1;
            int audioTrackIndex = -1;
            int videoTrackId = -1;
            int audioTrackId = -1;

            // Find video and audio tracks
            for (int i = 0; i < extractor.getTrackCount(); i++) {
                MediaFormat format = extractor.getTrackFormat(i);
                String mime = format.getString(MediaFormat.KEY_MIME);

                if (mime.startsWith("video/")) {
                    // Configure video compression
                    format.setInteger(MediaFormat.KEY_BIT_RATE, 1500000); // 1.5 Mbps
                    format.setInteger(MediaFormat.KEY_FRAME_RATE, 30);
                    format.setInteger(MediaFormat.KEY_I_FRAME_INTERVAL, 2);

                    videoTrackIndex = i;
                    videoTrackId = muxer.addTrack(format);
                } else if (mime.startsWith("audio/")) {
                    // Configure audio compression
                    format.setInteger(MediaFormat.KEY_BIT_RATE, 96000); // 96 kbps
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
                    bufferInfo.flags = MediaCodec.BUFFER_FLAG_SYNC_FRAME;
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
                    bufferInfo.flags = MediaCodec.BUFFER_FLAG_SYNC_FRAME;
                    bufferInfo.presentationTimeUs = extractor.getSampleTime();

                    muxer.writeSampleData(audioTrackId, buffer, bufferInfo);
                    extractor.advance();
                }
            }

            return true;

        } catch (Exception e) {
            e.printStackTrace();
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
                    e.printStackTrace();
                }
            }
        }
    }

/*    private void compressVideo(Uri videoUri) {
        File movieDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES);
        if (!movieDirectory.exists()) {
            movieDirectory.mkdirs();
        }

        File compressedVideoFile = new File(movieDirectory, "compressed_video_" + System.currentTimeMillis() + ".mp4");
        String inputPath = getPath(this, videoUri);
        String outputPath = compressedVideoFile.getAbsolutePath();

        videoFilePaths.add(outputPath);

        String[] cmd = {"-y", "-i", inputPath, "-vcodec", "libx264", "-crf", "28", "-preset", "fast", "-acodec", "aac", "-b:a", "192k", "-ac", "2", outputPath};

        progressDialog.show();

        FFmpeg.executeAsync(cmd, (executionId, returnCode) -> {
            if (returnCode == Config.RETURN_CODE_SUCCESS) {
                // Compression was successful
                progressDialog.dismiss();
                Uri compressedUri = Uri.fromFile(compressedVideoFile);
                videoUris.add(compressedUri);
                if (mediaViewAdapter != null) {
                    mediaViewAdapter.notifyDataSetChanged();
                }
                updateVideoTextView();
            } else {
                progressDialog.dismiss();
                Toast.makeText(CreatePostActivity.this, "Video compression failed", Toast.LENGTH_SHORT).show();
            }
        });
    }*/

    private void updateCombinedUriList() {
        mArrayUri.clear();
        mArrayUri.addAll(imageUris);
        mArrayUri.addAll(videoUris);
    }

    private void updateImageTextView() {
        updateCombinedUriList();
        if (mArrayUri.size() > 0) {
            image_textview.setVisibility(View.VISIBLE);
            image_textview.setText(mArrayUri.size() + " preview");
        } else {
            image_textview.setVisibility(View.GONE);
        }
    }

    private void updateVideoTextView() {
        updateCombinedUriList();
        if (mArrayUri.size() > 0) {
            image_textview.setVisibility(View.VISIBLE);
            image_textview.setText(mArrayUri.size() + " preview");
        } else {
            image_textview.setVisibility(View.GONE);
        }
    }

    private void postApi() {
        List<MultipartBody.Part> list = new ArrayList<>();
        File file;
        int compressionRatio = 20;
        MultipartBody.Part userMedia = null;

        for (Bitmap bitmap : mArrayGalleryPhoto) {
            file = bitmapToFile(bitmap, getString(R.string.app_name) + System.currentTimeMillis() + ".jpg");
            Log.d("mArrayGalleryPhoto...", file.toString());
            try {
                Bitmap bitmapDecoded = BitmapFactory.decodeFile(file.getPath());
                bitmapDecoded.compress(Bitmap.CompressFormat.JPEG, compressionRatio, new FileOutputStream(file));
            } catch (Throwable t) {
                Log.e("ERROR", "Error compressing file." + t);
                t.printStackTrace();
            }

            RequestBody pic = RequestBody.create(MediaType.parse("image/jpeg"), file);
            userMedia = MultipartBody.Part.createFormData("photo[]", file.getName(), pic);
            list.add(userMedia);
            Log.d("post_list", list.toString());
        }

        for (Uri videoUri : videoUris) {
            file = uriToFile(videoUri);
            Log.d("mArrayVideo...", file.toString());

            RequestBody video = RequestBody.create(MediaType.parse("video/mp4"), file);
            userMedia = MultipartBody.Part.createFormData("photo[]", file.getName(), video);
            list.add(userMedia);
            Log.d("post_list", list.toString());
        }

        APIInterface service = APIClient.getRetrofit().create(APIInterface.class);

        Call<PostPojo> call = service.uploadData("createpost",
                createPartFromString(sm.getString("user_id")),
                createPartFromString(String.valueOf(categoryid)),
                createPartFromString(edt.getText().toString()), list);
        call.enqueue(new Callback<PostPojo>() {
            @Override
            public void onResponse(Call<PostPojo> call, Response<PostPojo> response) {
                if (response.body().getStatus().equals("success")) {
                    apiStatus = "success";
                    mailSendApi();

                    for (String filePath : videoFilePaths) {
                        File file = new File(filePath);
                        if (file.exists()) {
                            file.delete();
                        }
                    }
                    videoFilePaths.clear();
                }
                Log.d("responseApi", response.toString());
            }

            @Override
            public void onFailure(Call<PostPojo> call, Throwable t) {
                Toast.makeText(CreatePostActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("response", t.getMessage());
            }
        });
    }

    private String getPath(Context context, Uri uri) {
        // DocumentProvider
        if (DocumentsContract.isDocumentUri(context, uri)) {
            if (isExternalStorageDocument(uri)) {
                String docId = DocumentsContract.getDocumentId(uri);
                String[] split = docId.split(":");
                String type = split[0];
                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            } else if (isDownloadsDocument(uri)) {
                String id = DocumentsContract.getDocumentId(uri);
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
                return getDataColumn(context, contentUri, null, null);
            } else if (isMediaDocument(uri)) {
                String docId = DocumentsContract.getDocumentId(uri);
                String[] split = docId.split(":");
                String type = split[0];
                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                String selection = "_id=?";
                String[] selectionArgs = new String[]{split[1]};
                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore and general
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }

    private String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        String[] projection = {MediaStore.MediaColumns.DATA};
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                int index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    private boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    private boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    private boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    private File uriToFile(Uri uri) {
        File file = new File(getCacheDir(), getFileName(uri));
        try (InputStream inputStream = getContentResolver().openInputStream(uri);
             FileOutputStream outputStream = new FileOutputStream(file)) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    @SuppressLint("Range")
    private String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }

    @Override
    public void removeImage(int position) {
        bitmapList.remove(position);
        mediaViewAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(String categoryName, String id) {

    }

    @Override
    public void onClickPostType(int id, String categoryName) {
        tv_post.setText(categoryName);
        this.categoryid = id;
        Log.d("post_list", String.valueOf(id));
        mail_dialog.cancel();
    }

    private void imageDialog(ArrayList<Uri> mArrayUri) {
        RecyclerView rv;
        ImageView cancel;
        CardView cv_dialog_exit;
        image_dialog = new Dialog(this, R.style.FullscreenDialog);
        image_dialog.setContentView(R.layout.open_image_dialog);
        rv = image_dialog.findViewById(R.id.rv_image);
        cancel = image_dialog.findViewById(R.id.cross_imageview);
        cv_dialog_exit = image_dialog.findViewById(R.id.cv_dialog_exit);

        rv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        mediaViewAdapter = new MediaViewAdapter(this, image_dialog, mArrayUri, CreatePostActivity.this);
        rv.setAdapter(mediaViewAdapter);
        LinearSnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(rv);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                image_dialog.cancel();
            }
        });

        cv_dialog_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                image_dialog.cancel();
            }
        });

        image_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(this, android.R.color.black)));
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(image_dialog.getWindow().getAttributes());
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.gravity = Gravity.CENTER;
        image_dialog.getWindow().setAttributes(layoutParams);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            image_dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            image_dialog.getWindow().setStatusBarColor(ContextCompat.getColor(this, android.R.color.black));
        }
        if (getSupportActionBar() != null) {
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(this, android.R.color.black)));
        }
        image_dialog.setCancelable(true);
        image_dialog.show();
    }

    @Override
    public void onImageClick(int pos) {

    }
    @Override
    public void onCrossClick(int pos, String from) {
        mArrayUri.remove(pos);
        mArrayGalleryPhoto.remove(pos);
        imageUploadAdapter.notifyDataSetChanged();

        if (mArrayUri.size() == 1) {
            image_textview.setVisibility(View.VISIBLE);
            image_textview.setText(mArrayUri.size() + " preview");
        }
        if (mArrayUri.size() >= 1) {
            image_textview.setVisibility(View.VISIBLE);
            image_textview.setText(mArrayUri.size() + " preview");
        }
        if (mArrayUri.size() == 0) {
            image_dialog.dismiss();
            image_textview.setVisibility(View.GONE);
        }
    }
    @Override
    public void onCrossClick1(int pos, String from) {

    }
    @Override
    public void onClick(View view) {
        if (checkAllFields()) {
            progressDialog.show();
            new SimulateTask().execute();
        }
    }
    @Override
    public void onItemClick(Uri uri) {

    }
    @Override
    public void onRemoveClick(int position, String type) {
        if (type.equals("Video")) {
            videoUris.remove(0);
        } else {
            imageUris.remove(position);
            mArrayGalleryPhoto.remove(position);

        }
        mArrayUri.remove(position);
        if (mediaViewAdapter != null) {
            mediaViewAdapter.notifyDataSetChanged();
        }
        updateMediaCountText();
    }

    private void updateMediaCountText() {
        if (mArrayUri.size() > 0) {
            image_textview.setVisibility(View.VISIBLE);
            image_textview.setText(mArrayUri.size() + " preview");
        } else {
            image_textview.setVisibility(View.GONE);
            image_dialog.cancel();
        }
    }

    public class SimulateTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            if (checkAllFields()) {
                postApi();
            }
            SystemClock.sleep(5000);
            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid) {
            if (apiStatus.equals("success")) {
                Intent i = new Intent(CreatePostActivity.this, MainActivity.class);
                startActivity(i);
                finish();
            }
            progressDialog.dismiss();
        }
    }

    public void mailSendApi() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UrlClass.MAIL_API, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Log.e("dfadfkjkkjkjksa", response);
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    status.equals("success");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("fasdfafsd", error.toString());
                Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        Volley.newRequestQueue(context).add(stringRequest);
    }

    public boolean checkAllFields() {
        // Get text values
        String postCategory = tv_post.getText().toString().trim();
        String message = edt.getText().toString().trim();
        // Check if post category is selected
        if (postCategory.isEmpty() || postCategory.equalsIgnoreCase("Type") ||
                postCategory.equalsIgnoreCase("Select Category")) {
            Toast.makeText(CreatePostActivity.this, "Please select post category", Toast.LENGTH_SHORT).show();
            return false;
        }
        // Check if message is empty
        if (message.isEmpty()) {
            return GlobalMethods.setError(edt, "Please enter your message");
        }
        // Check for bad words
        if (BadWordFilter.containsBadWord(message)) {
            GlobalMethods.getInstance(CreatePostActivity.this)
                    .globalDialogAbusiveWord(CreatePostActivity.this, getString(R.string.abusive_msg));
            return false;
        }
        return true;
    }



  /*  @Override
    public void onBackPressed() {
      //  super.onBackPressed();
       // startActivity(new Intent(CreatePostActivity.this, MainActivity.class));
    }*/


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 200) {
            boolean allGranted = true;

            for (int result : grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    allGranted = false;
                    break;
                }
            }

            if (allGranted) {
                // All permissions granted
                Toast.makeText(this, "Permissions granted!", Toast.LENGTH_SHORT).show();
                rl_upload_layout.setVisibility(View.VISIBLE); // Perform desired action
            } else {
                // Check if "Don't Ask Again" is selected
                boolean shouldShowRationale = false;
                for (String permission : permissions) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
                        shouldShowRationale = true;
                        break;
                    }
                }

                if (shouldShowRationale) {
                    // User denied permissions without "Don't Ask Again"
                    Toast.makeText(this, "Permissions are required for this feature.", Toast.LENGTH_SHORT).show();
                } else {
                    // User selected "Don't Ask Again"
                    showSettingsDialog();
                }
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

// Show Settings Dialog for "Don't Ask Again"
    private void showSettingsDialog() {
        String message;

        // Customize message based on Android version
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            message = "Camera permission is required for this feature. Please allow it from Settings.";
        } else {
            message = "Camera and storage permissions are required for this feature. Please allow them from Settings.";
        }

        new AlertDialog.Builder(this)
                .setTitle("Permissions Required")
                .setMessage(message) // Set the custom message
                .setPositiveButton("Go to Settings", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", getPackageName(), null);
                        intent.setData(uri);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .create()
                .show();
    }

}


