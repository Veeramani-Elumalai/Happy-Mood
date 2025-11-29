package com.example.happymood;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class JokeApiService {
    private static final String TAG = "JokeApiService";
    private static final String CHUCK_NORRIS_API = "https://api.chucknorris.io/jokes/random";
    private static final String JOKE_API = "https://v2.jokeapi.dev/joke/Any?type=single";
    
    public interface JokeCallback {
        void onJokeReceived(String joke, String emoji);
        void onError(String error);
    }
    
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
    
    public static void fetchRandomJoke(Context context, String mood, JokeCallback callback) {
        if (!isNetworkAvailable(context)) {
            callback.onError("No internet connection available");
            return;
        }
        
        new FetchJokeTask(mood, callback).execute();
    }
    
    private static class FetchJokeTask extends AsyncTask<Void, Void, String> {
        private String mood;
        private JokeCallback callback;
        private String errorMessage;
        
        public FetchJokeTask(String mood, JokeCallback callback) {
            this.mood = mood;
            this.callback = callback;
        }
        
        @Override
        protected String doInBackground(Void... voids) {
            try {
                // Try JokeAPI first (more reliable)
                String joke = fetchFromJokeApi();
                if (joke != null && !joke.isEmpty()) {
                    return joke;
                }
                
                // Fallback to Chuck Norris API
                joke = fetchFromChuckNorrisApi();
                if (joke != null && !joke.isEmpty()) {
                    return joke;
                }
                
                errorMessage = "Failed to fetch joke from online sources";
                return null;
                
            } catch (Exception e) {
                Log.e(TAG, "Error fetching joke", e);
                errorMessage = "Network error: " + e.getMessage();
                return null;
            }
        }
        
        @Override
        protected void onPostExecute(String joke) {
            if (joke != null && !joke.isEmpty()) {
                String emoji = getMoodEmoji(mood);
                callback.onJokeReceived(joke, emoji);
            } else {
                callback.onError(errorMessage != null ? errorMessage : "Failed to fetch joke");
            }
        }
        
        private String fetchFromJokeApi() {
            try {
                URL url = new URL(JOKE_API);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setConnectTimeout(5000);
                connection.setReadTimeout(5000);
                
                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String line;
                    
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    reader.close();
                    
                    JSONObject jsonResponse = new JSONObject(response.toString());
                    if (jsonResponse.has("joke")) {
                        return jsonResponse.getString("joke");
                    }
                }
            } catch (Exception e) {
                Log.e(TAG, "Error fetching from JokeAPI", e);
            }
            return null;
        }
        
        private String fetchFromChuckNorrisApi() {
            try {
                URL url = new URL(CHUCK_NORRIS_API);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setConnectTimeout(5000);
                connection.setReadTimeout(5000);
                
                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String line;
                    
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    reader.close();
                    
                    JSONObject jsonResponse = new JSONObject(response.toString());
                    if (jsonResponse.has("value")) {
                        return jsonResponse.getString("value");
                    }
                }
            } catch (Exception e) {
                Log.e(TAG, "Error fetching from Chuck Norris API", e);
            }
            return null;
        }
        
        private String getMoodEmoji(String mood) {
            switch (mood.toLowerCase()) {
                case "happy":
                    return "😄";
                case "sad":
                    return "😢";
                case "excited":
                    return "🤩";
                case "calm":
                    return "😌";
                default:
                    return "😊";
            }
        }
    }
}


