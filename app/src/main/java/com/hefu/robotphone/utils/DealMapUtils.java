package com.hefu.robotphone.utils;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.util.Base64;
import android.util.Log;



import com.hefu.robotphone.bean.Point3DF;



public class DealMapUtils {
    private static  Matrix   mCurrentDisplayMatrix ;
    private static RosImageView img;

    public static void init(RosImageView initImg) {
        img = initImg;

    }
    public static void dellRobotMessage(String message) {
        String[] res = message.split(" ");
        if (res[2].equals("087")) {//预览
            final Bitmap bitmap = convertStringToIcon(res[3].split(",")[0]);
            String[] origin = res[4].split(",");
            final Point3DF originPoint = new Point3DF(Float.valueOf(origin[0]), Float.valueOf(origin[1]), Float.valueOf(origin[2]));
            final float resolution = Float.valueOf(res[5]);
            Log.e("mapinfo","resolution = "+resolution);
            Log.e("mapinfo","bitmap W= "+bitmap.getWidth()+",bitmap H = "+bitmap.getHeight());
            Log.e("mapinfo","Point3DF= "+originPoint);
            img.post(new Runnable() {
                @Override
                public void run() {
                    img.setRosOriginPoint3DF(originPoint);
                    img.setResolution(resolution);
                    img.setImageBitmap(bitmap);
                }
            });
        } else if (res[2].equals("088")) {//最终图片

        } else if (res[2].equals("089")) {//实时位置
            String[] realPos = res[3].split(",");
            if(realPos[2].equals("nan"))
                realPos[2] = "0";
            Point3DF realPositionPoint = new Point3DF(Float.valueOf(realPos[0]), Float.valueOf(realPos[1]), Float.valueOf(realPos[2]));
            img.setRosRobotPoint3DF(realPositionPoint);
        }
    }
    /**
     * string转成bitmap
     *
     * @param st
     */
    private static Bitmap convertStringToIcon(String st) {
        Bitmap bitmap = null;
        try {
            byte[] bitmapArray;
            bitmapArray = Base64.decode(st, Base64.DEFAULT);
            bitmap =
                    BitmapFactory.decodeByteArray(bitmapArray, 0,
                            bitmapArray.length);
            return bitmap;
        } catch (Exception e) {
            return null;
        }
    }

}
