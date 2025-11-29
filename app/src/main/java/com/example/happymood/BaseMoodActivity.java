package com.example.happymood;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public abstract class BaseMoodActivity extends AppCompatActivity {
    
    protected TextView jokeTextView;
    protected Button nextButton;
    protected Button backButton;
    protected Button addJokeButton;
    protected ProgressBar loadingProgressBar;
    protected ImageView gifImageView;
    protected JokeManager jokeManager;
    protected String currentMood;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Hide the action bar to prevent overlap with content
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        
        setContentView(getLayoutResource());
        
        // Get mood from intent
        currentMood = getIntent().getStringExtra("mood");
        
        // Initialize views
        jokeTextView = findViewById(R.id.jokeTextView);
        nextButton = findViewById(R.id.nextButton);
        backButton = findViewById(R.id.backButton);
        addJokeButton = findViewById(R.id.addJokeButton);
        loadingProgressBar = findViewById(R.id.loadingProgressBar);
        gifImageView = findViewById(R.id.gifImageView);
        
        // Initialize joke manager
        jokeManager = new JokeManager(this);
        
        // Set click listeners
        nextButton.setOnClickListener(v -> generateNextJoke());
        backButton.setOnClickListener(v -> finish());
        addJokeButton.setOnClickListener(v -> openAddJokeActivity());
        
        // Generate first joke
        generateNextJoke();
    }
    
    protected abstract int getLayoutResource();
    
    protected abstract String getMoodTitle();
    
    protected abstract int getMoodColor();
    
    private void generateNextJoke() {
        // Show loading state
        showLoading(true);
        jokeTextView.setText("Fetching a fresh joke for you... 🔄");
        gifImageView.setVisibility(View.GONE);
        
        // Try to fetch online joke first, fallback to local
        if (jokeManager.hasInternetConnection()) {
            jokeManager.fetchOnlineJoke(currentMood, new JokeApiService.JokeCallback() {
                @Override
                public void onJokeReceived(String joke, String emoji) {
                    runOnUiThread(() -> {
                        showLoading(false);
                        jokeTextView.setText(emoji + " " + joke);
                        loadGifForMood(currentMood);
                    });
                }
                
                @Override
                public void onError(String error) {
                    runOnUiThread(() -> {
                        showLoading(false);
                        // Fallback to local jokes
                        String localJoke = jokeManager.getRandomJoke(currentMood);
                        if (localJoke != null) {
                            jokeTextView.setText(localJoke);
                        } else {
                            jokeTextView.setText("No jokes available for this mood yet. Add some jokes! 😊");
                        }
                        gifImageView.setVisibility(View.GONE);
                        Toast.makeText(BaseMoodActivity.this, "Using offline jokes", Toast.LENGTH_SHORT).show();
                    });
                }
            });
        } else {
            // No internet, use local jokes
            showLoading(false);
            String joke = jokeManager.getRandomJoke(currentMood);
            if (joke != null) {
                jokeTextView.setText(joke);
            } else {
                jokeTextView.setText("No jokes available for this mood yet. Add some jokes! 😊");
            }
            gifImageView.setVisibility(View.GONE);
            Toast.makeText(this, "No internet connection - using offline jokes", Toast.LENGTH_SHORT).show();
        }
    }
    
    private void showLoading(boolean show) {
        loadingProgressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        nextButton.setEnabled(!show);
    }

    private void loadGifForMood(String mood) {
        if (gifImageView == null) return;

        GiphyApiService.fetchMoodGif(this, mood, new GiphyApiService.GifCallback() {
            @Override
            public void onGifReceived(String gifUrl) {
                runOnUiThread(() -> {
                    gifImageView.setVisibility(View.VISIBLE);
                    // Use Glide to load the GIF
                    com.bumptech.glide.Glide.with(BaseMoodActivity.this)
                            .asGif()
                            .load(gifUrl)
                            .into(gifImageView);
                });
            }

            @Override
            public void onError(String error) {
                runOnUiThread(() -> gifImageView.setVisibility(View.GONE));
            }
        });
    }
    
    private void openAddJokeActivity() {
        Intent intent = new Intent(this, AddJokeActivity.class);
        intent.putExtra("mood", currentMood);
        startActivity(intent);
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        // Refresh joke manager when returning from AddJokeActivity
        jokeManager = new JokeManager(this);
    }
}


