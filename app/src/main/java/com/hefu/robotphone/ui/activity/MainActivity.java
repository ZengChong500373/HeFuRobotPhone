package com.hefu.robotphone.ui.activity;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v4.view.ViewPager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;

import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import com.hefu.robotphone.R;
import com.hefu.robotphone.ui.adapter.MainPagerAdapter;
import com.hefu.robotphone.utils.ConectionControl;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import java.io.File;
import java.util.List;

import hefu.robotphone.sdk.RobotSdk;
import hefu.robotphone.sdk.socket.ArpUtil;

import com.hefu.robotphone.bean.RobotBean;

import hefu.robotphone.sdk.socket.RobotCmdSocket;
import hefu.robotphone.sdk.utlis.CheckPermissionUtils;
import hefu.robotphone.sdk.utlis.Constant;
import hefu.robotphone.sdk.utlis.VersionUtils;
import hefu.robotphone.uilibrary.base.BaseActivity;
import hefu.robotphone.uilibrary.utils.GlideUtils;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends BaseActivity
        implements EasyPermissions.PermissionCallbacks {
    ViewPager vp;
    MainPagerAdapter adapter;
    TabLayout tab_layout;
    DrawerLayout drawer;
    Toolbar toolbar;
    public RobotBean robotBean;
    public static RobotCmdSocket socket;

    ImageView img_head;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        VersionUtils.init(this);
        //初始化权限
        initPermission();
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.main_menu);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        vp = findViewById(R.id.vp);
        adapter = new MainPagerAdapter(getSupportFragmentManager());
        vp.setAdapter(adapter);
        tab_layout = findViewById(R.id.tab_layout);
        tab_layout.addTab(tab_layout.newTab());
        tab_layout.addTab(tab_layout.newTab());
        tab_layout.addTab(tab_layout.newTab());
        tab_layout.setupWithViewPager(vp);
        tab_layout.getTabAt(0).setText("遥控");
        tab_layout.getTabAt(1).setText("机器人状态");
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_main_scann:
                        cameraTask();
                        break;
                    case R.id.action_main_creat_map:
                        Intent intent = new Intent(MainActivity.this, CreatMapActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.action_main_build_return:
                        if (socket.getReady()) {
                            socket.robotCmd(ConectionControl.returnOrigin());
                            socket.robotCmd(ConectionControl.speakWords("正在返回原点"));
                        } else {
                            showSnackbar("请扫描二维码绑定控制机器人");
                        }
                        break;
                }

                return false;
            }
        });
        robotBean = new RobotBean();
        socket = RobotCmdSocket.getInstance();
        img_head = navigationView.getHeaderView(0).findViewById(R.id.img_head);
        GlideUtils.loadCircular(img_head, "");
        link();
        Switch sw_auto_login = navigationView.getHeaderView(0).findViewById(R.id.sw_auto_login);
        sw_auto_login.setChecked(ConectionControl.getAutoLogin());
        sw_auto_login.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                ConectionControl.setAutoLogin(b);
            }
        });
    }

    /**
     * 初始化权限事件
     */
    private void initPermission() {
        //检查权限
        String[] permissions = CheckPermissionUtils.checkPermission(this);
        if (permissions.length == 0) {
            //权限都申请了
            //是否登录
        } else {
            //申请权限
            ActivityCompat.requestPermissions(this, permissions, 100);
        }
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        /**
         * 处理二维码扫描结果
         */
        if (requestCode == Constant.REQUEST_CODE) {
            //处理扫描结果（在界面上显示）
            if (null != data) {
                Bundle bundle = data.getExtras();
                if (bundle == null) {
                    return;
                }
                if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {
                    String result = bundle.getString(CodeUtils.RESULT_STRING);
                    robotBean.setData(result);
                    ArpUtil arpUtil = new ArpUtil();
                    List<ArpUtil.IP_MAC> IP_MAC_List = arpUtil.getLocalDevices();
                    for (ArpUtil.IP_MAC ip_mac : IP_MAC_List) {
                        if (ip_mac.getMac().equals(robotBean.getPad_mac())) {
                            ConectionControl.setPadIp(ip_mac.getIp());
                            link();
                        }
                        if (ip_mac.getMac().equals(robotBean.getComputer_mac())) {
                            ConectionControl.setComputerIp(ip_mac.getIp());
                        }
                    }
                    Log.e("jyh_result", result);
                } else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {

                }
            }
        }
    }

    public void link() {
        if (ConectionControl.getAutoLogin() && !TextUtils.isEmpty(ConectionControl.getPadIp())) {
            ArpUtil arpUtil = new ArpUtil();
            arpUtil.getLocalDevices();
            socket.setHostName(ConectionControl.getPadIp());
            socket.start();
        } else {
            showSnackbar("请扫描二维码绑定控制机器人");
        }
    }

    public void showSnackbar(String str) {
        Snackbar.make(toolbar, str, Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }

    /**
     * EsayPermissions接管权限处理逻辑
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
//        Toast.makeText(this, "执行onPermissionsGranted()...", Toast.LENGTH_SHORT).show();
    }

    /**
     * 请求CAMERA权限码
     */
    public static final int REQUEST_CAMERA_PERM = 101;

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        Toast.makeText(this, "执行onPermissionsDenied()...", Toast.LENGTH_SHORT).show();
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this, "当前App需要申请camera权限,需要打开设置页面么?")
                    .setTitle("权限申请")
                    .setPositiveButton("确认")
                    .setNegativeButton("取消", null /* click listener */)
                    .setRequestCode(REQUEST_CAMERA_PERM)
                    .build()
                    .show();
        }
    }

    @AfterPermissionGranted(REQUEST_CAMERA_PERM)
    public void cameraTask() {
        if (EasyPermissions.hasPermissions(this, Manifest.permission.CAMERA)) {
            // Have permission, do the thing!
            Intent intent = new Intent(MainActivity.this, ScannBindRobotActivity.class);
            startActivityForResult(intent, Constant.REQUEST_CODE);
        } else {
            // Ask for one permission
            EasyPermissions.requestPermissions(this, "需要请求camera权限",
                    REQUEST_CAMERA_PERM, Manifest.permission.CAMERA);
        }
    }

    public void install() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri contentUri = FileProvider.
                    getUriForFile(MainActivity.this,
                            "",
                            new File(Constant.APKPATH));
            intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
        } else {
            intent.setDataAndType(Uri.fromFile(new File(Constant.APKPATH)), "application/vnd.android.package-archive");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        RobotSdk.getContext().startActivity(intent);
    }
}
