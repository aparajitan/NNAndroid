package com.app_neighbrsnook.fragment;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app_neighbrsnook.MainActivity;
import com.app_neighbrsnook.intreface.OnUpdateFavListener;
import com.app_neighbrsnook.intreface.OnUpdateFavListenerBusiness;
import com.app_neighbrsnook.network.APIClient;
import com.app_neighbrsnook.network.APIInterface;
import com.app_neighbrsnook.pojo.marketPlacePojo.CommonPojoSuccess;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.app_neighbrsnook.R;
import com.app_neighbrsnook.model.wall.Listdatum;
import com.app_neighbrsnook.SalesModuleMarketPlace.SellerChatActivity;
import com.app_neighbrsnook.utils.GlobalMethods;
import com.app_neighbrsnook.utils.SharedPrefsManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BottomSheetBusinessFragment extends BottomSheetDialogFragment {

    RelativeLayout report_rl;
    RelativeLayout chat_rl, fav_rl, share_rl, block_rl;
    int id, favouritstatus, pos;
    String userName, userPic, msgSubject, businessName, category, catId, neighbrhood;
    Dialog dialog;
    TextView fav_tv, fav_subtitle_tv;
    String bid, userid;
    List<Listdatum> mList;
    List<com.app_neighbrsnook.pojo.Listdatum> businessList;
    SharedPrefsManager sm;
    View view1;
    String source = "abc";
    int status;
    OnUpdateFavListener mListener;
    OnUpdateFavListenerBusiness mListenerBusiness;
    Context context;
    String imageUrl;

    public BottomSheetBusinessFragment(Context context, String imageUrl, int favouritstatus, String businessName, String businessId, String businessSubject,
                                       String userName, String userPic, String userid, String categoryName, String neighbrhood,
                                       String source) {
        this.imageUrl = imageUrl;
        this.context = context;
        this.favouritstatus = favouritstatus;
        this.businessName = businessName;
        this.msgSubject = businessSubject;
        this.bid = businessId;
        this.userName = userName;
        this.userPic = userPic;
        this.userid = userid;
        this.category = categoryName;
        this.neighbrhood = neighbrhood;
        this.source = source;
    }

    public BottomSheetBusinessFragment(List<Listdatum> mList, int pos) {
        this.mList = mList;
        this.pos = pos;
    }

    public BottomSheetBusinessFragment(List<com.app_neighbrsnook.pojo.Listdatum> businessList, int pos, String business) {
        this.businessList = businessList;
        this.pos = pos;
        this.source = business;
    }

    public BottomSheetBusinessFragment(List<Listdatum> mList, int pos, String wall, String s) {
        this.mList = mList;
        this.pos = pos;
        this.source = wall;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sm = new SharedPrefsManager(getActivity());
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void setupDialog(Dialog dialog, int style) {
        View contentView = View.inflate(getContext(), R.layout.fragment_bottom_sheet, null);
        chat_rl = contentView.findViewById(R.id.dm_rl);
        fav_tv = contentView.findViewById(R.id.fav_tv);
        fav_rl = contentView.findViewById(R.id.fav_rl);
        view1 = contentView.findViewById(R.id.chat_view);
        share_rl = contentView.findViewById(R.id.share_rl);
        fav_subtitle_tv = contentView.findViewById(R.id.fav_subtitle_tv);
        block_rl = contentView.findViewById(R.id.block_rl);
//        if(source.equals("business"))
//        {
//            status = businessList.get(pos).getFastatus();
//            userName = businessList.get(pos).getUsername();
//            userPic= businessList.get(pos).getUserpic();
//            msgSubject = businessList.get(pos).getCategory();
//            businessName = businessList.get(pos).getName();
//            category= businessList.get(pos).getCategory();
//            catId =  businessList.get(pos).getCatid();
//            userid = Integer.parseInt(businessList.get(pos).getUserid());
//            id = Integer.parseInt(businessList.get(pos).getId());
//            neighbrhood = businessList.get(pos).getNeighborhood();
//        }
//        else {
//            status = mList.get(pos).getFavouritstatus();
//            userName = mList.get(pos).getUsername();
//            userPic= mList.get(pos).getUserpic();
//            msgSubject = mList.get(pos).getCategory();
//            businessName = mList.get(pos).getBusinessName();
//            category= mList.get(pos).getCategory();
////            catId =  mList.get(pos).getc();
//            id = Integer.parseInt(mList.get(pos).getbId());
//            neighbrhood = mList.get(pos).getNeighborhood();
//        }

        if (favouritstatus == 0) {
            fav_tv.setText("Favourite Post");
            fav_subtitle_tv.setText("Add this to my favourites.");
        } else {
            fav_tv.setText("Unfavourite Post");
            fav_subtitle_tv.setText("Remove this from my favourites.");
        }

        if (Integer.parseInt(userid) == Integer.parseInt(sm.getString("user_id"))) {
            chat_rl.setVisibility(View.GONE);
            view1.setVisibility(View.GONE);
            block_rl.setVisibility(View.GONE);
        }

        block_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(context)
                        .setTitle("Confirm Block")
                        .setMessage("Are you sure you want to block " + userName + "?")
                        .setCancelable(false)
                        .setPositiveButton("OK", (okDialog, which) -> {
                            HashMap<String, Object> hm = new HashMap<>();
                            hm.put("blocker_userid", Integer.parseInt(sm.getString("user_id")));
                            hm.put("blocked_userid", userid);
                            hm.put("action", "block");

                            APIInterface service = APIClient.getRetrofit3().create(APIInterface.class);
                            Call<CommonPojoSuccess> call = service.userBlockApi(hm);

                            call.enqueue(new Callback<CommonPojoSuccess>() {
                                @SuppressLint("NotifyDataSetChanged")
                                @Override
                                public void onResponse(Call<CommonPojoSuccess> call, Response<CommonPojoSuccess> response) {
                                    if (response.isSuccessful() && response.body() != null) {
                                        CommonPojoSuccess commonPojoSuccess = response.body();
                                        if ("success".equals(commonPojoSuccess.getStatus())) {
                                            Intent i = new Intent(BottomSheetBusinessFragment.this.context, MainActivity.class);
                                            context.startActivity(i);
                                            okDialog.dismiss();
                                            dialog.dismiss();
                                        }
                                    } else {
                                        Log.e(TAG, "Token Update Failed: " + response.errorBody());
                                    }
                                }

                                @Override
                                public void onFailure(Call<CommonPojoSuccess> call, Throwable t) {
                                    Log.e(TAG, "API Failure: " + t.getMessage());
                                }
                            });
                            okDialog.dismiss();
                            dialog.dismiss();
                        })
                        .setNegativeButton("Cancel", (okDialog, which) -> {
                            okDialog.dismiss();
                            dialog.dismiss();
                        })
                        .show();
            }
        });

        chat_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), SellerChatActivity.class);
                i.putExtra("eventId", Integer.parseInt(userid));
                i.putExtra("userName", userName);
                i.putExtra("subject", category);
                i.putExtra("pic", userPic);
                startActivity(i);
                dialog.dismiss();
            }
        });

        share_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                downloadAndShareImage(imageUrl, pos);
                dialog.dismiss();
            }
        });

        fav_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HashMap<String, Object> hm = new HashMap<>();
                hm.put("userid", Integer.parseInt(sm.getString("user_id")));
                hm.put("postid", Integer.parseInt(bid));
                hm.put("type", "Business");
                if (favouritstatus == 0) {
                    hm.put("neighbrhood", neighbrhood);
                    GlobalMethods.favApi("userfavouritespost", hm, dialog, getActivity());
                    if (source.equals("business")) {
                        sendWallStatus("business", "business");
                    } else if (source.equals("wall")) {
                        sendWallStatus("business", source);
                    } else if (source.equals("fav")) {
                        sendWallStatus("business", source);
                    }
                } else {
                    GlobalMethods.favApi("userunfavouritepost", hm, dialog, getActivity());
                    if (source.equals("business")) {
                        sendWallStatus("business", "business");
                    } else if (source.equals("wall")) {
                        sendWallStatus("business", source);
                    } else if (source.equals("fav")) {
                        sendWallStatus("business", source);
                    }
                }
            }
        });

        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setContentView(contentView);
        ((View) contentView.getParent()).setBackgroundColor(getResources().getColor(android.R.color.transparent));
    }

    @Override
    public int getTheme() {
        return R.style.CustomBottomSheetDialog;
    }

    private void msgDialog(String str) {
        RecyclerView rv;
        TextView confirm, msg_textview;
        ImageView cancel;
        dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.msg_dialog);
        cancel = dialog.findViewById(R.id.cross_imageview);
        msg_textview = dialog.findViewById(R.id.msg_textview);


        msg_textview.setText(str);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(getActivity(), android.R.color.transparent)));
        dialog.setCancelable(false);
        dialog.show();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (source.equals("business")) {
            if (context instanceof OnUpdateFavListenerBusiness) {
                mListenerBusiness = (OnUpdateFavListenerBusiness) context;
            } else {
                throw new ClassCastException(context.toString() + " must implement OnFragmentInteractionListener");
            }

        } else if (source.equals("wall")) {
            if (context instanceof OnUpdateFavListener) {
                mListener = (OnUpdateFavListener) context;
            } else {
                throw new ClassCastException(context.toString() + " must implement OnFragmentInteractionListener");
            }
        } else if (source.equals("fav")) {
            if (context instanceof OnUpdateFavListener) {
                mListener = (OnUpdateFavListener) context;
            } else {
                throw new ClassCastException(context.toString() + " must implement OnFragmentInteractionListener");
            }
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (source.equals("business")) {
            mListenerBusiness = null;
        } else if (source.equals("wall")) {
            mListener = null;
        } else if (source.equals("fav")) {
            mListener = null;
        }
    }

    private void sendWallStatus(String count, String type) {
        if (type.equals("business")) {
            if (mListenerBusiness != null) {
                mListenerBusiness.onListItemClicked(count);
            }
        } else if (type.equals("wall")) {
            if (mListener != null) {
                mListener.onListItemClicked(count, type);
            }
        } else if (type.equals("fav")) {
            if (mListener != null) {
                mListener.onListItemClicked(count, type);
            }
        }
    }

    private void downloadAndShareImage(String imageUrl, int pos) {
        if (imageUrl == null || imageUrl.isEmpty() || imageUrl.contains("video")) {
            shareText(pos);
            return;
        }

        new Thread(() -> {
            try {
                URL url = new URL(imageUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();

                InputStream inputStream = connection.getInputStream();
                File imageFile = new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "shared_image.jpg");
                FileOutputStream outputStream = new FileOutputStream(imageFile);

                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }

                outputStream.close();
                inputStream.close();

                shareImage(imageFile, pos);

            } catch (Exception e) {

                Toast.makeText(context, "Error downloading image: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }).start();
    }

    private void shareImage(File imageFile, int pos) {
        try {
            Uri imageUri = FileProvider.getUriForFile(context, context.getPackageName() + ".fileprovider", imageFile);

            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("image/*");
            shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Neighbrsnook business");

            shareIntent.putExtra(Intent.EXTRA_TEXT, "Checkout this event on Neighbrsnook,\n" + "\n" + businessName + "\n" + category +
                    "\n" + sm.getString("neighbrhood_name") +
                    "\n\nhttps://neighbrsnook.com/open-app" +
                    "\n\nhttps://neighbrsnook.com/");
            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

            context.startActivity(Intent.createChooser(shareIntent, "Share via"));
        } catch (Exception e) {

            Toast.makeText(context, "Error sharing image: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void shareText(int pos) {
        try {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Neighbrsnook business");

            shareIntent.putExtra(Intent.EXTRA_TEXT, "Checkout this event on Neighbrsnook,\n" + "\n" + businessName + "\n" + category +
                    "\n" + sm.getString("neighbrhood_name") +
                    "\n\nhttps://neighbrsnook.com/open-app" +
                    "\n\nhttps://neighbrsnook.com/");

            context.startActivity(Intent.createChooser(shareIntent, "Share via"));
        } catch (Exception e) {

            Toast.makeText(context, "Error sharing image: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}