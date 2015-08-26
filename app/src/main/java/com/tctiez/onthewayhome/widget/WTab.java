package com.tctiez.onthewayhome.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.tctiez.onthewayhome.R;

/**
 * Created by Eugene J. Jeon on 2015-08-19.
 */
public class WTab extends TextView implements View.OnClickListener {
    private OnTabSelectedListener mOnTabSelectedListener = null;

    private boolean mIsSelected = false;
    private int mTabBGColor = 0;
    private int mSelTabBGColor = 0;
    private int mTextColor = 0;
    private int mSelTextColor = 0;

    @SuppressLint("NewApi")
    public WTab(Context context) {
        super(context);
        generateViewId();

        mTabBGColor = getResources().getColor(Color.WHITE);
        mSelTabBGColor = getResources().getColor(Color.BLACK);

        mTextColor = getResources().getColor(Color.BLACK);
        mSelTextColor = getResources().getColor(Color.WHITE);

        initColor();

        setOnClickListener(this);
        setGravity(Gravity.CENTER);
    }

    public void initColor() {
        mIsSelected = false;
        setBackgroundColor(mTabBGColor);
        setTextColor(mTextColor);
    }

    private void selectedColor() {
        mIsSelected = true;
        setBackgroundColor(mSelTabBGColor);
        setTextColor(mSelTextColor);
    }

    public boolean isSelected() {
        return mIsSelected;
    }

    @Override
    public void onClick(View v) {
        onClick();
    }

    public void onClick() {
        if (mOnTabSelectedListener != null) {
            mOnTabSelectedListener.onSelected(this);
        }

        selectedColor();
    }

    public void setOnTabSelectedListener(OnTabSelectedListener listener) {
        mOnTabSelectedListener = listener;
    }

    public interface OnTabSelectedListener {
        void onSelected(WTab w);
    }
}