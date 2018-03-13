package com.hefu.robotphone.bean;


import hefu.robotphone.sdk.utlis.MyLog;

/**
 * 描述：解析机器人状态信息类
 * 作者：dc on 2016/9/23 15:26
 * 邮箱：597210600@qq.com
 */
public class RobotStatusInfoGetUtil {
    private static final String TAG = RobotStatusInfoGetUtil.class.getSimpleName();
    private static RobotStatusInfoGetUtil instance = null;

    private RobotStatusInfoGetUtil() {
    }

    public static synchronized RobotStatusInfoGetUtil getInstance() {
        if (instance == null) {
            instance = new RobotStatusInfoGetUtil();
        }
        return instance;
    }

    /**
     * @param info 机器人状态信息
     * @return 机器人状态信息实体类
     * @descriptoin 解析机器人状态信息 1 000C29C8109F 00E 10.01,20.2,150.15 55,23.4,1 1,0 3.56,4.75,5.00 1.63,2.5 0,0,0 192.168.2.219 0.01,28,50,0.2 \
     * @author dc
     * @date 2016/9/23 15:29
     */
    public synchronized RobotStatusInfoBean analysisRobotStatusInfo(String info) {
        RobotStatusInfoBean bean = new RobotStatusInfoBean();
        bean.setReport_time(System.currentTimeMillis());
        try {
            String robotStatusInfo[] = info.split(" ");
            if (robotStatusInfo.length >= 10) {
                String robotMac = robotStatusInfo[1].toString();  //主控地址
                bean.setRobotMac(robotMac);

                String locationInfo[] = robotStatusInfo[3].split(","); //位置信息
                bean.setxCoordinates(locationInfo[0]); //x
                bean.setyCoordinates(locationInfo[1]); //y
                bean.setAngle(locationInfo[2]); //角度

                String electricityInfo[] = robotStatusInfo[4].split(","); //电量信息
                bean.setElectricity(electricityInfo[0]); //电量
                bean.setVoltage(electricityInfo[1]); //电压
                bean.setChargeAnddischargeStatus(electricityInfo[2]); //充放电状态：布尔值（1：机器人在放电，0：机器人正在充电）

                String statusInfo[] = robotStatusInfo[5].split(","); //机器人状态信息
                bean.setMainRobotStatus(statusInfo[0]); //主状态
                if (4 == Integer.valueOf(statusInfo[0])) {  //主状态为状态状态，表示还有子状态
                    if (statusInfo.length > 1) {
                        bean.setSonRobotStatus(statusInfo[1]);
                    }
                }

                String ultrasonicInfo[] = robotStatusInfo[6].split(","); //超声波信息
                bean.setLeftUltrasonic(ultrasonicInfo[0]); //左
                bean.setRightUltrasonic(ultrasonicInfo[1]); //右
                bean.setBackUltrasonic(ultrasonicInfo[2]);

                String wheelspeedInfo[] = robotStatusInfo[7].split(","); //左右轮速
                bean.setLeftWheelspeed(wheelspeedInfo[0]); //线速度
                bean.setRightWheelspeed(wheelspeedInfo[1]); //角速度

                String dropStatusInfo[] = robotStatusInfo[8].split(","); //防跌落传感器状态
                bean.setLeftDropStatus(dropStatusInfo[0]); //左
                bean.setInDropStatus(dropStatusInfo[1]); //中
                bean.setRightDropStatus(dropStatusInfo[2]); //右

                String robotIp = robotStatusInfo[9].toString(); //主控ip
                bean.setRobotIp(robotIp);

                String environmentInfo[] = robotStatusInfo[10].split(","); //环境信息
                bean.setPm25(environmentInfo[0]); //pm
                bean.setTemp(environmentInfo[1]);  //温度
                bean.setHumidity(environmentInfo[2]); //湿度
                bean.setCo(environmentInfo[3]); //co


            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bean;
    }
}
