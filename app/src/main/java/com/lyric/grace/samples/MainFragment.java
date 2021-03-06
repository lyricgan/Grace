package com.lyric.grace.samples;

import android.Manifest;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;

import com.lyric.grace.R;
import com.lyric.grace.samples.app.BaseFragment;
import com.lyric.grace.samples.app.BaseFragmentActivity;
import com.lyric.grace.samples.util.AppRunningUtils;
import com.lyric.grace.samples.util.GifSizeFilter;
import com.lyric.grace.samples.util.PageJumpHelper;
import com.lyric.grace.samples.util.PermissionHelper;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;
import com.zhihu.matisse.filter.Filter;

public class MainFragment extends BaseFragment {

    public static MainFragment newInstance() {
        Bundle args = new Bundle();
        MainFragment fragment = new MainFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getContentViewId() {
        return R.layout.main_tab_layout;
    }

    @Override
    public void onCreateContentView(View view, Bundle savedInstanceState) {
        view.findViewById(R.id.tv_test_1).setOnClickListener(v -> PageJumpHelper.jumpFragmentPage(getActivity(), NestedScrollFragment.class, BaseFragmentActivity.class, "NestedPage", null));
        view.findViewById(R.id.tv_test_2).setOnClickListener(v -> {
            if (AppRunningUtils.isIgnoringBatteryOptimizations(getActivity())) {
                AppRunningUtils.jumpBatteryOptimizationsSettings(getActivity());
            } else {
                AppRunningUtils.requestIgnoreBatteryOptimizations(getActivity());
            }
        });
        view.findViewById(R.id.tv_test_3).setOnClickListener(v -> AppRunningUtils.handleBackgroundRunningSettings(getActivity()));
        view.findViewById(R.id.tv_test_4).setOnClickListener(v -> onAlbumClick(true));
        view.findViewById(R.id.tv_test_5).setOnClickListener(v -> onAlbumClick(false));
    }

    @Override
    public void onCreateData(Bundle savedInstanceState) {
    }

    private void onAlbumClick(boolean lightStyle) {
        if (PermissionHelper.hasPermissions(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            Matisse.from(getActivity())
                    .choose(MimeType.ofAll())
                    .theme(lightStyle ? R.style.Matisse_Zhihu : R.style.Matisse_Dracula)
                    .countable(true)
                    .maxSelectable(9)
                    .addFilter(new GifSizeFilter(320, 320, 10 * Filter.K * Filter.K))
                    .gridExpectedSize(getResources().getDimensionPixelSize(R.dimen.grid_expected_size))
                    .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                    .thumbnailScale(0.85f)
                    .imageEngine(new GlideEngine())
                    .showPreview(true)
                    .forResult(0);
        } else {
            PermissionHelper.requestPermissions(getActivity(), 10, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
    }
}
