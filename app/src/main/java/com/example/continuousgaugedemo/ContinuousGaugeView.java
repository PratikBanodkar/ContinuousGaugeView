package com.example.continuousgaugedemo;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;

public class ContinuousGaugeView extends View {

    private static String TAG = ContinuousGaugeView.class.getSimpleName();
    private Paint mBackgroundArcPaint;
    private static int mColorBackground = Color.GRAY;


    private float strokeWidthBg = 35;
    private float left = 60f, right = 200f, top = 60f, bottom = 200f;
    private float value = 0f;
    float valueSweepAngle = 0f;
    float fullSweepAngleBasedOnGaugeStyle = 0f;
    float startAngle = 270f;
    float endAngle = 0f;

    private int mNumberOfSections;
    private double[] mSectionLimitValues;
    private int[] mSectionColors;
    private ArrayList<Paint> mAllPaintsNeeded = new ArrayList<>();
    private ArrayList<Double> mLimitAnglesNeeded = new ArrayList<>();

    //region CONSTRUCTORS
    public ContinuousGaugeView(Context context) {
        super(context);
        setStartAngle(context,null);
    }

    public ContinuousGaugeView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setStartAngle(context, attrs);

    }

    public ContinuousGaugeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setStartAngle(context,attrs);
    }
    //endregion

    private void setupBackgroundArcPaint() {
        mBackgroundArcPaint = new Paint();
        mBackgroundArcPaint.setColor(mColorBackground);
        mBackgroundArcPaint.setAntiAlias(true);
        mBackgroundArcPaint.setStrokeWidth(strokeWidthBg);
        mBackgroundArcPaint.setStyle(Paint.Style.STROKE);

    }

    public void setValue(float value) {
        this.value = value;
    }

    private void setStartAngle(Context context,@Nullable AttributeSet attributeSet){
        if(attributeSet != null){
            final TypedArray a = context.obtainStyledAttributes(attributeSet,R.styleable.ContinuousGaugeView, 0, 0);
            try{
                String gaugeStyle = a.getString(R.styleable.ContinuousGaugeView_gaugeStyle);
                if (gaugeStyle != null) {
                    if(gaugeStyle.contentEquals("FULL")){
                        this.startAngle = 270f;
                        this.fullSweepAngleBasedOnGaugeStyle = 360f;
                    }else if(gaugeStyle.contentEquals("HALF")){
                        this.startAngle = 180f;
                        this.fullSweepAngleBasedOnGaugeStyle = 180f;
                    }
                }else{
                    this.startAngle = 270f;
                    this.fullSweepAngleBasedOnGaugeStyle = 360f;
                }
            }finally {
                a.recycle();
            }

        }
    }

    public void setValueSweepAngle(float angle) {
        this.valueSweepAngle = angle;
    }

    public float getAmountToSweepBy() {
        return ((this.value / 100) * fullSweepAngleBasedOnGaugeStyle);
    }

    public float getEndAngle() {
        return endAngle;
    }

    public void setNumberOfSections(int i) {
        this.mNumberOfSections = i;
    }

    public void setSectionLimitValues(double[] limitValues) {
        this.mSectionLimitValues = limitValues;
        setupSectionLimitAngles();
    }

    public void setSectionColors(int[] sectionColors) {
        if (sectionColors.length != (mNumberOfSections)) {
            throw new GaugeException("You need to set " + (mNumberOfSections + 1) + " section colors when you have " + mNumberOfSections + " sections defined", new Throwable());
        } else {
            this.mSectionColors = sectionColors;
            setupPaintsNeededForEachSection();
        }
    }

    public void setBackgroundArcColor(int backgroundArcColor) {
        mColorBackground = backgroundArcColor;
        setupBackgroundArcPaint();
    }

    private void setupPaintsNeededForEachSection() {
        for (int color : mSectionColors) {
            Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            paint.setColor(getResources().getColor(color));
            paint.setStrokeWidth(strokeWidthBg);
            paint.setStyle(Paint.Style.STROKE);
            mAllPaintsNeeded.add(paint);
        }
    }

    private void setupSectionLimitAngles() {
        for (double limit : mSectionLimitValues) {
            double angleValue = (limit / 100.0 * fullSweepAngleBasedOnGaugeStyle);
            mLimitAnglesNeeded.add(angleValue);
        }
    }


    //region DRAWING THE VIEW
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawBackgroundArc(canvas);
        drawValueArc(canvas);
    }

    private void drawBackgroundArc(Canvas canvas) {
        canvas.drawArc(left, top, right, bottom, startAngle, fullSweepAngleBasedOnGaugeStyle, false, mBackgroundArcPaint);
    }

    private void drawValueArc(Canvas canvas) {
        for (int i = 0; i < mLimitAnglesNeeded.size(); i++) {
            if (endAngle <= mLimitAnglesNeeded.get(i)) {
                canvas.drawArc(left, top, right, bottom, startAngle, endAngle, false, mAllPaintsNeeded.get(i));
                break;
            }
        }
        endAngle = valueSweepAngle;
    }
    //endregion

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        float mWidth = w;
        float mHeight = h;

        int xpad = getPaddingLeft() + getPaddingRight();

        int leftPad = getPaddingLeft();
        int rightPad = getPaddingRight();
        int topPad = getPaddingTop();
        int bottomPad = getPaddingBottom();

        //colorfill
        strokeWidthBg = (float) (0.06 * mWidth - xpad);
        strokeWidthBg *= 1.5f;
        left = 0 + leftPad + strokeWidthBg;
        right = mWidth - rightPad - strokeWidthBg;
        top = 0 + topPad + strokeWidthBg;
        bottom = mHeight - bottomPad - strokeWidthBg;
    }


    private static class GaugeException extends RuntimeException {

        String message;
        Throwable cause;

        public GaugeException() {
            super();
        }

        GaugeException(String message, Throwable cause) {
            super(message, cause);
            this.cause = cause;
            this.message = message;
        }
    }

}
