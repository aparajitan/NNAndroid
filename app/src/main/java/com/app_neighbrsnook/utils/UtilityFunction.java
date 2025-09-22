package com.app_neighbrsnook.utils;

import static android.content.Context.MODE_PRIVATE;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Base64OutputStream;
import android.util.Log;
import android.util.Patterns;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.textfield.TextInputEditText;
import com.app_neighbrsnook.R;
import com.app_neighbrsnook.login.LoginActivity;
import com.app_neighbrsnook.login.LoginActivity;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
public class UtilityFunction {
    public static final int RECORD_PERMISSION_REQUEST_CODE = 1;
    public static final int EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE = 2;
    public static final int CAMERA_PERMISSION_REQUEST_CODE = 3;
    public static final int CALL_PHONE_PERMISSION_REQUEST_CODE = 4;
    public static final int EXTERNAL_STORAGE_READ_PERMISSION_REQUEST_CODE = 5;
    public static final String PrefDB = "loginPref";

    public static String getSplitedDate(String date) {
        if (date != null) {
            String[] split = date.split("T");
            return changeDateFormat(split[0]);
        } else {
            return "No date";
        }
    }
    public static String changeDateFormat(String date) {
        try {
            String[] splitDate = date.split("-");
            return splitDate[2] + "-" + splitDate[1] + "-" + splitDate[0];
        }catch (Exception e){
            e.printStackTrace();
            return "";
        }
    }
    public static void SetBooleanRemember(Context context, String KEY, boolean Value) {
        SharedPreferences sp = context.getSharedPreferences(PrefDB, MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(KEY, Value);
        editor.commit();
    }
    public static String changeTimeFormat(String time) {
        if (time==null || time.equals("")){
            return "";
        }
        String[] splitDate = time.split(":");
        String hour=splitDate[0];
        try {
            int hh=Integer.parseInt(hour);
            if (hh>12){
                hh=hh-12;
                if (hh>9){
                    return hh+":"+splitDate[1]+" PM";
                }else {
                    return "0"+hh+":"+splitDate[1]+" PM";
                }
            }else {
                if (hh>9){
                    return hh+":"+splitDate[1]+" AM";
                }else {
                    return "0"+hh+":"+splitDate[1]+" AM";
                }
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        return "";
    }



    public static String getSplitedTime(String date) {
        if (date != null) {
            String[] split = date.split("T");
            return getTime(split[1]);
        } else {
            return "No date";
        }
    }

    public static String getTime(String dateTime) {
        String[] splitDate = dateTime.split(":");
        return splitDate[0] + ":" + splitDate[1];
    }

    public static String formatDate(String dateToFormat, String inputFormat, String outputFormat) {
        try {
            Log.e("DATE", "Input Date Date is " + dateToFormat);

            String convertedDate = new SimpleDateFormat(outputFormat)
                    .format(new SimpleDateFormat(inputFormat)
                            .parse(dateToFormat));


            Log.d("DATE", "Output Date is " + convertedDate);

            //Update Date
            return convertedDate;

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;

    }

    public static boolean emailValidator(String email) {
        Pattern pattern;
        Matcher matcher;
        final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }
    public static String getDay(String date_in_string_format){
        DateFormat df = DateFormat.getDateInstance();
        Date date;
        try {
            date = df.parse(date_in_string_format);
        } catch (Exception e) {
            Log.e("Error:","Exception " + e);
            return null;
        }
        return new SimpleDateFormat("EEEE").format(date);
    }
    public static String getCalculatedDate(String dateFormat, int days) {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat s = new SimpleDateFormat(dateFormat, Locale.getDefault());
        cal.add(Calendar.DAY_OF_YEAR, days);
        return s.format(new Date(cal.getTimeInMillis()));
    }

    public static String getFormattedDate(String date, String forChange, String toChange) {
        /*DateFormat originalFormat = new SimpleDateFormat("MMMM dd, yyyy", Locale.ENGLISH);
        DateFormat targetFormat = new SimpleDateFormat("yyyyMMdd");
        Date date = originalFormat.parse("August 21, 2012");
        */
        String formattedDate = "";
        try {
            DateFormat originalFormat = new SimpleDateFormat(forChange, Locale.ENGLISH);
            DateFormat targetFormat = new SimpleDateFormat(toChange);
            Date date1 = originalFormat.parse(date);
            formattedDate = targetFormat.format(date1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return formattedDate;
    }
    public static void hideKeybord(Context context, View view) {
        InputMethodManager inputManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        view.requestFocus();
        inputManager.hideSoftInputFromWindow(view.getWindowToken(),
                InputMethodManager.RESULT_UNCHANGED_SHOWN);
    }

    public static String getCountryTime(String timezone) {
        TimeZone tz = TimeZone.getTimeZone(timezone);
        Calendar c = Calendar.getInstance(tz);
        // c.setTimeZone(TimeZone.getTimeZone(timezone));
        // Date date = c.getTime();
        //  SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        //  String strDate = df.format(date);
        String time = c.get(Calendar.HOUR_OF_DAY) + ":" + c.get(Calendar.MINUTE) + ":" + c.get(Calendar.SECOND) + " " + c.get(Calendar.AM_PM);
        return time;
    }

   public static Boolean returnAlertValue=false;
    public static void alertGenerate(final Activity context, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Alert").setMessage(message);

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                returnAlertValue=false;

            }
        }).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                //Constants.ACTIVITY_NAME=Constants.HOME_ACTIVITY;
               // context.finishAffinity();
              //  context.onBackPressed();
                //   context.finish();
              //  returnAlertValue=true;
            }
        });

        AlertDialog dialog = builder.create();
        dialog.setCancelable(false);
        dialog.show();
        TextView messageView = dialog.findViewById(android.R.id.message);
        messageView.setGravity(Gravity.CENTER);

    }



    public static String[] SplitString(String stringValue) {
        try {
            String[] value = stringValue.split(" ");
            return value;
        } catch (Exception e) {
            String[] value = new String[]{stringValue};
            return value;
        }
    }
    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }
    public static String removeLastChar(String str) {
        return str.substring(0, str.length() - 1);
    }
    public static String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        Log.d("StringImage", encodedImage);
        return encodedImage;

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

// second solution is you can set the path inside decodeFile function
//viewImage.setImageBitmap(BitmapFactory.decodeFile("your iamge path"));

    public static boolean isValidEmail(CharSequence target) {
        return Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    public static boolean isValidMobileNumber(String phoneNumber) {
        return Patterns.PHONE.matcher(phoneNumber).matches() && phoneNumber.length() > 9;
    }
    private static final String[] permissions = {
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
    };
    public static boolean hasPermission(Activity activity) {
        for (String permission : permissions) {
            if (ActivityCompat.checkSelfPermission(activity, permission)
                    != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }
    public static boolean surveyIsAvailable(String city) {
        if (city.equalsIgnoreCase("AHMEDABAD")) {
            // BANGLORE  CHENNAI
            //context.startActivity(new Intent(context, Shedule_servay.class));
            return true;
        } else if (city.equalsIgnoreCase("BANGLORE")) {
            return true;
        } else if (city.equalsIgnoreCase("CHENNAI")) {
            return true;
        } else if (city.equalsIgnoreCase("GURGAON")) {
            return true;
        } else if (city.equalsIgnoreCase("HYDERABAD")) {
            return true;
        } else if (city.equalsIgnoreCase("KOLKATA")) {
            return true;
        } else if (city.equalsIgnoreCase("MUMBAI")) {
            return true;
        } else if (city.equalsIgnoreCase("NAGPUR")) {
            return true;
        } else if (city.equalsIgnoreCase("NEW DELHI")) {
            return true;
        } else if (city.equalsIgnoreCase("PUNE")) {
            return true;
        } else if (city.equalsIgnoreCase("Noida")) {
            return true;
        } else if (city.equalsIgnoreCase("Greater Noida")) {
            return true;
        } else if (city.equalsIgnoreCase("Faridabad")) {
            return true;
        } else if (city.equalsIgnoreCase("Delhi")) {
            return true;
        } else return city.equalsIgnoreCase("Gurugram");


    }

    public static Date convertStringToDate(String strDate) {
        //String strDate = "2013-05-15T10:00:00-0700";
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Date date = null;
        try {
            date = dateFormat.parse(strDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static void checkValueToSend(Context context, JSONObject jsonObject, String Key, String Value) {
        try {
            if (Value != null) {
                jsonObject.put(Key, Value);
            } else {
                jsonObject.put(Key, "");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String filterAddress(Context context, String lat, String lng, String filterType) {
        String city = "";
        String country = "";
        String state = "";
        String locality = "";
        Geocoder geocoder = new Geocoder(context);
        try {
            List<Address> addresses = geocoder.getFromLocation(Double.parseDouble(lat), Double.parseDouble(lng), 1);
            city = addresses.get(0).getSubAdminArea();//.getAdminArea();
            country = addresses.get(0).getCountryName();
            state = addresses.get(0).getAdminArea();
            locality = addresses.get(0).getLocality();

            Log.d("Locality", addresses.get(0).getLocality());
            Log.d("SubLocality", addresses.get(0).getSubLocality());

        } catch (Exception e) {
            e.printStackTrace();
        }
        if (filterType.equals("city")) {
            return city;
        } else if (filterType.equals("country")) {
            return country;
        } else if (filterType.equals("state")) {
            return state;
        } else {
            return locality;
        }

    }

    public static Bitmap getScaledBitmap(Bitmap b, int reqWidth, int reqHeight) {
        Matrix m = new Matrix();
        m.setRectToRect(new RectF(0, 0, b.getWidth(), b.getHeight()), new RectF(0, 0, reqWidth, reqHeight), Matrix.ScaleToFit.CENTER);
        return Bitmap.createBitmap(b, 0, 0, b.getWidth(), b.getHeight(), m, true);
    }

    public static String getCurrentTime() {
        //date output format
        DateFormat dateFormat = new SimpleDateFormat("dd-MM/yyyy HH:mm:ss");
        Calendar cal = Calendar.getInstance();
        return dateFormat.format(cal.getTime());
    }

    public static String removeQuotesFromStartAndEndOfString(String inputStr) {
        String result = inputStr;
        int firstQuote = inputStr.indexOf('\"');
        int lastQuote = result.lastIndexOf('\"');
        int strLength = inputStr.length();
        if (firstQuote == 0 && lastQuote == strLength - 1) {
            result = result.substring(1, strLength - 1);
        }
        return result;
    }


    public static boolean checkPermissionForCamera(Activity activity) {
        int result = ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    public static void requestPermissionForCamera(Activity activity) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.CAMERA)) {
            //Toast.makeText(activity,  activity.getString(R.string.permission_needed), Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST_CODE);
        }
    }

    public static void checkEditTextSetValue(String value, EditText editText) {
        if (value == null) {
            editText.setText("");

        } else if (value.isEmpty()) {
            editText.setText("");
        } else if (value.equals("null")) {
            editText.setText("");
        } else {
            editText.setText(value);
        }
    }

    public static void checkTextInputEditTextSetValue(String value, TextInputEditText editText) {
        if (value == null) {
            editText.setText("");
        } else if (value.isEmpty()) {
            editText.setText("");
        } else if (value.equals("null")) {
            editText.setText("");
        } else {
            editText.setText(value);
        }
    }

    public static void checkTextViewSetValue(String value, TextView textView) {
      try {
          if (value == null) {
              textView.setText("");
          } else if (value.isEmpty()) {
              textView.setText("");
          } else if (value.contains("null")) {
              textView.setText(value.replace("null","").trim());
              //textView.setText("");

          } else {
              textView.setText(value+"");
          }
      }catch (Exception e)
      {
          e.printStackTrace();
          textView.setText("");
      }
    }
    public static void setYesNo(Boolean value, TextView textView)
    {
     try {
         if (value)
         {
             textView.setText("Yes");
         }else {
             textView.setText("No");
         }
     }catch (Exception e)
     {
         e.printStackTrace();
         textView.setText("No");
     }
    }

  /*  public static MultipartBody.Part getMultiPartBody(String key, String mMediaUrl) {
        if (mMediaUrl != null) {
            File file = new File(mMediaUrl);
            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            return MultipartBody.Part.createFormData(key, file.getName(), requestFile);
        } else {
            return MultipartBody.Part.createFormData(key, "");
        }
    }*/
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[] {
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context The context.
     * @param uri The Uri to query.
     * @param selection (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }




    public final static String toRoman(int number) {
        final TreeMap<Integer, String> map = new TreeMap<Integer, String>();
        map.put(1000, "M");
        map.put(900, "CM");
        map.put(500, "D");
        map.put(400, "CD");
        map.put(100, "C");
        map.put(90, "XC");
        map.put(50, "L");
        map.put(40, "XL");
        map.put(10, "X");
        map.put(9, "IX");
        map.put(5, "V");
        map.put(4, "IV");
        map.put(1, "I");

        int l = map.floorKey(number);
        if (number == l) {
            return map.get(number);
        }
        return map.get(l) + toRoman(number - l);
    }

    public static void showLoader(final Activity activity, final String meassage)
    {
        final ProgressDialog dialog=new ProgressDialog(activity);
        dialog.setMessage("Please wait...");
        dialog.setCancelable(false);
        dialog.show();
        new CountDownTimer(10000, 1000) {
            @Override
            public void onTick(long l) {

            }

            @Override
            public void onFinish() {
                dialog.dismiss();
                Toast.makeText(activity, ""+meassage, Toast.LENGTH_SHORT).show();
                activity.onBackPressed();
            }
        }.start();
    }
    public static String getEncoded64ImageStringFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();
        String result = Base64.encodeToString(b, Base64.NO_WRAP);
        return result;
    }
    public static File createImageFile(Context context) {
        if (context == null)
            return null;
        // Create an image file name
        String imageFileName = "JPEG_" + System.currentTimeMillis() + "_";
        File storageDir;
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT)
            storageDir = context.getExternalFilesDir("Pictures");
        else storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        //TODO:Check path-
        String path = storageDir + "/" + imageFileName + ".jpg";
        File image = new File(path);
        // Save a file: path for use with ACTION_VIEW intents
        return image;
    }

    public static void goToLoginPageAlert(final Activity context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Alert").setMessage("Are you sure you want to Logout from this app.");

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

            }
        }).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Constants.ACTIVITY_NAME=Constants.HOME_ACTIVITY;
                // context.startActivity(new Intent(context,LoginActivity.class));
                // context.finish();

                /*if (Constants.googleSignInAccount!=null)
                {*/
               // PrefMananger.clearWholePreference(context);
                context.startActivity(new Intent(context, LoginActivity.class));
                context.finishAffinity();
              /*  }else   if (Constants.Facebook_Profile!=null)
                {

                    Constants.accessTokenTracker.stopTracking();
                    Constants.profileTracker.stopTracking();

                    startActivity(new Intent(LoginActivity.this, LoginActivity.class));

                }else {
                    startActivity(new Intent(LoginActivity.this, LoginActivity.class));
                }*/

            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
        TextView messageView = dialog.findViewById(android.R.id.message);
        messageView.setGravity(Gravity.CENTER);
    }
    static AlertDialog.Builder builder;
    static AlertDialog dialog;
    public static void gotoLoginSessionExpire(final Activity context) {
        if (dialog!=null){
            dialog.dismiss();
        }
         builder= new AlertDialog.Builder(context);
        builder.setTitle("Alert").setMessage("Your session is expire.\n Please login again.");

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

            }
        }).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(DialogInterface dialog, int which) {
               // PrefManager.clearWholePreference(context);
                //Constants.ACTIVITY_NAME=Constants.HOME_ACTIVITY;
                // context.startActivity(new Intent(context,LoginActivity.class));
                // context.finish();

                /*if (Constants.googleSignInAccount!=null)
                {*/
                context.startActivity(new Intent(context, LoginActivity.class));
                context.finishAffinity();
              /*  }else   if (Constants.Facebook_Profile!=null)
                {

                    Constants.accessTokenTracker.stopTracking();
                    Constants.profileTracker.stopTracking();

                    startActivity(new Intent(LoginActivity.this, LoginActivity.class));

                }else {
                    startActivity(new Intent(LoginActivity.this, LoginActivity.class));
                }*/

            }
        });

         dialog= builder.create();
        dialog.show();
        TextView messageView = dialog.findViewById(android.R.id.message);
        messageView.setGravity(Gravity.CENTER);
    }
    public static String getCurrentVersion(Context mContext) {
        String currentVersion="1.0";
        Log.d("checkApp","Here");
        PackageManager packageManager = mContext.getPackageManager();
        PackageInfo packageInfo = null;
        try {
            packageInfo = packageManager.getPackageInfo(mContext.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        currentVersion = packageInfo.versionName;
        Log.e("currentVersion", currentVersion);
        return currentVersion;
    }



    // Converting File to Base64.encode String type using Method
    public static String getStringFile(File f) {
        InputStream inputStream = null;
        String encodedFile= "", lastVal;
        try {
            inputStream = new FileInputStream(f.getAbsolutePath());

            byte[] buffer = new byte[10240];//specify the size to allow
            int bytesRead;
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            Base64OutputStream output64 = new Base64OutputStream(output, Base64.DEFAULT);

            while ((bytesRead = inputStream.read(buffer)) != -1) {
                output64.write(buffer, 0, bytesRead);
            }
            output64.close();
            encodedFile =  output.toString();
        }
        catch (FileNotFoundException e1 ) {
            e1.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        lastVal = encodedFile;
        return lastVal;
    }
    public static String convertFileToByteArray(File f) {
        byte[] byteArray = null;
        try {
            InputStream inputStream = new FileInputStream(f);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] b = new byte[1024 * 11];
            int bytesRead = 0;

            while ((bytesRead = inputStream.read(b)) != -1) {
                bos.write(b, 0, bytesRead);
            }

            byteArray = bos.toByteArray();

            Log.e("Byte array", ">" + byteArray);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return Base64.encodeToString(byteArray, Base64.NO_WRAP);
    }

    public static Bitmap getBitmapFromUri(Context context, Uri uri) throws IOException {
        ParcelFileDescriptor parcelFileDescriptor =
                context.getContentResolver().openFileDescriptor(uri, "r");
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        parcelFileDescriptor.close();
        return image;
    }
    public static String getMimeType(Context context, Uri uri) {
        String extension;

        //Check uri format to avoid null
        if (uri.getScheme().equals(ContentResolver.SCHEME_CONTENT)) {
            //If scheme is a content
            final MimeTypeMap mime = MimeTypeMap.getSingleton();
            extension = mime.getExtensionFromMimeType(context.getContentResolver().getType(uri));
        } else {
            //If scheme is a File
            //This will replace white spaces with %20 and also other special characters. This will avoid returning null values on file name with spaces and special characters.
            extension = MimeTypeMap.getFileExtensionFromUrl(Uri.fromFile(new File(uri.getPath())).toString());

        }
        Log.d("Extension",extension);
        return extension;
    }
   /* public static void openCreateSuccessDialog(final Activity context, String message) {
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.inflate_success_create_leave);
        Button btn_ok = dialog.findViewById(R.id.btn_ok);
        TextView tv_show_message = dialog.findViewById(R.id.tv_show_message);
        ImageView iv_success_view = dialog.findViewById(R.id.iv_success_view);
        tv_show_message.setText(message);
        dialog.setCancelable(false);
        dialog.show();

        iv_success_view.animate();
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //IS_NEW_CREATED = true;
                dialog.dismiss();
                context.onBackPressed();
                Intent intent=new Intent(UrlContainer.NEW_CREATED_RECIEVER);
                intent.putExtra(UrlContainer.NEW_CREATED,true);
                context.sendBroadcast(intent);
            }
        });
    }*/

    public static void showToast(final Activity context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
   /* public static void openSuccessDialog(final Activity context, String message) {
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.inflate_success_create_leave);
        Button btn_ok = dialog.findViewById(R.id.btn_ok);
        TextView tv_show_message = dialog.findViewById(R.id.tv_show_message);
        ImageView iv_success_view = dialog.findViewById(R.id.iv_success_view);
        tv_show_message.setText(message);
        dialog.setCancelable(false);
        dialog.show();

        iv_success_view.animate();
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //StringUtils.IS_NEW_CREATED = true;
                dialog.dismiss();
            }
        });
    }*/

    /*public static void setLanguage(Context context){
        Locale locale = new Locale(PrefManager.getLanguage(context));
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        context.getResources().updateConfiguration(config,context.getResources().getDisplayMetrics());
    }*/

 /*   public static RequestBody toRequestBody(String value) {
        return RequestBody.create(MultipartBody.FORM, value);
    }
*/
    public static Double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }

    public static String getTimeFormat() {
        //date output format
        DateFormat dateFormat = new SimpleDateFormat("MMM,dd,yyyy HH:mm a");
        Calendar cal = Calendar.getInstance();
        return dateFormat.format(cal.getTime());
    }
    public static void setSolidFontawsometypeface(Context a, TextView t)
    {

        Typeface font1 = Typeface.createFromAsset(a.getAssets(), "fonts/FontSolid.otf" );
        t.setTypeface(font1);
    }

    private static ProgressDialog processing;

    public static void showLoading(Context mContext, String message) {

        if(mContext != null){
            if (processing!=null){
                processing.dismiss();
            }
            processing = new ProgressDialog(mContext);
            processing.setMessage(message);
            processing.setCancelable(false);
            processing.setCanceledOnTouchOutside(false);
            processing.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            //processing.setIndeterminateDrawable(mContext.getResources().getDrawable(R.drawable.custom_progress_dialog));

            //mContext.getIndeterminateDrawable().setColorFilter(0xFFFFFFFF, android.graphics.PorterDuff.Mode.MULTIPLY);
            //  processing.setIndeterminateDrawable(mContext.getDrawable(R.drawable.custom_progress_dialog));
            processing.setIndeterminate(false);
            processing.setCanceledOnTouchOutside(false);

            if (!processing.isShowing()) {
                processing.show();
            }

        }

    }

    public static void hideLoading() {
        if (processing != null)
            processing.dismiss();
    }

    public static RequestBody toRequestBody(String value) {
        return RequestBody.create(MultipartBody.FORM, value);
    }
}


