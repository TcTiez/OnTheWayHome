package com.tctiez.onthewayhome.base;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.tctiez.onthewayhome.base.BaseActivity;

/**
 * Created by Eugene J. Jeon on 2015-08-19.
 */
public class WebBaseView extends WebView {
    private Context mContext = null;

    @SuppressLint("NewApi")
    public WebBaseView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context);
    }

    public WebBaseView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView(context);
    }

    public WebBaseView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public WebBaseView(Context context) {
        super(context);
        initView(context);
    }

    private void initView(Context context) {
        mContext = context;
    }

    @Override
    public void loadUrl(String url) {
        setProgress();
        super.loadUrl(url);
    }

    private void setProgress() {
        ((BaseActivity) mContext).showDarkView(null);
        setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                ((BaseActivity) mContext).hideDarkView(null);
            }
        });
    }
}
