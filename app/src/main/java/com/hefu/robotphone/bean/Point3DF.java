package com.hefu.robotphone.bean;

/**
 * Created by lenovo on 2017/5/13.
 */

public class Point3DF {
    public float x;
    public float y;
    public float w;
    public Point3DF() {}

    public Point3DF(float x, float y,float w) {
        this.w = w;
        this.x = x;
        this.y = y;
    }

    public float getW() {
        return w;
    }

    public void setW(float w) {
        this.w = w;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    @Override
    public String toString() {
        return "Point3DF{" +
                "w=" + w +
                ", x=" + x +
                ", y=" + y +
                '}';
    }
}
