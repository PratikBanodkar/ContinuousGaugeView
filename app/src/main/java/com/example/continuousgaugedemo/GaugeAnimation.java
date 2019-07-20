package com.example.continuousgaugedemo;

import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.Transformation;

public class GaugeAnimation extends Animation {

    private ContinuousGaugeView gaugeView;

    private float oldAngle;
    private float newAngle;
    private static String TAG = GaugeAnimation.class.getSimpleName();

    public GaugeAnimation(ContinuousGaugeView gaugeView) {
        this.oldAngle = gaugeView.getEndAngle();
        this.newAngle = gaugeView.getAmountToSweepBy();
        this.gaugeView = gaugeView;
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation transformation) {
        float angle = oldAngle + ((newAngle - oldAngle) * interpolatedTime);
        gaugeView.setValueSweepAngle(angle);
        gaugeView.invalidate();
    }
}
