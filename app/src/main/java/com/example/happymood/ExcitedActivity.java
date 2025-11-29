package com.example.happymood;

import android.os.Bundle;

public class ExcitedActivity extends BaseMoodActivity {
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    
    @Override
    protected int getLayoutResource() {
        return R.layout.activity_excited;
    }
    
    @Override
    protected String getMoodTitle() {
        return "Excited Mood 🤩";
    }
    
    @Override
    protected int getMoodColor() {
        return R.color.excited_color;
    }
}
