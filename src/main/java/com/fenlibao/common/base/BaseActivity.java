package com.fenlibao.common.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.alibaba.android.arouter.launcher.ARouter;

/**
 * 0318 16:09
 */
public abstract class BaseActivity extends AppCompatActivity {
    protected BaseActivity _activity;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _activity = BaseActivity.this;
        ARouter.getInstance().inject(BaseActivity.this);
        setContentView(getLayoutId());
        initViews();
        init();
    }

    public abstract void init();

    public abstract void initViews();

    public abstract int getLayoutId();

}
