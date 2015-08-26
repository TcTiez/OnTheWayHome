package com.tctiez.onthewayhome.base;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;

import com.tctiez.onthewayhome.util.AnimationUtil;

/**
 * Created by Eugene J. Jeon on 2015-08-14.
 */
public abstract class BasePopup extends FrameLayout {
    public static final int POPUPACTION_CANCEL = 0;
    public static final int POPUPACTION_OK     = 1;

    protected BaseActivity          mActivity;
    protected RootView              mRootView;
    protected onPopupActionListener mOnPopupActionListener = null;
    protected Object                mData;

    public BasePopup(BaseActivity context, RootView tParentView) {
        super(context);
        mActivity = context;
        mRootView = tParentView;
        mRootView.setMenu(false);
    }

    public void onPopupAction(int action, Object ret) {
        //mRootView.closePopupView();
        if (mOnPopupActionListener != null) {
            mOnPopupActionListener.onPopupAction(action, ret);
        }
    }

    public void closePopup() { onPopupAction(POPUPACTION_CANCEL, null); }

    public void setData(Object tData) {
        mData = tData;
    }

    public boolean onBackPressed() {
        return false;
    }

    public BasePopup setOnPopupActionListener(onPopupActionListener onPopupAction) {
        mOnPopupActionListener = onPopupAction;
        return this;
    }

    public void showPopup() {
        changeTheme();
    }

    public abstract void changeTheme();

    public abstract interface onPopupActionListener {
        public abstract void onPopupAction(int action, Object ret);
    }

    protected void initAnimationInfo() {
        BasePopup.this.setX(0f);
        BasePopup.this.setY(0f);
        BasePopup.this.setAlpha(1f);
        BasePopup.this.setTranslationX(0f);
        BasePopup.this.setTranslationY(0f);
        BasePopup.this.setRotation(0f);
        BasePopup.this.setRotationX(0f);
        BasePopup.this.setRotationY(0f);
        BasePopup.this.setScaleX(1f);
        BasePopup.this.setScaleX(1f);
    }

    public void showPopupAnimation(View preView, final BaseView.onViewAnimationEndListener l) {
        Animator tAni = AnimationUtil.getAlpha(BasePopup.this, BaseView.ANIMATION_DEFAULT_DURATION, 0f, 1f);
        tAni.addListener(new AnimatorListener() {
            public void onAnimationStart(Animator animation) {}

            public void onAnimationRepeat(Animator animation) {}

            public void onAnimationEnd(Animator animation) {
                if (l != null) {
                    l.onViewAnimationEnd(BaseView.ANIMATIONEND_ACTION_END);
                }
            }

            public void onAnimationCancel(Animator animation) {
                initAnimationInfo();
                if (l != null) {
                    l.onViewAnimationEnd(BaseView.ANIMATIONEND_ACTION_CANCEL);
                }
            }
        });
        tAni.start();
    }

    public void hidePopupAnimation(View preView, final BaseView.onViewAnimationEndListener l) {
        Animator tAni = AnimationUtil.getAlpha(BasePopup.this, BaseView.ANIMATION_DEFAULT_DURATION, 1f, 0f);
        tAni.addListener(new AnimatorListener() {
            public void onAnimationStart(Animator animation) {}

            public void onAnimationRepeat(Animator animation) {}

            public void onAnimationEnd(Animator animation) {
                if (l != null) {
                    l.onViewAnimationEnd(BaseView.ANIMATIONEND_ACTION_END);
                }
            }

            public void onAnimationCancel(Animator animation) {
                initAnimationInfo();
                if (l != null) {
                    l.onViewAnimationEnd(BaseView.ANIMATIONEND_ACTION_CANCEL);
                }
            }
        });
        tAni.start();
    }
}
