package com.example.happymood;

import android.os.Bundle;

public class SadActivity extends BaseMoodActivity {
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    
    @Override
    protected int getLayoutResource() {
        return R.layout.activity_sad;
    }
    
    @Override
    protected String getMoodTitle() {
        return "Sad Mood 😢";
    }
    
    @Override
    protected int getMoodColor() {
        return R.color.sad_color;
    }
}


