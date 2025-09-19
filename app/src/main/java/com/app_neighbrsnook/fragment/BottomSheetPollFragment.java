package com.app_neighbrsnook.fragment;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.app_neighbrsnook.MainActivity;
import com.app_neighbrsnook.intreface.OnUpdateFavListener;
import com.app_neighbrsnook.intreface.OnUpdateFavListenerPoll;
import com.app_neighbrsnook.network.APIClient;
import com.app_neighbrsnook.network.APIInterface;
import com.app_neighbrsnook.pojo.PollListPojo;
import com.app_neighbrsnook.pojo.marketPlacePojo.CommonPojoSuccess;
import com.app_neighbrsnook.utils.GlobalMethods;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.app_neighbrsnook.R;
import com.app_neighbrsnook.ReportActivity;
import com.app_neighbrsnook.model.wall.Listdatum;
import com.app_neighbrsnook.utils.SharedPrefsManager;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BottomSheetPollFragment extends BottomSheetDialogFragment {

    RelativeLayout share_rl, fav_rl, report_rl, block_rl;
    String userName, title, question;
    SharedPrefsManager sm;
    TextView fav_tv, fav_subtitle_tv;
    List<Listdatum> mList;
    List<PollListPojo> mList1;
    int pos;
    OnUpdateFavListener mListener;
    String source = "abc";
    OnUpdateFavListenerPoll mListenerBusiness;
    int favouritstatus;
    String username;
    String memberUserId;
    String neighborhood;
    String pollQuestion;
    String pollid;
    Context context;

    public BottomSheetPollFragment(List<Listdatum> mList, int pos, String source, String s) {
        this.mList = mList;
        this.pos = pos;
        this.source = source;
    }

    public BottomSheetPollFragment(List<PollListPojo> listdata, int pos, String source) {
        this.mList1 = listdata;
        this.pos = pos;
        this.source = source;
    }

    public BottomSheetPollFragment(List<Listdatum> mList, int pos) {
        this.mList = mList;
        this.pos = pos;
    }

    public BottomSheetPollFragment(Context context, int favouritstatus, String username, String neighborhood, String pollQuestion, String pollid, String memberUserId, String source) {
        this.context = context;
        this.favouritstatus = favouritstatus;
        this.username = username;
        this.neighborhood = neighborhood;
        this.pollQuestion = pollQuestion;
        this.pollid = pollid;
        this.memberUserId = memberUserId;
        this.source = source;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sm = new SharedPrefsManager(getActivity());
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void setupDialog(Dialog dialog, int style) {
        View contentView = View.inflate(getContext(), R.layout.fragment_bottom_sheet_poll, null);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        report_rl = contentView.findViewById(R.id.report_rl);
        fav_rl = contentView.findViewById(R.id.fav_rl);
        share_rl = contentView.findViewById(R.id.share_rl);
        fav_tv = contentView.findViewById(R.id.fav_tv);
        fav_subtitle_tv = contentView.findViewById(R.id.fav_subtitle_tv);
        block_rl = contentView.findViewById(R.id.block_rl);

        if (favouritstatus == 0) {
            fav_tv.setText("Favourite Poll");
            fav_subtitle_tv.setText("Add this to my favourites.");
        } else {
            fav_tv.setText("Unfavourite Poll");
            fav_subtitle_tv.setText("Remove this from my favourites.");
        }

        if (memberUserId.equals(sm.getString("user_id"))) {
            block_rl.setVisibility(View.GONE);
        }

        block_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(context)
                        .setTitle("Confirm Block")
                        .setMessage("Are you sure you want to block " + username + "?")
                        .setCancelable(false)
                        .setPositiveButton("OK", (okDialog, which) -> {
                            HashMap<String, Object> hm = new HashMap<>();
                            hm.put("blocker_userid", Integer.parseInt(sm.getString("user_id")));
                            hm.put("blocked_userid", memberUserId);
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
                                            Intent i = new Intent(BottomSheetPollFragment.this.context, MainActivity.class);
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

        report_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), ReportActivity.class);
                i.putExtra("title", userName);
                i.putExtra("subject", title);
                startActivity(i);
                dialog.dismiss();
            }
        });

        share_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_SUBJECT, "Neighbrsnook poll");

                sendIntent.putExtra(Intent.EXTRA_TEXT, "Checkout this poll on Neighbrsnook,\n" +
                        "\n" + "Poll" +
                        "\n" + pollQuestion +
                        "\n" + neighborhood +
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
                hm.put("postid", Integer.parseInt(pollid));
                hm.put("type", "Poll");

                if (favouritstatus == 0) {
                    hm.put("neighbrhood", neighborhood);
                    GlobalMethods.favApi("userfavouritespost", hm, dialog, getActivity());

                    if (source.equals("poll")) {
                        sendWallStatus("poll", "poll");
                    } else if (source.equals("wall")) {
                        sendWallStatus("poll", source);
                    } else if (source.equals("fav")) {
                        sendWallStatus("poll", source);
                    }
                } else {
                    GlobalMethods.favApi("userunfavouritepost", hm, dialog, getActivity());
                    if (source.equals("poll")) {
                        sendWallStatus("business", "poll");
                    } else if (source.equals("wall")) {
                        sendWallStatus("business", source);
                    } else if (source.equals("fav")) {
                        sendWallStatus("business", source);
                    }
                }


                dialog.dismiss();

            }
        });

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

        if (source.equals("poll")) {
            if (context instanceof OnUpdateFavListenerPoll) {
                mListenerBusiness = (OnUpdateFavListenerPoll) context;
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
        if (source.equals("poll")) {
            mListenerBusiness = null;
        } else if (source.equals("wall")) {
            mListener = null;
        } else if (source.equals("fav")) {
            mListener = null;
        }
    }

    private void sendWallStatus(String count, String type) {
        if (type.equals("poll")) {
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

}