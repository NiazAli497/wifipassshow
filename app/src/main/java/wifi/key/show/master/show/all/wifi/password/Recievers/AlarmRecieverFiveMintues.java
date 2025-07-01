package wifi.key.show.master.show.all.wifi.password.Recievers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;
import android.widget.Toast;

import wifi.key.show.master.show.all.wifi.password.SharedPreference.SharedPreference;

public class AlarmRecieverFiveMintues extends BroadcastReceiver {
    SharedPreference sharedPreference;
    @Override
    public void onReceive(Context context, Intent intent) {

        sharedPreference = new SharedPreference(context);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            if(sharedPreference.get("fivemins")==2) {
                Intent panelIntent = new Intent(Settings.Panel.ACTION_INTERNET_CONNECTIVITY);
                panelIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(panelIntent);
                Toast.makeText(context, "Please turn on Wifi manually", Toast.LENGTH_SHORT).show();
            }
            else if(sharedPreference.get("fivemintues")==2){
                Intent panelIntent = new Intent(Settings.Panel.ACTION_INTERNET_CONNECTIVITY);
                panelIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(panelIntent);
                Toast.makeText(context, "Please turn on Wifi manually since Android 10 and up version", Toast.LENGTH_SHORT).show();
            }
            else if(sharedPreference.get("onehour")==2){
                Intent panelIntent = new Intent(Settings.Panel.ACTION_INTERNET_CONNECTIVITY);
                panelIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(panelIntent);
                Toast.makeText(context, "Please turn on Wifi manually since Android 10 and up version", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(context, "not", Toast.LENGTH_SHORT).show();
            }
            }
        else {
            if(intent.getAction().equals("aa")|| intent.getAction().equals("hh") ){
                WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                wifiManager.setWifiEnabled(true);
                WifiConfiguration wifiConfig = new WifiConfiguration();

                wifiConfig.SSID = String.format("\"%s\"", "");
                wifiConfig.preSharedKey = String.format("\"%s\"", "");

                int netId = wifiManager.addNetwork(wifiConfig);
                wifiManager.disconnect();

            }
            else if(intent.getAction().equals("bb")) {
                WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                wifiManager.setWifiEnabled(true);
                WifiConfiguration wifiConfig = new WifiConfiguration();

                wifiConfig.SSID = String.format("\"%s\"", "");
                wifiConfig.preSharedKey = String.format("\"%s\"", "");

                int netId = wifiManager.addNetwork(wifiConfig);
                wifiManager.enableNetwork(netId, true);
            wifiManager.reconnect();
            }
        }
    }

}