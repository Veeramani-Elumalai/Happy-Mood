package com.example.happymood;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    
    private TextView jokeTextView;
    private Button happyButton, sadButton, excitedButton, calmButton, addJokeButton, instantJokeButton;
    private ProgressBar loadingProgressBar;
    private JokeManager jokeManager;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Hide the action bar to prevent overlap with content
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        
        setContentView(R.layout.activity_main);
        
        // Initialize views
        jokeTextView = findViewById(R.id.jokeTextView);
        happyButton = findViewById(R.id.happyButton);
        sadButton = findViewById(R.id.sadButton);
        excitedButton = findViewById(R.id.excitedButton);
        calmButton = findViewById(R.id.calmButton);
        addJokeButton = findViewById(R.id.addJokeButton);
        instantJokeButton = findViewById(R.id.instantJokeButton);
        loadingProgressBar = findViewById(R.id.loadingProgressBar);
        
        // Initialize joke manager
        jokeManager = new JokeManager(this);
        
        // Set click listeners
        happyButton.setOnClickListener(v -> openMoodActivity("happy"));
        sadButton.setOnClickListener(v -> openMoodActivity("sad"));
        excitedButton.setOnClickListener(v -> openMoodActivity("excited"));
        calmButton.setOnClickListener(v -> openMoodActivity("calm"));
        addJokeButton.setOnClickListener(v -> openAddJokeActivity());
        instantJokeButton.setOnClickListener(v -> showInstantJokes());
        
        // Show welcome message
        jokeTextView.setText("Welcome to Happy Mood! 😊\nChoose your mood to get a personalized joke!");
    }
    
    private void openMoodActivity(String mood) {
        Intent intent;
        switch (mood) {
            case "happy":
                intent = new Intent(this, HappyActivity.class);
                break;
            case "sad":
                intent = new Intent(this, SadActivity.class);
                break;
            case "excited":
                intent = new Intent(this, ExcitedActivity.class);
                break;
            case "calm":
                intent = new Intent(this, CalmActivity.class);
                break;
            default:
                return;
        }
        intent.putExtra("mood", mood);
        startActivity(intent);
    }
    
    private void openAddJokeActivity() {
        Intent intent = new Intent(this, AddJokeActivity.class);
        startActivity(intent);
    }
    
    private void showInstantJokes() {
        List<Joke> randomJokes = jokeManager.getRandomJokesFromAllMoods();
        
        if (randomJokes.isEmpty()) {
            jokeTextView.setText("No jokes available. Please add some jokes first! 😊");
            return;
        }
        
        StringBuilder jokeList = new StringBuilder();
        jokeList.append("Random Mix of Mood Jokes:\n\n");
        
        for (int i = 0; i < randomJokes.size(); i++) {
            Joke joke = randomJokes.get(i);
            String mood = capitalize(joke.getMood());
            jokeList.append(mood).append(" Joke:\n");
            jokeList.append(joke.getEmoji()).append(" ").append(joke.getText());
            if (i < randomJokes.size() - 1) {
                jokeList.append("\n\n");
            }
        }
        
        jokeTextView.setText(jokeList.toString());
    }
    
    private String capitalize(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        // Refresh joke manager when returning from AddJokeActivity
        jokeManager = new JokeManager(this);
    }
}
