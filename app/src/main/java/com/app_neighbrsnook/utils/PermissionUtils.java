package com.app_neighbrsnook.utils;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class PermissionUtils {
    private static final int CAMERA_PERMISSION_REQUEST_CODE = 100;
    private static final int STORAGE_PERMISSION_REQUEST_CODE = 101;

    // Check if the camera permission is granted
    public static boolean checkCameraPermission(Activity activity) {
        int cameraPermission = ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA);
        return cameraPermission == PackageManager.PERMISSION_GRANTED;
    }

    // Check if storage permissions (read and write) are granted
    public static boolean checkStoragePermissions(Activity activity) {
        int readPermission = ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE);
        int writePermission = ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        return readPermission == PackageManager.PERMISSION_GRANTED && writePermission == PackageManager.PERMISSION_GRANTED;
    }

    // Request camera permission
    public static void requestCameraPermission(Activity activity) {
        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST_CODE);
    }

    // Request storage permissions (read and write)
    public static void requestStoragePermissions(Activity activity) {
        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION_REQUEST_CODE);
    }
}
