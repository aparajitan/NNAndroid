package com.app_neighbrsnook.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefsManager {
    private static final String TAG = SharedPrefsManager.class.getName();
    private final SharedPreferences prefs;
    private static SharedPrefsManager uniqueInstance;
    public static final String PREF_NAME = "Neighbrsnook";
    public SharedPrefsManager(Context appContext) {
        prefs = appContext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }
    private static SharedPreferences getPreferences(Context context) {
        if (context != null) {
            return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        }
        return null;
    }
    /**
     * Throws IllegalStateException if this class is not initialized
     *
     * @return unique SharedPrefsManager instance
     */
    public static SharedPrefsManager getInstance() {
        if (uniqueInstance == null) {
            throw new IllegalStateException(
                    "SharedPrefsManager is not initialized, call initialize(applicationContext) " +
                            "static method first");
        }
        return uniqueInstance;
    }

    /**
     * Initialize this class using application Context,
     * should be called once in the beginning by any application Component
     *
     * @param appContext application context
     */
    public static void initialize(Context appContext) {
        if (appContext == null) {
            throw new NullPointerException("Provided application context is null");
        }
        if (uniqueInstance == null) {
            synchronized (SharedPrefsManager.class) {
                if (uniqueInstance == null) {
                    uniqueInstance = new SharedPrefsManager(appContext);
                }
            }
        }
    }

    private SharedPreferences getPrefs() {
        return prefs;
    }

    /**
     * Clears all data in SharedPreferences
     */
    public void clearPrefs() {
        SharedPreferences.Editor editor = getPrefs().edit();
        editor.clear();
        editor.commit();
    }

    public void removeKey(String key) {
        getPrefs().edit().remove(key).commit();
    }

    public boolean containsKey(String key) {
        return getPrefs().contains(key);
    }

    public String getString(String key, String defValue) {
        return getPrefs().getString(key, defValue);
    }

    public String getString(String key) {
        return getString(key, null);
    }
    public void setString(String key, String value) {
        SharedPreferences.Editor editor = getPrefs().edit();
        editor.putString(key, value);
        editor.apply();
    }
    public int getInt(String key, int defValue) {
        return getPrefs().getInt(key, defValue);
    }

    public int getInt(String key) {
        return getInt(key, 0);
    }

    public void setInt(String key, int value) {
        SharedPreferences.Editor editor = getPrefs().edit();
        editor.putInt(key, value);
        editor.apply();
    }

    public long getLong(String key, long defValue) {
        return getPrefs().getLong(key, defValue);
    }

    public long getLong(String key) {
        return getLong(key, 0L);
    }

    public void setLong(String key, long value) {
        SharedPreferences.Editor editor = getPrefs().edit();
        editor.putLong(key, value);
        editor.apply();
    }

    public boolean getBoolean(String key, boolean defValue) {
        return getPrefs().getBoolean(key, defValue);
    }

    public boolean getBoolean(String key) {
        return getBoolean(key, false);
    }

    public void setBoolean(String key, boolean value) {
        SharedPreferences.Editor editor = getPrefs().edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public boolean getFloat(String key) {
        return getFloat(key, 0f);
    }

    public boolean getFloat(String key, float defValue) {
        return getFloat(key, defValue);
    }

    public void setFloat(String key, Float value) {
        SharedPreferences.Editor editor = getPrefs().edit();
        editor.putFloat(key, value);
        editor.apply();
    }


    public static double getdouble(Context context, String key) {
        final SharedPreferences preferences = getPreferences(context);
        try {
            return Double.parseDouble(preferences.getString(key, ""));
        } catch (Exception e) {
        }
        return 0;
    }
    public static void savedouble(Context context, String key, double value) {
        final SharedPreferences preferences = getPreferences(context);
        if (preferences != null) {
            try {
                preferences.edit().putString(key, String.valueOf(value)).apply();
            } catch (Exception e) {

            }
        }
    }
    public static void saveLong(Context context, String key, long value) {
        final SharedPreferences preferences = getPreferences(context);
        if (preferences != null) {
            try {
                preferences.edit().putLong(key, value).apply();
            } catch (Exception e) {

            }
        }
    }
    public static float getFloat(Context context, String key) {
        final SharedPreferences preferences = getPreferences(context);
        if (preferences != null && preferences.contains(key)) {
            return preferences.getFloat(key, 0);
        }
        return 0;
    }
    public static void saveFloat(Context context, String key, float value) {
        final SharedPreferences preferences = getPreferences(context);
        if (preferences != null) {
            preferences.edit().putFloat(key, value).apply();
        }
    }
    public static void deleteAllData(Context context) {
        final SharedPreferences preferences = getPreferences(context);
        if (preferences != null) {
            preferences.edit().clear().apply();
        }
    }
    public static void delete(Context context, String key) {
        final SharedPreferences preferences = getPreferences(context);
        if (preferences != null) {
            preferences.edit().remove(key).apply();
        }
    }

    public static int getInt(Context context, String key) {
        final SharedPreferences preferences = getPreferences(context);
        try {
            return preferences.getInt(key, 0);
        } catch (Exception e) {

        }
        return 0;
    }

    public static void saveInt(Context context, String key, int value) {
        final SharedPreferences preferences = getPreferences(context);
        if (preferences != null) {
            try {
                preferences.edit().putInt(key, value).apply();
            } catch (Exception e) {

            }
        }
    }




}


