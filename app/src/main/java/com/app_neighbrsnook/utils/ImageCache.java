package com.app_neighbrsnook.utils;

import android.graphics.Bitmap;
import android.util.LruCache;

public class ImageCache {
    private LruCache<String, Bitmap> memoryCache;

    public ImageCache() {
        // Initialize the LruCache with a portion of the available memory
        int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        int cacheSize = maxMemory / 8;
        memoryCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                return bitmap.getByteCount() / 1024;
            }
        };
    }

    public void addBitmapToCache(String key, Bitmap bitmap) {
        if (getBitmapFromCache(key) == null) {
            memoryCache.put(key, bitmap);
        }
    }

    public Bitmap getBitmapFromCache(String key) {
        return memoryCache.get(key);
    }

}
