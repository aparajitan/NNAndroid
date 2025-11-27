package com.app_neighbrsnook.utils;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AppCommon {
    public static AppCommon mInstance = null;
    static Context mContext;


    public static AppCommon getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new AppCommon();
        }
        mContext = context;
        return mInstance;
    }

    public void onHideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
//Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
//If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }


    public boolean isEmailValid(String email) {
        boolean isValid = false;

        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }

    public void setNonTouchableFlags(Activity activity) {
        if (activity != null) {
            activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }
    }

    public void clearNonTouchableFlags(Activity mActivity) {
        if (mActivity != null) {
            mActivity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }
    }

    public boolean isConnectingToInternet(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
        }
        return false;
    }



//    public String getPhonenumber() {
//        SharedPreferences mSharedPreferences = mContext.getSharedPreferences(MyPreference.mPreferences, MODE_PRIVATE);
//        return mSharedPreferences.getString(MyPreference.cardPosition, "");
//    }

    public void showTimePicker(Activity activity, final TextView timeTextView) {
        final Calendar c = Calendar.getInstance();
        int mHour = c.get(Calendar.HOUR);
        int mMinute = c.get(Calendar.MINUTE);
        final TimePickerDialog timePickerDialog = new TimePickerDialog(activity,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {
                        String AM_PM;
                        AM_PM = (hourOfDay < 12) ? "AM" : "PM";
                        int hour = (hourOfDay < 12) ? hourOfDay : hourOfDay - 12;
                        timeTextView.setText(setTimeEditText(hour, minute, AM_PM));
                    }
                }, mHour, mMinute, false);
        timePickerDialog.show();
    }

    private String setTimeEditText(int selectedHour, int selectedMinute, String AM_PM) {
        if (selectedHour == 00) {
            selectedHour = 12;
        } else
            selectedHour = (selectedHour <= 12) ? selectedHour : selectedHour - 12;
        if (selectedHour < 10) {
            if (selectedMinute < 10) {

                return ("0" + selectedHour + ":" + "0" + selectedMinute + " " + AM_PM);
            } else
                return ("0" + selectedHour + ":" + selectedMinute + " " + AM_PM);
        } else {
            if (selectedMinute < 10) {
                return (selectedHour + ":" + "0" + selectedMinute + " " + AM_PM);
            } else
                return (selectedHour + ":" + selectedMinute + " " + AM_PM);
        }
    }

    public int typeOfuser(String emailId) {
        int flag = 0;
        if (emailId.equals("manager@test.com"))
            flag = 1;
        else if (emailId.equals("employee@test.com"))
            flag = 2;
        return flag;
    }

//    public void clearPreference() {
//        SharedPreferences.Editor editorLogin = mContext.getSharedPreferences(MyPreference.mUserLogin, MODE_PRIVATE).edit();
//        editorLogin.clear();
//        editorLogin.apply();
//        SharedPreferences.Editor login = mContext.getSharedPreferences(MyPreference.login, MODE_PRIVATE).edit();
//        login.clear();
//        login.apply();
//        SharedPreferences.Editor editor = mContext.getSharedPreferences(MyPreference.mDeviceToken, MODE_PRIVATE).edit();
//        editor.clear();
//        editor.apply();
//        SharedPreferences.Editor editorPreferences = mContext.getSharedPreferences(MyPreference.mPreferences, MODE_PRIVATE).edit();
//        editorPreferences.clear();
//        editorPreferences.apply();
//
//    }

//    public void clearType() {
//        SharedPreferences.Editor editorLogin = mContext.getSharedPreferences(MyPreference.type, MODE_PRIVATE).edit();
//        editorLogin.clear();
//        editorLogin.apply();
//
//
//    }
      /*  public void clearPreference() {
            SharedPreferences.Editor editorLogin = mContext.getSharedPreferences(MyPreference.mUserLogin, MODE_PRIVATE).edit();
            editorLogin.clear();
            editorLogin.apply();
            SharedPreferences.Editor editor = mContext.getSharedPreferences(MyPreference.mDeviceToken, MODE_PRIVATE).edit();
            editor.clear();
            editor.apply();
            SharedPreferences.Editor editorPreferences = mContext.getSharedPreferences(MyPreference.mPreferences, MODE_PRIVATE).edit();
            editorPreferences.clear();
            editorPreferences.apply();

        }*/

    public void finishingDailog(final Activity context, String msg) {

        if (!context.isFinishing()) {
            int b;
            final AlertDialog.Builder mABuilder = new AlertDialog.Builder(context);
            mABuilder.setTitle(msg);
            mABuilder.setCancelable(false);
            mABuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                    context.onBackPressed();
                }
            });
            mABuilder.show();
        }
    }

    public boolean validation(String text) {
        boolean flag = text.isEmpty();
        return flag;

    }





}