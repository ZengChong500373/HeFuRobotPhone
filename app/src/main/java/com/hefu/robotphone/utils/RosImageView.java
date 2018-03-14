package com.hefu.robotphone.utils;



import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Vibrator;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;


import com.hefu.robotphone.R;
import com.hefu.robotphone.bean.Point3DF;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;




/**
 * 缩放ImageView
 *
 * @author 贾瑞兴
 */
public class RosImageView extends android.support.v7.widget.AppCompatImageView {

    /**
     * 导航模式
     */
    public final static int WORK_MODE_NAVI_TASK = 0;
    /**
     * 建图模式
     */
    public final static int WORK_MODE_NAVI_CREATMAP = 1;
    private int work_mode = WORK_MODE_NAVI_TASK;

    /**
     * ImageView高度
     */
    private int imgHeight;
    /**
     * ImageView宽度
     */
    private int imgWidth;
    /**
     * 图片高度
     */
    private int intrinsicHeight;
    /**
     * 图片宽度
     */
    private int intrinsicWidth;
    /**
     * 最大缩放级别
     */
    private float mMaxScale = 5.0f;
    /**
     * 最小缩放级别
     */
    private float mMinScale = 1.0f;
    /**
     * 用于记录拖拉图片移动的坐标位置
     */
    private Matrix matrix = new Matrix();
    /**
     * 用于记录图片要进行拖拉时候的坐标位置
     */
    private Matrix currentMatrix = new Matrix();
    /**
     * 记录第一次点击的时间
     */
    private long firstTouchTime = 0;
    /**
     * 时间点击的间隔
     */
    private int intervalTime = 250;
    /**
     * 第一次点完坐标
     */
    private PointF firstPointF;
    /**
     * ros 坐标 充电桩
     */
    private PointF rosBatteryChargingPointF;
    /**
     * 机器人图片
     */
    Bitmap robotBitmap = null;
    /**
     * 选取点的图片
     */
    Bitmap pointFlagBitmap = null;
    /**
     * 地图图片
     */
    Bitmap mapBitmap = null;
    /**
     * ros原点坐标,左下角点的坐标
     */
    Point3DF rosOriginPoint3DF = new Point3DF(-100, -100, 0);
    /**
     * ros机器人坐标
     */
    Point3DF rosRobotPoint3DF = null;
    /**
     * 比例
     */
    float resolution = 0.8f;

    /**
     * 振动器
     */
    private Vibrator vibrator;

    /**
     * 按下的点
     */
    PointF pressDownPoint = null;
    /**
     * 弹起的点
     */
    PointF pressUpPoint = null;


    TouchListener touchListener = new TouchListener();
    /**
     * 任务点合集 基于ros坐标
     */
    ArrayList<Point3DF> pointFs = new ArrayList<Point3DF>();

    /**
     * 操作的时候,设置机器人位置,需要拖动的最小距离
     */
    float setRobotPositonMinDistans = 100f;



    public RosImageView(Context context) {
        super(context);
        initUI(context);

    }

    public RosImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initUI(context);
    }

    public RosImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        // TODO Auto-generated constructor stub
        initUI(context);
    }

    /**
     * 初始化UI
     */
    private void initUI(Context context) {

        vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        robotBitmap = BitmapFactory.decodeResource(getResources(),
                R.drawable.crear_map_direction);
        pointFlagBitmap = BitmapFactory.decodeResource(getResources(),
                R.mipmap.ic_launcher);
        this.setScaleType(ScaleType.FIT_CENTER);
        this.setOnTouchListener(touchListener);

        getImageViewWidthHeight();

    }

    @Override
    public void setImageDrawable(Drawable drawable) {
        super.setImageDrawable(drawable);
        getIntrinsicWidthHeight();
    }
    



    /**
     * 获得图片内在宽高
     */
    private void getIntrinsicWidthHeight() {
        BitmapDrawable drawable = (BitmapDrawable) this.getDrawable();
        mapBitmap = drawable.getBitmap();
        // 初始化bitmap的宽高
        intrinsicHeight = drawable.getIntrinsicHeight();
        intrinsicWidth = drawable.getIntrinsicWidth();
    }

    Timer timer = new Timer();
    MyTimerTask task = null;

    private final class TouchListener implements OnTouchListener {

        /**
         * 记录是拖拉照片模式还是放大缩小照片模式
         */
        private int mode = 0;// 初始状态
        /**
         * 拖拉照片模式
         */
        public static final int MODE_DRAG = 1;
        /**
         * 放大缩小照片模式
         */
        public static final int MODE_ZOOM = 2;
        /**
         * 长按选择模式
         */
        public static final int MODE_LONG_PRESS = 3;
        /**
         * 用于记录开始时候的坐标位置
         */
        private PointF startPoint = new PointF();
        /**
         * 两个手指的开始距离
         */
        private float startDis;
        /**
         * 两个手指的中间点
         */
        private PointF midPoint;

        public int getMode() {
            return mode;
        }

        public void setMode(int mode) {
            this.mode = mode;
        }

        public boolean onTouch(View v, MotionEvent event) {


            /** 通过与运算保留最后八位 MotionEvent.ACTION_MASK = 255 */
            switch (event.getAction() & MotionEvent.ACTION_MASK) {// 单点监听和多点触碰监听
                // 手指压下屏幕
                case MotionEvent.ACTION_DOWN:
                    mode = MODE_DRAG;
                    // 记录ImageView当前的移动位置
                    currentMatrix.set(getImageMatrix());
                    startPoint.set(event.getX(), event.getY());
                    matrix.set(currentMatrix);
                    PointF pointF = new PointF(event.getX(), event.getY());
                    if (work_mode == WORK_MODE_NAVI_TASK) {
                        timer = new Timer();
                        task = new MyTimerTask(pointF);
                        timer.schedule(task, 500);
                    }
                    makeImageViewFit();
                    break;
                // 手指在屏幕上移动，改事件会被不断触发
                case MotionEvent.ACTION_MOVE:
                    // 拖拉图片
                    if (mode == MODE_DRAG) {
                        // System.out.println("ACTION_MOVE_____MODE_DRAG");
                        float dx = event.getX() - startPoint.x; // 得到x轴的移动距离
                        float dy = event.getY() - startPoint.y; // 得到x轴的移动距离
                        if (Math.abs(dx) > 30 || Math.abs(dy) > 30) {
                            // 在没有移动之前的位置上进行移动z
                            if (work_mode == WORK_MODE_NAVI_TASK) timer.cancel();
                            matrix.set(currentMatrix);
                            float[] values = new float[9];
                            matrix.getValues(values);
                            dx = checkDxBound(values, dx);
                            dy = checkDyBound(values, dy);
                            matrix.postTranslate(dx, dy);
                        }
                    }
                    // 放大缩小图片
                    else if (mode == MODE_ZOOM) {
                        float endDis = distance(event);// 结束距离
                        if (endDis > 10f) { // 两个手指并拢在一起的时候像素大于10
                            float scale = endDis / startDis;// 得到缩放倍数
                            matrix.set(currentMatrix);

                            float[] values = new float[9];
                            matrix.getValues(values);

                            scale = checkFitScale(scale, values);

                            matrix.postScale(scale, scale, midPoint.x, midPoint.y);

                        }
                    } else if (mode == MODE_LONG_PRESS) {
                        float dx = event.getX() - startPoint.x; // 得到x轴的移动距离
                        float dy = event.getY() - startPoint.y; // 得到x轴的移动距离
                        pressUpPoint = new PointF(event.getX(), event.getY());
                        RosImageView.this.invalidate();
                    }
                    break;
                // 手指离开屏幕
                case MotionEvent.ACTION_UP:
                    if (work_mode == WORK_MODE_NAVI_TASK) timer.cancel();
                    setDoubleTouchEvent(event);
                    if (mode == MODE_LONG_PRESS) {
                        pressUpPoint = new PointF(event.getX(), event.getY());
                        mode = 0;
                        float dx = 0;
                        float dy = 0;

                        if (pressUpPoint != null) {
                            dx = pressUpPoint.x - pressDownPoint.x;
                            dy = pressUpPoint.y - pressDownPoint.y;
                        }

                        if (dx * dx + dy * dy > setRobotPositonMinDistans * setRobotPositonMinDistans) {//初始化位置
                            //TODO
                            double radian = 0;
                            if (dx == 0) {
                                if (dy > 0) {
                                    radian = Math.PI / 2;
                                } else {
                                    radian = -Math.PI / 2;
                                }

                            } else {
                                if (dx > 0) {
                                    radian = Math.atan((pressUpPoint.y - pressDownPoint.y) / (pressUpPoint.x - pressDownPoint.x));
                                } else if (dx < 0) {
                                    if (dy > 0) {
                                        radian = Math.PI + Math.atan((pressUpPoint.y - pressDownPoint.y) / (pressUpPoint.x - pressDownPoint.x));
                                    } else {
                                        radian = -Math.PI + Math.atan((pressUpPoint.y - pressDownPoint.y) / (pressUpPoint.x - pressDownPoint.x));
                                    }
                                }
                            }
                            Point3DF point3DF = convertMotionEvent2BasePoint(pressDownPoint);
//                            String sendMsgToken = FuwaApplication.robotInfoBean.getRobotId() + " " + SystemInfoUtil.getMac() + " 4" + ByteUtil.byteToHexStr(ByteUtil.intToByte(CodeInstructionSet.BUF_ACTION_NAVI_UPDATE_POSITION), "") + " " +
//                                    point3DF.x + " " + point3DF.y + " " + (-radian);
//                            if (!SocketServer.isConnected()) {
//                                ToastUtil.showTop("当前未连接上服务器");
//                            }
//                            new Thread(new SendMessageRunnable(null, sendMsgToken)).start();


                        } else {//选点
                            Point3DF point3DF = convertMotionEvent2BasePoint(pressDownPoint);
                            float degree = degreeOfPointBRelativeToPointA(pressDownPoint, pressUpPoint);
                            //导航点验证
//                            String sendMsgToken = FuwaApplication.robotInfoBean.getRobotId() + " " + SystemInfoUtil.getMac() + " 4" + ByteUtil.byteToHexStr(ByteUtil.intToByte(CodeInstructionSet.BUF_ACTION_NAVI_POINT_CLK_RESPONSE), "") + " " +
//                                    point3DF.x + " " + point3DF.y + " " + (degree);
//                            if (!SocketServer.isConnected()) {
//                                ToastUtil.showTop("当前未连接上服务器");
//                            }
//                            new Thread(new SendMessageRunnable(null, sendMsgToken)).start();
                            pointFs.add(convertMotionEvent2BasePoint(pressDownPoint));
                        }
                        RosImageView.this.invalidate();
                    }
                case MotionEvent.ACTION_POINTER_UP:
                    // System.out.println("ACTION_POINTER_UP");
                    if (work_mode == WORK_MODE_NAVI_TASK) timer.cancel();
                    mode = 0;
                    // matrix.set(currentMatrix);
                    float[] values = new float[9];
                    matrix.getValues(values);
                    makeImgCenter(values);
                    break;
                // 当屏幕上已经有触点(手指)，再有一个触点压下屏幕
                case MotionEvent.ACTION_POINTER_DOWN:
                    // System.out.println("ACTION_POINTER_DOWN");
                    if (work_mode == WORK_MODE_NAVI_TASK) timer.cancel();
                    mode = MODE_ZOOM;
                    /** 计算两个手指间的距离 */
                    startDis = distance(event);
                    /** 计算两个手指间的中间点 */
                    if (startDis > 10f) { // 两个手指并拢在一起的时候像素大于10
                        midPoint = mid(event);
                        // 记录当前ImageView的缩放倍数
                        currentMatrix.set(getImageMatrix());
                    }
                    break;

            }
            setImageMatrix(matrix);
            return true;
        }

        /**
         * 计算两个手指间的距离
         */
        private float distance(MotionEvent event) {
            float dx = event.getX(1) - event.getX(0);
            float dy = event.getY(1) - event.getY(0);
            /** 使用勾股定理返回两点之间的距离 */

            return (float) Math.sqrt(dx * dx + dy * dy);
        }

        /**
         * 计算两个手指间的中间点
         */
        private PointF mid(MotionEvent event) {
            float midX = (event.getX(1) + event.getX(0)) / 2;
            float midY = (event.getY(1) + event.getY(0)) / 2;
            return new PointF(midX, midY);
        }

        /**
         * 和当前矩阵对比，检验dy，使图像移动后不会超出ImageView边界
         *
         * @param values
         * @param dy
         * @return
         */
        private float checkDyBound(float[] values, float dy) {

            float height = imgHeight;
            if (intrinsicHeight * values[Matrix.MSCALE_Y] < height)
                return 0;
            if (values[Matrix.MTRANS_Y] + dy > 0)
                dy = -values[Matrix.MTRANS_Y];
            else if (values[Matrix.MTRANS_Y] + dy < -(intrinsicHeight
                    * values[Matrix.MSCALE_Y] - height))
                dy = -(intrinsicHeight * values[Matrix.MSCALE_Y] - height)
                        - values[Matrix.MTRANS_Y];
            return dy;
        }

        /**
         * 和当前矩阵对比，检验dx，使图像移动后不会超出ImageView边界
         *
         * @param values
         * @param dx
         * @return
         */
        private float checkDxBound(float[] values, float dx) {
            float width = imgWidth;
            if (intrinsicWidth * values[Matrix.MSCALE_X] < width)
                return 0;
            if (values[Matrix.MTRANS_X] + dx > 0)
                dx = -values[Matrix.MTRANS_X];
            else if (values[Matrix.MTRANS_X] + dx < -(intrinsicWidth
                    * values[Matrix.MSCALE_X] - width))
                dx = -(intrinsicWidth * values[Matrix.MSCALE_X] - width)
                        - values[Matrix.MTRANS_X];
            return dx;
        }

        /**
         * MSCALE用于处理缩放变换
         *
         *
         * MSKEW用于处理错切变换
         *
         *
         * MTRANS用于处理平移变换
         */

        /**
         * 检验scale，使图像缩放后不会超出最大倍数
         *
         * @param scale
         * @param values
         * @return
         */
        private float checkFitScale(float scale, float[] values) {
            if (scale * values[Matrix.MSCALE_X] > mMaxScale)
                scale = mMaxScale / values[Matrix.MSCALE_X];
            if (scale * values[Matrix.MSCALE_X] < mMinScale)
                scale = mMinScale / values[Matrix.MSCALE_X];
            return scale;
        }

        /**
         * 促使图片居中
         *
         * @param values (包含着图片变化信息)
         */
        private void makeImgCenter(float[] values) {

            // 缩放后图片的宽高
            float zoomY = intrinsicHeight * values[Matrix.MSCALE_Y];
            float zoomX = intrinsicWidth * values[Matrix.MSCALE_X];
            // 图片左上角Y坐标
            float leftY = values[Matrix.MTRANS_Y];
            // 图片左上角X坐标
            float leftX = values[Matrix.MTRANS_X];
            // 图片右下角Y坐标
            float rightY = leftY + zoomY;
            // 图片右下角X坐标
            float rightX = leftX + zoomX;

            // 使图片垂直居中
            if (zoomY < imgHeight) {
                float marY = (imgHeight - zoomY) / 2.0f;
                matrix.postTranslate(0, marY - leftY);
            }

            // 使图片水平居中
            if (zoomX < imgWidth) {

                float marX = (imgWidth - zoomX) / 2.0f;
                matrix.postTranslate(marX - leftX, 0);

            }

            // 使图片缩放后上下不留白（即当缩放后图片的大小大于imageView的大小，但是上面或下面留出一点空白的话，将图片移动占满空白处）
            if (zoomY >= imgHeight) {
                if (leftY > 0) {// 判断图片上面留白
                    matrix.postTranslate(0, -leftY);
                }
                if (rightY < imgHeight) {// 判断图片下面留白
                    matrix.postTranslate(0, imgHeight - rightY);
                }
            }

            // 使图片缩放后左右不留白
            if (zoomX >= imgWidth) {
                if (leftX > 0) {// 判断图片左边留白
                    matrix.postTranslate(-leftX, 0);
                }
                if (rightX < imgWidth) {// 判断图片右边不留白
                    matrix.postTranslate(imgWidth - rightX, 0);
                }
            }
        }


    }

    /**
     * 获取ImageView的宽高
     */
    private void getImageViewWidthHeight() {
        ViewTreeObserver vto2 = getViewTreeObserver();
        vto2.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
            @SuppressWarnings("deprecation")
            public void onGlobalLayout() {
                getViewTreeObserver().removeGlobalOnLayoutListener(this);
                imgWidth = getWidth();
                imgHeight = getHeight();

            }
        });
    }

    /**
     * 使得ImageView一开始便显示最适合的宽高比例，便是刚好容下的样子
     */
    private void makeImageViewFit() {
        if (getScaleType() != ScaleType.MATRIX) {
            setScaleType(ScaleType.MATRIX);
            matrix.postScale(1.0f, 1.0f, imgWidth / 2, imgHeight / 2);
        }
    }

    /**
     * 双击事件触发
     */
    private void setDoubleTouchEvent(MotionEvent event) {

        float values[] = new float[9];
        matrix.getValues(values);
        // 存储当前时间
        long currentTime = System.currentTimeMillis();
        // 判断两次点击间距时间是否符合
        if (currentTime - firstTouchTime >= intervalTime) {
            firstTouchTime = currentTime;
            firstPointF = new PointF(event.getX(), event.getY());
        } else {
            // 判断两次点击之间的距离是否小于30f
            if (Math.abs(event.getX() - firstPointF.x) < 30f
                    && Math.abs(event.getY() - firstPointF.y) < 30f) {
                // 判断当前缩放比例与最大最小的比例
                if (values[Matrix.MSCALE_X] < mMaxScale) {
                    matrix.postScale(mMaxScale / values[Matrix.MSCALE_X],
                            mMaxScale / values[Matrix.MSCALE_X], event.getX(),
                            event.getY());
                } else {
                    matrix.postScale(mMinScale / values[Matrix.MSCALE_X],
                            mMinScale / values[Matrix.MSCALE_X], event.getX(),
                            event.getY());
                }
            }

        }
    }

    /**
     * 设置图片的最大和最小的缩放比例
     *
     * @param mMaxScale
     * @param mMinScale
     */
    public void setPicZoomHeightWidth(float mMaxScale, float mMinScale) {
        this.mMaxScale = mMaxScale;
        this.mMinScale = mMinScale;
    }

    /**
     * 判断是否点击在图片外面
     *
     * @return
     */
    public boolean isTouchOutOfPic(PointF point) {
        Matrix imageMatrix = getImageMatrix();
        float[] value = new float[9];
        imageMatrix.getValues(value);
        float left = value[Matrix.MTRANS_X];
        float right = value[Matrix.MTRANS_X] + value[Matrix.MSCALE_X] * intrinsicWidth;
        float top = value[Matrix.MTRANS_Y];
        float bottom = value[Matrix.MTRANS_Y] + value[Matrix.MSCALE_Y] * intrinsicHeight;
//        Log.d("isTouchOutOfPic: ","left = "+left+",right = "+right+",top = "+top+",bottom = "+bottom);
//        Log.d("MotionEvent: ","x = "+x+",y = "+y);
        if (point.x > left && point.x < right && point.y > top && point.y < bottom) {//在里面
            return false;
        } else {
            return true;
        }
    }

    /**
     * 将点击位置转换成Ros上的点
     *
     * @return
     */
    public Point3DF convertMotionEvent2BasePoint(PointF pointF) {
        Matrix imageMatrix = getImageMatrix();
        float[] value = new float[9];
        imageMatrix.getValues(value);
        float left = value[Matrix.MTRANS_X];
        float top = value[Matrix.MTRANS_Y];
        //在图片上的坐标
        float pointX = (pointF.x - left) / value[Matrix.MSCALE_X];
        float pointY = (pointF.y - top) / value[Matrix.MSCALE_Y];

        //转化成ros坐标
        Point3DF rosPoint3DF = new Point3DF(pointX * resolution + rosOriginPoint3DF.x, (intrinsicHeight - pointY) * resolution + rosOriginPoint3DF.y, 0);

        return rosPoint3DF;
    }

    public float degreeOfPointBRelativeToPointA(PointF pointFa, PointF pointFb) {
        if (pointFa == null || pointFb == null) {
            return 0;
        }

        float dx = 0;
        float dy = 0;


        dx = pointFb.x - pointFa.x;
        dy = pointFb.y - pointFa.y;


        double radian = 0;
        if (dx == 0) {
            if (dy > 0) {
                radian = Math.PI / 2;
            } else if (dy < 0) {
                radian = -Math.PI / 2;
            } else if (dy == 0) {
                radian = 0;
            }

        } else {
            if (dx > 0) {
                radian = Math.atan((pressUpPoint.y - pressDownPoint.y) / (pressUpPoint.x - pressDownPoint.x));
            } else if (dx < 0) {
                if (dy > 0) {
                    radian = Math.PI + Math.atan((pressUpPoint.y - pressDownPoint.y) / (pressUpPoint.x - pressDownPoint.x));
                } else {
                    radian = -Math.PI + Math.atan((pressUpPoint.y - pressDownPoint.y) / (pressUpPoint.x - pressDownPoint.x));
                }
            }
        }
        return -(float) radian;
    }

    /**
     * 清空所有点
     */
    public void clearAllPoint() {
        pointFs.clear();
//        drawImage();
        postInvalidate();
    }

    /**
     * 删除一个点
     */
    public void removePoint(int index) {
        if (pointFs.size() > index) {
            pointFs.remove(index);
            postInvalidate();
        }
    }

    public int getWork_mode() {
        return work_mode;
    }

    public void setWork_mode(int work_mode) {
        this.work_mode = work_mode;
    }

    /**
     * 获取所有点
     *
     * @return
     */
    public ArrayList<Point3DF> getPointFs() {
        return pointFs;
    }

    /**
     * 设置所有点
     *
     * @return
     */
    public void setPointFs(ArrayList<Point3DF> pointFs) {
        this.pointFs = pointFs;
    }

    /**
     * 获取充电桩位置
     *
     * @return
     */
    public PointF getRosBatteryChargingPointF() {
        return rosBatteryChargingPointF;
    }

    /**
     * 设置充电桩位置
     */
    public void setRosBatteryChargingPointF(PointF rosBatteryChargingPointF) {
        this.rosBatteryChargingPointF = rosBatteryChargingPointF;
    }


    public Point3DF getRosOriginPoint3DF() {
        return rosOriginPoint3DF;
    }

    public void setRosOriginPoint3DF(Point3DF rosOriginPoint3DF) {
        this.rosOriginPoint3DF = rosOriginPoint3DF;
    }

    public float getResolution() {
        return resolution;
    }

    public void setResolution(float resolution) {
        this.resolution = resolution;
    }

    public Point3DF getRosRobotPoint3DF() {
        return rosRobotPoint3DF;
    }

    public void setRosRobotPoint3DF(Point3DF rosRobotPoint3DF) {
        this.rosRobotPoint3DF = rosRobotPoint3DF;
        postInvalidate();
    }

    class MyTimerTask extends TimerTask {
        PointF point;

        public MyTimerTask(PointF point) {
            this.point = point;
        }

        @Override
        public void run() {
            if (!isTouchOutOfPic(point)) {

//                pointFs.add(convertMotionEvent2BasePoint(point));
                touchListener.setMode(TouchListener.MODE_LONG_PRESS);
                pressDownPoint = point;
                long[] pattern = {100, 400};   // 停止 开启 停止 开启
                vibrator.vibrate(pattern, -1);           //重复两次上面的pattern 如果只想震动一次，index设为-1
                RosImageView.this.post(new Runnable() {
                    @Override
                    public void run() {
//                        drawImage();
                        RosImageView.this.postInvalidate();
//                        ToastUtil.showTop("拖动至圆外初始化机器人位置后设置任务导航点");
                    }
                });

            }
        }
    }



    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        if (rosOriginPoint3DF == null) {
            return;
        }
        Matrix imageMatrix = getImageMatrix();
        float[] value = new float[9];
        imageMatrix.getValues(value);
        float left = value[Matrix.MTRANS_X];
        float right = value[Matrix.MTRANS_X] + value[Matrix.MSCALE_X] * intrinsicWidth;
        float top = value[Matrix.MTRANS_Y];
        float bottom = value[Matrix.MTRANS_Y] + value[Matrix.MSCALE_Y] * intrinsicHeight;


        Paint paint = new Paint();
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.FILL);
        paint.setStrokeWidth(2);
        paint.setDither(true);
        paint.setFilterBitmap(true);

        Paint paint2 = new Paint();
        paint2.setColor(Color.CYAN);
        paint2.setStyle(Paint.Style.STROKE);
        paint2.setStrokeWidth(2);
        paint2.setDither(true);
        paint2.setFilterBitmap(true);

        for (Point3DF pointF : pointFs) {
            float pointX = (pointF.x - rosOriginPoint3DF.x) / resolution * value[Matrix.MSCALE_X] + left;
            float pointY = (intrinsicHeight - (pointF.y - rosOriginPoint3DF.y) / resolution) * value[Matrix.MSCALE_Y] + top;
            canvas.drawBitmap(pointFlagBitmap, pointX, pointY - pointFlagBitmap.getHeight(), paint);
//            canvas.drawCircle(pointX,pointY,10,paint);
        }


        //画机器人位置
        if (rosRobotPoint3DF != null) {
            float pointX = (rosRobotPoint3DF.x - rosOriginPoint3DF.x) / resolution * value[Matrix.MSCALE_X] + left;
            float pointY = (intrinsicHeight - (rosRobotPoint3DF.y - rosOriginPoint3DF.y) / resolution) * value[Matrix.MSCALE_Y] + top;
            float w = rosRobotPoint3DF.w;
            Matrix matrix = new Matrix();
            matrix.setRotate((float) -(w / 2 / Math.PI * 360), robotBitmap.getWidth() / 2, robotBitmap.getHeight() / 2);
            Bitmap bitmap2 = Bitmap.createBitmap(robotBitmap, 0, 0, robotBitmap.getWidth(), robotBitmap.getHeight(), matrix, true);
            canvas.drawBitmap(bitmap2, pointX - bitmap2.getWidth() / 2, pointY - bitmap2.getHeight() / 2, paint);
//            canvas.drawCircle(pointX,pointY,10,paint);
            if (robotBitmap != bitmap2) {
                bitmap2.recycle();
            }
        }

        //话操作的的提示
        if (touchListener.getMode() == TouchListener.MODE_LONG_PRESS) {
            if (pressDownPoint != null) {
                //画出选住的点的周选点和初始化位置的边界
                canvas.drawCircle(pressDownPoint.x, pressDownPoint.y, setRobotPositonMinDistans, paint2);
                if (pressUpPoint != null) {
                    //两点之间的相位差
                    float dx = pressUpPoint.x - pressDownPoint.x;
                    float dy = pressUpPoint.y - pressDownPoint.y;

                    double radian = 0;
                    double degree = 0;

                    if (dx * dx + dy * dy > setRobotPositonMinDistans * setRobotPositonMinDistans) {  //两点之间的距离
                        if (dx == 0) {
                            if (dy > 0) {
                                radian = Math.PI / 2;
                                degree = 90;
                            } else {
                                radian = -Math.PI / 2;
                                degree = -90;
                            }

                        } else {
                            if (dx > 0) {
                                radian = Math.atan((pressUpPoint.y - pressDownPoint.y) / (pressUpPoint.x - pressDownPoint.x));
                                degree = Math.toDegrees(radian);
                            } else if (dx < 0) {
                                if (dy > 0) {
                                    radian = Math.PI + Math.atan((pressUpPoint.y - pressDownPoint.y) / (pressUpPoint.x - pressDownPoint.x));
                                    degree = Math.toDegrees(radian);
                                } else {
                                    radian = -Math.PI + Math.atan((pressUpPoint.y - pressDownPoint.y) / (pressUpPoint.x - pressDownPoint.x));
                                    degree = Math.toDegrees(radian);
                                }
                            }
                        }
                        Matrix matrix = new Matrix();
                        matrix.setRotate((float) (degree), robotBitmap.getWidth() / 2, robotBitmap.getHeight() / 2);
                        Bitmap bitmap2 = Bitmap.createBitmap(robotBitmap, 0, 0, robotBitmap.getWidth(), robotBitmap.getHeight(), matrix, true);
                        canvas.drawBitmap(bitmap2, pressDownPoint.x - bitmap2.getWidth() / 2, pressDownPoint.y - bitmap2.getHeight() / 2, paint);
                        if (robotBitmap != bitmap2) {
                            bitmap2.recycle();
                        }

                    } else {//设置点
                        radian = degreeOfPointBRelativeToPointA(pressDownPoint, pressUpPoint);
                        degree = Math.toDegrees(radian);
                        Matrix matrix = new Matrix();
                        matrix.setRotate((float) (-degree), robotBitmap.getWidth() / 2, robotBitmap.getHeight() / 2);
                        Bitmap bitmap2 = Bitmap.createBitmap(robotBitmap, 0, 0, robotBitmap.getWidth(), robotBitmap.getHeight(), matrix, true);
                        canvas.drawBitmap(bitmap2, pressDownPoint.x - bitmap2.getWidth() / 2, pressDownPoint.y - bitmap2.getHeight() / 2, paint);
                        if (robotBitmap != bitmap2) {
                            bitmap2.recycle();
                        }
                    }

                }

            }
        }
    }



}