package com.lyricgan.grace;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public abstract class GraceActivity extends AppCompatActivity implements GraceAppListener {
    private boolean mActive;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        onCreatePrepare(savedInstanceState);
        super.onCreate(savedInstanceState);
        logMessage("onCreate()");

        Bundle args = getIntent().getExtras();
        if (args != null) {
            onCreateExtras(savedInstanceState, args);
        }
        setContentView(getContentViewId());
        View decorView = getWindow().getDecorView();

        onCreateTitleBar(decorView, savedInstanceState);
        onCreateContentView(decorView, savedInstanceState);
        onCreateData(savedInstanceState);
    }

    @Override
    protected void onStart() {
        super.onStart();
        logMessage("onStart()");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        logMessage("onRestart()");
    }

    @Override
    protected void onResume() {
        super.onResume();
        logMessage("onResume()");
        mActive = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        logMessage("onPause()");
        mActive = false;
    }

    @Override
    protected void onStop() {
        super.onStop();
        logMessage("onStop()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        logMessage("onDestroy()");
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        logMessage("onNewIntent()");
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        logMessage("onSaveInstanceState()");
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        logMessage("onRestoreInstanceState()");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        logMessage("onActivityResult()");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        logMessage("onRequestPermissionsResult()");
    }

    protected abstract void onCreateTitleBar(View decorView, Bundle savedInstanceState);

    /**
     * 返回页面是否处于活动状态
     *
     * @return true or false
     */
    public boolean isActive() {
        return mActive;
    }

    private void logMessage(String message) {
        Log.d(getClass().getName(), message);
    }
}