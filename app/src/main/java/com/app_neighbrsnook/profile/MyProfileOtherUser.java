package com.app_neighbrsnook.profile;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.app_neighbrsnook.PostActivity;
import com.app_neighbrsnook.businessModule.BusinessActivity;
import com.app_neighbrsnook.event.EventAllListCurrentData;
import com.app_neighbrsnook.group.GroupActivity;
import com.app_neighbrsnook.marketPlace.YourItemListActivity;
import com.app_neighbrsnook.pojo.ProfilePojo;
import com.app_neighbrsnook.pollModule.PollActivityDemo2;
import com.app_neighbrsnook.utils.VerticalLineView;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.app_neighbrsnook.R;
import com.app_neighbrsnook.network.APIClient;
import com.app_neighbrsnook.network.APIInterface;
import com.app_neighbrsnook.utils.PrefMananger;
import com.app_neighbrsnook.utils.SharedPrefsManager;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyProfileOtherUser extends AppCompatActivity {
    FrameLayout frm_events, frm_groups, frm_profession;
    ImageView img_back, img_edit_two;
    FrameLayout frm_suggestion, frmMobileGone;
    String chooseInterest, interestId, profesionId, location_neighbrhood, fname, post, suggestion, event, business, stUsername, createuser, poll, lovengh, loveId, group, directmsg, mail, favourite, tv_phone, emergency, dob, address, addressOne, addressTwo, genderProfile, whatDoYou;
    TextView tv_mail;
    TextView tv_mobile, tv_emergency, tv_address, tv_dob, tv_gender, tv_what_do, tv_interest, tv_love;
    FrameLayout img_edit_other, img_edit_personal, img_love_ngh, img_address, frm_posts;
    FrameLayout frm_poll, frm_profile_busines, frm_fav, frm_market;
    Activity activity;
    TextView tvLastName;
    ProgressBar progressEvent, progressPolls, progressBusiness, progressGroups, progressPosts;
    Context context;
    TextView post_no, suggestion_no, tv_events, tv_business, user_create_ac, tv_polls, tv_groups, tv_dm, tv_favourite, tvdeo;
    TextView tv_user_name, tv_profession, view_profession, tv_location_ngh;
    CircleImageView img_profile;
    HashMap<String, Object> hashMap = new HashMap<>();
    RelativeLayout rl_my_profile;
    int fromuserid;
    ImageView img_edit_profile;
    TextView tv_take_photo, tv_choose_photo, tv_cancel;
    SharedPrefsManager sm;
    String stPostTotal, stPostPerc, stPollTotal, stPollPerc, stBusinessTotal, stBusinessPerc, stGroupTotal, stGrouopPer, stEventNeigh, stEventPer;
    TextView eventsNeighbrhoodTotal, eventPercent, pollTotal, pollPerc, postTotal, postPerc, businessTotal, businessPerc, groupTotal, groupPerc, tvMobileGone;

    ProfilePojo pojo;

    String whiteCircle = "\u25E6", solidCircle = "\u2022", smallSquare = "\u25AA", start = "\u2605";
    VerticalLineView eventLinePer, pollLinePer, businessLinePer, groupLinePer, postLinePer, marketLinePer;
    int eventPer, pollPer, businessPer, groupPer, postPer, marketPer;
    int eventColor = 0xFF9DAC7C, pollColor = 0xFFAF9D66, businessColor = 0xFF829591, groupColor = 0xFF4F7959, postColor = 0xFF985646, marketColor = 0xFF8E836F;
    TextView market_no, marketTotal, marketPerc;
    String strMarket, strMarketTot, strMarketPer;

    // private Bitmap bitmap;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = activity = this;
        setContentView(R.layout.activity_my_profile_other);

        sm = new SharedPrefsManager(this);
        Intent i = getIntent();
        fromuserid = i.getIntExtra("user_id", 0);

        item();
        otherUserProfile();

        img_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent detailScreen = new Intent(context, OtherUserShowPicture.class);
                detailScreen.putExtra("user_id", fromuserid);
                context.startActivity(detailScreen);
            }
        });
        frm_market.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!strMarket.equals("0")) {
                    Intent intent = new Intent(MyProfileOtherUser.this, YourItemListActivity.class);
                    intent.putExtra("user_id", fromuserid);
                    intent.putExtra("checkSource", true);
                    startActivity(intent);
                } else {
                    Toast.makeText(MyProfileOtherUser.this, "Marketplace post is zero!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        frm_events.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyProfileOtherUser.this, EventAllListCurrentData.class);
                intent.putExtra("user_id", fromuserid);
                startActivity(intent);
            }
        });
        frm_groups.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyProfileOtherUser.this, GroupActivity.class);
                intent.putExtra("user_id", fromuserid);
                startActivity(intent);
            }
        });
        frm_poll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyProfileOtherUser.this, PollActivityDemo2.class);
                intent.putExtra("user_id", fromuserid);
                startActivity(intent);
            }
        });
        frm_profile_busines.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyProfileOtherUser.this, BusinessActivity.class);
                intent.putExtra("user_id", fromuserid);
                startActivity(intent);
            }
        });
        frm_posts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyProfileOtherUser.this, PostActivity.class);
                intent.putExtra("user_id", fromuserid);
                intent.putExtra("source", "other");
                startActivity(intent);
            }
        });
    }

    private void item() {
        frm_events = findViewById(R.id.events_card_id);
        tv_cancel = findViewById(R.id.cancle_tv);
        eventPercent = findViewById(R.id.eventPercent);
        eventsNeighbrhoodTotal = findViewById(R.id.eventsNeighbrhoodTotal);
        progressEvent = findViewById(R.id.progressBar);
        progressPolls = findViewById(R.id.progressBarPolls);
        progressBusiness = findViewById(R.id.progressBarBusiness);
        progressGroups = findViewById(R.id.progressBarGroups);
        progressPosts = findViewById(R.id.progressBarPosts);
        tvMobileGone = findViewById(R.id.tvMobileGone);
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
        postTotal = findViewById(R.id.postTotal);
        postPerc = findViewById(R.id.postPerc);
        pollTotal = findViewById(R.id.pollTotal);
        pollPerc = findViewById(R.id.pollPerc);
        groupTotal = findViewById(R.id.groupsTotal);
        groupPerc = findViewById(R.id.groupPerc);
        businessTotal = findViewById(R.id.businessTotal);
        businessPerc = findViewById(R.id.businessPerc);
        tvLastName = findViewById(R.id.lastName);
        frmMobileGone = findViewById(R.id.frmMobileGone);
        img_edit_profile = findViewById(R.id.img_edit_profile);
        rl_my_profile = findViewById(R.id.rl_my_profile);
        img_edit_two = findViewById(R.id.id_edit_profile);
        tvdeo = findViewById(R.id.deo);
        img_profile = findViewById(R.id.id_profile_img);
        tv_love = findViewById(R.id.tv_love_safe_id);
        user_create_ac = findViewById(R.id.member_create);
        frm_groups = findViewById(R.id.frm_groups_id);
        img_back = findViewById(R.id.img_back);
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
        tv_address = findViewById(R.id.tv_address);
        tv_dob = findViewById(R.id.tv_dob_profile);
        tv_gender = findViewById(R.id.tv_gender);
        tv_what_do = findViewById(R.id.tv_what_do_you_do);
        tv_interest = findViewById(R.id.tv_interest);
        frm_posts = findViewById(R.id.frm_posts);
        frm_profession = findViewById(R.id.frm_profession);
        tv_profession = findViewById(R.id.tv_profession);
        view_profession = findViewById(R.id.view_profession);
        tv_take_photo = findViewById(R.id.take_photo);
        tv_choose_photo = findViewById(R.id.choose_photo);
    }

    private void otherUserProfile() {
        hashMap.put("userid", fromuserid);
        hashMap.put("loggeduser", Integer.parseInt(PrefMananger.GetLoginData(context).getId() + ""));
        Log.e("fdsfgsd", hashMap.toString());
        userProfile(hashMap);
    }

    private void userProfile(HashMap<String, Object> hm) {
        ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Please wait...");
        dialog.setCancelable(false);
        dialog.show();
        APIInterface service = APIClient.getRetrofit().create(APIInterface.class);
        Call<JsonElement> call = service.otherProfileview("userprofile", Integer.parseInt(PrefMananger.GetLoginData(context).getId()), hm);
        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                dialog.dismiss();
                JsonElement jsonElement = response.body();
                JsonObject jsonObject = jsonElement.getAsJsonObject();
                try {
                    location_neighbrhood = jsonObject.get("neighborhood").getAsString();
                    post = jsonObject.get("posts").getAsString();
                    suggestion = jsonObject.get("suggestions").getAsString();
                    event = jsonObject.get("events").getAsString();
                    business = jsonObject.get("business").getAsString();
                    mail = jsonObject.get("emailid").getAsString();
                    createuser = jsonObject.get("createddate").getAsString();
                    poll = jsonObject.get("polls").getAsString();
                    group = jsonObject.get("groups").getAsString();
                    directmsg = jsonObject.get("directmessage").getAsString();
                    favourite = jsonObject.get("favourites").getAsString();
                    tv_phone = jsonObject.get("phoneno").getAsString();
                    emergency = jsonObject.get("emer_phone").getAsString();
                    address = jsonObject.get("addressone").getAsString();
                    dob = jsonObject.get("dob").getAsString();
                    genderProfile = jsonObject.get("gender").getAsString();
                    whatDoYou = jsonObject.get("nbrs_type").getAsString();
                    chooseInterest = jsonObject.get("intersttype").getAsString();
                    interestId = jsonObject.get("intid").getAsString();
                    profesionId = jsonObject.get("profid").getAsString();
                    lovengh = jsonObject.get("reason").getAsString();

                    stUsername = jsonObject.get("username").getAsString();
                    strMarket = jsonObject.get("market").getAsString();
                    strMarketTot = jsonObject.get("totmarket").getAsString();
                    strMarketPer = jsonObject.get("marketper").getAsString();

                    stEventNeigh = jsonObject.get("totevents").getAsString();
                    stEventPer = jsonObject.get("eventper").getAsString();
                    stPostTotal = jsonObject.get("totposts").getAsString();
                    stPollTotal = jsonObject.get("totpolls").getAsString();
                    stBusinessTotal = jsonObject.get("totbusiness").getAsString();
                    stGroupTotal = jsonObject.get("totgroups").getAsString();
                    stPostPerc = jsonObject.get("postper").getAsString();
                    stPollPerc = jsonObject.get("pollper").getAsString();
                    stBusinessPerc = jsonObject.get("businessper").getAsString();
                    stGrouopPer = jsonObject.get("groupper").getAsString();

                    if (whatDoYou.isEmpty()) {
                        frm_profession.setVisibility(View.GONE);
                        view_profession.setVisibility(View.GONE);
                    }

                    tv_profession.setText(whatDoYou);
                    eventsNeighbrhoodTotal.setText(stEventNeigh);
                    eventPercent.setText(stEventPer);
                    tv_user_name.setText(stUsername);
                    postTotal.setText(stPostTotal);
                    postPerc.setText(stPostPerc);
                    pollTotal.setText(stPollTotal);
                    pollPerc.setText(stPollPerc);
                    businessTotal.setText(stBusinessTotal);
                    businessPerc.setText(stBusinessPerc);
                    groupTotal.setText(stGroupTotal);
                    groupPerc.setText(stGrouopPer);
                    market_no.setText(strMarket);
                    marketTotal.setText(strMarketTot);
                    marketPerc.setText(strMarketPer);
                    tv_location_ngh.setText(location_neighbrhood);
                    post_no.setText(post);
                    suggestion_no.setText(suggestion);
                    tv_events.setText(event);
                    tv_business.setText(business);
                    tv_mail.setText(mail);
                    user_create_ac.setText(createuser);
                    tv_polls.setText(poll);
                    tv_groups.setText(group);
                    tv_dm.setText(directmsg);
                    tv_favourite.setText(favourite);
                    tv_dob.setText(dob);
                    tv_gender.setText(genderProfile);

                    if (address == null || address.trim().isEmpty()) {
                        tv_address.setText("NA");
                    } else {
                        tv_address.setText(address);
                    }

                    if (tv_phone == null || tv_phone.trim().isEmpty()) {
                        tv_mobile.setText("NA");
                    } else {
                        tv_mobile.setText(tv_phone);
                    }

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
                Picasso.get().load(jsonObject.get("userpic").getAsString()).into(img_profile);
                if (jsonObject.get("userpic").getAsString().isEmpty()) {
                    img_profile.setImageResource(R.drawable.profile_circle_white_background);
                } else {
                    Picasso.get().load(jsonObject.get("userpic").getAsString()).fit().centerCrop().error(R.drawable.profile_circle_white_background).placeholder(R.drawable.profile_circle_white_background)
                            .into(img_profile);
                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                Toast.makeText(MyProfileOtherUser.this, "Data found", Toast.LENGTH_SHORT).show();
                Log.d("res", t.getMessage());
            }
        });
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

}