package com.app_neighbrsnook.utils;

import static com.app_neighbrsnook.utils.PrefMananger.GetLoginData;

import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app_neighbrsnook.R;
import com.app_neighbrsnook.network.APIClient;
import com.app_neighbrsnook.network.APIInterface;
import com.app_neighbrsnook.pojo.ReviewPojo;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VerifiedPopup {

    public static void showSuccessDialog(Context context, String message, int userId) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.dialog_verified, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(dialogView);

        AlertDialog dialog = builder.create();
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        }
        dialog.setOnShowListener(dialogInterface -> {
            if (dialog.getWindow() != null) {
                WindowManager.LayoutParams layoutParams = dialog.getWindow().getAttributes();
                layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
                dialog.getWindow().setAttributes(layoutParams);
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                ViewGroup.MarginLayoutParams marginParams = (ViewGroup.MarginLayoutParams) dialogView.getLayoutParams();
                marginParams.setMargins(140, 0, 140, 0);
                dialogView.setLayoutParams(marginParams);
            }
        });
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);

        TextView successMessage = dialogView.findViewById(R.id.successMessage);
       // successMessage.setText(message);
        LinearLayout dialog_verified = dialogView.findViewById(R.id.dialog_verified);
        dialog_verified.setVisibility(View.VISIBLE);
        Button okButton = dialogView.findViewById(R.id.okButton);
        okButton.setOnClickListener(v -> {
                    HashMap<String, Object> hm = new HashMap<>();
                    hm.put("userid", userId);

                    APIInterface service = APIClient.getRetrofit().create(APIInterface.class);
                    Call<ReviewPojo> call = service.getPopupVerified("popupVerifiedStatus", hm);
                    call.enqueue(new Callback<ReviewPojo>() {
                        @Override
                        public void onResponse(Call<ReviewPojo> call, Response<ReviewPojo> response) {
//                            Toast.makeText(context, "Popup Status Changed Successfully", Toast.LENGTH_SHORT).show();
                            Log.d("res",response.body().getMessage());
                        }

                        @Override
                        public void onFailure(Call<ReviewPojo> call, Throwable t) {
                            Toast.makeText(context, "Device Id Api Error", Toast.LENGTH_SHORT).show();
                            Log.d("res", t.getMessage());
                        }
                    });

            PrefMananger.setVerified(context, "1");
            String updatedVerifiedValue = GetLoginData(context).getVerified();
            Log.d("UpdatedVerified", "Verified value updated to: " + updatedVerifiedValue);

            dialog.dismiss();
        });

        dialog.show();
    }
}
