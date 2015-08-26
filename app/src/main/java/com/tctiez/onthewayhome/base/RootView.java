package com.tctiez.onthewayhome.base;

import android.animation.Animator;
import android.view.View;
import android.widget.FrameLayout;

import com.tctiez.onthewayhome.VIntro;
import com.tctiez.onthewayhome.VMap;
import com.tctiez.onthewayhome.base.Const.ChildViewKey;
import com.tctiez.onthewayhome.R;
import com.tctiez.onthewayhome.util.CircularQueueHashMap;
import com.tctiez.onthewayhome.widget.WMenu;

import android.content.Intent;
import android.content.res.Configuration;
import android.view.KeyEvent;
import android.view.ViewGroup;

/**
 * Created by Eugene J. Jeon on 2015-08-14.
 */
public class RootView extends BaseView {

    /** 최근 사용한 뷰의 재사용을 위한 캐시역활 */
    CircularQueueHashMap<String, BaseView> mViewCache;

    /** 루트 뷰 */
    FrameLayout mRoot = null;

    /** 현재 화면에 출력중인 뷰 */
    BaseView mNowVisibleView = null;

    /** 메뉴 위젯 */
    WMenu mMenuView = null;
    private boolean mIsMenu = true;

    /** 뷰 에니메이션 관련 */
    @SuppressWarnings("unused")
    private Animator mAni_ViewAnimator = null;
    private boolean  mIs_ViewAnimation = false;

    public RootView(BaseActivity context) {
        super(context);

        View v = inflate(context, R.layout.root, null);
        addView(v, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

        mRoot = (FrameLayout) v.findViewById(R.id.baseFrameLayout);

        mViewCache = new CircularQueueHashMap<String, BaseView>(ChildViewKey.values().length);

        mMenuView = new WMenu(mActivity, RootView.this);
        ((FrameLayout) v.findViewById(R.id.root)).addView(mMenuView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        mMenuView.setVisibility(INVISIBLE);
        hideMenu(false);
    }

    public boolean isViewAnimation() {
        return mIs_ViewAnimation;
    }

    /**
     * initView
     */
    @Override
    public void initView() {
        // TODO: YOU SHOULD RE-WEITE, IF CHANGE MAIN ACTIVITY.
        BaseView tView = getChildView(ChildViewKey.VIntro);
        changeVisibleView(tView);
    }

    @Override
    public void gonePreView(BaseView _BView_PreView) {}

    @Override
    public void onCallStateChanged(int state, String incomingNumber) {
        super.onCallStateChanged(state, incomingNumber);
        if (mNowVisibleView != null && mNowVisibleView.getVisibility() == VISIBLE) {
            mNowVisibleView.onCallStateChanged(state, incomingNumber);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mNowVisibleView != null && mNowVisibleView.getVisibility() == VISIBLE) {
            mNowVisibleView.onResume();
        }
        mActivity.hideNotify();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mNowVisibleView != null && mNowVisibleView.getVisibility() == VISIBLE) {
            mNowVisibleView.onPause();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (mNowVisibleView != null && mNowVisibleView.getVisibility() == VISIBLE) {
            mNowVisibleView.onConfigurationChanged(newConfig);
        }
    }

    /**
     * setMenu
     *
     * @param value
     */
    public void setMenu(boolean value) {
        mIsMenu = value;
    }

    @Override
    public boolean onKeyDownEvent(int keyCode, KeyEvent event) {
        boolean isRet = false;
        if (mNowVisibleView != null && mNowVisibleView.getVisibility() == VISIBLE) {
            isRet = mNowVisibleView.onKeyDownEvent(keyCode, event);
        }

        if (isRet) {
            return true;
        } else {
            if (mIsMenu) {
                switch(keyCode) {
                    case KeyEvent.KEYCODE_MENU:
                        if (mMenuView.getVisibility() == VISIBLE) hideMenu(true);
                        else showMenu(true);
                        break;
                }
            }
            return super.onKeyDownEvent(keyCode, event);
        }
    }

    @Override
    public boolean onKeyUpEvent(int keyCode, KeyEvent event) {
        boolean isRet = false;
        if (mNowVisibleView != null && mNowVisibleView.getVisibility() == VISIBLE) {
            isRet = mNowVisibleView.onKeyUpEvent(keyCode, event);
        }

        if (isRet) {
            return true;
        } else {
            return super.onKeyUpEvent(keyCode, event);
        }
    }

    /**
     * onBackPressed
     */
    @Override
    public boolean onBackPressed() {
        boolean isRet = false;
        if (mIs_ViewAnimation) {
            isRet = true;
        } else if (mMenuView != null && mMenuView.getVisibility() == VISIBLE) {
            hideMenu(true);
            isRet = true;
        } else if (mNowVisibleView != null && mNowVisibleView.getVisibility() == VISIBLE) {
            if (mNowVisibleView instanceof VIntro) {
                isRet = mNowVisibleView.onBackPressed();
            } else {
                addVisibleView(ChildViewKey.VMap, ANIMATION_STATE_VISIBLE.IN_UP_ZOOM, null);
                isRet = true;
            }
        }
        return isRet;
    }

    /**
     * onActivityResult
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (mNowVisibleView != null && mNowVisibleView.getVisibility() == VISIBLE) {
            mNowVisibleView.onActivityResult(requestCode, resultCode, data);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void showMenu(boolean isAnimation) {
        mMenuView.setVisibility(VISIBLE);
        mAni_ViewAnimator = mMenuView.startAnimationToVisible(ANIMATION_STATE_VISIBLE.IN_UP_ZOOM_FADE, null, new onViewAnimationEndListener() {
            public void onViewAnimationEnd(int action) {
                mIs_ViewAnimation = false;
                if (ANIMATIONEND_ACTION_END == action) {}
                mMenuView.onAnimationComplete();
            }
        });
    }

    public void hideMenu(boolean isAnimation) {
        mAni_ViewAnimator = mMenuView.startAnimationToGone(ANIMATION_STATE_GONE.OUT_DOWN_ZOOM_FIDE, null, new onViewAnimationEndListener() {
            public void onViewAnimationEnd(int action) {
                mIs_ViewAnimation = false;
                if (ANIMATIONEND_ACTION_END == action) {
                    mMenuView.setVisibility(GONE);
                }
                mMenuView.onAnimationComplete();
            }
        });
    }

    /**
     * 모든 뷰를 제거하고 전달받은 뷰를 화면에 출력
     *
     * @param tView
     * @return 성공여부
     */
    public boolean changeVisibleView(BaseView tView) {
        return changeVisibleView(tView, null, null);
    }

    /**
     * 모든 뷰를 제거하고 전달받은 뷰를 화면에 출력
     *
     * @param tView
     * @param tObj
     * @param tReturnData
     * @return 성공여부
     */
    public boolean changeVisibleView(BaseView tView, Object tObj, ReturnData tReturnData) {
        boolean isRet = false;
        try {
            if (tView != null) {
                try {
                    ViewGroup tParentView = (ViewGroup) tView.getParent();
                    tParentView.removeView(tView);
                } catch (Throwable e) {}
                mRoot.removeAllViews();
                mRoot.addView(tView);
                mNowVisibleView = tView;
                mNowVisibleView.onAnimationStart();
                mNowVisibleView.setVisibility(VISIBLE);
                mNowVisibleView.setData(tObj, tReturnData);
                mNowVisibleView.initView();

                mNowVisibleView.onAnimationComplete();
                isRet = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            isRet = false;
        }
        return isRet;
    }

    /**
     * 전달한 뷰를 부모에게서 제거후 루트뷰 최상단에 표시
     *
     * @param tView
     * @return 성공여부
     */
    public boolean addVisibleView(BaseView tView, ANIMATION_STATE_VISIBLE State, final onViewAnimationEndListener l) {
        return addVisibleView(tView, null, null, State, l);
    }

    /**
     * 전달한 뷰를 부모에게서 제거후 루트뷰 최상단에 표시
     *
     * @param tView
     * @return 성공여부
     */
    public boolean addVisibleView(BaseView tView, Object tObj, ReturnData tReturnData, ANIMATION_STATE_VISIBLE State,
                                  final onViewAnimationEndListener l) {
        boolean isRet = false;
        if (!mIs_ViewAnimation) {
            try {
                if (tView != null) {
                    if (!tView.equals(mNowVisibleView)) {
                        mIs_ViewAnimation = true;
                        try {
                            ViewGroup tParentView = (ViewGroup) tView.getParent();
                            tParentView.removeView(tView);
                        } catch (Throwable e) {}
                        final BaseView tBView = mNowVisibleView;
                        mRoot.addView(tView);
                        mNowVisibleView = tView;
                        mNowVisibleView.onAnimationStart();
                        mNowVisibleView.setVisibility(VISIBLE);
                        mNowVisibleView.setData(tObj, tReturnData);
                        mNowVisibleView.initView();

                        mAni_ViewAnimator = mNowVisibleView.startAnimationToVisible(State, tBView, new onViewAnimationEndListener() {
                            public void onViewAnimationEnd(int action) {
                                mIs_ViewAnimation = false;
                                if (ANIMATIONEND_ACTION_END == action) {
                                    mNowVisibleView.gonePreView(tBView);
                                }
                                mNowVisibleView.onAnimationComplete();
                                if (l != null) {
                                    l.onViewAnimationEnd(action);
                                }
                            }
                        });
                        isRet = true;
                    } else {
                        mNowVisibleView.setData(tObj, tReturnData);
                        mNowVisibleView.initView();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                mIs_ViewAnimation = false;
                isRet = false;
            }
        }
        return isRet;
    }

    /**
     * 전달한 뷰를 부모에게서 제거후 루트뷰 최상단에 표시
     *
     * @param tChildViewKey
     * @return 성공여부
     */
    public boolean addVisibleView(ChildViewKey tChildViewKey, ANIMATION_STATE_VISIBLE State, onViewAnimationEndListener l) {
        BaseView tView = getChildView(tChildViewKey);
        return addVisibleView(tView, State, l);
    }

    /**
     * 전달한 뷰를 부모에게서 제거후 루트뷰 최상단에 표시
     *
     * @param tChildViewKey
     * @return 성공여부
     */
    public boolean addVisibleView(ChildViewKey tChildViewKey, Object tObj, ReturnData tReturnData, ANIMATION_STATE_VISIBLE State,
                                  onViewAnimationEndListener l) {
        BaseView tView = getChildView(tChildViewKey);
        return addVisibleView(tView, tObj, tReturnData, State, l);
    }

    /**
     * 마지막에 추가한 뷰를 제거<br>
     * <i>호출 => removeVisibleView(null, null);</i>
     */
    public boolean removeVisibleView() {
        return removeVisibleView(null, null);
    }

    /**
     * 마지막에 추가한 뷰를 제거<br>
     * <i>호출 => removeVisibleView(null, l);</i>
     */
    public boolean removeVisibleView(onViewAnimationEndListener l) {
        return removeVisibleView(null, l);
    }

    /**
     * 마지막에 추가한 뷰를 제거
     */
    public boolean removeVisibleView(ANIMATION_STATE_GONE State, final onViewAnimationEndListener l) {
        boolean isRet = false;
        if (!mIs_ViewAnimation) {
            try {
                if (mRoot.getChildCount() > 1) {
                    mIs_ViewAnimation = true;
                    final BaseView ret = (BaseView) mRoot.getChildAt(mRoot.getChildCount() - 2);
                    if (ret != null) {
                        ret.onAnimationStart();
                        ret.setVisibility(VISIBLE);
                    }

                    onViewAnimationEndListener tVael = new onViewAnimationEndListener() {
                        public void onViewAnimationEnd(int action) {
                            mIs_ViewAnimation = false;
                            if (ANIMATIONEND_ACTION_END == action) {
                                mRoot.removeView(mNowVisibleView);
                            }
                            mNowVisibleView = ret;
                            mNowVisibleView.onAnimationComplete();
                            if (l != null) {
                                l.onViewAnimationEnd(action);
                            }
                        }
                    };

                    if (State != null) {
                        mAni_ViewAnimator = mNowVisibleView.startAnimationToGone(State, ret, tVael);
                    } else {
                        mAni_ViewAnimator = mNowVisibleView.startAnimationToGone(tVael, ret);
                    }
                    isRet = true;
                }
            } catch (Exception e) {
                e.printStackTrace();
                mIs_ViewAnimation = false;
                isRet = false;
            }
        }
        return isRet;
    }

    public BaseView getVisibleView() {
        return mNowVisibleView;
    }

    public boolean showPopup(BasePopup popup, Object tData, BasePopup.onPopupActionListener onPopupAction) {
        boolean isRet = false;
        try {
            popup.setData(tData);
            popup.setOnPopupActionListener(onPopupAction);
            mActivity.showPopupView(popup);
            //mActivity.showFullScreenPopupView(popup);
            popup.showPopup();
            popup.showPopupAnimation(getVisibleView(), null);
            isRet = true;
        } catch (Exception e) {
            e.printStackTrace();
            isRet = false;
        }
        return isRet;
    }

    public boolean closePopupView() {
        mIsMenu = true;

        boolean isRet = mActivity.isShowPopup();
        View tPopup = mActivity.getLastPopupView();
        if (tPopup != null && tPopup instanceof BasePopup) {
            ((BasePopup) tPopup).hidePopupAnimation(getVisibleView(), new onViewAnimationEndListener() {
                public void onViewAnimationEnd(int action) {
                    mActivity.closePopupView();
                }
            });
        } else {
            //mActivity.closePopupView();
        }
        return isRet;
    }

    /**
     * 키에 해당하는 뷰를 리턴
     *
     * @param tChildViewKey
     * @return
     */
    public BaseView getChildView(ChildViewKey tChildViewKey) {
        BaseView ret = null;

        if (mViewCache.containKey(tChildViewKey.get())) {
            ret = mViewCache.get(tChildViewKey.get());
        }

        if (ret == null) {
            // TODO: YOU SHOULD WRITE, ADD VIEW.
            switch (tChildViewKey) {
                case VIntro:
                    ret = new VIntro(mActivity, RootView.this);
                    break;

                case VMap:
                    ret = new VMap(mActivity, RootView.this);
                    break;

                default:
                    ret = null;
                    break;
            }
        }

        if (ret != null) {
            mViewCache.put(tChildViewKey.get(), ret);
        }

        return ret;
    }

    public View getNowVisibleView() {
        return mNowVisibleView;
    }

    public BaseActivity getBaseActivity() {
        return mActivity;
    }
}
