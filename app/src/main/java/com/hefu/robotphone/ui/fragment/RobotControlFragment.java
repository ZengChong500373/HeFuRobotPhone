package com.hefu.robotphone.ui.fragment;


import android.graphics.BitmapFactory;

import com.hefu.robotphone.R;
import com.hefu.robotphone.databinding.FragmentRobotControlBinding;
import com.hefu.robotphone.ui.activity.MainActivity;
import com.hefu.robotphone.utils.ConectionControl;
import com.hefu.robotphone.utils.DealMapUtils;

import hefu.robotphone.sdk.listener.RobotInfoCallBack;
import hefu.robotphone.sdk.socket.RobotMapSocket;
import hefu.robotphone.sdk.utlis.ByteUtil;
import hefu.robotphone.sdk.utlis.CodeInstructionSet;
import hefu.robotphone.sdk.utlis.SystemInfoUtil;
import hefu.robotphone.uilibrary.base.LazyFragment;
import hefu.robotphone.uilibrary.customview.DirectionControlView;

public class RobotControlFragment extends LazyFragment<FragmentRobotControlBinding> {
    String sendMsgToken;
    RobotMapSocket robotMapSocket = RobotMapSocket.getInstance();
    boolean isCreatMap = false;

    @Override
    public int setFragmentView() {
        return R.layout.fragment_robot_control;
    }

    @Override
    public void onFirstUserVisible() {
        DealMapUtils.init(jyhBinding.imgMap);
        jyhBinding.imgMap.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.creat_map_backgroud));
        jyhBinding.directionContralView.setOnDirectionListener(new DirectionControlView.OnDirectionListener() {
            @Override
            public void direction(DirectionControlView.Direction direction) {
                initMap();
                MainActivity.socket.goWhere(ConectionControl.getDirectionString(direction));
            }
        });
    }

    public void initMap() {
        if (MainActivity.socket.getReady() && isCreatMap == false) {
            String sendMsg = ConectionControl.getPadIp() + " " + SystemInfoUtil.getMac() + " 4" + ByteUtil.byteToHexStr(ByteUtil.intToByte(CodeInstructionSet.BUF_ACTION_MAKE_MAP_START), "");
            MainActivity.socket.robotCmd(sendMsg);
            robotMapSocket.setHostName(ConectionControl.getComputerIp());
            robotMapSocket.start();
            robotMapSocket.setCallBack(new RobotInfoCallBack() {
                @Override
                public void RobotInfoSuccess(final String str) {
                    isCreatMap = true;
                    DealMapUtils.dellRobotMessage(str);
                }

                @Override
                public void RobotInfoFail() {

                }
            });
        }

    }
}
