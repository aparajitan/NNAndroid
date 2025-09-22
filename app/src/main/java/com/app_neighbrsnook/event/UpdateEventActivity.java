package com.app_neighbrsnook.event;

import static android.view.View.VISIBLE;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.app_neighbrsnook.R;
import com.app_neighbrsnook.adapter.ImageUploadAdapter1;
import com.app_neighbrsnook.adapter.VideoUploadAdapter;
import com.app_neighbrsnook.apiService.ApiExecutor;
import com.app_neighbrsnook.apiService.UrlClass;
import com.app_neighbrsnook.database.DAO;
import com.app_neighbrsnook.database.Database;
import com.app_neighbrsnook.login.AddressResponse;
import com.app_neighbrsnook.model.CreateEventModule;
import com.app_neighbrsnook.model.ImagePOJO;
import com.app_neighbrsnook.network.APIClient;
import com.app_neighbrsnook.network.APIInterface;
import com.app_neighbrsnook.pojo.EventDetailsPojo;
import com.app_neighbrsnook.utils.GlobalMethods;
import com.app_neighbrsnook.utils.PrefMananger;
import com.app_neighbrsnook.utils.SharedPrefsManager;
import com.app_neighbrsnook.utils.UtilityFunction;
import com.squareup.picasso.Picasso;
import com.app_neighbrsnook.libraries.cropper.CropImage;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicReference;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateEventActivity extends AppCompatActivity implements ImageUploadAdapter1.ImageItemClick, VideoUploadAdapter.ImageItemClick {
    TextView tv_start_date_event, tv_end_date_event;
    ImageView img_start_date_event, img_end_date_event;
    Calendar calendar_start, calendar_end;
    FrameLayout frm_start_date, frm_event_end, frm_post;
    ImageView img_back;
    TextView tv_cancel;
    String st_title, st_about, st_endEvent, st_username, st_startDate, st_endDate, st_startTime, st_endTime, st_attendees, st_non_attds, st_total_likes, st_address1, st_address2, st_tym_ngh, type;

    RelativeLayout rl_upload_layout;
    RelativeLayout rl_parent_layout;
    FrameLayout frm_upload_image;
    String currentPhotoPath;
    private final int TAKE_PIC_FROM_CAMERA = 4;

    Context context;
    TextView tv_take, tv_choose_photo, updateEvent;
    Activity activity;
    RecyclerView photo_recycler_view;
    ArrayList<ImagePOJO> bitmapList = new ArrayList<>();
    VideoUploadAdapter uploadAdapter;
    EditText edt_title, et_description, edt_address_two, edt_address_one;
    CreateEventModule createEventModule;
    TextView tv_start_time, tv_end_time;
    ImageView img_event_pic;
    String openBusiness, opentime, closeTime, weekoff;
    int updateOpenHrs = 10, updateOpenMns = 00, updateCloseHrs = 19, updateCloseMns = 00;
    ProgressDialog progressDialog;
    SharedPrefsManager sm;
    int id;
    private String existingCoverImage; // Store the fetched image URL

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = activity = this;
        sm = new SharedPrefsManager(this);
        setContentView(R.layout.activity_create_event);

        Bundle intent = getIntent().getExtras();
        assert intent != null;
        id = intent.getInt("id", 0);
        calendar_start = Calendar.getInstance();
        edt_title = findViewById(R.id.edt_title);
        updateEvent = findViewById(R.id.updateEvent);
        calendar_end = Calendar.getInstance();
        photo_recycler_view = findViewById(R.id.photos_recycler_view);
        edt_address_one = findViewById(R.id.edt_address_one);
        edt_address_two = findViewById(R.id.edt_address_two);
        frm_start_date = findViewById(R.id.frm_stard_date);
        img_event_pic = findViewById(R.id.img_create_event);
        frm_post = findViewById(R.id.upload_id_event);
        frm_event_end = findViewById(R.id.frm_end_event);
        tv_start_date_event = findViewById(R.id.tv_event_start_date);
        tv_end_date_event = findViewById(R.id.tv_event_end_date);
        img_start_date_event = findViewById(R.id.img_event_start_date);
        img_end_date_event = findViewById(R.id.img_event_end_date);
        img_back = findViewById(R.id.img_back);
        frm_upload_image = findViewById(R.id.group_crt_upload_image_frm);
        rl_upload_layout = findViewById(R.id.upload_options_rl);
        rl_parent_layout = findViewById(R.id.rl_parent_layout);
        tv_cancel = findViewById(R.id.cancle_tv);
        tv_take = findViewById(R.id.take_photo);
        tv_choose_photo = findViewById(R.id.choose_photo);
        tv_start_time = findViewById(R.id.start_time);
        tv_end_time = findViewById(R.id.end_time);
        frm_post.setOnClickListener(this::onClick);
        et_description = findViewById(R.id.edt_description);
        uploadAdapter = new VideoUploadAdapter(this, bitmapList, this);
        photo_recycler_view.setLayoutManager(new LinearLayoutManager(context, RecyclerView.VERTICAL, false));
        photo_recycler_view.setAdapter(uploadAdapter);
        progressDialog = new ProgressDialog(context);
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
        updateEvent.setText("Update Event");
     //   GlobalMethods.checkPermission(this);
        eventDetailsShowApi(id);

        tv_start_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timePicker(tv_start_time, "opentime", updateOpenHrs, updateOpenMns);
                String time = tv_start_time.getText().toString();

                // GlobalMethods.timePicker(context,tv_start_time);
            }
        });
        tv_end_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // GlobalMethods.timePicker(context, tv_end_time);
                timePicker(tv_end_time, "opentime", updateCloseHrs, updateCloseMns);
                String time = tv_end_time.getText().toString();

            }
        });
        rl_upload_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rl_upload_layout.setVisibility(View.GONE);
            }
        });
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rl_upload_layout.setVisibility(View.GONE);
            }
        });
        frm_upload_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ( GlobalMethods.checkCameraAndGalleryPermission(UpdateEventActivity.this)) {
                    if (bitmapList.size() < 1) {
                        CLICK_ON = FIRST_IMAGE;
                        rl_upload_layout.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
        tv_take.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                capturePhoto();
                CLICK_ON = FIRST_IMAGE;
                rl_upload_layout.setVisibility(View.GONE);
            }
        });
        tv_choose_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CLICK_ON = FIRST_IMAGE;
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 2);
                rl_upload_layout.setVisibility(View.GONE);
            }
        });
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        frm_start_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calenderPicker1();

            }
        });
        frm_event_end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                calenderPicker2();
            }
        });
        DatePickerDialog.OnDateSetListener eventend = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar_end.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                calendar_end.set(Calendar.MONTH, month);
                calendar_end.set(Calendar.YEAR, year);
                Enddate();
            }
        };
    }

    public void onClick(View view) {
        if (view.getId() == R.id.upload_id_event && CheckAllFields()) {
            updateEvent();
        }
    }

    Calendar calendar1;
    Calendar calendar2;

    public void calenderPicker1() {
        calendar1 = Calendar.getInstance();
        int mYear = calendar1.get(Calendar.YEAR);
        int mMonth = calendar1.get(Calendar.MONTH);
        int mDay = calendar1.get(Calendar.DAY_OF_MONTH);
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        DatePickerDialog dpd = new DatePickerDialog(context, R.style.MyTimePickerDialogStyle,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        calendar1.set(year, monthOfYear, dayOfMonth);
                        tv_start_date_event.setText(df.format(calendar1.getTime()));

                    }
                }, mYear, mMonth, mDay);
        dpd.getDatePicker().setMinDate(System.currentTimeMillis());
        if (tv_end_date_event.getText().toString().isEmpty() || calendar2 == null) {
            // dpd.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        } else {
            dpd.getDatePicker().setMinDate(calendar1.getTimeInMillis());
        }

        dpd.getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dpd.show();
    }

    public void calenderPicker2() {
        calendar2 = Calendar.getInstance();
        int mYear = calendar2.get(Calendar.YEAR);
        int mMonth = calendar2.get(Calendar.MONTH);
        int mDay = calendar2.get(Calendar.DAY_OF_MONTH);
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        DatePickerDialog dpd = new DatePickerDialog(context, R.style.MyTimePickerDialogStyle,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        calendar2.set(year, monthOfYear, dayOfMonth);
                        tv_end_date_event.setText(df.format(calendar2.getTime()));

                    }
                }, mYear, mMonth, mDay);
        if (tv_start_date_event.getText().toString().isEmpty() || calendar1 == null) {
            dpd.getDatePicker().setMinDate(System.currentTimeMillis());
        } else {
            dpd.getDatePicker().setMinDate(calendar1.getTimeInMillis());
        }

        dpd.getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dpd.show();
    }

    private class InsertCreateEvent extends AsyncTask<CreateEventModule, Void, Void>
            implements com.app_neighbrsnook.event.InsertCreateEvent {
        DAO dao = Database.createDBInstance(UpdateEventActivity.this).getDao();

        @Override
        protected Void doInBackground(CreateEventModule... investers) {
            dao.insertEvent(investers[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            onBackPressed();
        }
    }

    public void Enddate() {
        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        tv_end_date_event.setText(sdf.format(calendar_end.getTime()));
    }

    String CLICK_ON = "";
    String FIRST_IMAGE = "FirstImage";
    String currentPath = "";
    Uri imageUri;

//    public File bitmapToFile(Bitmap bitmap, String fileNameToSave) {
//        File file = null;
//        try {
//            file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + File.separator + fileNameToSave);
//            file.createNewFile();
//            ByteArrayOutputStream bos = new ByteArrayOutputStream();
//            bitmap.compress(Bitmap.CompressFormat.PNG, 0, bos);
//            byte[] bitmapdata = bos.toByteArray();
//            FileOutputStream fos = new FileOutputStream(file);
//            fos.write(bitmapdata);
//            fos.flush();
//            fos.close();
//            return file;
//        } catch (Exception e) {
//            e.printStackTrace();
//            return file;
//        }
//    }

    Bitmap bitmap;
    Uri filePath;
    Bitmap bitmap1;
    Uri filePath1;
    private static final int PIC_CROP_REQUEST = 2;

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                Log.e("1258963", bitmap + "");
                filePath = imageUri;
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                bitmap1 = bitmap;
                img_event_pic.setImageBitmap(bitmap);
                img_event_pic.setVisibility(View.VISIBLE);
                performCrop(filePath);
                img_event_pic.setImageBitmap(bitmap);
                if (CLICK_ON.equals(FIRST_IMAGE)) {
                    bitmap1 = bitmap;
                    img_event_pic.setImageBitmap(bitmap);
                }
            } else if (requestCode == PIC_CROP_REQUEST && data != null) {
                filePath = data.getData();
                performCrop(filePath);
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), filePath);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (CLICK_ON.equals(FIRST_IMAGE)) {
                    bitmap1 = bitmap;
                    filePath1 = filePath;
                    img_event_pic.setImageBitmap(bitmap);
                    img_event_pic.setVisibility(View.VISIBLE);
                    ImagePOJO imagePOJO = new ImagePOJO();
                    imagePOJO.bitmap = bitmap;
                    imagePOJO.imageUri = filePath;
                }
            } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                if (resultCode == RESULT_OK) {
                    Uri resultUri = result.getUri();
                    Bitmap photo1;
                    try {
                        photo1 = MediaStore.Images.Media.getBitmap(context.getContentResolver(), resultUri);
                        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                        photo1.compress(Bitmap.CompressFormat.JPEG, 5, bytes);
                        byte[] byteArrayImage = bytes.toByteArray();
                        filePath = resultUri;
                        bitmap = photo1;
                        if (CLICK_ON.equals(FIRST_IMAGE)) {
                            bitmap1 = bitmap;
                            img_event_pic.setImageBitmap(bitmap);
                        }
                        String imageurl = MediaStore.Images.Media.insertImage(context.getContentResolver(), photo1, getString(R.string.app_name) + "_" + System.currentTimeMillis(), null);
                        //Log.e("captured uri", imagepath);
                        String[] projection = {MediaStore.Images.Media.DATA};
                        Cursor cursor = context.getContentResolver().query(Uri.parse(imageurl), projection, null, null, null);
                        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                        cursor.moveToFirst();
                        imageurl = cursor.getString(column_index); //cursor.getString(column_index);
                        Log.e("imagepath ", "imagepath " + imageurl);
                        Log.e("captured uri", imageurl);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                    Exception error = result.getError();
                    error.printStackTrace();
                }
            }
        }
    }

    @Override
    public void removeImage(int position) {
        bitmapList.remove(position);
        uploadAdapter.notifyDataSetChanged();
    }

    private void performCrop(Uri fileUri) {
        try {
            CropImage.ActivityBuilder a1 = CropImage.activity(fileUri);
            a1.setCropMenuCropButtonTitle("Set Image");
            a1.setAspectRatio(1, 1);
            a1.setFixAspectRatio(false);

            a1.start(activity);
        } catch (ActivityNotFoundException anfe) {
            //display an error message
            String errorMessage = "Whoops - your device doesn't support the crop action!";
            Toast toast = Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    private boolean CheckAllFields() {
        if (edt_title.getText().toString().matches("")) {
            GlobalMethods.setError(edt_title, "Please enter    title");
            return false;
        } else if (tv_start_date_event.getText().toString().equals("Start date")) {
            Toast.makeText(UpdateEventActivity.this, "Please select start date", Toast.LENGTH_SHORT).show();
            return false;
        } else if (tv_end_date_event.getText().toString().equals("End date")) {
            Toast.makeText(UpdateEventActivity.this, "Please select end date", Toast.LENGTH_SHORT).show();
            return false;
        } else if (tv_start_time.getText().toString().equals("Start time")) {
            Toast.makeText(UpdateEventActivity.this, "Please select start time", Toast.LENGTH_SHORT).show();
            return false;
        } else if (tv_end_time.getText().toString().equals("End time")) {
            Toast.makeText(UpdateEventActivity.this, "Please select end time", Toast.LENGTH_SHORT).show();
            return false;
        } else if (et_description.getText().toString().matches("")) {
            GlobalMethods.setError(et_description, "Please enter description");
            return false;
        } else if (edt_address_one.length() == 0) {
            GlobalMethods.setError(edt_address_one, "Please enter address line 1");
            return false;
        }
        return true;
    }

    String tim;

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
        TimePickerDialog timePickerDialog = new TimePickerDialog(UpdateEventActivity.this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {
                        boolean isPM = (hourOfDay >= 12);
//                tv.setText();
                        tim = String.format("%02d:%02d %s", (hourOfDay == 12 || hourOfDay == 0) ? 12 : hourOfDay % 12, minute, isPM ? "pm" : "am");
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

    public void capturePhoto() {
        String fileName = System.currentTimeMillis() + "";
        File fileDirectory = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        try {
            File imageFile = File.createTempFile(fileName, ".jpg", fileDirectory);
            currentPath = imageFile.getAbsolutePath();
            imageUri = FileProvider.getUriForFile(this, "com.app_neighbrsnook.fileprovider", imageFile);
            Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            takePicture.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            startActivityForResult(takePicture, 1);//zero can be replaced with any action code (called
        } catch (IOException e) {
            e.printStackTrace();
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

    private void eventDetailsShowApi(int id) {
        ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Please wait...");
        dialog.setCancelable(false);
        dialog.show();

        HashMap<String, Object> hm = new HashMap<>();
        hm.put("userid", Integer.parseInt(sm.getString("user_id")));
        hm.put("eventid", id);

        APIInterface service = APIClient.getRetrofit().create(APIInterface.class);
        Call<EventDetailsPojo> call = service.eventDetails("viewalldataeventlistdetails", hm);
        call.enqueue(new Callback<EventDetailsPojo>() {
            @Override
            public void onResponse(Call<EventDetailsPojo> call, Response<EventDetailsPojo> response) {
                EventDetailsPojo rootObject = response.body();
                dialog.dismiss();

                try {
                    // Fetch existing image
                    existingCoverImage = rootObject.getCoverImage();

                    // Populate fields
                    edt_title.setText(rootObject.getTitle());
                    et_description.setText(rootObject.getEventDetail());
                    tv_start_date_event.setText(rootObject.getEventStartDate());
                    tv_end_date_event.setText(rootObject.getEventEndDate());
                    tv_start_time.setText(rootObject.getEventStarttime());
                    tv_end_time.setText(rootObject.getEventEndtime());
                    edt_address_one.setText(rootObject.getAddlineone());
                    edt_address_two.setText(rootObject.getAddlinetwo());

                    // Load image into ImageView
                    if (existingCoverImage != null && !existingCoverImage.isEmpty()) {
                        img_event_pic.setVisibility(VISIBLE);
                        Picasso.get().load(existingCoverImage).into(img_event_pic);
                    } else {
                        img_event_pic.setImageResource(R.drawable.marketplace_white_background);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<EventDetailsPojo> call, Throwable t) {
                Toast.makeText(UpdateEventActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
    }

    private void updateEvent() {
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Please wait...");
        dialog.setCancelable(false);
        dialog.show();

        try {
            AtomicReference<File> file1 = new AtomicReference<>();
            AtomicReference<MultipartBody.Part> eventPhoto = new AtomicReference<>();

            if (bitmap != null) {
                // Bitmap from gallery or camera
                file1.set(bitmapToFile(bitmap, getString(R.string.app_name) + System.currentTimeMillis() + ".jpg"));
            } else if (existingCoverImage != null && !existingCoverImage.isEmpty()) {
                // Download the image from URL
                new Thread(() -> {
                    try {
                        Bitmap downloadedBitmap = downloadBitmapFromUrl(existingCoverImage);
                        if (downloadedBitmap != null) {
                            file1.set(bitmapToFile(downloadedBitmap, getString(R.string.app_name) + "_cover_" + System.currentTimeMillis() + ".jpg"));

                            runOnUiThread(() -> {
                                if (file1.get() != null && file1.get().exists()) {
                                    // File creation successful
                                    RequestBody imagePart = RequestBody.create(MediaType.parse("image/jpeg"), file1.get());
                                    eventPhoto.set(MultipartBody.Part.createFormData("eventpic", file1.get().getName(), imagePart));
                                    proceedWithApiCall(eventPhoto.get(), dialog);
                                } else {
                                    Toast.makeText(UpdateEventActivity.this, "Failed to process image", Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                }
                            });
                        } else {
                            runOnUiThread(() -> {
                                Toast.makeText(UpdateEventActivity.this, "Failed to download image from URL", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            });
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        runOnUiThread(dialog::dismiss);
                    }
                }).start();
                return;
            }

            if (file1.get() != null && file1.get().exists()) {
                RequestBody imagePart = RequestBody.create(MediaType.parse("image/jpeg"), file1.get());
                eventPhoto.set(MultipartBody.Part.createFormData("eventpic", file1.get().getName(), imagePart));
            } else {
                Toast.makeText(this, "No image selected or processed", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                return;
            }

            proceedWithApiCall(eventPhoto.get(), dialog);

        } catch (Exception e) {
            e.printStackTrace();
            dialog.dismiss();
        }
    }

    private void proceedWithApiCall(MultipartBody.Part eventPhoto, ProgressDialog dialog) {
        HashMap<String, RequestBody> hashMap = new HashMap<>();
        hashMap.put("userid", RequestBody.create(MultipartBody.FORM, PrefMananger.GetLoginData(context).getId() + ""));
        hashMap.put("title", RequestBody.create(MultipartBody.FORM, edt_title.getText().toString()));
        hashMap.put("eventdate", RequestBody.create(MultipartBody.FORM, tv_start_date_event.getText().toString()));
        hashMap.put("eventenddate", RequestBody.create(MultipartBody.FORM, tv_end_date_event.getText().toString()));
        hashMap.put("eventstarttime", RequestBody.create(MultipartBody.FORM, tv_start_time.getText().toString()));
        hashMap.put("eventendtime", RequestBody.create(MultipartBody.FORM, tv_end_time.getText().toString()));
        hashMap.put("eventdetails", RequestBody.create(MultipartBody.FORM, et_description.getText().toString()));
        hashMap.put("addlineone", RequestBody.create(MultipartBody.FORM, edt_address_one.getText().toString()));
        hashMap.put("addlinetwo", RequestBody.create(MultipartBody.FORM, edt_address_two.getText().toString()));
        hashMap.put("datelong", RequestBody.create(MultipartBody.FORM, "05"));
        hashMap.put("e_id", RequestBody.create(MultipartBody.FORM, String.valueOf(id)));

        ApiExecutor.getApiService().eventcreat("updateevent", eventPhoto, hashMap).enqueue(new Callback<AddressResponse>() {
            @Override
            public void onResponse(Call<AddressResponse> call, Response<AddressResponse> response) {
                UtilityFunction.hideLoading();
                try {
                    if (response.body().getStatus().equals("success")) {
                        dialog.dismiss();
                        mailSendApi();
                        Log.d("response----", response.toString());
                        Intent i = new Intent(UpdateEventActivity.this, EventAllListCurrentData.class);
                        finish();
                        i.putExtra("neighbrhood", "drawar");
                        startActivity(i);

                        Toast.makeText(UpdateEventActivity.this, "Event updated succesful", Toast.LENGTH_SHORT).show();
                    } else if (response.body().getMessage() != null) {
                        Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<AddressResponse> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(UpdateEventActivity.this, "Something went wrong: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private Bitmap downloadBitmapFromUrl(String imageUrl) {
        Bitmap bitmap = null;
        try {
            URL url = new URL(imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            bitmap = BitmapFactory.decodeStream(input);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    private File bitmapToFile(Bitmap bitmap, String fileName) {
        File file = new File(getCacheDir(), fileName);
        try (FileOutputStream fos = new FileOutputStream(file)) {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (getCurrentFocus() != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
        return super.dispatchTouchEvent(ev);
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
                if (bitmapList.size() < 1) {
                    CLICK_ON = FIRST_IMAGE;
                    rl_upload_layout.setVisibility(View.VISIBLE);
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

