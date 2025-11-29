package com.example.happymood;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class AddJokeActivity extends AppCompatActivity {
    
    private EditText jokeTextEditText;
    private EditText emojiEditText;
    private RadioGroup moodRadioGroup;
    private Button saveJokeButton;
    private Button backButton;
    private JokeManager jokeManager;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Hide the action bar to prevent overlap with content
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        
        setContentView(R.layout.activity_add_joke);
        
        // Initialize views
        jokeTextEditText = findViewById(R.id.jokeTextEditText);
        emojiEditText = findViewById(R.id.emojiEditText);
        moodRadioGroup = findViewById(R.id.moodRadioGroup);
        saveJokeButton = findViewById(R.id.saveJokeButton);
        backButton = findViewById(R.id.backButton);
        
        // Initialize joke manager
        jokeManager = new JokeManager(this);
        
        // Set click listeners
        saveJokeButton.setOnClickListener(v -> saveJoke());
        backButton.setOnClickListener(v -> finish());
        
        // Set default emoji
        emojiEditText.setText("😊");
        
        // Pre-select mood if coming from a specific mood activity
        String preSelectedMood = getIntent().getStringExtra("mood");
        if (preSelectedMood != null) {
            selectMoodRadioButton(preSelectedMood);
        }
    }
    
    private void selectMoodRadioButton(String mood) {
        switch (mood) {
            case "happy":
                findViewById(R.id.happyRadio).setVisibility(View.VISIBLE);
                ((RadioButton) findViewById(R.id.happyRadio)).setChecked(true);
                break;
            case "sad":
                findViewById(R.id.sadRadio).setVisibility(View.VISIBLE);
                ((RadioButton) findViewById(R.id.sadRadio)).setChecked(true);
                break;
            case "excited":
                findViewById(R.id.excitedRadio).setVisibility(View.VISIBLE);
                ((RadioButton) findViewById(R.id.excitedRadio)).setChecked(true);
                break;
            case "calm":
                findViewById(R.id.calmRadio).setVisibility(View.VISIBLE);
                ((RadioButton) findViewById(R.id.calmRadio)).setChecked(true);
                break;
        }
    }
    
    private void saveJoke() {
        String jokeText = jokeTextEditText.getText().toString().trim();
        String emoji = emojiEditText.getText().toString().trim();
        int selectedMoodId = moodRadioGroup.getCheckedRadioButtonId();
        
        // Validate inputs
        if (jokeText.isEmpty()) {
            Toast.makeText(this, "Please enter a joke!", Toast.LENGTH_SHORT).show();
            return;
        }
        
        if (emoji.isEmpty()) {
            Toast.makeText(this, "Please enter an emoji!", Toast.LENGTH_SHORT).show();
            return;
        }
        
        if (selectedMoodId == -1) {
            Toast.makeText(this, "Please select a mood!", Toast.LENGTH_SHORT).show();
            return;
        }
        
        // Get selected mood
        RadioButton selectedMoodRadio = findViewById(selectedMoodId);
        String mood = selectedMoodRadio.getText().toString().toLowerCase();
        
        // Create and save joke
        Joke newJoke = new Joke(jokeText, emoji, mood);
        jokeManager.addJoke(newJoke);
        
        Toast.makeText(this, "Joke saved successfully! 🎉", Toast.LENGTH_SHORT).show();
        
        // Clear form
        jokeTextEditText.setText("");
        emojiEditText.setText("😊");
        moodRadioGroup.clearCheck();
    }
}
