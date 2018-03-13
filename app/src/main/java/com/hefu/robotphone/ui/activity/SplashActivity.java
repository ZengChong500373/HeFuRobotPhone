package com.hefu.robotphone.ui.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Window;
import android.view.WindowManager;

import com.hefu.robotphone.R;
import com.hefu.robotphone.databinding.ActivitySplashBinding;

import hefu.robotphone.uilibrary.base.BaseActivity;

public class SplashActivity extends BaseActivity {
   ActivitySplashBinding binding;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//隐藏标题栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);//隐藏状态栏

        binding= DataBindingUtil.setContentView(this, R.layout.activity_splash);
        initView();
    }

    private void initView() {
        binding.splashParent.postDelayed(new Runnable() {
            @Override
            public void run() {
                go2Main();
            }
        },2500);


    }

    public void go2Main(){
        Intent intent =new Intent(SplashActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
