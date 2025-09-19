package com.app_neighbrsnook.group;

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
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app_neighbrsnook.R;
import com.app_neighbrsnook.adapter.ImageUploadAdapter1;
import com.app_neighbrsnook.adapter.VideoUploadAdapter;
import com.app_neighbrsnook.apiService.ApiExecutor;
import com.app_neighbrsnook.database.DAO;
import com.app_neighbrsnook.database.Database;
import com.app_neighbrsnook.login.AddressResponse;
import com.app_neighbrsnook.model.GroupModelP;
import com.app_neighbrsnook.model.ImagePOJO;
import com.app_neighbrsnook.network.APIClient;
import com.app_neighbrsnook.network.APIInterface;
import com.app_neighbrsnook.pojo.GroupDetailsPojo;
import com.app_neighbrsnook.utils.FileUtil;
import com.app_neighbrsnook.utils.GlobalMethods;
import com.app_neighbrsnook.utils.PrefMananger;
import com.app_neighbrsnook.utils.SharedPrefsManager;
import com.app_neighbrsnook.utils.UtilityFunction;
import com.squareup.picasso.Picasso;
import com.app_neighbrsnook.libraries.cropper.CropImage;

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

public class UpdateCreateGroups extends AppCompatActivity implements View.OnClickListener, ImageUploadAdapter1.ImageItemClick, VideoUploadAdapter.ImageItemClick {
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
    EditText  edt_name_second, edt_group_description;
    GroupModelP groupModelP;
    FrameLayout frm_publish_btn;
    //String
    RadioGroup rg_publish,rg_can_join;
    RadioButton rb_yes,rb_no,rb_anyone,rb_approved;
    SharedPrefsManager sm;
    String Approved_members="";
    String publish_nearby="";
    String groupName,groupPrivate,groupAbout,groupMembers;
    EditText tv_group_name,tv_members_details,tv_about,tv_private;
    String group_id,userid;
    ImageView img_group_details;
    int id;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = activity = this;
        setContentView(R.layout.activity_edit_group);
        Intent intent = getIntent();
        sm = new SharedPrefsManager(this);
        id = intent.getIntExtra("id",0);

       // img_group_details = findViewById(R.id.img_create_group);
        //tv_members_details=findViewById(R.id.tv_members_details);
        tv_group_name=findViewById(R.id.edt_group_name);
        tv_about=findViewById(R.id.about_group_id);
       // tv_private=findViewById(R.id.tv_join_type_details);


        img_group_image=findViewById(R.id.img_create_group);
        rg_publish=findViewById(R.id.nearby_radgroup_id);
        rg_can_join=findViewById(R.id.can_join_radgrp_id);
        rb_yes=findViewById(R.id.nearby_yes_id);
        rb_no=findViewById(R.id.nearby_no_id);
        rb_anyone=findViewById(R.id.can_join_anyone);
        rb_approved=findViewById(R.id.can_join_approve_id);
       // edt_demo=findViewById(R.id.group_demo);
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
        //edt_name_group = findViewById(R.id.edt_group_name);
        edt_name_second = findViewById(R.id.edt_group_name_second);
       // edt_group_description = findViewById(R.id.about_group_id);
        frm_publish_btn.setOnClickListener(this);
        uploadAdapter = new VideoUploadAdapter(this, bitmapList, this);
        photo_recycler_view.setLayoutManager(new LinearLayoutManager(context, RecyclerView.VERTICAL, false));
        photo_recycler_view.setAdapter(uploadAdapter);
        rl_upload_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rl_upload_layout.setVisibility(View.GONE);
            }
        });

        frm_upload_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ( GlobalMethods.checkCameraAndGalleryPermission(UpdateCreateGroups.this)) {
                    rl_upload_layout.setVisibility(View.VISIBLE);
                }
            }
        });
        tv_take_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
            }
        });
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        businessDetailApi(id);


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

    String currentPath="";
    Uri imageUri;
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
        if (view.getId() == R.id.upload_id) {
            if (CheckAllFields()) {
                UpdateGroupApi();
            }

        }
    }
    private class InsertGroupData extends AsyncTask<GroupModelP, Void, Void>
            implements com.app_neighbrsnook.group.InsertGroupData {
        DAO dao = Database.createDBInstance(UpdateCreateGroups.this).getDao();
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
    private void UpdateGroupApi() {
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Please wait...");
        dialog.setCancelable(false);
        dialog.show();
        try {
            File file1;
            int compressionRatio = 25; //1 == originalImage, 2 = 50% compression, 4=25% compress
            MultipartBody.Part  aadharFront=null;
            if (filePath1 != null) {
                file1 = FileUtil.from(this, filePath1);
                try {
                } catch (Throwable t) {
                    Log.e("ERROR", "Error compressing file." + t.toString());
                    t.printStackTrace();
                }
                RequestBody videoPart = null;
                try {
                    Bitmap bmp = BitmapFactory.decodeFile(file1.getAbsolutePath());
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    bmp.compress(Bitmap.CompressFormat.JPEG, 20, bos);
                    Log.e("imageSize", bos.toByteArray().length + " Byte \n" + (bos.toByteArray().length / 1024) + "KB");
                    videoPart = RequestBody.create(MediaType.parse(getContentResolver().getType(filePath1)), bos.toByteArray());
                } catch (Exception e) {
                    e.printStackTrace();
                    videoPart = RequestBody.create(MediaType.parse(".jpg"), file1);
                }
                aadharFront = MultipartBody.Part.createFormData("groupimage", file1.getName(), videoPart);
            } else if (bitmap1!=null){
                file1 = bitmapToFile(bitmap1, getString(R.string.app_name) + System.currentTimeMillis() + ".jpg");
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
            hashMap.put("userid", RequestBody.create(MultipartBody.FORM, PrefMananger.GetLoginData(context).getId()+""));
            hashMap.put("groupname",RequestBody.create(MultipartBody.FORM,  tv_group_name.getText().toString()));
            hashMap.put("description",RequestBody.create(MultipartBody.FORM,  tv_about.getText().toString()));
            hashMap.put("groupid",RequestBody.create(MultipartBody.FORM, String.valueOf(id)));
            if(rb_anyone.isChecked()) {
                Approved_members= "1";
                hashMap.put("join_type", RequestBody.create(MultipartBody.FORM, "1"));
               // Log.d("log----",Approved_members);
            }
            if(rb_approved.isChecked()) {
                Approved_members= "0";
                hashMap.put("join_type", RequestBody.create(MultipartBody.FORM, "0"));
                //Log.d("log----",Approved_members);
            }
            if(rb_yes.isChecked()) {
                publish_nearby= "1";
                hashMap.put("visibility", RequestBody.create(MultipartBody.FORM, "1"));

            }
            if(rb_no.isChecked()) {
                publish_nearby= "0";
                hashMap.put("visibility", RequestBody.create(MultipartBody.FORM, "0"));
            }

            ApiExecutor.getApiService().updateGroup("updategroup",aadharFront,hashMap).enqueue(new Callback<AddressResponse>() {
                @Override
                public void onResponse(Call<AddressResponse> call, Response<AddressResponse> response) {
                    UtilityFunction.hideLoading();
                    try {
                        if (  response.body().getStatus().equals("success")){
                            Intent i = new Intent(UpdateCreateGroups.this, GroupActivity.class);
                            i.putExtra("neighbrhood", "drawar");
                            startActivity(i);
                            Log.d("response----", response.toString());
                            Toast.makeText(UpdateCreateGroups.this, "Succesful", Toast.LENGTH_SHORT).show();
                            finishAfterTransition();
                        }else if (response.body().getMessage()!=null){
                            Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
                @Override
                public void onFailure(Call<AddressResponse> call, Throwable t) {
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

          if (tv_group_name.getText().toString().matches("")) {
            GlobalMethods.setError(tv_group_name, "Please enter group name");
            return false;
        }
        else if (tv_about.getText().toString().matches("")) {
            GlobalMethods.setError(tv_about,"Please enter group description");
            return false;
        }

         else if(rg_can_join.getCheckedRadioButtonId() == -1) {
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

    private void businessDetailApi(int id) {
        HashMap<String, Object> hm = new HashMap<>();
        hm.put("groupid", id);
        //hm.put("userid", Integer.parseInt((PrefMananger.GetLoginData(context).getId() +"")));
        hm.put("userid", Integer.parseInt(sm.getString("user_id")));
        APIInterface service = APIClient.getRetrofit().create(APIInterface.class);
        Call<GroupDetailsPojo> call = service.groupupdateShow("groupdetail", hm);
        call.enqueue(new Callback<GroupDetailsPojo>() {
            @Override
            public void onResponse(Call<GroupDetailsPojo> call, Response<GroupDetailsPojo> response) {
                GroupDetailsPojo rootObject= response.body();
                //JsonObject rootObject = businessDetailPojo.getAsJsonObject();
                try {
                    groupName= rootObject.getGroupname();
                    groupAbout= rootObject.getDescription();
                    userid=rootObject.getCreatedby();
                    group_id = rootObject.getGroupid() ;
                    tv_group_name.setText(groupName);
                    tv_about.setText(groupAbout);
                    if (rootObject.getGroupType().equals("Private")){
                        rb_approved.setChecked(true);
                        rb_anyone.setChecked(false);
                    } else {
                        rb_approved.setChecked(false);
                        rb_anyone.setChecked(true);
                    } if (rootObject.getNearbyneighbrhood().equals("1")){
                        rb_yes.setChecked(true);
                        rb_no.setChecked(false);
                    } else {
                        rb_no.setChecked(false);
                        rb_no.setChecked(true);
                    }

                    Picasso.get().load(rootObject.getImage()).into(img_group_image);
                    if (rootObject.getImage().isEmpty()) {
                        img_group_image.setImageResource(R.drawable.profile);
                    } else{
                        Picasso.get().load(rootObject.getImage()).fit().centerCrop().error(R.drawable.profile).placeholder(R.drawable.profile)
                                .into(img_group_image);
                    }

                }catch (Exception exception)
                {
                }

            }

            @Override
            public void onFailure(Call<GroupDetailsPojo> call, Throwable t) {
                Toast.makeText(UpdateCreateGroups.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("res", t.getMessage());
            }
        });

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