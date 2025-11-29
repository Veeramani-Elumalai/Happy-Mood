package com.example.happymood;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkUtils {
    
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
    
    public static String getNetworkStatus(Context context) {
        if (isNetworkAvailable(context)) {
            return "🌐 Online - Fresh jokes available!";
        } else {
            return "📱 Offline - Using saved jokes";
        }
    }
}


