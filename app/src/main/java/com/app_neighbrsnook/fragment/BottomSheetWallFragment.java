package com.app_neighbrsnook.fragment;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.app_neighbrsnook.MainActivity;
import com.app_neighbrsnook.intreface.OnUpdateFavListener;
import com.app_neighbrsnook.model.wall.WallPojo;
import com.app_neighbrsnook.network.APIClient;
import com.app_neighbrsnook.network.APIInterface;
import com.app_neighbrsnook.pojo.EmojiPojo;
import com.app_neighbrsnook.pojo.marketPlacePojo.CommonPojoSuccess;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.app_neighbrsnook.R;
import com.app_neighbrsnook.ReportActivity;
import com.app_neighbrsnook.model.wall.Listdatum;
import com.app_neighbrsnook.pojo.ImagePojo;
import com.app_neighbrsnook.SalesModuleMarketPlace.SellerChatActivity;
import com.app_neighbrsnook.utils.SharedPrefsManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BottomSheetWallFragment extends BottomSheetDialogFragment {
    RelativeLayout chat_rl, fav_rl, report_rl, share_rl,delete_rl, block_rl;
    int id, pos;
    Dialog dialog;
    SharedPrefsManager sm;
    TextView fav_tv, fav_subtitle_tv;
    View view;
    List<Listdatum> mList = new ArrayList<>();
    List<ImagePojo> postImages = new ArrayList();
    ArrayList<String> postImg = new ArrayList<>();
    OnUpdateFavListener mListener;
    Context context;

    public BottomSheetWallFragment(Context context, List<Listdatum> mList, int pos) {
        this.context = context;
        this.mList = mList;
        this.pos = pos;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sm = new SharedPrefsManager(getActivity());
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @SuppressLint("RestrictedApi")
    @Override
    public void setupDialog(Dialog dialog, int style) {
        View contentView = View.inflate(getContext(), R.layout.fragment_bottom_sheet_wall, null);

        fav_rl = contentView.findViewById(R.id.fav_rl);
        chat_rl = contentView.findViewById(R.id.chat_rl);
        fav_tv = contentView.findViewById(R.id.fav_tv);
        report_rl = contentView.findViewById(R.id.report_rl);
        view = contentView.findViewById(R.id.chat_view);
        delete_rl = contentView.findViewById(R.id.delete_rl);
        share_rl = contentView.findViewById(R.id.share_rl);
        fav_subtitle_tv = contentView.findViewById(R.id.fav_subtitle_tv);
        block_rl = contentView.findViewById(R.id.block_rl);

        id = Integer.parseInt(sm.getString("user_id"));

        if (mList.get(pos).getPostImages() != null && !mList.get(pos).getPostImages().isEmpty()) {
            postImages.clear(); // Clear the list before adding new data
            postImages.addAll(mList.get(pos).getPostImages());
        }

        if ((mList.get(pos).getPostImages() != null && mList.get(pos).getPostImages().size() != 0)) {
            for (int i = 0; i < mList.get(pos).getPostImages().size(); i++) {
                postImg.add(mList.get(pos).getPostImages().get(i).getImg());
            }
        }

        if (id == Integer.parseInt(mList.get(pos).getCreatedby())) {
            chat_rl.setVisibility(View.GONE);
            view.setVisibility(View.GONE);
            report_rl.setVisibility(View.GONE);
            delete_rl.setVisibility(View.VISIBLE);
            block_rl.setVisibility(View.GONE);
        }

        if (mList.get(pos).getFavouritstatus() == 0) {
            fav_tv.setText("Favourite Post");
            fav_subtitle_tv.setText("Add this to my favourites.");
        } else {
            fav_tv.setText("Unfavourite Post");
            fav_subtitle_tv.setText("Remove this from my favourites.");
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
                                            Intent i = new Intent(BottomSheetWallFragment.this.context, MainActivity.class);
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
                if (id == Integer.parseInt(mList.get(pos).getCreatedby())) {
//                    chat_rl.setVisibility(View.GONE);
//                    msgDialog("You can not send massage to yourself.");
                } else {

                    Intent i = new Intent(getActivity(), SellerChatActivity.class);
                    i.putExtra("eventId", Integer.parseInt(mList.get(pos).getCreatedby()));
                    i.putExtra("userName", mList.get(pos).getUsername());
                    i.putExtra("subject", mList.get(pos).getPost_type());
                    i.putExtra("pic", mList.get(pos).getUserpic());
                    startActivity(i);
                    dialog.dismiss();
                }

            }
        });

        report_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), ReportActivity.class);
                i.putStringArrayListExtra("data", postImg);
                i.putParcelableArrayListExtra("postImages", new ArrayList<>(postImages));
//                i.putExtra("postImages", new ArrayList<>(postImages));
                i.putExtra("userName", mList.get(pos).getUsername());
                i.putExtra("profilePhoto", mList.get(pos).getUserpic());
                i.putExtra("neighborhood", mList.get(pos).getNeighborhood());
                i.putExtra("post_type", mList.get(pos).getPost_type());
                i.putExtra("post_id", mList.get(pos).getPostid());
                i.putExtra("subject", mList.get(pos).getPost_message());
                startActivity(i);
                dialog.dismiss();
            }
        });

        share_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_SUBJECT, "Neighbrsnook post");
                sendIntent.putExtra(Intent.EXTRA_TEXT, "Checkout this post on Neighbrsnook,\n" +
                        "\n" + mList.get(pos).getPost_type() +
                        "\n" + mList.get(pos).getPost_message() +
                        "\n" + mList.get(pos).getNeighborhood() +
                        "\n\nhttps://neighbrsnook.com/open-app" +
                        "\n\nhttps://neighbrsnook.com/");
                sendIntent.setType("text/plain");
                Intent shareIntent = Intent.createChooser(sendIntent, null);
                startActivity(shareIntent);
            }
        });

        fav_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HashMap<String, Object> hm = new HashMap<>();
                hm.put("userid", Integer.parseInt(sm.getString("user_id")));
                hm.put("postid", Integer.parseInt(mList.get(pos).getPostid()));
                hm.put("type", "Post");

                if (mList.get(pos).getFavouritstatus() == 0) {
                    hm.put("neighbrhood", mList.get(pos).getNeighborhood());
                    favApi("userfavouritespost", hm, dialog, getActivity());
                } else {
                    favApi("userunfavouritepost", hm, dialog, getActivity());
                }
            }
        });

        delete_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmDeleteDialog(Integer.parseInt(mList.get(pos).getPostid()));
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

    public void favApi(String type, HashMap<String, Object> hm, Dialog dialog, Context context) {
        APIInterface service = APIClient.getRetrofit().create(APIInterface.class);
        Call<WallPojo> call = service.favApi(type, hm);
        call.enqueue(new Callback<WallPojo>() {
            @Override
            public void onResponse(Call<WallPojo> call, Response<WallPojo> response) {
                if (response.body().getStatus().equals("success")) {
                    sendWallStatus(response.body().getMessage());
                    if (response.body().getMessage().equals("Remove From Favourites Successfully")) {
                        fav_tv.setText("Favourite Post");
                    } else {
                        fav_tv.setText("Unfavourite Post");
                    }
//                    onListItemClick("afafa");
                    Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            }
            @Override
            public void onFailure(Call<WallPojo> call, Throwable t) {

            }
        });
    }

    private void sendWallStatus(String count) {
        if (mListener != null) {
            mListener.onListItemClicked(count, "wall");
        }
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

    public void confirmDeleteDialog(int post) {
        Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.post_delete_pop_up);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(getActivity(), android.R.color.transparent)));
        dialog.getWindow().setAttributes(lp);
        dialog.show();
        TextView tv_no = dialog.findViewById(R.id.no_id);
        TextView tv_yes = dialog.findViewById(R.id.yes_id);
        ImageView img_cancel = dialog.findViewById(R.id.iv9);
        tv_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        tv_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                HashMap<String, Object> hm = new HashMap<>();
                hm.put("userid", Integer.parseInt(sm.getString("user_id")));
                hm.put("postid", post);
                deletePostApi(hm);
            }
        });
    }

    private void deletePostApi(HashMap<String, Object> hm) {
        APIInterface service = APIClient.getRetrofit().create(APIInterface.class);
        Call<EmojiPojo> call = service.deletePostApi("deletepostbyuser", hm);
        call.enqueue(new Callback<EmojiPojo>() {
            @Override
            public void onResponse(Call<EmojiPojo> call, Response<EmojiPojo> response) {
                if (response.body().getStatus().equals("success")) {
                    Intent i = new Intent(getActivity(), MainActivity.class);
                    startActivity(i);
                    getActivity().finish();
                    Log.d("response emoji---", response.body().toString());
                }
            }

            @Override
            public void onFailure(Call<EmojiPojo> call, Throwable t) {
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("res---", t.getMessage());
            }
        });
    }
}