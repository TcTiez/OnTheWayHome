package com.tctiez.onthewayhome.base;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.AnimatorSet;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.tctiez.onthewayhome.util.AnimationUtil;

import java.util.ArrayList;

/**
 * Created by Eugene J. Jeon on 2015-08-14.
 */
public abstract class BaseView extends FrameLayout {
    public static final ANIMATION_STATE_VISIBLE VISIBLE_ANIMATION = ANIMATION_STATE_VISIBLE.NONE;

    public static final int ANIMATION_FAST_DURATION     = 100;
    public static final int ANIMATION_DEFAULT_DURATION  = 250;
    public static final int ANIMATION_SLOW_DURATION     = 300;
    public static final int ANIMATION_VERYSLOW_DURATION = 900;

    public static final int ANIMATIONEND_ACTION_START  = 1;
    public static final int ANIMATIONEND_ACTION_END    = 0;
    public static final int ANIMATIONEND_ACTION_CANCEL = 2;

    public ANIMATION_STATE_VISIBLE mAnistate_Visible = ANIMATION_STATE_VISIBLE.NONE;

    private Animator mAni_ViewAnimator = null;

    protected Context        mContext;
    protected BaseActivity   mActivity;
    protected LayoutInflater mInflater;
    protected Object         mSetObject;
    protected ReturnData     mReturnData;

    protected SharedPreferences mPref   = null;
    protected Editor            mEditor = null;

    public BaseView(BaseActivity context) {
        super(context);
        mActivity = context;
        mContext = context;
        mInflater = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        mPref = mActivity.getPref();
        mEditor = mActivity.getEditor();
    }

    public void onCallStateChanged(int state, String incomingNumber) {}

    public void onResume() {}

    public void onPause() {}

    public void onDestroy() {}

    public void onConfigurationChanged(Configuration newConfig) {}

    public void onActivityResult(int requestCode, int resultCode, Intent data) {}

    public boolean onKeyDownEvent(int keyCode, KeyEvent event) {
        return false;
    }

    public boolean onKeyUpEvent(int keyCode, KeyEvent event) {
        return false;
    }

    public boolean onBackPressed() { return false; }

    public void setData(Object tObj, ReturnData tReturnData) {
        mSetObject = tObj;
        mReturnData = tReturnData;
    }

    public void setReturnData(ReturnData tReturnData) {
        mReturnData = tReturnData;
    }

    public abstract void initView();

    public void gonePreView(BaseView _BView_PreView) {
        if (_BView_PreView != null) {
            _BView_PreView.setVisibility(View.GONE);
        }
    }

    public void onAnimationStart() {}

    public void onAnimationComplete() {}

    public void initAnimationInfo() {
        BaseView.this.setX(0f);
        BaseView.this.setY(0f);
        BaseView.this.setAlpha(1f);
        BaseView.this.setTranslationX(0f);
        BaseView.this.setTranslationY(0f);
        BaseView.this.setRotation(0f);
        BaseView.this.setRotationX(0f);
        BaseView.this.setRotationY(0f);
        BaseView.this.setScaleX(1f);
        BaseView.this.setScaleY(1f);
    }

    public abstract interface ReturnData {
        public abstract void returnData(Object tObj);
    }

    public abstract interface onViewAnimationEndListener {
        public abstract void onViewAnimationEnd(int action);
    }

    public static enum ANIMATION_STATE_VISIBLE {
        NONE(0), IN_NOTE_NONEANI(100000), IN_LEFT(100001), IN_UP(100002), IN_DOWN(100003), IN_DOWN_FIDE(100004), IN_LEFT_ZOOM(100005), IN_UP_ZOOM(
                100006), IN_UP_FIDE(100007), IN_LEFT_ZOOM_FIDE_SLOW(100008), IN_UP_ZOOM_FADE(100009);

        private int value;

        ANIMATION_STATE_VISIBLE(int value) {
            this.value = value;
        }

        public int get() {
            return value;
        }

        public static ANIMATION_STATE_VISIBLE getValueToState(int value) {
            ANIMATION_STATE_VISIBLE ret = ANIMATION_STATE_VISIBLE.NONE;
            switch (value) {
                case 100000:
                    ret = ANIMATION_STATE_VISIBLE.IN_NOTE_NONEANI;
                    break;
                case 100001:
                    ret = ANIMATION_STATE_VISIBLE.IN_LEFT;
                    break;
                case 100002:
                    ret = ANIMATION_STATE_VISIBLE.IN_UP;
                    break;
                case 100003:
                    ret = ANIMATION_STATE_VISIBLE.IN_DOWN;
                    break;
                case 100004:
                    ret = ANIMATION_STATE_VISIBLE.IN_DOWN_FIDE;
                    break;
                case 100005:
                    ret = ANIMATION_STATE_VISIBLE.IN_LEFT_ZOOM;
                    break;
                case 100006:
                    ret = ANIMATION_STATE_VISIBLE.IN_UP_ZOOM;
                    break;
                case 100007:
                    ret = ANIMATION_STATE_VISIBLE.IN_UP_FIDE;
                    break;
                case 100008:
                    ret = ANIMATION_STATE_VISIBLE.IN_LEFT_ZOOM_FIDE_SLOW;
                    break;
                case 100009:
                    ret = ANIMATION_STATE_VISIBLE.IN_UP_ZOOM_FADE;
                    break;

                default:
                    ret = ANIMATION_STATE_VISIBLE.NONE;
                    break;
            }
            return ret;
        }

        public ANIMATION_STATE_GONE getMatchGoneState() {
            ANIMATION_STATE_GONE ret = ANIMATION_STATE_GONE.NONE;
            switch (ANIMATION_STATE_VISIBLE.this) {
                case IN_NOTE_NONEANI:
                    ret = ANIMATION_STATE_GONE.OUT_NOTE_NONEANI;
                    break;
                case IN_LEFT:
                    ret = ANIMATION_STATE_GONE.OUT_RIGHT;
                    break;
                case IN_UP:
                    ret = ANIMATION_STATE_GONE.OUT_DOWN;
                    break;
                case IN_DOWN:
                    ret = ANIMATION_STATE_GONE.OUT_UP;
                    break;
                case IN_DOWN_FIDE:
                    ret = ANIMATION_STATE_GONE.OUT_UP_FIDE;
                    break;
                case IN_LEFT_ZOOM:
                    ret = ANIMATION_STATE_GONE.OUT_RIGHT_ZOOM;
                    break;
                case IN_UP_ZOOM:
                    ret = ANIMATION_STATE_GONE.OUT_DOWN_ZOOM;
                    break;
                case IN_UP_FIDE:
                    ret = ANIMATION_STATE_GONE.OUT_DOWN_FIDE;
                    break;
                case IN_LEFT_ZOOM_FIDE_SLOW:
                    ret = ANIMATION_STATE_GONE.OUT_RIGHT_ZOOM_FIDE_SLOW;
                    break;
                case IN_UP_ZOOM_FADE:
                    ret = ANIMATION_STATE_GONE.OUT_DOWN_ZOOM_FIDE;
                    break;

                case NONE:
                default:
                    ret = ANIMATION_STATE_GONE.NONE;
                    break;

            }
            return ret;
        }
    }

    public static enum ANIMATION_STATE_GONE {
        NONE(0), OUT_NOTE_NONEANI(200000), OUT_RIGHT(200001), OUT_DOWN(200002), OUT_UP(200003), OUT_UP_FIDE(200004), OUT_RIGHT_ZOOM(
                200005), OUT_DOWN_ZOOM(200006), OUT_DOWN_FIDE(200007), OUT_RIGHT_ZOOM_FIDE_SLOW(200008), OUT_DOWN_ZOOM_FIDE(200009);

        private int value;

        ANIMATION_STATE_GONE(int value) {
            this.value = value;
        }

        public int get() {
            return value;
        }

        public static ANIMATION_STATE_GONE getValueToState(int value) {
            ANIMATION_STATE_GONE ret = ANIMATION_STATE_GONE.NONE;
            switch (value) {
                case 200000:
                    ret = ANIMATION_STATE_GONE.OUT_NOTE_NONEANI;
                    break;
                case 200001:
                    ret = ANIMATION_STATE_GONE.OUT_RIGHT;
                    break;
                case 200002:
                    ret = ANIMATION_STATE_GONE.OUT_DOWN;
                    break;
                case 200003:
                    ret = ANIMATION_STATE_GONE.OUT_UP;
                    break;
                case 200004:
                    ret = ANIMATION_STATE_GONE.OUT_UP_FIDE;
                    break;
                case 200005:
                    ret = ANIMATION_STATE_GONE.OUT_RIGHT_ZOOM;
                    break;
                case 200006:
                    ret = ANIMATION_STATE_GONE.OUT_DOWN_ZOOM;
                    break;
                case 200007:
                    ret = ANIMATION_STATE_GONE.OUT_DOWN_FIDE;
                    break;
                case 200008:
                    ret = ANIMATION_STATE_GONE.OUT_RIGHT_ZOOM_FIDE_SLOW;
                    break;
                case 200009:
                    ret = ANIMATION_STATE_GONE.OUT_DOWN_ZOOM_FIDE;
                    break;

                default:
                    ret = ANIMATION_STATE_GONE.NONE;
                    break;
            }
            return ret;
        }

        public ANIMATION_STATE_VISIBLE getMatchGoneState() {
            ANIMATION_STATE_VISIBLE ret = ANIMATION_STATE_VISIBLE.NONE;
            switch (ANIMATION_STATE_GONE.this) {
                case OUT_NOTE_NONEANI:
                    ret = ANIMATION_STATE_VISIBLE.IN_NOTE_NONEANI;
                    break;
                case OUT_RIGHT:
                    ret = ANIMATION_STATE_VISIBLE.IN_LEFT;
                    break;
                case OUT_DOWN:
                    ret = ANIMATION_STATE_VISIBLE.IN_UP;
                    break;
                case OUT_UP:
                    ret = ANIMATION_STATE_VISIBLE.IN_DOWN;
                    break;
                case OUT_UP_FIDE:
                    ret = ANIMATION_STATE_VISIBLE.IN_DOWN_FIDE;
                    break;
                case OUT_RIGHT_ZOOM:
                    ret = ANIMATION_STATE_VISIBLE.IN_LEFT_ZOOM;
                    break;
                case OUT_DOWN_ZOOM:
                    ret = ANIMATION_STATE_VISIBLE.IN_UP_ZOOM;
                    break;
                case OUT_DOWN_FIDE:
                    ret = ANIMATION_STATE_VISIBLE.IN_UP_FIDE;
                    break;
                case OUT_RIGHT_ZOOM_FIDE_SLOW:
                    ret = ANIMATION_STATE_VISIBLE.IN_LEFT_ZOOM_FIDE_SLOW;
                    break;
                case OUT_DOWN_ZOOM_FIDE:
                    ret = ANIMATION_STATE_VISIBLE.IN_UP_ZOOM_FADE;
                    break;

                case NONE:
                default:
                    ret = ANIMATION_STATE_VISIBLE.NONE;
                    break;

            }
            return ret;
        }
    }

    public Animator startAnimationToVisible(ANIMATION_STATE_VISIBLE State, BaseView tGoneView, final onViewAnimationEndListener l) {
        mAnistate_Visible = State;
        if (mAni_ViewAnimator != null && mAni_ViewAnimator.isStarted()) {
            mAni_ViewAnimator.cancel();
        }
        initAnimationInfo();
        ArrayList<Animator> tAniArr = new ArrayList<Animator>();

        switch (State) {
            case IN_LEFT:
                tAniArr.add(AnimationUtil.getTranslationX(BaseView.this, ANIMATION_DEFAULT_DURATION, mActivity.getLcdWidth(), 0));
                break;

            case IN_UP_FIDE:
                tAniArr.add(AnimationUtil.getAlpha(BaseView.this, ANIMATION_SLOW_DURATION, 0, 1f));
            case IN_UP:
                tAniArr.add(AnimationUtil.getTranslationY(BaseView.this, ANIMATION_SLOW_DURATION,
                        mActivity.getLcdHeight() + mActivity.getStatusBarHeight(), 0));
                break;

            case IN_DOWN_FIDE:
                tAniArr.add(AnimationUtil.getAlpha(BaseView.this, ANIMATION_SLOW_DURATION, 0, 1f));
            case IN_DOWN:
                tAniArr.add(AnimationUtil.getTranslationY(BaseView.this, ANIMATION_SLOW_DURATION,
                        -(mActivity.getLcdHeight() + mActivity.getStatusBarHeight()), 0));
                break;

            case IN_LEFT_ZOOM:
                tAniArr.add(AnimationUtil.getTranslationX(BaseView.this, ANIMATION_DEFAULT_DURATION, mActivity.getLcdWidth(), 0));
                if (tGoneView != null) {
                    tGoneView.setPivotX(tGoneView.getWidth() / 2);
                    tGoneView.setPivotY(tGoneView.getHeight() / 2);
                    tAniArr.add(AnimationUtil.getScaleX(tGoneView, ANIMATION_DEFAULT_DURATION, 1f, 0.6f));
                    tAniArr.add(AnimationUtil.getScaleY(tGoneView, ANIMATION_DEFAULT_DURATION, 1f, 0.6f));
                }
                break;

            case IN_UP_ZOOM: {
            /*
             * AnimatorSet tAni_Set = new AnimatorSet(); ObjectAnimator tAni1 =
             * AnimationUtil.getTranslationY(BaseView.this,
             * ANIMATION_DEFAULT_DURATION, mActivity.getLcdHeight() +
             * mActivity.getStatusBarHeight(), 0); if (tGoneView != null) {
             * tGoneView.setPivotX(tGoneView.getWidth() / 2);
             * tGoneView.setPivotY(tGoneView.getHeight() / 2); ObjectAnimator
             * tAni2 = AnimationUtil.getScaleX(tGoneView,
             * ANIMATION_DEFAULT_DURATION, 1f, 0.6f); ObjectAnimator tAni3 =
             * AnimationUtil.getScaleY(tGoneView, ANIMATION_DEFAULT_DURATION,
             * 1f, 0.6f); tAni_Set.playTogether(tAni1, tAni2, tAni3); } else {
             * tAni_Set.playTogether(tAni1); } mAni_ViewAnimator = tAni_Set;
             */

                float height = mActivity.getLcdHeight() + mActivity.getStatusBarHeight();
                float heightsub = height / 10;

                float[] tFloatArr = {
                        height, heightsub * 8, heightsub * 6, heightsub * 4, heightsub * 3, heightsub * 2, (float) (heightsub * 1.5), heightsub * 1,
                        (float) (heightsub * 0.5), 0
                };
                tAniArr.add(AnimationUtil.getTranslationY(BaseView.this, ANIMATION_DEFAULT_DURATION, tFloatArr));
                break;
            }

            case IN_NOTE_NONEANI:
                tAniArr.add(AnimationUtil.getTranslationY(BaseView.this, 10, mActivity.getLcdHeight() + mActivity.getStatusBarHeight(), 0));
                break;

            case IN_LEFT_ZOOM_FIDE_SLOW: {
                tAniArr.add(AnimationUtil.getAlpha(BaseView.this, ANIMATION_SLOW_DURATION, 0f, 1f));
                tAniArr.add(AnimationUtil.getTranslationX(BaseView.this, ANIMATION_SLOW_DURATION, mActivity.getLcdWidth(), 0f));

                if (tGoneView != null) {
                    tGoneView.setPivotX(tGoneView.getWidth() / 2);
                    tGoneView.setPivotY(tGoneView.getHeight() / 2);
                    tAniArr.add(AnimationUtil.getAlpha(tGoneView, ANIMATION_SLOW_DURATION, 1f, 0f));
                    tAniArr.add(AnimationUtil.getScaleX(tGoneView, ANIMATION_SLOW_DURATION, 1f, 0.3f));
                    tAniArr.add(AnimationUtil.getScaleY(tGoneView, ANIMATION_SLOW_DURATION, 1f, 0.3f));
                }

                break;
            }

            case IN_UP_ZOOM_FADE: {
                float height = mActivity.getLcdHeight() + mActivity.getStatusBarHeight();
                float heightsub = height / 10;

                float[] tFloatArr = {
                        height, heightsub * 8, heightsub * 6, heightsub * 4, heightsub * 3, heightsub * 2, (float) (heightsub * 1.5), heightsub * 1,
                        (float) (heightsub * 0.5), 0
                };

                tAniArr.add(AnimationUtil.getAlpha(BaseView.this, ANIMATION_SLOW_DURATION, 0, 1f));
                tAniArr.add(AnimationUtil.getTranslationY(BaseView.this, ANIMATION_DEFAULT_DURATION, tFloatArr));

                break;
            }

            case NONE:
            default:
                tAniArr.clear();
                break;
        }

        if (tAniArr.size() > 0) {
            AnimatorSet tAniSet = new AnimatorSet();
            tAniSet.playTogether(tAniArr);
            mAni_ViewAnimator = tAniSet;
            mAni_ViewAnimator.addListener(new AnimatorListener() {
                public void onAnimationStart(Animator animation) {}

                public void onAnimationRepeat(Animator animation) {}

                public void onAnimationEnd(Animator animation) {
                    if (l != null) {
                        l.onViewAnimationEnd(ANIMATIONEND_ACTION_END);
                    }
                }

                public void onAnimationCancel(Animator animation) {
                    initAnimationInfo();
                    if (l != null) {
                        l.onViewAnimationEnd(ANIMATIONEND_ACTION_CANCEL);
                    }
                }
            });
            mAni_ViewAnimator.start();
        } else {
            setVisibility(View.VISIBLE);
            if (l != null) {
                l.onViewAnimationEnd(ANIMATIONEND_ACTION_END);
            }
        }

        return mAni_ViewAnimator;
    }

    public Animator startAnimationToGone(onViewAnimationEndListener l, BaseView tGoneView) {
        return startAnimationToGone(mAnistate_Visible.getMatchGoneState(), tGoneView, l);
    }

    public Animator startAnimationToGone(ANIMATION_STATE_GONE State, BaseView tGoneView, final onViewAnimationEndListener l) {
        ArrayList<Animator> tAniArr = new ArrayList<Animator>();

        switch (State) {
            case OUT_RIGHT:
                tAniArr.add(AnimationUtil.getTranslationX(BaseView.this, ANIMATION_DEFAULT_DURATION, 0, mActivity.getLcdWidth()));
                break;

            case OUT_DOWN_FIDE:
                tAniArr.add(AnimationUtil.getAlpha(BaseView.this, ANIMATION_SLOW_DURATION, 1f, 0f));
            case OUT_DOWN:
                tAniArr.add(AnimationUtil.getTranslationY(BaseView.this, ANIMATION_SLOW_DURATION, 0,
                        mActivity.getLcdHeight() + mActivity.getStatusBarHeight()));
                break;

            case OUT_UP_FIDE:
                tAniArr.add(AnimationUtil.getAlpha(BaseView.this, ANIMATION_SLOW_DURATION, 1f, 0f));
            case OUT_UP:
                tAniArr.add(AnimationUtil.getTranslationY(BaseView.this, ANIMATION_SLOW_DURATION, 0,
                        -(mActivity.getLcdHeight() + mActivity.getStatusBarHeight())));
                break;

            case OUT_RIGHT_ZOOM:
                tAniArr.add(AnimationUtil.getTranslationX(BaseView.this, ANIMATION_DEFAULT_DURATION, 0, mActivity.getLcdWidth()));
                if (tGoneView != null) {
                    tGoneView.setPivotX(tGoneView.getWidth() / 2);
                    tGoneView.setPivotY(tGoneView.getHeight() / 2);
                    tAniArr.add(AnimationUtil.getScaleX(tGoneView, ANIMATION_DEFAULT_DURATION, 0.6f, 1f));
                    tAniArr.add(AnimationUtil.getScaleY(tGoneView, ANIMATION_DEFAULT_DURATION, 0.6f, 1f));
                }
                break;

            case OUT_DOWN_ZOOM: {
            /*
             * AnimatorSet tAni_Set = new AnimatorSet(); ObjectAnimator tAni1 =
             * AnimationUtil.getTranslationY(BaseView.this,
             * ANIMATION_DEFAULT_DURATION, 0, mActivity.getLcdHeight() +
             * mActivity.getStatusBarHeight()); if (tGoneView != null) {
             * tGoneView.setPivotX(tGoneView.getWidth() / 2);
             * tGoneView.setPivotY(tGoneView.getHeight() / 2); ObjectAnimator
             * tAni2 = AnimationUtil.getScaleX(tGoneView,
             * ANIMATION_DEFAULT_DURATION, 0.6f, 1f); ObjectAnimator tAni3 =
             * AnimationUtil.getScaleY(tGoneView, ANIMATION_DEFAULT_DURATION,
             * 0.6f, 1f); tAni_Set.playTogether(tAni1, tAni2, tAni3); } else {
             * tAni_Set.playTogether(tAni1); } mAni_ViewAnimator = tAni_Set;
             */

                float height = mActivity.getLcdHeight() + mActivity.getStatusBarHeight();
                float heightsub = height / 10;

                float[] tFloatArr = {
                        0, heightsub * 2, heightsub * 4, heightsub * 6, heightsub * 7, heightsub * 8, (float) (heightsub * 8.5), (float) (heightsub * 9),
                        (float) (heightsub * 9.5), height
                };
                tAniArr.add(AnimationUtil.getTranslationY(BaseView.this, ANIMATION_DEFAULT_DURATION, tFloatArr));
                break;
            }

            case OUT_NOTE_NONEANI:
                tAniArr.add(AnimationUtil.getTranslationY(BaseView.this, ANIMATION_DEFAULT_DURATION, 0,
                        mActivity.getLcdHeight() + mActivity.getStatusBarHeight()));
                break;

            case OUT_RIGHT_ZOOM_FIDE_SLOW: {
                tAniArr.add(AnimationUtil.getAlpha(BaseView.this, ANIMATION_SLOW_DURATION, 1f, 0f));
                tAniArr.add(AnimationUtil.getTranslationX(BaseView.this, ANIMATION_SLOW_DURATION, 0f, mActivity.getLcdWidth()));

                if (tGoneView != null) {
                    tGoneView.setPivotX(tGoneView.getWidth() / 2);
                    tGoneView.setPivotY(tGoneView.getHeight() / 2);
                    tAniArr.add(AnimationUtil.getAlpha(tGoneView, ANIMATION_SLOW_DURATION, 0f, 1f));
                    tAniArr.add(AnimationUtil.getScaleX(tGoneView, ANIMATION_SLOW_DURATION, 0.3f, 1f));
                    tAniArr.add(AnimationUtil.getScaleY(tGoneView, ANIMATION_SLOW_DURATION, 0.3f, 1f));
                }

                break;
            }

            case OUT_DOWN_ZOOM_FIDE: {
                float height = mActivity.getLcdHeight() + mActivity.getStatusBarHeight();
                float heightsub = height / 10;

                float[] tFloatArr = {
                        0, heightsub * 2, heightsub * 4, heightsub * 6, heightsub * 7, heightsub * 8, (float) (heightsub * 8.5), (float) (heightsub * 9),
                        (float) (heightsub * 9.5), height
                };

                tAniArr.add(AnimationUtil.getAlpha(BaseView.this, ANIMATION_SLOW_DURATION, 1f, 0f));
                tAniArr.add(AnimationUtil.getTranslationY(BaseView.this, ANIMATION_DEFAULT_DURATION, tFloatArr));

                break;
            }

            case NONE:
            default:
                tAniArr.clear();
                break;
        }

        if (tAniArr.size() > 0) {
            AnimatorSet tAniSet = new AnimatorSet();
            tAniSet.playTogether(tAniArr);
            mAni_ViewAnimator = tAniSet;
            mAni_ViewAnimator.addListener(new AnimatorListener() {
                public void onAnimationStart(Animator animation) {}

                public void onAnimationRepeat(Animator animation) {}

                public void onAnimationEnd(Animator animation) {
                    if (l != null) {
                        l.onViewAnimationEnd(ANIMATIONEND_ACTION_END);
                    }
                }

                public void onAnimationCancel(Animator animation) {
                    initAnimationInfo();
                    if (l != null) {
                        l.onViewAnimationEnd(ANIMATIONEND_ACTION_CANCEL);
                    }
                }
            });
            mAni_ViewAnimator.start();
        } else {
            setVisibility(View.GONE);
            if (l != null) {
                l.onViewAnimationEnd(ANIMATIONEND_ACTION_END);
            }
        }

        return mAni_ViewAnimator;
    }
}
