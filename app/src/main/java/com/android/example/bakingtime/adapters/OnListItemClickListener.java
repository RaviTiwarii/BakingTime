package com.android.example.bakingtime.adapters;

import android.support.annotation.NonNull;

@FunctionalInterface
public interface OnListItemClickListener<T> {
    void onItemClick(@NonNull T item);
}
