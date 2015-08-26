package com.tctiez.onthewayhome.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tctiez.onthewayhome.R;

/**
 * Created by Eugene J. Jeon on 2015-08-19.
 */
public class WTopbar extends LinearLayout {
    private ImageView mLeftButton = null;
    private ImageView mRightButton = null;
    private TextView mTitleText = null;

    public WTopbar(Context context) {
        super(context);
        init();
    }

    public WTopbar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.widget_topbar, this, true);

        mLeftButton = (ImageView) findViewById(R.id.topbar_left_button);
        mRightButton = (ImageView) findViewById(R.id.topbar_right_button);
        mTitleText = (TextView) findViewById(R.id.topbar_title_text);

        this.setVisibility(GONE);
    }

    public void setTitle(String title) {
        mTitleText.setText(title);
        mTitleText.setVisibility(View.VISIBLE);
    }

    public void setButton(Integer leftButtonResId, OnClickListener leftButtonClickListener, Integer rightButtonResId,
                          OnClickListener rightButtonClickListener) {
        if (leftButtonResId != null) {
            mLeftButton.setImageResource(leftButtonResId);
            mLeftButton.setOnClickListener(leftButtonClickListener);
        } else
            mLeftButton.setVisibility(INVISIBLE);

        if (rightButtonResId != null) {
            mRightButton.setImageResource(rightButtonResId);
            mRightButton.setOnClickListener(rightButtonClickListener);
        } else
            mRightButton.setVisibility(INVISIBLE);
    }
}
