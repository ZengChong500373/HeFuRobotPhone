package com.hefu.robotphone.utils;

import com.google.gson.Gson;
import com.hefu.robotphone.bean.NavigationTaskBean;

import hefu.robotphone.sdk.utlis.ByteUtil;
import hefu.robotphone.sdk.utlis.CodeInstructionSet;
import hefu.robotphone.sdk.utlis.SystemInfoUtil;
import hefu.robotphone.sdk.utlis.spUtils;
import hefu.robotphone.uilibrary.customview.DirectionControlView;

public class ConectionControl {
    public static void setPadIp(String str) {
        spUtils.put("PADIP", str);
    }

    public static String getPadIp() {
        return (String) spUtils.get("PADIP", "");
    }

    public static void setComputerIp(String str) {
        spUtils.put("COMPUTERIP", str);
    }

    public static String getComputerIp() {
        return (String) spUtils.get("COMPUTERIP", "");
    }

    public static void setAutoLogin(Boolean isAutoLogin) {
        spUtils.put("AUTOLOGIN", isAutoLogin);
    }

    public static void setYsSerialNum(String str){
        spUtils.put("YsSerialNum", str);
    }
    public static String getYsSerialNum(){
        return (String) spUtils.get("YsSerialNum", "");
    }
    public static void setYsVerificationCode(String str){
        spUtils.put("YsVerificationCode", str);
    }
    public static Boolean isScannBindRobot(){
        return (Boolean) spUtils.get("isScannBindRobot", false);
    }

    public static void setScannBindRobot(Boolean isScann){
        spUtils.put("isScannBindRobot", isScann);
    }

    public static String getYsVerificationCode(){
        return (String) spUtils.get("YsVerificationCode", "");
    }
    public static boolean getAutoLogin() {
        return (boolean) spUtils.get("AUTOLOGIN", true);
    }

    public static void setRobotId(String id) {
        spUtils.put("ROBOTID", id);
    }

    public static String getRoboId() {
        return (String) spUtils.get("ROBOTID", "");
    }

    public static String returnOrigin() {
        Gson gson1 = new Gson();
        NavigationTaskBean navigationTaskBean = new NavigationTaskBean();
        String pointList = "0,0,0|";
        navigationTaskBean.setPointList(pointList);
        navigationTaskBean.setRobotId(getRoboId());
        navigationTaskBean.setStatus("1");
        navigationTaskBean.setName("讲解模式导航路线");
        navigationTaskBean.setIndex("5");
        navigationTaskBean.setTimeStart("0:0");
        navigationTaskBean.setTimeEnd("23:59");
        navigationTaskBean.setType("1");
        navigationTaskBean.setDateStart("");
        navigationTaskBean.setDateEnd("");
        String pathlist = gson1.toJson(navigationTaskBean);
        String sendMsgToken = getRoboId()
                + " " + SystemInfoUtil.getMac()
                + " " + ByteUtil.byteToHexStr(ByteUtil.intToByte(CodeInstructionSet.BUF_ACTION_NAVI_EVENT_QUERY), "")
                + " " + pathlist;
        return sendMsgToken;
    }

    public static String getDirectionString(DirectionControlView.Direction direction) {
        if (direction == DirectionControlView.Direction.DIRECTION_UP) {
            return getRoboId() + " " + SystemInfoUtil.getMac() + " 4" + ByteUtil.byteToHexStr(ByteUtil.intToByte(CodeInstructionSet.BUF_ACTION_FORWARD), "") + " " + 60;
        } else if (direction == DirectionControlView.Direction.DIRECTION_DOWN) {
            return getRoboId() + " " + SystemInfoUtil.getMac() + " 4" + ByteUtil.byteToHexStr(ByteUtil.intToByte(CodeInstructionSet.BUF_ACTION_BACK), "") + " " + 60;
        } else if (direction == DirectionControlView.Direction.DIRECTION_LEFT) {
            return getRoboId() + " " + SystemInfoUtil.getMac() + " 4" + ByteUtil.byteToHexStr(ByteUtil.intToByte(CodeInstructionSet.BUF_ACTION_LEFT), "") + " " + 60;
        } else if (direction == DirectionControlView.Direction.DIRECTION_RIGHT) {
            return getRoboId() + " " + SystemInfoUtil.getMac() + " 4" + ByteUtil.byteToHexStr(ByteUtil.intToByte(CodeInstructionSet.BUF_ACTION_RIGHT), "") + " " + 60;
        } else if (direction == DirectionControlView.Direction.DIRECTION_CANCEL) {
            return "";
        }
        return "";
    }

    public static String saveMap() {
        return getRoboId() + " " + SystemInfoUtil.getMac() + " 4" + ByteUtil.byteToHexStr(ByteUtil.intToByte(CodeInstructionSet.BUF_ACTION_MAKE_MAP_SAVE), "");
    }

    public static String creatMap() {
        return getRoboId() + " " + SystemInfoUtil.getMac() + " 4" + ByteUtil.byteToHexStr(ByteUtil.intToByte(CodeInstructionSet.BUF_ACTION_MAKE_MAP_START), "");
    }

    public static String cancleCreatMap() {
        return getRoboId() + " " + SystemInfoUtil.getMac() + " 4" + ByteUtil.byteToHexStr(ByteUtil.intToByte(CodeInstructionSet.BUF_ACTION_MAKE_MAP_CANCEL), "");
    }

    public static String syncMap() {
        return getRoboId() + " " + SystemInfoUtil.getMac() + " 4" + ByteUtil.byteToHexStr(ByteUtil.intToByte(CodeInstructionSet.BUF_ACTION_MAP_SYNC), "");
    }

    public static String speakWords(String str) {
        return "speakTTS" + " " + SystemInfoUtil.getMac() + " 4" + " " + " " + "888" + " " + str;
    }
}
