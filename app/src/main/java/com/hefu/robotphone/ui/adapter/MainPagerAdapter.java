package com.hefu.robotphone.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.hefu.robotphone.ui.fragment.RobotControlFragment;
import com.hefu.robotphone.ui.fragment.RobotStatusInfoFragment;

import java.util.List;



public class MainPagerAdapter  extends FragmentStatePagerAdapter {

    public MainPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        if (position==0)
        {
            return new RobotControlFragment();
        }
        return new RobotStatusInfoFragment();
    }

    @Override
    public int getCount() {

        return 2;
    }

}
