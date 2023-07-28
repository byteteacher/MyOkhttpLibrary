package com.byteteacher.library.mvp.activity;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.byteteacher.library.mvp.HzBasePresenter;


/**
 * Created by cj on 2020/9/25.
 */
public abstract class BaseMvpActivity<P extends HzBasePresenter> extends AppCompatActivity {

    protected P mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mPresenter = initPresenter();

    }

    abstract P initPresenter();


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mPresenter!=null){
            mPresenter.onDestroy();
        }
    }
}
