package com.example.happymood;

import android.os.Bundle;

public class CalmActivity extends BaseMoodActivity {
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    
    @Override
    protected int getLayoutResource() {
        return R.layout.activity_calm;
    }
    
    @Override
    protected String getMoodTitle() {
        return "Calm Mood 😌";
    }
    
    @Override
    protected int getMoodColor() {
        return R.color.calm_color;
    }
}


