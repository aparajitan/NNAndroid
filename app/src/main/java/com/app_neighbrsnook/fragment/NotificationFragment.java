package com.app_neighbrsnook.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app_neighbrsnook.CommentActivity;
import com.app_neighbrsnook.R;
import com.app_neighbrsnook.adapter.NotificationAdapter;
import com.app_neighbrsnook.businessModule.BusinessDetailActivity;
import com.app_neighbrsnook.event.UserViewEvent;
import com.app_neighbrsnook.group.GroupActivity;
import com.app_neighbrsnook.group.GroupChat2Activity;
import com.app_neighbrsnook.group.GroupDetailActivity;
import com.app_neighbrsnook.model.notification.Nbdatum;
import com.app_neighbrsnook.model.notification.NotificationModel;
import com.app_neighbrsnook.network.APIClient;
import com.app_neighbrsnook.network.APIInterface;
import com.app_neighbrsnook.pollModule.PollDetailActivity;
import com.app_neighbrsnook.SalesModuleMarketPlace.SellDetailActivity;
import com.app_neighbrsnook.SalesModuleMarketPlace.SellerChatActivity;
import com.app_neighbrsnook.utils.GlobalMethods;
import com.app_neighbrsnook.utils.SharedPrefsManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationFragment extends Fragment implements NotificationAdapter.NotificationOnClick {
    RecyclerView notification_rv;
    NotificationAdapter notificationAdapter;
    List<NotificationModel> list = new ArrayList<>();
    TextView member_number_tv, title;
    ImageView searchImageView, addImageView, img_back;
    SharedPrefsManager sm;
    NotificationModel notificationModel;
    OnNotificationCountListener mListener;
    private SwipeRefreshLayout swipeRefreshLayout;
    private List<Nbdatum> al = new ArrayList<>(); // Store notification data

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notification, container, false);

        sm = new SharedPrefsManager(getActivity());

        notification_rv = view.findViewById(R.id.notification_rv);
        img_back = view.findViewById(R.id.back_btn);
        title = view.findViewById(R.id.title);
        member_number_tv = view.findViewById(R.id.member_number_tv);
        searchImageView = view.findViewById(R.id.search_imageview);
        addImageView = view.findViewById(R.id.add_imageview);
        swipeRefreshLayout = view.findViewById(R.id.swipe_layout);

        // Initialize RecyclerView
        notification_rv.setLayoutManager(new LinearLayoutManager(getActivity()));

        // Set up adapter with empty data initially
        notificationAdapter = new NotificationAdapter(new ArrayList<>(), NotificationFragment.this);
        notification_rv.setAdapter(notificationAdapter);

        // Set up swipe refresh listener
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                notificationApi(); // Refresh data
            }
        });

        title.setText("Notification");
        img_back.setVisibility(View.GONE);
        searchImageView.setVisibility(View.GONE);
        addImageView.setVisibility(View.GONE);

        notificationApi();

        return view;
    }

    private void notificationApi() {
        Boolean isInternetConnection = GlobalMethods.checkConnection(getActivity());
        if (isInternetConnection) {
            HashMap<String, Object> hm = new HashMap<>();
            hm.put("userid", Integer.parseInt(sm.getString("user_id")));
            APIInterface service = APIClient.getRetrofit().create(APIInterface.class);
            Call<NotificationModel> call = service.notificationListApi("notification", "abc1239", hm);
            call.enqueue(new Callback<NotificationModel>() {
                @Override
                public void onResponse(Call<NotificationModel> call, Response<NotificationModel> response) {
                    // Hide refresh indicator when response is received
                    if (swipeRefreshLayout.isRefreshing()) {
                        swipeRefreshLayout.setRefreshing(false);
                    }

                    try {
                        notificationModel = response.body();
                        if (notificationModel != null && notificationModel.getNbdata() != null) {
                            al = notificationModel.getNbdata();
                            sendNotificationCount(al.size());
                            if (mListener != null) {
                                mListener.onNotificationCount(al.size());
                            }

                            // Update adapter with new data
                            notificationAdapter.updateData(al);
                            notificationAdapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(getActivity(), "No notifications found", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        Toast.makeText(getActivity(), "Error loading notifications", Toast.LENGTH_SHORT).show();
                        Log.e("NotificationFragment", "Error: " + e.getMessage());
                    }
                }

                @Override
                public void onFailure(Call<NotificationModel> call, Throwable t) {
                    // Hide refresh indicator on failure
                    if (swipeRefreshLayout.isRefreshing()) {
                        swipeRefreshLayout.setRefreshing(false);
                    }

                    Toast.makeText(getActivity(), "Failed to load: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.d("res", t.getMessage());
                }
            });

        } else {
            // Hide refresh indicator if no internet
            if (swipeRefreshLayout.isRefreshing()) {
                swipeRefreshLayout.setRefreshing(false);
            }

            GlobalMethods.getInstance(getActivity()).globalDialog(getActivity(), "     No internet connection.");
        }
    }

    @Override
    public void onClickBusiness(int id, int notification_id) {
        hideNotification(notification_id);
        Log.d("businessId-", id + "");
        Intent i = new Intent(getActivity(), BusinessDetailActivity.class);
        i.putExtra("id", id);
        Log.d("businessId--", id + "");
        i.putExtra("type", "detail");
        startActivity(i);
    }

    public void onClickGroupChat(int pos, List<Nbdatum> nearbyList) {
        hideNotification(Integer.parseInt(nearbyList.get(pos).getNotificationId()));
        Intent i = new Intent(getActivity(), GroupChat2Activity.class);
        i.putExtra("groupid", nearbyList.get(pos).getId());
        i.putExtra("groupName", nearbyList.get(pos).getGroupchat_name());
        i.putExtra("pic", nearbyList.get(pos).getGroupchat_image());
        startActivity(i);
    }

    @Override
    public void onClickMarketPlaceChat(int id, int notification_id) {
        hideNotification(notification_id);
        Intent i = new Intent(getActivity(), SellDetailActivity.class);
        i.putExtra("id", id);
        Log.d("businessIdd--", id + "");
        startActivity(i);
    }

    @Override
    public void onClickGroup(int pos, List<Nbdatum> nearbyList) {
        hideNotification(Integer.parseInt(nearbyList.get(pos).getNotificationId()));
        if (nearbyList.get(pos).getIs_delet().equals("0")) {
            if (nearbyList.get(pos).getGrouptype().equals("Private")) {
                if (nearbyList.get(pos).getUserid().equals(sm.getString("user_id"))) {
                    Intent detailScreen = new Intent(getActivity(), GroupDetailActivity.class);
                    detailScreen.putExtra("id", Integer.parseInt(nearbyList.get(pos).getId()));
                    detailScreen.putExtra("data", nearbyList.get(pos).getGrouptype());
                    detailScreen.putExtra("type", "detail");
                    startActivity(detailScreen);
                } else if (nearbyList.get(pos).getGetjoin().equals("pending")) {
                    Intent groupIntent = new Intent(getActivity(), GroupActivity.class);
                    groupIntent.putExtra("neighbrhood", "");
                    startActivity(groupIntent);

                } else if (nearbyList.get(pos).getGetjoin().equals("join")) {
                    Intent groupIntent = new Intent(getActivity(), GroupActivity.class);
                    groupIntent.putExtra("neighbrhood", "");
                    startActivity(groupIntent);
                } else if (nearbyList.get(pos).getGetjoin().equals("joined")) {
                    Intent detailScreen = new Intent(getActivity(), GroupDetailActivity.class);
                    detailScreen.putExtra("id", Integer.parseInt(nearbyList.get(pos).getId()));
                    detailScreen.putExtra("data", nearbyList.get(pos).getGrouptype());
                    detailScreen.putExtra("type", "detail");
                    startActivity(detailScreen);
                } else if ((nearbyList.get(pos).getGetjoin().equals("owner"))) {
                    Intent detailScreen = new Intent(getActivity(), GroupDetailActivity.class);
                    detailScreen.putExtra("id", Integer.parseInt(nearbyList.get(pos).getId()));
                    detailScreen.putExtra("data", nearbyList.get(pos).getGrouptype());
                    detailScreen.putExtra("type", "detail");
                    startActivity(detailScreen);
                }
            } else if (nearbyList.get(pos).getGetjoin().equals("pending")) {
                Intent groupIntent = new Intent(getActivity(), GroupActivity.class);
                groupIntent.putExtra("neighbrhood", "");
                startActivity(groupIntent);
            } else {
                Intent detailScreen = new Intent(getActivity(), GroupDetailActivity.class);
                detailScreen.putExtra("id", Integer.parseInt(nearbyList.get(pos).getId()));
                detailScreen.putExtra("data", nearbyList.get(pos).getGrouptype());
                detailScreen.putExtra("type", "detail");
                startActivity(detailScreen);
            }
        }
    }

    @Override
    public void onClickPoll(int id, int notification_id) {
        hideNotification(notification_id);
        Intent pollDetailIntent = new Intent(getActivity(), PollDetailActivity.class);
        pollDetailIntent.putExtra("id", id);
        startActivity(pollDetailIntent);
    }

    @Override
    public void onClickPost(int id, String title, int notification_id) {
        hideNotification(notification_id);
        WallFragment fragmentB = new WallFragment(id);
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragmentB);
        transaction.addToBackStack(null); // Optional: Add to back stack
        transaction.commit();
    }

    @Override
    public void onClickEvent(int id, int notification_id) {
        hideNotification(notification_id);
        Intent detailScreen = new Intent(getActivity(), UserViewEvent.class);
        detailScreen.putExtra("data", id);
        startActivity(detailScreen);
    }

    @Override
    public void onClickWelcom(int id, int notificationId) {
        hideNotification(notificationId);
        WallFragment fragmentB = new WallFragment(0);
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragmentB);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onClickDm(int id, int notificationId, String userName, String userImg) {
        hideNotification(notificationId);
        Intent i = new Intent(getActivity(), SellerChatActivity.class);
        i.putExtra("eventId", id);
        i.putExtra("userName", userName);
        i.putExtra("subject", "");
        i.putExtra("pic", userImg);
        startActivity(i);
    }

    private void hideNotification(int id) {
        HashMap<String, Object> hm = new HashMap<>();
        hm.put("userid", Integer.parseInt(sm.getString("user_id")));
        hm.put("not_Id", id);
        APIInterface service = APIClient.getRetrofit().create(APIInterface.class);
        Call<NotificationModel> call = service.hideNotificationApi("hidenotification", "abc1239", hm);
        call.enqueue(new Callback<NotificationModel>() {
            @Override
            public void onResponse(Call<NotificationModel> call, Response<NotificationModel> response) {
                if ((response.body().getStatus().equals("success")) && (response.body().getMessage().equals("success updated"))) {
                    notificationApi();
                }
            }

            @Override
            public void onFailure(Call<NotificationModel> call, Throwable t) {
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("res", t.getMessage());
            }
        });
    }

    @Override
    public void onClickPostComment(String postId, String title, int notification_id) {
        hideNotification(notification_id);
        Intent i = new Intent(getActivity(), CommentActivity.class);
        i.putExtra("pos", 0);
        i.putExtra("source", "Notification");
        i.putExtra("postid", postId);
        startActivity(i);
    }

    public interface OnNotificationCountListener {
        void onNotificationCount(int notificationCount);
    }

    private void sendNotificationCount(int count) {
        if (mListener != null) {
            mListener.onNotificationCount(count);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof OnNotificationCountListener) {
            mListener = (OnNotificationCountListener) context;
        } else {
            throw new ClassCastException(context.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}