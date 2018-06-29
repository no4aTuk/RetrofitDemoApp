package com.example.apple.retrofitdemoapp.Helpers;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.io.IOException;

public class NetworkUtils {

    public static final String PING_GOOGLE_PUBLIC_SERVER_COMMAND = "/system/bin/ping -c 1 8.8.8.8";

    public static boolean hasInternetConnection(Context context, boolean ping) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        boolean isNetWorkAvailable = networkInfo != null && networkInfo.isConnectedOrConnecting();
        if (ping) {
            return pingPublicServer(isNetWorkAvailable, PING_GOOGLE_PUBLIC_SERVER_COMMAND);
        } else {
            return isNetWorkAvailable;
        }
    }

    public static boolean pingPublicServer(boolean isNetWorkAvailable, String command) {
        boolean result = false;
        if (isNetWorkAvailable) {
            Runtime runtime = Runtime.getRuntime();
            try {
                java.lang.Process ipProcess = runtime.exec(command);
                int exitValue = ipProcess.waitFor();
                result = (exitValue == 0);
            } catch (IOException ioe) {
                ioe.printStackTrace();
            } catch (InterruptedException ie) {
                ie.printStackTrace();
            }
        }
        return result;
    }
}
