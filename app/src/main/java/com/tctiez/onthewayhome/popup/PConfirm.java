package com.tctiez.onthewayhome.popup;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.tctiez.onthewayhome.R;
import com.tctiez.onthewayhome.base.BaseActivity;
import com.tctiez.onthewayhome.base.BasePopup;
import com.tctiez.onthewayhome.base.RootView;

/**
 * Created by Eugene J. Jeon on 2015-08-20.
 */
public class PConfirm extends BasePopup implements View.OnClickListener {
    public static final int CONFIRM_LEFT    = 0;
    public static final int CONFIRM_RIGHT   = 1;

    private TextView    mTitle      = null;
    private Button      mLeftBtn    = null;
    private Button      mRightBtn   = null;

    public PConfirm(BaseActivity context, RootView tParentView, String title, String leftValue) {
        super(context, tParentView);
        init(context, title, leftValue, null);
    }

    public PConfirm(BaseActivity context, RootView tParentView, String title, String leftValue, String rightValue) {
        super(context, tParentView);
        init(context, title, leftValue, rightValue);
    }

    private void init(BaseActivity context, String title, String leftValue, String rightValue) {
        // inflate layout
        View layout = View.inflate(context, R.layout.popup_confirm, null);
        addView(layout, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

        mTitle = (TextView) findViewById(R.id.confirm_title);
        mLeftBtn = (Button) findViewById(R.id.confirm_btn_left);
        mRightBtn = (Button) findViewById(R.id.confirm_btn_right);

        mTitle.setText(title);
        mLeftBtn.setText(leftValue);

        mLeftBtn.setOnClickListener(this);

        if (rightValue != null) {
            mRightBtn.setText(rightValue);
            mRightBtn.setOnClickListener(this);
        } else {
            mRightBtn.setVisibility(GONE);
        }
    }

    public void setTitle(String title) {
        mTitle.setText(title);
    }

    @Override
    public void changeTheme() {}

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.confirm_btn_left:
                onPopupAction(CONFIRM_LEFT, CONFIRM_LEFT);
                break;
            case R.id.confirm_btn_right:
                onPopupAction(CONFIRM_RIGHT, CONFIRM_RIGHT);
                break;
        }
    }

    @Override
    public boolean onBackPressed() { return true; }
}
