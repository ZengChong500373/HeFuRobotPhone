package com.hefu.robotphone.bean;

import com.hefu.robotphone.utils.ConectionControl;

public class RobotBean {
    String robot_id;
    String fcamid;
    String fpass_word;
    String pad_mac;
    String computer_mac;
    String enter = "\n";
    public String getPad_mac() {
        return pad_mac;
    }

    public void setPad_mac(String pad_mac) {
        this.pad_mac = pad_mac;
    }


    public String getRobot_id() {
        return robot_id;
    }

    public void setRobot_id(String robot_id) {
        this.robot_id = robot_id;
    }

    public String getFcamid() {
        return fcamid;
    }

    public void setFcamid(String fcamid) {
        this.fcamid = fcamid;
    }

    public String getFpass_word() {
        return fpass_word;
    }

    public void setFpass_word(String fpass_word) {
        this.fpass_word = fpass_word;
    }



    public String getComputer_mac() {
        return computer_mac;
    }
    public void setComputer_mac(String computer_mac) {
        this.computer_mac = computer_mac;
    }
    public void setData(String str){
        String[]  strs=str.split(enter);
        setRobot_id(strs[0]);
        ConectionControl.setRobotId(strs[0]);
        setFcamid(strs[1]);
        setFpass_word(strs[2]);
        setPad_mac(strs[3]);
        setComputer_mac(strs[4]);


    }




}
