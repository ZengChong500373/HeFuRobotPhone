package com.hefu.robotphone;

import android.app.Application;
import android.content.Context;

import com.uuzuche.lib_zxing.activity.ZXingLibrary;
import com.videogo.openapi.EZOpenSDK;

import hefu.robotphone.sdk.RobotSdk;
import hefu.robotphone.sdk.utlis.Constant;
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
        RobotSdk.init(this);
        //二维码扫描第三方
        ZXingLibrary.initDisplayOpinion(mContext);
        GlideUtils.init(mContext);
        initSDK();

    }
    private  void initSDK() {
        {
            /**
             * sdk日志开关，正式发布需要去掉
             */
            EZOpenSDK.showSDKLog(true);

            /**
             * 设置是否支持P2P取流,详见api
             */
            EZOpenSDK.enableP2P(true);

            /**
             * APP_KEY请替换成自己申请的
             */
            EZOpenSDK.initLib(this, Constant.YS_APP_KEY);
        }
    }
    public static EZOpenSDK getOpenSDK() {
        return EZOpenSDK.getInstance();
    }

    /**
     * 全局上下文
     */
    public static Context getContext() {
        return mContext;
    }


}
