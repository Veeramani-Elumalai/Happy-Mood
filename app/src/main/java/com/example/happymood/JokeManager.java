package com.example.happymood;

import android.content.Context;
import android.content.SharedPreferences;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class JokeManager {
    private Context context;
    private SharedPreferences sharedPreferences;
    private List<Joke> allJokes;
    private Random random;
    
    public JokeManager(Context context) {
        this.context = context;
        this.sharedPreferences = context.getSharedPreferences("jokes_prefs", Context.MODE_PRIVATE);
        this.random = new Random();
        this.allJokes = new ArrayList<>();
        
        loadJokes();
        initializeDefaultJokes();
    }
    
    private void loadJokes() {
        String jokesJson = sharedPreferences.getString("saved_jokes", "");
        if (!jokesJson.isEmpty()) {
            Gson gson = new Gson();
            Type listType = new TypeToken<List<Joke>>(){}.getType();
            allJokes = gson.fromJson(jokesJson, listType);
        }
    }
    
    private void saveJokes() {
        Gson gson = new Gson();
        String jokesJson = gson.toJson(allJokes);
        sharedPreferences.edit().putString("saved_jokes", jokesJson).apply();
    }
    
    private void initializeDefaultJokes() {
        // Check if jokes are already initialized
        if (allJokes.isEmpty()) {
            addDefaultJokes();
            saveJokes();
        }
    }
    
    private void addDefaultJokes() {
        // Happy jokes
        allJokes.add(new Joke("Why don't scientists trust atoms? Because they make up everything!", "😄", "happy"));
        allJokes.add(new Joke("What do you call a fake noodle? An impasta!", "😂", "happy"));
        allJokes.add(new Joke("Why did the scarecrow win an award? He was outstanding in his field!", "🤣", "happy"));
        allJokes.add(new Joke("What do you call a bear with no teeth? A gummy bear!", "😆", "happy"));
        allJokes.add(new Joke("Why don't eggs tell jokes? They'd crack each other up!", "🥚", "happy"));
        
        // Sad jokes (uplifting ones)
        allJokes.add(new Joke("Even the darkest night will end and the sun will rise.", "🌅", "sad"));
        allJokes.add(new Joke("This too shall pass, and you'll be stronger for it.", "💪", "sad"));
        allJokes.add(new Joke("Every cloud has a silver lining, even storm clouds!", "⛅", "sad"));
        allJokes.add(new Joke("You're braver than you believe and stronger than you seem.", "🦸", "sad"));
        allJokes.add(new Joke("The best is yet to come, keep your head up!", "🌟", "sad"));
        
        // Excited jokes
        allJokes.add(new Joke("What do you call a dinosaur that crashes his car? Tyrannosaurus Wrecks!", "🦕", "excited"));
        allJokes.add(new Joke("Why did the math book look so sad? Because it had too many problems!", "📚", "excited"));
        allJokes.add(new Joke("What do you call a fish wearing a bowtie? So-fish-ticated!", "🐟", "excited"));
        allJokes.add(new Joke("Why don't scientists trust stairs? Because they're always up to something!", "🔬", "excited"));
        allJokes.add(new Joke("What do you call a can opener that doesn't work? A can't opener!", "🥫", "excited"));
        
        // Calm jokes
        allJokes.add(new Joke("What do you call a sleeping bull? A bulldozer!", "🐂", "calm"));
        allJokes.add(new Joke("Why did the coffee file a police report? It got mugged!", "☕", "calm"));
        allJokes.add(new Joke("What do you call a belt made of watches? A waist of time!", "⏰", "calm"));
        allJokes.add(new Joke("Why don't oysters donate to charity? Because they are shellfish!", "🦪", "calm"));
        allJokes.add(new Joke("What do you call a fish with no eyes? Fsh!", "🐠", "calm"));
    }
    
    public String getRandomJoke(String mood) {
        List<Joke> moodJokes = new ArrayList<>();
        for (Joke joke : allJokes) {
            if (joke.getMood().equals(mood)) {
                moodJokes.add(joke);
            }
        }
        
        if (moodJokes.isEmpty()) {
            return null;
        }
        
        Joke randomJoke = moodJokes.get(random.nextInt(moodJokes.size()));
        return randomJoke.getFullJoke();
    }
    
    public void addJoke(Joke joke) {
        allJokes.add(joke);
        saveJokes();
    }
    
    public List<Joke> getAllJokes() {
        return new ArrayList<>(allJokes);
    }
    
    public List<Joke> getJokesByMood(String mood) {
        List<Joke> moodJokes = new ArrayList<>();
        for (Joke joke : allJokes) {
            if (joke.getMood().equals(mood)) {
                moodJokes.add(joke);
            }
        }
        return moodJokes;
    }
    
    public void fetchOnlineJoke(String mood, JokeApiService.JokeCallback callback) {
        JokeApiService.fetchRandomJoke(context, mood, new JokeApiService.JokeCallback() {
            @Override
            public void onJokeReceived(String joke, String emoji) {
                // Add the online joke to our collection
                Joke onlineJoke = new Joke(joke, emoji, mood);
                allJokes.add(onlineJoke);
                saveJokes();
                callback.onJokeReceived(joke, emoji);
            }
            
            @Override
            public void onError(String error) {
                // Fallback to local jokes if online fails
                String localJoke = getRandomJoke(mood);
                if (localJoke != null) {
                    callback.onJokeReceived(localJoke, "");
                } else {
                    callback.onError("No jokes available. Please add some jokes or check your internet connection.");
                }
            }
        });
    }
    
    public boolean hasInternetConnection() {
        return JokeApiService.isNetworkAvailable(context);
    }
    
    public List<Joke> getRandomJokesFromAllMoods() {
        List<String> allMoods = Arrays.asList("happy", "sad", "excited", "calm");
        List<Joke> randomJokes = new ArrayList<>();
        
        for (String mood : allMoods) {
            List<Joke> moodJokes = getJokesByMood(mood);
            if (!moodJokes.isEmpty()) {
                Joke randomJoke = moodJokes.get(random.nextInt(moodJokes.size()));
                randomJokes.add(randomJoke);
            }
        }
        
        // Shuffle the jokes for random order
        Collections.shuffle(randomJokes);
        
        return randomJokes;
    }
}
