package com.app_neighbrsnook;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.core.view.WindowCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app_neighbrsnook.intreface.OnUpdateFavListener;
import com.app_neighbrsnook.marketPlace.MarketPlace;

import com.app_neighbrsnook.pojo.marketPlacePojo.CommonPojoSuccess;
import com.app_neighbrsnook.realStateModule.RealStateMainScreenPg;
import com.app_neighbrsnook.utils.GlobalMethods;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.app_neighbrsnook.businessModule.BusinessActivity;
import com.app_neighbrsnook.event.EventAllListCurrentData;
import com.app_neighbrsnook.fragment.DmFragment;
import com.app_neighbrsnook.fragment.EmergencyFragment;
import com.app_neighbrsnook.fragment.FavFragment;
import com.app_neighbrsnook.fragment.MarketPlaceFragment;
import com.app_neighbrsnook.fragment.NotificationFragment;
import com.app_neighbrsnook.fragment.RentSellPgFragment;
import com.app_neighbrsnook.fragment.SellFragment;
import com.app_neighbrsnook.fragment.WallFragment;
import com.app_neighbrsnook.group.GroupActivity;
import com.app_neighbrsnook.login.LoginActivity;
import com.app_neighbrsnook.model.MenuModel;
import com.app_neighbrsnook.myNeighbourhood.MyNeighbourhoodActivity;
import com.app_neighbrsnook.network.APIClient;
import com.app_neighbrsnook.network.APIInterface;
import com.app_neighbrsnook.pojo.StatePojo;
import com.app_neighbrsnook.pollModule.PollActivity;
import com.app_neighbrsnook.profile.MyProfile;
import com.app_neighbrsnook.setting.SettingAcitivity;
import com.app_neighbrsnook.utils.PrefMananger;
import com.app_neighbrsnook.utils.SharedPrefsManager;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.shashank.sony.fancytoastlib.FancyToast;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener, WallFragment.BottomSheetListener, WallFragment.OnFragmentInteractionListener, NotificationFragment.OnNotificationCountListener, OnUpdateFavListener, MyProfile.UpdateUserPic {
    BottomNavigationView bottomNavigationView;
    DrawerLayout drawerLayout;
    List<MenuModel> headerList = new ArrayList<>();
    HashMap<MenuModel, List<MenuModel>> childList = new HashMap<>();
    ExpandableListAdapter expandableListAdapter;
    private Button button;
    private View notificationBadge;
    NotificationFragment notificationFragment;
    DmFragment dmFragment;
    EmergencyFragment emergencyFragment;
    FavFragment favFragment;
    ImageView img_back, img_plus;
    WallFragment wallFragment;
    SellFragment sellFragment;
    MarketPlaceFragment marketPlaceFragment;
    String fname, lName, neighbrhoodName;
    int count;
    RentSellPgFragment rentSellPgFragment;
    TextView user_neighbourhood;
    ExpandableListView expandableListView;
    HashMap<String, Object> hashMapProfile;
    TextView tv;
    LinearLayout ll_expend_profile;
    TextView tv_user_name;
    ImageView img_photo;
    Context context;
    Activity activity;
    int version;
    String vs;
    SharedPrefsManager sm;
    HashMap<String, Object> hashMap;
    boolean isVerifiedUser;
    String value;
    Dialog dialog;
    private static final String TAG = "FCM Token";
    private static final String LOGINTAG = "LOGIN FCM Token";
    String token, loginToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = activity = this;
        setContentView(R.layout.activity_main);
        if (!GlobalMethods.checkNotificationPermissionWallUsed(this)) {
            GlobalMethods.requestNotificationPermission(this);
        }
        sm = new SharedPrefsManager(this);
        PrefMananger.saveScreen(context, PrefMananger.MAIN_ACTIVITY);
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) { // API 21+
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.them_color));
        }*/
        hashMap = new HashMap<>();
        hashMap.put("loggeduser", Integer.parseInt(sm.getString("user_id")));
        userProfile(hashMap);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            value = extras.getString("title");
            if (value.equals("Business created")) {
                String s = "Your business page has been created successfully. It will be published shortly after a quick review.";
//                GlobalMethods.getInstance(this).globalDialog(this, s);
                globalDialog(s);
            } else {
                globalDialog("Business has been modified succesfully. It will be published after a quick review.");
            }

        }
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        drawerLayout = findViewById(R.id.drawer_layout);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.home);
        expandableListView = findViewById(R.id.expandableListView);
//            button = findViewById(R.id.button);

        NavigationView navigationView = findViewById(R.id.nav_view);
        img_photo = navigationView.getHeaderView(0).findViewById(R.id.img_profile);
        ll_expend_profile = navigationView.getHeaderView(0).findViewById(R.id.ll_expend_profile);
        tv_user_name = navigationView.getHeaderView(0).findViewById(R.id.user_name);


//            Picasso.get().load(sm.getString("userphoto")).into(img_photo);

        user_neighbourhood = navigationView.getHeaderView(0).findViewById(R.id.user_neighbourhood);

        vs = "V" + getAppVersionName() + " @Neighbrsnook";
        notificationCount();
        addBadgeView();
        prepareMenuData();
        populateExpandableList();
        ll_expend_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MyProfile.class);
                startActivity(intent);
                drawerLayout.close();
            }
        });


        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                        return;
                    }

                    token = task.getResult();
                    loginToken = sm.getString("firebase_token");
                    Log.d(TAG, "FCM Token: " + token);
                    Log.d(LOGINTAG, "Login FCM Token: " + sm.getString("firebase_token"));
                    if (loginToken == null || loginToken.isEmpty() || !loginToken.equals(token)) {
                        sendTokenToServer(token);
                    }
                });
    }


    private void addBadgeView() {
        BottomNavigationMenuView menuView = (BottomNavigationMenuView) bottomNavigationView.getChildAt(0);
        BottomNavigationItemView itemView = (BottomNavigationItemView) menuView.getChildAt(3);
        notificationBadge = LayoutInflater.from(this).inflate(R.layout.view_notification_badge, menuView, false);
        tv = notificationBadge.findViewById(R.id.badge);
        tv.setVisibility(GONE);
        itemView.addView(notificationBadge);
    }


    private void prepareMenuData() {
        MenuModel menuModel = new MenuModel("My Profile", true, false, "");
        headerList.add(menuModel);

        menuModel = new MenuModel("My Neighbourhood", true, false, "");
        headerList.add(menuModel);

        menuModel = new MenuModel("Business", true, false, "");
        headerList.add(menuModel);

/*
        menuModel = new MenuModel("Direct Message", true, false, "");
        headerList.add(menuModel);
*/

        menuModel = new MenuModel("Event", true, false, "");
        headerList.add(menuModel);

        menuModel = new MenuModel("Group", true, false, "");
        headerList.add(menuModel);

        menuModel = new MenuModel("Market", true, false, "");
        headerList.add(menuModel);
        /*menuModel = new MenuModel("Favourite", true, false, "");
        headerList.add(menuModel);
        */

        menuModel = new MenuModel("Poll", true, false, "");
        headerList.add(menuModel);

        menuModel = new MenuModel("Post", true, false, "");
        headerList.add(menuModel);

        menuModel = new MenuModel("Public Agency Directory", true, false, "");
        headerList.add(menuModel);

        menuModel = new MenuModel("Refer a neighbour", true, false, "");
        headerList.add(menuModel);

        menuModel = new MenuModel("Share app", true, false, "");
        headerList.add(menuModel);


        menuModel = new MenuModel("Settings", true, false, "");
        headerList.add(menuModel);

        /*menuModel = new MenuModel("About Us", true, false, "");
        headerList.add(menuModel);

        menuModel = new MenuModel("FAQ", true, false, "");
        headerList.add(menuModel);

        menuModel = new MenuModel("Privacy Policy", true, false, "");
        headerList.add(menuModel);*/

        menuModel = new MenuModel("Contact us", true, false, "");
        headerList.add(menuModel);



        menuModel = new MenuModel(vs, true, false, "");
        headerList.add(menuModel);
    }

    private void populateExpandableList() {
        int[] icon = {R.drawable.profile,
                R.drawable.location,
                R.drawable.business,
                //   R.drawable.ic_outline_send_dm_24,
                R.drawable.event_ict,
                R.drawable.marketplace_ic,
                //  R.drawable.ic_baseline_star_outline_24,
                R.drawable.group_ict,
                R.drawable.poll_ict,
                R.drawable.post_ict,
                R.drawable.public_dir,
                R.drawable.ic_refer,
                R.drawable.share,

                // R.drawable.ic_market,
                // R.drawable.contact_us,
                //  R.drawable.about_us,
                //   R.drawable.faq,
                //18.02.25 R.drawable.realstate_ict,
                R.drawable.setting_ict,
                R.drawable.contact_ict,
//                   R.drawable.setting,
                0};

        expandableListAdapter = new ExpandableListAdapter(this, headerList, childList, icon);
        expandableListView.setAdapter(expandableListAdapter);
        expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                if (headerList.get(groupPosition).isGroup) {
                    if (!headerList.get(groupPosition).hasChildren) {
                        if (headerList.get(groupPosition).menuName.equals("Business")) {
                            Intent intent = new Intent(MainActivity.this, BusinessActivity.class);
                            intent.putExtra("title", "Businesses");
                            startActivity(intent);
                            drawerLayout.close();
                            bottomNavigationView.setSelectedItemId(R.id.home);
                        } else if (headerList.get(groupPosition).menuName.equals("Poll")) {
                            Intent intent = new Intent(MainActivity.this, PollActivity.class);
                            intent.putExtra("from", "drawar");
                            startActivity(intent);
                            drawerLayout.close();
                            bottomNavigationView.setSelectedItemId(R.id.home);
                        } else if (headerList.get(groupPosition).menuName.equals("Public Agency Directory")) {
                            Intent intent = new Intent(MainActivity.this, PublicAgencyDirActivity.class);
                            intent.putExtra("title", "Public Agency Directory");
                            startActivity(intent);
                            drawerLayout.close();
                            bottomNavigationView.setSelectedItemId(R.id.home);
                        } else if (headerList.get(groupPosition).menuName.equals("Post")) {
                            Intent intent = new Intent(MainActivity.this, PostActivity.class);
                            intent.putExtra("source", "wall");
                            startActivity(intent);
                            drawerLayout.close();
                        }

//                            else if (headerList.get(groupPosition).menuName.equals("My near by")) {
//                                Intent intent = new Intent(MainActivity.this, NearByActivity.class);
//                                intent.putExtra("title", "My near by");
//                                startActivity(intent);
//                                drawerLayout.close();
//                                bottomNavigationView.setSelectedItemId(R.id.home);
//                            }

                        /*else if (headerList.get(groupPosition).menuName.equals("Direct Message")) {
                            if (isVerifiedUser) {
                                dmFragment = new DmFragment();
                                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, dmFragment).addToBackStack("Direct_msg").commit();
                            } else {
                                GlobalMethods.getInstance(MainActivity.this).globalDialog(MainActivity.this, "You have limited access till verification is complete.");
                            }
                            drawerLayout.close();
//                                bottomNavigationView.setSelectedItemId(R.id.home);
                        } else if (headerList.get(groupPosition).menuName.equals("Favourite")) {
                            favFragment = new FavFragment();
                            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                    favFragment).addToBackStack("fav").commit();
//                                bottomNavigationView.setSelectedItemId(R.id.favourite);
                            drawerLayout.close();
                        }*/

                        else if (headerList.get(groupPosition).menuName.equals("Group")) {
                            Intent i = new Intent(MainActivity.this, GroupActivity.class);
                            i.putExtra("neighbrhood", "drawar");
                            startActivity(i);

                            drawerLayout.close();
                        } else if (headerList.get(groupPosition).menuName.equals("Event")) {
                            Intent intent = new Intent(MainActivity.this, EventAllListCurrentData.class);
                            intent.putExtra("neighbrhood", "drawar");
                            startActivity(intent);
                            drawerLayout.close();
                        } else if (headerList.get(groupPosition).menuName.equals("Market")) {
                            Intent intent = new Intent(MainActivity.this, MarketPlace.class);
                            startActivity(intent);
                            drawerLayout.close();

                        } else if (headerList.get(groupPosition).menuName.equals("My Profile")) {
                            Intent intent = new Intent(MainActivity.this, MyProfile.class);
//                                intent.putExtra("title", "Public Agency Directory");
                            startActivity(intent);
                            drawerLayout.close();
                        } else if (headerList.get(groupPosition).menuName.equals("My Neighbourhood")) {
                            Intent intent = new Intent(MainActivity.this, MyNeighbourhoodActivity.class);
//                                intent.putExtra("title", "Public Agency Directory");
                            startActivity(intent);
                            drawerLayout.close();
                        }

                        else if (headerList.get(groupPosition).menuName.equals("Refer a neighbour")) {
                            Intent intent = new Intent(MainActivity.this, ReferAnNeighbourActivity.class);
                            startActivity(intent);
                            drawerLayout.close();
                        }
                        else if (headerList.get(groupPosition).menuName.equals("Settings")) {
                            Intent intent = new Intent(MainActivity.this, SettingAcitivity.class);
                            startActivity(intent);
                            drawerLayout.close();

                        } else if (headerList.get(groupPosition).menuName.equals("Share app")) {
                            Intent sendIntent = new Intent();
                            sendIntent.setAction(Intent.ACTION_SEND);
                            sendIntent.putExtra(Intent.EXTRA_SUBJECT, "Inviting you to join Neighbrsnook");
                            sendIntent.putExtra(Intent.EXTRA_TEXT, "Neighbrsnook is a hyperlocal social networking service connecting neighbours.\n\nDownload the app from" +
                                    "\nhttps://neighbrsnook.com/open-app" + "\n\nhttps://neighbrsnook.com/");
                            sendIntent.setType("text/plain");
                            Intent shareIntent = Intent.createChooser(sendIntent, null);
                            startActivity(shareIntent);
                            drawerLayout.close();
                        } else if (headerList.get(groupPosition).menuName.equals("Contact us")) {
                            Intent intent = new Intent(MainActivity.this, ContactUsActivity.class);
                            startActivity(intent);
                            drawerLayout.close();
                        } /*else if (headerList.get(groupPosition).menuName.equals("Realstate")) {
                            Intent intent = new Intent(MainActivity.this, RealStateMainScreenPg.class);
                            startActivity(intent);
                            drawerLayout.close();
                        } */ else if (headerList.get(groupPosition).menuName.equals("Logout")) {
                            // showExitAlert();
                            logoutDialog();

                        } else if (headerList.get(groupPosition).menuName.equals(vs)) {
                            headerList.get(groupPosition).setMenuName(vs);
                        }
                    }
                }
                return false;
            }
        });
        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition,
                                        int childPosition, long id) {
                if (childList.get(headerList.get(groupPosition)) != null) {
                    MenuModel model = childList.get(headerList.get(groupPosition)).get(childPosition);

                    if (model.menuName.equals("List of Investors")) {
                    }
                }
                return false;
            }
        });
    }

    @Override
    public void onDataReceived(String data) {

    }

    @Override
    public void onFragmentInteraction(Boolean isVerifiedUser) {
        this.isVerifiedUser = isVerifiedUser;

    }

    @Override
    public void onNotificationCount(int notificationCount) {
        notificationCount();
    }

    @Override
    public void updateProfileImage(String userpic) {
        hashMap.put("loggeduser", Integer.parseInt(PrefMananger.GetLoginData(context).getId() + ""));
        userProfile(hashMap);

    }


    public class ExpandableListAdapter extends BaseExpandableListAdapter {
        private final Context context;
        private final List<MenuModel> listDataHeader;
        private final HashMap<MenuModel, List<MenuModel>> listDataChild;
        int[] icon;

        public ExpandableListAdapter(Context context, List<MenuModel> listDataHeader,
                                     HashMap<MenuModel, List<MenuModel>> listChildData, int[] icon) {
            this.context = context;
            this.listDataHeader = listDataHeader;
            this.listDataChild = listChildData;
            this.icon = icon;
        }

        @Override
        public MenuModel getChild(int groupPosition, int childPosititon) {
            return this.listDataChild.get(this.listDataHeader.get(groupPosition))
                    .get(childPosititon);
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public View getChildView(int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            final String childText = getChild(groupPosition, childPosition).menuName;
            TextView txtListChild = convertView.findViewById(R.id.lblListItem);

            if (convertView == null) {
                LayoutInflater infalInflater = (LayoutInflater) this.context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = infalInflater.inflate(R.layout.list_group, null);
            }
            if (childText.equals(vs)) {
//                    txtListChild.setTextColor(ContextCompat.getColor(MainActivity.this, them_color));
            }

            txtListChild.setText(childText);

//                txtListChild.setTextColor(ContextCompat.getColor(MainActivity.this, them_color));
            return convertView;
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            if (this.listDataChild.get(this.listDataHeader.get(groupPosition)) == null)
                return 0;
            else
                return this.listDataChild.get(this.listDataHeader.get(groupPosition)).size();
        }

        @Override
        public MenuModel getGroup(int groupPosition) {
            return this.listDataHeader.get(groupPosition);
        }

        @Override
        public int getGroupCount() {
            return this.listDataHeader.size();
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            String headerTitle = getGroup(groupPosition).menuName;
            if (convertView == null) {
                LayoutInflater infalInflater = (LayoutInflater) this.context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = infalInflater.inflate(R.layout.list1_item, null);
            }
            TextView lblListHeader = convertView.findViewById(R.id.lblListHeader);
//            if (headerTitle.equals(vs)) {
//                lblListHeader.setTextColor(ContextCompat.getColor(MainActivity.this, them_color));
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                    lblListHeader.setTypeface(getResources().getFont(R.font.montserrat_regular));
//                }
//
//                lblListHeader.setGravity(Gravity.END);
//            }
            ImageView imageview = convertView.findViewById(R.id.imageview);
            imageview.setColorFilter(Color.parseColor("#555555"), PorterDuff.Mode.SRC_IN);
//                lblListHeader.setTypeface(null, Typeface.NORMAL);
            lblListHeader.setText(headerTitle);
            imageview.setImageResource(icon[groupPosition]);
            return convertView;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.home:
                wallFragment = new WallFragment(0);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, wallFragment).commit();
                return true;

            case R.id.favourite:
                if (isVerifiedUser) {
                    dmFragment = new DmFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, dmFragment).addToBackStack("Direct_msg").commit();
                } else {
                    GlobalMethods.getInstance(MainActivity.this).globalDialog(MainActivity.this, getString(R.string.unverified_msg));
                }
                return true;
            case R.id.dm:
                if (isVerifiedUser) {
                    favFragment = new FavFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, favFragment).commit();
                } else {
                    GlobalMethods.getInstance(MainActivity.this).globalDialog(MainActivity.this, getString(R.string.unverified_msg));
                }
//
                return true;
            case R.id.notification:
                if (isVerifiedUser) {
                    notificationFragment = new NotificationFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, notificationFragment).commit();
                } else {
                    GlobalMethods.getInstance(MainActivity.this).globalDialog(MainActivity.this, getString(R.string.unverified_msg));
                }
                return true;
            case R.id.navigation:
                drawerLayout.openDrawer(Gravity.LEFT);
//                    Toast.makeText(getApplicationContext(),"navigation",Toast.LENGTH_SHORT).show();
                return true;
        }
        return false;
    }

    private String getAppVersionName() {
        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            return packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "N/A"; // Return a default value or handle the exception as needed.
        }
    }

    private void notificationCount() {
        HashMap<String, Object> hm = new HashMap<>();
        hm.put("userid", Integer.parseInt(sm.getString("user_id")));
        APIInterface service = APIClient.getRetrofit().create(APIInterface.class);
        Call<StatePojo> call = service.notificationCountApi("counter", "abc1239", hm);
        call.enqueue(new Callback<StatePojo>() {
            @Override
            public void onResponse(Call<StatePojo> call, Response<StatePojo> response) {

                if (response.body().getStatus().equals("success")) {

                    count = response.body().getNotification_count();
                    if (count > 0) {
                        tv.setVisibility(VISIBLE);
                        notificationBadge.setVisibility(VISIBLE);
                        tv.setText(String.valueOf(count));
                    } else {
                        tv.setVisibility(GONE);
                        notificationBadge.setVisibility(GONE);
                    }
                }
            }

            @Override
            public void onFailure(Call<StatePojo> call, Throwable t) {

//                AppCommon.getInstance(PlaceOrderActivity.this).clearNonTouchableFlags(PlaceOrderActivity.this);
                Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("res", t.getMessage());
            }
        });
    }


    public void logoutDialog() {
        Dialog dialog = new Dialog(MainActivity.this);
        dialog.setContentView(R.layout.logout_pop_up);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(MainActivity.this, android.R.color.transparent)));
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
                // after logout cacheclear
                clearAppCache();
                dialog.dismiss();
                PrefMananger.SaveLoginData(getApplicationContext(), null);
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                finishAffinity();
            }
        });
    }

    private void clearAppCache() {
        try {
            File dir = getCacheDir();
            if (dir != null && dir.isDirectory()) {
                String[] children = dir.list();
                for (String child : children) {
                    new File(dir, child).delete();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void userProfile(HashMap<String, Object> hm) {
        ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Please wait...");
        dialog.setCancelable(false);
        dialog.show();
        APIInterface service = APIClient.getRetrofit().create(APIInterface.class);
        Call<JsonElement> call = service.profileapisec("userprofile", Integer.parseInt(sm.getString("user_id")), hm);
        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                dialog.dismiss();
                JsonElement jsonElement = response.body();
                JsonObject jsonObject = jsonElement.getAsJsonObject();
                try {
//                    sm.setString("userphoto", jsonObject.get("userpic").getAsString());
                    fname = jsonObject.get("username").getAsString();
                    //lName = jsonObject.get("lastname").getAsString();
                    neighbrhoodName = jsonObject.get("neighborhood").getAsString();
                    if (jsonObject.get("verfied_msg").getAsString().equals("User Verification is completed!")) {
                        isVerifiedUser = true;
                    } else {
                        isVerifiedUser = false;
                    }
                    user_neighbourhood.setText(neighbrhoodName);
                    tv_user_name.setText(fname);
                    Picasso.get().load(jsonObject.get("userpic").getAsString()).fit().centerCrop().error(R.drawable.profile).placeholder(R.drawable.profile)
                            .into(img_photo);
                    Picasso.get().load(jsonObject.get("userpic").getAsString()).fit().into(img_photo);
                } catch (Exception e) {

                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                dialog.dismiss();

            }
        });
    }

    @Override
    public void onListItemClicked(String itemName, String source) {
        // Handle the item click here
        if (source.equals("wall")) {
            wallFragment = new WallFragment(0);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, wallFragment).commit();
        } else if (source.equals("fav")) {
            favFragment = new FavFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, favFragment).commit();
        }

    }

    public void globalDialog(String str) {

        TextView msg, ok, thanks_tv;


        ImageView cancel, iv_uploaded_image;
        CardView card;
        dialog = new Dialog(MainActivity.this);
        dialog.setContentView(R.layout.global_dialog);
        ok = dialog.findViewById(R.id.ok_textview);
        msg = dialog.findViewById(R.id.msg);
        thanks_tv = dialog.findViewById(R.id.thanks_tv);
        thanks_tv.setVisibility(VISIBLE);

        msg.setText(str);

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(MainActivity.this, android.R.color.transparent)));
        dialog.setCancelable(false);
        dialog.show();
    }

    private void sendTokenToServer(String token) {
        HashMap<String, Object> hm = new HashMap<>();
        hm.put("userid", Integer.parseInt(sm.getString("user_id")));
        hm.put("firebase_token", token);

        APIInterface service = APIClient.getRetrofit3().create(APIInterface.class);
        Call<CommonPojoSuccess> call = service.updateTokenApi(hm);

        call.enqueue(new Callback<CommonPojoSuccess>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(Call<CommonPojoSuccess> call, Response<CommonPojoSuccess> response) {
                if (response.isSuccessful() && response.body() != null) {
                    CommonPojoSuccess commonPojoSuccess = response.body();
                    if ("success".equals(commonPojoSuccess.getStatus())) {
                        sm.setString("firebase_token", token);
                        // FancyToast.makeText(getApplicationContext(), "Token has been updated.", Toast.LENGTH_LONG, FancyToast.SUCCESS, false).show();
                    } else {
                        //  FancyToast.makeText(getApplicationContext(), "Token update failed.", Toast.LENGTH_LONG, FancyToast.ERROR, false).show();
                    }
                } else {
                    Log.e(TAG, "Token Update Failed: " + response.errorBody());
                    FancyToast.makeText(getApplicationContext(), "API Error: Failed to update token.", Toast.LENGTH_LONG, FancyToast.ERROR, false).show();
                }
            }

            @Override
            public void onFailure(Call<CommonPojoSuccess> call, Throwable t) {
                Log.e(TAG, "API Failure: " + t.getMessage());
                FancyToast.makeText(getApplicationContext(), "Network Error: Failed to update token.", Toast.LENGTH_LONG, FancyToast.ERROR, false).show();
            }
        });
    }

}