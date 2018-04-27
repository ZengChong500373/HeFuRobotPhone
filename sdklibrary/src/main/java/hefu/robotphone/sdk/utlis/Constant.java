package hefu.robotphone.sdk.utlis;

import hefu.robotphone.sdk.RobotSdk;

public class Constant {
    /**
     * 扫描跳转Activity RequestCode
     */
    public static final int REQUEST_CODE = 111;


    /** 指令的结束符号 */
    public final static String BUF_INSTRUCTION_SPLIT_SYMBOL = "\\";
    /**apk 在本地的存放地址*/
    public static String APKPATH = RobotSdk.getContext().getExternalFilesDir(null).getAbsolutePath() + "/fuwa.apk";

    public static String YS_APP_KEY = "bff9469066d3420fb3fd9e9850071a54";
    public static String YS_SECRET = "6c0135e65c6d3ef4dbbda2895766cce9";
}
