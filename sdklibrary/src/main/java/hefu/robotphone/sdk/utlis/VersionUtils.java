package hefu.robotphone.sdk.utlis;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;



import java.io.File;
import hefu.robotphone.sdk.RobotSdk;
import hefu.robotphone.sdk.bean.VersionBean;
import hefu.robotphone.sdk.http.Network;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.BufferedSink;
import okio.BufferedSource;
import okio.Okio;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class VersionUtils {
  private static String newVersionUrl;
  private static Context mContext;
    public static void init(Context initContext) {
        mContext=initContext;
        Network.getMethods().getVersionInfos().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<VersionBean>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable throwable) {
                CrashHandler.getInstance().saveCrashInfo2File(throwable);
            }

            @Override
            public void onNext(VersionBean versionBean) {
                MyLog.write2File("getVersion "+getVersion());
                MyLog.write2File("versionBean "+versionBean.toString());
                if (versionBean != null && getVersion() < versionBean.build) {
                    newVersionUrl=versionBean.installUrl;
                    showDialog();
                }
            }
        });
    }

    public static int getVersion() {
        int versionCode = 0;
        try {
            //获取软件版本号，对应AndroidManifest.xml下android:versionCode
            versionCode = RobotSdk.getContext().getPackageManager().
                    getPackageInfo(RobotSdk.getContext().getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionCode;
    }

    public static void showDialog() {
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(mContext);
        builder.setMessage("当前有最新版本，请问是否更新？");
        builder.setNegativeButton("取消",null);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                downLoadFile(newVersionUrl);
            }
        });
        AlertDialog dialog= builder.create();
        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.parseColor("#3F51B5"));
        dialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(Color.parseColor("#3F51B5"));
    }

    public static void downLoadFile(final String uri) {
        Observable.just(uri).map(new Func1<String, Boolean>() {
            @Override
            public Boolean call(String s) {
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder().url(s.toString()).build();
                try {
                    Response response = client.newCall(request).execute();
                    if (!response.isSuccessful()) {
                        return false;
                    }
                    ResponseBody body = response.body();
                    long contentLength = body.contentLength();
                    BufferedSource source = body.source();
                    File file = new File(Constant.APKPATH);
                    BufferedSink sink = Okio.buffer(Okio.sink(file));
                    sink.writeAll(source);
                    sink.flush();
                } catch (Exception e) {
                    e.printStackTrace();
                    return false;
                }
                return true;
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<Boolean>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                CrashHandler.getInstance().saveCrashInfo2File(e);

            }

            @Override
            public void onNext(Boolean aBoolean) {
                install(Constant.APKPATH);
            }
        });

    }

    public static void install(String apkPath) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri contentUri = FileProvider.
                    getUriForFile( RobotSdk.getContext(),
                            "com.hefu.robotphone.fileprovider",
                            new File(apkPath));
            intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
        } else {
            intent.setDataAndType(Uri.fromFile(new File(apkPath)), "application/vnd.android.package-archive");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        RobotSdk.getContext().startActivity(intent);
    }
}
