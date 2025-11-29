package com.example.happymood;

import android.os.Bundle;

public class HappyActivity extends BaseMoodActivity {
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    
    @Override
    protected int getLayoutResource() {
        return R.layout.activity_happy;
    }
    
    @Override
    protected String getMoodTitle() {
        return "Happy Mood 😄";
    }
    
    @Override
    protected int getMoodColor() {
        return R.color.happy_color;
    }
}


