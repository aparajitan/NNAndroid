package com.app_neighbrsnook.fragment;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app_neighbrsnook.pojo.marketPlacePojo.CommonPojoSuccess;
import com.google.firebase.firestore.FirebaseFirestore;
import com.app_neighbrsnook.R;
import com.app_neighbrsnook.adapter.NeighbourhoodMemberAdapter;
import com.app_neighbrsnook.chat.User;
import com.app_neighbrsnook.network.APIClient;
import com.app_neighbrsnook.network.APIInterface;
import com.app_neighbrsnook.pojo.neighborhoodMember.Listdatum;
import com.app_neighbrsnook.pojo.neighborhoodMember.MemberPojo;
import com.app_neighbrsnook.SalesModuleMarketPlace.SellerChatActivity;
import com.app_neighbrsnook.utils.SharedPrefsManager;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatUserListFragment extends Fragment implements NeighbourhoodMemberAdapter.UserCallBack {
    ImageView back_btn, cancel_iv;
    TextView titleTv;
    RecyclerView userList_rv;
    NeighbourhoodMemberAdapter neighbourhoodMemberAdapter;
    FirebaseFirestore db;
    NeighbourhoodMemberAdapter.UserCallBack ucb= this;
    SharedPrefsManager sm;
    List<Listdatum> list1 = new ArrayList<>();
    EditText search_et;
    RelativeLayout search_rl;
    ImageView searchImageView, addImageView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat_user_list, container, false);
        db = FirebaseFirestore.getInstance();
        sm = new SharedPrefsManager(getActivity());
        titleTv = view.findViewById(R.id.title);
        back_btn = view.findViewById(R.id.back_btn);
        searchImageView = view.findViewById(R.id.search_imageview);
        addImageView = view.findViewById(R.id.add_imageview);
        searchImageView.setVisibility(View.VISIBLE);
        addImageView.setVisibility(View.GONE);
        search_et = view.findViewById(R.id.search_et);
        search_rl = view.findViewById(R.id.search_rl);
        cancel_iv = view.findViewById(R.id.cancel_iv);

        titleTv.setText("Members");
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });
        userList_rv = view.findViewById(R.id.user_list_rv);
        userList_rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        getAllUsers();
        searchImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search_rl.setVisibility(View.VISIBLE);
                search_et.requestFocus();
            }
        });
        cancel_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search_rl.setVisibility(View.GONE);
                search_et.setText("");
                search_et.setHint("Search...");
                hideKeyboard();
            }
        });
        search_et.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    hideKeyboard();
                    return true;
                }
                return false;
            }
        });
        EditText finalSearch_et = search_et;
        search_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                finalSearch_et.setFocusable(true);
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                //after the change calling the method and passing the search input
                filter(editable.toString());

            }
        });
        return view;
    }
    private void hideKeyboard() {
        InputMethodManager mgr = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        mgr.hideSoftInputFromWindow(search_et.getWindowToken(), 0);
    }

    @Override
    public void onUserClick(int id, String name, String userPic, int isBlocked) {
        if (isBlocked == 0) {
            Intent i = new Intent(getActivity(), SellerChatActivity.class);
            i.putExtra("eventId", id);
            i.putExtra("userName", name);
            i.putExtra("pic", userPic);
            i.putExtra("subject", "");
            startActivity(i);
        } else {
            Toast.makeText(getActivity(), "User is blocked. Cannot chat.", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void onUserBlock(String memberUserId, String flag) {
        // Input validation
        if (memberUserId == null || memberUserId.trim().isEmpty()) {
            Log.w(TAG, "Invalid memberUserId provided");
            showErrorToast("Invalid user ID");
            return;
        }
        if (flag == null || flag.trim().isEmpty()) {
            Log.w(TAG, "Invalid flag provided");
            showErrorToast("Invalid action flag");
            return;
        }
        // Get user ID with error handling
        String userIdStr = sm.getString("user_id");
        if (userIdStr == null || userIdStr.trim().isEmpty()) {
            Log.e(TAG, "User ID not found in shared preferences");
            showErrorToast("Authentication error");
            return;
        }
        int userId;
        try {
            userId = Integer.parseInt(userIdStr);
        } catch (NumberFormatException e) {
            Log.e(TAG, "Invalid user ID format: " + userIdStr, e);
            showErrorToast("Invalid user ID format");
            return;
        }
        // Build request payload
        Map<String, Object> requestPayload = new HashMap<>();
        requestPayload.put("blocker_userid", userId);
        requestPayload.put("blocked_userid", memberUserId);
        requestPayload.put("action", flag);
        // Make API call
        APIInterface service = APIClient.getRetrofit3().create(APIInterface.class);
        Call<CommonPojoSuccess> call = service.userBlockApi1(requestPayload);
        call.enqueue(new Callback<CommonPojoSuccess>() {
            @Override
            public void onResponse(Call<CommonPojoSuccess> call, Response<CommonPojoSuccess> response) {
                if (!response.isSuccessful()) {
                    handleApiError(response);
                    return;
                }

                CommonPojoSuccess responseBody = response.body();
                if (responseBody == null) {
                    Log.e(TAG, "Response body is null");
                    showErrorToast("Invalid server response");
                    return;
                }

                if ("success".equals(responseBody.getStatus())) {
                    handleBlockSuccess(memberUserId, flag);
                } else {
                    handleBlockFailure(responseBody);
                }
            }

            @Override
            public void onFailure(Call<CommonPojoSuccess> call, Throwable t) {
                Log.e(TAG, "Network error during user block operation", t);
                showErrorToast("Network error. Please check your connection.");
            }
        });
    }

    // Helper methods for better code organization
    private void handleBlockSuccess(String memberUserId, String flag) {
        Log.i(TAG, "User block operation successful for user: " + memberUserId + ", action: " + flag);
        // Add your success logic here
        // For example: update UI, refresh data, show success message, etc.
    }

    private void handleBlockFailure(CommonPojoSuccess response) {
        String errorMessage = response.getMessage() != null ? response.getMessage() : "Unknown error";
        Log.e(TAG, "User block operation failed: " + errorMessage);
        showErrorToast("Operation failed: " + errorMessage);
    }

    private void handleApiError(Response<CommonPojoSuccess> response) {
        String errorMsg = "API Error (Code: " + response.code() + ")";
        Log.e(TAG, errorMsg);

        try {
            if (response.errorBody() != null) {
                String errorBody = response.errorBody().string();
                Log.e(TAG, "Error body: " + errorBody);
            }
        } catch (Exception e) {
            Log.e(TAG, "Error reading error body", e);
        }

        showErrorToast("Server error occurred");
    }

    private void showErrorToast(String message) {
        if (getActivity() != null && !getActivity().isFinishing()) {
            FancyToast.makeText(getActivity(), message, Toast.LENGTH_LONG, FancyToast.ERROR, false).show();
        }
    }

    public void getAllUsers() {
        HashMap<String, Object> hm =  new HashMap<>();
        hm.put("userid", Integer.parseInt(sm.getString("user_id")));
        hm.put("nbd_id",(sm.getString(("neighbrhood"))));

        APIInterface service = APIClient.getRetrofit().create(APIInterface.class);
        Call<MemberPojo> call = service.memberListApi("neighbrhoodmemberlist", hm);
        call.enqueue(new Callback<MemberPojo>() {
            @Override
            public void onResponse(Call<MemberPojo> call, Response<MemberPojo> response) {

                if(response.body().getMessage().equals("Data Found")) {
                    list1.clear();
                    list1 = response.body().getListdata();
                    neighbourhoodMemberAdapter = new NeighbourhoodMemberAdapter(list1, ucb);
                    userList_rv.setAdapter(neighbourhoodMemberAdapter);
                }
                else {
                    Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<MemberPojo> call, Throwable t) {
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("res", t.getMessage());
            }
        });


    }

    private void filter(String text) {
        List<Listdatum> filteredlist = new ArrayList<>();
        for (Listdatum item : list1) {
            if (item.getFullname() != null &&item.getFullname().toLowerCase().contains(text.toLowerCase()) ||
                    item.getNeighbrhood() != null &&item.getNeighbrhood().toLowerCase().contains(text.toLowerCase())
            )
            {
                filteredlist.add(item);
            }
        }


        if (filteredlist.isEmpty()) {
            Toast.makeText(getActivity(), "No Data Found..", Toast.LENGTH_SHORT).show();
        } else {
            neighbourhoodMemberAdapter.filterList(filteredlist);
        }
    }

}