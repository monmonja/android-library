package com.monmonja.library.views;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.monmonja.library.R;
import com.monmonja.library.utils.ViewUtils;


/**
 * Created by almondjoseph on 16/3/15.
 */
public class BaseFrameLayout extends FrameLayout {
    private int mStatusBarColor;
    private int mNavigationBarColor;

    private View mNavigationView;
    private View mStatusBarView;
    private int mFullBgResId;
    private ImageView mFullBgView;

    public BaseFrameLayout(Context context) {
        super(context);
        init(null);
    }

    public BaseFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        if (attrs != null) {
            TypedArray attributesFromXmlLayout = getContext().obtainStyledAttributes(attrs, R.styleable.baseLayout);
            mStatusBarColor = attributesFromXmlLayout.getColor(R.styleable.baseLayout_statusBarColor, Integer.MAX_VALUE);
            mNavigationBarColor = attributesFromXmlLayout.getColor(R.styleable.baseLayout_navigationBarColor, Integer.MAX_VALUE);
            mFullBgResId = attributesFromXmlLayout.getResourceId(R.styleable.baseLayout_fullBackground, -1);
            attributesFromXmlLayout.recycle();
        }

        if (mStatusBarColor != Integer.MAX_VALUE) {
            setupStatusBarWithColor();
        }
        if (mNavigationBarColor != Integer.MAX_VALUE) {
            setupNavigationBarWithColor();
        }
        if (mFullBgResId != -1) {
            addDesignReference();
        }
    }

    private void addDesignReference() {
        mFullBgView = new ImageView(getContext());
        mFullBgView.setImageResource(mFullBgResId);
        mFullBgView.setScaleType(ImageView.ScaleType.FIT_XY);
        addView(mFullBgView);
    }

    public void setupStatusBarWithColor () {
        mStatusBarView = new View(getContext());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = ((Activity)getContext()).getWindow();
            window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

            ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewUtils.getStatusBarHeight(getContext()));
            //action bar height
            mStatusBarView.setLayoutParams(layoutParams);
            mStatusBarView.setBackgroundColor(mStatusBarColor);
            mStatusBarView.setAlpha(1);
        }
        addView(mStatusBarView, 0);
    }


    public void setupNavigationBarWithColor () {
        mNavigationView = new View(getContext());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = ((Activity)getContext()).getWindow();
            window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getNavigationBarHeight());
            layoutParams.gravity = Gravity.BOTTOM;
            //action bar height
            mNavigationView.setLayoutParams(layoutParams);
            mNavigationView.setBackgroundColor(mNavigationBarColor);
            mNavigationView.setAlpha(1);
        }
        addView(mNavigationView, 0);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            if (!(child.equals(mNavigationView) || child.equals(mStatusBarView) || child.equals(mFullBgView))) {
                child.setFitsSystemWindows(true);
            }
        }
    }



    public int getNavigationBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("navigation_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }
}
