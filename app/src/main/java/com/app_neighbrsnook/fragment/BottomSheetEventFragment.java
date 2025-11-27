package com.app_neighbrsnook.fragment;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;

import com.app_neighbrsnook.MainActivity;
import com.app_neighbrsnook.intreface.OnUpdateFavListener;
import com.app_neighbrsnook.network.APIClient;
import com.app_neighbrsnook.network.APIInterface;
import com.app_neighbrsnook.pojo.marketPlacePojo.CommonPojoSuccess;
import com.app_neighbrsnook.utils.GlobalMethods;
import com.app_neighbrsnook.utils.SharedPrefsManager;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.app_neighbrsnook.R;
import com.app_neighbrsnook.model.wall.Listdatum;
import com.app_neighbrsnook.SalesModuleMarketPlace.SellerChatActivity;

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

public class BottomSheetEventFragment extends BottomSheetDialogFragment {
    RelativeLayout rl, share_rl, fav_rl, block_rl;
    int id, pos;
    String userName, title;
    List<Listdatum> mList;
    SharedPrefsManager sm;
    OnUpdateFavListener mListener;
    TextView fav_tv, fav_subtitle_tv;
    Context context;

    public BottomSheetEventFragment(Context context, List<Listdatum> mList, int pos) {
        this.context = context;
        this.mList = mList;
        this.pos = pos;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @SuppressLint("RestrictedApi")
    @Override
    public void setupDialog(Dialog dialog, int style) {
        View contentView = View.inflate(getContext(), R.layout.fragment_bottom_sheet_event, null);
        sm = new SharedPrefsManager(getActivity());
        rl = contentView.findViewById(R.id.dm_rl);
        share_rl = contentView.findViewById(R.id.share_rl);
        fav_rl = contentView.findViewById(R.id.fav_rl);
        fav_tv = contentView.findViewById(R.id.fav_tv);
        fav_subtitle_tv = contentView.findViewById(R.id.fav_subtitle_tv);
        block_rl = contentView.findViewById(R.id.block_rl);

        if (mList.get(pos).getFavouritstatus() == 0) {
            fav_tv.setText("Favourite Post");
            fav_subtitle_tv.setText("Add this to my favourites.");
        } else {
            fav_tv.setText("Unfavourite Post");
            fav_subtitle_tv.setText("Remove this from my favourites.");
        }

        if (mList.get(pos).getCreatedby().equals(sm.getString("user_id"))) {
            block_rl.setVisibility(View.GONE);
        }

        block_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(context)
                        .setTitle("Confirm Block")
                        .setMessage("Are you sure you want to block " + mList.get(pos).getUsername() + "?")
                        .setCancelable(false)
                        .setPositiveButton("OK", (okDialog, which) -> {
                            HashMap<String, Object> hm = new HashMap<>();
                            hm.put("blocker_userid", Integer.parseInt(sm.getString("user_id")));
                            hm.put("blocked_userid", mList.get(pos).getCreatedby());
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
                                            Intent i = new Intent(BottomSheetEventFragment.this.context, MainActivity.class);
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

        rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), SellerChatActivity.class);
                i.putExtra("eventId", Integer.parseInt(mList.get(pos).getCreatedby()));
                i.putExtra("userName", mList.get(pos).getUsername());
                i.putExtra("subject", mList.get(pos).getEvent_name());
                i.putExtra("pic", mList.get(pos).getUserpic());
                startActivity(i);
                dialog.dismiss();
            }
        });

        share_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String imageUrl = mList.get(pos).getEventCoverImage();
                downloadAndShareImage(imageUrl, pos);

                dialog.dismiss();
            }
        });

        fav_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HashMap<String, Object> hm = new HashMap<>();
                hm.put("userid", Integer.parseInt(sm.getString("user_id")));
                hm.put("postid", Integer.parseInt(mList.get(pos).getEventid()));
                hm.put("type", "Event");
//                if(fav_tv.getText().equals("Favourite Business")){
                if (mList.get(pos).getFavouritstatus() == 0) {
                    hm.put("neighbrhood", mList.get(pos).getNeighborhood());
                    GlobalMethods.favApi("userfavouritespost", hm, dialog, getActivity());
                    sendWallStatus("Event");
                } else {
                    GlobalMethods.favApi("userunfavouritepost", hm, dialog, getActivity());
                    sendWallStatus("Event");
                }

                dialog.dismiss();
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

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // Ensure the hosting activity implements the interface
        if (context instanceof NotificationFragment.OnNotificationCountListener) {
            mListener = (OnUpdateFavListener) context;
        } else {
            throw new ClassCastException(context.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private void sendWallStatus(String count) {
        if (mListener != null) {
            mListener.onListItemClicked(count, "wall");
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
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Neighbrsnook event");
            shareIntent.putExtra(Intent.EXTRA_TEXT, "Checkout this event on Neighbrsnook,\n" + "\n" + mList.get(pos).getEvent_name() +
                    "\n" + "Start date : " + mList.get(pos).getEvent_start_date() + "\n" + "End date : " + mList.get(pos).getEvent_end_date() +
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
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Neighbrsnook event");
            shareIntent.putExtra(Intent.EXTRA_TEXT, "Checkout this event on Neighbrsnook,\n" + "\n" + mList.get(pos).getEvent_name() +
                    "\n" + "Start date : " + mList.get(pos).getEvent_start_date() + "\n" + "End date : " + mList.get(pos).getEvent_end_date() +
                    "\n" + sm.getString("neighbrhood_name") +
                    "\n\nhttps://neighbrsnook.com/open-app" +
                    "\n\nhttps://neighbrsnook.com/");

            context.startActivity(Intent.createChooser(shareIntent, "Share via"));
        } catch (Exception e) {

            Toast.makeText(context, "Error sharing image: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

}
