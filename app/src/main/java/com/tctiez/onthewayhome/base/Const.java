package com.tctiez.onthewayhome.base;

import android.content.Context;
import android.os.Environment;

import com.tctiez.onthewayhome.VIntro;
import com.tctiez.onthewayhome.VMap;

/**
 * Created by Eugene J. Jeon on 2015-08-14.
 */
public class Const {
    // TRIAL, LIMIT DAY
    public static final boolean IS_START_TESTDATECHECK = true;
    public static final String  TESTDATECHECK_DATESTR  = "20150820051000000";

    public static enum ViewKey {
        VRoot(RootView.class.getName());

        private String value;

        ViewKey(String value) {
            this.value = value;
        }

        public String get() {
            return value;
        }
    }

    public static enum ChildViewKey {
        // TODO: YOU SHOULD WRITE, IF ADD VIEW.
        VIntro(VIntro.class.getName()), VMap(VMap.class.getName());

        private String value;

        ChildViewKey(String value) {
            this.value = value;
        }

        public String get() {
            return value;
        }
    }

    public static final String SDPath = Environment.getExternalStorageDirectory().getAbsolutePath();

    public static final String getLocalFileDir(Context cont) {
        String ret = null;
        try {
            ret = cont.getFilesDir().getAbsolutePath();
        } catch (Exception e) {
            ret = null;
        }
        return ret;
    }

    public static final String getExternalFileDir(Context cont) {
        String ret = null;
        try {
            ret = cont.getExternalFilesDir(null).getAbsolutePath();
        } catch (Exception e) {
            ret = null;
        }
        return ret;
    }
}
