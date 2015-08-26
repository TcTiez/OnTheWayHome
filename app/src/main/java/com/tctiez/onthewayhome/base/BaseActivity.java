package com.tctiez.onthewayhome.base;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Display;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.tctiez.onthewayhome.R;
import com.tctiez.onthewayhome.base.BaseView.ANIMATION_STATE_GONE;
import com.tctiez.onthewayhome.base.BaseView.ANIMATION_STATE_VISIBLE;
import com.tctiez.onthewayhome.base.Const.ViewKey;
import com.tctiez.onthewayhome.service.GPS;
import com.tctiez.onthewayhome.util.AnimationUtil;
import com.tctiez.onthewayhome.util.TimeUtil;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Eugene J. Jeon on 2015-08-14.
 */
public abstract class BaseActivity extends Activity {
    protected int          mSDKVer             = 0;
    protected BaseActivity mContext            = null;
    protected FrameLayout  mBaseFrameLayout    = null;
    protected FrameLayout  mBasePopupLayout    = null;
    protected FrameLayout  mBaseProgressLayout = null;

    protected View mDarkView = null;

    public Object tTempObj = null;

    /** 화면 회전 여부 */
    public boolean mIsLandscape = false;
    /** 화면 가로, 세로 길이 */
    public int     mLcdWidth, mLcdHeight;
    /** 상태바 세로 길이 */
    public int     mStatusBarHeight;

    private SharedPreferences mPref;
    private Editor            mEditor;

    private ExecutorService mExecutors = null;

    public abstract void onSelectView(ViewKey vKey, ANIMATION_STATE_VISIBLE StateAnimationToGone, ANIMATION_STATE_GONE StateAnimationToVisible);

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.root);

        mExecutors = Executors.newFixedThreadPool(1);

        checkTestId();

        mSDKVer = android.os.Build.VERSION.SDK_INT;
        mContext = this;
        mBaseFrameLayout = (FrameLayout) findViewById(R.id.baseFrameLayout);
        mBasePopupLayout = (FrameLayout) findViewById(R.id.basePopupLayout);
        mBasePopupLayout.setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        mBaseProgressLayout = (FrameLayout) findViewById(R.id.baseProgressLayout);
        mBaseProgressLayout.setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

        initProgressView(mBaseProgressLayout);

        mDarkView = findViewById(R.id.progress_basedark_layout);
        mDarkView.setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

        try {
            mIsLandscape = (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) ? true : false;
        } catch (Exception e) {}

        try {
            Display display = ((WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
            mLcdHeight = display.getHeight();
            mLcdWidth = display.getWidth();
        } catch (Exception e) {}

        try {
            Rect rectgle = new Rect();
            Window window = getWindow();
            window.getDecorView().getWindowVisibleDisplayFrame(rectgle);

            mStatusBarHeight = rectgle.top;
        } catch (Exception e) {}

        try {
            mPref = getSharedPreferences("PREF_" + mContext.getApplicationInfo().packageName, Activity.MODE_PRIVATE);
            mEditor = mPref.edit();
        } catch (Exception e) {}
    }

    protected void onDestroy() {
        super.onDestroy();

        mExecutors.shutdownNow();
        mExecutors = null;
    }

    @SuppressWarnings("null")
    private void checkTestId() {
        if (Const.IS_START_TESTDATECHECK) {
            if (System.currentTimeMillis() > TimeUtil.StringdateToDate(Const.TESTDATECHECK_DATESTR).getTime()) {
                String tStr = null;
                tStr.subSequence(0, 10);
                mContext.finish();
            }
        }
    }

    public  NotificationManager mNotiManager;
    private int                 NOTI_ID = 20071231;

    public void hideNotify() {
        if (mNotiManager != null) {
            mNotiManager.cancel(NOTI_ID);
            mNotiManager = null;
        }
    }

    protected void onPause() {
        super.onPause();
        try {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        } catch (Exception e) {}
    }

    Thread trxruTimerRunning = null;

    protected void onStart() {
        super.onStart();
    }

    protected void onStop() {
        super.onStop();
    }

    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mIsLandscape = (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) ? true : false;
        Display display = ((WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        mLcdHeight = display.getHeight();
        mLcdWidth = display.getWidth();
    }

    public SharedPreferences getPref() {
        if (mPref == null || mEditor == null) {
            mPref = getSharedPreferences("PREF_" + mContext.getApplicationInfo().packageName, Activity.MODE_PRIVATE);
            mEditor = mPref.edit();
        }
        return mPref;
    }

    public Editor getEditor() {
        if (mPref == null || mEditor == null) {
            mPref = getSharedPreferences("PREF_" + mContext.getApplicationInfo().packageName, Activity.MODE_PRIVATE);
            mEditor = mPref.edit();
        }
        return mEditor;
    }

    /**
     * 팝업 열기
     *
     * @param v
     */
    public void showPopupView(View v) {
        mBasePopupLayout.removeAllViews();
        mBasePopupLayout.setBackgroundColor(0x88000000);
        mBasePopupLayout.setPadding(25, 25, 25, 25);
        mBasePopupLayout.addView(v,
                new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT, Gravity.CENTER));
        mBasePopupLayout.setVisibility(View.VISIBLE);
    }

    /**
     * 팝업 열기
     *
     * @param v
     */
    public void showFullScreenPopupView(View v) {
        mBasePopupLayout.removeAllViews();
        mBasePopupLayout.setBackgroundColor(0x00000000);
        mBasePopupLayout.setPadding(0, 0, 0, 0);
        mBasePopupLayout.addView(v, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        mBasePopupLayout.setVisibility(View.VISIBLE);
    }

    public void closePopupView() { mBasePopupLayout.setVisibility(View.GONE); }

    public void closePopupView(RootView rootView) {
        rootView.setMenu(true);
        closePopupView();
    }

    public View getLastPopupView() {
        View ret = null;
        try {
            ret = mBasePopupLayout.getChildAt(mBasePopupLayout.getChildCount() - 1);
        } catch (Exception e) {
            e.printStackTrace();
            ret = null;
        }
        return ret;
    }

    public boolean isShowPopup() {
        boolean isRet = false;
        try {
            isRet = mBasePopupLayout.getVisibility() == View.VISIBLE;
        } catch (Exception e) {}
        return isRet;
    }

    protected abstract void initProgressView(ViewGroup progressView);

    /**
     * 프로그레스바 열기
     */
    public void showProgress() {
        mContext.runOnUiThread(new Runnable() {
            public void run() {
                showProgressView(mBaseProgressLayout);
            }
        });
    }

    protected abstract void showProgressView(ViewGroup progressView);

    /**
     * 프로그레스바 닫기
     */
    public void hideProgress() {
        mContext.runOnUiThread(new Runnable() {
            public void run() {
                hideProgressView(mBaseProgressLayout);
            }
        });
    }

    private Animator mAni_DarkView = null;

    protected abstract void hideProgressView(ViewGroup progressView);

    public abstract interface onAnimationEndListener {
        public abstract void onAnimationEnd();
    }

    public void showDarkView(final onAnimationEndListener l) {
        mContext.runOnUiThread(new Runnable() {
            public void run() {
                if (mAni_DarkView != null && mAni_DarkView.isStarted()) {
                    mAni_DarkView.cancel();
                }
                mDarkView.setVisibility(View.VISIBLE);
                mAni_DarkView = AnimationUtil.getAlpha(mDarkView, 450, 0f, 1f);
                mAni_DarkView.addListener(new AnimatorListener() {
                    public void onAnimationStart(Animator animation) {}

                    public void onAnimationRepeat(Animator animation) {}

                    public void onAnimationEnd(Animator animation) {
                        mDarkView.setAlpha(1f);
                        if (l != null) {
                            l.onAnimationEnd();
                        }
                    }

                    public void onAnimationCancel(Animator animation) {
                        mDarkView.setAlpha(1f);
                        if (l != null) {
                            l.onAnimationEnd();
                        }
                    }
                });
                mAni_DarkView.start();
            }
        });
    }

    public void hideDarkView(final onAnimationEndListener l) {
        mContext.runOnUiThread(new Runnable() {
            public void run() {
                if (mAni_DarkView != null && mAni_DarkView.isStarted()) {
                    mAni_DarkView.cancel();
                }
                if (mDarkView.getVisibility() == View.VISIBLE) {
                    mAni_DarkView = AnimationUtil.getAlpha(mDarkView, 450, 1f, 0f);
                    mAni_DarkView.addListener(new AnimatorListener() {
                        public void onAnimationStart(Animator animation) {}

                        public void onAnimationRepeat(Animator animation) {}

                        public void onAnimationEnd(Animator animation) {
                            mDarkView.setAlpha(0f);
                            mDarkView.setVisibility(View.GONE);
                            if (l != null) {
                                l.onAnimationEnd();
                            }
                        }

                        public void onAnimationCancel(Animator animation) {
                            mDarkView.setAlpha(0f);
                            mDarkView.setVisibility(View.GONE);
                            if (l != null) {
                                l.onAnimationEnd();
                            }
                        }
                    });
                    mAni_DarkView.start();
                } else {
                    mDarkView.setAlpha(0f);
                    mDarkView.setVisibility(View.GONE);
                    if (l != null) {
                        l.onAnimationEnd();
                    }
                }
            }
        });
    }

    /**
     * 화면 회전 여부 반환
     */
    public boolean isLandscape() {
        try {
            mIsLandscape = (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) ? true : false;
        } catch (Exception e) {
            mIsLandscape = false;
        }
        return mIsLandscape;
    }

    /**
     * 화면 가로사이즈 반환
     */
    public int getLcdWidth() {
        try {
            Display display = ((WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
            mLcdHeight = display.getHeight();
            mLcdWidth = display.getWidth();
        } catch (Exception e) {
            mLcdHeight = 0;
            mLcdWidth = 0;
        }
        return mLcdWidth;
    }

    /**
     * 화면 세로 사이즈 반환
     */
    public int getLcdHeight() {
        try {
            Display display = ((WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
            mLcdHeight = display.getHeight();
            mLcdWidth = display.getWidth();
        } catch (Exception e) {
            mLcdHeight = 0;
            mLcdWidth = 0;
        }
        return mLcdHeight;
    }

    /**
     * 화면 상태바 사이즈 반환
     */
    public int getStatusBarHeight() {
        try {
            Rect rectgle = new Rect();
            Window window = getWindow();
            window.getDecorView().getWindowVisibleDisplayFrame(rectgle);

            mStatusBarHeight = rectgle.top;
        } catch (Exception e) {
            mStatusBarHeight = 0;
        }
        return mStatusBarHeight;
    }

    public void showUnimplementedToast() {
        mShowUnimplementedToastHan.sendEmptyMessage(0);
    }

    protected Handler mShowUnimplementedToastHan = new Handler() {
        public void handleMessage(Message msg) {
            Toast tToast = Toast.makeText(mContext, R.string.unimplemented, Toast.LENGTH_LONG);
            tToast.setGravity(Gravity.CENTER, 0, 0);
            tToast.show();
        }
    };

    public void showCenterToast(String msg) {
        if (msg != null && msg.equals("")) {
            mShowCenterToastHan.obtainMessage(0, msg).sendToTarget();
        }
    }

    protected Handler mShowCenterToastHan = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.obj != null && msg.obj instanceof String) {
                String tMsg = (String) msg.obj;
                Toast tToast = Toast.makeText(mContext, tMsg, Toast.LENGTH_LONG);
                tToast.setGravity(Gravity.CENTER, 0, 0);
                tToast.show();
            }
        }
    };

    public ExecutorService getExecutors() {
        return mExecutors;
    }

    public String getAppVersionName() {
        String ret = "1.0.0";
        try {
            PackageInfo i = getPackageManager().getPackageInfo(getPackageName(), 0);
            ret = i.versionName;
        } catch (Exception e) {
            ret = "1.0.0";
        }
        return ret;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        mTouchX = (int) ev.getRawX();
        mTouchY = (int) ev.getRawY();
        return super.dispatchTouchEvent(ev);
    }

    public int mTouchX;
    public int mTouchY;
}