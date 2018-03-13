package com.hefu.robotphone.bean;

import java.io.Serializable;

/**
 * Created by lenovo on 2017/5/18.
 */

public class NavigationTaskBean implements Serializable{


    /**
     * dateEnd : string
     * dateStart : string
     * index : string
     * name : string
     * pointList : string
     * remark : string
     * robotId : string
     * status : string
     * timeEnd : string
     * timeStart : string
     * type : string
     */

    private String dateEnd;
    private String dateStart;
    private String index;
    private String name;
    private String pointList;
    private String remark;
    private String robotId;
    private String status;
    private String timeEnd;
    private String timeStart;
    private String type;

    public void setDateEnd(String dateEnd) {
        this.dateEnd = dateEnd;
    }

    public void setDateStart(String dateStart) {
        this.dateStart = dateStart;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPointList(String pointList) {
        this.pointList = pointList;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public void setRobotId(String robotId) {
        this.robotId = robotId;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setTimeEnd(String timeEnd) {
        this.timeEnd = timeEnd;
    }

    public void setTimeStart(String timeStart) {
        this.timeStart = timeStart;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDateEnd() {
        return dateEnd;
    }

    public String getDateStart() {
        return dateStart;
    }

    public String getIndex() {
        return index;
    }

    public String getName() {
        return name;
    }

    public String getPointList() {
        return pointList;
    }

    public String getRemark() {
        return remark;
    }

    public String getRobotId() {
        return robotId;
    }

    public String getStatus() {
        return status;
    }

    public String getTimeEnd() {
        return timeEnd;
    }

    public String getTimeStart() {
        return timeStart;
    }

    public String getType() {
        return type;
    }

}
