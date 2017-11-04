package com.lyric.grace.common;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.lyric.grace.GraceApplication;
import com.lyric.grace.R;
import com.lyric.grace.utils.ViewUtils;
import com.lyric.grace.widget.LoadingDialog;
import com.lyric.grace.widget.TitleBar;

/**
 * 基类Activity，继承于FragmentActivity
 * @author lyricgan
 * @date 2016/5/26 10:25
 */
public abstract class BaseActivity extends FragmentActivity implements IBaseListener, ILoadingListener, IMessageProcessor {
    private Context mParent;
    private boolean mDestroy = false;
    private TitleBar mTitleBar;
    private LoadingDialog mLoadingDialog;
    private BaseHandler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        onPrepareCreate(savedInstanceState);
        adjustTitleBar(this);
        super.onCreate(savedInstanceState);
        mParent = this;
        setContentView(getLayoutId());
        if (isInject()) {
            injectStatusBar();
        }
        onLayoutCreated(savedInstanceState);
    }

    @Override
    public void setContentView(int layoutResID) {
        setContentView(getLayoutInflater().inflate(layoutResID, null));
    }

    @Override
    public void setContentView(View view) {
        if (isUseTitleBar()) {
            LinearLayout rootLayout = new LinearLayout(this);
            rootLayout.setOrientation(LinearLayout.VERTICAL);
            rootLayout.addView(mTitleBar, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            rootLayout.addView(view, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            super.setContentView(rootLayout);
        } else {
            super.setContentView(view);
        }
    }

    @Override
    public void onPrepareCreate(Bundle savedInstanceState) {
        mHandler = new BaseHandler(this);
        Intent intent = getIntent();
        if (intent != null) {
            initExtras(intent);
        }
    }

    private void adjustTitleBar(Context context) {
        if (isUseTitleBar()) {
            mTitleBar = new TitleBar(context);
            onTitleBarCreated(mTitleBar);
        }
    }

    protected boolean isUseTitleBar() {
        return true;
    }

    protected void onTitleBarCreated(TitleBar titleBar) {
        titleBar.setLeftDrawable(R.drawable.icon_back);
        titleBar.setLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onTitleBarLeftClick();
            }
        });
    }

    protected TitleBar getTitleBar() {
        return mTitleBar;
    }

    protected void onTitleBarLeftClick() {
        super.onBackPressed();
    }

    protected void initExtras(Intent intent) {
    }

    @Override
    public abstract int getLayoutId();

    @Override
    public abstract void onLayoutCreated(Bundle savedInstanceState);

    @Override
    public void onClick(View v) {
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onResume() {
        mDestroy = false;
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        mDestroy = true;
        super.onDestroy();
    }

    public Context getParentContext() {
        return mParent;
    }

    protected boolean isDestroy() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            return isDestroyed();
        }
        return mDestroy;
    }

    protected boolean isInject() {
        return false;
    }

    protected void injectStatusBar() {
        ViewUtils.setStatusBarColor(this, ContextCompat.getColor(GraceApplication.getContext(), R.color.title_bar_bg));
    }

    protected boolean isHideKeyboard() {
        return false;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (MotionEvent.ACTION_DOWN == ev.getAction()) {
            View v = getCurrentFocus();
            if (isHideKeyboard() && isShouldHideKeyboard(v, ev)) {
                hideKeyboard(v.getWindowToken());
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    private boolean isShouldHideKeyboard(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] location = {0, 0};
            v.getLocationInWindow(location);
            int left = location[0];
            int top = location[1];
            int bottom = top + v.getHeight();
            int right = left + v.getWidth();
            return !(event.getX() > left && event.getX() < right && event.getY() > top && event.getY() < bottom);
        }
        // 如果焦点不是EditText则忽略，这个发生在视图刚绘制完，第一个焦点不在EditText上，和用户用轨迹球选择其他的焦点
        return false;
    }

    private void hideKeyboard(IBinder token) {
        if (token == null) {
            return;
        }
        InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (im != null) {
            im.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    protected <T extends View> T findViewWithId(int id) {
        return (T) super.findViewById(id);
    }

    @Override
    public void showLoading(CharSequence message, boolean isCancelable) {
        if (mLoadingDialog == null) {
            mLoadingDialog = new LoadingDialog(this);
        }
        mLoadingDialog.setMessage(message);
        mLoadingDialog.setCancelable(isCancelable);
        mLoadingDialog.setCanceledOnTouchOutside(false);
        mLoadingDialog.show();
    }

    @Override
    public void hideLoading() {
        if (mLoadingDialog != null) {
            mLoadingDialog.dismiss();
        }
    }

    protected void showLoading(CharSequence message) {
        showLoading(message, true);
    }

    @Override
    public void handleMessage(Message msg) {
    }

    @Override
    public Handler getHandler() {
        return mHandler;
    }
}