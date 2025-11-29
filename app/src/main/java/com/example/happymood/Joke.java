package com.example.happymood;

public class Joke {
    private String text;
    private String emoji;
    private String mood;
    
    public Joke(String text, String emoji, String mood) {
        this.text = text;
        this.emoji = emoji;
        this.mood = mood;
    }
    
    public String getText() {
        return text;
    }
    
    public void setText(String text) {
        this.text = text;
    }
    
    public String getEmoji() {
        return emoji;
    }
    
    public void setEmoji(String emoji) {
        this.emoji = emoji;
    }
    
    public String getMood() {
        return mood;
    }
    
    public void setMood(String mood) {
        this.mood = mood;
    }
    
    public String getFullJoke() {
        return emoji + " " + text;
    }
    
    @Override
    public String toString() {
        return getFullJoke();
    }
}


