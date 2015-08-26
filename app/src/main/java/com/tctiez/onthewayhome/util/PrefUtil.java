package com.tctiez.onthewayhome.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
import android.util.Log;

/**
 * Created by Eugene J. Jeon on 2015-08-19.
 */
public class PrefUtil {
    public static final String KEY_NEWBIE = "PREF_KEY_NEWBIE";

    private static PrefUtil mInstance = null;

    public static PrefUtil getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new PrefUtil(context);
        }

        return mInstance;
    }

    private Context             mContext    = null;
    private SharedPreferences   mPref       = null;
    private Editor              mEditor     = null;

    private PrefUtil(Context context) {
        mContext = context;
        mPref = PreferenceManager.getDefaultSharedPreferences(mContext);
        mEditor = mPref.edit();
    }

    public boolean get(String key, boolean defaultValue) {
        return mPref.getBoolean(key, defaultValue);
    }

    public boolean set(String key, boolean value) {
        try {
            mEditor.putBoolean(key, value);
            mEditor.commit();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public int get(String key, int defaultValue) {
        return mPref.getInt(key, defaultValue);
    }

    public boolean set(String key, int value) {
        try {
            mEditor.putInt(key, value);
            mEditor.commit();
            return true;
        } catch(Exception e) {
            return false;
        }
    }

    public float get(String key, float defaultValue) {
        return mPref.getFloat(key, defaultValue);
    }

    public boolean set(String key, float value){
        try {
            mEditor.putFloat(key, value);
            mEditor.commit();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
