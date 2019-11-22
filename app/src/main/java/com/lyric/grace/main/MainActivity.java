package com.lyric.grace.main;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lyric.support.app.AppFragmentPagerAdapter;
import com.lyric.support.util.DisplayUtils;
import com.lyric.grace.GraceApplication;
import com.lyric.grace.R;
import com.lyric.grace.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity {
    private TextView tvCurrentPage, tvTotalPage;
    private RelativeLayout relativePoint;
    private LinearLayout linearPoint;
    private View viewFocusPoint;
    private ViewPager viewPager;

    private int mPointPageMargin;

    @Override
    public int getContentViewId() {
        return R.layout.activity_main;
    }

    @Override
    public void onCreateContentView(View view, Bundle savedInstanceState) {
        tvCurrentPage = findViewById(R.id.tv_current_page);
        tvTotalPage = findViewById(R.id.tv_total_page);
        relativePoint = findViewById(R.id.relative_point);
        linearPoint = findViewById(R.id.linear_point);
        viewFocusPoint = findViewById(R.id.view_focus_point);
        viewPager = findViewById(R.id.view_pager);

        viewPager.setPageMargin(DisplayUtils.dip2px(GraceApplication.getContext(), 8));
        viewPager.setPageMarginDrawable(new ColorDrawable(Color.BLACK));
    }

    @Override
    public void onCreateData(Bundle savedInstanceState) {
        final int size = 3;
        List<Fragment> fragments = new ArrayList<>();
        List<String> titles = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            fragments.add(MainFragment.newInstance());
            titles.add(String.valueOf(i + 1));
        }
        updatePagerIndicator(0);
        String totalPage = "/" + size;
        tvTotalPage.setText(totalPage);

        relativePoint.setVisibility(View.VISIBLE);
        int itemSize = DisplayUtils.dip2px(GraceApplication.getContext(), 4);
        for (int i = 0; i < size; i++) {
            View childView = new View(this);
            childView.setBackgroundResource(R.drawable.circle_white);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(itemSize, itemSize);
            if (i > 0) {
                params.leftMargin = itemSize;
            }
            childView.setLayoutParams(params);
            linearPoint.addView(childView);
        }
        linearPoint.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    linearPoint.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                } else {
                    linearPoint.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }
                if (linearPoint.getChildCount() > 1) {
                    mPointPageMargin = linearPoint.getChildAt(1).getLeft() - linearPoint.getChildAt(0).getLeft();
                }
            }
        });
        AppFragmentPagerAdapter adapter = new AppFragmentPagerAdapter(getSupportFragmentManager(), fragments, titles);
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(size);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (mPointPageMargin > 0) {
                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) viewFocusPoint.getLayoutParams();
                    params.leftMargin = (int) (mPointPageMargin * positionOffset) + mPointPageMargin * position;
                    viewFocusPoint.setLayoutParams(params);
                }
            }

            @Override
            public void onPageSelected(int position) {
                updatePagerIndicator(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    private void updatePagerIndicator(int currentPage) {
        tvCurrentPage.setText(String.valueOf(currentPage + 1));
    }
}
