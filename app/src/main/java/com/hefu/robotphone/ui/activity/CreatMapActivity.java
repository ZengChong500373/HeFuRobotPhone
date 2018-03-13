package com.hefu.robotphone.ui.activity;

import android.databinding.DataBindingUtil;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.hefu.robotphone.R;
import com.hefu.robotphone.databinding.ActivityCreatMapBinding;
import com.hefu.robotphone.utils.ConectionControl;
import com.hefu.robotphone.utils.DealMapUtils;
import com.hefu.robotphone.utils.RosImageView;

import hefu.robotphone.sdk.listener.RobotInfoCallBack;
import hefu.robotphone.sdk.socket.RobotMapSocket;
import hefu.robotphone.sdk.utlis.ByteUtil;
import hefu.robotphone.sdk.utlis.CodeInstructionSet;
import hefu.robotphone.sdk.utlis.SystemInfoUtil;
import hefu.robotphone.uilibrary.customview.DirectionControlView;

public class CreatMapActivity extends AppCompatActivity {
    ActivityCreatMapBinding binding;
    RobotMapSocket robotMapSocket = RobotMapSocket.getInstance();
    String sendMsgToken;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_creat_map);
        initClickListener();
        initData();
    }

    private void initData() {
        String sendMsg = ConectionControl.getPadIp() + " " + SystemInfoUtil.getMac() + " 4" + ByteUtil.byteToHexStr(ByteUtil.intToByte(CodeInstructionSet.BUF_ACTION_MAKE_MAP_START), "");
        MainActivity.socket.robotCmd(sendMsg);
        binding.imgMap.setImageBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.creat_map_backgroud));
        binding.imgMap.setWork_mode(RosImageView.WORK_MODE_NAVI_CREATMAP);
        robotMapSocket.setHostName(ConectionControl.getComputerIp());
        robotMapSocket.start();
        robotMapSocket.setCallBack(new RobotInfoCallBack() {
            @Override
            public void RobotInfoSuccess(final String str) {
                DealMapUtils.dellRobotMessage(str);
            }

            @Override
            public void RobotInfoFail() {

            }
        });
        binding.directionContralView.setOnDirectionListener(new DirectionControlView.OnDirectionListener() {
            @Override
            public void direction(DirectionControlView.Direction direction) {
                if (direction == DirectionControlView.Direction.DIRECTION_UP) {
                    sendMsgToken = ConectionControl.getRoboId() + " " + SystemInfoUtil.getMac() + " 4" + ByteUtil.byteToHexStr(ByteUtil.intToByte(CodeInstructionSet.BUF_ACTION_FORWARD), "") + " " + 60;
                    MainActivity.socket.goWhere(sendMsgToken);
                } else if (direction == DirectionControlView.Direction.DIRECTION_DOWN) {
                    sendMsgToken = ConectionControl.getRoboId() + " " + SystemInfoUtil.getMac() + " 4" + ByteUtil.byteToHexStr(ByteUtil.intToByte(CodeInstructionSet.BUF_ACTION_BACK), "") + " " + 60;
                    MainActivity.socket.goWhere(sendMsgToken);
                } else if (direction == DirectionControlView.Direction.DIRECTION_LEFT) {
                    sendMsgToken = ConectionControl.getRoboId() + " " + SystemInfoUtil.getMac() + " 4" + ByteUtil.byteToHexStr(ByteUtil.intToByte(CodeInstructionSet.BUF_ACTION_LEFT), "") + " " + 60;
                    MainActivity.socket.goWhere(sendMsgToken);
                } else if (direction == DirectionControlView.Direction.DIRECTION_RIGHT) {
                    sendMsgToken = ConectionControl.getRoboId() + " " + SystemInfoUtil.getMac() + " 4" + ByteUtil.byteToHexStr(ByteUtil.intToByte(CodeInstructionSet.BUF_ACTION_RIGHT), "") + " " + 60;
                    MainActivity.socket.goWhere(sendMsgToken);
                } else if (direction == DirectionControlView.Direction.DIRECTION_CANCEL) {
                    MainActivity.socket.goWhere("");
                }
            }
        });
    }

    private void initClickListener() {
      DealMapUtils.init(binding.imgMap);
        binding.toolbar.inflateMenu(R.menu.activity_creat_map_menu);
        binding.toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_save_map:
                        Snackbar.make(binding.toolbar, "保存地图成功", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                        break;
                }
                return false;
            }
        });
        binding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }


}
