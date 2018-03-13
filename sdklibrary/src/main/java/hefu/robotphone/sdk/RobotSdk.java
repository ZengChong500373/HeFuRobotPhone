package hefu.robotphone.sdk;

import android.content.Context;


import com.github.moduth.blockcanary.BlockCanary;

import hefu.robotphone.sdk.utlis.AppBlockCanaryContext;
import hefu.robotphone.sdk.utlis.CrashHandler;


public class RobotSdk {
    /**
     * 上下文对象
     */
    private static Context mContext;

    public static void init(Context initContext) {
        mContext = initContext;
        CrashHandler.getInstance().init(mContext);

        BlockCanary.install(mContext, new AppBlockCanaryContext()).start();

    }

    /**
     * 全局上下文
     */
    public static Context getContext() {
        return mContext;
    }
}
