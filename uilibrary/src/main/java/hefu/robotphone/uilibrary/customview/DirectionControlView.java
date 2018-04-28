package hefu.robotphone.uilibrary.customview;

import android.content.Context;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;


public class DirectionControlView extends View {
    Paint smallCirclePaint, bigCirclePaint, linPaint, moveCirclePanit, maxCirclePaint;
    int centerX, centerY;
    int smallRadius, bigRadius;
    int lenth, halflenth;

    float movecenterX = 0;
    float movecenterY = 0;

    // 360°平分4份的边缘角度(旋转45度)
    private static final double ANGLE_ROTATE45_4D_OF_0P = 45;
    private static final double ANGLE_ROTATE45_4D_OF_1P = 135;
    private static final double ANGLE_ROTATE45_4D_OF_2P = 225;
    private static final double ANGLE_ROTATE45_4D_OF_3P = 315;
    // 角度
    private static final double ANGLE_0 = 0;
    private static final double ANGLE_360 = 360;

    public DirectionControlView(Context context) {
        super(context);
        initView();
    }

    public DirectionControlView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public DirectionControlView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }


    public void initView() {
        smallCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        smallCirclePaint.setColor(Color.parseColor("#31bfcf"));
        smallCirclePaint.setStyle(Paint.Style.STROKE);            //设置空心实心STROKE空心  FILL实心
        smallCirclePaint.setStrokeWidth(2);

        bigCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        bigCirclePaint.setColor(Color.parseColor("#33000000"));
        bigCirclePaint.setStyle(Paint.Style.FILL);            //设置空心实心STROKE空心  FILL实心
        bigCirclePaint.setStrokeWidth(6);

        linPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        linPaint.setColor(Color.WHITE);
        linPaint.setStrokeWidth(6);

        moveCirclePanit = new Paint(Paint.ANTI_ALIAS_FLAG);
        moveCirclePanit.setColor(Color.parseColor("#8CFFFFFF"));
        moveCirclePanit.setStyle(Paint.Style.FILL);
        moveCirclePanit.setStrokeWidth(6);

        maxCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        maxCirclePaint.setColor(Color.parseColor("#BFFFFFFF"));
        maxCirclePaint.setStyle(Paint.Style.STROKE);            //设置空心实心STROKE空心  FILL实心
        maxCirclePaint.setStrokeWidth(18);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int w = MeasureSpec.getSize(widthMeasureSpec);
        int h = MeasureSpec.getSize(heightMeasureSpec);
        if (w >= h) {
            bigRadius = (int) ((h * 0.7) / 2);
            smallRadius = (int) (bigRadius * 0.4);
        } else {
            bigRadius = (int) ((w * 0.8) / 2);
            smallRadius = bigRadius / 2;
        }
        setMeasuredDimension(w, h);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        centerX = getWidth() / 2;
        centerY = getHeight() / 2;
        lenth = (bigRadius - smallRadius) / 2;
        halflenth = (bigRadius - smallRadius) / 4;
        canvas.translate(centerX, centerY);

        canvas.drawCircle(0, 0, bigRadius, bigCirclePaint);
        canvas.drawCircle(0, 0, smallRadius, smallCirclePaint);
        canvas.drawCircle(movecenterX, movecenterY, smallRadius - 4, moveCirclePanit);
        canvas.drawCircle(0, 0, bigRadius + 2, maxCirclePaint);
//右
        canvas.drawLine(lenth + smallRadius, 0, halflenth + smallRadius, halflenth, linPaint);
        canvas.drawLine(lenth + smallRadius, 0, halflenth + smallRadius, -halflenth, linPaint);
//左
        canvas.drawLine(-(lenth + smallRadius), 0, -(halflenth + smallRadius), halflenth, linPaint);
        canvas.drawLine(-(lenth + smallRadius), 0, -(halflenth + smallRadius), -halflenth, linPaint);

        //上
        canvas.drawLine(0, -(lenth + smallRadius), halflenth, -(halflenth + smallRadius), linPaint);
        canvas.drawLine(0, -(lenth + smallRadius), -halflenth, -(halflenth + smallRadius), linPaint);
        //下
        canvas.drawLine(0, lenth + smallRadius, halflenth, halflenth + smallRadius, linPaint);
        canvas.drawLine(0, lenth + smallRadius, -halflenth, halflenth + smallRadius, linPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:// 按下
//                Log.e("jyh_down", "downx=" + event.getY() + " downy=" + event.getY());
                break;
            case MotionEvent.ACTION_MOVE:// 移动
//                Log.e("jyh_move", "movex=" + event.getY() + " movey=" + event.getY());
                event2Point(event.getX(), event.getY());
                break;
            case MotionEvent.ACTION_UP:// 抬起
                event2Point(centerX, centerY);
                if (listener != null) {
                    listener.direction(Direction.DIRECTION_CANCEL, 0);
                }
            case MotionEvent.ACTION_CANCEL:// 移出区域
                break;
        }
        return true;
    }

    public void event2Point(float endx, float endy) {
        // 两点在X轴的距离
        float lenX = (float) (endx - centerX);
        // 两点在Y轴距离
        float lenY = (float) (endy - centerY);
        // 两点距离
        float lenXY = (float) Math.sqrt((double) (lenX * lenX + lenY * lenY));
        // 计算弧度
        double radian = Math.acos(lenX / lenXY) * (endy < centerY ? -1 : 1);
        float percent = 0;
        if (lenXY <= bigRadius) {
            movecenterX = lenX;
            movecenterY = lenY;
            percent = lenXY / bigRadius;
            invalidate();
        } else {
            movecenterX = (int) (centerX + (bigRadius) * Math.cos(radian)) - centerX;
            movecenterY = (int) (centerY + (bigRadius) * Math.sin(radian)) - centerY;
            percent = 1;
            invalidate();
        }

        double angle = radian2Angle(radian);
        calculateDirection(angle, percent);
    }

    /**
     * 通过角度计算方向
     */
    public void calculateDirection(double angle, float percent) {

        if (listener == null) return;
        if (ANGLE_0 <= angle && ANGLE_ROTATE45_4D_OF_0P > angle || ANGLE_ROTATE45_4D_OF_3P <= angle && ANGLE_360 > angle) {
            // 右
            listener.direction(Direction.DIRECTION_RIGHT, percent);
        } else if (ANGLE_ROTATE45_4D_OF_0P <= angle && ANGLE_ROTATE45_4D_OF_1P > angle) {
            // 下
            listener.direction(Direction.DIRECTION_DOWN, percent);
        } else if (ANGLE_ROTATE45_4D_OF_1P <= angle && ANGLE_ROTATE45_4D_OF_2P > angle) {
            // 左
            listener.direction(Direction.DIRECTION_LEFT, percent);
        } else if (ANGLE_ROTATE45_4D_OF_2P <= angle && ANGLE_ROTATE45_4D_OF_3P > angle) {
            // 上

            listener.direction(Direction.DIRECTION_UP, percent);
        }
    }

    /**
     * 弧度转角度
     *
     * @param radian 弧度
     * @return 角度[0, 360)
     */
    private double radian2Angle(double radian) {
        double tmp = Math.round(radian / Math.PI * 180);
        return tmp >= 0 ? tmp : 360 + tmp;
    }

    public interface OnDirectionListener {
        void direction(Direction enumDirection, float percent);
    }

    public OnDirectionListener listener;

    public void setOnDirectionListener(OnDirectionListener listener) {
        this.listener = listener;
    }

    /**
     * 方向
     */
    public enum Direction {
        DIRECTION_LEFT, // 左
        DIRECTION_RIGHT, // 右
        DIRECTION_UP, // 上
        DIRECTION_DOWN, // 下
        DIRECTION_CANCEL //取消
    }

    @Override
    protected void onWindowVisibilityChanged(int visibility) {
        super.onWindowVisibilityChanged(visibility);
        if (visibility == GONE) {
            if (listener != null) {
                event2Point(centerX, centerY);
                listener.direction(Direction.DIRECTION_CANCEL, 0);
            }

        }
    }
}
