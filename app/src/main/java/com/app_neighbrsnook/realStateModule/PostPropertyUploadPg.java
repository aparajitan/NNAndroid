package com.app_neighbrsnook.realStateModule;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app_neighbrsnook.R;
import com.app_neighbrsnook.adapter.ImageUploadAdapter1;
import com.app_neighbrsnook.model.ImagePOJO;
import com.app_neighbrsnook.libraries.cropper.CropImage;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class PostPropertyUploadPg extends AppCompatActivity implements ImageUploadAdapter1.ImageItemClick {
    RelativeLayout rl_upload_img;

    FrameLayout frm_single_room,frm_upload_image;
    ArrayList<ImagePOJO> bitmapList = new ArrayList<>();
    Context context;
    Activity activity;
    ImageUploadAdapter1 uploadAdapter;
    RecyclerView photo_recycler_view;
    FrameLayout frm_show;
    FrameLayout frm_upload_post;
    TextView tv_take_photo,tv_choose_photo,tv_cancel;
    final int PIC_CROP = 2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context=activity=this;
        setContentView(R.layout.activity_post_property_upload_pg);
        frm_single_room=findViewById(R.id.frm_single_room);
        frm_upload_post=findViewById(R.id.upload_id);
        frm_upload_image=findViewById(R.id.upload_img_img_testing);
        photo_recycler_view=findViewById(R.id.photos_recycler_view);
        rl_upload_img=findViewById(R.id.upload_options_rl);
        frm_show=findViewById(R.id.frm_show_image);
        tv_cancel=findViewById(R.id.cancle_tv);
        tv_choose_photo=findViewById(R.id.choose_photo);
        tv_take_photo=findViewById(R.id.take_photo);
        uploadAdapter=new ImageUploadAdapter1(this,bitmapList,this);
        photo_recycler_view.setLayoutManager(new GridLayoutManager(context,3,RecyclerView.VERTICAL,false));
        photo_recycler_view.setAdapter(uploadAdapter);
        frm_single_room.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PostPropertyUploadPg.this, RoomDetails.class));
            }
        });
        frm_upload_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (bitmapList.size() < 10) {
                    CLICK_ON = FIRST_IMAGE;
                    rl_upload_img.setVisibility(View.VISIBLE);
                    frm_show.setVisibility(View.VISIBLE);
                } else {
                    Toast.makeText(context, "You can Upload maximum 10 Photos", Toast.LENGTH_SHORT).show();
                }
            }
        });
        tv_take_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE_SECURE);
                    startActivityForResult(intent, 1);
                    rl_upload_img.setVisibility(View.GONE); }
            }
        });
        tv_choose_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                // intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select Picture"),PIC_CROP_REQUEST);
                // frm_show.setVisibility(View.VISIBLE);
                rl_upload_img.setVisibility(View.GONE);
            }
        });
        rl_upload_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rl_upload_img.setVisibility(View.GONE);
            }
        });
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rl_upload_img.setVisibility(View.GONE);
            }
        });

    }
    String CLICK_ON = "";
    String FIRST_IMAGE = "FirstImage";
    public File bitmapToFile(Bitmap bitmap, String fileNameToSave) {
        File file = null;
        try {
            file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + File.separator + fileNameToSave);
            file.createNewFile();
//Convert bitmap to byte array
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 0, bos); // YOU can also save it in JPEG
            byte[] bitmapdata = bos.toByteArray();
//write the bytes in fil
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
    private static final int PIC_CROP_REQUEST = 10;
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                bitmap = (Bitmap) data.getExtras().get("data");
                Log.e("1258963", bitmap + "");
                File file1 = bitmapToFile(bitmap, getString(R.string.app_name) + "_" + System.currentTimeMillis() + ".jpg");
                filePath = Uri.fromFile(file1);
                bitmap1 = bitmap;
                performCrop(filePath);
                frm_upload_post.setVisibility(View.VISIBLE);
            }if (requestCode == PIC_CROP_REQUEST && data != null) {
                frm_upload_post.setVisibility(View.VISIBLE);
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                    Bundle extras = data.getExtras();
                    bitmap = extras.getParcelable("data");
                } else {
                    Uri uri = data.getData();
                    if (uri != null) {
                        try {
                            bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), uri);
                            filePath = uri;
                            performCrop(filePath);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Bundle extras = data.getExtras();
                        bitmap = extras.getParcelable("data");
                    }
                }
/*                ImagePOJO imagePOJO = new ImagePOJO();
                imagePOJO.bitmap = bitmap;
                imagePOJO.imageUri = filePath;
                bitmapList.add(imagePOJO);
                uploadAdapter.notifyDataSetChanged();*/
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                byte[] byteArrayImage = bytes.toByteArray();
            } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                if (resultCode == RESULT_OK) {
                    Uri resultUri = result.getUri();
                    Bitmap photo1;
                    try {
                        photo1 = MediaStore.Images.Media.getBitmap(context.getContentResolver(), resultUri);
                        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                        photo1.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                        byte[] byteArrayImage = bytes.toByteArray();
                        //encodedImage = Base64.encodeToString(byteArrayImage, Base64.DEFAULT);
                        filePath = resultUri;
                        bitmap = photo1;
                        // binding.ivUser.setImageBitmap(photo1);
                        ImagePOJO imagePOJO = new ImagePOJO();
                        imagePOJO.bitmap = bitmap;
                        imagePOJO.imageUri = filePath;
                        bitmapList.add(imagePOJO);
                        uploadAdapter.notifyDataSetChanged();
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
                } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                    Exception error = result.getError();
                    error.printStackTrace();
                }
            }
        }
        else if (requestCode == PIC_CROP && data != null && data.getClipData() != null){
            int count = data.getClipData().getItemCount();
            //performCrop(filePath);
            for(int i = 0; i < count; i++) {
                Uri imageUri = data.getClipData().getItemAt(i).getUri();
                try {
                    bitmap =uriToBitmap(imageUri);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                filePath=imageUri;
                ImagePOJO imagePOJO = new ImagePOJO();
                imagePOJO.bitmap = bitmap;
                imagePOJO.imageUri = filePath;
                bitmapList.add(imagePOJO);
                //  performCrop(filePath);
            }
            uploadAdapter.notifyDataSetChanged();
        }
        else if (requestCode == PIC_CROP && data != null && data.getData() != null){
            filePath = data.getData();
            //performCrop(filePath);
            try {
                bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), filePath);
                String[] filePathColumn = { MediaStore.Images.Media.DATA };
                // iv_UploadImage.setImageBitmap(bitmap);
                // performCrop(filePath);
            } catch (IOException e) {
                e.printStackTrace();
            }
            // if (CLICK_ON.equals(FIRST_IMAGE)) {
            bitmap1 = bitmap;
            filePath1 = filePath;
            ImagePOJO imagePOJO = new ImagePOJO();
            imagePOJO.bitmap = bitmap;
            imagePOJO.imageUri = filePath;
            bitmapList.add(imagePOJO);
            uploadAdapter.notifyDataSetChanged();
            //   performCrop(filePath);
            //   }
        }
    }
    private Bitmap uriToBitmap(Uri selectedFileUri) {
        Bitmap image=null;
        try {
            ParcelFileDescriptor parcelFileDescriptor =
                    getContentResolver().openFileDescriptor(selectedFileUri, "r");
            FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
            image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
            parcelFileDescriptor.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }
    @Override
    public void removeImage(int position) {
        bitmapList.remove(position);
        uploadAdapter.notifyDataSetChanged();
        frm_show.setVisibility(View.GONE);
        frm_upload_post.setVisibility(View.GONE);
    }
    private void performCrop(Uri fileUri) {
        try {
            CropImage.ActivityBuilder a1 = CropImage.activity(fileUri);
            a1.setCropMenuCropButtonTitle("Set Image");
            a1.setAspectRatio(1, 1);
            a1.start(activity);
        } catch (ActivityNotFoundException anfe) {
            //display an error message
            String errorMessage = "Whoops - your device doesn't support the crop action!";
            Toast toast = Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT);
            toast.show();
        }
    }

}