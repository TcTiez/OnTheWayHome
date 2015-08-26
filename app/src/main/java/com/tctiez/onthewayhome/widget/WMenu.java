package com.tctiez.onthewayhome.widget;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;

import com.tctiez.onthewayhome.R;
import com.tctiez.onthewayhome.base.BaseActivity;
import com.tctiez.onthewayhome.base.BasePopup;
import com.tctiez.onthewayhome.base.BaseView;
import com.tctiez.onthewayhome.base.RootView;
import com.tctiez.onthewayhome.popup.PSearch;

/**
 * Created by Eugene J. Jeon on 2015-08-14.
 */
public class WMenu extends BaseView implements OnClickListener {
    private BaseActivity    mContext    = null;
    private RootView        mParentView = null;

    private FrameLayout     mRoot       = null;

    public WMenu(BaseActivity context, RootView tParentView) {
        super(context);
        mContext = context;
        mParentView = tParentView;

        View v = View.inflate(context, R.layout.widget_menu, null);
        addView(v, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

        mRoot = (FrameLayout) v.findViewById(R.id.lay_root);
        mRoot.setSoundEffectsEnabled(false);
        mRoot.setOnClickListener(this);

        // View Find & new

        // TODO: YOU SHOULD WRITE, ADD BUTTON.
        // Set Listener
        mRoot.findViewById(R.id.menu_dark_screen).setOnClickListener(this);
        mRoot.findViewById(R.id.btn_exit).setOnClickListener(this);
        mRoot.findViewById(R.id.btn_search).setOnClickListener(this);
        mRoot.findViewById(R.id.btn_station_list).setOnClickListener(this);

        // Etc Setting
    }

    @Override
    public void initView() {}

    @Override
    public void onClick(View v) {
        boolean isHideSidebar = true;

        switch (v.getId()) {
            case R.id.menu_dark_screen:
                break;

            case R.id.btn_exit:
                mActivity.finish();
                break;

            // TODO: TODO: YOU SHOULD WRITE, ADD BUTTON.
            // mParentView.addVisibleView(ChildViewKey.v_DashBoard, ANIMATION_STATE_VISIBLE.IN_LEFT, null);
            case R.id.btn_search:
                mParentView.showPopup(new PSearch(mContext, mParentView), null, new BasePopup.onPopupActionListener() {
                    @Override
                    public void onPopupAction(int action, Object ret) {
                        switch((int)ret) {
                            case PSearch.SEARCH:
                                mParentView.closePopupView();
                                break;

                            case PSearch.SEARCH_CANCEL:
                                mParentView.closePopupView();
                                break;
                        }
                    }
                });
                break;

            case R.id.btn_station_list:
                break;

            default:
                isHideSidebar = false;
                break;
        }

        if (isHideSidebar) {
            mParentView.hideMenu(true);
        }
    }
}
