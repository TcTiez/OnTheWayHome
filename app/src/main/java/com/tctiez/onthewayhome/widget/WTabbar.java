package com.tctiez.onthewayhome.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Eugene J. Jeon on 2015-08-19.
 *
 *
 * <간단 사용법>
 *     Tab tTab = addTab("Test1", null);
 *     addTab("Test22", null);
 *     List(Tab) tTabList = getTabs();
 *     WTabbar wTabbar = getTabbar();       // create method;
 *     wTabbar.setOnTabSelectedListener(new OnTabSelectedListener() {
 *         @Override
 *         public void onSelected(Tab tab) {
 *             // ...
 *         }
 *     }
 */
public class WTabbar extends LinearLayout implements WTab.OnTabSelectedListener {
    private WTab.OnTabSelectedListener mOnTabSelectedListener = null;

    private List<WTab> mTabList = null;
    private Context mContext = null;

    public WTabbar(Context context) {
        super(context);
        init();
    }

    public WTabbar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mContext = getContext();

        mTabList = new ArrayList<WTab>();

        setVisibility(GONE);
    }

    public int getTabCount() {
        return mTabList.size();
    }

    public List<WTab> getTabs() {
        return mTabList;
    }

    public WTab addTab(String name) {
        WTab tab = new WTab(mContext);
        tab.setText(name);
        tab.setOnTabSelectedListener(this);

        addView(tab, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, 1));
        if (getVisibility() != VISIBLE) {
            setVisibility(VISIBLE);
        }

        mTabList.add(tab);
        return tab;
    }

    public void initTabs() {
        for (WTab tab : mTabList) {
            tab.initColor();
        }
    }

    public void setOnTabSelectedListener(WTab.OnTabSelectedListener l) {
        mOnTabSelectedListener = l;
    }

    @Override
    public void onSelected(WTab w) {
        initTabs();

        if (mOnTabSelectedListener != null) {
            mOnTabSelectedListener.onSelected(w);
        }
    }
}