package com.tctiez.onthewayhome.popup;

import android.content.Context;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.tctiez.onthewayhome.R;
import com.tctiez.onthewayhome.base.BaseActivity;
import com.tctiez.onthewayhome.base.BasePopup;
import com.tctiez.onthewayhome.base.RootView;

/**
 * Created by Eugene J. Jeon on 2015-08-20.
 */
public class PSearch extends BasePopup implements View.OnClickListener, View.OnKeyListener {
    public static final int SEARCH_CANCEL   = 0;
    public static final int SEARCH          = 1;

    private BaseActivity context    = null;

    private View        mDarkScreen = null;
    private EditText    mSearchBar  = null;
    private Button      mSearchBtn  = null;

    private InputMethodManager mImm = null;

    public PSearch(BaseActivity context, RootView parentView) {
        super(context, parentView);
        // inflate layout
        View layout = View.inflate(context, R.layout.popup_search, null);
        addView(layout, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

        mDarkScreen = findViewById(R.id.search_dark_screen);
        mSearchBar = (EditText) findViewById(R.id.search_bar);
        mSearchBtn = (Button) findViewById(R.id.search_btn);

        mDarkScreen.setOnClickListener(this);
        mSearchBtn.setOnClickListener(this);

        mSearchBar.setOnKeyListener(this);
        mImm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
    }

    @Override
    public void changeTheme() {}

    @Override
    public void onClick(View view) {
        mImm.hideSoftInputFromWindow(mSearchBar.getWindowToken(), 0);

        switch(view.getId()) {
            case R.id.search_btn:
                onPopupAction(SEARCH, SEARCH);
                break;

            default:
                onPopupAction(SEARCH_CANCEL, SEARCH_CANCEL);
                break;
        }
    }

    @Override
    public boolean onBackPressed() {
        onPopupAction(SEARCH_CANCEL, SEARCH_CANCEL);
        return super.onBackPressed();
    }

    @Override
    public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
        // event.getAction() == KeyEvent.ACTION_DOWN || event.getAction() == KeyEvent.ACTION_UP

        if (keyCode == KeyEvent.KEYCODE_ENTER) {
            mSearchBtn.callOnClick();
            return true;
        }

        return false;
    }
}
