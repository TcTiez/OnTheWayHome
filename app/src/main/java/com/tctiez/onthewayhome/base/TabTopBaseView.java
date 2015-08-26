package com.tctiez.onthewayhome.base;

import android.content.Context;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.tctiez.onthewayhome.R;
import com.tctiez.onthewayhome.base.BaseActivity;
import com.tctiez.onthewayhome.base.BaseView;
import com.tctiez.onthewayhome.base.RootView;
import com.tctiez.onthewayhome.widget.WTab;
import com.tctiez.onthewayhome.widget.WTabbar;
import com.tctiez.onthewayhome.widget.WTopbar;

import java.util.List;

/**
 * Created by Eugene J. Jeon on 2015-08-19.
 */
public abstract class TabTopBaseView extends BaseView implements View.OnClickListener {
    protected RootView      mParentView = null;
    protected LinearLayout  mRoot       = null;

    private WTopbar mTopbar = null;
    private WTabbar mTabbar = null;

    private LinearLayout mContents = null;
    private View         baseView  = null;

    public TabTopBaseView(BaseActivity context, RootView tParentView) {
        super(context);
        mParentView = tParentView;

        baseView = View.inflate(context, R.layout.view_tabtopbase, null);
        addView(baseView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

        mRoot = (LinearLayout) baseView.findViewById(R.id.layout_view_tab_top_base_root);
        mRoot.setSoundEffectsEnabled(false);
        mRoot.setOnClickListener(this);

        mTopbar = (WTopbar) baseView.findViewById(R.id.topbar);
        mTabbar = (WTabbar) baseView.findViewById(R.id.tabbar);
        mContents = (LinearLayout) baseView.findViewById(R.id.contents);
    }

    protected void setTopbar(int titleResId) {
        setTopbar(getResources().getString(titleResId), null, null, null, null);
    }

    protected void setTopbar(String titleStr) {
        setTopbar(titleStr, null, null, null, null);
    }

    protected void setTopbar(int titleResId, Integer leftButtonResId, OnClickListener leftButtonClickListener,
                             Integer rightButtonResId, OnClickListener rightButtonClickListener) {
        setTopbar(getResources().getString(titleResId), leftButtonResId, leftButtonClickListener, rightButtonResId,
                rightButtonClickListener);
    }

    protected void setTopbar(String titleStr, Integer leftButtonResId, OnClickListener leftButtonClickListener,
                             Integer rightButtonResId, OnClickListener rightButtonClickListener) {
        mTopbar.setTitle(titleStr);
        mTopbar.setButton(leftButtonResId, leftButtonClickListener, rightButtonResId, rightButtonClickListener);
        mTopbar.setVisibility(VISIBLE);
    }

    protected void setTopbarTitle(String titleStr) {
        mTopbar.setTitle(titleStr);
    }

    protected WTab addTab(String tabText) {
        return mTabbar.addTab(tabText);
    }

    protected WTabbar getTabbar() {
        return mTabbar;
    }

    protected List<WTab> getTabs() {
        return mTabbar.getTabs();
    }

    protected void setContentsLayout(int resId) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ViewGroup layout = (ViewGroup) inflater.inflate(resId, null, false);
        mContents.addView(layout, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
    }
}
