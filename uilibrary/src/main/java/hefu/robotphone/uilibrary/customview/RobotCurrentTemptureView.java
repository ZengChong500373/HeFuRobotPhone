package hefu.robotphone.uilibrary.customview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import hefu.robotphone.uilibrary.R;


/**
 * Created by HF_05 on 2017/8/11.
 */

public class RobotCurrentTemptureView extends View {

    private Paint mBackgroundPaint;
    private Paint mTextPain;
    private Paint mIconPain;

    private int mMeasureWidth;
    private int mMeasureHeight;

    private String textTemp = "26℃";
    private String textHumidity = "75%";
    private String textFtemp = "78.8℉";

    /**
     * Sensor参数
     **/
    private float sensorParam = 0f;
    private float sensorParamTrans = 0f;

    private String textParam = "";
    private String textParam2 = "Test";
    private String textParam3 = "Test";

    public RobotCurrentTemptureView(Context context)
    {
        super(context);
        initView();
    }
    public RobotCurrentTemptureView(Context context, AttributeSet attributeSet)
    {
        super(context,attributeSet);
        initView();
    }
    public RobotCurrentTemptureView(Context context, AttributeSet attributeSet, int defStyleValue)
    {
        super(context,attributeSet,defStyleValue);
        initView();
    }
    public void initView()
    {
        mBackgroundPaint = new Paint();
        mBackgroundPaint.setColor(Color.DKGRAY);
        mBackgroundPaint.setAntiAlias(true);
        mBackgroundPaint.setAlpha(50);
        mBackgroundPaint.setStyle(Paint.Style.FILL);

        mTextPain = new Paint();
        mTextPain.setColor(Color.WHITE);
        mTextPain.setAntiAlias(true);
        mTextPain.setStyle(Paint.Style.FILL);

    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
        mMeasureHeight = MeasureSpec.getSize(heightMeasureSpec);
        mMeasureWidth = MeasureSpec.getSize(widthMeasureSpec);
        setMeasuredDimension(mMeasureWidth,mMeasureHeight);
    }

    @Override
    public void onDraw(Canvas canvas){
        canvas.save();
        canvas.translate(mMeasureWidth/2,mMeasureHeight/2);
        RectF rectf1 = new RectF(-mMeasureWidth/2f,-mMeasureHeight/2f,mMeasureWidth/2f,mMeasureHeight/2f);
        canvas.drawRoundRect(rectf1,30f,30f,mBackgroundPaint);

        Rect rectText1 = new Rect();
        mTextPain.setTextSize(mMeasureWidth/4);
        mTextPain.getTextBounds(textTemp,0,textTemp.length(),rectText1);

        int textTempWidth = rectText1.width();
        int textTempHeight = rectText1.height();

        canvas.drawText(textTemp,-mMeasureWidth/2+10,textTempHeight/4,mTextPain);

        Rect rectText2 = new Rect();
        mTextPain.setTextSize(mMeasureWidth/10);
        mTextPain.getTextBounds(textHumidity,0,textHumidity.length(),rectText2);

        int textHumidityWidth = rectText2.width();
        int textHumidityHeight = rectText2.height();

        canvas.drawText(textHumidity,mMeasureWidth/2-50-textHumidityWidth,mMeasureHeight/5,mTextPain);

        Rect rectText3 = new Rect();
        mTextPain.setTextSize(mMeasureWidth/10);
        mTextPain.getTextBounds(textFtemp,0,textFtemp.length(),rectText3);

        int textTempFWidth = rectText3.width();
        int textTempFHeight = rectText3.height();

        canvas.drawText(textFtemp,mMeasureWidth/2-50-textTempFWidth,-mMeasureHeight/5+textTempFHeight/2,mTextPain);

        BitmapDrawable bitmapDrawable = (BitmapDrawable)getResources().getDrawable(R.drawable.temp);
        Bitmap mBitmap = bitmapDrawable.getBitmap();

        RectF rectImage = new RectF(mMeasureWidth/2-50-textTempFWidth-textTempFHeight/2f,-mMeasureHeight/5+textTempFHeight/2-textTempFHeight/1.1f,mMeasureWidth/2-50-textTempFWidth,-mMeasureHeight/5+textTempFHeight/2);
        canvas.drawBitmap(mBitmap,null,rectImage,mIconPain);

        BitmapDrawable bitmapDrawable2 = (BitmapDrawable)getResources().getDrawable(R.drawable.humy);
        Bitmap mBitmap2 = bitmapDrawable2.getBitmap();

        RectF rectImage2 = new RectF(mMeasureWidth/2-50-textTempFWidth-textTempFHeight/2f,mMeasureHeight/5-textTempFHeight/1.1f,mMeasureWidth/2-50-textTempFWidth,mMeasureHeight/5);
        canvas.drawBitmap(mBitmap2,null,rectImage2,mIconPain);

    }

    public void setTemps(String temps) {
        if (TextUtils.isEmpty(temps)){
            return;
        }
        sensorParam = getFloat(temps);
        sensorParamTrans = sensorParam;
        textTemp = String.valueOf((int) sensorParam)+ "℃";
        invalidate();
    }

    public void setFTemps(String Ftemps) {
        if (TextUtils.isEmpty(Ftemps)){
            return;
        }
        sensorParam = getFloat(Ftemps)*1.8F+32;
        sensorParamTrans = sensorParam;
        textFtemp = String.valueOf((int) sensorParam)+ "℉";
        invalidate();
    }

    public void setHumy(String humy) {
        if (TextUtils.isEmpty(humy)){
            return;
        }
        sensorParam = getFloat(humy);
        sensorParamTrans = sensorParam;
        textHumidity= String.valueOf(sensorParam)+"%";
        textHumidity = textHumidity.substring(0,textHumidity.indexOf("."))+"%";
        invalidate();
    }
    public float getFloat(String str) {
        return Float.parseFloat(str);
    }

}
