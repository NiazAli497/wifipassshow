package wifi.key.show.master.show.all.wifi.password.Recievers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

public class WifiTestReciever extends BroadcastReceiver {
    public void onReceive(final Context context, final Intent intent) {
        Toast.makeText(context, "start receiver", Toast.LENGTH_SHORT).show();


        Log.d("WifiTestReciever", intent.getAction());
        if (intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
            NetworkInfo networkInfo =
                    intent.getParcelableExtra(ConnectivityManager.EXTRA_NETWORK_INFO);
            if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI &&
                    networkInfo.isConnected()) {
                Log.d("Inetify", "Wifi is connected: " + String.valueOf(networkInfo));

                Log.e("intent action", intent.getAction());
                if (isNetworkConnected(context)) {
                    Log.e("WiFi", "is Connected. Saving...");
                    Toast.makeText(context, "Connected", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        if (ni != null) {
            Log.e("NetworkInfo", "!=null");

            try {
                //For 3G check
                boolean is3g = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
                        .isConnectedOrConnecting();
                //For WiFi Check
                boolean isWifi = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
                        .isConnected();

                Log.e("isWifi", "isWifi=" + isWifi);
                Log.e("is3g", "is3g=" + is3g);
                if (!isWifi) {

                    return false;
                } else {
                    return true;
                }

            } catch (Exception er) {
                return false;
            }

        } else {
            Log.e("NetworkInfo", "==null");
            return false;
        }
    }

}