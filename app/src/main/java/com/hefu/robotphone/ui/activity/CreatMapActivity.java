package com.hefu.robotphone.ui.activity;

import android.databinding.DataBindingUtil;
import android.graphics.BitmapFactory;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.hefu.robotphone.R;
import com.hefu.robotphone.databinding.ActivityCreatMapBinding;
import com.hefu.robotphone.utils.ConectionControl;
import com.hefu.robotphone.utils.DealMapUtils;
import com.hefu.robotphone.utils.RosImageView;

import hefu.robotphone.sdk.listener.RobotInfoCallBack;
import hefu.robotphone.sdk.socket.RobotMapSocket;
import hefu.robotphone.uilibrary.customview.DirectionControlView;

public class CreatMapActivity extends AppCompatActivity {
    ActivityCreatMapBinding binding;
    RobotMapSocket robotMapSocket = RobotMapSocket.getInstance();
    boolean isCreatMap = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_creat_map);
        initClickListener();
        initData();
    }

    private void initData() {
        if (DealMapUtils.getCurrentBit() != null) {
            binding.rlBackground.setBackgroundColor(Color.parseColor("#7F7F7F"));
        }
        binding.imgMap.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.creat_map_backgroud));
        binding.imgMap.setWork_mode(RosImageView.WORK_MODE_NAVI_CREATMAP);
        robotMapSocket.setHostName(ConectionControl.getComputerIp());
        robotMapSocket.start();
        robotMapSocket.setCallBack(new RobotInfoCallBack() {
            @Override
            public void RobotInfoSuccess(final String str) {
                isCreatMap = true;
                if (isCreatMap) {
                    binding.rlBackground.setBackgroundColor(Color.parseColor("#7F7F7F"));
                }
                DealMapUtils.dellRobotMessage(str);
            }

            @Override
            public void RobotInfoFail() {

            }
        });
        binding.directionContralView.setOnDirectionListener(new DirectionControlView.OnDirectionListener() {

            @Override
            public void direction(DirectionControlView.Direction direction, float percent) {
                MainActivity.socket.goWhere(ConectionControl.getDirectionString(direction,percent));
            }
        });
    }

    private void initClickListener() {
        DealMapUtils.init(binding.imgMap);
        binding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }


    public void creatMap(View view) {
        if (MainActivity.socket.getReady()) {
            MainActivity.socket.robotCmd(ConectionControl.creatMap());
            MainActivity.socket.robotSpeak("正在创建地图");
            showSnackbar("正在创建地图");
        } else {
            showSnackbar("请扫描二维码绑定控制机器人");
        }
    }

    public void cancleCreatMap(View view) {
        if (MainActivity.socket.getReady()) {
            MainActivity.socket.robotCmd(ConectionControl.cancleCreatMap());
            MainActivity.socket.robotSpeak("取消建图");
            showSnackbar("取消建图");
        } else {
            showSnackbar("未连接机器人");
        }
    }

    public void saveMap(View view) {
        if (MainActivity.socket.getReady()) {
            MainActivity.socket.robotCmd(ConectionControl.saveMap());
            MainActivity.socket.robotSpeak("地图保存成功");
            showSnackbar("保存成功");
            binding.toolbar.postDelayed(new Runnable() {
                @Override
                public void run() {
                    finish();
                }
            }, 2000);
        } else {
            showSnackbar("未连接机器人");
        }
    }

//    public void syncMap(View view) {
//        if (MainActivity.socket.getReady()) {
//            MainActivity.socket.robotCmd(ConectionControl.syncMap());
//            showSnackbar("同步地图");
//        } else {
//            showSnackbar("未连接机器人");
//        }
//    }

    public void showSnackbar(final String str) {
        Snackbar.make(binding.toolbar, str, Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }
}
