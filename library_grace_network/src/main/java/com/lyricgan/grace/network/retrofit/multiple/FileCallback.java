package com.lyricgan.grace.network.retrofit.multiple;

public interface FileCallback {

    void onProgress(long currentSize, long totalSize, boolean isCompleted);
}
