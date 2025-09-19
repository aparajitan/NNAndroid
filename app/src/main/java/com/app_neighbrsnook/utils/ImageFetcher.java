package com.app_neighbrsnook.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import java.io.File;

public class ImageFetcher extends AsyncTask<String, Void, Bitmap> {

    private Context context;

    public ImageFetcher(Context context) {
        this.context = context;
    }

    @Override
    protected Bitmap doInBackground(String... params) {
        String fileName = params[0];
        Bitmap bitmap = null;

        try {
            File directory = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
//            File directory = new File(Environment.getExternalStorageDirectory(), "YourAppFolder");
            File file = new File(directory, "your_image.png");

            if (file.exists()) {
                bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                Log.d("saved image1...", bitmap.toString());
            }
        } catch (Exception e) {
            Log.e("ImageFetcher", "Error fetching image from storage: " + e.getMessage());
        }

        return bitmap;
    }
}