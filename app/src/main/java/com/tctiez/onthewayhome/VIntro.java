package com.tctiez.onthewayhome;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.tctiez.onthewayhome.base.BaseActivity;
import com.tctiez.onthewayhome.base.BaseView;
import com.tctiez.onthewayhome.base.Const;
import com.tctiez.onthewayhome.base.RootView;
import com.tctiez.onthewayhome.base.TabTopBaseView;
import com.tctiez.onthewayhome.service.GPS;
import com.tctiez.onthewayhome.util.PrefUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Eugene J. Jeon on 2015-08-14.
 */
public class VIntro extends TabTopBaseView {
    private ViewPager       mPager              = null;
    private List<View>      mPagerItems         = null;
    private LinearLayout    mPagerMarkLayout    = null;
    private int             mPagerCount         = 0;

    private BaseActivity    mContext            = null;
    private boolean         mIsNewbie           = false;

    private int mBGRes      = Color.TRANSPARENT;
    private int mMarkRes    = R.drawable.sel_gray_dot;
    private int mSelMarkRes = R.drawable.sel_red_dot;

    public VIntro(BaseActivity context, RootView parentView) {
        super(context, parentView);
        mContext = context;
        mParentView.setMenu(false);

        //TODO:DEBUG
        //mIsNewbie = PrefUtil.getInstance(mContext).get(PrefUtil.KEY_NEWBIE, false);

        setContentsLayout(R.layout.view_intro);

        mContext.showDarkView(null);

        mPagerItems = new ArrayList<>();
        mPagerItems.add(new VIntroItem(R.drawable.intro_item_01));
        mPagerItems.add(new VIntroItem(R.drawable.intro_item_02));
        mPagerItems.add(new VIntroLogin());

        mPagerCount = mPagerItems.size();

        mPager = (ViewPager) findViewById(R.id.intro_pager);
        mPager.setAdapter(new SplashPagerAdapter());
        mPager.addOnPageChangeListener(onPageChangeListener);
    }

    @Override
    public void initView() {
        if (mIsNewbie) {
            mParentView.addVisibleView(Const.ChildViewKey.VMap, ANIMATION_STATE_VISIBLE.IN_UP_ZOOM, null);
        } else {
            mContext.hideDarkView(null);
        }
    }

    @Override
    public void onClick(View view) {

    }

    private ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {
        private int currentPage = 0;

        @Override
        public void onPageScrolled(int i, float v, int i2) {}

        @Override
        public void onPageSelected(int position) {
            ((ImageView) mPagerMarkLayout.getChildAt(currentPage)).setImageResource(mMarkRes);
            ((ImageView) mPagerMarkLayout.getChildAt(position)).setImageResource(mSelMarkRes);

            currentPage = position;
        }

        @Override
        public void onPageScrollStateChanged(int i) {}
    };

    private class SplashPagerAdapter extends PagerAdapter {
        public SplashPagerAdapter() {
            mPagerMarkLayout = (LinearLayout) findViewById(R.id.intro_pager_mark);
            int tImageWidth = getResources().getDimensionPixelSize(R.dimen.xxh_45px);
            int tImageHeight = tImageWidth;
            int tImageLeftRightMargin = (tImageWidth / 2) / 2;

            for (int i = 0; i < mPagerCount; i++) {
                ImageView mark = new ImageView(mContext);
                mark.setBackgroundResource(mBGRes);
                mark.setImageResource((i == 0 ? mSelMarkRes : mMarkRes));
                mPagerMarkLayout.addView(mark, new LayoutParams(tImageWidth, tImageHeight));

                LinearLayout.LayoutParams markParams = (LinearLayout.LayoutParams) mark.getLayoutParams();
                markParams.setMargins(tImageLeftRightMargin, 0, tImageLeftRightMargin, 0);
                mark.setLayoutParams(markParams);
            }
        }

        @Override
        public int getCount() {
            return mPagerItems.size();
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(mPagerItems.get(position), new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
            return mPagerItems.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

    private class VIntroItem extends BaseView {
        private ImageView mImageView = null;

        public VIntroItem(int resId) {
            super((BaseActivity) VIntro.this.mContext);

            mImageView = new ImageView(mContext);
            mImageView.setBackgroundResource(resId);

            addView(mImageView);
        }

        @Override
        public void initView() {}
    }

    private class VIntroLogin extends BaseView implements OnClickListener {
        private Button      mButton     = null;

        private RelativeLayout mRelativeLayout = null;

        public VIntroLogin() {
            super((BaseActivity) VIntro.this.mContext);

            mButton = new Button(mContext);
            mButton.setText("START");
            mButton.setOnClickListener(this);

            RelativeLayout.LayoutParams tParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            tParams.setMargins(100, 0, 100, 150);
            tParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
            mButton.setLayoutParams(tParams);

            mRelativeLayout = new RelativeLayout(mContext);
            mRelativeLayout.addView(mButton);

            addView(mRelativeLayout, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        }

        @Override
        public void initView() {}

        @Override
        public void onClick(View view) {
            if (view.equals(mButton)) {
                PrefUtil.getInstance(mContext).set(PrefUtil.KEY_NEWBIE, true);
                mParentView.addVisibleView(Const.ChildViewKey.VMap, ANIMATION_STATE_VISIBLE.IN_UP_ZOOM, null);
            }
        }
    }
}
