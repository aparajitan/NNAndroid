package com.app_neighbrsnook.profile;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsControllerCompat;

import com.app_neighbrsnook.FavFragment1;
import com.app_neighbrsnook.MainActivity;
import com.app_neighbrsnook.PostActivity;
import com.app_neighbrsnook.R;
import com.app_neighbrsnook.abusive.BadWordFilter;
import com.app_neighbrsnook.apiService.ApiExecutor;
import com.app_neighbrsnook.businessModule.BusinessActivity;
import com.app_neighbrsnook.event.EventShowAll;
import com.app_neighbrsnook.event.ImageShowActivity;
import com.app_neighbrsnook.group.GroupsActivity;
import com.app_neighbrsnook.login.AddressResponse;
import com.app_neighbrsnook.marketPlace.YourItemListActivity;
import com.app_neighbrsnook.model.ImagePOJO;
import com.app_neighbrsnook.network.APIClient;
import com.app_neighbrsnook.network.APIInterface;
import com.app_neighbrsnook.pollModule.PollActivityDemo1;
import com.app_neighbrsnook.registration.SecondPageUserLocationRegisteration;
import com.app_neighbrsnook.utils.GlobalMethods;
import com.app_neighbrsnook.utils.PrefMananger;
import com.app_neighbrsnook.utils.SharedPrefsManager;
import com.app_neighbrsnook.utils.UtilityFunction;
import com.app_neighbrsnook.utils.VerticalLineView;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;
import com.app_neighbrsnook.libraries.cropper.CropImage;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyProfile extends AppCompatActivity {
    FrameLayout frm_events, frm_groups, edit_address_neighbrhood;
    ImageView img_back, img_edit_two;
    FrameLayout frm_suggestion;
    TextView tvFirtsName, tvLastName;
    String chooseInterest, interestId, stUsername, profesionId, location_neighbrhood, fnameFirst, fLastName, post, suggestion, event,
            business, createuser, poll, lovengh, loveId, group, directmsg, mail, favourite, tv_phone, emergency, dob, stCity, stState, stPincode, address, addressOne, addressTwo, genderProfile, whatDoYou,
            stUploadDocument, userVerify;
    TextView tv_mail, tvUploadDocument;
    TextView tv_mobile, tv_emergency, tv_address, tv_dob, tv_gender, tv_what_do, tv_interest, tv_love, delete_photo;
    FrameLayout img_edit_other, img_edit_personal, img_love_ngh, img_address, frm_market;
    FrameLayout frm_poll, frm_profile_busines, frm_fav;
    Activity activity;
    Context context;
    TextView post_no, suggestion_no, tv_events, tv_business, user_create_ac, tv_polls, tv_groups, tv_dm, tv_favourite, tvdeo, tv_address_one, tv_address_two;
    TextView tv_user_name, tv_location_ngh;
    CircleImageView img_profile, imgDefaultImg;
    HashMap<String, String> hm = new HashMap<>();
    HashMap<String, Object> hashMap;
    RelativeLayout rl_my_profile, rl_profile_layout;
    ImageView img_edit_icon, profileFrontView, profileBackView;
    TextView tv_take_photo, tv_choose_photo, tv_cancel, tvLine;
    Bitmap bitmap2, bitmap4, bitmap3;
    SharedPrefsManager sm;
    FrameLayout frm_posts, editNameUser, editDocument, usernameFrm;
    ProgressBar progressEvent, progressPolls, progressBusiness, progressGroups, progressPosts;
    String stPostTotal, stPostPerc, stUserName, stPollTotal, stPollPerc, stBusinessTotal, stBusinessPerc, stGroupTotal, stGrouopPer, stEventNeigh, stEventPer, stName;
    TextView eventsNeighbrhoodTotal, eventPercent, pollTotal, pollPerc, postTotal, postPerc, businessTotal, businessPerc, groupTotal, groupPerc;
    boolean isVerifiedUser = true;
    UpdateUserPic updateUserPic;
    String whiteCircle = "\u25E6", solidCircle = "\u2022", smallSquare = "\u25AA", start = "\u2605";
    VerticalLineView eventLinePer, pollLinePer, businessLinePer, groupLinePer, postLinePer, marketLinePer;
    int eventPer, pollPer, businessPer, groupPer, postPer, marketPer;
    int eventColor = 0xFF9DAC7C, pollColor = 0xFFAF9D66, businessColor = 0xFF829591, groupColor = 0xFF4F7959, postColor = 0xFF985646, marketColor = 0xFF8E836F;
    TextView market_no, marketTotal, marketPerc;
    String strMarket, strMarketTot, strMarketPer;
    LinearLayout lnrAdd2;
    private String frontImageUrl = "";
    private String backImageUrl = "";
    private String source = "";
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = activity = this;
        setContentView(R.layout.activity_my_profile);
        Intent intent = getIntent();
        source = intent.getStringExtra("source");
        frm_events = findViewById(R.id.events_card_id);
        tv_cancel = findViewById(R.id.cancle_tv);
        sm = new SharedPrefsManager(this);
        img_edit_icon = findViewById(R.id.img_edit_profile);
        tvFirtsName = findViewById(R.id.firstName);
        rl_profile_layout = findViewById(R.id.rl_profile_layout);
        delete_photo = findViewById(R.id.delete_photo);
        frm_posts = findViewById(R.id.frm_posts);
        eventPercent = findViewById(R.id.eventPercent);
        editNameUser = findViewById(R.id.editNameUser);
        lnrAdd2 = findViewById(R.id.lnrAdd2);
        eventsNeighbrhoodTotal = findViewById(R.id.eventsNeighbrhoodTotal);
        tvUploadDocument = findViewById(R.id.uploadDocument);
        editDocument = findViewById(R.id.editDocument);
        progressEvent = findViewById(R.id.progressBar);
        progressPolls = findViewById(R.id.progressBarPolls);
        progressBusiness = findViewById(R.id.progressBarBusiness);
        progressGroups = findViewById(R.id.progressBarGroups);
        progressPosts = findViewById(R.id.progressBarPosts);
        frm_market = findViewById(R.id.frm_market);
        eventLinePer = findViewById(R.id.eventLinePer);
        pollLinePer = findViewById(R.id.pollLinePer);
        businessLinePer = findViewById(R.id.businessLinePer);
        groupLinePer = findViewById(R.id.groupLinePer);
        postLinePer = findViewById(R.id.postLinePer);
        marketLinePer = findViewById(R.id.marketLinePer);
        market_no = findViewById(R.id.tv_market_no);
        marketTotal = findViewById(R.id.marketTotal);
        marketPerc = findViewById(R.id.marketPerc);
        tvLastName = findViewById(R.id.lastName);
        postTotal = findViewById(R.id.postTotal);
        postPerc = findViewById(R.id.postPerc);
        pollTotal = findViewById(R.id.pollTotal);
        pollPerc = findViewById(R.id.pollPerc);
        groupTotal = findViewById(R.id.groupsTotal);
        groupPerc = findViewById(R.id.groupPerc);
        businessTotal = findViewById(R.id.businessTotal);
        businessPerc = findViewById(R.id.businessPerc);
        rl_my_profile = findViewById(R.id.rl_my_profile);
        img_edit_two = findViewById(R.id.id_edit_profile);
        tvdeo = findViewById(R.id.deo);
        img_profile = findViewById(R.id.id_profile_img);
        tv_address_two = findViewById(R.id.tv_address_two);
        tv_address_one = findViewById(R.id.tv_add_one);
        tv_love = findViewById(R.id.tv_love_safe_id);
        user_create_ac = findViewById(R.id.member_create);
        frm_groups = findViewById(R.id.frm_groups_id);
        img_back = findViewById(R.id.img_back);
        edit_address_neighbrhood = findViewById(R.id.edit_address_neighbrhood);
        frm_suggestion = findViewById(R.id.frm_suggestion_id);
        tv_mail = findViewById(R.id.mail_id);
        tv_mobile = findViewById(R.id.tv_call_mobile);
        tv_emergency = findViewById(R.id.tv_emergency_no);
        img_edit_personal = findViewById(R.id.edit_personal_information);
        img_edit_other = findViewById(R.id.edit_other_imformation);
        img_love_ngh = findViewById(R.id.edit_love);
        img_address = findViewById(R.id.edit_address);
        frm_poll = findViewById(R.id.frm_poll_profile);
        frm_profile_busines = findViewById(R.id.frm_profile_business);
        frm_fav = findViewById(R.id.frm_fav);
        tv_user_name = findViewById(R.id.p_user_name);
        tv_location_ngh = findViewById(R.id.location_profile);
        post_no = findViewById(R.id.post_no);
        suggestion_no = findViewById(R.id.tv_suggestion_no);
        tv_events = findViewById(R.id.tv_events_no);
        tv_business = findViewById(R.id.tv_business_no);
        tv_polls = findViewById(R.id.tv_poll_no);
        tv_groups = findViewById(R.id.tv_groups_no);
        tv_dm = findViewById(R.id.tv_direct_message);
        tv_favourite = findViewById(R.id.tv_favorite_no);
        tv_dob = findViewById(R.id.tv_dob_profile);
        tv_gender = findViewById(R.id.tv_gender);
        tv_what_do = findViewById(R.id.tv_what_do_you_do);
        tv_interest = findViewById(R.id.tv_interest);
        imgDefaultImg = findViewById(R.id.idDefaultImf);
        usernameFrm = findViewById(R.id.usernameFrm);
        profileFrontView = findViewById(R.id.profileFrontView);
        profileBackView = findViewById(R.id.profileBackView);
        bitmap2 = bitmap4;
        tv_take_photo = findViewById(R.id.take_photo);
        tvLine = findViewById(R.id.tvLine);
        tv_choose_photo = findViewById(R.id.choose_photo);
        bitmap3 = GlobalMethods.getInstance(context).getInitialBitmap(imgDefaultImg, sm.getString("user_name"), MyProfile.this);
        imgDefaultImg.setImageBitmap(bitmap3);
        progressEvent.setMax(100);
        img_address.setVisibility(View.GONE);
        edit_address_neighbrhood.setVisibility(View.GONE);
        // बस यह method use करें
        profileFrontView.setOnClickListener(v -> showImageDialogSimple(frontImageUrl, "Front Document"));
        profileBackView.setOnClickListener(v -> showImageDialogSimple(backImageUrl, "Back Document"));
        img_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (imgDefaultImg.getDrawable() == null) {
                    // No image set, show a message to the user
                    Toast.makeText(MyProfile.this, "Please select a profile picture first.", Toast.LENGTH_SHORT).show();
                } else {
                    // Image is set, proceed to the next activity
                    startActivity(new Intent(MyProfile.this, ImageShowActivity.class));
                }
            }
        });
        frm_market.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MyProfile.this, YourItemListActivity.class));
            }
        });

        editNameUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(MyProfile.this);
                dialog.setContentView(R.layout.edit_name_layout);
                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                lp.copyFrom(dialog.getWindow().getAttributes());
                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                lp.gravity = Gravity.CENTER;

                TextView tvSaveBtn = dialog.findViewById(R.id.saveBtn);
                EditText etUsername = dialog.findViewById(R.id.firstName);
                TextView tvCancel = dialog.findViewById(R.id.cancelTv);

                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(
                        ContextCompat.getColor(MyProfile.this, android.R.color.transparent)
                ));
                dialog.getWindow().setAttributes(lp);

                etUsername.setText(stName);
                tvSaveBtn.setEnabled(false);
                tvSaveBtn.setAlpha(0.5f);

                etUsername.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        String inputText = s.toString().trim();

                        // Bad word check
                        if (BadWordFilter.containsBadWord(inputText)) {
                            tvSaveBtn.setEnabled(false);
                            tvSaveBtn.setAlpha(0.5f); // inactive
                            etUsername.setError("Inappropriate word detected"); // optional error message
                            return;
                        }

                        // Normal validation
                        if (!inputText.equals(stName.trim())) {
                            tvSaveBtn.setEnabled(true);
                            tvSaveBtn.setAlpha(1f); // active
                        } else {
                            tvSaveBtn.setEnabled(false);
                            tvSaveBtn.setAlpha(0.5f); // inactive
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable s) {}
                });


                dialog.show();

                tvCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                tvSaveBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        hm.put("userid", sm.getString("user_id"));
                        hm.put("name", etUsername.getText().toString());
                        userNameChanged();
                        dialog.dismiss();
                    }
                });
            }
        });

        img_edit_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (GlobalMethods.checkCameraAndGalleryPermission(MyProfile.this)) {
                    rl_my_profile.setVisibility(View.VISIBLE);
                }
            }
        });
        tv_take_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                capturePhoto();
                CLICK_ON = FIRST_IMAGE;
                rl_my_profile.setVisibility(View.GONE);
            }
        });
        tv_choose_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 2);
                CLICK_ON = FIRST_IMAGE;
                rl_my_profile.setVisibility(View.GONE);
            }
        });
        delete_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            /*    HashMap<String, Object> hm = new HashMap<>();
                hm.put("userid",Integer.parseInt(PrefMananger.GetLoginData(context).getId() +""));
                apiProfileImageDelete(hm);*/
                updateImageWithoutLoader(bitmap3);
                rl_my_profile.setVisibility(View.GONE);
                userProfile(hashMap);
            }
        });
        rl_profile_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rl_my_profile.setVisibility(View.GONE);
            }
        });
        frm_profile_busines.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyProfile.this, BusinessActivity.class);
                intent.putExtra("title", "profile");
                startActivity(intent);
            }
        });
        frm_fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FavFragment1 favFragment = new FavFragment1();
                getSupportFragmentManager().beginTransaction().replace(R.id.rl_profile_layout, favFragment).addToBackStack("fav").commit();
            }
        });
        frm_poll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyProfile.this, PollActivityDemo1.class);
                intent.putExtra("from", "profile");
                startActivity(intent);

            }
        });
        frm_posts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* Intent intent = new Intent(MyProfile.this, PostActivity.class);
                intent.putExtra("source", "profile");
                startActivity(intent);*/

                Intent intent = new Intent(MyProfile.this, PostActivity.class);
                intent.putExtra("user_id", Integer.parseInt(sm.getString("user_id")));
                intent.putExtra("source", "profile");
                startActivity(intent);
            }
        });
        img_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent memberIntent = new Intent(MyProfile.this, ProfileUpdateDocumentUser.class);
                memberIntent.putExtra("dobb", dob);
                memberIntent.putExtra("gender", genderProfile);
                memberIntent.putExtra("neighbrhood", location_neighbrhood);
                memberIntent.putExtra("address1", addressOne);
                memberIntent.putExtra("address2", addressTwo);
                memberIntent.putExtra("stUploadDocument", stUploadDocument);
                memberIntent.putExtra("addres", addressOne);
                memberIntent.putExtra("city", stCity);
                memberIntent.putExtra("state", stState);
                memberIntent.putExtra("pincode", stPincode);
                memberIntent.putExtra("source", "profile");
                startActivity(memberIntent);
            }
        });
        edit_address_neighbrhood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent memberIntent = new Intent(MyProfile.this, SecondPageUserLocationRegisteration.class);
                memberIntent.putExtra("neighbrhood", location_neighbrhood);
                memberIntent.putExtra("address1", addressOne);
                memberIntent.putExtra("stUploadDocument", stUploadDocument);
                memberIntent.putExtra("addressone", addressOne);
                memberIntent.putExtra("city", stCity);
                memberIntent.putExtra("state", stState);
                memberIntent.putExtra("pincode", stPincode);
                memberIntent.putExtra("source", "profile");
                startActivity(memberIntent);
            }
        });
        img_love_ngh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MyProfile.this, ProfileStepTwoActivity.class));
            }
        });
        img_edit_other.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MyProfile.this, ProfileStepTwoActivity.class);
                startActivity(i);
            }
        });
        tv_mobile.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                Dialog dialog = new Dialog(MyProfile.this);
                dialog.setContentView(R.layout.calling_dialog_pop_up);
                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                lp.copyFrom(dialog.getWindow().getAttributes());
                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                lp.gravity = Gravity.CENTER;
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(MyProfile.this,
                        android.R.color.transparent)));
                dialog.getWindow().setAttributes(lp);
                dialog.show();
                TextView tv_call = dialog.findViewById(R.id.call_id);
                TextView tv_no = dialog.findViewById(R.id.tv_no);
                TextView tv_cancel_dialog = dialog.findViewById(R.id.cancel_id);
                tv_no.setText(tv_phone);
                tv_cancel_dialog.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                tv_call.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent sIntent = new Intent(Intent.ACTION_DIAL);
                        sIntent.setData(Uri.parse("tel:" + tv_phone));
                        startActivity(sIntent);

                    }
                });

            }
        });
        tv_emergency.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                Dialog dialog = new Dialog(MyProfile.this);
                dialog.setContentView(R.layout.calling_dialog_pop_up_second);
                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                lp.copyFrom(dialog.getWindow().getAttributes());
                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                lp.gravity = Gravity.CENTER;
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(MyProfile.this,
                        android.R.color.transparent)));
                dialog.getWindow().setAttributes(lp);
                dialog.show();
                //littleMoreSkip();
                TextView tv_call = dialog.findViewById(R.id.call_id);
                TextView tv_emgncy_no = dialog.findViewById(R.id.tv_emergency_dialog_no);
                TextView tv_cancel_dialog = dialog.findViewById(R.id.cancel_id);
                tv_emgncy_no.setText(emergency);
                tv_cancel_dialog.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                tv_call.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent sIntent = new Intent(Intent.ACTION_DIAL);
                        sIntent.setData(Uri.parse("tel:" + emergency));
                        startActivity(sIntent);
                    }
                });
            }
        });
        tv_mail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto", "pallavi.kanungo@gmail.com", null));
                intent.putExtra(Intent.EXTRA_SUBJECT, "HOW ARE YOU");
                intent.putExtra(Intent.EXTRA_TEXT, "Hi i am arsad here");
                startActivity(Intent.createChooser(intent, "Choose an Email client :"));
            }
        });
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        frm_groups.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyProfile.this, GroupsActivity.class);
                // intent.putExtra("neighbrhood", "profile");
                startActivity(intent);
            }
        });
        frm_events.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyProfile.this, EventShowAll.class);
                // intent.putExtra("from", "profile");
                startActivity(intent);
            }
        });
        tvdeo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // updateImage();
            }
        });
        img_edit_personal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MyProfile.this, ProfileStepTwoActivity.class);
                startActivity(i);
            }
        });

        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rl_my_profile.setVisibility(View.GONE);
            }
        });
        littleMoreSkip();

    }

    private void littleMoreSkip() {
        hashMap = new HashMap<>();
        hashMap.put("loggeduser", sm.getString("user_id"));
        userProfile(hashMap);
    }

    private void userProfile(HashMap<String, Object> hm) {
        Boolean isInternetConnection = GlobalMethods.checkConnection(context);
        if (isInternetConnection) {
            ProgressDialog dialog = new ProgressDialog(this);
            dialog.setMessage("Please wait...");
            dialog.setCancelable(false);
            dialog.show();
            APIInterface service = APIClient.getRetrofit().create(APIInterface.class);
            //step2 rgn Call<JsonElement> call = service.profileapisec("userprofile", Integer.parseInt(PrefMananger.GetLoginData(context).getId()), hm);
            Call<JsonElement> call = service.profileapisec("userprofile", Integer.parseInt(sm.getString("user_id")), hm);
            call.enqueue(new Callback<JsonElement>() {
                @Override
                public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                    dialog.dismiss();
                    JsonElement jsonElement = response.body();
                    JsonObject jsonObject = jsonElement.getAsJsonObject();
                    try {
                        sm.setString("userphoto", jsonObject.get("userpic").getAsString());
                        // fname = jsonObject.get("firstname"+"lastname").getAsString(   );
                        location_neighbrhood = jsonObject.get("neighborhood").getAsString();
                        userVerify = jsonObject.get("verfied_msg").getAsString();
                        post = jsonObject.get("posts").getAsString();
                        suggestion = jsonObject.get("suggestions").getAsString();
                        event = jsonObject.get("events").getAsString();
                        business = jsonObject.get("business").getAsString();
                        mail = jsonObject.get("emailid").getAsString();
                        createuser = jsonObject.get("createddate").getAsString();
                        poll = jsonObject.get("polls").getAsString();
                        group = jsonObject.get("groups").getAsString();
                        directmsg = jsonObject.get("directmessage").getAsString();
                        favourite = jsonObject.get("totfavourites").getAsString();
                        tv_phone = jsonObject.get("phoneno").getAsString();
                        emergency = jsonObject.get("emer_phone").getAsString();
                        address = jsonObject.get("address").getAsString();
                        addressOne = jsonObject.get("addressone").getAsString();
                        // addressTwo = jsonObject.get("addlinetwo").getAsString();
                        dob = jsonObject.get("dob").getAsString();
                        genderProfile = jsonObject.get("gender").getAsString();
                        whatDoYou = jsonObject.get("nbrs_type").getAsString();
                        chooseInterest = jsonObject.get("intersttype").getAsString();
                        interestId = jsonObject.get("intid").getAsString();
                        profesionId = jsonObject.get("profid").getAsString();
                        lovengh = jsonObject.get("reason").getAsString();
                        stEventNeigh = jsonObject.get("totevents").getAsString();
                        stEventPer = jsonObject.get("eventper").getAsString();
                        stName = jsonObject.get("username").getAsString();
                        stUploadDocument = jsonObject.get("uploaded_doc").getAsString();
                        stPostTotal = jsonObject.get("totposts").getAsString();
                        stPollTotal = jsonObject.get("totpolls").getAsString();
                        stBusinessTotal = jsonObject.get("totbusiness").getAsString();
                        stGroupTotal = jsonObject.get("totgroups").getAsString();
                        stPostPerc = jsonObject.get("postper").getAsString();
                        stPollPerc = jsonObject.get("pollper").getAsString();
                        stBusinessPerc = jsonObject.get("businessper").getAsString();
                        stGrouopPer = jsonObject.get("groupper").getAsString();
                        stUserName = jsonObject.get("username").getAsString();
                        strMarket = jsonObject.get("market").getAsString();
                        strMarketTot = jsonObject.get("totmarket").getAsString();
                        strMarketPer = jsonObject.get("marketper").getAsString();
                        stCity = jsonObject.get("city").getAsString();
                        stState = jsonObject.get("state").getAsString();
                        stPincode = jsonObject.get("pincode").getAsString();

                        if (userVerify.equals("User Verification is completed!")) {
                            edit_address_neighbrhood.setVisibility(View.GONE);
                            img_address.setVisibility(View.GONE);
                            editDocument.setVisibility(View.GONE);
                            tvLine.setVisibility(View.GONE);
                            usernameFrm.setVisibility(View.GONE);
                            isVerifiedUser = false;
                        } else {
                            img_address.setVisibility(View.VISIBLE);
                            edit_address_neighbrhood.setVisibility(View.VISIBLE);
                        }
                        tvUploadDocument.setText(stUploadDocument);
                        tvFirtsName.setText(stName);
                        tv_location_ngh.setText(location_neighbrhood);
                        post_no.setText(post);
                        tv_user_name.setText(stUserName);
                        suggestion_no.setText(suggestion);
                        tv_events.setText(event);
                        tv_business.setText(business);
                        tv_mail.setText(mail);
                        user_create_ac.setText(createuser);
                        tv_polls.setText(poll);
                        tv_groups.setText(group);
                        tv_dm.setText(directmsg);
                        tv_favourite.setText(favourite);
                        tv_mobile.setText(tv_phone);
                        // tv_address.setText(address);
                        tv_address_one.setText(addressOne + ", " + location_neighbrhood);
                        //  tv_address_two.setText(addressTwo);
                        tv_dob.setText(dob);
                        tv_gender.setText(genderProfile);
                        market_no.setText(strMarket);
                        marketTotal.setText(strMarketTot);
                        marketPerc.setText(strMarketPer);

                        if (emergency == null || emergency.trim().isEmpty()) {
                            tv_emergency.setText("NA");
                        } else {
                            tv_emergency.setText(emergency);
                        }

                        if (whatDoYou == null || whatDoYou.trim().isEmpty()) {
                            tv_what_do.setText("NA");
                        } else {
                            tv_what_do.setText(whatDoYou);
                        }

                        String strInterest = formatTextWithBullets(chooseInterest, smallSquare);
                        tv_interest.setText(strInterest);
                        String strLoveNgh = formatTextWithBullets(lovengh, smallSquare);
                        tv_love.setText(strLoveNgh);


                        eventsNeighbrhoodTotal.setText(stEventNeigh);
                        eventPercent.setText(stEventPer);
                        postTotal.setText(stPostTotal);
                        postPerc.setText(stPostPerc);
                        pollTotal.setText(stPollTotal);
                        pollPerc.setText(stPollPerc);
                        businessTotal.setText(stBusinessTotal);
                        businessPerc.setText(stBusinessPerc);
                        groupTotal.setText(stGroupTotal);
                        groupPerc.setText(stGrouopPer);
                        progressEvent.setProgress(Integer.parseInt(stEventPer));
                        progressPolls.setProgress(Integer.parseInt(stPollPerc));
                        progressBusiness.setProgress(Integer.parseInt(stBusinessPerc));
                        progressGroups.setProgress(Integer.parseInt(stGrouopPer));
                        progressPosts.setProgress(Integer.parseInt(stPostPerc));
                        eventPer = Integer.parseInt(stEventPer);
                        eventLinePer.setCurrentValue(eventPer);
                        eventLinePer.setLineColor(eventColor);

                        pollPer = Integer.parseInt(stPollPerc);
                        pollLinePer.setCurrentValue(pollPer);
                        pollLinePer.setLineColor(pollColor);

                        businessPer = Integer.parseInt(stBusinessPerc);
                        businessLinePer.setCurrentValue(businessPer);
                        businessLinePer.setLineColor(businessColor);

                        groupPer = Integer.parseInt(stGrouopPer);
                        groupLinePer.setCurrentValue(groupPer);
                        groupLinePer.setLineColor(groupColor);

                        postPer = Integer.parseInt(stPostPerc);
                        postLinePer.setCurrentValue(postPer);
                        postLinePer.setLineColor(postColor);
                        marketPer = Integer.parseInt(strMarketPer);
                        marketLinePer.setCurrentValue(marketPer);
                        marketLinePer.setLineColor(marketColor);
                    } catch (Exception e) {
                    }

                    if (jsonObject.has("uploaded_doc_img") && !jsonObject.get("uploaded_doc_img").isJsonNull()) {
                        JsonArray uploadedDocArray = jsonObject.getAsJsonArray("uploaded_doc_img");

                        if (uploadedDocArray.size() > 0) {
                            // Set first image to profileFrontView
                            frontImageUrl = uploadedDocArray.get(0).getAsString();
                            if (!frontImageUrl.isEmpty()) {
                                Picasso.get()
                                        .load(frontImageUrl)
                                        .fit()
                                        .centerCrop()
                                        .error(R.drawable.profile_circle_white_background)
                                        .placeholder(R.drawable.profile_circle_white_background)
                                        .into(profileFrontView);

                                profileFrontView.setVisibility(View.VISIBLE);

                                // Add click listener for front image
                                profileFrontView.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        showImageDialog(frontImageUrl, "Front Document");
                                    }
                                });
                            }

                            // Set second image to profileBackView (if exists)
                            if (uploadedDocArray.size() > 1) {
                                backImageUrl = uploadedDocArray.get(1).getAsString();
                                if (!backImageUrl.isEmpty()) {
                                    Picasso.get()
                                            .load(backImageUrl)
                                            .fit()
                                            .centerCrop()
                                            .error(R.drawable.profile_circle_white_background)
                                            .placeholder(R.drawable.profile_circle_white_background)
                                            .into(profileBackView);

                                    profileBackView.setVisibility(View.VISIBLE);

                                    // Add click listener for back image
                                    profileBackView.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            showImageDialog(backImageUrl, "Back Document");
                                        }
                                    });
                                } else {
                                    profileBackView.setVisibility(View.GONE);
                                }
                            } else {
                                // Hide back view if only one image
                                profileBackView.setVisibility(View.GONE);
                            }
                        } else {
                            // Hide both views if array is empty
                            profileFrontView.setVisibility(View.GONE);
                            profileBackView.setVisibility(View.GONE);
                        }
                    } else {
                        // Hide both views if no uploaded_doc_img field
                        profileFrontView.setVisibility(View.GONE);
                        profileBackView.setVisibility(View.GONE);
                    }

                    if (jsonObject.get("userpic").getAsString().isEmpty()) {
                        bitmap3 = GlobalMethods.getInstance(context).getInitialBitmap(imgDefaultImg, sm.getString("user_name"), MyProfile.this);
                        imgDefaultImg.setImageBitmap(bitmap3);
                        imgDefaultImg.setVisibility(View.VISIBLE);
                        img_profile.setVisibility(View.GONE);
                    } else {
                        Picasso.get().load(jsonObject.get("userpic").getAsString()).fit().centerCrop().
                                error(R.drawable.profile_circle_white_background).placeholder(R.drawable.profile_circle_white_background)
                                .into(img_profile);
                        Picasso.get().load(jsonObject.get("userpic").getAsString()).into(img_profile);
                        imgDefaultImg.setVisibility(View.GONE);
                        img_profile.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onFailure(Call<JsonElement> call, Throwable t) {
                    GlobalMethods.getInstance(MyProfile.this).globalDialog(context, "Something seems to have gone wrong.Please try again");
                    dialog.dismiss();
                    Log.d("res", t.getMessage());
                }
            });
        } else {
            GlobalMethods.getInstance(MyProfile.this).globalDialog(context, "     No internet connection.");
        }
    }

    String FIRST_IMAGE = "FirstImage";
    String CLICK_ON = "";

    public File bitmapToFile(Bitmap bitmap, String fileNameToSave) {
        //create a file to write bitmap data
        File file = null;
        try {
            String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + File.separator + getString(R.string.app_name);
            File directory = new File(path);

            file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + File.separator + fileNameToSave);
            file.createNewFile();
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
                filePath = imageUri;
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
                    updateImageWithoutLoader(bitmap1);
                    // updateImageWithoutLoader(bitmap3);
                    imgDefaultImg.setVisibility(View.GONE);
                    img_profile.setVisibility(View.VISIBLE);

                    ImagePOJO imagePOJO = new ImagePOJO();
                    Log.e("1258963", bitmap + "");
                    imagePOJO.bitmap = bitmap;
                }
            } else if (requestCode == PIC_CROP_REQUEST && data != null) {
                filePath = data.getData();
                performCrop(filePath);
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), filePath);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                bitmap1 = bitmap;
                filePath1 = filePath;
                img_profile.setImageBitmap(bitmap);
            } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                if (resultCode == RESULT_OK) {
                    Uri resultUri = result.getUri();
                    Bitmap photo1;
                    try {
                        photo1 = MediaStore.Images.Media.getBitmap(context.getContentResolver(), resultUri);
                        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                        photo1.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                        filePath = resultUri;
                        bitmap = photo1;
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
                    updateImageWithoutLoader(bitmap);
                    imgDefaultImg.setVisibility(View.GONE);
                    img_profile.setVisibility(View.VISIBLE);


                } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                    Exception error = result.getError();
                    error.printStackTrace();
                }
            }
        }
    }

    private void updateImageWithoutLoader(Bitmap img) {
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Please wait...");
        dialog.setCancelable(false);
        dialog.show();
        try {
            File file1;
            int compressionRatio = 25; //1 == originalImage, 2 = 50% compression, 4=25% compress
            MultipartBody.Part aadharFront = null;
            if (img != null) {
                file1 = bitmapToFile(img, getString(R.string.app_name) + System.currentTimeMillis() + ".jpg");
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
                    aadharFront = MultipartBody.Part.createFormData("userpic", file1.getName(), videoPart);
                }
            }
            HashMap<String, RequestBody> hashMap = new HashMap<>();

            hashMap.put("userid", RequestBody.create(MultipartBody.FORM, PrefMananger.GetLoginData(context).getId() + ""));

            ApiExecutor.getApiService().profileImageUpdate("userprofilephoto", aadharFront, hashMap).enqueue(new Callback<AddressResponse>() {
                @Override
                public void onResponse(Call<AddressResponse> call, Response<AddressResponse> response) {
                    try {
                        //  Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        if (response.body().getStatus().equals("success")) {
                            dialog.dismiss();
//                            response.body().getImage();
                            updateUserPic.updateProfileImage("changeImage");
                        } else if (response.body().getMessage() != null) {
                            dialog.dismiss();
                            updateUserPic.updateProfileImage("changeImage");
                            Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<AddressResponse> call, Throwable t) {
                    Log.e("fdsadf", t.toString());
                }
            });

        } catch (Exception e) {
            e.printStackTrace();

        }
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

    String currentPath = "";
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

    private void userNameChanged() {
        if (UtilityFunction.isNetworkConnected(context)) {
            UtilityFunction.showLoading(context, "Please wait...");
            ApiExecutor.getApiService().usernameUpdate("reg-step-I", hm).enqueue(new Callback<AddressResponse>() {
                @Override
                public void onResponse(Call<AddressResponse> call, Response<AddressResponse> response) {
                    UtilityFunction.hideLoading();
                    try {
                        if (response.body().getStatus().equals("success")) {
                            Log.d("response----", response.toString());
                            Log.d("SOURCE_CHECK", "Source value: " + source);
                            // null & empty dono handle karega
                            if (source == null || source.trim().isEmpty()) {
                                // agar source null ya empty hai to welcomeDialog call kare
                                userProfile(hashMap);
                            } else {
                                // agar source me koi value hai to userProfile call kare
                                welcomeDialog(response.body().getMessage());
                            }
                        } else if (response.body().getMessage() != null) {
                            Toast.makeText(MyProfile.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<AddressResponse> call, Throwable t) {
                    UtilityFunction.hideLoading();
                }
            });
        } else {
            Toast.makeText(context, "Network is not available", Toast.LENGTH_SHORT).show();
        }
    }

    public interface UpdateUserPic {
        void updateProfileImage(String userpic);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // API call jab screen par wapas aaye
        userProfile();
    }

    private void userProfile() {
        Log.d("API_CALL", "User profile API called");
    }

    private String formatTextWithBullets(String text, String bulletSymbol) {
        if (text == null || text.trim().isEmpty()) {
            return "NA"; // agar pura text empty hai to kuch bhi return na ho
        }

        String[] parts = text.split(",");
        StringBuilder formattedText = new StringBuilder();

        for (String part : parts) {
            String trimmedPart = part.trim();
            if (!trimmedPart.isEmpty()) { // sirf non-empty parts add karo
                formattedText.append(bulletSymbol)
                        .append(" ")
                        .append(trimmedPart)
                        .append("\n");
            }
        }

        return formattedText.toString().trim();
    }

    public void welcomeDialog(String message){
        final Dialog dialog = new Dialog(MyProfile.this);
        dialog.setContentView(R.layout.thanks_neighbrsnook_layout_step_two);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(MyProfile.this,
                android.R.color.transparent)));
        dialog.getWindow().setAttributes(lp);
        TextView tvMessage = dialog.findViewById(R.id.tvMessage);
        //  TextView tvcancel = dialog.findViewById(R.id.tvCancel);
        FrameLayout frm_choose=dialog.findViewById(R.id.post_frm);
        tvMessage.setText(message);
        dialog.show();
        frm_choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // PrefMananger.saveScreen(context,"");
                startActivity(new Intent(MyProfile.this, MainActivity.class));
                finishAffinity();

            }
        });
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
                rl_my_profile.setVisibility(View.VISIBLE);
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

    private void awaitStatus() {
        if (UtilityFunction.isNetworkConnected(context)) {
            // UtilityFunction.showLoading(context, "Please wait...");
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("userid", sm.getString("user_id"));
            hashMap.put("status", "success");
            ApiExecutor.getApiService().requestNeighbrhood("awaitstatus", hashMap).enqueue(new Callback<AddressResponse>() {
                @Override
                public void onResponse(Call<AddressResponse> call, Response<AddressResponse> response) {
                    // UtilityFunction.hideLoading();
                    try {
                        if (response.body().getStatus().equals("success")) {
                            Log.d("response----", response.toString());
                        } else if (response.body().getMessage() != null) {
                            // Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
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


    private void showImageDialog(String imageUrl, String title) {
        if (imageUrl == null || imageUrl.isEmpty()) {
            Toast.makeText(this, "Image not available", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create dialog
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_image_view);

        // Make dialog fullscreen
        Window window = dialog.getWindow();
        if (window != null) {
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            window.setBackgroundDrawable(new ColorDrawable(Color.BLACK));
        }

        // Find views
        ImageView dialogImageView = dialog.findViewById(R.id.dialog_image_view);
        TextView dialogTitle = dialog.findViewById(R.id.dialog_title);
        ImageView closeButton = dialog.findViewById(R.id.dialog_close_button);

        // Set title
        if (dialogTitle != null) {
            dialogTitle.setText(title);
        }

        // Load image with Picasso
        Picasso.get()
                .load(imageUrl)
                .fit()
                .centerInside()
                .error(R.drawable.profile_circle_white_background)
                .placeholder(R.drawable.profile_circle_white_background)
                .into(dialogImageView);

        // Close button click
        if (closeButton != null) {
            closeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
        }

        // Click outside to close
        dialogImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    // Alternative method using AlertDialog (simpler approach)
    private void showImageDialogSimple(String imageUrl, String title) {
        if (imageUrl == null || imageUrl.isEmpty()) {
            Toast.makeText(this, "Image not available", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create ImageView
        ImageView imageView = new ImageView(this);
        imageView.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        imageView.setAdjustViewBounds(true);

        // Load image
        Picasso.get()
                .load(imageUrl)
                .into(imageView);

        // Create AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title)
                .setView(imageView)
                .setPositiveButton("Close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }


    private void setStatusBarColor() {
        Window window = getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.them_color));

            // Android 14+ के लिए
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                WindowInsetsControllerCompat controller =
                        WindowCompat.getInsetsController(window, window.getDecorView());
                controller.setAppearanceLightStatusBars(false);
            }
        }
        WindowCompat.setDecorFitsSystemWindows(window, true);
    }
}