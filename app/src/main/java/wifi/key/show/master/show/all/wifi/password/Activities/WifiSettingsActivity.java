package wifi.key.show.master.show.all.wifi.password.Activities;

import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import wifi.key.show.master.show.all.wifi.password.MyPrefHelper;
import wifi.key.show.master.show.all.wifi.password.R;
import wifi.key.show.master.show.all.wifi.password.SharedPreference.MyPreference;
import wifi.key.show.master.show.all.wifi.password.SharedPreference.SharedPreference;
import wifi.key.show.master.show.all.wifi.password.databinding.ActivityWifiSettingNewBinding;
import wifi.key.show.master.show.all.wifi.password.utils.AdLoadingDialog;
import wifi.key.show.master.show.all.wifi.password.utils.Constants;
import com.cleversolutions.ads.MediationManager;
import com.cleversolutions.ads.AdPaidCallback;
import com.cleversolutions.ads.AdStatusHandler;
import wifi.key.show.master.show.all.wifi.password.Ads.ApplicationClass;

public class WifiSettingsActivity extends AppCompatActivity {
    SharedPreference preference;
    MyPreference MyPreference;
    //  AdView adView;
    Context context;
    ActivityWifiSettingNewBinding binding;
    private static String TAG= "WifiSettingsActivityDataTT";
    private ConnectivityManager connManager;
    private NetworkInfo mWifi;
    private boolean isWifiSetting=false;
    MyPrefHelper prefHelper;
    private MediationManager adManager;
    private AdLoadingDialog adLoadingDialog;
    private AdPaidCallback adCallback;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= ActivityWifiSettingNewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        context = WifiSettingsActivity.this;
        preference = new SharedPreference(this);
        MyPreference = new MyPreference(this);
        connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        binding.switchWifiEnable.setChecked(mWifi.isConnected());
        setUpWifiCheckListener();
        findViewById(R.id.btnDataUsage).setOnClickListener(v -> {
            openDataUsageScreen();
        });

        prefHelper= new MyPrefHelper(this);

        adManager = ApplicationClass.adManager;
        adLoadingDialog = new AdLoadingDialog(this);
        adCallback = new AdPaidCallback() {
            @Override public void onShown(AdStatusHandler ad) { adLoadingDialog.dismiss(); }
            @Override public void onAdRevenuePaid(AdStatusHandler ad) {}
            @Override public void onShowFailed(String message) { adLoadingDialog.dismiss(); WifiSettingsActivity.super.onBackPressed(); }
            @Override public void onClicked() {}
            @Override public void onComplete() {}
            @Override public void onClosed() { adLoadingDialog.dismiss(); WifiSettingsActivity.super.onBackPressed(); }
        };

        IntentFilter filter = new IntentFilter(WifiManager.WIFI_STATE_CHANGED_ACTION);
        registerReceiver(mReceiver, filter);

    }





    private void setUpWifiCheckListener() {
        binding.switchWifiEnable.setOnCheckedChangeListener((compoundButton, b) -> {

            if(compoundButton.isPressed()){
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    Log.d(TAG, "Executed when True");
                    startActivity(new Intent(Settings.Panel.ACTION_INTERNET_CONNECTIVITY));
                    isWifiSetting = true;
                }
                else {

                    if(b){
                        preference.setint("callannouncerr", 2);
                        WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                        wifiManager.setWifiEnabled(true);
                        Toast.makeText(WifiSettingsActivity.this, getString(R.string.on), Toast.LENGTH_LONG).show();

                    }
                    else {
                        binding.switchWifiEnable.setChecked(false);
                        WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                        wifiManager.setWifiEnabled(false);
                        Toast.makeText(WifiSettingsActivity.this, getString(R.string.off), Toast.LENGTH_LONG).show();
                    }

                }
            }


        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(isWifiSetting){
            mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            binding.switchWifiEnable.setChecked(mWifi.isConnected());
            isWifiSetting=false;
        }

        if(mReceiver!=null) {
            IntentFilter filter = new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");
            registerReceiver(mReceiver, filter);
        }


    }

    @Override
    protected void onPause() {
        super.onPause();
        if(mReceiver!=null)
            try {
                unregisterReceiver(mReceiver);
            } catch (RuntimeException e) {
                e.printStackTrace();
            }
    }

    private void openDataUsageScreen() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            try {
                Intent intent = new Intent();
                intent.setComponent(new ComponentName("com.android.settings", "com.android.settings.Settings$DataUsageSummaryActivity"));
                startActivity(intent);
            } catch (ActivityNotFoundException exception) {
                exception.printStackTrace();
                Toast.makeText(this, R.string.unable_to_open, Toast.LENGTH_SHORT).show();
            }
        }
        //custom data usage activity works below android 7
        else {
            startActivity(new Intent(this, DataUsageActivity.class));
        }
    }



    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {

            ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo wifi = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            boolean isConnected = wifi != null && wifi.isConnectedOrConnecting();
            binding.switchWifiEnable.setChecked(isConnected);
        }
    };

    @Override
    public void onBackPressed() {
        Constants.personalInterCounterBack++;
        if (adManager != null && adManager.isInterstitialReady() && Constants.personalInterCounterBack % 2 == 1) {
            adLoadingDialog.show();
            adManager.showInterstitial(this, adCallback);
        } else {
            super.onBackPressed();
        }
    }

}
