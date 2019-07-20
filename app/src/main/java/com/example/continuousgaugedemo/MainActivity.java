package com.example.continuousgaugedemo;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    ContinuousGaugeView continuousGaugeView1,continuousGaugeView2;
    EditText editText;
    float newValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button updateGaugeButton = findViewById(R.id.button_UpdateGauge);
        editText = findViewById(R.id.editText);
        editText.setText("50");
        newValue = Float.parseFloat(editText.getText().toString());
        initializeGauge1();
        initializeGauge2();


        updateGaugeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newValue = Float.parseFloat(editText.getText().toString());
                updateGauge();
            }
        });

        updateGauge();

    }

    private void initializeGauge1() {
        continuousGaugeView1 = findViewById(R.id.continuousGaugeView1);
        continuousGaugeView1.setNumberOfSections(3);
        continuousGaugeView1.setSectionLimitValues(new double[]{30,50,100});
        continuousGaugeView1.setSectionColors(new int[]{R.color.good,R.color.fair,R.color.poor});
        continuousGaugeView1.setBackgroundArcColor(getResources().getColor(R.color.gaugeGreyColor));
    }

    private void initializeGauge2() {
        continuousGaugeView2 = findViewById(R.id.continuousGaugeView2);
        continuousGaugeView2.setNumberOfSections(3);
        continuousGaugeView2.setSectionLimitValues(new double[]{30,50,100});
        continuousGaugeView2.setSectionColors(new int[]{R.color.good,R.color.fair,R.color.poor});
        continuousGaugeView2.setBackgroundArcColor(getResources().getColor(R.color.gaugeGreyColor));
    }

    private void updateGauge() {
        continuousGaugeView1.setValue(Float.parseFloat(editText.getText().toString()));
        continuousGaugeView2.setValue(Float.parseFloat(editText.getText().toString()));
        animateGauge1();
        animateGauge2();
    }

    private void animateGauge1(){
        GaugeAnimation animation = new GaugeAnimation(continuousGaugeView1);
        animation.setDuration(1000);
        continuousGaugeView1.startAnimation(animation);
    }

    private void animateGauge2(){
        GaugeAnimation animation = new GaugeAnimation(continuousGaugeView2);
        animation.setDuration(1000);
        continuousGaugeView2.startAnimation(animation);
    }
}
