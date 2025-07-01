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

public class AlarmRecieverThirtyMintues extends BroadcastReceiver {
    SharedPreference sharedPreference;
    @Override
    public void onReceive(Context context, Intent intent) {
        sharedPreference = new SharedPreference(context);
        if (Build.VERSION.SDK_INT >= 29) {
            if(sharedPreference.get("thirtymintues")==2) {
                Intent panelIntent = new Intent(Settings.Panel.ACTION_INTERNET_CONNECTIVITY);
                panelIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(panelIntent);
                Toast.makeText(context, "Please turn on Wifi", Toast.LENGTH_SHORT).show();
            }

        }
        else {
            WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            wifiManager.setWifiEnabled(true);
            WifiConfiguration wifiConfig = new WifiConfiguration();

            wifiConfig.SSID = String.format("\"%s\"", "");
            wifiConfig.preSharedKey = String.format("\"%s\"", "");

            int netId = wifiManager.addNetwork(wifiConfig);
            wifiManager.disconnect();
            wifiManager.enableNetwork(netId, true);
            wifiManager.reconnect();

            /*
             */

            WifiConfiguration conf = new WifiConfiguration();
            conf.SSID = "\"\"" + "" + "\"\"";
            conf.preSharedKey = "\"" + "" + "\"";
            wifiManager.addNetwork(conf);
            Toast.makeText(context, "Wifi turned On After 30 Minutes ", Toast.LENGTH_SHORT).show();
        }
    }

}