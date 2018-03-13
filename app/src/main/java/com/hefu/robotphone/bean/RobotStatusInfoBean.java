package com.hefu.robotphone.bean;

/**
 * 描述：机器人状态信息实体类
 * 作者：dc on 2016/9/23 15:11
 * 邮箱：597210600@qq.com
 */
public class RobotStatusInfoBean {
    private long report_time =0;
    private String robotMac = ""; // 主控地址
    private String xCoordinates = ""; //X坐标
    private String yCoordinates = ""; //Y坐标
    private String angle = ""; //角度
    private String electricity = ""; //电量
    private String voltage = ""; //电压
    private String chargeAnddischargeStatus = ""; //充放电状态：布尔值（1：机器人在放电，0：机器人正在充电）
    private String mainRobotStatus = ""; //主状态
    private String sonRobotStatus = ""; //子状态
    private String leftUltrasonic = ""; //前左超声波
    private String rightUltrasonic = ""; //前右超声波
    private String backUltrasonic = ""; //后超声波
    private String leftWheelspeed = ""; //线速度
    private String rightWheelspeed = ""; //右轮速
    private String inDropStatus = ""; //中防跌落传感器状态
    private String leftDropStatus = ""; //左防跌落传感器状态
    private String rightDropStatus = ""; //右防跌落传感器状态
    private String robotIp = "";  //主控ip
    private String pm25 = ""; //pm2.5
    private String formaldehyde = ""; //甲醛
    private String humidity = ""; //湿度
    private String co = ""; //一氧化碳


    private String temp = ""; //温度

    public long getReport_time() {
        return report_time;
    }

    public void setReport_time(long report_time) {
        this.report_time = report_time;
    }

    public String getxCoordinates() {
        return xCoordinates;
    }

    public void setxCoordinates(String xCoordinates) {
        this.xCoordinates = xCoordinates;
    }

    public String getyCoordinates() {
        return yCoordinates;
    }

    public void setyCoordinates(String yCoordinates) {
        this.yCoordinates = yCoordinates;
    }

    public String getAngle() {
        return angle;
    }

    public void setAngle(String angle) {
        this.angle = angle;
    }

    public String getElectricity() {
        return electricity;
    }

    public void setElectricity(String electricity) {
        this.electricity = electricity;
    }

    public String getVoltage() {
        return voltage;
    }

    public void setVoltage(String voltage) {
        this.voltage = voltage;
    }

    public String getChargeAnddischargeStatus() {
        return chargeAnddischargeStatus;
    }

    public void setChargeAnddischargeStatus(String chargeAnddischargeStatus) {
        this.chargeAnddischargeStatus = chargeAnddischargeStatus;
    }

    public String getMainRobotStatus() {
        return mainRobotStatus;
    }

    public void setMainRobotStatus(String mainRobotStatus) {
        this.mainRobotStatus = mainRobotStatus;
    }

    public String getSonRobotStatus() {
        return sonRobotStatus;
    }

    public void setSonRobotStatus(String sonRobotStatus) {
        this.sonRobotStatus = sonRobotStatus;
    }

    public String getLeftUltrasonic() {
        return leftUltrasonic;
    }

    public void setLeftUltrasonic(String leftUltrasonic) {
        this.leftUltrasonic = leftUltrasonic;
    }

    public String getRightUltrasonic() {
        return rightUltrasonic;
    }

    public void setRightUltrasonic(String rightUltrasonic) {
        this.rightUltrasonic = rightUltrasonic;
    }

    public String getBackUltrasonic() {
        return backUltrasonic;
    }

    public void setBackUltrasonic(String backUltrasonic) {
        this.backUltrasonic = backUltrasonic;
    }

    public String getLeftWheelspeed() {
        return leftWheelspeed;
    }

    public void setLeftWheelspeed(String leftWheelspeed) {
        this.leftWheelspeed = leftWheelspeed;
    }

    public String getRightWheelspeed() {
        return rightWheelspeed;
    }

    public void setRightWheelspeed(String rightWheelspeed) {
        this.rightWheelspeed = rightWheelspeed;
    }

    public String getInDropStatus() {
        return inDropStatus;
    }

    public void setInDropStatus(String inDropStatus) {
        this.inDropStatus = inDropStatus;
    }

    public String getLeftDropStatus() {
        return leftDropStatus;
    }

    public void setLeftDropStatus(String leftDropStatus) {
        this.leftDropStatus = leftDropStatus;
    }

    public String getRightDropStatus() {
        return rightDropStatus;
    }

    public void setRightDropStatus(String rightDropStatus) {
        this.rightDropStatus = rightDropStatus;
    }

    public String getPm25() {
        return pm25;
    }

    public void setPm25(String pm25) {
        this.pm25 = pm25;
    }

    public String getFormaldehyde() {
        return formaldehyde;
    }

    public void setFormaldehyde(String formaldehyde) {
        this.formaldehyde = formaldehyde;
    }

    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public String getRobotIp() {
        return robotIp;
    }

    public void setRobotIp(String robotIp) {
        this.robotIp = robotIp;
    }

    public String getRobotMac() {
        return robotMac;
    }

    public void setRobotMac(String robotMac) {
        this.robotMac = robotMac;
    }


    public String getTemp() {
        return temp;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }

    public String getCo() {
        return co;
    }

    public void setCo(String co) {
        this.co = co;
    }
}
