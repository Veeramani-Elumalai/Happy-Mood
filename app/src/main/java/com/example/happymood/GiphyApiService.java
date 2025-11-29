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
import java.util.Random;

public class GiphyApiService {

    private static final String TAG = "GiphyApiService";

    // Giphy API key
    private static final String API_KEY = "BBcJYchESsutmWrVDyerHHJTVkgwYR4F";
    private static final String SEARCH_ENDPOINT =
            "https://api.giphy.com/v1/gifs/search?api_key=%s&q=%s&limit=25&rating=g&lang=en";

    public interface GifCallback {
        void onGifReceived(String gifUrl);

        void onError(String error);
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static void fetchMoodGif(Context context, String mood, GifCallback callback) {
        if (!isNetworkAvailable(context)) {
            callback.onError("No internet connection for GIF");
            return;
        }

        // Convert mood to appropriate search keywords for better GIF relevance
        String searchQuery = getMoodSearchQuery(mood);
        new FetchGifTask(searchQuery, callback).execute();
    }

    /**
     * Maps mood categories to relevant Giphy search terms
     */
    private static String getMoodSearchQuery(String mood) {
        if (mood == null) {
            return "funny";
        }

        String moodLower = mood.toLowerCase().trim();
        
        switch (moodLower) {
            case "happy":
                // Happy mood: cheerful, joyful, funny GIFs
                return "happy funny laugh smile joy celebration";
            case "sad":
                // Sad mood: uplifting, encouraging, supportive GIFs
                return "uplifting encouraging support hug comfort positive";
            case "calm":
                // Calm mood: peaceful, relaxing, zen GIFs
                return "peaceful relaxing calm zen meditation serene";
            case "excited":
                // Excited mood: energetic, celebration, party GIFs
                return "excited celebration party happy dance energetic";
            default:
                return "funny";
        }
    }

    private static class FetchGifTask extends AsyncTask<Void, Void, String> {

        private final String keyword;
        private final GifCallback callback;
        private String errorMessage;

        FetchGifTask(String keyword, GifCallback callback) {
            this.keyword = keyword;
            this.callback = callback;
        }

        @Override
        protected String doInBackground(Void... voids) {
            try {
                String encodedQuery = keyword == null ? "" : keyword.trim().replace(" ", "+");
                if (encodedQuery.isEmpty()) {
                    encodedQuery = "funny";
                }

                String urlString = String.format(SEARCH_ENDPOINT, API_KEY, encodedQuery);
                URL url = new URL(urlString);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setConnectTimeout(5000);
                connection.setReadTimeout(5000);

                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader reader =
                            new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String line;

                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    reader.close();

                    JSONObject jsonResponse = new JSONObject(response.toString());
                    JSONArray dataArray = jsonResponse.optJSONArray("data");
                    if (dataArray != null && dataArray.length() > 0) {
                        // Pick a random GIF from results for a bit of variety
                        Random random = new Random();
                        JSONObject gifObject = dataArray.getJSONObject(random.nextInt(dataArray.length()));
                        JSONObject images = gifObject.optJSONObject("images");
                        if (images != null) {
                            JSONObject fixedHeight = images.optJSONObject("fixed_height");
                            if (fixedHeight != null && fixedHeight.has("url")) {
                                return fixedHeight.getString("url");
                            }
                        }
                    } else {
                        errorMessage = "No GIFs found";
                    }
                } else {
                    errorMessage = "Giphy error: HTTP " + responseCode;
                }
            } catch (Exception e) {
                Log.e(TAG, "Error fetching GIF from Giphy", e);
                errorMessage = "Network error: " + e.getMessage();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String gifUrl) {
            if (gifUrl != null && !gifUrl.isEmpty()) {
                callback.onGifReceived(gifUrl);
            } else {
                callback.onError(errorMessage != null ? errorMessage : "Failed to fetch GIF");
            }
        }
    }
}


