package hefu.robotphone.sdk.utlis;



import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import hefu.robotphone.sdk.RobotSdk;


/**
 * Created by Administrator on 2017/6/2.
 */

public class MyLog {
    public static String path = RobotSdk.getContext().getExternalFilesDir(null).getAbsolutePath();
    public static String name = path + File.separator + "jyhLte.txt";
    public static String name2 = path + File.separator + "info.txt";

    public static void write2File(String str) {
        StackTraceElement ste[] = Thread.currentThread().getStackTrace();
        StackTraceElement heihei=ste[3];
        StringBuffer stringBuffer=new StringBuffer();
        stringBuffer.append("["+heihei.getClassName()+"]").append("["+heihei.getMethodName()+" "+ heihei.getLineNumber()+"]");
        BufferedWriter out = null;
        try {
            out = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(name, true)));
            Date currentTime = new Date();

                        SimpleDateFormat formatter = new SimpleDateFormat(
                    " HH:mm:ss");
            String dateString = formatter.format(currentTime);

            out.write(dateString + " " + str + "\r\n");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
    public static void write2File2(String str) {
        StackTraceElement ste[] = Thread.currentThread().getStackTrace();
        StackTraceElement heihei=ste[3];
        StringBuffer stringBuffer=new StringBuffer();
        stringBuffer.append("["+heihei.getClassName()+"]").append("["+heihei.getMethodName()+" "+ heihei.getLineNumber()+"]");
        BufferedWriter out = null;
        try {
            out = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(name2, true)));
            Date currentTime = new Date();
//            SimpleDateFormat formatter = new SimpleDateFormat(
//                    "yyyy-MM-dd HH:mm:ss");
            SimpleDateFormat formatter = new SimpleDateFormat(
                    " HH:mm:ss");
            String dateString = formatter.format(currentTime);
//            out.write(dateString + " " +stringBuffer.toString()+" "+ str + "\r\n");
            out.write(dateString + " " + str + "\r\n");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }


    public static void init() {
        File dir = new File(path);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    public static void dele() {
        File f = new File(name);
        if (f.exists()) {
            f.delete();
        }
    }
}
