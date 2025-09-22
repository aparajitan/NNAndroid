package com.app_neighbrsnook.businessModule;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
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
import android.graphics.Color;
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
import android.os.Handler;
import android.os.SystemClock;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.app_neighbrsnook.MainActivity;
import com.app_neighbrsnook.R;
import com.app_neighbrsnook.abusive.BadWordFilter;
import com.app_neighbrsnook.adapter.BusinessCategoryAdapter;
import com.app_neighbrsnook.adapter.CountryAdapter;
import com.app_neighbrsnook.adapter.DayAdapter;
import com.app_neighbrsnook.adapter.DocImageAdapter;
import com.app_neighbrsnook.adapter.DocumentUploadAdapter;
import com.app_neighbrsnook.adapter.ImageUploadAdapter;
import com.app_neighbrsnook.adapter.MediaViewAdapter;
import com.app_neighbrsnook.adapter.UriPagerAdapter;
import com.app_neighbrsnook.apiService.UrlClass;
import com.app_neighbrsnook.event.EventAllListCurrentData;
import com.app_neighbrsnook.model.BusinessModel1;
import com.app_neighbrsnook.network.APIClient;
import com.app_neighbrsnook.network.APIInterface;
import com.app_neighbrsnook.pojo.BusinessDetailPojo;
import com.app_neighbrsnook.pojo.Day;
import com.app_neighbrsnook.pojo.Nbdatum;
import com.app_neighbrsnook.pojo.StatePojo;
import com.app_neighbrsnook.profile.MyProfile;
import com.app_neighbrsnook.utils.GlobalMethods;
import com.app_neighbrsnook.utils.SharedPrefsManager;
import com.bumptech.glide.Glide;
import com.github.barteksc.pdfviewer.PDFView;
import com.app_neighbrsnook.libraries.cropper.CropImage;

import org.json.JSONObject;

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
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateBusinessPageActivity extends AppCompatActivity implements View.OnClickListener, BusinessCategoryAdapter.CategoryInterface, ImageUploadAdapter.ImageRequest, DocumentUploadAdapter.DocRequest, CountryAdapter.OnItemSelected, DayAdapter.DayInterface, DocImageAdapter.DocRequest, MediaViewAdapter.OnItemClickListener {
    TextView titleTv, et_neighbourhood,image_count_textview, doc_count_textview, from_textview, to_textview, doc_textview, day_textview, piblish_textview, video_or_image_upload_rl, business_category, cancle_tv, make_video, choose_video, take_photo, choose_photo, image_textview, country_tv, city_tv, state_tv;
    ImageView back_btn, full_screen_imageview, pdf_hide_imageview, upload_doc_imageview, offer_add_imageview, add_imageview1, search_imageview, add_imageview;
    SharedPrefsManager sm;
    String u_businessName, u_tagline, u_desc, u_opentime, u_closeTime, u_web, u_phone, u_telephone, u_rating, u_review, u_email, u_neighbrhood, u_category, u_week_off, u_document, u_add1, u_add2, u_city, u_state, u_pin, u_country, u_business_category, u_business_id, u_catid, u_takePicFromCamera,
            u_countryid, u_stateid, u_cityid, u_businessid;
    View view_weekly;
    Activity activity;
    String currentPhotoPath;
    String[] occasionString;
    String interesID = "";
    PDFView pdfView;
    CountryAdapter countryAdapter;
    String countryName, stateName, cityName;
    List<Nbdatum> statelist = new ArrayList<>();
    List<Nbdatum> citylist = new ArrayList<>();
    List<Nbdatum> countryList = new ArrayList<>();
    List<Nbdatum> categorylist = new ArrayList<>();
    ArrayList<String> imageList1 = new ArrayList<>();
    DocImageAdapter docImageAdapter;
    List<String> days1 = new ArrayList<>();
    List<Day> dayList = new ArrayList<>();
    String cityId;
    int countryid, stateid;
    RadioGroup rg;
    EditText search_et;
    Dialog mail_dialog, image_dialog, country_dialog, showImageDialog, doc_dialog, dialog;
    RadioButton radio_weekly_off, radio_all_day_open, radio_menu, radio_rate, radio_tarrif, radio_others;
    RelativeLayout weekly_off_relativelayout, upload_doc_rl, upload_options_rl, image_layout, doc_rl;
    EditText business_name, business_tagline, business_desc, add1_et, add2_et, pin_et, web_et, email_et, mobile_et, tel_et;
    String categoryName, doc_type = "Menu";
    RecyclerView rv_doc, rv;
    Bitmap bitmap;
    String business_page_type;
    String opentime, closeTime;
    private final int TAKE_VIDEO_FROM_GALLARY = 1;
    private final int TAKE_VIDEO_FROM_CAMERA = 2;
    private final int TAKE_PIC_FROM_GALLARY = 3;
    private final int TAKE_PIC_FROM_CAMERA = 4;
    private final int REQUEST_CODE_DOC = 5;
    Uri imageUri;
    RelativeLayout pdf_rl, offer_rl, offer_text_rl;
    Context context = CreateBusinessPageActivity.this;
    BusinessCategoryAdapter businessCategoryAdapter;
    int updateOpenHrs = 10, updateOpenMns = 00, updateCloseHrs = 19, updateCloseMns = 00;
    InputMethodManager imm;
    Bitmap galleryPhoto;
    ArrayList<Bitmap> mArrayGalleryPhoto = new ArrayList<>();
    ArrayList<Bitmap> mArrayGalleryPdf = new ArrayList<>();
    private int maxImageCountDoc = 0;
    private int maxImageCount = 0;
    String tim;
    ProgressDialog progressDialog;
    ScrollView root;
    String apiStatus;
    ArrayList<Uri> mArrayUri = new ArrayList<>();
    ArrayList<Uri> mArrayUri1 = new ArrayList<>();
    ArrayList<Uri> imageUris = new ArrayList<>();
    ArrayList<Uri> videoUris = new ArrayList<>();
    int totalImageCount, totalVideoCount, itemCount, postVideoSize;
    private ArrayList<String> videoFilePaths = new ArrayList<>();
    MediaViewAdapter mediaViewAdapter;
    ImageUploadAdapter imageUploadAdapter;
    DocumentUploadAdapter documentUploadAdapter;
    private String downloadedFilePath;
    StringBuilder builder;

    @SuppressLint({"SetTextI18n", "ClickableViewAccessibility"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_business_page);

        sm = new SharedPrefsManager(this);
        context = activity = this;
     //    GlobalMethods.checkPermission(this);

        init();
        categoryApi();
        countryApi();
        stateApi();

        days1.add("Open All Days");
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        search_imageview.setVisibility(View.GONE);
        add_imageview.setVisibility(View.GONE);

        root.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                hideKeyboard1();
                return false;
            }
        });
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup arg0, int id) {
                switch (id) {

                    case R.id.radio_menu:
                        doc_type = "Menu";

                        break;
                    case R.id.radio_rate:
                        doc_type = "Rate";
                        break;
                    case R.id.radio_tarrif:
                        doc_type = "Tarrif";
                        break;

                    case R.id.radio_others:
                        doc_type = "Other";
                        break;
                    default:

                        break;
                }
            }
        });
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        business_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectCategory();
            }
        });
    }

    private void hideKeyboard1() {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        View currentFocus = getCurrentFocus();
        if (currentFocus != null) {
            imm.hideSoftInputFromWindow(currentFocus.getWindowToken(), 0);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.radio_all_day_open:
                days1.clear();
                days1.add("Open All Days");
                weekly_off_relativelayout.setVisibility(View.GONE);
                view_weekly.setVisibility(View.GONE);
                break;

            case R.id.radio_weekly_off:
                days1.clear();
                weekly_off_relativelayout.setVisibility(View.VISIBLE);
                view_weekly.setVisibility(View.VISIBLE);
                break;

            case R.id.from_textview:
                timePicker(from_textview, "opentime", updateOpenHrs, updateOpenMns);
                String time = from_textview.getText().toString();
                break;

            case R.id.to_textview:
                timePicker(to_textview, "closeTime", updateCloseHrs, updateCloseMns);
                break;

            case R.id.day_textview:
                selectDay();
                break;

            case R.id.offer_rl:
                takePhotoFromGallery();
                break;

            case R.id.weekly_off_relativelayout:

                break;

            case R.id.upload_doc_imageview:
                if (GlobalMethods.checkCameraAndGalleryPermission(CreateBusinessPageActivity.this)) {
                    if (mArrayUri1.size() < maxImageCountDoc) {
                        takePhotoFromGallery1();
                    } else {
                        GlobalMethods.getInstance(CreateBusinessPageActivity.this).globalDialog(CreateBusinessPageActivity.this, "You can upload only " + maxImageCountDoc + " Documents.");                    }
                }
                break;

            case R.id.piblish_textview:
                if (business_page_type.equals("edit")) {
                    progressDialog.show();
                    new SimulateTask1().execute();
                } else {
                    if (CheckAllFields()) {
                        progressDialog.show();
                        new SimulateTask().execute();
                    } else {
                    }
                }
                break;

            case R.id.add_imageview1:
                if (GlobalMethods.checkCameraAndGalleryPermission(CreateBusinessPageActivity.this)) {
                    hideKeyboard1();
                    if (mArrayUri.size() < itemCount) {
                        upload_options_rl.setVisibility(View.VISIBLE);
                    } else {
                        upload_options_rl.setVisibility(View.GONE);
                        GlobalMethods.getInstance(CreateBusinessPageActivity.this).globalDialog(CreateBusinessPageActivity.this, "You can only add up to " + itemCount + " media items (" + totalVideoCount + " video and " + totalImageCount + " images)");
                    }
                }

                break;

            case R.id.cancle_tv:
                upload_options_rl.setVisibility(View.GONE);
                break;

            case R.id.make_video:
                takeVideoFromCamera();
                upload_options_rl.setVisibility(View.GONE);
                break;

            case R.id.choose_video:
                takeVideoFromGallary();
                upload_options_rl.setVisibility(View.GONE);
                break;

            case R.id.take_photo:
                takePhotoFromCamera();
                upload_options_rl.setVisibility(View.GONE);
                break;

            case R.id.choose_photo:
                takePhotoFromGallery();
                upload_options_rl.setVisibility(View.GONE);
                break;

            case R.id.pdf_hide_imageview:
                pdf_rl.setVisibility(View.GONE);
                break;

            case R.id.image_textview:
                imageDialog(mArrayUri, "img");
                break;

            case R.id.city_tv:
                listOfCountrt(citylist, "city");
                break;

            case R.id.state_tv:
                listOfCountrt(statelist, "state");
                break;

            case R.id.doc_textview:
                DocDialog(mArrayUri1);
                break;

            case R.id.business_category:
                selectCategory();
                break;

        }
    }

    private void takePhotoFromGallery1() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        String[] mimeTypes = {"application/pdf", "image/*"};
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
        startActivityForResult(intent, REQUEST_CODE_DOC);
    }

    public void timePicker(TextView tv, String type, int hr, int min) {
        final Calendar c = Calendar.getInstance();
        int hour = 0;
        int minute = 0;
        if (type.equals("opentime")) {
            hour = hr;
            minute = min;
        }
        if (type.equals("closeTime")) {
            hour = hr;
            minute = min;
        }

        TimePickerDialog timePickerDialog = new TimePickerDialog(CreateBusinessPageActivity.this, R.style.TimePickerDialogStyle,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {
                        boolean isPM = (hourOfDay >= 12);
                        tim = String.format("%02d:%02d %s", (hourOfDay == 12 || hourOfDay == 0) ? 12 : hourOfDay % 12, minute, isPM ? "PM" : "AM");
                        tv.setText(tim);

                        if (type.equals("opentime")) {
                            opentime = tim;
                            updateOpenHrs = hourOfDay;
                            updateOpenMns = minute;
                            Log.d("OpenTime", opentime);
                        }
                        if (type.equals("closeTime")) {
                            closeTime = tim;
                            updateCloseHrs = hourOfDay;
                            updateCloseMns = minute;
                            Log.d("CloseTime", closeTime);

                        }
                    }
                }, hour, minute, false);

        timePickerDialog.show();
    }

    private void documentDialog(int position) {
        PDFView pdfView;
        CardView cardView;
        doc_dialog = new Dialog(this);
        doc_dialog.setContentView(R.layout.open_doc_dialog);
        cardView = doc_dialog.findViewById(R.id.cardView);
        pdfView = doc_dialog.findViewById(R.id.pdfView);
        int pageNumber = 0;
        pdfView.fromUri(mArrayUri1.get(position))
                .defaultPage(pageNumber)
                .enableSwipe(true)
                .swipeHorizontal(true)
                .enableAnnotationRendering(true)
                .load();
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doc_dialog.cancel();
            }
        });
        doc_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(CreateBusinessPageActivity.this, android.R.color.transparent)));
        doc_dialog.setCancelable(false);
        doc_dialog.show();
    }

    private void imageDialog(ArrayList<Uri> mArrayUri, String from) {
        //Shubham Upadte
        RecyclerView rv;
        ImageView cancel;
        image_dialog = new Dialog(this);
        image_dialog.setContentView(R.layout.open_image_dialog);
        rv = image_dialog.findViewById(R.id.rv_image);
        cancel = image_dialog.findViewById(R.id.cross_imageview);
        pdfView = image_dialog.findViewById(R.id.pdfView);
        rv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        mediaViewAdapter = new MediaViewAdapter(this, image_dialog, mArrayUri, CreateBusinessPageActivity.this);
        rv.setAdapter(mediaViewAdapter);

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

    private void DocDialog(ArrayList<Uri> mArrayUri1) {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.item_documents_dialog);

        CardView cancel = dialog.findViewById(R.id.cv_dialog_exit);
        ViewPager2 viewPager = dialog.findViewById(R.id.documentViewPager);
        CardView removeBtn = dialog.findViewById(R.id.remove_doc_card);

        ArrayList<String> typeList = new ArrayList<>();
        for (Uri uri : mArrayUri1) {
            String mimeType = getContentResolver().getType(uri);
            if (mimeType != null && mimeType.startsWith("image/")) {
                typeList.add("image");
            } else {
                typeList.add("pdf");
            }
        }

        UriPagerAdapter adapter = new UriPagerAdapter(this, mArrayUri1, typeList);
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(3);

        cancel.setOnClickListener(v -> dialog.dismiss());

        removeBtn.setOnClickListener(v -> {
            int currentPosition = viewPager.getCurrentItem();
            mArrayUri1.remove(currentPosition);
            typeList.remove(currentPosition);

            if (mArrayUri1.isEmpty()) {
                dialog.dismiss();
                doc_textview.setVisibility(View.GONE);
            } else {
                UriPagerAdapter newAdapter = new UriPagerAdapter(this, mArrayUri1, typeList);
                viewPager.setAdapter(newAdapter);
                updatePdfTextView();
            }
        });

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.BLACK));
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(dialog.getWindow().getAttributes());
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;
        dialog.getWindow().setAttributes(layoutParams);
        dialog.show();
    }

    private void takePhotoFromGallery() {
        //Shubham Upadte
        try {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), TAKE_PIC_FROM_GALLARY);
        } catch (Exception e) {
        }
    }

    private void takePhotoFromCamera() {
        //Shubham Upadte
        String fileName = System.currentTimeMillis() + "";
        File storageDirectory = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        try {
            File imageFile = File.createTempFile(fileName, "jpg", storageDirectory);
            currentPhotoPath = imageFile.getAbsolutePath();
            imageUri = FileProvider.getUriForFile(CreateBusinessPageActivity.this, "com.app_neighbrsnook.fileprovider", imageFile);
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            startActivityForResult(intent, TAKE_PIC_FROM_CAMERA);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void takeVideoFromGallary() {
        //Shubham Upadte
        Intent TAKE_VIDEO_FROM_GALLARYIntent = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(TAKE_VIDEO_FROM_GALLARYIntent, TAKE_VIDEO_FROM_GALLARY);
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
            } else if (req == REQUEST_CODE_DOC && result == RESULT_OK) {
                handleDocResult(data);
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

    private void handleDocResult(Intent data) {
        if (data.getClipData() != null) {
            // Handle multiple files selected
            ClipData mClipData = data.getClipData();
            int count = maxImageCountDoc - mArrayUri1.size(); // Limit number of files
            for (int i = 0; i < mClipData.getItemCount(); i++) {
                if (i < count) {
                    Uri fileUri = mClipData.getItemAt(i).getUri();
                    String mimeType = getContentResolver().getType(fileUri);

                    // Check if the file is an image or PDF
                    if (mimeType != null && mimeType.startsWith("image/")) {
                        // Add image URI to mArrayUri1
                        mArrayUri1.add(fileUri);
                    } else if (mimeType != null && mimeType.equals("application/pdf")) {
                        // Add PDF URI to mArrayUri1
                        mArrayUri1.add(fileUri);
                    }
                }
            }
        } else if (data.getData() != null) {
            // Handle single file selected
            Uri fileUri = data.getData();
            String mimeType = getContentResolver().getType(fileUri);

            // Check if the file is an image or PDF
            if (mimeType != null && mimeType.startsWith("image/")) {
                mArrayUri1.add(fileUri); // Add image URI to mArrayUri1
            } else if (mimeType != null && mimeType.equals("application/pdf")) {
                mArrayUri1.add(fileUri); // Add PDF URI to mArrayUri1
            }
        }

        // Update TextView to display the number of selected files
        updatePdfTextView();
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
                Toast.makeText(CreateBusinessPageActivity.this, "Video compression failed", Toast.LENGTH_SHORT).show();
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

    private void updatePdfTextView() {
        //Shubham Upadte
        if (mArrayUri1.size() > 0) {
            doc_textview.setVisibility(View.VISIBLE);
            doc_textview.setText(mArrayUri1.size() + " Document");
        } else {
            doc_textview.setVisibility(View.GONE);
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
        //Shubham Upadte
        if (mArrayUri.size() > 0) {
            image_textview.setVisibility(View.VISIBLE);
            image_textview.setText(mArrayUri.size() + " preview");
        } else {
            image_textview.setVisibility(View.GONE);
            image_dialog.cancel();
        }
    }

    private void updatePdfCountText() {
        //Shubham Upadte
        if (mArrayUri1.size() > 0) {
            doc_textview.setVisibility(View.VISIBLE);
            doc_textview.setText(mArrayUri.size() + " preview");
        } else {
            doc_textview.setVisibility(View.GONE);
            image_dialog.cancel();
        }
    }

    private void apiCreateBusiness() {
        List<MultipartBody.Part> mediaList = new ArrayList<>();
        List<MultipartBody.Part> pdfList = new ArrayList<>();
        File mediaFile;
        int compressionRatio = 20;
        MultipartBody.Part userMedia;
        MultipartBody.Part userDoc;

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
            userMedia = MultipartBody.Part.createFormData("image[]", mediaFile.getName(), image);
            mediaList.add(userMedia);
            Log.d("post_list", mediaList.toString());
        }

        // Handle videos
        for (Uri videoUri : videoUris) {
            mediaFile = uriToFile(videoUri);
            Log.d("mArrayVideo...", mediaFile.toString());

            RequestBody video = RequestBody.create(MediaType.parse("video/mp4"), mediaFile);
            userMedia = MultipartBody.Part.createFormData("image[]", mediaFile.getName(), video);
            mediaList.add(userMedia);
            Log.d("post_list", mediaList.toString());
        }


        if (mArrayUri1.size() != 0) { // Assuming mArrayUri1 contains both images and PDF URIs
            for (Uri fileUri : mArrayUri1) {
                String mimeType = getContentResolver().getType(fileUri);

                if (mimeType != null) {
                    mediaFile = uriToFile(fileUri); // Convert the Uri to a File
                    Log.d("FileURI...", mediaFile.toString());

                    if (mimeType.equals("application/pdf")) {
                        // Handle PDF
                        RequestBody document = RequestBody.create(MediaType.parse("application/pdf"), mediaFile);
                        userDoc = MultipartBody.Part.createFormData("document[]", mediaFile.getName(), document);
                        pdfList.add(userDoc); // Add PDF to the list
                    } else if (mimeType.startsWith("image/")) {
                        // Handle Image
                        RequestBody document = RequestBody.create(MediaType.parse("image/*"), mediaFile);
                        userDoc = MultipartBody.Part.createFormData("document[]", mediaFile.getName(), document);
                        pdfList.add(userDoc); // Add Image to the list (same as PDF)
                    }
                }
            }
        }

        APIInterface service = APIClient.getRetrofit().create(APIInterface.class);
        Call<BusinessModel1> call = service.createBusiness1("newcreatebusiness",
                RequestBody.create(MultipartBody.FORM, business_name.getText().toString()),
                RequestBody.create(MultipartBody.FORM, business_tagline.getText().toString()),
                RequestBody.create(MultipartBody.FORM, String.valueOf(Integer.parseInt(u_catid))),
                RequestBody.create(MultipartBody.FORM, business_desc.getText().toString()),
                RequestBody.create(MultipartBody.FORM, String.valueOf(opentime)),
                RequestBody.create(MultipartBody.FORM, String.valueOf(closeTime)),
                RequestBody.create(MultipartBody.FORM, String.valueOf(days1)),
                RequestBody.create(MultipartBody.FORM, add1_et.getText().toString()),
                RequestBody.create(MultipartBody.FORM, add2_et.getText().toString()),
                RequestBody.create(MultipartBody.FORM, pin_et.getText().toString()),
                RequestBody.create(MultipartBody.FORM, web_et.getText().toString()),
                RequestBody.create(MultipartBody.FORM, email_et.getText().toString()),
                RequestBody.create(MultipartBody.FORM, mobile_et.getText().toString()),
                RequestBody.create(MultipartBody.FORM, tel_et.getText().toString()),
                RequestBody.create(MultipartBody.FORM, sm.getString("user_id")),
                RequestBody.create(MultipartBody.FORM, doc_type),
                mediaList, pdfList,"application/json");  // Use list for both images and videos together

        call.enqueue(new Callback<BusinessModel1>() {
            @Override
            public void onResponse(Call<BusinessModel1> call, Response<BusinessModel1> response) {
                Log.d("response", response.body().toString());
                if (response.isSuccessful()) {
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
            }

            @Override
            public void onFailure(Call<BusinessModel1> call, Throwable t) {
                Log.d("res", t.getMessage());
            }
        });
    }

    private void apiCreateBusiness1() {
        List<MultipartBody.Part> mediaList = new ArrayList<>();
        List<MultipartBody.Part> pdfList = new ArrayList<>();
        File mediaFile;
        int compressionRatio = 20;
        MultipartBody.Part userMedia;
        MultipartBody.Part userDoc;

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
            userMedia = MultipartBody.Part.createFormData("image[]", mediaFile.getName(), image);
            mediaList.add(userMedia);
            Log.d("post_list", mediaList.toString());
        }

        // Handle videos
        for (Uri videoUri : videoUris) {
            mediaFile = uriToFile(videoUri);
            Log.d("mArrayVideo...", mediaFile.toString());

            RequestBody video = RequestBody.create(MediaType.parse("video/mp4"), mediaFile);
            userMedia = MultipartBody.Part.createFormData("image[]", mediaFile.getName(), video);
            mediaList.add(userMedia);
            Log.d("post_list", mediaList.toString());
        }

        // Handle PDF Documents
        if (mArrayUri1.size() != 0) { // Assuming `mArrayUri1` contains the selected PDF URIs
            for (Uri pdfUri : mArrayUri1) {
                mediaFile = uriToFile(pdfUri); // Convert the Uri to a File
                Log.d("mArrayGalleryPdf...", mediaFile.toString());

                RequestBody document = RequestBody.create(MediaType.parse("application/pdf"), mediaFile);
                userDoc = MultipartBody.Part.createFormData("document[]", mediaFile.getName(), document);
                pdfList.add(userDoc);
                Log.d("post_list", pdfList.toString());
            }
        }

//        // Handle Document
//        if (mArrayGalleryPdf.size() != 0) {
//            for (int i = 0; i < mArrayGalleryPdf.size(); i++) {
//                mediaFile = bitmapToFile(mArrayGalleryPdf.get(i), getString(R.string.app_name) + System.currentTimeMillis() + ".jpg");
//                Log.d("mArrayGalleryPdf...", mediaFile.toString());
//                try {
//                    Bitmap bitmap = BitmapFactory.decodeFile(mediaFile.getPath());
//                    bitmap.compress(Bitmap.CompressFormat.JPEG, compressionRatio, new FileOutputStream(mediaFile));
//                } catch (Throwable t) {
//                    Log.e("ERROR", "Error compressing file." + t);
//                    t.printStackTrace();
//                }
//                RequestBody document = RequestBody.create(MediaType.parse(".jpg"), mediaFile);
//                userDoc = MultipartBody.Part.createFormData("document[]", mediaFile.getName(), document);
//                pdfList.add(userDoc);
//                Log.d("post_list", pdfList.toString());
//            }
//        }


        APIInterface service = APIClient.getRetrofit().create(APIInterface.class);
        Call<BusinessModel1> call = service.updateBusiness("newcreatebusiness",
                RequestBody.create(MultipartBody.FORM, business_name.getText().toString()),
                RequestBody.create(MultipartBody.FORM, business_tagline.getText().toString()),
                RequestBody.create(MultipartBody.FORM, String.valueOf(Integer.parseInt(u_catid))),
                RequestBody.create(MultipartBody.FORM, business_desc.getText().toString()),
                RequestBody.create(MultipartBody.FORM, u_opentime),
                RequestBody.create(MultipartBody.FORM, u_closeTime),
                RequestBody.create(MultipartBody.FORM, String.valueOf(days1)),
                RequestBody.create(MultipartBody.FORM, add1_et.getText().toString()),
                RequestBody.create(MultipartBody.FORM, add2_et.getText().toString()),
                RequestBody.create(MultipartBody.FORM, pin_et.getText().toString()),
                RequestBody.create(MultipartBody.FORM, web_et.getText().toString()),
                RequestBody.create(MultipartBody.FORM, email_et.getText().toString()),
                RequestBody.create(MultipartBody.FORM, mobile_et.getText().toString()),
                RequestBody.create(MultipartBody.FORM, tel_et.getText().toString()),
                RequestBody.create(MultipartBody.FORM, sm.getString("user_id")),
                RequestBody.create(MultipartBody.FORM, doc_type),
                RequestBody.create(MultipartBody.FORM, String.valueOf(Integer.parseInt(u_business_id))),
                mediaList, pdfList,"application/json");  // Use list for image and video, listPdf remains unchanged
        call.enqueue(new Callback<BusinessModel1>() {
            @Override
            public void onResponse(Call<BusinessModel1> call, Response<BusinessModel1> response) {
                Log.d("response", response.body().toString());
                if (response.isSuccessful()) {
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
            }

            @Override
            public void onFailure(Call<BusinessModel1> call, Throwable t) {
                Log.d("res", t.getMessage());
            }
        });
    }

    private void businessDetailApi(int id) {
        progressDialog.show();
        HashMap<String, Object> hm = new HashMap<>();
        hm.put("business_id", id);
        hm.put("userid", Integer.parseInt(sm.getString("user_id")));
        APIInterface service = APIClient.getRetrofit().create(APIInterface.class);
        Call<BusinessDetailPojo> call = service.businessDetail("businessdetails", hm);
        call.enqueue(new Callback<BusinessDetailPojo>() {
            @Override
            public void onResponse(Call<BusinessDetailPojo> call, Response<BusinessDetailPojo> response) {
                BusinessDetailPojo rootObject = response.body();
                titleTv.setText("Edit Business Page");

                u_businessName = rootObject.getBusinessName();
                u_tagline = rootObject.getTagline();
                u_desc = rootObject.getDescription();
                u_opentime = rootObject.getFromtime();
                u_closeTime = rootObject.getTotime();
                u_web = rootObject.getWeb();
                u_phone = rootObject.getMobile();
                u_telephone = rootObject.getTelephone();
                u_rating = rootObject.getRating();
                u_review = rootObject.getReview();
                u_email = rootObject.getEmail();
                u_neighbrhood = rootObject.getNeighborhood();
                u_category = rootObject.getCatid();
                u_week_off = rootObject.getWeeklyOff();
                u_add1 = rootObject.getAdd1();
                u_add2 = rootObject.getAdd2();
                u_city = rootObject.getCity();
                u_state = rootObject.getState();
                u_pin = rootObject.getPincode();
                u_country = rootObject.getCountry();
                u_business_category = rootObject.getCategory();
                u_business_id = rootObject.getId();
                u_catid = rootObject.getCatid();
                u_countryid = rootObject.getCountryId();
                u_stateid = rootObject.getStateId();
                u_cityid = rootObject.getCityId();

                business_name.setText(u_businessName);
                business_tagline.setText(u_tagline);
                business_desc.setText(u_desc);
                add1_et.setText(u_add1);
                add2_et.setText(u_add2);
                pin_et.setText(u_pin);
                web_et.setText(u_web);
                email_et.setText(u_email);
                mobile_et.setText(u_phone);
                tel_et.setText(u_telephone);
                city_tv.setText(u_city);
                state_tv.setText(u_state);
                country_tv.setText("India");
                from_textview.setText(u_opentime);
                to_textview.setText(u_closeTime);
                business_category.setText(rootObject.getCategory());

                switch (rootObject.getDoctype()) {
                    case "Menu":
                        radio_menu.setChecked(true);
                        break;

                    case "Rate":
                        radio_rate.setChecked(true);
                        break;

                    case "Tarrif":
                        radio_tarrif.setChecked(true);
                        break;

                    case "Other":
                        radio_others.setChecked(true);
                        break;

                    default:
                        break;
                }

                if (rootObject.getWeeklyOff().equals("Open All Days")) {
                    radio_all_day_open.setChecked(true);
                } else {
                    radio_weekly_off.setChecked(true);
                    weekly_off_relativelayout.setVisibility(View.VISIBLE);
                    view_weekly.setVisibility(View.VISIBLE);
                    day_textview.setText(u_week_off);
                }

                // For images/videos
                if (rootObject.getImage() != null && !rootObject.getImage().isEmpty()) {
                    for (int i = 0; i < rootObject.getImage().size(); i++) {
                        try {
                            if (rootObject.getImage().get(i).getImg() != null && !rootObject.getImage().get(i).getImg().isEmpty()) {
                                String img = rootObject.getImage().get(i).getImg();
                                new DownloadTask("image").execute(img);
                            } else if (rootObject.getImage().get(i).getVideo() != null && !rootObject.getImage().get(i).getVideo().isEmpty()) {
                                String video = rootObject.getImage().get(i).getVideo();
                                new DownloadTask("image").execute(video);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }


                // For documents
                if (rootObject.getDocument() != null && !rootObject.getDocument().isEmpty()) {
                    doc_textview.setVisibility(View.VISIBLE);
                    doc_textview.setText(rootObject.getDocument().size() + " PDF");

                    for (int i = 0; i < rootObject.getDocument().size(); i++) {
                        try {
                            String docUrl = rootObject.getDocument().get(i).getDoc();
                            if (docUrl != null && !docUrl.isEmpty()) {
                                new DownloadTask("document").execute(docUrl);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

                progressDialog.dismiss();

            }

            @Override
            public void onFailure(Call<BusinessDetailPojo> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(CreateBusinessPageActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("res", t.getMessage());
            }
        });
    }

    public File bitmapToFile(Bitmap bitmap, String fileNameToSave) {
        //Shubham Upadte
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

    private void selectDay() {
        RecyclerView rv;
        TextView confirm;
        ImageView cancel;

        String[] days = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
        mail_dialog = new Dialog(this);
        mail_dialog.setContentView(R.layout.open_selected_dialog_mail);
        rv = mail_dialog.findViewById(R.id.rv_days);
        confirm = mail_dialog.findViewById(R.id.confirm_textview);
        cancel = mail_dialog.findViewById(R.id.cross_imageview);
        rv.setLayoutManager(new LinearLayoutManager(this));
        dayList.clear();
        days1.clear();
        if (builder == null) {
            builder = new StringBuilder();
        }
        DayAdapter emailListAdapter = new DayAdapter(this, getDayList(), builder);
        rv.setAdapter(emailListAdapter);
        for (Day day : dayList) {
            day.setChecked(false);
            if (u_week_off != null && !u_week_off.isEmpty()) {
                if (u_week_off.contains(day.getName())) {
                    day.setChecked(true);
                }
            }
        }

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                interesID = "";
                String intrestdValues = "";
                String d = "";
                for (Day nbdatum : dayList) {
                    if (nbdatum.isChecked()) {
                        days1.add(nbdatum.getName());
                    }
                }
                builder = new StringBuilder();
                for (String da : days1) {
                    builder.append(da);
                    builder.append(", ");
                }
                day_textview.setText(builder.toString());
                mail_dialog.cancel();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mail_dialog.cancel();
            }
        });

        mail_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(CreateBusinessPageActivity.this, android.R.color.transparent)));
        mail_dialog.setCancelable(false);
        mail_dialog.show();
    }

    private List<Day> getDayList() {
        dayList.add(new Day("Monday"));
        dayList.add(new Day("Tuesday"));
        dayList.add(new Day("Wednesday"));
        dayList.add(new Day("Thursday"));
        dayList.add(new Day("Friday"));
        dayList.add(new Day("Saturday"));
        dayList.add(new Day("Sunday"));

        return dayList;
    }

    private void selectCategory() {
        ImageView cancel;
        mail_dialog = new Dialog(this);
        mail_dialog.setContentView(R.layout.open_category_list);
        rv = mail_dialog.findViewById(R.id.rv_category);
        cancel = mail_dialog.findViewById(R.id.cross_imageview);
        TextView tv = mail_dialog.findViewById(R.id.tv_itm);
        tv.setText("Business categories");
        rv.setLayoutManager(new LinearLayoutManager(this));
        businessCategoryAdapter = new BusinessCategoryAdapter(CreateBusinessPageActivity.this, categorylist);
        rv.setAdapter(businessCategoryAdapter);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mail_dialog.cancel();
            }
        });
        mail_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(CreateBusinessPageActivity.this, android.R.color.transparent)));
        mail_dialog.setCancelable(true);
        mail_dialog.show();
    }

    @Override
    public void onClick(String name, String id) {
        business_category.setText(name);
        categoryName = name;
        u_catid = id;
        mail_dialog.cancel();
    }

    @Override
    public void onImageClick(int pos) {

    }

    @Override
    public void onCrossClick(int pos, String from) {
//        if(from.equals("img")) {
        mArrayUri.remove(pos);
        mArrayGalleryPhoto.remove(pos);
//            imageList.remove(pos);
        imageUploadAdapter.notifyDataSetChanged();

        if (mArrayUri.size() == 1) {
            image_textview.setVisibility(View.VISIBLE);
            image_textview.setText(mArrayUri.size() + " Image");
        }
        if (mArrayUri.size() >= 1) {
            image_textview.setVisibility(View.VISIBLE);
            image_textview.setText(mArrayUri.size() + " Images");
        }
        if (mArrayUri.size() == 0) {
            image_dialog.dismiss();
            image_textview.setVisibility(View.GONE);
        }
//        }
    }

    @Override
    public void onCrossClick1(int pos, String from) {
        mArrayUri1.remove(pos);
        mArrayGalleryPdf.remove(pos);
//            imageList.remove(pos);
        imageUploadAdapter.notifyDataSetChanged();

        if (mArrayUri1.size() == 1) {
            doc_textview.setVisibility(View.VISIBLE);
            doc_textview.setText(mArrayUri1.size() + " Image");
        }
        if (mArrayUri1.size() >= 1) {
            doc_textview.setVisibility(View.VISIBLE);
            doc_textview.setText(mArrayUri1.size() + " Images");
        }
        if (mArrayUri1.size() == 0) {
            image_dialog.dismiss();
            doc_textview.setVisibility(View.GONE);
        }
    }

    @Override
    public void onDocClick(int position) {

        documentDialog(position);
    }

    @Override
    public void onDocRemove(int position) {
        mArrayUri1.remove(position);
        if (documentUploadAdapter != null) {
            documentUploadAdapter.notifyDataSetChanged();
        }
        updatePdfCountText();
    }

    private boolean CheckAllFields() {
        if (business_name.getText().toString().matches("")) {
            GlobalMethods.setError(business_name, "Please enter business name");
            return false;
        } else if (BadWordFilter.containsBadWord(business_name.getText().toString())) {
            GlobalMethods.getInstance(CreateBusinessPageActivity.this).globalDialogAbusiveWord(CreateBusinessPageActivity.this, getString(R.string.abusive_msg));
            return false;
        }

        else if (business_category.getText().toString().equals("Select category")) {
            Toast.makeText(CreateBusinessPageActivity.this, "Please select business category", Toast.LENGTH_SHORT).show();
            return false;
        } else if (business_desc.getText().toString().matches("")) {
            GlobalMethods.setError(business_desc, "Please describe the business");
            return false;
        } else if (business_category.getText().toString().equals("00:00 am")) {
            Toast.makeText(CreateBusinessPageActivity.this, "Please select business categrory", Toast.LENGTH_SHORT).show();
            return false;
        } else if (from_textview.getText().toString().equals("00:00 am")) {
            Toast.makeText(CreateBusinessPageActivity.this, "Please select business open time", Toast.LENGTH_SHORT).show();
            return false;
        } else if (to_textview.getText().toString().equals("00:00 am")) {
            Toast.makeText(CreateBusinessPageActivity.this, "Please select business close time", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if (add1_et.length() == 0) {
            GlobalMethods.setError(add1_et, "Please enter address line 1");
            return false;
        } else if (add2_et.getText().toString().matches("")) {
            GlobalMethods.setError(add2_et, "Please enter address line 2");
            return false;
        } else if (state_tv.getText().toString().equals("Select state")) {
            Toast.makeText(CreateBusinessPageActivity.this, "Please select state", Toast.LENGTH_SHORT).show();
            return false;
        } else if (city_tv.getText().toString().trim().equals("Select city")) {
            Toast.makeText(CreateBusinessPageActivity.this, "Please select city", Toast.LENGTH_SHORT).show();
            return false;
        } else if (pin_et.getText().toString().matches("")) {
            GlobalMethods.setError(pin_et, "Please enter pincode");
            return false;
        } else if (!(pin_et.getText().toString().length() >= 6)) {
            GlobalMethods.setError(pin_et, "Please enter 6 digits pincode");
            return false;
        }
        else if (rg.getCheckedRadioButtonId() == -1) {
            if (radio_weekly_off.isChecked() || radio_all_day_open.isChecked()) {
                Log.d("QAOD", "Gender is Selected");
            } else {
                Toast.makeText(getApplicationContext(), "Please select Gender", Toast.LENGTH_SHORT).show();
                Log.d("QAOD", "Gender is Null");
            }
            return false;
        }
        return true;
    }

    public void init() {
        search_imageview = findViewById(R.id.search_imageview);
        add_imageview = findViewById(R.id.add_imageview);
        titleTv = findViewById(R.id.title);
        back_btn = findViewById(R.id.back_btn);
        et_neighbourhood = findViewById(R.id.et_neighbourhood);
        et_neighbourhood.setText(sm.getString("neighbrhood_name"));
        country_tv = findViewById(R.id.country_tv);
        country_tv.setText("India");
        city_tv = findViewById(R.id.city_tv);
        city_tv.setText(sm.getString("city"));
        state_tv = findViewById(R.id.state_tv);
        state_tv.setText(sm.getString("state"));
        //city_tv.setOnClickListener(this);
       // state_tv.setOnClickListener(this);
        titleTv.setText("Create Business Page");
        rv_doc = findViewById(R.id.rv_doc);
        rg = findViewById(R.id.doc_type_radio);
        root = findViewById(R.id.root);
        image_count_textview = findViewById(R.id.image_count_textview);
        doc_count_textview = findViewById(R.id.doc_count_textview);
        rv_doc.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        business_category = findViewById(R.id.business_category);
        occasionString = getResources().getStringArray(R.array.occasion);
        to_textview = findViewById(R.id.to_textview);
        from_textview = findViewById(R.id.from_textview);
        doc_textview = findViewById(R.id.doc_textview);
        doc_textview.setOnClickListener(this);
        doc_rl = findViewById(R.id.doc_rl);
        business_category.setOnClickListener(this);
        offer_text_rl = findViewById(R.id.offer_text_rl);
        full_screen_imageview = findViewById(R.id.full_screen_imageview);
        radio_all_day_open = findViewById(R.id.radio_all_day_open);
        upload_options_rl = findViewById(R.id.upload_options_rl);
        radio_weekly_off = findViewById(R.id.radio_weekly_off);
        day_textview = findViewById(R.id.day_textview);
        piblish_textview = findViewById(R.id.piblish_textview);
        upload_doc_rl = findViewById(R.id.upload_doc_rl);
        cancle_tv = findViewById(R.id.cancle_tv);
        image_layout = findViewById(R.id.image_layout);
        add_imageview1 = findViewById(R.id.add_imageview1);
        radio_rate = findViewById(R.id.radio_rate);
        radio_tarrif = findViewById(R.id.radio_tarrif);
        radio_others = findViewById(R.id.radio_others);
        radio_menu = findViewById(R.id.radio_menu);
        weekly_off_relativelayout = findViewById(R.id.weekly_off_relativelayout);
        business_name = findViewById(R.id.business_name);
        business_tagline = findViewById(R.id.business_tagline);
        business_desc = findViewById(R.id.business_desc);
        add1_et = findViewById(R.id.add1_et);
        add2_et = findViewById(R.id.add2_et);
       // add2_et.setText(sm.getString("addlinetwo"));
        pin_et = findViewById(R.id.pin_et);
        pin_et.setText(sm.getString("pincode"));
        pin_et.setEnabled(false);
        web_et = findViewById(R.id.web_et);
        email_et = findViewById(R.id.email_et);
        mobile_et = findViewById(R.id.mobile_et);
        tel_et = findViewById(R.id.tel_et);
        choose_photo = findViewById(R.id.choose_photo);
        take_photo = findViewById(R.id.take_photo);
        make_video = findViewById(R.id.make_video);
        choose_video = findViewById(R.id.choose_video);
        view_weekly = findViewById(R.id.view_weekly);
        image_textview = findViewById(R.id.image_textview);
        offer_add_imageview = findViewById(R.id.offer_add_imageview);
        upload_doc_imageview = findViewById(R.id.upload_doc_imageview);
        upload_doc_imageview.setOnClickListener(this);
        image_textview.setOnClickListener(this);
        take_photo.setOnClickListener(this);
        choose_photo.setOnClickListener(this);
        choose_video.setOnClickListener(this);
        make_video.setOnClickListener(this);
        add_imageview1.setOnClickListener(this);
        weekly_off_relativelayout.setOnClickListener(this);
        upload_doc_rl.setOnClickListener(this);
        piblish_textview.setOnClickListener(this);
        day_textview.setOnClickListener(this);
        radio_weekly_off.setOnClickListener(this);
        radio_all_day_open.setOnClickListener(this);
        from_textview.setOnClickListener(this);
        to_textview.setOnClickListener(this);
        upload_options_rl.setOnClickListener(this);
        cancle_tv.setOnClickListener(this);
        pdfView = findViewById(R.id.pdfView);
        pdf_rl = findViewById(R.id.pdf_rl);
        offer_rl = findViewById(R.id.offer_rl);
        offer_rl.setOnClickListener(this);
        pdf_hide_imageview = findViewById(R.id.pdf_hide_imageview);
        pdf_hide_imageview.setOnClickListener(this);
        Bundle extras = getIntent().getExtras();

        business_page_type = extras.getString("business_page_type");
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);

        if (business_page_type.equals("edit")) {
            progressDialog.show();
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    // code to run after delay
                    businessDetailApi(Integer.parseInt(extras.getString("business_id")));
                }
            }, 6000);
        }
        mArrayUri = new ArrayList<Uri>();
    }

    private void countryApi() {
        APIInterface service = APIClient.getRetrofit().create(APIInterface.class);
        Call<StatePojo> call = service.countryList("country");
        call.enqueue(new Callback<StatePojo>() {
            @Override
            public void onResponse(Call<StatePojo> call, Response<StatePojo> response) {
                countryList = response.body().getNbdata();
            }

            @Override
            public void onFailure(Call<StatePojo> call, Throwable t) {
                Toast.makeText(CreateBusinessPageActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("res", t.getMessage());
            }
        });
    }

    private void stateApi() {
        List<Integer> list = new ArrayList<>();
        list.add(100);
        APIInterface service = APIClient.getRetrofit().create(APIInterface.class);
        Call<StatePojo> call = service.stateList("state", list);
        call.enqueue(new Callback<StatePojo>() {
            @Override
            public void onResponse(Call<StatePojo> call, Response<StatePojo> response) {
                statelist = response.body().getNbdata();
            }

            @Override
            public void onFailure(Call<StatePojo> call, Throwable t) {
                Toast.makeText(CreateBusinessPageActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("res", t.getMessage());
            }
        });

    }

    private void cityApi(int id) {
        int stateId = id;
        List<Integer> list = new ArrayList<>();
        list.add(id);
        APIInterface service = APIClient.getRetrofit().create(APIInterface.class);
        Call<StatePojo> call = service.city("city", list);
        call.enqueue(new Callback<StatePojo>() {
            @Override
            public void onResponse(Call<StatePojo> call, Response<StatePojo> response) {
                citylist = response.body().getNbdata();
            }

            @Override
            public void onFailure(Call<StatePojo> call, Throwable t) {
                Toast.makeText(CreateBusinessPageActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("res", t.getMessage());
            }
        });

    }

    private void categoryApi() {
        APIInterface service = APIClient.getRetrofit().create(APIInterface.class);
        Call<StatePojo> call = service.businessCategoryList("businesstype");
        call.enqueue(new Callback<StatePojo>() {
            @Override
            public void onResponse(Call<StatePojo> call, Response<StatePojo> response) {
                categorylist = response.body().getNbdata();
                maxImageCount = Integer.parseInt(response.body().getBusinessImgLimit());
                maxImageCountDoc = Integer.parseInt(response.body().getBusinessDocLimit());
                totalImageCount = Integer.parseInt(response.body().getBusinessImgLimit());
                totalVideoCount = Integer.parseInt(response.body().getBusinessVideoLimit());
                postVideoSize = Integer.parseInt(response.body().getBusinessVideoSize());

                itemCount = totalImageCount + totalVideoCount;
                image_count_textview.setText("Max : " + totalImageCount + " Images " + totalVideoCount + " Video");
                doc_count_textview.setText("Max documents : " + response.body().getBusinessDocLimit());
            }

            @Override
            public void onFailure(Call<StatePojo> call, Throwable t) {
                Toast.makeText(CreateBusinessPageActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("res---", t.getMessage());
            }
        });
    }

    private void listOfCountrt(List<Nbdatum> ls, String country) {
        RecyclerView rv;
        TextView confirm;
        ImageView cancel;

        country_dialog = new Dialog(this);
        country_dialog.setContentView(R.layout.country_dialog);
        search_et = country_dialog.findViewById(R.id.search_et);
        search_et.requestFocus();
        imm.showSoftInput(search_et, InputMethodManager.SHOW_IMPLICIT);
        EditText finalSearch_et = search_et;
        search_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                finalSearch_et.setFocusable(true);
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                //after the change calling the method and passing the search input
                filter(editable.toString(), country);

            }
        });

        rv = country_dialog.findViewById(R.id.rv);
        search_et = country_dialog.findViewById(R.id.search_et);
        rv.setLayoutManager(new LinearLayoutManager(this));
        if (country.equals("country")) {
            countryAdapter = new CountryAdapter(ls, "country", this);
            rv.setAdapter(countryAdapter);
        }
        if (country.equals("state")) {
            countryAdapter = new CountryAdapter(ls, "state", this);
            rv.setAdapter(countryAdapter);
        }
        if (country.equals("city")) {
            countryAdapter = new CountryAdapter(ls, "city", this);
            rv.setAdapter(countryAdapter);
        }

        country_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(CreateBusinessPageActivity.this, android.R.color.transparent)));
        country_dialog.setCancelable(true);
        country_dialog.show();
    }

    private void filter(String text, String country) {
        ArrayList<Nbdatum> filteredlist = new ArrayList<Nbdatum>();
        if (country.equals("country")) {
            for (Nbdatum item : countryList) {
                if (item.getCountryname().toLowerCase().contains(text.toLowerCase())) {
                    filteredlist.add(item);
                }
            }
        }
        if (country.equals("state")) {
            for (Nbdatum item : statelist) {
                if (item.getStateName().toLowerCase().contains(text.toLowerCase())) {
                    filteredlist.add(item);
                }
            }
        }
        if (country.equals("city")) {
            for (Nbdatum item : citylist) {
                if (item.getCity_name().toLowerCase().contains(text.toLowerCase())) {
                    filteredlist.add(item);
                }
            }
        }
        if (filteredlist.isEmpty()) {
            Toast.makeText(this, "No Data Found..", Toast.LENGTH_SHORT).show();
        } else {
            countryAdapter.filterList(filteredlist);
        }
    }

    @Override
    public void onSelected(int position, String name, String id, String type) {
        if (type.equals("country")) {
            country_dialog.dismiss();
            country_tv.setText(name);
            countryName = name;
            countryid = Integer.parseInt(id);
        }
        if (type.equals("state")) {
            country_dialog.dismiss();
            state_tv.setText(name);
            stateName = name;
            stateid = Integer.parseInt(id);
            cityApi(Integer.parseInt(id));
        }
        if (type.equals("city")) {
            country_dialog.dismiss();
            city_tv.setText(name);
            cityName = name;
            cityId = name;
        }
    }

    @Override
    public void onDocCrossClick(int pos) {
        mArrayUri1.remove(pos);
        imageList1.remove(pos);
        docImageAdapter.notifyDataSetChanged();
        if (mArrayUri1.size() == 1) {
            doc_textview.setVisibility(View.VISIBLE);
            doc_textview.setText(mArrayUri1.size() + " Image");
        }
        if (mArrayUri1.size() >= 1) {
            doc_textview.setVisibility(View.VISIBLE);
            doc_textview.setText(mArrayUri1.size() + " Images");
        }
        if (mArrayUri1.size() == 0) {
            doc_dialog.dismiss();
            doc_textview.setVisibility(View.GONE);
        }
    }

    public interface Router {
        void businessSuccess(String msg);
    }

    private class SimulateTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            if (CheckAllFields()) {
                apiCreateBusiness();
            }
            SystemClock.sleep(5000);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Intent intent = new Intent(CreateBusinessPageActivity.this, BusinessActivity.class);
            intent.putExtra("title", "createbusiness");
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            progressDialog.dismiss();
        }
    }

    private class SimulateTask1 extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {

            apiCreateBusiness1();
            SystemClock.sleep(5000);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if (apiStatus.equals("success")) {
                Intent i = new Intent(CreateBusinessPageActivity.this, BusinessActivity.class);
                i.putExtra("title", "createbusiness");
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
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

    private String generateFileName(String extension) {
        return System.currentTimeMillis() + "." + extension;
    }


    private class DownloadTask extends AsyncTask<String, Void, Uri> {

        private String downloadedFilePath;
        private String fileType;
        private String sourceType; // <-- new field

        public DownloadTask(String sourceType) {
            this.sourceType = sourceType;
        }

        @Override
        protected Uri doInBackground(String... urls) {
            String mediaUrl = urls[0];
            String fileName;

            if (mediaUrl.endsWith(".mp4")) {
                fileName = generateFileName("mp4");
                fileType = "video";
            } else if (mediaUrl.endsWith(".jpg") || mediaUrl.endsWith(".png")) {
                fileName = generateFileName("jpg");
                fileType = "image";
            } else if (mediaUrl.endsWith(".pdf")) {
                fileName = generateFileName("pdf");
                fileType = "pdf";
            } else {
                return null;
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                return downloadToMediaStore(mediaUrl, fileName);
            } else {
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

                if ("image".equals(fileType)) {
                    values.put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg");
                    values.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS);
                } else if ("video".equals(fileType)) {
                    values.put(MediaStore.MediaColumns.MIME_TYPE, "video/mp4");
                    values.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS);
                } else if ("pdf".equals(fileType)) {
                    values.put(MediaStore.MediaColumns.MIME_TYPE, "application/pdf");
                    values.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS);
                }

                Uri mediaUri = getContentResolver().insert(MediaStore.Files.getContentUri("external"), values);
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
                if ("image".equals(fileType)) {
                    if ("image".equals(sourceType)) {
                        imageUris.add(mediaUri);
                        try {
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), mediaUri);
                            mArrayGalleryPhoto.add(bitmap);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        updateImageTextView();
                    } else if ("document".equals(sourceType)) {
                        mArrayUri1.add(mediaUri);
                        updatePdfTextView();
                    }
                } else if ("video".equals(fileType)) {
                    videoUris.add(mediaUri);
                    updateVideoTextView();
                } else if ("pdf".equals(fileType)) {
                    mArrayUri1.add(mediaUri);
                    updatePdfTextView();
                }

                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q && downloadedFilePath != null) {
                    File file = new File(downloadedFilePath);
                    if (file.exists()) file.delete();
                }
            }
        }
    }


//    private class DownloadTask extends AsyncTask<String, Void, Uri> {
//
//        private String downloadedFilePath;
//        private boolean isImage = false;
//
//        @Override
//        protected Uri doInBackground(String... urls) {
//            String mediaUrl = urls[0];
//            String fileName;
//
//            if (mediaUrl.endsWith(".mp4")) {
//                fileName = generateFileName("mp4");
//            } else if (mediaUrl.endsWith(".jpg") || mediaUrl.endsWith(".png")) {
//                fileName = generateFileName("jpg");
//                isImage = true;
//            } else {
//                return null; // Unsupported file type
//            }
//
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//                // Android 10 and above: Use MediaStore
//                return downloadToMediaStore(mediaUrl, fileName);
//            } else {
//                // Android 9 and below: Use legacy storage
//                return downloadToLegacyStorage(mediaUrl, fileName);
//            }
//        }
//
//        private Uri downloadToMediaStore(String mediaUrl, String fileName) {
//            try {
//                URL url = new URL(mediaUrl);
//                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//                connection.connect();
//
//                if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
//                    return null;
//                }
//
//                ContentValues values = new ContentValues();
//                values.put(MediaStore.MediaColumns.DISPLAY_NAME, fileName);
//
//                if (isImage) {
//                    values.put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg");
//                    values.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES);
//                } else {
//                    values.put(MediaStore.MediaColumns.MIME_TYPE, "video/mp4");
//                    values.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_MOVIES);
//                }
//
//                Uri mediaUri = getContentResolver().insert(isImage ? MediaStore.Images.Media.EXTERNAL_CONTENT_URI : MediaStore.Video.Media.EXTERNAL_CONTENT_URI, values);
//                if (mediaUri == null) {
//                    return null;
//                }
//
//                InputStream inputStream = new BufferedInputStream(connection.getInputStream());
//                OutputStream outputStream = getContentResolver().openOutputStream(mediaUri);
//
//                byte[] data = new byte[4096];
//                int count;
//                while ((count = inputStream.read(data)) != -1) {
//                    outputStream.write(data, 0, count);
//                }
//
//                outputStream.flush();
//                outputStream.close();
//                inputStream.close();
//                return mediaUri;
//            } catch (Exception e) {
//                Log.e("DownloadTask", "Error: " + e.getMessage());
//                return null;
//            }
//        }
//
//
//        private Uri downloadToLegacyStorage(String mediaUrl, String fileName) {
//            downloadedFilePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/" + fileName;
//
//            try {
//                URL url = new URL(mediaUrl);
//                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//                connection.connect();
//
//                if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
//                    return null;
//                }
//
//                InputStream inputStream = new BufferedInputStream(connection.getInputStream());
//                FileOutputStream outputStream = new FileOutputStream(new File(downloadedFilePath));
//
//                byte[] data = new byte[4096];
//                int count;
//                while ((count = inputStream.read(data)) != -1) {
//                    outputStream.write(data, 0, count);
//                }
//
//                outputStream.flush();
//                outputStream.close();
//                inputStream.close();
//                return Uri.fromFile(new File(downloadedFilePath));
//            } catch (Exception e) {
//                Log.e("DownloadTask", "Error: " + e.getMessage());
//                return null;
//            }
//        }
//
//        @Override
//        protected void onPostExecute(Uri mediaUri) {
//            if (mediaUri != null) {
//                if (isImage) {
//                    imageUris.add(mediaUri);
//                    try {
//                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(CreateBusinessPageActivity.this.getContentResolver(), mediaUri);
//                        mArrayGalleryPhoto.add(bitmap);
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                    updateImageTextView();
////                    Toast.makeText(CreateBusinessPageActivity.this, "Image downloaded and added to list: " + mediaUri.toString(), Toast.LENGTH_LONG).show();
//                } else {
//                    videoUris.add(mediaUri);
//                    updateVideoTextView();
////                    Toast.makeText(CreateBusinessPageActivity.this, "Video downloaded and added to list: " + mediaUri.toString(), Toast.LENGTH_LONG).show();
//                }
//
//                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q && downloadedFilePath != null) {
//                    File file = new File(downloadedFilePath);
//                    if (file.exists() && file.delete()) {
//                        Toast.makeText(CreateBusinessPageActivity.this, "File deleted from local storage", Toast.LENGTH_SHORT).show();
//                    } else {
//                        Toast.makeText(CreateBusinessPageActivity.this, "Failed to delete file", Toast.LENGTH_SHORT).show();
//                    }
//                }
//            } else {
//                Toast.makeText(CreateBusinessPageActivity.this, "Download failed", Toast.LENGTH_LONG).show();
//            }
//        }
//    }



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
                hideKeyboard1();
                if (mArrayUri.size() < itemCount) {
                    upload_options_rl.setVisibility(View.VISIBLE);
                } else {
                    upload_options_rl.setVisibility(View.GONE);
                    GlobalMethods.getInstance(CreateBusinessPageActivity.this).globalDialog(CreateBusinessPageActivity.this, "You can only add up to " + itemCount + " media items (" + totalVideoCount + " video and " + totalImageCount + " images)");
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
