package com.hefu.robotphone.ui.fragment;



import com.hefu.robotphone.R;
import com.hefu.robotphone.bean.RobotStatusInfoBean;
import com.hefu.robotphone.bean.RobotStatusInfoGetUtil;
import com.hefu.robotphone.databinding.FragmentRobotStatusInfoBinding;
import com.hefu.robotphone.ui.activity.MainActivity;

import hefu.robotphone.sdk.listener.RobotInfoCallBack;

import hefu.robotphone.uilibrary.base.LazyFragment;

public class RobotStatusInfoFragment extends LazyFragment<FragmentRobotStatusInfoBinding> implements RobotInfoCallBack {
    @Override
    public int setFragmentView() {
        return R.layout.fragment_robot_status_info;
    }

    @Override
    public void onFirstUserVisible() {
        MainActivity.socket.setRobotInfoCallBack(this);
        jyhBinding.customViewRobotPower.setPower("0");
        jyhBinding.customViewRobotPm.setPM("0");
        jyhBinding.customViewRobotCo.setCO("0");
    }

    @Override
    public void RobotInfoSuccess(final String str) {
        jyhBinding.customViewRobotPower.post(new Runnable() {
            @Override
            public void run() {
                RobotStatusInfoBean bean= RobotStatusInfoGetUtil.getInstance().analysisRobotStatusInfo(str);
                jyhBinding.customViewRobotPower.setPower(bean.getElectricity());
                jyhBinding.customViewRobotPm.setPM(bean.getPm25());
                jyhBinding.customViewRobotCo.setCO(bean.getCo());
                jyhBinding.customViewRobotTemp.setHumy(bean.getHumidity());
                jyhBinding.customViewRobotTemp.setTemps(bean.getTemp());
                jyhBinding.customViewRobotTemp.setFTemps(bean.getTemp());
            }
        });
    }

    @Override
    public void RobotInfoFail() {

    }
}
