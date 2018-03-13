package com.hefu.robotphone.ui.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.hefu.robotphone.R;

import com.uuzuche.lib_zxing.activity.CaptureFragment;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import hefu.robotphone.sdk.utlis.ToastUtils;
import hefu.robotphone.uilibrary.base.BaseActivity;

public class ScannBindRobotActivity extends BaseActivity {
    Toolbar toolbar;
    TextView tv_flashlight;
    private CaptureFragment captureFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scann_bind_robot);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        captureFragment = new CaptureFragment();
        CodeUtils.setFragmentArgs(captureFragment, R.layout.my_camera);
        captureFragment.setAnalyzeCallback(analyzeCallback);
        getSupportFragmentManager().beginTransaction().replace(R.id.fl_my_container, captureFragment).commit();
        tv_flashlight=findViewById(R.id.tv_flashlight);
        tv_flashlight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setIsOpen();
            }
        });
    }

    /**
     * 二维码解析回调函数
     */
    CodeUtils.AnalyzeCallback analyzeCallback = new CodeUtils.AnalyzeCallback() {
        @Override
        public void onAnalyzeSuccess(Bitmap mBitmap, String result) {
            Intent resultIntent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putInt(CodeUtils.RESULT_TYPE, CodeUtils.RESULT_SUCCESS);
            bundle.putString(CodeUtils.RESULT_STRING, result);
            resultIntent.putExtras(bundle);
            ScannBindRobotActivity.this.setResult(RESULT_OK, resultIntent);
            ScannBindRobotActivity.this.finish();
        }

        @Override
        public void onAnalyzeFailed() {
            Toast.makeText(ScannBindRobotActivity.this, "onAnalyzeFailed  ", Toast.LENGTH_LONG).show();
            Intent resultIntent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putInt(CodeUtils.RESULT_TYPE, CodeUtils.RESULT_FAILED);
            bundle.putString(CodeUtils.RESULT_STRING, "");
            resultIntent.putExtras(bundle);
            ScannBindRobotActivity.this.setResult(RESULT_OK, resultIntent);
            ScannBindRobotActivity.this.finish();
        }
    };
    public Boolean isOpen=false;
    public void setIsOpen() {
        if (isOpen==false) {
            CodeUtils.isLightEnable(true);
            Drawable top = getResources().getDrawable(R.drawable.flashlight_selected);
            tv_flashlight.setCompoundDrawablesWithIntrinsicBounds(null, top , null, null);
            tv_flashlight.setText("轻触关闭");
            isOpen = true;
        } else {
            CodeUtils.isLightEnable(false);
            Drawable top = getResources().getDrawable(R.drawable.flashlight);
            tv_flashlight.setCompoundDrawablesWithIntrinsicBounds(null, top , null, null);
            tv_flashlight.setText("轻触照亮");
            isOpen = false;
        }
    }
}
