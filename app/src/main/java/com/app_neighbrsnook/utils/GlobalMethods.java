package com.app_neighbrsnook.utils;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.provider.Settings;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Base64;
import android.util.Log;
import android.util.Patterns;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.app_neighbrsnook.R;
import com.app_neighbrsnook.model.AppLimitPojo;
import com.app_neighbrsnook.model.wall.WallPojo;
import com.app_neighbrsnook.network.APIClient;
import com.app_neighbrsnook.network.APIInterface;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class GlobalMethods {
    public static GlobalMethods mInstance = null;
    static Context mContext;
    SharedPrefsManager sm;
    public static ArrayList<Uri> mArrayDocUri = new ArrayList<Uri>();

    public static GlobalMethods getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new GlobalMethods();
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
    public void globalDialogCenter(Context context, String str) {
        Dialog dialog;
        RecyclerView rv;
        TextView msg, ok;
        ImageView cancel, iv_uploaded_image;
        CardView card;
        dialog = new Dialog(context);
        dialog.setContentView(R.layout.global_dialog_center);
//        cancel = dialog.findViewById(R.id.cross_imageview);
        ok = dialog.findViewById(R.id.ok_textview);
        msg = dialog.findViewById(R.id.msg);
        msg.setText(str);

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(context, android.R.color.transparent)));
        dialog.setCancelable(true);
        dialog.show();
}

    public static void favApi(String type, HashMap<String, Object> hm, Dialog dialog, Context context) {


        APIInterface service = APIClient.getRetrofit().create(APIInterface.class);
        Call<WallPojo> call = service.favApi(type, hm);
        call.enqueue(new Callback<WallPojo>() {
            @Override
            public void onResponse(Call<WallPojo> call, Response<WallPojo> response) {

                if (response.body().getStatus().equals("success")) {
                    dialog.dismiss();

                    Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<WallPojo> call, Throwable t) {

            }
        });

    }

    public static void calenderPicker2(Context context, TextView textView) {
        Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);
//        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        // Launch Date Picker Dialog
        DatePickerDialog dpd = new DatePickerDialog(context, R.style.MyTimePickerDialogStyle,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        newDateFrom.set(year, monthOfYear, dayOfMonth);
                        textView.setText(df.format(newDateFrom.getTime()));
                    }
                }, mYear, mMonth, mDay);
        dpd.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        dpd.getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dpd.show();
    }

    public static void calenderPicker1(Context context, TextView textView) {
        Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        // Launch Date Picker Dialog
        DatePickerDialog dpd = new DatePickerDialog(context, R.style.MyTimePickerDialogStyle,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        newDateFrom.set(year, monthOfYear, dayOfMonth);
                        textView.setText(df.format(newDateFrom.getTime()));
                    }
                }, mYear, mMonth, mDay);
        dpd.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
//        dpd.getWindow().setBackgroundDrawableResource(R.drawable.round_corner_dialog);
        dpd.getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dpd.show();
    }

    public static void calenderPickerHideBeforeDate(Context context, TextView textView, long openDate) {
        Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        // Launch Date Picker Dialog
        DatePickerDialog dpd = new DatePickerDialog(context, R.style.MyTimePickerDialogStyle,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        newDateFrom.set(year, monthOfYear, dayOfMonth);
                        textView.setText(df.format(newDateFrom.getTime()));
                    }
                }, mYear, mMonth, mDay);
        dpd.getDatePicker().setMinDate(openDate);
//        dpd.getWindow().setBackgroundDrawableResource(R.drawable.round_corner_dialog);
        dpd.getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dpd.show();
    }


    public static String timePicker(Context context, TextView tv) {
        TimePickerDialog mTimePicker;
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        String hrs, min;


        mTimePicker = new TimePickerDialog(context, R.style.MyTimePickerDialogStyle, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
//                String timeing="";
////                String am_pm = (selectedHour < 12) ? "AM" : "PM";
////                tv.setText(String.valueOf(selectedHour)+":"+String.valueOf(selectedMinute) +" "+ am_pm);
//                boolean isPM = (selectedHour >= 12);
//                tv.setText(String.format("%02d:%02d %s", (selectedHour == 12 || selectedHour == 0) ? 12 : selectedHour % 12, minute, isPM ? "PM" : "AM"));
//               timeing = tv.getText().toString();
////                Log.d("timeing", timeing);

                String AM_PM = "AM";

                if (selectedHour < 12) {
                    AM_PM = "AM";

                } else {
                    //AM_PM = "AM";

                }
                tv.setText(selectedHour + " : " + selectedMinute + " " + AM_PM);


            }
        }, hour, minute, false);
        mTimePicker.getWindow().setBackgroundDrawableResource(R.drawable.round_corner_dialog);
        mTimePicker.getWindow().setLayout(100, 200);
        mTimePicker.setTitle("Select Time");
        mTimePicker.show();
        boolean isPM = (hour >= 12);
//        return (String.valueOf(hour)+ ":" +String.valueOf(hour)+ " " +String.valueOf(isPM ? "PM" : "AM"));
//        return "";
//        openCloseTime = String.format("%02d:%02d %s", (hour == 12 || hour == 0) ? 12 : hour % 12, minute, isPM ? "PM" : "AM");
//        Log.d("openCloseTime", openCloseTime);
        return String.format("%02d:%02d %s", (hour == 12 || hour == 0) ? 12 : hour % 12, minute, isPM ? "PM" : "AM");
    }


    private String getTime(int hr, int min) {
        Time tme = new Time(hr, min, 0);//seconds by default set to zero
        Format formatter;
        formatter = new SimpleDateFormat("h:mm a");
        return formatter.format(tme);
    }


    public static void report(Context context, String str) {
        Dialog preview_dialog;
        Spinner spinner;
        ImageView cross_imageview;
        List<String> stateList = new ArrayList<>();
        preview_dialog = new Dialog(context);
        preview_dialog.setContentView(R.layout.report_layout);
        TextView textView = preview_dialog.findViewById(R.id.report_tv);
        TextView submit_tv = preview_dialog.findViewById(R.id.submit_tv);
        cross_imageview = preview_dialog.findViewById(R.id.cross_imageview);
        textView.setText(str);
        spinner = preview_dialog.findViewById(R.id.spinner1);
        stateList.add("Fake News");
        stateList.add("Abuse");
        stateList.add("Harrassment");
        stateList.add("Spam");
        stateList.add("Voilece");
        stateList.add("Other");

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter(context, android.R.layout.simple_list_item_1, stateList);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (parent.getItemAtPosition(position).equals("aa")) {
                } else {
                    String item = parent.getItemAtPosition(position).toString();
//                    Toast.makeText(parent.getContext(),"Selected: " +item, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        cross_imageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                preview_dialog.dismiss();
            }
        });

        submit_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                preview_dialog.dismiss();
            }
        });
        preview_dialog.getWindow().setBackgroundDrawableResource(R.drawable.round_corner_report);
        preview_dialog.setCancelable(true);
        preview_dialog.show();

    }

    public static void progressDialog(boolean showing, Context context) {
        ProgressDialog dialog = new ProgressDialog(context);
        dialog.setContentView(R.layout.dialog_progress);
        dialog.setCancelable(false);
//
//        ProgressDialog dialog = new ProgressDialog(this);
//        dialog.setMessage("Please wait...");
//        dialog.setCancelable(false);
//        dialog.show();

//        if (!showing)
//            dialog.dismiss();
//        else dialog.show();

        if (showing) {
            dialog.show();
        } else {
            dialog.dismiss();
        }
    }

    static Calendar newDateFrom = Calendar.getInstance();
    private static final int ACCESSIBILITY_ENABLED = 1;
    private static final String PASSWORD_PATTERN =
            "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{8,50})";

    public static boolean isEmailValid(String value) {
        return (!TextUtils.isEmpty(value) && Patterns.EMAIL_ADDRESS.matcher(value).matches());
    }


    /*public static boolean isPasswordValid(String value) {
        if (value.length() >= 6 && value.length() <= 20) {
            return true;
        } else
            return false;
    }*/

    public static boolean isPasswordValidate(String password) {
        Matcher matcher = Pattern.compile(PASSWORD_PATTERN).matcher(password);
        return matcher.matches();

    }

    public static long timeDifference(long given_time) {
        final long MILLIS_PER_DAY = 1000 * 60 * 60 * 24;

        Log.e("check_value", (given_time % MILLIS_PER_DAY) + "==" +
                (System.currentTimeMillis() % MILLIS_PER_DAY));

        return TimeUnit.DAYS.convert((given_time % MILLIS_PER_DAY) -
                (System.currentTimeMillis() % MILLIS_PER_DAY), TimeUnit.MILLISECONDS);
    }


    public void globalDialog(Context context, String str) {
        Dialog dialog;
        TextView msg, ok;
        dialog = new Dialog(context);
        dialog.setContentView(R.layout.global_dialog);
        ok = dialog.findViewById(R.id.ok_textview);
        msg = dialog.findViewById(R.id.msg);
        msg.setText(str);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
            }
        }, 3000);

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(context, android.R.color.transparent)));
        dialog.setCancelable(true);
        dialog.show();
    }

    public void globalDialogAbusiveWord(Context context, String str) {
        Dialog dialog;
        TextView msg, ok;
        dialog = new Dialog(context);
        dialog.setContentView(R.layout.global_dialog_abusive);
        ok = dialog.findViewById(R.id.ok_textview);
        msg = dialog.findViewById(R.id.msg);
        msg.setText(str);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(context, android.R.color.transparent)));
        dialog.setCancelable(true);
        dialog.show();
    }
    public static int getDaysBetween(long start_) {

        Timestamp end = new Timestamp(System.currentTimeMillis());
        Timestamp start = new Timestamp(start_);

        boolean negative = false;
        if (end.before(start)) {
            negative = true;
            Timestamp temp = start;
            start = end;
            end = temp;
        }

        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(start);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        GregorianCalendar calEnd = new GregorianCalendar();
        calEnd.setTime(end);
        calEnd.set(Calendar.HOUR_OF_DAY, 0);
        calEnd.set(Calendar.MINUTE, 0);
        calEnd.set(Calendar.SECOND, 0);
        calEnd.set(Calendar.MILLISECOND, 0);


        if (cal.get(Calendar.YEAR) == calEnd.get(Calendar.YEAR)) {
            if (negative)
                return (calEnd.get(Calendar.DAY_OF_YEAR) - cal.get(Calendar.DAY_OF_YEAR)) * -1;
            return calEnd.get(Calendar.DAY_OF_YEAR) - cal.get(Calendar.DAY_OF_YEAR);
        }

        int days = 0;
        while (calEnd.after(cal)) {
            cal.add(Calendar.DAY_OF_YEAR, 1);
            days++;
        }
        if (negative)
            return days * -1;
        return days;
    }

    public static String timeMilesToDate(String dateInMilliseconds, String dateFormat) {
        return DateFormat.format(dateFormat, Long.parseLong(dateInMilliseconds)).toString();
    }

    public static String dateFormat_yyyy_MM_dd(String date) {
        Date oneWayTripDate = null;
        SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat output = new SimpleDateFormat("dd/MM/yyyy");
        try {
            oneWayTripDate = input.parse(date);  // parse input
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Log.e("check_date_patt", date + "");
        return output.format(oneWayTripDate);
    }


    public static void calNumberPicker(Context context, EditText et) {
        DatePicker datePicker;
        TextView tv_confirm;
        final String[] date = new String[1];
        final String[] new_edit_date = new String[1];

        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.cal_number_picker);

        datePicker = dialog.findViewById(R.id.datePicker);
        tv_confirm = dialog.findViewById(R.id.tv_confirm);


        tv_confirm.setOnClickListener(view -> {
            dialog.cancel();
            if (((datePicker.getMonth() + 1) == 10) || ((datePicker.getMonth() + 1) == 11
                    || ((datePicker.getMonth() + 1) == 12)))
                date[0] = (datePicker.getYear() + "-" + (datePicker.getMonth() + 1) + "-" + datePicker.getDayOfMonth());
            else
                date[0] = (datePicker.getYear() + "-0" + (datePicker.getMonth() + 1) + "-" + datePicker.getDayOfMonth());


            new_edit_date[0] = date[0];

            for (int i = 1; i <= 9; i++) {
                if (Integer.parseInt(date[0].substring(date[0].lastIndexOf("-"))) == i) {
                    Log.e("date_loop", String.valueOf(date[0].charAt(0)));
                    new_edit_date[0] = date[0].substring(date[0].lastIndexOf("-")) + "0" + date[0];
                }
            }
            et.setText(dateFormat_yyyy_MM_dd(new_edit_date[0]));
        });

        dialog.show();

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.BOTTOM;
        lp.windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setAttributes(lp);
    }


    public static String getDate(long time) {
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(time);
        String date = DateFormat.format("yyyy-MM-dd", cal).toString();
        return date;
    }

//    public static String additionMonth(String cur_date, int mon_add){
//        LocalDate date = LocalDate.parse(cur_date);
//        LocalDate newDate = date.plusMonths(mon_add);
//        return newDate.toString();
//    }


    public static String hideSomeCharOfEmail(String emailStr) {
        String email = "";
        if (emailStr != null && !emailStr.equals("")) {
            //email = emailStr.replaceAll("(\\w{1,3})(\\w+)(@.*)", "$1****$3");
            email = emailStr.replaceAll("\\S{1,3}@", "***@");
        }
        return email;
    }

    public static int getFileSize(String selectedPath) {
        int file_size = 0;
        if (selectedPath != null && !selectedPath.equals("")) {
            try {
                File file = new File(selectedPath);
                file_size = Integer.parseInt(String.valueOf(file.length() / 1024));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return file_size;
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager inputManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        try {
            if (inputManager != null && inputManager.isActive())
                if (activity.getCurrentFocus() != null) {

                    inputManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                }
        } catch (Exception e) {
            Log.e("hide keyboard exception", e.toString());
            e.printStackTrace();
        }
    }

    public static Bitmap StringToBitMap(String encodedString) {
        try {
            byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public String BitMapToString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        String temp = Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }

    public static String convertBitmapToString(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        String result = Base64.encodeToString(byteArray, Base64.DEFAULT);
        return result;
    }

    public static String getStringFromFile(String filePath) throws Exception {
        File fl = new File(filePath);
        FileInputStream fin = new FileInputStream(fl);
        String ret = convertStreamToString(fin);
        //Make sure you close all streams.
        fin.close();
        return ret;
    }

    public static String convertStreamToString(InputStream is) throws IOException {
        // http://www.java2s.com/Code/Java/File-Input-Output/ConvertInputStreamtoString.htm
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = null;
        Boolean firstLine = true;
        while ((line = reader.readLine()) != null) {
            if (firstLine) {
                sb.append(line);
                firstLine = false;
            } else {
                sb.append("\n").append(line);
            }
        }
        reader.close();
        return sb.toString();
    }


    public static boolean setError(EditText et, String err_msg) {
        et.setError(err_msg);
        et.requestFocus();
//        String str = "<a>This is a  <font color='#800000FF'> blue text</font> and this is a <font color='red'> red text</font> </a>";
//        et.setError(Html.fromHtml(str +err_msg+ "</font>"));
        return false;
    }


    public static void setLocale(Activity context, String lang) {
        SharedPrefsManager sm = new SharedPrefsManager(context);
        //SET Up Language
        String languageToLoad = "hi";
        Locale locale = new Locale(sm.getString("lan", "en"));
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        context.getBaseContext().getResources().updateConfiguration(config,
                context.getBaseContext().getResources().getDisplayMetrics());
    }

    public static Boolean isLocationEnabled(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            return lm.isLocationEnabled();
        } else {
            int mode = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE,
                    Settings.Secure.LOCATION_MODE_OFF);
            return (mode != Settings.Secure.LOCATION_MODE_OFF);

        }
    }

    public static String decimalTwoDigit(Double amt) {
        DecimalFormat df2;
        df2 = new DecimalFormat("#.##");
        return df2.format(amt);
    }

    public static String getAddress(Context context, double LATITUDE, double LONGITUDE, String type) {
        String country = "NA";
        String city = "NA";
//Set Address
        try {
            Geocoder geocoder = new Geocoder(context, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null && addresses.size() > 0) {

                city = addresses.get(0).getLocality();
                country = addresses.get(0).getCountryName();

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (type.equals("country")) return country;
        else return city;
    }

    private static void sendMessage(String mobile, String msg) {
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(mobile, null, msg, null, null);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static JSONObject objectData(SharedPrefsManager sm) {
        JSONObject object = null;
        try {
            if (sm.containsKey("emp_data")) object = new JSONObject(sm.getString("emp_data"));
            else object = new JSONObject(sm.getString("bus_data"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return object;
    }

    public static void hideKeyboard(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static String light_color(String str) {
        return str.charAt(0) + "33" + str.substring(1);
    }

    public void appImageSize(Context context) {
        sm = new SharedPrefsManager(context);
        APIInterface service = APIClient.getRetrofit().create(APIInterface.class);
        Call<AppLimitPojo> call = service.appImageSettingApi("userapplimit");
        call.enqueue(new Callback<AppLimitPojo>() {
            @Override
            public void onResponse(Call<AppLimitPojo> call, retrofit2.Response<AppLimitPojo> response) {
                if (response.body().getStatus().equals("success")) {
                    //Toast.makeText(context, response.body().getBusinessDocSize(), Toast.LENGTH_SHORT).show();
                    sm.setString("post_img_limit", response.body().getPostImgLimit());
                    sm.setString("post_image_size", response.body().getPostImageSize());
                    sm.setString("post_video_size", response.body().getPostVideoSize());
                    sm.setString("event_img_limit", response.body().getEventImgLimit());
                    sm.setString("event_image_size", response.body().getEventImageSize());
                    sm.setString("business_img_limit", response.body().getBusinessImgLimit());
                    sm.setString("business_image_size", response.body().getBusinessImageSize());
                    sm.setString("business_doc_size", response.body().getBusinessDocSize());
                    sm.setString("business_video_size", response.body().getBusinessVideoSize());
//                    Log.d("response emoji---", response.body().toString());
                }
            }

            @Override
            public void onFailure(Call<AppLimitPojo> call, Throwable t) {
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
//                Log.d("res---", t.getMessage());
            }
        });
    }

//    public void userPhoto(ImageView img, String userpicUrl)
//    {
//        boolean userHasPhoto = false; // Replace with your logic
//
//        if (userHasPhoto) {
//            // Set the user's provided photo
////            Picasso.get().load(userpicUrl).fit().centerCrop().into(img);
////            img.setImageResource(R.drawable.user_photo);
//        } else {
//            // Create a bitmap with the initial letter
//            Bitmap initialBitmap = getInitialBitmap("John");
//
//            // Set the bitmap in the ImageView
//            img.setImageBitmap(initialBitmap);
//        }
//    }
//22-01-25
   /* public Bitmap getInitialBitmap(ImageView img, String name, Context context) {
        boolean userHasPhoto = false;
        Bitmap bitmap = Bitmap.createBitmap(96, 96, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawColor(ColorUtils.getRandomColor());
        Paint paint = new Paint();
        paint.setColor(Color.parseColor("#FFFFFF"));
        paint.setTextSize(35);
        paint.setFakeBoldText(true);
        paint.setAntiAlias(true);
        paint.setTextAlign(Paint.Align.CENTER);
//        paint.setTypeface(Typeface.createFromFile(context.getResources().getResourceTypeName(R.font.montserrat_medium)));
        paint.setTypeface(ResourcesCompat.getFont(context, R.font.montserrat_medium));
        int xPos = canvas.getWidth() / 2;
        int yPos = (int) ((canvas.getHeight() / 2) - ((paint.descent() + paint.ascent()) / 2));
        canvas.drawText(name.substring(0, 1).toUpperCase(), xPos, yPos, paint);
        img.setImageBitmap(bitmap);
        return bitmap;
    }*/


    public Bitmap getInitialBitmap(ImageView img, String name, Context context)  {
        // Create a Bitmap
        Bitmap bitmap = Bitmap.createBitmap(96, 96, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);

        // Check if 'name' is null or empty
        if (name == null || name.trim().isEmpty()) {
            // Use a default drawable resource as the background image
            Drawable defaultDrawable = ContextCompat.getDrawable(context, R.drawable.user_green_icon); // Replace with your default image resource
            if (defaultDrawable != null) {
                defaultDrawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
                defaultDrawable.draw(canvas); // Draw the default image on the canvas
            }
        } else {
            // Draw background with a random color
            canvas.drawColor(ColorUtils.getRandomColor());

            // Set up paint for text
            Paint paint = new Paint();
            paint.setColor(Color.parseColor("#FFFFFF")); // White text color
            paint.setTextSize(35); // Text size
            paint.setFakeBoldText(true);
            paint.setAntiAlias(true);
            paint.setTextAlign(Paint.Align.CENTER);
            paint.setTypeface(ResourcesCompat.getFont(context, R.font.montserrat_medium)); // Custom font
            // Calculate text position
            int xPos = canvas.getWidth() / 2;
            int yPos = (int) ((canvas.getHeight() / 2) - ((paint.descent() + paint.ascent()) / 2));

            // Draw the first letter of the name
            canvas.drawText(name.substring(0, 1).toUpperCase(), xPos, yPos, paint);
        }

        // Set the generated Bitmap to the ImageView
        img.setImageBitmap(bitmap);

        return bitmap;
    }
    public static boolean checkCameraAndGalleryPermission(Activity activity) {
        String[] permissions;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // Android 13 and above
            permissions = new String[]{
                    Manifest.permission.CAMERA
            };
        } else {
            // Below Android 13
            permissions = new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.CAMERA
            };
        }
        boolean allPermissionsGranted = true;
        // Check if all permissions are granted
        for (String permission : permissions) {
            if (ActivityCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED) {
                allPermissionsGranted = false;
                break;
            }
        }
        if (!allPermissionsGranted) {
            // Request permissions
            ActivityCompat.requestPermissions(activity, permissions, 200);
            return false; // Permissions not yet granted
        }
        return true; // All permissions are granted
    }
    public static boolean checkConnection(Context context) {
        final ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connMgr != null) {
            NetworkInfo activeNetworkInfo = connMgr.getActiveNetworkInfo();

            if (activeNetworkInfo != null) { // connected to the internet
                // connected to the mobile provider's data plan
                if (activeNetworkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                    // connected to wifi
                    return true;
                } else return activeNetworkInfo.getType() == ConnectivityManager.TYPE_MOBILE;
            }
        }
        return false;
    }
    public static boolean checkPermission(Context context) {

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

            // Request permissions if not granted
            ActivityCompat.requestPermissions((Activity) context,
                    new String[]{
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.CAMERA
                    },
                    200);

            return false; // Permissions not yet granted
        }
        return true; // All permissions are granted
    }

    // Handle the user's permission response





    public static boolean checkPermission1(Context context) {
        // Check if all required permissions are granted
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            // Request permissions if not granted
            ActivityCompat.requestPermissions((Activity) context, new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.CAMERA,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
            }, 200);

            return false; // Permissions not granted yet
        }

        return true; // All permissions are granted
    }

    public static void checkCameraPermission(Activity context) {
        // Check if camera permission is granted
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            // Request camera permission if not granted
            ActivityCompat.requestPermissions(context,
                    new String[]{Manifest.permission.CAMERA},
                    100); // Request code for camera
        } else {
            // If permission is already granted, perform your action
            onCameraPermissionGranted(context);
        }
    }

    public static void onCameraPermissionGranted(Activity context) {
        // Perform the action when camera permission is granted
        Toast.makeText(context, "Camera permission granted. You can now use the camera.", Toast.LENGTH_SHORT).show();
    }
  /*  public static boolean checkNotificationPermission(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) { // Android 13+
            return ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED;
        }
        return true; // Older versions don't need explicit notification permission
    }*/

  /*  public static void requestNotificationPermission(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) { // Android 13+
            if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.POST_NOTIFICATIONS}, 300);
            }
        }
    }*/
  public static boolean checkNotificationPermission(Context context) {
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) { // Android 13+
          boolean notificationPermission = ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED;
          boolean fineLocationPermission = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
          boolean coarseLocationPermission = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
          return notificationPermission && (fineLocationPermission || coarseLocationPermission);
      } else {
          // For Android 12 and below, only check location
          boolean fineLocationPermission = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
          boolean coarseLocationPermission = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
          return fineLocationPermission || coarseLocationPermission;
      }
  }

    public static boolean checkNotificationPermissionWallUsed(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) { // Android 13+
            return ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED;
        } else {
            // Android 12 aur neeche ke liye notification permission ki zarurat nahi hoti
            return true;
        }
    }

    public static void requestNotificationPermission(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(activity, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(activity,
                        new String[]{Manifest.permission.POST_NOTIFICATIONS}, 300);
            }
        }
    }
    public static boolean hasAllRequiredPermissions(Context context) {
        boolean notificationPermission = true;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            notificationPermission = ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED;
        }


        return notificationPermission;
    }

    public static void requestNotificationAndLocationPermissions(Activity activity) {
        List<String> permissionsToRequest = new ArrayList<>();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(activity, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                permissionsToRequest.add(Manifest.permission.POST_NOTIFICATIONS);
            }
        }

        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permissionsToRequest.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }

        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permissionsToRequest.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        }

        if (!permissionsToRequest.isEmpty()) {
            ActivityCompat.requestPermissions(activity,
                    permissionsToRequest.toArray(new String[0]), 300);
        }
    }

}
