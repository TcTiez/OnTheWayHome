package com.tctiez.onthewayhome;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout.LayoutParams;
import android.widget.Toast;

import com.tctiez.onthewayhome.base.BaseActivity;
import com.tctiez.onthewayhome.base.BasePopup;
import com.tctiez.onthewayhome.base.BaseView;
import com.tctiez.onthewayhome.base.BaseView.ANIMATION_STATE_GONE;
import com.tctiez.onthewayhome.base.BaseView.ANIMATION_STATE_VISIBLE;
import com.tctiez.onthewayhome.base.Const;
import com.tctiez.onthewayhome.base.Const.ViewKey;

import com.tctiez.onthewayhome.base.RootView;
import com.tctiez.onthewayhome.service.GPS;
import com.tctiez.onthewayhome.util.AnimationUtil;
import com.tctiez.onthewayhome.util.CircularQueueHashMap;

/**
 * Created by Eugene J. Jeon on 2015-08-14.
 */
public class MainActivity extends BaseActivity {
    /** 최근 사용한 뷰의 재사용을 위한 캐시역활 */
    CircularQueueHashMap<String, BaseView> mViewCache;
    /** 메인 뷰 */
    BaseView                              mMainViewCache = null;
    /** 현재 화면에 출력중인 뷰 */
    BaseView                              mSelectView    = null;

    private boolean mMenuPopup      = false;
    private long    mBackKeyTime    = 0;

    private Const.ViewKey mNowView = Const.ViewKey.VRoot;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mViewCache = new CircularQueueHashMap<String, BaseView>(10);

        new Handler() {
            public void handleMessage(Message msg) {
                onSelectView(mNowView, ANIMATION_STATE_VISIBLE.NONE, ANIMATION_STATE_GONE.NONE);
            }
        }.sendEmptyMessage(0);
    }

    protected void onResume() {
        super.onResume();
        try {
            mSelectView.onResume();
        } catch (Exception e) {}
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        try {
            mSelectView.onConfigurationChanged(newConfig);
        } catch (Exception e) {}
    }

    protected void onPause() {
        super.onPause();
        try {
            mSelectView.onPause();
        } catch (Exception e) {}
    }

    protected void onStop() {
        super.onStop();
    }

    public BaseView getSelectView() {
        return mSelectView;
    }

    public void onSelectView(ViewKey vKey, ANIMATION_STATE_VISIBLE StateAnimationToVisible, ANIMATION_STATE_GONE StateAnimationToGone) {
        if (vKey == null) {
            return;
        }

        BaseView tChangeView = null;
        if (mViewCache.containKey(vKey.get())) {
            tChangeView = mViewCache.get(vKey.get());
        } else {
            switch (vKey) {
                case VRoot:
                    tChangeView = new RootView(mContext);
                    break;
                default:
                    return;
            }
        }

        if (tChangeView == null) {
            return;
        }
        mNowView = vKey;
        mViewCache.put(tChangeView.getClass().getName(), tChangeView);

        mBaseFrameLayout.removeAllViews();
        if (mSelectView != null) {
            mBaseFrameLayout.addView(mSelectView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
            mSelectView.startAnimationToGone(StateAnimationToGone, null, null);
        }
        tChangeView.initView();
        mBaseFrameLayout.addView(tChangeView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        tChangeView.startAnimationToVisible(StateAnimationToVisible, null, null);
        mSelectView = tChangeView;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        boolean isRet = mSelectView.onKeyDownEvent(keyCode, event);

        if (isRet) {
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        boolean isRet = mSelectView.onKeyUpEvent(keyCode, event);

        if (isRet) {
            return true;
        } else {
            return super.onKeyUp(keyCode, event);
        }
    }

    public void onBackPressed() {
        if (mDarkView.getVisibility() == View.VISIBLE) {
            return;
        } else if (mBaseProgressLayout.getVisibility() == View.VISIBLE) {
            return;
        } else if (mBasePopupLayout.getVisibility() == View.VISIBLE) {
            try {
                View tView = mBasePopupLayout.getChildAt(0);
                if (tView instanceof BasePopup) {
                    if (!((BasePopup) tView).onBackPressed()) {
                        ((BasePopup) tView).closePopup();
                    }
                } else {
                    closePopupView();
                }
            } catch (Exception e) {
                closePopupView();
            }
            return;
        }

        boolean isRet = mSelectView.onBackPressed();
        if (isRet) {
            return;
        }

        switch (mNowView) {
            case VRoot:
                if (mBackKeyTime > (System.currentTimeMillis() - 2500)) {
                    finish();
                } else {
                    mBackKeyTime = System.currentTimeMillis();
                    Toast tToast = Toast.makeText(mContext, R.string.app_exit_toast, Toast.LENGTH_SHORT);
                    tToast.setGravity(Gravity.CENTER, 0, 0);
                    tToast.show();
                }
                return;
            default:
                break;
        }
        super.onBackPressed();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (mSelectView != null) {
            mSelectView.onActivityResult(requestCode, resultCode, data);
        }
    }

    /**
     * 앱 버전 체크
     *
     * @return
     */
    public String appVersion() {
        String version = "";
        try {
            PackageInfo pInfo = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0);
            version = pInfo.versionName;
            // version = Integer.toString(pInfo.versionCode);
        } catch (Exception e) {
            version = "0.0.0";
        }
        return version;
    }

    @Override
    protected void initProgressView(ViewGroup progressView) {}

    private Animator mAni_Progress = null;

    final float tF_Alpha = 0.9f;

    @Override
    protected void showProgressView(ViewGroup progressView) {
        try {
            if (mAni_Progress != null && mAni_Progress.isStarted()) {
                mAni_Progress.cancel();
            }

            progressView.setVisibility(View.VISIBLE);

            mAni_Progress = AnimationUtil.getAlpha(progressView, 350, 0.2f, 1f);

            mAni_Progress.addListener(new AnimatorListener() {
                public void onAnimationStart(Animator animation) {}

                public void onAnimationRepeat(Animator animation) {}

                public void onAnimationEnd(Animator animation) {}

                public void onAnimationCancel(Animator animation) {}
            });
            mAni_Progress.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void hideProgressView(final ViewGroup progressView) {
        try {
            if (mAni_Progress != null && mAni_Progress.isStarted()) {
                mAni_Progress.cancel();
            }

            mAni_Progress = AnimationUtil.getAlpha(progressView, 350, 1f, 0.2f);
            mAni_Progress.addListener(new AnimatorListener() {
                public void onAnimationStart(Animator animation) {}

                public void onAnimationRepeat(Animator animation) {}

                public void onAnimationEnd(Animator animation) {
                    progressView.setVisibility(View.GONE);
                }

                public void onAnimationCancel(Animator animation) {
                    progressView.setVisibility(View.GONE);
                }
            });
            mAni_Progress.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
