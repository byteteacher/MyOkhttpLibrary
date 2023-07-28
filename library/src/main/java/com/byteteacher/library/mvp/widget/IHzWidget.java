package com.byteteacher.library.mvp.widget;

import android.content.Context;

public interface IHzWidget {
    void onCreate(Context context);
    void onResume();
    void onPause();
    void onDestroy(Context context);
}
