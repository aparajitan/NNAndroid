package com.app_neighbrsnook.group;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import android.view.WindowInsetsController;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import com.app_neighbrsnook.login.AddressResponse;
import com.app_neighbrsnook.login.LoginActivity;
import com.app_neighbrsnook.model.GroupModelP;
import com.app_neighbrsnook.model.ImagePOJO;
import com.app_neighbrsnook.profile.MyProfile;
import com.app_neighbrsnook.utils.FileUtil;
import com.app_neighbrsnook.utils.GlobalMethods;
import com.app_neighbrsnook.utils.PrefMananger;
import com.app_neighbrsnook.utils.SharedPrefsManager;
import com.app_neighbrsnook.libraries.cropper.CropImage;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateGroups extends AppCompatActivity implements View.OnClickListener, ImageUploadAdapter1.ImageItemClick, VideoUploadAdapter.ImageItemClick {
    FrameLayout frm_upload_image;
    TextView tv_cancel;
    RelativeLayout rl_upload_layout;
    RelativeLayout rl_parent_layout;
    ImageView img_back,img_group_image;
    Context context;
    Activity activity;
    TextView tv_take_photo, tv_choose_photo;
    RecyclerView photo_recycler_view;
    ArrayList<ImagePOJO> bitmapList = new ArrayList<>();
    VideoUploadAdapter uploadAdapter;
    EditText edt_name_group, edt_name_second, edt_group_description;
    FrameLayout frm_publish_btn;
    //String
    RadioGroup rg_publish,rg_can_join;
    RadioButton rb_yes,rb_no,rb_anyone,rb_approved;
    SharedPrefsManager sm;
    String Approved_members="";
    ProgressDialog progressDialog;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sm = new SharedPrefsManager(this);

        context = activity = this;
        setContentView(R.layout.activity_create_groups);
        img_group_image=findViewById(R.id.img_create_group);
        rg_publish=findViewById(R.id.nearby_radgroup_id);
        rg_can_join=findViewById(R.id.can_join_radgrp_id);
        rb_yes=findViewById(R.id.nearby_yes_id);
        rb_no=findViewById(R.id.nearby_no_id);
        rb_anyone=findViewById(R.id.can_join_anyone);
        rb_approved=findViewById(R.id.can_join_approve_id);

        progressDialog = new ProgressDialog(context);
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);

       // edt_demo=findViewById(R.id.group_demo);
        Intent intent = getIntent();
        frm_publish_btn=findViewById(R.id.upload_id);
        //String title = intent.getStringExtra("title");
        img_back = findViewById(R.id.img_back);
        frm_upload_image = findViewById(R.id.group_crt_upload_image_frm);
        rl_upload_layout = findViewById(R.id.upload_options_rl);
        rl_parent_layout = findViewById(R.id.rl_parent_layout);
        tv_cancel = findViewById(R.id.cancle_tv);
        tv_take_photo = findViewById(R.id.take_photo);
        tv_choose_photo = findViewById(R.id.choose_photo);
        photo_recycler_view = findViewById(R.id.photos_recycler_view);
        edt_name_group = findViewById(R.id.edt_group_name);
        edt_name_second = findViewById(R.id.edt_group_name_second);
        edt_group_description = findViewById(R.id.about_group_id);
        //frm_publish_btn.setOnClickListener(this);
     /*   uploadAdapter = new VideoUploadAdapter(this, bitmapList, this);
        photo_recycler_view.setLayoutManager(new LinearLayoutManager(context, RecyclerView.VERTICAL, false));
        photo_recycler_view.setAdapter(uploadAdapter);*/
      //28-01  GlobalMethods.checkPermission(this);

        rl_upload_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rl_upload_layout.setVisibility(View.GONE);
            }
        });
        frm_publish_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Boolean isInternetConnection = GlobalMethods.checkConnection(context);
                if (CheckAllFields()){
                    progressDialog.show();
                    creatGroupApi();
                }else {
                   // GlobalMethods.getInstance(CreateGroups.this).globalDialog(context, "     No internet connection."     );
                }
            }
        });

        if (LoginActivity.InternetConnection.checkConnection(context)) {
            //Toast.makeText(context, "Internet here", Toast.LENGTH_SHORT).show();
        } else {
            GlobalMethods.getInstance(CreateGroups.this).globalDialog(context, "     No internet connection.     "     );
        }



        frm_upload_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ( GlobalMethods.checkCameraAndGalleryPermission(CreateGroups.this)) {
                    rl_upload_layout.setVisibility(View.VISIBLE);
                }
            }
        });
        tv_take_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // takePhotoFromCamera();
              /*  Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 1);*/
                capturePhoto();
                CLICK_ON=FIRST_IMAGE;
                //performCrop(filePath);
                rl_upload_layout.setVisibility(View.GONE);
            }
        });
        tv_choose_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CLICK_ON=FIRST_IMAGE;
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 2);
                rl_upload_layout.setVisibility(View.GONE);
                img_group_image.setVisibility(View.GONE);

            }
        });
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
    String currentPath="";
    Uri imageUri;
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
    String CLICK_ON = "";
    String FIRST_IMAGE = "FirstImage";

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
                Log.e("1258963", bitmap + "");

                filePath = imageUri;// Uri.fromFile(file1);
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                bitmap1 = bitmap;
                img_group_image.setImageBitmap(bitmap);
                img_group_image.setVisibility(View.VISIBLE);
                performCrop(filePath);
                img_group_image.setImageBitmap(bitmap);

                if (CLICK_ON.equals(FIRST_IMAGE)) {
                    bitmap1 = bitmap;
                    img_group_image.setImageBitmap(bitmap);
                }
            }

            else if (requestCode == PIC_CROP_REQUEST && data != null) {
                filePath = data.getData();
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
                    img_group_image.setImageBitmap(bitmap);
                    img_group_image.setVisibility(View.VISIBLE);

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

                        if (CLICK_ON.equals(FIRST_IMAGE)) {
                            bitmap1 = bitmap;
                            img_group_image.setImageBitmap(bitmap);

                        }

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
    @Override
    public void onClick(View view) {

    }
    private class InsertGroupData extends AsyncTask<GroupModelP, Void, Void>
            implements com.app_neighbrsnook.group.InsertGroupData {
        DAO dao = Database.createDBInstance(CreateGroups.this).getDao();
        @Override
        protected Void doInBackground(GroupModelP... investers) {
            dao.insertGroupData(investers[0]);
            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            onBackPressed();
        }
    }
    private void creatGroupApi() {
            try {
                File file1;
                int compressionRatio = 25; //1 == originalImage, 2 = 50% compression, 4=25% compress
                MultipartBody.Part aadharFront = null;
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
                    aadharFront = MultipartBody.Part.createFormData("groupimage", file1.getName(), videoPart);
                } else if (bitmap != null) {
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
                        aadharFront = MultipartBody.Part.createFormData("groupimage", file1.getName(), videoPart);
                    }
                }

                HashMap<String, RequestBody> hashMap = new HashMap<>();
                hashMap.put("createdby", RequestBody.create(MultipartBody.FORM, PrefMananger.GetLoginData(context).getId() + ""));
                hashMap.put("groupname", RequestBody.create(MultipartBody.FORM, edt_name_group.getText().toString()));
                hashMap.put("description", RequestBody.create(MultipartBody.FORM, edt_group_description.getText().toString()));
                if (rb_anyone.isChecked()) {
                    Approved_members = "1";
                    hashMap.put("join_type", RequestBody.create(MultipartBody.FORM, "1"));
                }
                if (rb_approved.isChecked()) {
                    Approved_members = "0";
                    hashMap.put("join_type", RequestBody.create(MultipartBody.FORM, "0"));
                    // Log.d("log----",Approved_members);
                }
                ApiExecutor.getApiService().createGroup("creategroup", aadharFront, hashMap).enqueue(new Callback<AddressResponse>() {
                    @Override
                    public void onResponse(Call<AddressResponse> call, Response<AddressResponse> response) {
                        try {
                            //  Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            if (response.body().getStatus().equals("success")) {
                                mailSendApi();
                                Toast.makeText(CreateGroups.this, "Group created succesful", Toast.LENGTH_SHORT).show();
                                onBackPressed();

                                finish();
                                progressDialog.dismiss();
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
                        progressDialog.dismiss();
                        GlobalMethods.getInstance(CreateGroups.this).globalDialog(context, "Something seems to have gone wrong.Please try again"     );

                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }
            }
    }
    private void performCrop(Uri fileUri) {
        CropImage.activity(fileUri)
                .setCropMenuCropButtonTitle("Set Image")
                .setAspectRatio(1, 1)
                .setFixAspectRatio(false)
                .start(this);
    }
    private boolean CheckAllFields() {

          if (edt_name_group.getText().toString().matches("")) {
            GlobalMethods.setError(edt_name_group, "Please enter group name");
            return false;
        } else if (BadWordFilter.containsBadWord(edt_name_group.getText().toString())) {
              GlobalMethods.getInstance(CreateGroups.this).globalDialogAbusiveWord(CreateGroups.this, getString(R.string.abusive_msg));
              return false;
          } else if (edt_group_description.getText().toString().matches("")) {
            GlobalMethods.setError(edt_group_description,"Please enter group description");
            return false;
        } else if (BadWordFilter.containsBadWord(edt_group_description.getText().toString())) {
              GlobalMethods.getInstance(CreateGroups.this).globalDialogAbusiveWord(CreateGroups.this, getString(R.string.abusive_msg));
              return false;
          } else if(rg_can_join.getCheckedRadioButtonId() == -1) {
             if(rb_anyone.isChecked() || rb_approved.isChecked()) {
                 Log.d("QAOD", "Gender is Selected");
             } else{
                 Toast.makeText(getApplicationContext(), "Please select who can join", Toast.LENGTH_SHORT).show();
                 Log.d("QAOD", "Gender is Null");
             }

             return false;
         }
return true;
    }
    public static class InternetConnection {

        /**
         * CHECK WHETHER INTERNET CONNECTION IS AVAILABLE OR NOT
         */
        public static boolean checkConnection(Context context) {
            final ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

            if (connMgr != null) {
                NetworkInfo activeNetworkInfo = connMgr.getActiveNetworkInfo();

                if (activeNetworkInfo != null) { // connected to the internet
                    // connected to the mobile provider's data plan
                    if (activeNetworkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                        // connected to wifi
                        return true;
                    } else return activeNetworkInfo.getType() == ConnectivityManager.TYPE_MOBILE;
                }
            }
            return false;
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
                rl_upload_layout.setVisibility(View.VISIBLE);
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