package com.polvazo.speakmany.speakMany.Util;


         import android.content.Context;
         import android.net.ConnectivityManager;
         import android.net.Network;
         import android.net.NetworkInfo;
         import android.os.Build;

public class ConnectionUtils {

    private ConnectionUtils() {
    }

    /**
     * Verifica si hay conexión a internet.
     */
    public static boolean isOnline(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Network[] nets = connectivityManager.getAllNetworks();
            NetworkInfo infoNet;
            for (Network red : nets) {
                infoNet = connectivityManager.getNetworkInfo(red);
                if (infoNet.getState().equals(NetworkInfo.State.CONNECTED)) {
                    return true;
                }
            }
        } else {
            if (connectivityManager != null) {
                @SuppressWarnings("deprecation")
                NetworkInfo[] infoNets = connectivityManager.getAllNetworkInfo();
                if (infoNets != null) {
                    for (NetworkInfo infoNet : infoNets) {
                        if (infoNet.getState() == NetworkInfo.State.CONNECTED) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }
}