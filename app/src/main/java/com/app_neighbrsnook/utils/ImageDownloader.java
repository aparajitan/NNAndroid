package com.app_neighbrsnook.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ImageDownloader extends AsyncTask<String, Void, Bitmap> {
    private Context context;
    Bitmap bitmap;

    public ImageDownloader(Context context) {
        this.context = context;
    }

    @Override
    protected Bitmap doInBackground(String... params) {
        String url = params[0];
        String fileName = System.currentTimeMillis() + "";

        try {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(url)
                    .build();

            Response response = client.newCall(request).execute();

            if (response.isSuccessful()) {
                bitmap = BitmapFactory.decodeStream(new java.net.URL(url).openStream());

                // Save the bitmap to local storage
                saveBitmapToStorage(bitmap, fileName);

                return bitmap;
            }
        } catch (IOException e) {
            Log.e("ImageDownloader", "Error downloading image: " + e.getMessage());
        }

        return bitmap;
    }

    private void saveBitmapToStorage(Bitmap bitmap, String fileName) {

        File directory = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        String filename = "your_image.png";
        File file = new File(directory, filename);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            Log.d("saved image...", bitmap.toString());
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }



    }
}