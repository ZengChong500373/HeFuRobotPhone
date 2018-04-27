package com.hefu.robotphone.ui.fragment;

import android.view.View;

import com.hefu.robotphone.R;
import com.hefu.robotphone.databinding.FragmentRobotSpeakBinding;
import com.hefu.robotphone.ui.activity.MainActivity;

import hefu.robotphone.sdk.utlis.SystemInfoUtil;
import hefu.robotphone.sdk.utlis.ToastUtils;
import hefu.robotphone.uilibrary.base.LazyFragment;

/**
 * Created by zc on 2018/4/8.
 */

public class RobotSpeakFragment extends LazyFragment<FragmentRobotSpeakBinding> implements View.OnClickListener{
    String base = "speakTTS" + " " + SystemInfoUtil.getMac() + " 4" + " "+ " " + "888"+ " "  ;
    @Override
    public int setFragmentView() {
        return R.layout.fragment_robot_speak;
    }

    @Override
    public void onFirstUserVisible() {
        jyhBinding.edSpeak.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        String word=jyhBinding.butHello.getText().toString().trim();
        MainActivity.socket.robotCmd(base+word);
        ToastUtils.getInstance().show("nihao");
    }
}
