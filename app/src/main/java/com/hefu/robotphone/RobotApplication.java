package com.hefu.robotphone;

import android.app.Application;
import android.content.Context;

import com.uuzuche.lib_zxing.activity.ZXingLibrary;

import hefu.robotphone.sdk.RobotSdk;
import hefu.robotphone.uilibrary.utils.GlideUtils;


/**
 * Created by jyh on 2016/9/7.
 */
public class RobotApplication extends Application {
    /**
     * 上下文对象
     */
    private static Context mContext;


    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;

        /**  sdk 初始化*/
        RobotSdk.init(mContext);
        //二维码扫描第三方
        ZXingLibrary.initDisplayOpinion(mContext);
        GlideUtils.init(mContext);


    }


    /**
     * 全局上下文
     */
    public static Context getContext() {
        return mContext;
    }


}
