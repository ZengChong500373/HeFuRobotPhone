package hefu.robotphone.uilibrary.customview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;


/**
 * Created by shuai on 2017/8/6.
 */

public class RobotStatusInfosView extends View {


    /**
     * 画笔参数
     **/
    private Paint mInstrumentPaintD;
    private Paint mInstrumentPaintC;
    private Paint mInstrumentPaintNI;
    private Paint mInstrumentPaintNO;


    /**
     * 屏幕参数
     **/
    private int mMeasureWidth;
    private int mMeasureHeight;

    /**
     * Sensor参数
     **/
    private float sensorParam = 0f;
    private float sensorParamTrans = 0f;

    private String textParam = "";
    private String textParam2 = "";
    private String textParam3 = "Test";


    public RobotStatusInfosView(Context context) {
        super(context);
        initView();
    }

    public RobotStatusInfosView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public RobotStatusInfosView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    public void initView() {

        /**设置默认仪表盘画笔**/
        mInstrumentPaintD = new Paint();
        mInstrumentPaintD.setColor(Color.GRAY);                  //GRAY
        mInstrumentPaintD.setAntiAlias(true);                    //抗锯齿
        mInstrumentPaintD.setStyle(Paint.Style.STROKE);            //设置空心实心STROKE空心  FILL实心
        mInstrumentPaintD.setStrokeWidth(12);
        mInstrumentPaintD.setAlpha(50);

        /**设置进度画笔**/
        mInstrumentPaintC = new Paint();
        mInstrumentPaintC.setColor(Color.WHITE);
        mInstrumentPaintC.setAntiAlias(true);
        mInstrumentPaintC.setStyle(Paint.Style.STROKE);
        mInstrumentPaintC.setStrokeWidth(12);

        /**设置框内数字画笔**/
        mInstrumentPaintNI = new Paint();
        mInstrumentPaintNI.setColor(Color.WHITE);
        mInstrumentPaintNI.setAntiAlias(true);
        mInstrumentPaintNI.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));

        /**设置框外数字画笔**/
        mInstrumentPaintNO = new Paint();
        mInstrumentPaintNO.setColor(Color.WHITE);
        mInstrumentPaintNO.setAntiAlias(true);
        mInstrumentPaintNO.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD_ITALIC));
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        mMeasureHeight = MeasureSpec.getSize(heightMeasureSpec);
        mMeasureWidth = MeasureSpec.getSize(widthMeasureSpec);
        setMeasuredDimension(mMeasureWidth, mMeasureHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.save();
        canvas.translate(mMeasureWidth / 2, mMeasureHeight / 2);
        canvas.drawCircle(0, 0, mMeasureWidth / 3 + mMeasureWidth / 15, mInstrumentPaintD);
        RectF rect = new RectF(-(mMeasureWidth / 3 + mMeasureWidth / 15), -(mMeasureWidth / 3 + mMeasureWidth / 15), mMeasureWidth / 3 + mMeasureWidth / 15, mMeasureWidth / 3 + mMeasureWidth / 15);
        canvas.drawArc(rect, 0 + 90, sensorParamTrans, false, mInstrumentPaintC);

        Rect rect1 = new Rect();
        mInstrumentPaintNI.setTextSize(mMeasureWidth / 5);
        mInstrumentPaintNI.getTextBounds(textParam, 0, textParam.length(), rect1);
        int textWidth = rect1.width();
        int textHeight = rect1.height();
        canvas.drawText(textParam, -textWidth / 2, textHeight / 3, mInstrumentPaintNI);


        Rect rect2 = new Rect();
        mInstrumentPaintNO.setTextSize(mMeasureWidth / 6);
        mInstrumentPaintNO.getTextBounds(textParam2, 0, textParam2.length(), rect2);

        int textWidth2 = rect2.width();
        int textHeight2 = rect2.height();
        canvas.drawText(textParam2, -textWidth2 / 2, mMeasureWidth / 2 + mMeasureWidth / 8, mInstrumentPaintNO);
        canvas.restore();
    }

    public void setPower(String power) {
        if (TextUtils.isEmpty(power)){
            return;
        }
        sensorParam = getFloat(power);
        sensorParamTrans = (float) (3.6 * sensorParam);
        textParam = (int) sensorParam + "%";
        textParam2 = "电量:" + (int) sensorParam + "%";
        invalidate();
    }


    public void setPM(String PM) {
        if (TextUtils.isEmpty(PM)){
            return;
        }
        sensorParam = getFloat(PM);
        sensorParamTrans = sensorParam;
        textParam = String.valueOf((int) sensorParam);
        textParam2 = "PM2.5:" + (int) sensorParam;
        textParam3 = "μg/m³";
        invalidate();
    }

    public void setCO(String CO) {
        if (TextUtils.isEmpty(CO)){
            return;
        }
        sensorParam = getFloat(CO);
        sensorParamTrans = sensorParam;
        textParam = String.valueOf(sensorParam);
        textParam2 = "CO:" + sensorParam;
        textParam3 = "ppm";
        invalidate();
    }

    public float getFloat(String str) {
        return Float.parseFloat(str);
    }

}
