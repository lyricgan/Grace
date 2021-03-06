package com.lyric.grace.samples;

import android.os.Bundle;
import android.view.View;

import com.lyric.grace.R;
import com.lyric.grace.samples.app.BaseFragment;

/**
 * 刷新页面
 * @author Lyric Gan
 * @since 2021/4/30
 */
public class RefreshFragment extends BaseFragment {

    public static RefreshFragment newInstance() {
        Bundle args = new Bundle();
        RefreshFragment fragment = new RefreshFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getContentViewId() {
        return R.layout.refresh_layout;
    }

    @Override
    public void onCreateContentView(View view, Bundle savedInstanceState) {
    }

    @Override
    public void onCreateData(Bundle savedInstanceState) {
    }
}
