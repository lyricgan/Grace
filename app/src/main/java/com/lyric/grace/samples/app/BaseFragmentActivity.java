package com.lyric.grace.samples.app;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentFactory;
import androidx.fragment.app.FragmentManager;

import com.lyric.grace.R;
import com.lyric.grace.samples.constants.IExtras;

/**
 * Fragment容器Activity
 *
 * @author Lyric Gan
 * @since 2019/4/18
 */
public class BaseFragmentActivity extends BaseActivity {
    private String mTitle;
    private String mFragmentName;
    private Bundle mParams;
    private Fragment mFragment;

    @Override
    public void onCreateExtras(Bundle savedInstanceState, Bundle args) {
        super.onCreateExtras(savedInstanceState, args);
        mFragmentName = args.getString(IExtras.KEY_NAME);
        mTitle = args.getString(IExtras.KEY_TITLE);
        mParams = args.getBundle(IExtras.KEY_PARAMS);
    }

    @Override
    public int getContentViewId() {
        return R.layout.app_fragment_layout;
    }

    @Override
    public void onCreateContentView(View view, Bundle savedInstanceState) {
        TitleBar titleBar = getTitleBar();
        if (titleBar != null) {
            titleBar.setCenterText(mTitle);
        }
        if (savedInstanceState == null) {
            initFragment(mFragmentName, mParams);
        }
    }

    @Override
    public void onCreateData(Bundle savedInstanceState) {
    }

    @Override
    public void onBackPressed() {
        if (mFragment instanceof BaseFragment) {
            if (((BaseFragment) mFragment).onBackPressed()) {
                return;
            }
        }
        super.onBackPressed();
    }

    private void initFragment(String fragmentName, Bundle args) {
        try {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentFactory fragmentFactory = fragmentManager.getFragmentFactory();
            Fragment fragment = fragmentFactory.instantiate(getClassLoader(), fragmentName);
            if (args != null) {
                args.setClassLoader(fragment.getClass().getClassLoader());
                fragment.setArguments(args);
            }
            fragmentManager.beginTransaction()
                    .replace(R.id.fragment_content, fragment)
                    .commitAllowingStateLoss();
            fragmentManager.executePendingTransactions();

            mFragment = fragment;
        } catch (Exception e) {
            e.printStackTrace();
            finish();
        }
    }

    public Bundle getParams() {
        return mParams;
    }

    public Fragment getContainerFragment() {
        return mFragment;
    }

    public void replaceContainerFragment() {
        String fragmentName = mFragmentName;
        if (TextUtils.isEmpty(fragmentName)) {
            return;
        }
        initFragment(fragmentName, mParams);
    }
}
