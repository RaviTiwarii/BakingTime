package com.android.example.bakingtime.ui.downloader;

import java.util.List;

@FunctionalInterface
public interface DelayerCallback<T> {
    void onDone(List<T> items);
}