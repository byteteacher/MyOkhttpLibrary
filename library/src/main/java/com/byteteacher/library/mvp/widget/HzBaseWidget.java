package com.byteteacher.library.mvp.widget;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;

/**
 * Created by cj on 2020/4/24.
 */
public abstract class HzBaseWidget implements IHzWidget {

    public static final String TAG = "cj";

    private Context mCtx;
    private View view;

    public HzBaseWidget() {
    }

    /**
     * activity中使用此onCreate绑定
     */
    public void onCreate(Context context) {
        this.onCreate(context,null);
    }

    /**
     * fragment中使用此onCreate绑定
     */
    public void onCreate(Context context, View view) {
        this.mCtx = context;
        this.view = view;
    }

    public Context getContext() {
        return this.mCtx;
    }

    public Activity getActivity() {
        if (mCtx instanceof Activity) {
            return (Activity) this.mCtx;
        } else {
            return null;
        }
    }

    protected <T extends View> T viewFindViewById(int id) {
        if (mCtx == null) {
            return null;
        }
        if (mCtx instanceof Activity) {
            return (T) view.findViewById(id);
        } else {
            return null;
        }
    }

    protected <T extends View> T activityFindViewById(int id) {
        if (mCtx == null) {
            return null;
        }
        if (mCtx instanceof Activity) {
            return (T) ((Activity) (mCtx)).findViewById(id);
        } else {
            return null;
        }
    }

    protected <T extends View> T findViewById(int id) {
        if (view != null) {
            return viewFindViewById(id);
        } else {
            return activityFindViewById(id);
        }
    }

    protected void startActivity(Intent intent) {
        try {
            this.mCtx.startActivity(intent);
        } catch (Exception var3) {
            var3.printStackTrace();
        }

    }

    protected void startActivity(String action) {
        try {
            this.mCtx.startActivity(new Intent(action));
        } catch (Exception var3) {
            var3.printStackTrace();
        }

    }
}
