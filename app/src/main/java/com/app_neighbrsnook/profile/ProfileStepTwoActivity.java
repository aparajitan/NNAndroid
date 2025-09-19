package com.app_neighbrsnook.profile;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.app_neighbrsnook.MainActivity;
import com.app_neighbrsnook.R;
import com.app_neighbrsnook.adapter.BusinessCategoryAdapter2;
import com.app_neighbrsnook.adapter.ImageUploadAdapter;
import com.app_neighbrsnook.adapter.RegistrationGenderAdapter;
import com.app_neighbrsnook.adapter.SkipCategoryAdapter;
import com.app_neighbrsnook.adapter.SkipScreenLoveMyNghAdapter;
import com.app_neighbrsnook.apiService.ApiExecutor;
import com.app_neighbrsnook.event.ImageShowActivity;
import com.app_neighbrsnook.login.AddressResponse;
import com.app_neighbrsnook.model.ImagePOJO;
import com.app_neighbrsnook.network.APIClient;
import com.app_neighbrsnook.network.APIInterface;
import com.app_neighbrsnook.pojo.Nbdatum;
import com.app_neighbrsnook.pojo.StatePojo;
import com.app_neighbrsnook.utils.FileUtil;
import com.app_neighbrsnook.utils.PrefMananger;
import com.app_neighbrsnook.utils.SharedPrefsManager;
import com.app_neighbrsnook.utils.UtilityFunction;
import com.squareup.picasso.Picasso;
import com.app_neighbrsnook.libraries.cropper.CropImage;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileStepTwoActivity extends AppCompatActivity implements View.OnClickListener, BusinessCategoryAdapter2.CategoryInterface,
        RegistrationGenderAdapter.CategoryInterface1,SkipCategoryAdapter.InterestCategoryInterface,SkipScreenLoveMyNghAdapter.LoveMyNeighbrhood {
    FrameLayout frm_next;
    ImageView img_little_back, img_caledar;
    Calendar calendar;
    TextView tv_date_of_birth, select_gender, tv_emergency;
    TextView tv_gender, select_what_do_you, tv_skip, tv_love;
    Dialog mail_dialog, interestDialog;
    FrameLayout frm_choose_interest;
    TextView tv_choose_interest,frm_love_my_ngh;
    String lovengh,emergency,dob,genderProfile,whatDoYou,chooseInterest,userVerify;
    Activity activity;

    Context context;
    String love_neighbrhood = "";
    String categoriId = "";
    CircleImageView img_profile;
    ArrayList<ImagePOJO> bitmapList = new ArrayList<>();
    HashMap<String, Object> hashMapProfile;
    HashMap<String, Object> hashMap;
    HashMap<String, Object> hmedit;
    FrameLayout frm_photo,loveNeighb;
    ImageUploadAdapter uploadAdapter;
    RecyclerView rv;
    SharedPrefsManager sm;
    String type;
    TextView tv_take_photo,tv_choose_photo,tv_cancel;
    RelativeLayout rl_my_profile;


    String u_dob,u_gender,u_what_do_you,u_interes,u_love_ngh,u_emergency;
    HashMap<String, Object> hmUpdate = new HashMap<>();

    String interesID="",loveId="",profesionId="";
    List<Nbdatum> categorylist = new ArrayList<>();
    List<Nbdatum> interesList = new ArrayList<>();
    List<Nbdatum> chooseLoveList = new ArrayList<>();
    BusinessCategoryAdapter2 businessCategoryAdapter;
    SkipCategoryAdapter adapterInterest;
    SkipScreenLoveMyNghAdapter loveneighbrhood;
    ImageView img_edit_icon;
    String category,interest,loveselectcat, doc_type;
    boolean isVerifiedUser = true;

    //String tvda

    /*String[] days = {"Male","Female","Other"};
    List<String> emailsList = new ArrayList<>();*/
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_steptwo);
        sm = new SharedPrefsManager(this);

        context = activity = this;
        calendar = Calendar.getInstance();
        img_caledar = findViewById(R.id.img_calendar);
        img_edit_icon = findViewById(R.id.img_edit_icon);
        tv_cancel = findViewById(R.id.cancle_tv);
        tv_take_photo = findViewById(R.id.take_photo);
        tv_choose_photo = findViewById(R.id.choose_photo);
        rl_my_profile=findViewById(R.id.rl_my_profile);
        frm_photo = findViewById(R.id.frm_photo);
        tv_date_of_birth = findViewById(R.id.tv_date_of_birth);
        frm_next = findViewById(R.id.next_id);
        img_profile = findViewById(R.id.cicle_image_view_id);
        img_little_back = findViewById(R.id.img_little_back);
        tv_gender = findViewById(R.id.gender_id_img);
        frm_love_my_ngh = findViewById(R.id.frm_love_ngh);
        frm_choose_interest = findViewById(R.id.choose_interest_frm);
        select_gender = findViewById(R.id.select_gender_frm);
        select_what_do_you = findViewById(R.id.what_do_you_do_frm);
        tv_skip = findViewById(R.id.skip_id);
        tv_emergency = findViewById(R.id.tv_emergency_contact);
        tv_choose_interest = findViewById(R.id.choose_interes_tv);
        loveNeighb = findViewById(R.id.loveNeighb);
        categoryApi();
        chooseInterestApi();
        loveNeighrhoodApi();

        tv_take_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                capturePhoto();
                CLICK_ON=FIRST_IMAGE;
                rl_my_profile.setVisibility(View.GONE);
            }
        });
        tv_choose_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 2);
                CLICK_ON=FIRST_IMAGE;

                rl_my_profile.setVisibility(View.GONE);
            }
        });
        img_edit_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rl_my_profile.setVisibility(View.VISIBLE);
            }
        });

        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rl_my_profile.setVisibility(View.GONE);
            }
        });
        img_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProfileStepTwoActivity.this, ImageShowActivity.class));

            }
        });

        tv_skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProfileStepTwoActivity.this, MainActivity.class));
            }
        });
        select_what_do_you.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectCategory();
            }
        });
        frm_choose_interest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseInterestDialog();

            }
        });
        tv_choose_interest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseInterestDialog();

            }
        });
        select_gender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 selectGender();

            }
        });
        frm_love_my_ngh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseloveNeighbrhdDialog();
            }
        });
        loveNeighb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseloveNeighbrhdDialog();
            }
        });
        frm_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

              updateProfileStep();
               awaitStatus();
            }
        });
        littleMoreSkip();
        DatePickerDialog.OnDateSetListener fromdate = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.YEAR, year);
                date1();
            }
        };

        tv_date_of_birth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isVerifiedUser)
                {
                    Toast.makeText(ProfileStepTwoActivity.this, "For updating birth date, Kindly send mail to info@neighbrsnook.com attaching one id proof.", Toast.LENGTH_LONG).show();
                }
                else {
                    new DatePickerDialog(ProfileStepTwoActivity.this, fromdate, calendar.get(Calendar.YEAR)-13,
                            calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();



                }

            }
        });
        img_little_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // startActivity(new Intent(LittleMoreAboutYouActivity.this,RegistrationActivity.class));
                onBackPressed();
            }
        });
        categoryApi();
    }
    public void date1() {
        String myFormat = "dd-MM-yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        tv_date_of_birth.setText(sdf.format(calendar.getTime()));

    }

    private void selectGender() {
        RecyclerView rv;
        ImageView cancel;
        mail_dialog = new Dialog(this);
        mail_dialog.setContentView(R.layout.selct_genderr);
        rv = mail_dialog.findViewById(R.id.rv_category);
//        confirm = mail_dialog.findViewById(R.id.cross_imageview);
        cancel = mail_dialog.findViewById(R.id.cross_imageview);
        rv.setLayoutManager(new LinearLayoutManager(this));
        RegistrationGenderAdapter emailListAdapter = new RegistrationGenderAdapter(mail_dialog, ProfileStepTwoActivity.this);
        rv.setAdapter(emailListAdapter);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mail_dialog.cancel();
            }
        });
        mail_dialog.getWindow().setBackgroundDrawableResource(R.drawable.round_corner_dialog);
        mail_dialog.setCancelable(true);
        mail_dialog.show();/*
        FrameLayout frmpost=mail_dialog.findViewById(R.id.post_frm);
        frmpost.setVisibility(View.GONE);*/
    }


    String FIRST_IMAGE = "FirstImage";
    String CLICK_ON = "";

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
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                Log.e("1258963", bitmap + "");
                //  File file1 = bitmapToFile(bitmap, getString(R.string.app_name) + "_" + System.currentTimeMillis() + ".jpg");
                filePath = imageUri;// Uri.fromFile(file1);
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                bitmap1 = bitmap;
                img_profile.setImageBitmap(bitmap);
                img_profile.setVisibility(View.VISIBLE);
                performCrop(filePath);
                img_profile.setImageBitmap(bitmap);
                if (CLICK_ON.equals(FIRST_IMAGE)) {
                    bitmap1 = bitmap;
                    img_profile.setImageBitmap(bitmap);
                    //updateImageWithoutLoader(bitmap1);
                    // updateImageWithoutLoader(bitmap3);
                   // imgDefaultImg.setVisibility(View.GONE);
                    img_profile.setVisibility(View.VISIBLE);

                    ImagePOJO imagePOJO = new ImagePOJO();
                    Log.e("1258963", bitmap + "");
                    imagePOJO.bitmap = bitmap;
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
                // if (CLICK_ON.equals(FIRST_IMAGE)) {
                bitmap1 = bitmap;
                filePath1 = filePath;
                img_profile.setImageBitmap(bitmap);
                //updateImageWithoutLoader(bitmap);

                // bitmapList.add(imagePOJO);
                // uploadAdapter.notifyDataSetChanged();
                //}
            }
            else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                if (resultCode == RESULT_OK) {
                    Uri resultUri = result.getUri();
                    Bitmap photo1;
                    try {
                        photo1 = MediaStore.Images.Media.getBitmap(context.getContentResolver(), resultUri);
                        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                        photo1.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                        //byte[] byteArrayImage = bytes.toByteArray();
                        //encodedImage = Base64.encodeToString(byteArrayImage, Base64.DEFAULT);
                        filePath = resultUri;
                        bitmap = photo1;
                        // binding.ivUser.setImageBitmap(photo1);

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

                    bitmap1 = bitmap;
                    img_profile.setImageBitmap(bitmap);
                    // updateImage(bitmap);
                  //  updateImageWithoutLoader(bitmap);
                 //   imgDefaultImg.setVisibility(View.GONE);
                    img_profile.setVisibility(View.VISIBLE);


                } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                    Exception error = result.getError();
                    error.printStackTrace();
                }}}}
    private void categoryApi() {
        APIInterface service = APIClient.getRetrofit().create(APIInterface.class);
        Call<StatePojo> call = service.skip2CategoryApi("profession");
        call.enqueue(new Callback<StatePojo>() {
            @Override
            public void onResponse(Call<StatePojo> call, Response<StatePojo> response) {
                categorylist = response.body().getNbdata();
            }
            @Override
            public void onFailure(Call<StatePojo> call, Throwable t) {
                Toast.makeText(ProfileStepTwoActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("res---", t.getMessage());
            }
        });
    }
    private void chooseInterestApi() {
        APIInterface service = APIClient.getRetrofit().create(APIInterface.class);
        Call<StatePojo> call = service.skip2ChooseInteres("interest");
        call.enqueue(new Callback<StatePojo>() {
            @Override
            public void onResponse(Call<StatePojo> call, Response<StatePojo> response) {
                interesList = response.body().getNbdata();
            }
            @Override
            public void onFailure(Call<StatePojo> call, Throwable t) {
                Toast.makeText(ProfileStepTwoActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("res---", t.getMessage());
            }
        });
    }
    private void loveNeighrhoodApi() {
        APIInterface service = APIClient.getRetrofit().create(APIInterface.class);
        Call<StatePojo> call = service.skip2loveNeighbrhoodset("reason");
        call.enqueue(new Callback<StatePojo>() {
            @Override
            public void onResponse(Call<StatePojo> call, Response<StatePojo> response) {
                chooseLoveList = response.body().getNbdata();
            }
            @Override
            public void onFailure(Call<StatePojo> call, Throwable t) {
                Toast.makeText(ProfileStepTwoActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("res---", t.getMessage());
            }
        });
    }
    @Override
    public void onClick(View view) {

    }
    private void selectCategory() {
        ImageView cancel;
        mail_dialog = new Dialog(this);
        mail_dialog.setContentView(R.layout.open_profession_step2);
        rv = mail_dialog.findViewById(R.id.rv_category);
        cancel = mail_dialog.findViewById(R.id.cross_imageview);
        rv.setLayoutManager(new LinearLayoutManager(this));
            businessCategoryAdapter = new BusinessCategoryAdapter2( ProfileStepTwoActivity.this, categorylist);
        rv.setAdapter(businessCategoryAdapter);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mail_dialog.cancel();
            }
        });
        mail_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(ProfileStepTwoActivity.this, android.R.color.transparent)));
        mail_dialog.setCancelable(true);
        mail_dialog.show();
        /*FrameLayout frm=mail_dialog.findViewById(R.id.post_frm);
        frm.setVisibility(View.GONE);*/
    }
    private void chooseInterestDialog() {
        ImageView cancel;
        mail_dialog = new Dialog(this);
        mail_dialog.setContentView(R.layout.select_interes_recy_layout);
        rv = mail_dialog.findViewById(R.id.rv_category);
        cancel = mail_dialog.findViewById(R.id.cross_imageview);
      FrameLayout  post_frm = mail_dialog.findViewById(R.id.post_frm);
        rv.setLayoutManager(new LinearLayoutManager(this));
        for (Nbdatum nbdatum : interesList){
            nbdatum.setSelected(false);
            if (chooseInterest!=null && !chooseInterest.isEmpty()){
                if (chooseInterest.contains(nbdatum.getMember_title())){
                    nbdatum.setSelected(true);
                }
            }
        }


        adapterInterest = new SkipCategoryAdapter( ProfileStepTwoActivity.this, interesList);
        rv.setAdapter(adapterInterest);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mail_dialog.cancel();
            }
        });
        post_frm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //mail_dialog.cancel();
                interesID = "";
              String  intrestdValues =  "";
                for (Nbdatum nbdatum : interesList){
                    if (nbdatum.isSelected()){
                        if (interesID.isEmpty()){
                            intrestdValues=nbdatum.getMember_title();
                            interesID=nbdatum.getId();
                        }else {
                            interesID=interesID+ ","+nbdatum.getId();
                            intrestdValues=intrestdValues+", "+nbdatum.getMember_title();
                        }
                    }
                }
                tv_choose_interest.setText(  intrestdValues);
                mail_dialog.dismiss();
            }
        });
        mail_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(ProfileStepTwoActivity.this, android.R.color.transparent)));
        mail_dialog.setCancelable(true);
        mail_dialog.show();
    }
    private void chooseloveNeighbrhdDialog() {
        ImageView cancel;
        mail_dialog = new Dialog(this);
        mail_dialog.setContentView(R.layout.i_love_neighbrhood_rec_layout);
        rv = mail_dialog.findViewById(R.id.rv_category);
        cancel = mail_dialog.findViewById(R.id.cross_imageview);
        FrameLayout  post_frm = mail_dialog.findViewById(R.id.post_frm);
        rv.setLayoutManager(new LinearLayoutManager(this));
        /*for (Nbdatum nbdatum : chooseLoveList){
            nbdatum.setSelected(false);
            if (nbdatum.isSelected()){
                nbdatum.getId();
            }
        }
*/
        for (Nbdatum nbdatum : chooseLoveList){
            nbdatum.setSelected(false);
            if (lovengh!=null && !lovengh.isEmpty()){
                if (lovengh.contains(nbdatum.getMember_title())){
                    nbdatum.setSelected(true);
                }
            }
            // if (nbdatum.isSelected()){
            //  nbdatum.getId();
            //  }
        }
        loveneighbrhood = new SkipScreenLoveMyNghAdapter( ProfileStepTwoActivity.this, chooseLoveList);
        rv.setAdapter(loveneighbrhood);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mail_dialog.cancel();
            }
        });
        post_frm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loveId = "";
                String  intrestdValues = "";
                for (Nbdatum nbdatum : chooseLoveList){
                    if (nbdatum.isSelected()){
                        if (loveId.isEmpty()){
                            intrestdValues=nbdatum.getMember_title();
                            loveId=nbdatum.getMember_title();
                        }else {
                            loveId=loveId+","+nbdatum.getMember_title();
                            intrestdValues=intrestdValues+", "+nbdatum.getMember_title();
                        }
                    }
                }
                frm_love_my_ngh.setText(intrestdValues);
                mail_dialog.dismiss();
            }
        });
        mail_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(ProfileStepTwoActivity.this, android.R.color.transparent)));
        mail_dialog.setCancelable(true);
        mail_dialog.show();
    }
    @Override
    public void onClick3(String categoryName, String id) {

        select_what_do_you.setText(categoryName);
        category = categoryName;
        categoriId = id;
        mail_dialog.cancel();
    }

    @Override
    public void onClick(String categoryName) {

        select_gender.setText(categoryName);
         categoryName = categoryName;
       // interesID = id;

        mail_dialog.cancel();}

    @Override
    public void onClick1(String categoryName, String id) {
        tv_choose_interest.setText(categoryName);
        interest = categoryName;
        interesID = id;
        mail_dialog.cancel();
    }
    @Override
    public void onClick2(String categoryName, String id) {
        frm_love_my_ngh.setText(categoryName);
        loveselectcat = categoryName;
        //loveId = id;
        mail_dialog.cancel();
    }
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (getCurrentFocus() != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
        return super.dispatchTouchEvent(ev);
    }

    private void performCrop(Uri fileUri) {
        try {
            CropImage.ActivityBuilder a1 = CropImage.activity(fileUri);
            a1.setCropMenuCropButtonTitle("Set Image");
            a1.setAspectRatio(1,1);
            a1.start(activity);
        } catch (ActivityNotFoundException anfe) {
            //display an error message
            String errorMessage = "Whoops - your device doesn't support the crop action!";
            Toast toast = Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT);
            toast.show();
        }
    }
    private void updateProfileStep() {
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Please wait...");
        dialog.setCancelable(false);
        dialog.show();
        try {
            File file1;
            int compressionRatio = 25; //1 == originalImage, 2 = 50% compression, 4=25% compress

            MultipartBody.Part userpic=null;
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
                    Log.e("Name", file1.getName());
                    Log.e("Type", file1.getAbsolutePath());
                } catch (Exception e) {
                    e.printStackTrace();
                    videoPart = RequestBody.create(MediaType.parse(".jpg"), file1);
                }
                userpic = MultipartBody.Part.createFormData("userpic", file1.getName(), videoPart);
            } else if (bitmap1!=null){
                file1 = bitmapToFile(bitmap1, getString(R.string.app_name) + System.currentTimeMillis() + ".jpg");
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
                    userpic = MultipartBody.Part.createFormData("userpic", file1.getName(), videoPart);
                }
            }
            HashMap<String, RequestBody> hashMap = new HashMap<>();
            hashMap.put("userid", RequestBody.create(MultipartBody.FORM, PrefMananger.GetLoginData(context).getId()+""));
            hashMap.put("profession", RequestBody.create(MultipartBody.FORM, categoriId));
            hashMap.put("interest", RequestBody.create(MultipartBody.FORM, interesID));
            hashMap.put("reason",RequestBody.create(MultipartBody.FORM, frm_love_my_ngh.getText().toString()));
            hashMap.put("emerphoneno",RequestBody.create(MultipartBody.FORM,  tv_emergency.getText().toString()));
            ApiExecutor.getApiService().moreaboutYou("moreaboutyou",userpic,hashMap).enqueue(new Callback<AddressResponse>() {
                @Override
                public void onResponse(Call<AddressResponse> call, Response<AddressResponse> response) {
                    UtilityFunction.hideLoading();
                    try {

                        if (  response.body().getStatus().equals("success")){
                           // welcomeDialog(response.body().getMessage());
                        //  onBackPressed();
                            dialog.dismiss();
                            Intent intent = new Intent(ProfileStepTwoActivity.this, MyProfile.class);
                            startActivity(intent);
                            finish();
                           // Log.d("response----", response.toString());
                          // Toast.makeText(ProfileStepTwoActivity.this, "Succesfully", Toast.LENGTH_SHORT).show();

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
    private void littleMoreSkip() {
        hashMapProfile = new HashMap<>();
        hashMapProfile.put("userid", Integer.parseInt(PrefMananger.GetLoginData(context).getId() +""));
        userProfile(hashMapProfile);
    }
    private void userProfile(HashMap<String, Object> hm) {
        APIInterface service = APIClient.getRetrofit().create(APIInterface.class);
        Call<JsonElement> call = service.profileapisec("userprofile" ,Integer.parseInt(PrefMananger.GetLoginData(context).getId()),hm);
        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                JsonElement jsonElement=response.body();
                JsonObject jsonObject = jsonElement.getAsJsonObject();
                try {
                    dob = jsonObject.get("dob").getAsString();
                    genderProfile = jsonObject.get("gender").getAsString();
                    whatDoYou = jsonObject.get("nbrs_type").getAsString();
                    chooseInterest = jsonObject.get("intersttype").getAsString();
                    lovengh = jsonObject.get("reason").getAsString();
                    emergency = jsonObject.get("emer_phone").getAsString();
                    interesID = jsonObject.get("intid").getAsString();
                    categoriId = jsonObject.get("profid").getAsString();
                    userVerify = jsonObject.get("verfied_msg").getAsString();
                    if(userVerify.equals("User Verification is not completed!")){
                        isVerifiedUser=false;
                    }
                    tv_date_of_birth.setText(dob);
                    select_gender.setText(genderProfile);
                    select_what_do_you.setText(whatDoYou);
                    tv_choose_interest.setText(chooseInterest);
                    frm_love_my_ngh.setText(lovengh);
                    tv_emergency.setText(emergency);
                    Picasso.get().load(jsonObject.get("userpic").getAsString()).into(img_profile);
                    if (jsonObject.get("userpic").getAsString().isEmpty()) {
                        img_profile.setImageResource(R.drawable.profile);
                    } else{
                        Picasso.get().load(jsonObject.get("userpic").getAsString()).fit().centerCrop().error(R.drawable.profile).placeholder(R.drawable.profile)
                                .into(img_profile);
                    }
                    // img_profile.setImageURI(bitmap);
                }catch (Exception e){

                }

            }
            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {

//                AppCommon.getInstance(PlaceOrderActivity.this).clearNonTouchableFlags(PlaceOrderActivity.this);
                Toast.makeText(ProfileStepTwoActivity.this, "Data not found", Toast.LENGTH_SHORT).show();
                Log.d("res", t.getMessage());
            }
        });
    }

String currentPath="";
    Uri imageUri;

    public void capturePhoto() {
        // Permission has already been granted
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
    private void awaitStatus() {
        if (UtilityFunction.isNetworkConnected(context)) {
            // UtilityFunction.showLoading(context, "Please wait...");
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("userid", sm.getString("user_id"));
            //    hashMap.put("flag","25");
            hashMap.put("status", "success");
            ApiExecutor.getApiService().requestNeighbrhood("awaitstatus", hashMap).enqueue(new Callback<AddressResponse>() {
                @Override
                public void onResponse(Call<AddressResponse> call, Response<AddressResponse> response)
                {
                    // UtilityFunction.hideLoading();
                    try {
                        if (  response.body().getStatus().equals("success")){
                            Log.d("response----", response.toString());
                        }else if (response.body().getMessage()!=null){
                            Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
                @Override
                public void onFailure(Call<AddressResponse> call, Throwable t) {
                    // UtilityFunction.hideLoading();
                }
            });
        } else {
            Toast.makeText(context, "Network is not available", Toast.LENGTH_SHORT).show();
        }
    }


}
