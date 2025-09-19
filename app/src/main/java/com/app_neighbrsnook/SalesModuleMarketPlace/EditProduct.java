package com.app_neighbrsnook.SalesModuleMarketPlace;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ContentUris;
import android.content.ContentValues;
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
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.OrientationHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.app_neighbrsnook.R;
import com.app_neighbrsnook.adapter.ImageUploadAdapter;
import com.app_neighbrsnook.adapter.ImageUploadAdapter1;
import com.app_neighbrsnook.adapter.MarketPlaceCategoryAdapter;
import com.app_neighbrsnook.adapter.MarketPlaceDetailImgAdapter;
import com.app_neighbrsnook.adapter.MediaSliderAdapter;
import com.app_neighbrsnook.adapter.MediaViewAdapter;
import com.app_neighbrsnook.adapter.WallChildAdapter;
import com.app_neighbrsnook.apiService.ApiExecutor;
import com.app_neighbrsnook.login.AddressResponse;
import com.app_neighbrsnook.model.ImagePOJO;
import com.app_neighbrsnook.network.APIClient;
import com.app_neighbrsnook.network.APIInterface;
import com.app_neighbrsnook.pojo.ImagePojo;
import com.app_neighbrsnook.pojo.Nbdatum;
import com.app_neighbrsnook.pojo.StatePojo;
import com.app_neighbrsnook.pojo.marketPlacePojo.CommonPojoSuccess;
import com.app_neighbrsnook.pojo.marketPlacePojo.DetailsPojoChild;
import com.app_neighbrsnook.pojo.marketPlacePojo.DetailsPojoMarketPlaceParent;
import com.app_neighbrsnook.utils.GlobalMethods;
import com.app_neighbrsnook.utils.SharedPrefsManager;

import com.shashank.sony.fancytoastlib.FancyToast;
import com.app_neighbrsnook.libraries.cropper.CropImage;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditProduct extends AppCompatActivity implements MarketPlaceCategoryAdapter.CategoryInterface, View.OnClickListener, MarketPlaceDetailImgAdapter.ImgRequest, ImageUploadAdapter1.ImageItemClick, ImageUploadAdapter.ImageRequest, MediaViewAdapter.OnItemClickListener,WallChildAdapter.ImageCallBack  {
    TextView item_category, image_textview, titleTv, cancle_tv, tv_take_photo, tv_choose_photo, tv_take_video, tv_choose_video, paid_item_tv, free_item_tv, negotiable_item_tv, fix_item_tv;
    EditText item_name, item_price, item_desc, item_brand, item_quantity, totalAmount;
    Dialog mail_dialog, preview_dialog;
    String edit;
    String categoriId = "";
    SharedPrefsManager sm;
    CommonPojoSuccess wishlistResponse;
    String stProductName,category, stItemPrice, stQuantity, stType, stNeighbrhoodName, stAboutUs, stItemPostedBy, stCreateTime, stBrandName,stSoldStatus, stCategory;

    File imageFile;
    RecyclerView rvMarketPlaceDetails;
    ArrayList<ImagePojo> ar1 = new ArrayList<>();
    String[] days = {"Architecture & Interiors", "Automobile", "Bakery", "Day Care", "Education",
            "Entertainment and Media", "Fruits & Vegetables", "Garments", "Entertainment and Media",
            "Fruits & Vegetables", "Garments", "Entertainment and Media", "Fruits & Vegetables", "Garments",
            "Entertainment and Media", "Fruits & Vegetables", "Garments", "Entertainment and Media",
            "Fruits & Vegetables", "Garments"};
    List<String> emailsList = new ArrayList<>();
    ImageView back_btn, add_imageview;
    ArrayList<Uri> mArrayUri = new ArrayList();
    List<Nbdatum> categorylist = new ArrayList<>();

    MarketPlaceCategoryAdapter emailListAdapter;
    Uri imageUri;
    ImageUploadAdapter imageUploadAdapter;

    String currentPhotoPath;
    Dialog image_dialog;
    RelativeLayout upload_options_rl, video_or_image_upload_rl;
    RelativeLayout rl_upload;
    LinearLayout price_ll;
    ArrayList<ImagePOJO> bitmapList = new ArrayList<>();
    String CLICK_ON = "";
    String FIRST_IMAGE = "FirstImage";
    FrameLayout preview_textview;
    ImageUploadAdapter1 uploadAdapter;
    String price_status = "Sell";
    ArrayList<Bitmap> mArrayGalleryPhoto = new ArrayList<>();
    Context context;
    Activity activity;
    RecyclerView.LayoutManager layoutManager;
    ImageView imgsearch_imageview, imgAddImage;
    CheckBox chSoldMarkSold;
    int id;
    private final int TAKE_VIDEO_FROM_GALLARY = 1;
    private final int TAKE_VIDEO_FROM_CAMERA = 2;
    private final int TAKE_PIC_FROM_GALLARY = 3;
    private final int TAKE_PIC_FROM_CAMERA = 4;
    ProgressDialog progressDialog;
    ArrayList<Uri> imageUris = new ArrayList<>();
    ArrayList<Uri> videoUris = new ArrayList<>();
    int totalImageCount, totalVideoCount, itemCount, postVideoSize;
    private ArrayList<String> videoFilePaths = new ArrayList<>();
    MediaViewAdapter mediaViewAdapter;
    WallChildAdapter.ImageCallBack imageCallBack = this;
    LinearLayout lnrMarkAsSold;
    CheckBox tvDeactivate,tvActiveItem;
    LinearLayout lnrDeactivate,lnrActivate;
    private String lastClicked = ""; // "deactivate" या "activate"

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = activity = this;
        setContentView(R.layout.activity_edit_product);
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
        sm = new SharedPrefsManager(this);
        Intent intent = getIntent();
        id = intent.getIntExtra("id", 0);
        init();
        businessDetailApi(id);
        categoryApi();
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rvMarketPlaceDetails.setLayoutManager(layoutManager);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_btn:
                onBackPressed();
                break;

            case R.id.item_category:
                selectCategory();
                break;

            case R.id.title:
                onBackPressed();
                break;

            case R.id.video_or_image_upload_rl:
                if ( GlobalMethods.checkCameraAndGalleryPermission(EditProduct.this)) {
                    if (bitmapList.size() < 100) {
                        CLICK_ON = FIRST_IMAGE;
                        upload_options_rl.setVisibility(View.VISIBLE);
                    }
                }
                break;

            case R.id.take_photo:
                upload_options_rl.setVisibility(View.GONE);
                takePhotoFromCamera();
                break;

            case R.id.choose_photo:
                upload_options_rl.setVisibility(View.GONE);
                takePhotoFromGallery();
                break;

            case R.id.make_video:
                upload_options_rl.setVisibility(View.GONE);
                takeVideoFromCamera();
                break;

            case R.id.choose_video:
                upload_options_rl.setVisibility(View.GONE);
                takeVideoFromGallary();
                break;

            case R.id.preview_textview:
                if (CheckAllFields()) {
                    // यदि सभी फील्ड्स वैध हैं
                    editItem();
                } else {
                    // यदि कोई भी फील्ड अपडेट नहीं हुई है
                    productInactiveApi();
                }
                break;

            case R.id.free_item_tv:
                price_status = "Donate";
                resetColor();
                price_ll.setVisibility(View.GONE);
                free_item_tv.setTextColor(ContextCompat.getColor(this, R.color.white));
                free_item_tv.setBackground(ContextCompat.getDrawable(this, R.drawable.round_corner_dialog_themcolor));
                item_price.setText("0");
                break;

            case R.id.paid_item_tv:
                price_status = "Sell";
                resetColor();
                paid_item_tv.setTextColor(ContextCompat.getColor(this, R.color.white));
                paid_item_tv.setBackground(ContextCompat.getDrawable(this, R.drawable.round_corner_dialog_themcolor));
                price_ll.setVisibility(View.VISIBLE);
                break;


            case R.id.negotiable_item_tv:
                resetColor1();
                negotiable_item_tv.setTextColor(getResources().getColor(R.color.white));
                negotiable_item_tv.setBackground(getResources().getDrawable(R.drawable.round_corner_dialog_themcolor));
                break;

            case R.id.fix_item_tv:
                resetColor1();
                fix_item_tv.setTextColor(getResources().getColor(R.color.white));
                fix_item_tv.setBackground(getResources().getDrawable(R.drawable.round_corner_dialog_themcolor));
                break;
        }

    }

    private void takePhotoFromGallery() {
        //Shubham Upadte
        try {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), TAKE_PIC_FROM_GALLARY);
        } catch (Exception e) { }
    }

    private void takePhotoFromCamera() {
        //Shubham Upadte
        String fileName = "photo";
        File storageDirectory = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        try {
            imageFile = File.createTempFile(fileName, "jpg", storageDirectory);
            currentPhotoPath = imageFile.getAbsolutePath();
            imageUri = FileProvider.getUriForFile(EditProduct.this, "com.app_neighbrsnook.fileprovider", imageFile);
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            startActivityForResult(intent, TAKE_PIC_FROM_CAMERA);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void takeVideoFromGallary() {
        //Shubham Upadte
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("video/*");
        startActivityForResult(Intent.createChooser(intent, "Select Video"), TAKE_VIDEO_FROM_GALLARY);
    }

    private void takeVideoFromCamera() {
        //Shubham Upadte
        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        startActivityForResult(intent, TAKE_VIDEO_FROM_CAMERA);
    }

    @Override
    protected void onActivityResult(int req, int result, Intent data) {
        super.onActivityResult(req, result, data);
        //Shubham Upadte
        if (result == RESULT_OK) {
            if (req == TAKE_PIC_FROM_GALLARY) {
                handleImageResult(data, false);
            } else if (req == TAKE_PIC_FROM_CAMERA) {
                handleImageResult(data, true);
            } else if (req == TAKE_VIDEO_FROM_GALLARY || req == TAKE_VIDEO_FROM_CAMERA) {
                handleVideoResult(data);
            } else if (req == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                handleCropResult(data);
            } else if (req == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                onBackPressed();
            }
        }
    }

    private void handleImageResult(Intent data, boolean isCamera) {
        //Shubham Upadte
        if (imageUris.size() >= totalImageCount) {
            Toast.makeText(this, "You can only add up to " + totalImageCount + " images", Toast.LENGTH_LONG).show();
            return;
        }

        if (isCamera) {
            // If the image is captured from the camera
            Uri imageUri = Uri.fromFile(new File(currentPhotoPath));
            performCrop(imageUri);
        } else if (data != null && data.getClipData() != null) {
            // If multiple images are selected from the gallery
            ClipData clipData = data.getClipData();
            int maxImageCountToAdd = Math.min(clipData.getItemCount(), totalImageCount - imageUris.size());

            for (int i = 0; i < maxImageCountToAdd; i++) {
                Uri imageUri = clipData.getItemAt(i).getUri();
                performCrop(imageUri);

                // Break if the maximum image limit is reached
                if (imageUris.size() + videoUris.size() >= totalImageCount) break;
            }
        } else if (data != null && data.getData() != null) {
            // If a single image is selected from the gallery
            Uri imageUri = data.getData();
            performCrop(imageUri);
        }
    }

    private void handleVideoResult(Intent data) {
        //Shubham Upadte
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

    private void handleCropResult(Intent data) {
        //Shubham Upadte
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

    private void performCrop(Uri fileUri) {
        //Shubham Upadte
        CropImage.activity(fileUri)
                .setCropMenuCropButtonTitle("Set Image")
                .setAspectRatio(1, 1)
                .setFixAspectRatio(false)
                .start(this);
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

   /* private void compressVideo(Uri videoUri) {
        //Shubham Upadte
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
                Toast.makeText(EditProduct.this, "Video compression failed", Toast.LENGTH_SHORT).show();
            }
        });
    }*/

    private void updateCombinedUriList() {
        //Shubham Upadte
        mArrayUri.clear();
        mArrayUri.addAll(imageUris);
        mArrayUri.addAll(videoUris);
    }

    private void updateImageTextView() {
        //Shubham Upadte
        updateCombinedUriList();
        if (mArrayUri.size() > 0) {
            image_textview.setVisibility(View.VISIBLE);
            image_textview.setText(mArrayUri.size() + " preview");
        } else {
            image_textview.setVisibility(View.GONE);
        }
    }

    private void updateVideoTextView() {
        //Shubham Upadte
        updateCombinedUriList();
        if (mArrayUri.size() > 0) {
            image_textview.setVisibility(View.VISIBLE);
            image_textview.setText(mArrayUri.size() + " preview");
        } else {
            image_textview.setVisibility(View.GONE);
        }
    }

    private String getPath(Context context, Uri uri) {
        //Shubham Upadte
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
        //Shubham Upadte
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
        //Shubham Upadte
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
        //Shubham Upadte
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
    public void onItemClick(Uri uri) {

    }

    @Override
    public void onRemoveClick(int position, String type) {
        //Shubham Upadte
        if(type.equals("Video")){
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
        //Shubham Upadte
        if (mArrayUri.size() > 0) {
            image_textview.setVisibility(View.VISIBLE);
            image_textview.setText(mArrayUri.size() + " preview");
        } else {
            image_textview.setVisibility(View.GONE);
            image_dialog.cancel();
        }
    }

    private void imageDialog(ArrayList<Uri> mArrayUri) {
        //Shubham Upadte
        RecyclerView rv;
        ImageView cancel;
        image_dialog = new Dialog(this);
        image_dialog.setContentView(R.layout.open_image_dialog);
        rv = image_dialog.findViewById(R.id.rv_image);
        cancel = image_dialog.findViewById(R.id.cross_imageview);
        rv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        mediaViewAdapter = new MediaViewAdapter(this, image_dialog, mArrayUri, EditProduct.this);
        rv.setAdapter(mediaViewAdapter);
        LinearSnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(rv);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                image_dialog.dismiss();
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

    private void imageDialog(List<ImagePojo> media, int pos) {
        //Shubham Upadte
        RecyclerView rv;
        ImageView cancel;
        image_dialog = new Dialog(this);
        image_dialog.setContentView(R.layout.open_image_dialog);
        rv = image_dialog.findViewById(R.id.rv_image);
        cancel = image_dialog.findViewById(R.id.cross_imageview);
        rv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        MediaSliderAdapter mediaSliderAdapter = new MediaSliderAdapter(image_dialog, media, "detail", pos, EditProduct.this);
        rv.setAdapter(mediaSliderAdapter);
        rv.scrollToPosition(pos);
        LinearSnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(rv);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                image_dialog.dismiss();
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
    private void editItem() {
        progressDialog.show();
        ArrayList<MultipartBody.Part> mediaList = new ArrayList<>();
        File mediaFile;
        int compressionRatio = 20;
        MultipartBody.Part userMedia;

        // Handle images
        for (Bitmap bitmap : mArrayGalleryPhoto) {
            mediaFile = bitmapToFile(bitmap, getString(R.string.app_name) + System.currentTimeMillis() + ".jpg");
            Log.d("mArrayGalleryPhoto...", mediaFile.toString());

            try {
                Bitmap bitmapDecoded = BitmapFactory.decodeFile(mediaFile.getPath());
                bitmapDecoded.compress(Bitmap.CompressFormat.JPEG, compressionRatio, new FileOutputStream(mediaFile));
            } catch (Throwable t) {
                Log.e("ERROR", "Error compressing file." + t);
                t.printStackTrace();
            }

            RequestBody image = RequestBody.create(MediaType.parse("image/jpeg"), mediaFile);
            userMedia = MultipartBody.Part.createFormData("p_images[]", mediaFile.getName(), image);
            mediaList.add(userMedia);
            Log.d("post_list", mediaList.toString());
        }

        // Handle videos
        for (Uri videoUri : videoUris) {
            mediaFile = uriToFile(videoUri);
            Log.d("mArrayVideo...", mediaFile.toString());

            RequestBody video = RequestBody.create(MediaType.parse("video/mp4"), mediaFile);
            userMedia = MultipartBody.Part.createFormData("p_images[]", mediaFile.getName(), video);
            mediaList.add(userMedia);
            Log.d("post_list", mediaList.toString());
        }

        // Prepare request body map
        HashMap<String, RequestBody> hashMap = new HashMap<>();
        hashMap.put("p_title", RequestBody.create(MultipartBody.FORM, item_name.getText().toString()));
        hashMap.put("p_description", RequestBody.create(MultipartBody.FORM, item_desc.getText().toString()));
        hashMap.put("sale_type", RequestBody.create(MultipartBody.FORM, price_status != null ? price_status : "Sell"));
        hashMap.put("sale_price", RequestBody.create(MultipartBody.FORM, item_price.getText().toString()));
        hashMap.put("seller_name", RequestBody.create(MultipartBody.FORM, sm.getString("user_name")));
        hashMap.put("cat_id", RequestBody.create(MultipartBody.FORM, categoriId));
        hashMap.put("p_status", RequestBody.create(MultipartBody.FORM, String.valueOf(1)));
        hashMap.put("id", RequestBody.create(MultipartBody.FORM, String.valueOf(id)));
        hashMap.put("neighborhood_id", RequestBody.create(MultipartBody.FORM, String.valueOf(Integer.parseInt(sm.getString("neighbrhood")))));
        hashMap.put("created_by", RequestBody.create(MultipartBody.FORM, String.valueOf(Integer.parseInt(sm.getString("user_id")))));

        // API Call
        ApiExecutor.getLaravelApiService()
                .createProduct(mediaList, hashMap, "application/json")
                .enqueue(new Callback<AddressResponse>() {
                    @Override
                    public void onResponse(Call<AddressResponse> call, Response<AddressResponse> response) {
                        try {
                            if (response.body() != null && response.body().getStatus().equals("200")) {
                                onBackPressed();
                                Intent detailScreen = new Intent();
                                detailScreen.putExtra("id", id);
                                detailScreen.putExtra("type", "detail");
                                startActivity(detailScreen);
                                finish();
                                progressDialog.dismiss();
                            } else if (response.body() != null && response.body().getMessage() != null) {
                                Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            } else {
                                Log.e("API_ERROR", "Response body null ya status mismatch: " + response.toString());
                                progressDialog.dismiss();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                        }
                    }

                    @Override
                    public void onFailure(Call<AddressResponse> call, Throwable t) {
                        progressDialog.dismiss();
                        if (t.toString().contains("timeout")) {
                            Toast.makeText(EditProduct.this, "Something seems to have gone wrong. Please try again", Toast.LENGTH_SHORT).show();
                        } else if (t.toString().contains("Unable to resolve host")) {
                            Toast.makeText(EditProduct.this, "No internet", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(EditProduct.this, "Something seems to have gone wrong. Please try again", Toast.LENGTH_SHORT).show();
                        }
                        Log.e("fdsadf", t.toString());
                    }
                });
    }

    @Override
    public void removeImage(int position) {
        bitmapList.remove(position);
        uploadAdapter.notifyDataSetChanged();
    }
    public File bitmapToFile(Bitmap bitmap, String fileNameToSave) {
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
    private void resetColor() {
        paid_item_tv.setBackground(getResources().getDrawable(R.drawable.round_corner_white));
        free_item_tv.setBackground(getResources().getDrawable(R.drawable.round_corner_white));
        paid_item_tv.setTextColor(getResources().getColor(R.color.text_color));
        free_item_tv.setTextColor(getResources().getColor(R.color.text_color));
    }
    private void resetColor1() {
        negotiable_item_tv.setBackground(getResources().getDrawable(R.drawable.round_corner_white));
        fix_item_tv.setBackground(getResources().getDrawable(R.drawable.round_corner_white));
        negotiable_item_tv.setTextColor(getResources().getColor(R.color.text_color));
        fix_item_tv.setTextColor(getResources().getColor(R.color.text_color));
    }
    private boolean CheckAllFields() {
        if (item_name.getText().toString().matches("")) {
            return GlobalMethods.setError(item_name, "Please enter item name");
        } else if (item_price.getText().toString().matches("")) {
            return GlobalMethods.setError(item_price, "Please enter item price");

        } else if (item_desc.length() == 0) {
            return GlobalMethods.setError(item_desc, "Please enter description");

        }


        return true;
    }

    public void init() {
        Intent i = getIntent();
        titleTv = findViewById(R.id.title);
        titleTv.setText("Edit item");
        emailsList = Arrays.asList(days);
        item_category = findViewById(R.id.item_category);
        tvActiveItem = findViewById(R.id.tvActiveItem);
        tvDeactivate = findViewById(R.id.tvInactiveItem);
        lnrDeactivate = findViewById(R.id.lnrDeactivate);
        lnrActivate = findViewById(R.id.lnrActivate);
        preview_textview = findViewById(R.id.preview_textview);
        item_name = findViewById(R.id.item_name);
        item_price = findViewById(R.id.item_price);
        totalAmount = findViewById(R.id.totalAmount);
        item_desc = findViewById(R.id.item_desc);
        item_brand = findViewById(R.id.item_brand);
        free_item_tv = findViewById(R.id.free_item_tv);
        paid_item_tv = findViewById(R.id.paid_item_tv);
        item_quantity = findViewById(R.id.quantity_price);
        image_textview = findViewById(R.id.image_textview);
        imgsearch_imageview = findViewById(R.id.search_imageview);
        imgAddImage = findViewById(R.id.add_imageview);
        fix_item_tv = findViewById(R.id.fix_item_tv);
        cancle_tv = findViewById(R.id.cancle_tv);
        negotiable_item_tv = findViewById(R.id.negotiable_item_tv);
        rl_upload = findViewById(R.id.upload_options_rl);
        video_or_image_upload_rl = findViewById(R.id.video_or_image_upload_rl);
        upload_options_rl = findViewById(R.id.upload_options_rl);
        add_imageview = findViewById(R.id.add_imageview);
        price_ll = findViewById(R.id.price_ll);
        back_btn = findViewById(R.id.back_btn);
        chSoldMarkSold = findViewById(R.id.chSoldMarkSold);
        rvMarketPlaceDetails = findViewById(R.id.rvMarketPlaceDetails);
        tv_take_photo = findViewById(R.id.take_photo);
        tv_choose_photo = findViewById(R.id.choose_photo);
        tv_take_video = findViewById(R.id.make_video);
        tv_choose_video = findViewById(R.id.choose_video);
        lnrMarkAsSold = findViewById(R.id.lnrMarkAsSold);

        free_item_tv.setOnClickListener(this);
        paid_item_tv.setOnClickListener(this);
        negotiable_item_tv.setOnClickListener(this);
      //  preview_textview.setOnClickListener(this);
        video_or_image_upload_rl.setOnClickListener(this);
        item_category.setOnClickListener(this);
        titleTv.setOnClickListener(this);
        add_imageview.setOnClickListener(this);
        tv_take_photo.setOnClickListener(this);
        tv_choose_photo.setOnClickListener(this);
        tv_take_video.setOnClickListener(this);
        tv_choose_video.setOnClickListener(this);
        back_btn.setOnClickListener(this);
        fix_item_tv.setOnClickListener(this);
        add_imageview.setVisibility(View.GONE);
        imgsearch_imageview.setVisibility(View.GONE);
        image_textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageDialog(mArrayUri);
            }
        });
    /*    chSoldMarkSold.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                productSold();
            }
        });*/

        chSoldMarkSold.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // Checkbox is checked
                    chSoldMarkSold.setText("Mark as Sold");
                    lastClicked="sold";
                    productSold();
                }
            }
        });

        tvDeactivate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvDeactivate.setText("Deactive");
                lastClicked = "deactivate"; // State को "deactivate" सेट करें
                productInactiveApi();
            }
        });

        tvActiveItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvActiveItem.setText("Active");
                lastClicked = "activate"; // State को "activate" सेट करें
                productActiveApi();
            }
        });

        preview_textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lastClicked.equals("deactivate")) {
                    // यदि आखिरी बार "deactivate" बटन पर क्लिक किया गया था
                    productInactiveApi();
                    onBackPressed();

                } else if (lastClicked.equals("activate")) {
                    // यदि आखिरी बार "activate" बटन पर क्लिक किया गया था
                    onBackPressed();
                    productActiveApi();
                } else if (lastClicked.equals("sold")) {
                    // यदि आखिरी बार "activate" बटन पर क्लिक किया गया था
                    onBackPressed();
                    productSold();
                } else {
                    // अन्यथा, CheckAllFields() के आधार पर editItem() कॉल करें
                    if (CheckAllFields()) {
                        editItem();
                    }
                }
            }
        });
        lnrMarkAsSold.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                productSold();
            }
        });
        cancle_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rl_upload.setVisibility(View.GONE);
            }
        });
    }
    @Override
    public void onClick(String categoryName, String id) {
        item_category.setText(categoryName);
        category = categoryName;

        categoriId = id;
        mail_dialog.cancel();


    }
    @Override
    public void onImageClick(int pos) {
    }
    @Override
    public void onCrossClick(int pos, String from) {
        mArrayUri.remove(pos);
        mArrayGalleryPhoto.remove(pos);
        imageUploadAdapter.notifyDataSetChanged();
    }
    @Override
    public void onCrossClick1(int pos, String from) {
    }
    private void categoryApi() {
        APIInterface service = APIClient.getRetrofit3().create(APIInterface.class);
        Call<StatePojo> call = service.createCategoryProducr();
        call.enqueue(new Callback<StatePojo>() {
            @Override
            public void onResponse(Call<StatePojo> call, Response<StatePojo> response) {
                if (response.isSuccessful() && response.body() != null) {
                    StatePojo statePojo = response.body();
                    if (statePojo.getCategory() != null) {
                        categorylist = statePojo.getCategory();
                    } else {
                        Log.e("EditProduct", "Category list is null");
                        Toast.makeText(EditProduct.this, "Category list is null", Toast.LENGTH_SHORT).show();
                    }

                    // Check and parse other fields
                    try {
                        totalImageCount = Integer.parseInt(statePojo.getMkpImageLimit());
                        totalVideoCount = Integer.parseInt(statePojo.getMkpVideoLimit());
                        postVideoSize = Integer.parseInt(statePojo.getMkpVideoSize());
                    } catch (NumberFormatException e) {
                        Log.e("EditProduct", "Error parsing API response: " + e.getMessage());
                        Toast.makeText(EditProduct.this, "Error parsing API response", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.e("EditProduct", "API response is null or unsuccessful");
                    Toast.makeText(EditProduct.this, "API response is null or unsuccessful", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<StatePojo> call, Throwable t) {
                Toast.makeText(EditProduct.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("res---", t.getMessage());
            }
        });
    }
    private void selectCategory() {
        RecyclerView rv;
        ImageView cancel;
        mail_dialog = new Dialog(this);
        mail_dialog.setContentView(R.layout.market_place_open_category_list);
        rv = mail_dialog.findViewById(R.id.rv_category);
        cancel = mail_dialog.findViewById(R.id.cross_imageview);
        rv.setLayoutManager(new LinearLayoutManager(this));
        emailListAdapter = new MarketPlaceCategoryAdapter(EditProduct.this, categorylist);
        rv.setAdapter(emailListAdapter);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mail_dialog.cancel();
            }
        });
        mail_dialog.getWindow().setBackgroundDrawableResource(R.drawable.round_corner_dialog);
        mail_dialog.setCancelable(true);
        mail_dialog.show();
    }
    private void businessDetailApi(int id) {
        ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Please wait...");
        dialog.setCancelable(false);
        dialog.show();
        HashMap<String, Object> hm = new HashMap<>();
        hm.put("product_id", Integer.parseInt(String.valueOf((id))));
        APIInterface service = APIClient.getRetrofit3().create(APIInterface.class);
        Call<DetailsPojoMarketPlaceParent> call = service.marketPlaceDetailsApiEdit(id);
        call.enqueue(new Callback<DetailsPojoMarketPlaceParent>() {
            @Override
            public void onResponse(Call<DetailsPojoMarketPlaceParent> call, Response<DetailsPojoMarketPlaceParent> response) {
                DetailsPojoMarketPlaceParent rootObject = response.body();
                rootObject.getStatus().equals("200");
                List<DetailsPojoChild> reviewListData = response.body().getProductdetail();
                try {
                    @SuppressLint("WrongConstant") LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, OrientationHelper.VERTICAL, false);

                    stProductName = reviewListData.get(0).getpTitle();
                    stItemPrice = reviewListData.get(0).getSalePrice();
                    stNeighbrhoodName = reviewListData.get(0).getNeighborhoodName();
                    stAboutUs = reviewListData.get(0).getpDescription();
                    stItemPostedBy = reviewListData.get(0).getSellerName();
                    stCreateTime = reviewListData.get(0).getCreatedTime();
                    stQuantity = String.valueOf(reviewListData.get(0).getpQuantity());
                    stType = reviewListData.get(0).getSaleType();
                    stBrandName = reviewListData.get(0).getBrandName();
                    stCategory = reviewListData.get(0).getCatName();
                    categoriId = String.valueOf(reviewListData.get(0).getCatId());
                    Log.d("sdfwwwwwwsfsfs",categoriId);
                    stSoldStatus = String.valueOf(reviewListData.get(0).getpStatus());
                    Log.d("sdfsdfsfe",stSoldStatus);

                    if (stType.equals("Sell")) {
                        price_status = "Sell";
                        paid_item_tv.setBackground(getResources().getDrawable(R.drawable.round_corner_dialog_themcolor));
                        paid_item_tv.setTextColor(getResources().getColor(R.color.white));
                    } else if (stType.equals("Donate")) {
                        price_status = "Donate";
                        free_item_tv.setBackground(getResources().getDrawable(R.drawable.round_corner_dialog_themcolor));
                        free_item_tv.setTextColor(getResources().getColor(R.color.white));
                    }

                    item_name.setText(stProductName);
                    item_price.setText(stItemPrice);
                    item_desc.setText(stAboutUs);
                    item_quantity.setText(stQuantity);
                    item_brand.setText(stBrandName);
                    item_category.setText(stCategory);
                    Log.d("sdfwfsdfsfs",stCategory);
                    if (stSoldStatus.equals("2")){
                        lnrDeactivate.setVisibility(View.GONE);
                        lnrActivate .setVisibility(View.GONE);
                    } else if (stSoldStatus.equals("0")){
                        lnrDeactivate.setVisibility(View.GONE);
                        lnrActivate.setVisibility(View.VISIBLE);
                    }else if (stSoldStatus.equals("1")){
                        lnrDeactivate.setVisibility(View.VISIBLE);
                        lnrActivate.setVisibility(View.GONE);
                    }
                    List<ImagePojo> ar = reviewListData.get(0).getpImages();
                    ar1 = new ArrayList<>(ar);

                    WallChildAdapter wallChildAdapter = new WallChildAdapter(ar1, EditProduct.this, imageCallBack);
                    rvMarketPlaceDetails.setAdapter(wallChildAdapter);

                    if (reviewListData.get(0).getpImages().size() > 0) {

                        for (int i = 0; i < reviewListData.get(0).getpImages().size(); i++) {
                            try {
                                if (reviewListData.get(0).getpImages().get(i).getImg() != null) {
                                    String img = reviewListData.get(0).getpImages().get(i).getImg();
                                    new EditProduct.DownloadTask().execute(img);
                                } else if (reviewListData.get(0).getpImages().get(i).getVideo() != null) {
                                    String video = reviewListData.get(0).getpImages().get(i).getVideo();
                                    new EditProduct.DownloadTask().execute(video);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }

                } catch (Exception e) {

                }
                dialog.dismiss();
            }

            @Override
            public void onFailure(Call<DetailsPojoMarketPlaceParent> call, Throwable t) {
                dialog.dismiss();
                Log.d("res", t.getMessage());
            }
        });
    }
    @Override
    public void onClickImg(List<ImagePojo> img) {
    }
    private void productSold() {
        Boolean isInternetConnection = GlobalMethods.checkConnection(this);
        if (isInternetConnection) {
            HashMap<String, Object> hm = new HashMap<>();
            hm.put("user_id", Integer.parseInt(sm.getString("user_id")));

            hm.put("product_id", (String.valueOf((id))));
            Log.d("product_id", String.valueOf(id));

            APIInterface service = APIClient.getRetrofit3().create(APIInterface.class);
            Call<CommonPojoSuccess> call = service.productSold(hm);
            call.enqueue(new Callback<CommonPojoSuccess>() {
                @SuppressLint("NotifyDataSetChanged")
                @Override
                public void onResponse(Call<CommonPojoSuccess> call, Response<CommonPojoSuccess> response) {
                    try {
                        if (wishlistResponse.getStatus().equals("200")) {
                            onBackPressed();
                            FancyToast.makeText(getApplicationContext(), "Your item has been removed.", FancyToast.LENGTH_LONG, FancyToast.SUCCESS, false).show();
                        } else if (wishlistResponse.getStatus().equals("error")) {
                            FancyToast.makeText(getApplicationContext(), "Your item has been failed.", FancyToast.LENGTH_LONG, FancyToast.SUCCESS, false).show();
                        }
                    } catch (Exception e) {

                    }
                }

                @Override
                public void onFailure(Call<CommonPojoSuccess> call, Throwable t) {
                }
            });
        } else {
            GlobalMethods.getInstance(context).globalDialog(this, "     No internet connection.");
        }
    }

    private void productInactiveApi() {
        Boolean isInternetConnection = GlobalMethods.checkConnection(this);
        if (isInternetConnection) {
            HashMap<String, Object> hm = new HashMap<>();
            hm.put("user_id", Integer.parseInt(sm.getString("user_id")));

            hm.put("product_id", (String.valueOf((id))));
            Log.d("product_id", String.valueOf(id));

            APIInterface service = APIClient.getRetrofit3().create(APIInterface.class);
            Call<CommonPojoSuccess> call = service.inActiveApi(hm);
            call.enqueue(new Callback<CommonPojoSuccess>() {
                @SuppressLint("NotifyDataSetChanged")
                @Override
                public void onResponse(Call<CommonPojoSuccess> call, Response<CommonPojoSuccess> response) {
                    try {
                        if (wishlistResponse.getStatus().equals("200")) {
                            onBackPressed();
                            FancyToast.makeText(getApplicationContext(), " .", FancyToast.LENGTH_LONG, FancyToast.SUCCESS, false).show();
                        } else if (wishlistResponse.getStatus().equals("error")) {
                            FancyToast.makeText(getApplicationContext(), "Your item has been failed.", FancyToast.LENGTH_LONG, FancyToast.SUCCESS, false).show();
                        }
                    } catch (Exception e) {

                    }
                }

                @Override
                public void onFailure(Call<CommonPojoSuccess> call, Throwable t) {
                }
            });
        } else {
            GlobalMethods.getInstance(context).globalDialog(this, "     No internet connection.");
        }
    }

    private void productActiveApi() {
        Boolean isInternetConnection = GlobalMethods.checkConnection(this);
        if (isInternetConnection) {
            HashMap<String, Object> hm = new HashMap<>();
            hm.put("user_id", Integer.parseInt(sm.getString("user_id")));

            hm.put("product_id", (String.valueOf((id))));
            Log.d("product_id", String.valueOf(id));

            APIInterface service = APIClient.getRetrofit3().create(APIInterface.class);
            Call<CommonPojoSuccess> call = service.activeStatus(hm);
            call.enqueue(new Callback<CommonPojoSuccess>() {
                @SuppressLint("NotifyDataSetChanged")
                @Override
                public void onResponse(Call<CommonPojoSuccess> call, Response<CommonPojoSuccess> response) {
                    try {
                        if (wishlistResponse.getStatus().equals("200")) {
                            onBackPressed();
                            FancyToast.makeText(getApplicationContext(), "Your item has been active.", FancyToast.LENGTH_LONG, FancyToast.SUCCESS, false).show();
                        } else if (wishlistResponse.getStatus().equals("error")) {
                            FancyToast.makeText(getApplicationContext(), "Your item has been failed.", FancyToast.LENGTH_LONG, FancyToast.SUCCESS, false).show();
                        }
                    } catch (Exception e) {

                    }
                }

                @Override
                public void onFailure(Call<CommonPojoSuccess> call, Throwable t) {
                }
            });
        } else {
            GlobalMethods.getInstance(context).globalDialog(this, "     No internet connection.");
        }
    }

    @Override
    public void onImageClick(List<ImagePojo> list, int pos) {
        imageDialog(list, pos);
    }

    private String generateFileName(String extension) {
        return System.currentTimeMillis() + "." + extension;
    }

    private class DownloadTask extends AsyncTask<String, Void, Uri> {

        private String downloadedFilePath;
        private boolean isImage = false;

        @Override
        protected Uri doInBackground(String... urls) {
            String mediaUrl = urls[0];
            String fileName;

            if (mediaUrl.endsWith(".mp4")) {
                fileName = generateFileName("mp4");
            } else if (mediaUrl.endsWith(".jpg") || mediaUrl.endsWith(".png")) {
                fileName = generateFileName("jpg");
                isImage = true;
            } else {
                return null; // Unsupported file type
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                // Android 10 and above: Use MediaStore
                return downloadToMediaStore(mediaUrl, fileName);
            } else {
                // Android 9 and below: Use legacy storage
                return downloadToLegacyStorage(mediaUrl, fileName);
            }
        }

        private Uri downloadToMediaStore(String mediaUrl, String fileName) {
            try {
                URL url = new URL(mediaUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    return null;
                }

                ContentValues values = new ContentValues();
                values.put(MediaStore.MediaColumns.DISPLAY_NAME, fileName);

                if (isImage) {
                    values.put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg");
                    values.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES);
                } else {
                    values.put(MediaStore.MediaColumns.MIME_TYPE, "video/mp4");
                    values.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_MOVIES);
                }

                Uri mediaUri = getContentResolver().insert(isImage ? MediaStore.Images.Media.EXTERNAL_CONTENT_URI : MediaStore.Video.Media.EXTERNAL_CONTENT_URI, values);
                if (mediaUri == null) {
                    return null;
                }

                InputStream inputStream = new BufferedInputStream(connection.getInputStream());
                OutputStream outputStream = getContentResolver().openOutputStream(mediaUri);

                byte[] data = new byte[4096];
                int count;
                while ((count = inputStream.read(data)) != -1) {
                    outputStream.write(data, 0, count);
                }

                outputStream.flush();
                outputStream.close();
                inputStream.close();
                return mediaUri;
            } catch (Exception e) {
                Log.e("DownloadTask", "Error: " + e.getMessage());
                return null;
            }
        }

        private Uri downloadToLegacyStorage(String mediaUrl, String fileName) {
            downloadedFilePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/" + fileName;

            try {
                URL url = new URL(mediaUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    return null;
                }

                InputStream inputStream = new BufferedInputStream(connection.getInputStream());
                FileOutputStream outputStream = new FileOutputStream(new File(downloadedFilePath));

                byte[] data = new byte[4096];
                int count;
                while ((count = inputStream.read(data)) != -1) {
                    outputStream.write(data, 0, count);
                }

                outputStream.flush();
                outputStream.close();
                inputStream.close();
                return Uri.fromFile(new File(downloadedFilePath));
            } catch (Exception e) {
                Log.e("DownloadTask", "Error: " + e.getMessage());
                return null;
            }
        }

        @Override
        protected void onPostExecute(Uri mediaUri) {
            if (mediaUri != null) {
                if (isImage) {
                    imageUris.add(mediaUri);
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(EditProduct.this.getContentResolver(), mediaUri);
                        mArrayGalleryPhoto.add(bitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    updateImageTextView();
//                    Toast.makeText(CreateBusinessPageActivity.this, "Image downloaded and added to list: " + mediaUri.toString(), Toast.LENGTH_LONG).show();
                } else {
                    videoUris.add(mediaUri);
                    updateVideoTextView();
//                    Toast.makeText(CreateBusinessPageActivity.this, "Video downloaded and added to list: " + mediaUri.toString(), Toast.LENGTH_LONG).show();
                }

                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q && downloadedFilePath != null) {
                    File file = new File(downloadedFilePath);
                    if (file.exists() && file.delete()) {
                        Toast.makeText(EditProduct.this, "File deleted from local storage", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(EditProduct.this, "Failed to delete file", Toast.LENGTH_SHORT).show();
                    }
                }
            } else {
                Toast.makeText(EditProduct.this, "Download failed", Toast.LENGTH_LONG).show();
            }
        }
    }


    // Handle Permission Results
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
                if (bitmapList.size() < 100) {
                    CLICK_ON = FIRST_IMAGE;
                    upload_options_rl.setVisibility(View.VISIBLE);
                }
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