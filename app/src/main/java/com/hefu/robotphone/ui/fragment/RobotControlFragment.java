package com.hefu.robotphone.ui.fragment;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import com.hefu.robotphone.R;
import com.hefu.robotphone.databinding.FragmentRobotControlBinding;
import com.hefu.robotphone.ui.activity.MainActivity;
import com.hefu.robotphone.utils.ConectionControl;
import com.hefu.robotphone.utils.DealMapUtils;
import hefu.robotphone.sdk.listener.RobotInfoCallBack;
import hefu.robotphone.sdk.socket.RobotMapSocket;

import hefu.robotphone.uilibrary.base.LazyFragment;
import hefu.robotphone.uilibrary.customview.DirectionControlView;

public class RobotControlFragment extends LazyFragment<FragmentRobotControlBinding> {

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
                MainActivity.socket.goWhere(ConectionControl.getDirectionString(direction));
            }
        });
    }

    public void initMap() {
        if (MainActivity.socket.getReady() && isCreatMap == false) {
            if (DealMapUtils.getCurrentBit()!=null){
                jyhBinding.rlBackground.setBackgroundColor(Color.parseColor("#7F7F7F"));
            }
            robotMapSocket.setHostName(ConectionControl.getComputerIp());
            robotMapSocket.start();
            robotMapSocket.setCallBack(new RobotInfoCallBack() {
                @Override
                public void RobotInfoSuccess(final String str) {
                    isCreatMap = true;
                    if (isCreatMap){
                        jyhBinding.rlBackground.setBackgroundColor(Color.parseColor("#7F7F7F"));
                    }
                    DealMapUtils.dellRobotMessage(str);
                }
                @Override
                public void RobotInfoFail() {

                }
            });
      }
    }



    @Override
    public void onResume() {
        super.onResume();
        jyhBinding.imgMap.postDelayed(new Runnable() {
            @Override
            public void run() {
                initMap();
                Bitmap bitmap=DealMapUtils.getCurrentBit();
                if (bitmap!=null){
                    jyhBinding.imgMap.setImageBitmap(bitmap);
                }
            }
        },800);
    }
}
