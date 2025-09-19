package com.app_neighbrsnook.event;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app_neighbrsnook.adapter.EventCommentAdapter;
import com.app_neighbrsnook.adapter.ImageSliderDialogAdapter;
import com.app_neighbrsnook.group.GroupActivity;
import com.app_neighbrsnook.model.postComment.CommentPojo;
import com.app_neighbrsnook.pojo.eventDetails.EventListComentPojo;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.JsonElement;
import com.app_neighbrsnook.R;
import com.app_neighbrsnook.adapter.EventDetailsMemberJoinList;
import com.app_neighbrsnook.adapter.EventLikesMemberListAdapter;
import com.app_neighbrsnook.adapter.ImageSlideAdapter;
import com.app_neighbrsnook.adapter.ImageSlideAdapterEventMltpl;
import com.app_neighbrsnook.adapter.ImageUploadAdapter;
import com.app_neighbrsnook.adapter.ImageUploadAdapter1;
import com.app_neighbrsnook.adapter.NonAttendeesMemberAdapterList;
import com.app_neighbrsnook.login.AddressResponse;
import com.app_neighbrsnook.model.ImagePOJO;
import com.app_neighbrsnook.network.APIClient;
import com.app_neighbrsnook.network.APIInterface;
import com.app_neighbrsnook.pojo.DetailsImagesArrayShowMlt;
import com.app_neighbrsnook.pojo.EventDetailsJoinMemberListPojo;
import com.app_neighbrsnook.pojo.EventDetailsPojo;
import com.app_neighbrsnook.pojo.EventDetailsUserListPojo;
import com.app_neighbrsnook.pojo.EventLikesStatusPojo;
import com.app_neighbrsnook.pojo.EventUsersLikesEvent;
import com.app_neighbrsnook.pojo.ImagePojo;
import com.app_neighbrsnook.pojo.JoinEventUserListPojo;
import com.app_neighbrsnook.pojo.NonAttendeesPojoinDetails;
import com.app_neighbrsnook.profile.MyProfileOtherUser;
import com.app_neighbrsnook.utils.GlobalMethods;
import com.app_neighbrsnook.utils.PrefMananger;
import com.app_neighbrsnook.utils.SharedPrefsManager;
import com.squareup.picasso.Picasso;
import com.app_neighbrsnook.libraries.cropper.CropImage;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserViewEvent extends AppCompatActivity implements ImageUploadAdapter1.ImageItemClick, EventDetailsMemberJoinList.NewRequest,
        ImageSlideAdapterEventMltpl.ImgRequest, EventLikesMemberListAdapter.LikesRequest, ImageUploadAdapter.ImageRequest, ImageSlideAdapter.ImgRequest {
    TextView tv_yes, tv_no, tv_tym_neighbrhod, image_count_textview, tvCommentCount;
    FrameLayout frm_yes_layout, frmEditEvent;
    Activity activity;
    Context context;
    RelativeLayout rl_upload_layout, rlUploadImages;
    String st_user_id;
    int compressionRatio = 25;
    int totalImageCount;
    File imageFile;

    ArrayList<ImagePOJO> bitmapList = new ArrayList<>();
    TextView tv_take_photo, tv_choose_photo;
    ImageUploadAdapter1 uploadAdapter;
    ImageUploadAdapter imageUploadAdapter;
    ImageSlideAdapterEventMltpl imageSlideAdapter, ownerImageAdapter;
    FrameLayout frm_upload_img_lyt, lnr_owner;
    TextView tv_cancel;
    ImageView img_back;
    LinearLayout lnr_yes, lnr_no;
    String st_title, st_about, st_endEvent, stCommnet, st_username, st_startDate, st_endDate, st_startTime, st_endTime, st_attendees, st_non_attds, st_total_likes, st_address1, st_address2, st_tym_ngh, type;
    SharedPrefsManager sm;
    ImageView event_cover_image, img_user_pic_details;
    TextView event_title, tv_about, tv_owner, tv_title_child, tv_start_date_details, tv_end_date, tv_start_time, tv_end_time, tv_attendees, tv_non_attendees, tv_total_likes, tv_address1, tv_address2;
    int id;
    FrameLayout img_dlt_icn;
    List<EventDetailsUserListPojo> groupListPojos = new ArrayList<>();
    List<NonAttendeesPojoinDetails> nonAttendPojo = new ArrayList<>();
    List<EventUsersLikesEvent> likesAttendeesPojo = new ArrayList<>();
    List<EventDetailsJoinMemberListPojo> eventJoinDetailsList = new ArrayList<>();

    List<ImagePojo> ar1 = new ArrayList<>();
    List<ImagePojo> ar2 = new ArrayList<>();
    EventDetailsMemberJoinList eventMemberList;
    EventLikesMemberListAdapter eventLikesMemberListAdapter;
    NonAttendeesMemberAdapterList nonAttendeesAdapter;
    ImageView img_likes, img_blue_likes, img_details_show;
    Boolean is_current = false;
    Boolean is_upcoming = false;
    FrameLayout frm_uplad_id;
    RecyclerView for_you_rv;
    LinearLayoutManager layoutManager;

    ArrayList<String> imageList1 = new ArrayList<>();
    ArrayList<String> imageList = new ArrayList<>();
    HashMap<String, Object> hm = new HashMap<>();
    FrameLayout frm_layout;
    RecyclerView photos_recycler_view;
    ImageView add_imageview1;
    Uri imageUri;
    CardView card_owner, card_user;

    Bitmap photo1, galleryPhoto;
    ArrayList<Bitmap> mArrayGalleryPhoto = new ArrayList<>();
    Bitmap selectedImage, pdfBitmap, bitmap, bitmap1;
    Uri filePath, videoUri, pdfuri, pdfuri1;
    private final int REQUEST_CODE_DOC = 5;
    private final int CROP_PIC = 9;
    // EventDetailsPojo root;
    int count1, count2, alreadyUploadCount = 0;


    String currentPhotoPath, takePicFromCamera;
    private final int TAKE_VIDEO_FROM_GALLARY = 1;
    private final int TAKE_VIDEO_FROM_CAMERA = 2;
    private final int TAKE_PIC_FROM_GALLARY = 3;
    private final int TAKE_PIC_FROM_CAMERA = 4;
    TextView image_textview, doc_textview;
    Dialog mail_dialog, image_dialog;
    ArrayList<Uri> mArrayUri = new ArrayList();
    ArrayList<Uri> mArrayUri1 = new ArrayList<>();
    ArrayList<String> mArrayFile = new ArrayList<>();
    String CLICK_ON = "";
    String FIRST_IMAGE = "FirstImage";
    private static final String VIDEO_DIRECTORY = "/demonuts";
    RecyclerView rec_owner;
    ProgressDialog progressDialog;
    EventDetailsPojo eventDetailsPojo;
    ImagePojo model2;
    private int maxImageCount = 0;
    LinearLayout commntEvent, rv_ll;
    EditText msg_et;
    ImageView send_imageview;
    RecyclerView comment_rv;
    List<EventListComentPojo> commentList = new ArrayList<>();
    EventCommentAdapter commentAdapter;
    AddressResponse root;


    @SuppressLint({"MissingInflatedId", "WrongViewCast"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = activity = this;
        setContentView(R.layout.activity_user_view_event);
        for_you_rv = findViewById(R.id.for_you_rv);
        sm = new SharedPrefsManager(this);
        comment_rv = findViewById(R.id.comment_rv);
        event_cover_image = findViewById(R.id.event_cover_image);
        add_imageview1 = findViewById(R.id.add_imageview1);
        doc_textview = findViewById(R.id.doc_textview);
        rec_owner = findViewById(R.id.rec_owner);
        card_owner = findViewById(R.id.card_owner);
        rv_ll = findViewById(R.id.rv_ll);
        card_user = findViewById(R.id.card_user);
        tv_address2 = findViewById(R.id.tv_address_two);
        image_textview = findViewById(R.id.image_textview);
        frm_layout = findViewById(R.id.frm_layout);
        img_blue_likes = findViewById(R.id.img_blue_likes);
        img_likes = findViewById(R.id.img_likes_event);
        img_dlt_icn = findViewById(R.id.delete_icon_img);
        frm_upload_img_lyt = findViewById(R.id.upload_img_img_testing);
        tv_tym_neighbrhod = findViewById(R.id.tv_tym_neighbrhod);
        tv_address1 = findViewById(R.id.tv_address_1);
        frm_uplad_id = findViewById(R.id.upload_id);
        event_title = findViewById(R.id.event_title_id);
        tv_title_child = findViewById(R.id.tv_title_child);
        tv_end_date = findViewById(R.id.tv_end_date_details);
        tv_start_time = findViewById(R.id.tv_start_time_details);
        tv_end_time = findViewById(R.id.tv_end_time_details);
        tv_attendees = findViewById(R.id.tv_attendees);
        tv_non_attendees = findViewById(R.id.tv_non_attendees);
        tvCommentCount = findViewById(R.id.tvCommentCount);
        tv_yes = findViewById(R.id.yes_id);
        tv_start_date_details = findViewById(R.id.tv_start_date_details);
        tv_owner = findViewById(R.id.tv_owner);
        tv_about = findViewById(R.id.tv_about);
        lnr_owner = findViewById(R.id.lnr_owner);
        msg_et = findViewById(R.id.msg_et);
        rlUploadImages = findViewById(R.id.rlUploadImages);
        img_user_pic_details = findViewById(R.id.img_user_pic_details);
        image_count_textview = findViewById(R.id.image_count_textview);
        maxImageCount = Integer.parseInt(sm.getString("event_img_limit"));
        image_count_textview.setText("count : " + sm.getString("event_img_limit"));
        commntEvent = findViewById(R.id.commntEvent);
        comment_rv.setLayoutManager(new LinearLayoutManager(this));

//        Intent intent = getIntent();
        Bundle intent = getIntent().getExtras();
        assert intent != null;
        id = intent.getInt("data", 0);
        eventUserListDialog();
        eventNonAttendees();
        eventLikesUserlist();
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        for_you_rv.setLayoutManager(layoutManager);
        // type = intent.getStringExtra("data");
        eventDetailsShowApi(id);
        tv_no = findViewById(R.id.no_id);
        tv_total_likes = findViewById(R.id.tv_total_likes);
        lnr_yes = findViewById(R.id.lnr_yes);
        lnr_no = findViewById(R.id.lnr_no);
        tv_cancel = findViewById(R.id.cancle_tv);
        frm_yes_layout = findViewById(R.id.frame_layout_yes);
        photos_recycler_view = findViewById(R.id.photos_recycler_view);
        tv_take_photo = findViewById(R.id.take_photo);
        tv_choose_photo = findViewById(R.id.choose_photo);
        send_imageview = findViewById(R.id.send_imageview);
        rl_upload_layout = findViewById(R.id.upload_options_rl_user_view);
        img_back = findViewById(R.id.img_back);
        frmEditEvent = findViewById(R.id.frmEditEvent);

        lnr_owner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, MyProfileOtherUser.class);
                i.putExtra("user_id", Integer.parseInt(st_user_id));
                context.startActivity(i);

            }
        });
        uploadAdapter = new ImageUploadAdapter1(this, bitmapList, this);
        photos_recycler_view.setLayoutManager(new GridLayoutManager(context, 4, RecyclerView.VERTICAL, false));
        photos_recycler_view.setAdapter(uploadAdapter);
        progressDialog = new ProgressDialog(context);
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
        //commentListApi();
        send_imageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //eventCommentApi();
            }
        });

        commntEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                msg_et.requestFocus();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(msg_et, InputMethodManager.SHOW_IMPLICIT);

            }
        });


        //  GlobalMethods.checkPermission(this);
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
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
                    imageUri = FileProvider.getUriForFile(UserViewEvent.this, "com.app_neighbrsnook.fileprovider", imageFile);

                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                    startActivityForResult(intent, TAKE_PIC_FROM_CAMERA);

                } catch (IOException e) {
                    e.printStackTrace();
                }
                frm_uplad_id.setVisibility(VISIBLE);


                //capturePhoto();
            }
        });
        tv_choose_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), TAKE_PIC_FROM_GALLARY);
                rl_upload_layout.setVisibility(GONE);
                frm_uplad_id.setVisibility(VISIBLE);
            }
        });

        rl_upload_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rl_upload_layout.setVisibility(View.GONE);
            }
        });
        image_textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageDialog(mArrayUri, "img");
            }
        });
        rlUploadImages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (GlobalMethods.checkCameraAndGalleryPermission(UserViewEvent.this)) {
                    if (mArrayUri.size() < totalImageCount) {
                        rl_upload_layout.setVisibility(View.VISIBLE);
                    } else {
                        rl_upload_layout.setVisibility(View.GONE);
                        GlobalMethods.getInstance(UserViewEvent.this).globalDialog(UserViewEvent.this, "You have already uploaded max images allowed.");
                    }
                }
            }
        });
        add_imageview1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (GlobalMethods.checkCameraAndGalleryPermission(UserViewEvent.this)) {
                    if (mArrayUri.size() < totalImageCount) {
                        rl_upload_layout.setVisibility(View.VISIBLE);
                    } else {
                        rl_upload_layout.setVisibility(View.GONE);
                        GlobalMethods.getInstance(UserViewEvent.this).globalDialog(UserViewEvent.this, "You have already uploaded max images allowed.");
                    }
                }
            }
        });
        frm_uplad_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.show();
                frm_uplad_id.setVisibility(GONE);

                new Handler().postDelayed(() -> {
                    postApi(hm);
                }, 200);
            }
        });


        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rl_upload_layout.setVisibility(View.GONE);
            }
        });
        img_likes.setOnClickListener(view -> {
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("userid", sm.getString("user_id"));
            hashMap.put("eventid", id);
            hashMap.put("username", sm.getString("user_name"));
            hashMap.put("status", "1");
            hashMap.put("usercase", "LIKES");
            likesEvent(hashMap);
            eventDetailsShowApi(id);
        });
        tv_yes.setOnClickListener(view -> {
            resetColor();
            tv_yes.setBackgroundResource(R.drawable.select_background_event_list);
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("userid", sm.getString("user_id"));
            //hashMap.put("eventid",eventData.getId());
            hashMap.put("eventid", id);
            hashMap.put("username", sm.getString("user_name"));
            hashMap.put("status", "1");
            hashMap.put("usercase", "ATTEMPT");
            joinEvent(hashMap);
            eventDetailsShowApi(id);

        });
        tv_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetColor();
                tv_no.setBackgroundResource(R.drawable.red_backgound);
                frm_yes_layout.setVisibility(GONE);

                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put("userid", sm.getString("user_id"));
                //hashMap.put("eventid",eventData.getId());
                hashMap.put("eventid", id);
                hashMap.put("username", sm.getString("user_name"));
                hashMap.put("status", "0");
                hashMap.put("usercase", "ATTEMPT");
                noJoinEvent(hashMap);

            }
        });
        img_dlt_icn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new Dialog(UserViewEvent.this);
                dialog.setContentView(R.layout.delete_event_layout);
                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                lp.copyFrom(dialog.getWindow().getAttributes());
                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                lp.gravity = Gravity.CENTER;
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(UserViewEvent.this, android.R.color.transparent)));
                dialog.getWindow().setAttributes(lp);
                dialog.show();
                ImageView img = dialog.findViewById(R.id.iv9);
                TextView tv_yes = dialog.findViewById(R.id.tv_yes_id);
                TextView tv_no = dialog.findViewById(R.id.tv_no);
                tv_no.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                tv_yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        HashMap<String, Object> hm = new HashMap<>();

                        //  hm.put("userid", Integer.parseInt(sm.getString("user_id")));
                        hm.put("userid", Integer.parseInt(PrefMananger.GetLoginData(context).getId() + ""));

                        hm.put("e_id", id);
                        apiDeleteEvent(hm);


                    }
                });
                img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
            }
        });


        frmEditEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, UpdateEventActivity.class);
                i.putExtra("id", id);
                context.startActivity(i);

            }
        });
    }

    private void resetColor() {
        tv_yes.setBackgroundResource(R.drawable.yes_no_button_background);
        tv_no.setBackgroundResource(R.drawable.yes_no_button_background);
    }

    String currentPath = "";

    @Override
    public void removeImage(int position) {
        bitmapList.remove(position);
        uploadAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(int pos) {
    }

    private void joinEvent(HashMap<String, Object> hm) {
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Please wait...");
        dialog.setCancelable(false);
        dialog.show();
        APIInterface service = APIClient.getRetrofit().create(APIInterface.class);
        Call<JsonElement> call = service.eventJoinRequest("userjoinevent", hm);
        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                dialog.dismiss();
                if (response.isSuccessful()) {
                    //   card_user.setVisibility(VISIBLE);
                    //  card_owner.setVisibility(VISIBLE);

                }
                Toast.makeText(context, "Event joined succesfully ", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("res", t.getMessage());
            }
        });
    }

    private void noJoinEvent(HashMap<String, Object> hm) {
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Please wait...");
        dialog.setCancelable(false);
        dialog.show();
        APIInterface service = APIClient.getRetrofit().create(APIInterface.class);
        Call<JsonElement> call = service.eventJoinRequest("userjoinevent", hm);
        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                dialog.dismiss();
                if (response.isSuccessful()) {
                    tv_no.setBackgroundResource(R.drawable.red_backgound);
                    card_owner.setVisibility(GONE);
                    card_user.setVisibility(GONE);
                }
                Toast.makeText(context, "Noted", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("res", t.getMessage());
            }
        });
    }

    private void likesEvent(HashMap<String, Object> hm) {
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Please wait...");
        dialog.setCancelable(false);
        dialog.show();
        APIInterface service = APIClient.getRetrofit().create(APIInterface.class);
        Call<JsonElement> call = service.eventJoinRequest("userjoinevent", hm);
        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                dialog.dismiss();
                //*      Intent i = new Intent(GroupMemberListSecond.this, GroupActivityShow.class);
                //  startActivity(i);
                // finish();
                //finishAffinity();//*
                eventLikesUserlist();
                if (response.isSuccessful()) {
                    img_blue_likes.setVisibility(View.VISIBLE);
                    img_likes.setVisibility(GONE);
                    eventDetailsShowApi(id);
                    // notify();
                }
                Toast.makeText(context, "Likes", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("res", t.getMessage());
            }
        });
    }

    private void eventDetailsShowApi(int id) {
        ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Please wait...");
        dialog.setCancelable(false);
        dialog.show();
        HashMap<String, Object> hm = new HashMap<>();
        hm.put("userid", Integer.parseInt(sm.getString("user_id")));
        hm.put("eventid", id);
        // Log.e("hhjhjhj",hm.toString());
        APIInterface service = APIClient.getRetrofit().create(APIInterface.class);
        Call<EventDetailsPojo> call = service.eventDetails("viewalldataeventlistdetails", hm);
        call.enqueue(new Callback<EventDetailsPojo>() {
            @Override
            public void onResponse(Call<EventDetailsPojo> call, Response<EventDetailsPojo> response) {
                EventDetailsPojo rootObject = response.body();
                dialog.dismiss();
                try {
                    totalImageCount = Integer.parseInt(response.body().getEvent_img_remain_limit());
                    maxImageCount = Integer.parseInt(response.body().getEvent_img_limit());
                    image_count_textview.setText("Max images: " + response.body().getEvent_img_remain_limit());
                    st_title = String.valueOf(rootObject.getTitle());
                    st_username = String.valueOf(rootObject.getCreateby());
                    st_about = String.valueOf(rootObject.getEventDetail());
                    // st_tym_ngh=rootObject.get
                    st_startDate = rootObject.getEventStartDate();
                    st_endDate = rootObject.getEventEndDate();
                    st_startTime = rootObject.getEventStarttime();
                    st_endTime = rootObject.getEventEndtime();
                    st_attendees = rootObject.getTotalJoin();
                    st_non_attds = rootObject.getNojoin();
                    st_total_likes = rootObject.getTotalLike();
                    st_address1 = rootObject.getAddlineone();
                    st_address2 = rootObject.getAddlinetwo();
                    st_tym_ngh = rootObject.getDatetimeandneighbrhood();
                    st_endEvent = rootObject.getIseventrunning();
                    stCommnet = rootObject.getTotcomment();
                    tv_owner.setText(st_username);
                    st_user_id = rootObject.getUserid();
                    tv_about.setText(st_about);
                    event_title.setText(st_title);
                    tv_title_child.setText(st_title);
                    tv_start_date_details.setText(st_startDate);
                    tv_end_date.setText(st_endDate);
                    tv_start_time.setText(st_startTime);
                    tv_end_time.setText(st_endTime);
                    tv_attendees.setText(st_attendees);
                    tv_non_attendees.setText(st_non_attds);
                    tv_total_likes.setText(st_total_likes);
                    tv_address1.setText(st_address1);
                    tv_address2.setText(st_address2);
                    tv_tym_neighbrhod.setText(st_tym_ngh);
                    tvCommentCount.setText(stCommnet);

                    List<ImagePojo> ar = rootObject.getImages();
                    ar1 = new ArrayList<>();
                    ar2 = new ArrayList<>();
                    lnr_yes.setOnClickListener(view -> {
                        if (!"0".equals(st_non_attds)) {
                            nonAttendees();
                        }
                    });
                    lnr_yes.setOnClickListener(view -> {
                        if (!"0".equals(st_attendees)) {
                            yesAttendeesMember();
                        }
                    });
                    tv_total_likes.setOnClickListener(view -> {
                        if ("0".equals(st_total_likes)) {
                        } else {
                            likesAttendeesMemberList();

                        }
                    });
                    for (ImagePojo data : ar) {
                        data.setOwner(false);
                        if (data.getUserid().equals(sm.getString("user_id"))) {
                            alreadyUploadCount++;
                        }
                        if (data.getType().equals("owner")) {
                            ar1.add(data);
                        } else {
                            ar2.add(data);
                        }

                        if (data.getType().equals("owner")) {
                            count1 = ar1.size();
                        } else {
                            count2 = ar2.size();
                        }
                    }
                    if (ar1.isEmpty()) {
                        card_owner.setVisibility(GONE);
                    } else {
                        card_owner.setVisibility(VISIBLE);
                    }
                    if (ar2.isEmpty()) {
                        card_user.setVisibility(GONE);
                    } else {
                        card_user.setVisibility(VISIBLE);
                    }

                    rec_owner.setLayoutManager(new GridLayoutManager(context, 4, RecyclerView.VERTICAL, false));
                    ownerImageAdapter = new ImageSlideAdapterEventMltpl(ar1, UserViewEvent.this);
                    rec_owner.setAdapter(ownerImageAdapter);
                    if (rootObject.getUserid().equals(sm.getString("user_id"))) {
                        for (ImagePojo pojo : ar2) {
                            pojo.setOwner(true);
                            //  pojo.setUserid(sm.getString("user_id"));
                        }
                    }
                    for_you_rv.setLayoutManager(new GridLayoutManager(context, 4, RecyclerView.VERTICAL, false));
                    imageSlideAdapter = new ImageSlideAdapterEventMltpl(ar2, UserViewEvent.this);

                    for_you_rv.setAdapter(imageSlideAdapter);

                    if (rootObject.getUserlikes().equals("0")) {
                        img_likes.setVisibility(VISIBLE);
                        img_blue_likes.setVisibility(GONE);
                    } else if (rootObject.getUserlikes().equals("1")) {
                        img_blue_likes.setVisibility(VISIBLE);
                        img_likes.setVisibility(GONE);
                    }
                    {
                        Picasso.get().load(rootObject.getUserpic()).fit().fit().error(R.drawable.marketplace_white_background).placeholder(R.drawable.marketplace_white_background)
                                .into(img_user_pic_details);
                    }
                    //  Picasso.get().load(rootObject.getCoverImage()).into(event_cover_image);
                    if (rootObject.getCoverImage().isEmpty()) {
                        event_cover_image.setImageResource(R.drawable.marketplace_white_background);
                    } else {
                        Glide.with(context)
                                .load(rootObject.getCoverImage())
                                .apply(RequestOptions.centerCropTransform())
                                .into(event_cover_image);
                        //  Picasso.get().load(rootObject.getCoverImage()).fit().into(event_cover_image);
                    }
                } catch (Exception e) {
                }
                eventJoinDetailsList = rootObject.getUserjoinmemberlist();

                for (int i = 0; i < eventJoinDetailsList.size(); i++) {
                    if (sm.getString("user_id").equals(eventJoinDetailsList.get(i).getUserid())) {
                        resetColor();
                        if (eventJoinDetailsList.get(i).getStatus().equals("1")) {
                            tv_yes.setBackgroundResource(R.drawable.select_background_event_list);
                            frm_yes_layout.setVisibility(VISIBLE);
                        } else if (eventJoinDetailsList.get(i).getStatus().equals("0")) {
                            tv_no.setBackgroundResource(R.drawable.red_backgound);
                            frm_yes_layout.setVisibility(View.GONE);
                        }
                    }
                }
                if (sm.getString("user_id").equals(rootObject.getUserid())) {
                    img_dlt_icn.setVisibility(VISIBLE);
                    frmEditEvent.setVisibility(VISIBLE);
                    frm_upload_img_lyt.setVisibility(GONE);
                    lnr_yes.setVisibility(VISIBLE);
                    lnr_no.setVisibility(VISIBLE);
                    frm_yes_layout.setVisibility(VISIBLE);
                }
                else {
                    frmEditEvent.setVisibility(GONE);
                    img_dlt_icn.setVisibility(GONE);
                    frm_upload_img_lyt.setVisibility(VISIBLE);
                    lnr_yes.setVisibility(GONE);
                    lnr_no.setVisibility(GONE);
                }
                if (rootObject.getIseventrunning().equals("1") && rootObject.getFutureeventstatus().equals("1")) {
                    if (sm.getString("user_id").equals(rootObject.getUserid())) {
                        frm_upload_img_lyt.setVisibility(GONE);
                        frm_yes_layout.setVisibility(VISIBLE);
                    } else {
                        frm_upload_img_lyt.setVisibility(VISIBLE);
                        frm_yes_layout.setVisibility(GONE);
                    }
                } else if (rootObject.getIseventrunning().equals("0") && rootObject.getFutureeventstatus().equals("0")) {
                    frm_upload_img_lyt.setVisibility(GONE);
                    frm_yes_layout.setVisibility(GONE);
                }

            }

            @Override
            public void onFailure(Call<EventDetailsPojo> call, Throwable t) {
                Toast.makeText(UserViewEvent.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                Log.d("res", t.getMessage());
            }
        });
    }

    private void apiDeleteEvent(HashMap<String, Object> hm) {
        APIInterface service = APIClient.getRetrofit().create(APIInterface.class);
        Call<JsonElement> call = service.deleteEvent("deleteevent", hm);
        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                Intent i = new Intent(UserViewEvent.this, EventAllListCurrentData.class);
                i.putExtra("neighbrhood", "drawar");

                startActivity(i);
                finish();
                Toast.makeText(UserViewEvent.this, "Event deleted successful", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                Toast.makeText(UserViewEvent.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("res", t.getMessage());
            }
        });
    }

    public void yesAttendeesMember() {
        Dialog dialog = new Dialog(UserViewEvent.this);
        dialog.setContentView(R.layout.neighbourshood_dialog);
        RecyclerView recy = dialog.findViewById(R.id.rec_event_join_user_list);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        recy.setLayoutManager(new LinearLayoutManager(this));
        eventMemberList = new EventDetailsMemberJoinList(groupListPojos, dialog);
        recy.setAdapter(eventMemberList);
/*
        GroupDetailsbyNameResponse groupDetailsbyNameResponse=new GroupDetailsbyNameResponse();
        groupDetailsbyNameResponse=response.body();
        memberlist=groupDetailsbyNameResponse.getMemberlist();
        groupListAdapter=new GroupDetailsJoinDetailsAdapter(memberlist);
        rec_details.setAdapter(groupListAdapter);

*/
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(UserViewEvent.this, android.R.color.transparent)));
        dialog.getWindow().setAttributes(lp);
        dialog.show();
        ImageView img_close = dialog.findViewById(R.id.iv9);
        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }

    public void likesAttendeesMemberList() {
        Dialog dialog = new Dialog(UserViewEvent.this);
        dialog.setContentView(R.layout.likes_attendees_list);
        RecyclerView recy = dialog.findViewById(R.id.rec_event_join_user_list);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        recy.setLayoutManager(new LinearLayoutManager(this));
        eventLikesMemberListAdapter = new EventLikesMemberListAdapter(likesAttendeesPojo, dialog);
        recy.setAdapter(eventLikesMemberListAdapter);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(UserViewEvent.this, android.R.color.transparent)));
        dialog.getWindow().setAttributes(lp);
        dialog.show();
        ImageView img_close = dialog.findViewById(R.id.iv9);
        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }

    public void nonAttendees() {
        Dialog dialog = new Dialog(UserViewEvent.this);
        dialog.setContentView(R.layout.list_participain_by_no);
        RecyclerView recy = dialog.findViewById(R.id.rec_event_join_user_list);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        recy.setLayoutManager(new LinearLayoutManager(this));
        nonAttendeesAdapter = new NonAttendeesMemberAdapterList(nonAttendPojo, dialog);
        recy.setAdapter(nonAttendeesAdapter);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(UserViewEvent.this, android.R.color.transparent)));
        dialog.getWindow().setAttributes(lp);
        dialog.show();
        ImageView img_close = dialog.findViewById(R.id.iv9);
        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }

    private void eventUserListDialog() {
        HashMap<String, Object> hm = new HashMap<>();
        //  hm.put("eventid","198");
        hm.put("userid", Integer.parseInt(sm.getString("user_id")));
        hm.put("eventid", id);
        hm.put("type", "1");

//        AppCommon.getInstance(BusinessActivity.this).setNonTouchableFlags(BusinessActivity.this);
        APIInterface service = APIClient.getRetrofit().create(APIInterface.class);
        Call<JoinEventUserListPojo> call = service.eventUserListDetails("eventaddjoinlist", hm);
        call.enqueue(new Callback<JoinEventUserListPojo>() {
            @Override
            public void onResponse(Call<JoinEventUserListPojo> call, Response<JoinEventUserListPojo> response) {
                groupListPojos = response.body().getListdata();
            }

            @Override
            public void onFailure(Call<JoinEventUserListPojo> call, Throwable t) {
                Toast.makeText(UserViewEvent.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("res---", t.getMessage());
            }
        });
    }

    private void eventNonAttendees() {
        HashMap<String, Object> hm = new HashMap<>();
        //  hm.put("eventid","198");
        hm.put("userid", Integer.parseInt(sm.getString("user_id")));
        hm.put("eventid", id);
        // hm.put("type","1");

//        AppCommon.getInstance(BusinessActivity.this).setNonTouchableFlags(BusinessActivity.this);
        APIInterface service = APIClient.getRetrofit().create(APIInterface.class);
        Call<EventDetailsPojo> call = service.eventDetails("viewalldataeventlistdetails", hm);
        call.enqueue(new Callback<EventDetailsPojo>() {
            @Override
            public void onResponse(Call<EventDetailsPojo> call, Response<EventDetailsPojo> response) {
                nonAttendPojo = response.body().getUserunjoinmemberlist();
            }

            @Override
            public void onFailure(Call<EventDetailsPojo> call, Throwable t) {
                Toast.makeText(UserViewEvent.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("res---", t.getMessage());
            }
        });
    }

    private void eventLikesUserlist() {
        HashMap<String, Object> hm = new HashMap<>();
        //  hm.put("eventid","198");
        //  hm.put("userid",Integer.parseInt(sm.getString("user_id")));
        hm.put("eventid", id);
//        AppCommon.getInstance(BusinessActivity.this).setNonTouchableFlags(BusinessActivity.this);
        APIInterface service = APIClient.getRetrofit().create(APIInterface.class);
        Call<EventLikesStatusPojo> call = service.eventLikesUserLists("eventlikeslist", hm);
        call.enqueue(new Callback<EventLikesStatusPojo>() {
            @Override
            public void onResponse(Call<EventLikesStatusPojo> call, Response<EventLikesStatusPojo> response) {
                likesAttendeesPojo = response.body().getListdata();
            }

            @Override
            public void onFailure(Call<EventLikesStatusPojo> call, Throwable t) {
                Toast.makeText(UserViewEvent.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("res---", t.getMessage());
            }
        });
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
        if (req == TAKE_PIC_FROM_GALLARY && result == RESULT_OK && null != data) {
            //  Uri uri = data.getData();
            if (data.getClipData() != null) {
                ClipData mClipData = data.getClipData();
                int cout = totalImageCount - mArrayUri.size();
                for (int i = 0; i < mClipData.getItemCount(); i++) {
                    if (i < cout) {
                        Uri imageurl = data.getClipData().getItemAt(i).getUri();
                        try {
                            galleryPhoto = MediaStore.Images.Media.getBitmap(context.getContentResolver(), imageurl);
                            mArrayGalleryPhoto.add(galleryPhoto);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        mArrayUri.add(imageurl);
                    }
                }
            } else {
                Uri imageurl = data.getData();
                mArrayUri.add(imageurl);
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageurl);
                    String takePicFromGallery = GlobalMethods.convertBitmapToString(bitmap);
                    mArrayGalleryPhoto.add(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (mArrayUri.size() == 1) {
                image_textview.setVisibility(View.VISIBLE);
                image_textview.setText(mArrayUri.size() + " Preview");
            }
            if (mArrayUri.size() >= 1) {
                image_textview.setVisibility(View.VISIBLE);
                image_textview.setText(mArrayUri.size() + " Preview");
            }
            if (mArrayUri.size() == 0) {
                image_textview.setVisibility(View.GONE);
            }
        } else if (req == TAKE_PIC_FROM_CAMERA && result == RESULT_OK) {
            Bitmap bm = BitmapFactory.decodeFile(currentPhotoPath);
//            filePath = bitmapToUri(this, bm);
            filePath = imageUri;
            performCrop(filePath);

        } else if (req == REQUEST_CODE_DOC && result == RESULT_OK) {
            if (data.getClipData() != null) {
                ClipData mClipData = data.getClipData();
                int cout = data.getClipData().getItemCount();
                Bitmap bitmap;
                for (int i = 0; i < cout; i++) {
                    Uri imageurl = data.getClipData().getItemAt(i).getUri();
                    mArrayUri1.add(imageurl);
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageurl);
                        String takePicFromGallery = GlobalMethods.convertBitmapToString(bitmap);
                        imageList1.add(takePicFromGallery);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            } else {
                Uri imageurl = data.getData();
                mArrayUri1.add(imageurl);
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageurl);
                    String takePicFromGallery = GlobalMethods.convertBitmapToString(bitmap);
                    imageList1.add(takePicFromGallery);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (mArrayUri1.size() == 1) {
                doc_textview.setVisibility(View.VISIBLE);
                doc_textview.setText(mArrayUri1.size() + " Image");
            }
            if (mArrayUri1.size() >= 1) {
                doc_textview.setVisibility(View.VISIBLE);
                doc_textview.setText(mArrayUri1.size() + " Images");
            }
            if (mArrayUri1.size() == 0) {
                doc_textview.setVisibility(View.GONE);
            }
        }//not user i think ref createsellActivity

        else if (req == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result1 = CropImage.getActivityResult(data);
            if (result == RESULT_OK) {
                Uri resultUri = result1.getUri();
                mArrayUri.add(resultUri);
                Bitmap photo1;
                try {
                    photo1 = MediaStore.Images.Media.getBitmap(context.getContentResolver(), resultUri);
                    mArrayGalleryPhoto.add(photo1);
                    filePath = resultUri;
                    bitmap = photo1;

                } catch (IOException e) {
                    e.printStackTrace();
                }
                takePicFromCamera = GlobalMethods.convertBitmapToString(uriToBitmap(resultUri));
            }

            if (mArrayUri.size() == 1) {
                image_textview.setVisibility(View.VISIBLE);
                image_textview.setText(mArrayUri.size() + " Image");
            }
            if (mArrayUri.size() >= 1) {
                image_textview.setVisibility(View.VISIBLE);
                image_textview.setText(mArrayUri.size() + " Images");
            }
            if (mArrayUri.size() == 0) {
                image_textview.setVisibility(View.GONE);
            }

        } else if (req == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
        }
    }

    @Override
    public void onClickImgSec(List<DetailsImagesArrayShowMlt> img) {
    }

    @Override
    public void onImageRemove(ImagePojo model) {


        exitLayout(model);


        // GroupDetailsbyNamePojo pojo = memberlist.get(position);


    }

    @Override
    public void onClickImgView(List<ImagePojo> img) {
        imageDialogApi(img);
    }

    private void performCrop(Uri fileUri) {
        try {
            CropImage.ActivityBuilder a1 = CropImage.activity(fileUri);
            a1.setCropMenuCropButtonTitle("Set Image");
//            a1.setRotationDegrees(90);
            a1.setAspectRatio(4, 1);
//            a1.setMultiTouchEnabled(true);
            a1.setFixAspectRatio(false);
            a1.start(activity);
        } catch (ActivityNotFoundException anfe) {

            //display an error message
            String errorMessage = "Whoops - your device doesn't support the crop action!";
            Toast toast = Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    @Override
    public void onImageClick(int pos) {
    }

    @Override
    public void onCrossClick(int pos, String from) {
        mArrayUri.remove(pos);
        mArrayGalleryPhoto.remove(pos);
        //imageList.remove(pos);
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
    }

    @Override
    public void onCrossClick1(int pos, String from) {
        mArrayUri.remove(pos);
        mArrayGalleryPhoto.remove(pos);
        //imageList.remove(pos);
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
    }

    public String getPath(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if (cursor == null) return null;
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String s = cursor.getString(column_index);
        cursor.close();
        return s;
    }

    private Bitmap uriToBitmap(Uri selectedFileUri) {
        try {
            ParcelFileDescriptor parcelFileDescriptor =
                    getContentResolver().openFileDescriptor(selectedFileUri, "r");
            FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
            pdfBitmap = BitmapFactory.decodeFileDescriptor(fileDescriptor);

            parcelFileDescriptor.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return pdfBitmap;
    }

    private void postApi(HashMap<String, Object> hm) {

        List<MultipartBody.Part> list = new ArrayList<>();
        File file1;
        RequestBody pic;
        MultipartBody.Part userpic = null, userpic1 = null;
        if (mArrayGalleryPhoto.size() != 0) {
            for (int i = 0; i < mArrayGalleryPhoto.size(); i++) {
                file1 = bitmapToFile(mArrayGalleryPhoto.get(i), getString(R.string.app_name) + System.currentTimeMillis() + ".jpg");
                // Log.d("mArrayGalleryPhoto...", file1.toString());
                try {
                    Bitmap bitmap = BitmapFactory.decodeFile(file1.getPath());
                    bitmap.compress(Bitmap.CompressFormat.JPEG, compressionRatio, new FileOutputStream(file1));
                } catch (Throwable t) {
                    Log.e("ERROR", "Error compressing file." + t);
                    t.printStackTrace();
                }
                pic = RequestBody.create(MediaType.parse(".jpg"), file1);
                userpic = MultipartBody.Part.createFormData("image[]", file1.getName(), pic);
                list.add(userpic);
            }
        }

        APIInterface service = APIClient.getRetrofit().create(APIInterface.class);
        Call<AddressResponse> call = service.uploadImageEvent("eventimage", createPartFromString(sm.getString("user_id")),
                createPartFromString(String.valueOf(id)), list);

        call.enqueue(new Callback<AddressResponse>() {
            @Override
            public void onResponse(Call<AddressResponse> call, Response<AddressResponse> response) {
                progressDialog.dismiss();
                if (response.body().getMessage().equals("Saved Successfully")) {
                    eventDetailsShowApi(id);
                    frm_uplad_id.setVisibility(GONE);
                    image_textview.setVisibility(GONE);
                    mArrayGalleryPhoto.clear();
                    mArrayUri.clear();
                } else if (response.body().getMessage() != null) {
                    Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    frm_uplad_id.setVisibility(GONE);
                    //  Intent intent = new Intent(AddressProof.this, MainActivity.class);
                    //  startActivity(intent);
                }
//                PostPojo postPojo = response.body();
                // Log.d("responseApi", response.toString());
            }

            @Override
            public void onFailure(Call<AddressResponse> call, Throwable t) {

                if (t.toString().contains("timeout")) {
                    Toast.makeText(UserViewEvent.this, "Something seems to have gone wrong.Please try again", Toast.LENGTH_SHORT).show();


                } else if (t.toString().contains("Unable to resolve host")) {
                    Toast.makeText(UserViewEvent.this, "No internet", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(UserViewEvent.this, "Something seems to have gone wrong.Please try again", Toast.LENGTH_SHORT).show();


                }

                progressDialog.dismiss();
                Toast.makeText(UserViewEvent.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("response", t.getMessage());
            }
        });
    }

    private RequestBody createPartFromString(String data) {
        return RequestBody.create(MediaType.parse("text/plain"), data);
    }

    private void imageDelete(HashMap<String, Object> hm) {
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Please wait...");
        dialog.setCancelable(false);
        dialog.show();
        APIInterface service = APIClient.getRetrofit().create(APIInterface.class);
        Call<JsonElement> call = service.imageDelete("deleteimageevent", hm);
        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                dialog.dismiss();
                Toast.makeText(context, "Image deleted succesful", Toast.LENGTH_SHORT).show();
                eventDetailsShowApi(id);
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("res", t.getMessage());
            }
        });

    }

    private void imageDialogApi(List<ImagePojo> img) {
        RecyclerView rv;
        TextView confirm;
        ImageView cardView, cancel, iv_uploaded_image;
        CardView card, carGone;
        image_dialog = new Dialog(this);
        image_dialog.setContentView(R.layout.open_image_dialog);
        rv = image_dialog.findViewById(R.id.rv_image);
        cancel = image_dialog.findViewById(R.id.cross_imageview);
        card = image_dialog.findViewById(R.id.card);
        carGone = image_dialog.findViewById(R.id.cardGone);
        cardView = image_dialog.findViewById(R.id.cardView);
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HashMap<String, Object> hm = new HashMap<>();
                //  Log.d("sjfdk",String.valueOf(groupid));
                hm.put("userid", Integer.parseInt(sm.getString("user_id")));
                //hm.put("userid",Integer.parseInt(sm.getString("user_id")));
                //hm.put("userid", eventDetailsPojo.getUserid());
                hm.put("event_id", id);
                hm.put("owner", model2.getUserid());
                hm.put("imageurl", model2.getImg());
                // hm.put("imageurl","https://neighbrsnook.com/Neighbrsnook2023/admin/eventimage/EPI_648ae8a2bb58a.jpeg" );
                //  hm.put("username", );
                // Log.d("username---",sm.getString("user_name"));
                hm.put("imgid", model2.getImgid());
                imageDelete(hm);
            }
        });

        iv_uploaded_image = image_dialog.findViewById(R.id.iv_uploaded_image);
//        if(type.equals("single"))
//        {
//           card.setVisibility(View.VISIBLE);
//            iv_uploaded_image.
//
//        }
        rv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        ImageSliderDialogAdapter imageUploadAdapter = new ImageSliderDialogAdapter(image_dialog, img, "detail", 1);
        rv.setAdapter(imageUploadAdapter);
        cancel.setOnClickListener(new View.OnClickListener() {
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
    public void onClickImg(List<ImagePojo> img) {
        imageDialogApi(ar1);

    }

    public void capturePhoto() {
    /*    if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(activity, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, 200);
        }
        else {*/
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

    private void imageDialog(ArrayList<Uri> mArrayUri, String from) {
        RecyclerView rv;
        TextView confirm;
        ImageView cancel, cardView, iv_uploaded_image;
        CardView card;
        image_dialog = new Dialog(this);
        image_dialog.setContentView(R.layout.open_image_dialog);
        rv = image_dialog.findViewById(R.id.rv_image);
        cancel = image_dialog.findViewById(R.id.cross_imageview);
        card = image_dialog.findViewById(R.id.card);
        cardView = image_dialog.findViewById(R.id.cardView);
        iv_uploaded_image = image_dialog.findViewById(R.id.iv_uploaded_image);
        rv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        imageUploadAdapter = new ImageUploadAdapter(image_dialog, mArrayUri, UserViewEvent.this, from);
        rv.setAdapter(imageUploadAdapter);

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

    private void eventCommentApi() {
        progressDialog.show();
        HashMap<String, Object> hm = new HashMap<>();
        hm.put("userid", Integer.parseInt(sm.getString("user_id")));
        hm.put("eventid", id);
        hm.put("username", sm.getString("user_name"));
        hm.put("commenttext", msg_et.getText().toString());
        APIInterface service = APIClient.getRetrofit().create(APIInterface.class);
        Call<AddressResponse> call = service.eventCommnentApii("eventcomments", hm);
        call.enqueue(new Callback<AddressResponse>() {
            @Override
            public void onResponse(Call<AddressResponse> call, Response<AddressResponse> response) {
                root = response.body();
                response.body().getStatus();
                msg_et.setText("");
                InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                mgr.hideSoftInputFromWindow(msg_et.getWindowToken(), 0);
                //commentListApi();
                eventDetailsShowApi(id);
                progressDialog.dismiss();


            }

            @Override
            public void onFailure(Call<AddressResponse> call, Throwable t) {
                progressDialog.dismiss();


            }
        });


    }

    private void commentListApi() {
        HashMap<String, Object> hm = new HashMap<>();
        hm.put("userid", Integer.parseInt(sm.getString("user_id")));
        hm.put("event_id", id);
        // Log.d("postid....commentApi", String.valueOf(postId));
        APIInterface service = APIClient.getRetrofit().create(APIInterface.class);
        Call<CommentPojo> call = service.eventListApi("eventcommentslist", hm);
        call.enqueue(new Callback<CommentPojo>() {
            @Override
            public void onResponse(Call<CommentPojo> call, Response<CommentPojo> response) {
                CommentPojo commentPojo = response.body();

                if (commentPojo.getStatus().equals("success")) {
                    commentList = commentPojo.getEventlistdata();
                    commentAdapter = new EventCommentAdapter(commentList);
                    comment_rv.setAdapter(commentAdapter);
                    if (commentList.size() == 0) {
                        rv_ll.setVisibility(View.GONE);
                    } else {
                        rv_ll.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onFailure(Call<CommentPojo> call, Throwable t) {

            }
        });
    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if (requestCode == 200) {
//            boolean allGranted = true;
//            for (int result : grantResults) {
//                if (result != PackageManager.PERMISSION_GRANTED) {
//                    allGranted = false;
//                    break;
//                }
//            }
//            if (allGranted) {
//                // Permissions granted
//            } else {
//                // Permissions denied, show appropriate message
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


    public void exitLayout(ImagePojo model) {
        Dialog dialog = new Dialog(UserViewEvent.this);
        dialog.setContentView(R.layout.event_image_delete_photo);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(UserViewEvent.this, android.R.color.transparent)));
        dialog.getWindow().setAttributes(lp);
        dialog.show();
        TextView tv_no = dialog.findViewById(R.id.no_id);
        TextView tv_yes = dialog.findViewById(R.id.yes_id);
        tv_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        tv_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HashMap<String, Object> hm = new HashMap<>();
                hm.put("userid", Integer.parseInt(sm.getString("user_id")));
                //hm.put("userid",Integer.parseInt(sm.getString("user_id")));
                //hm.put("userid", eventDetailsPojo.getUserid());
                hm.put("event_id", id);
                hm.put("owner", model.getUserid());
                hm.put("imageurl", model.getImg());
                // hm.put("imageurl","https://neighbrsnook.com/Neighbrsnook2023/admin/eventimage/EPI_648ae8a2bb58a.jpeg" );
                //  hm.put("username", );
                // Log.d("username---",sm.getString("user_name"));
                hm.put("imgid", model.getImgid());
                imageDelete(hm);
                dialog.dismiss();
            }

        });
    }


}

