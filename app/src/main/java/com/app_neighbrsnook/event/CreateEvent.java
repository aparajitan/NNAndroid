package com.app_neighbrsnook.event;

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
import android.text.Editable;
import android.text.TextWatcher;
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
import com.app_neighbrsnook.abusive.BadWordFilter;
import com.app_neighbrsnook.adapter.ImageUploadAdapter1;
import com.app_neighbrsnook.adapter.VideoUploadAdapter;
import com.app_neighbrsnook.apiService.ApiExecutor;
import com.app_neighbrsnook.apiService.UrlClass;
import com.app_neighbrsnook.database.DAO;
import com.app_neighbrsnook.database.Database;
import com.app_neighbrsnook.group.CreateGroups;
import com.app_neighbrsnook.login.AddressResponse;
import com.app_neighbrsnook.model.CreateEventModule;
import com.app_neighbrsnook.model.ImagePOJO;
import com.app_neighbrsnook.pollModule.CreatePollActivity;
import com.app_neighbrsnook.utils.FileUtil;
import com.app_neighbrsnook.utils.GlobalMethods;
import com.app_neighbrsnook.utils.PrefMananger;
import com.app_neighbrsnook.utils.UtilityFunction;
import com.app_neighbrsnook.libraries.cropper.CropImage;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateEvent extends AppCompatActivity implements ImageUploadAdapter1.ImageItemClick, VideoUploadAdapter.ImageItemClick {
    TextView tv_start_date_event,tv_end_date_event;
    ImageView img_start_date_event,img_end_date_event;
    Calendar calendar_start,calendar_end;
    FrameLayout frm_start_date,frm_event_end,frm_post;
    ImageView img_back;
    TextView tv_cancel;
    RelativeLayout rl_upload_layout;
    RelativeLayout rl_parent_layout;
    FrameLayout frm_upload_image;
    String currentPhotoPath;
    private final int TAKE_PIC_FROM_CAMERA = 4;

    Context context;
    TextView tv_take,tv_choose_photo;
    Activity activity;
    RecyclerView photo_recycler_view;
    ArrayList<ImagePOJO> bitmapList = new ArrayList<>();
    VideoUploadAdapter uploadAdapter;
    EditText edt_title,et_description,edt_address_two,edt_address_one;
    CreateEventModule createEventModule;
    TextView tv_start_time,tv_end_time;
    ImageView img_event_pic;
    String openBusiness, opentime, closeTime, weekoff;
    int updateOpenHrs = 10, updateOpenMns = 00, updateCloseHrs = 19, updateCloseMns = 00;
    ProgressDialog progressDialog;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context=activity=this;
        setContentView(R.layout.activity_create_event);
        calendar_start = Calendar.getInstance();
        edt_title=findViewById(R.id.edt_title);
        calendar_end = Calendar.getInstance();
        photo_recycler_view=findViewById(R.id.photos_recycler_view);
        edt_address_one=findViewById(R.id.edt_address_one);
        edt_address_two=findViewById(R.id.edt_address_two);
        frm_start_date=findViewById(R.id.frm_stard_date);
        img_event_pic=findViewById(R.id.img_create_event);
        frm_post=findViewById(R.id.upload_id_event);
        frm_event_end=findViewById(R.id.frm_end_event);
        tv_start_date_event=findViewById(R.id.tv_event_start_date);
        tv_end_date_event=findViewById(R.id.tv_event_end_date);
        img_start_date_event=findViewById(R.id.img_event_start_date);
        img_end_date_event=findViewById(R.id.img_event_end_date);
        img_back=findViewById(R.id.img_back);
        frm_upload_image=findViewById(R.id.group_crt_upload_image_frm);
        rl_upload_layout=findViewById(R.id.upload_options_rl);
        rl_parent_layout=findViewById(R.id.rl_parent_layout);
        tv_cancel=findViewById(R.id.cancle_tv);
        tv_take=findViewById(R.id.take_photo);
        tv_choose_photo=findViewById(R.id.choose_photo);
        tv_start_time=findViewById(R.id.start_time);
        tv_end_time=findViewById(R.id.end_time);
        frm_post.setOnClickListener(this::onClick);
        et_description=findViewById(R.id.edt_description);
        uploadAdapter = new VideoUploadAdapter(this, bitmapList, this);
        photo_recycler_view.setLayoutManager(new LinearLayoutManager(context, RecyclerView.VERTICAL, false));
        photo_recycler_view.setAdapter(uploadAdapter);
        progressDialog = new ProgressDialog(context);
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);

        edt_title.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                String input = s.toString().trim();
                if (!input.isEmpty()) {
                    String[] words = input.split(" ");
                    StringBuilder titleCase = new StringBuilder();

                    for (int i = 0; i < words.length; i++) {
                        if (i == 1) { // second word
                            // keep first letter lowercase
                            titleCase.append(words[i].substring(0, 1).toLowerCase())
                                    .append(words[i].substring(1));
                        } else {
                            // capitalize first letter
                            titleCase.append(words[i].substring(0, 1).toUpperCase())
                                    .append(words[i].substring(1).toLowerCase());
                        }
                        if (i < words.length - 1) {
                            titleCase.append(" ");
                        }
                    }

                    edt_title.removeTextChangedListener(this);
                    edt_title.setText(titleCase.toString());
                    edt_title.setSelection(titleCase.length()); // cursor end me
                    edt_title.addTextChangedListener(this);
                }
            }
        });


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
            public void onClick(View view)  {
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
                if ( GlobalMethods.checkCameraAndGalleryPermission(CreateEvent.this)) {
                    if (bitmapList.size() < 1) {
                        CLICK_ON = FIRST_IMAGE;
                        rl_upload_layout.setVisibility(View.VISIBLE);
                    } else {
                        // Toast.makeText(context, "You can upload only 1 banner picture", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        tv_take.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                capturePhoto();
                CLICK_ON=FIRST_IMAGE;
                //performCrop(filePath);
                rl_upload_layout.setVisibility(View.GONE);
               // Toast.makeText(context, "You can Upload only 1 banner picture", Toast.LENGTH_SHORT).show();
            }
        });
        tv_choose_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CLICK_ON=FIRST_IMAGE;
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
//        ActionBar actionBar = getSupportActionBar();
//        actionBar.hide();
        DatePickerDialog.OnDateSetListener eventend = new DatePickerDialog.OnDateSetListener() {
            @Override
                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                            calendar_end.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                calendar_end.set(Calendar.MONTH, month);
                calendar_end.set(Calendar.YEAR, year);

                // datePickerDidaalog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                Enddate();
            }
        };
        frm_start_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {/*
                new DatePickerDialog(CreateEvent.this,eventstart, calendar_start.get(calendar_start.YEAR),
                        calendar_start.get(Calendar.MONTH), calendar_start.get(Calendar.DAY_OF_MONTH)).show();*/
              //  GlobalMethods.calenderPicker1(context, tv_start_date_event);
                calenderPicker1();

            }
        });
        frm_event_end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {/*
                new DatePickerDialog(CreateEvent.this,eventstart, calendar_start.get(calendar_start.YEAR),
                        calendar_start.get(Calendar.MONTH), calendar_start.get(Calendar.DAY_OF_MONTH)).show();*/
               // GlobalMethods.calenderPicker1(context, tv_end_date_event);
                calenderPicker2();
            }
        });

    }


        public void onClick(View view) {
            if (view.getId() == R.id.upload_id_event) {
                if (CheckAllFields()) {
                    createEvent();
                }
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
        // Launch Date Picker Dialog
        DatePickerDialog dpd = new DatePickerDialog(context,R.style.MyTimePickerDialogStyle,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        calendar1.set(year, monthOfYear, dayOfMonth);
                        tv_start_date_event.setText(df.format(calendar1.getTime()));

                    }
                }, mYear, mMonth, mDay);
        dpd.getDatePicker().setMinDate(System.currentTimeMillis());
        if (tv_end_date_event.getText().toString().isEmpty() || calendar2==null){
           // dpd.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        }else {
            dpd.getDatePicker().setMinDate(calendar1.getTimeInMillis());
        }

//        dpd.getWindow().setBackgroundDrawableResource(R.drawable.round_corner_dialog);
        dpd.getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dpd.show();
    }
    public void calenderPicker2() {

        calendar2 = Calendar.getInstance();

        int mYear = calendar2.get(Calendar.YEAR);
        int mMonth = calendar2.get(Calendar.MONTH);
        int mDay = calendar2.get(Calendar.DAY_OF_MONTH);
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        // Launch Date Picker Dialog
        DatePickerDialog dpd = new DatePickerDialog(context,R.style.MyTimePickerDialogStyle,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        calendar2.set(year, monthOfYear, dayOfMonth);
                        tv_end_date_event.setText(df.format(calendar2.getTime()));

                    }
                }, mYear, mMonth, mDay);
        if (tv_start_date_event.getText().toString().isEmpty() || calendar1==null){
            dpd.getDatePicker().setMinDate(System.currentTimeMillis());
        }else {
            dpd.getDatePicker().setMinDate(calendar1.getTimeInMillis());
        }

//        dpd.getWindow().setBackgroundDrawableResource(R.drawable.round_corner_dialog);
        dpd.getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dpd.show();
    }

    private class InsertCreateEvent extends AsyncTask<CreateEventModule, Void, Void>
            implements com.app_neighbrsnook.event.InsertCreateEvent {
        DAO dao = Database.createDBInstance(CreateEvent.this).getDao();
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
    String currentPath="";
    Uri imageUri;
    public File bitmapToFile(Bitmap bitmap, String fileNameToSave) { // File name like "image.png"
        //create a file to write bitmap data
        File file = null;
        try {
          /*  String path = Environment.getExternalStorageDirectory() + File.separator + getString(R.string.app_name);
            File directory = new File(path);*/

            file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + File.separator + fileNameToSave);
            file.createNewFile();
//Convert bitmap to byte array
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 0, bos); // YOU can also save it in JPEG
            byte[] bitmapdata = bos.toByteArray();
//write the bytes in file
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(bitmapdata);
            fos.flush();
            fos.close();
            return file;
        } catch (Exception e) {
            e.printStackTrace();
            return file; // it will return null
        }
    }
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

                //bitmap = (Bitmap) data.getExtras().get("data");
                Log.e("1258963", bitmap + "");
                //  File file1 = bitmapToFile(bitmap, getString(R.string.app_name) + "_" + System.currentTimeMillis() + ".jpg");
                filePath = imageUri;// Uri.fromFile(file1);
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
                    //updateImageWithoutLoader(bitmap1);
                    ImagePOJO imagePOJO = new ImagePOJO();
                    Log.e("1258963", bitmap + "");
                    imagePOJO.bitmap = bitmap;
                }
            }

            else if (requestCode == PIC_CROP_REQUEST && data != null) {
                filePath = data.getData();
                // File file1 = bitmapToFile(bitmap, getString(R.string.app_name) + "_" + System.currentTimeMillis() + ".jpg");
                // filePath = Uri.fromFile(file1);
                // bitmap1 = bitmap;
                performCrop(filePath);
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), filePath);
                    //iv_UploadImage.setImageBitmap(bitmap);
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
                    // bitmapList.add(imagePOJO);
                    // uploadAdapter.notifyDataSetChanged();
                }
            }

            else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                if (resultCode == RESULT_OK) {
                    Uri resultUri = result.getUri();
                    Bitmap photo1;
                    try {
                        photo1 = MediaStore.Images.Media.getBitmap(context.getContentResolver(), resultUri);
                        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                        photo1.compress(Bitmap.CompressFormat.JPEG, 5, bytes);
                        byte[] byteArrayImage = bytes.toByteArray();
                        // encodedImage = Base64.encodeToString(byteArrayImage, Base64.DEFAULT);
                        filePath = resultUri;
                        bitmap = photo1;
                        // binding.ivUser.setImageBitmap(photo1);

                        if (CLICK_ON.equals(FIRST_IMAGE)) {
                            bitmap1 = bitmap;
                            img_event_pic.setImageBitmap(bitmap);

                            //   imagePOJO.imageUri = filePath;
                            //  bitmapList.add(imagePOJO);
                            // uploadAdapter.notifyDataSetChanged();
                        }
                        // ProfileFragment.iv_profile.setImageBitmap(photo1);
                        String imageurl = MediaStore.Images.Media.insertImage(context.getContentResolver(), photo1, getString(R.string.app_name) + "_" + System.currentTimeMillis(), null);
                        //Log.e("captured uri", imagepath);
                        String[] projection = {MediaStore.Images.Media.DATA};
                        Cursor cursor = context.getContentResolver().query(Uri.parse(imageurl), projection, null, null, null);
                        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                        cursor.moveToFirst();
                        imageurl = cursor.getString(column_index);//cursor.getString(column_index);
                        Log.e("imagepath ", "imagepath " + imageurl);
                        Log.e("captured uri", imageurl);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
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

    private void createEvent() {
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Please wait...");
        dialog.setCancelable(false);
        dialog.show();
        try {
            File file1;
            int compressionRatio = 25; //1 == originalImage, 2 = 50% compression, 4=25% compress
            MultipartBody.Part  aadharFront=null;
            if (filePath != null) {
                file1 = FileUtil.from(this, filePath);
                try {
                    //Bitmap bitmap = BitmapFactory.decodeFile (file1.getPath ());
                    //bitmap.compress (Bitmap.CompressFormat.JPEG, compressionRatio, new FileOutputStream(file1));
                } catch (Throwable t) {
                    Log.e("ERROR", "Error compressing file." + t.toString());
                    t.printStackTrace();
                }
                   /* if (videoUri!=null && !videoSavingPath.isEmpty()){
                        file1=new File(videoUri.getPath());
                    }*/
                RequestBody videoPart = null;
                try {
                    Bitmap bmp = BitmapFactory.decodeFile(file1.getAbsolutePath());
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    bmp.compress(Bitmap.CompressFormat.JPEG, 20, bos);
                    Log.e("imageSize", bos.toByteArray().length + " Byte \n" + (bos.toByteArray().length / 1024) + "KB");
                    videoPart = RequestBody.create(MediaType.parse(getContentResolver().getType(filePath)), bos.toByteArray());
                    //  RequestBody.create(MEDIA_TYPE_PNG, bos.toByteArray());

                    Log.e("Name", file1.getName());
                    Log.e("Type", file1.getAbsolutePath());
                } catch (Exception e) {
                    e.printStackTrace();
                    videoPart = RequestBody.create(MediaType.parse(".jpg"), file1);
                }
                aadharFront = MultipartBody.Part.createFormData("eventpic", file1.getName(), videoPart);
            } else if (bitmap!=null){
                file1 = bitmapToFile(bitmap, getString(R.string.app_name) + System.currentTimeMillis() + ".jpg");
                //String realpath = getRealPathFromURI_API19(AddQoute.this, imageRequests.get(i).uri);
                // file1=new File(realpath);
                try {
                    Bitmap bitmap = BitmapFactory.decodeFile(file1.getPath());
                    bitmap.compress(Bitmap.CompressFormat.JPEG, compressionRatio, new FileOutputStream(file1));
                } catch (Throwable t) {
                    Log.e("ERROR", "Error compressing file." + t);
                    t.printStackTrace();
                }
                RequestBody videoPart = RequestBody.create(MediaType.parse(".jpg"), file1);
                if (file1.getName() == null) {

                } else {
                    aadharFront = MultipartBody.Part.createFormData("eventpic", file1.getName(), videoPart);
                }
            }
            HashMap<String, RequestBody> hashMap = new HashMap<>();
            hashMap.put("userid", RequestBody.create(MultipartBody.FORM, PrefMananger.GetLoginData(context).getId()+""));
            hashMap.put("title",RequestBody.create(MultipartBody.FORM,  edt_title.getText().toString()));
            hashMap.put("eventdate",RequestBody.create(MultipartBody.FORM,  tv_start_date_event.getText().toString()));
            hashMap.put("eventenddate",RequestBody.create(MultipartBody.FORM,  tv_end_date_event.getText().toString()));
            hashMap.put("eventstarttime",RequestBody.create(MultipartBody.FORM,  tv_start_time.getText().toString()));
            hashMap.put("eventendtime",RequestBody.create(MultipartBody.FORM,  tv_end_time.getText().toString()));
            hashMap.put("eventdetails",RequestBody.create(MultipartBody.FORM,  et_description.getText().toString()));
            hashMap.put("addlineone",RequestBody.create(MultipartBody.FORM,  edt_address_one.getText().toString()));
            hashMap.put("addlinetwo",RequestBody.create(MultipartBody.FORM,  edt_address_two.getText().toString()));
            hashMap.put("datelong",RequestBody.create(MultipartBody.FORM,"05"));
            //hashMap.put("join_type",RequestBody.create(MultipartBody.FORM,Approved_members));
            // hashMap.put("createdby", RequestBody.create(MultipartBody.FORM,"96" ));
            ApiExecutor.getApiService().eventcreat("createevent",aadharFront,hashMap).enqueue(new Callback<AddressResponse>() {
                @Override
                public void onResponse(Call<AddressResponse> call, Response<AddressResponse> response) {
                    UtilityFunction.hideLoading();
                    try {
                        //  Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        if (  response.body().getStatus().equals("success")){

                              dialog.dismiss();
                            mailSendApi();

                       /*     Log.d("response----", response.toString());
                            Intent i = new Intent(CreateEvent.this, EventAllListCurrentData.class);
                            i.putExtra("neighbrhood", "drawar");*/

                        /*    Intent i = new Intent();
                            i.putExtra("neighbrhood", "drawar");
                            startActivity(i);
*/
                            onBackPressed();

                            finish();
                            Toast.makeText(CreateEvent.this, "Event created succesful", Toast.LENGTH_SHORT).show();

                            // startActivity(new Intent(LittleMoreAboutYouActivity.this,NeighbourhoodSelection.class));
                            // Intent intent = new Intent(LittleMoreAboutYouActivity.this, NeighbourhoodSelection.class);
                        }else if (response.body().getMessage()!=null){
                            Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                            //  Intent intent = new Intent(AddressProof.this, MainActivity.class);
                            //  startActivity(intent);

                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
                @Override
                public void onFailure(Call<AddressResponse> call, Throwable t) {

                    if (t.toString().contains("timeout")){
                        Toast.makeText(CreateEvent.this, "Something seems to have gone wrong.Please try again", Toast.LENGTH_SHORT).show();


                    }else if (t.toString().contains("Unable to resolve host")){
                        Toast.makeText(CreateEvent.this, "No internet", Toast.LENGTH_SHORT).show();

                    }else {
                        Toast.makeText(CreateEvent.this, "Something seems to have gone wrong.Please try again", Toast.LENGTH_SHORT).show();


                    }
                    dialog.dismiss();
                    Log.e("fdsadf",t.toString());
                    // Toast.makeText(context, "Something went wrong.", Toast.LENGTH_SHORT).show();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            if (dialog != null) {
                dialog.dismiss();
            }
        }

//        dialog.setCanceledOnTouchOutside(true);
    }
    private void performCrop(Uri fileUri) {
        try {
            CropImage.ActivityBuilder a1 = CropImage.activity(fileUri);
            a1.setCropMenuCropButtonTitle("Set Image");
            a1.setAspectRatio(1,1);
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
        }else if (BadWordFilter.containsBadWord(edt_title.getText().toString())) {
            GlobalMethods.getInstance(CreateEvent.this).globalDialogAbusiveWord(CreateEvent.this, getString(R.string.abusive_msg));
            return false;
        } else if(tv_start_date_event.getText().toString().equals("Start date"))
        {
            Toast.makeText(CreateEvent.this, "Please select start date", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(tv_end_date_event.getText().toString().equals("End date"))
        {
            Toast.makeText(CreateEvent.this, "Please select end date", Toast.LENGTH_SHORT).show();
            return false;
        }
         else if(tv_start_time.getText().toString().equals("Start time"))
        {
            Toast.makeText(CreateEvent.this, "Please select start time", Toast.LENGTH_SHORT).show();
            return false;
        }
         else if(tv_end_time.getText().toString().equals("End time"))
        {
            Toast.makeText(CreateEvent.this, "Please select end time", Toast.LENGTH_SHORT).show();
            return false;
        }
        else  if (et_description.getText().toString().matches("")) {
            GlobalMethods.setError(et_description, "Please enter description");
            return false;
        }else if (BadWordFilter.containsBadWord(et_description.getText().toString())) {
            GlobalMethods.getInstance(CreateEvent.this).globalDialogAbusiveWord(CreateEvent.this, getString(R.string.abusive_msg));
            return false;
        } else if (edt_address_one.length() == 0) {
            GlobalMethods.setError(edt_address_one,"Please enter address line 1");
            return false;
        }
        return true;
    }

    String tim;
    public  void timePicker(TextView tv, String type, int hr, int min) {
        final Calendar c = Calendar.getInstance();
        int hour = 0;
        int minute =0;
        if(type.equals("opentime"))
        {
            hour = hr;
            minute = min;
        }
        if(type.equals("closeTime"))
        {
            hour = hr;
            minute = min;
        }
        TimePickerDialog timePickerDialog = new TimePickerDialog(CreateEvent.this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {
                        boolean isPM = (hourOfDay >= 12);
//                tv.setText();
                        tim = String.format("%02d:%02d %s", (hourOfDay == 12 || hourOfDay == 0) ? 12 : hourOfDay % 12, minute, isPM ? "pm" : "am");
                        tv.setText(tim);
                        if(type.equals("opentime"))
                        {
                            opentime = tim;
                            updateOpenHrs = hourOfDay;
                            updateOpenMns = minute;
                            Log.d("OpenTime", opentime);
                        }
                        if(type.equals("closeTime"))
                        {
                            closeTime = tim;
                            updateCloseHrs = hourOfDay;
                            updateCloseMns = minute;
                            Log.d("CloseTime", closeTime);
                        }
                    }
                }, hour, minute, false);
        // at last we are calling show to
        // display our time picker dialog.
        timePickerDialog.show();
    }
    private void takePhotoFromCamera() {
        String fileName = System.currentTimeMillis() + "";
        File storageDirectory = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        try {

            File imageFile = File.createTempFile(fileName , "jpg",storageDirectory);
            currentPhotoPath = imageFile.getAbsolutePath();
            imageUri = FileProvider.getUriForFile(CreateEvent.this,"com.app_neighbrsnook.fileprovider",imageFile);
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            startActivityForResult(intent,PIC_CROP_REQUEST);

        } catch (IOException e) {
            e.printStackTrace();
        }
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

