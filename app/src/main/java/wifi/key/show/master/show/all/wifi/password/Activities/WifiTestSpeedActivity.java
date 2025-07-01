package wifi.key.show.master.show.all.wifi.password.Activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.TrafficStats;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import java.io.IOException;
import java.util.Calendar;

import es.dmoral.toasty.Toasty;
import wifi.key.show.master.show.all.wifi.password.DatabaseHelper.DataBaseWifiHistory;
import wifi.key.show.master.show.all.wifi.password.MyPrefHelper;
import wifi.key.show.master.show.all.wifi.password.R;
import wifi.key.show.master.show.all.wifi.password.SharedPreference.MyPreference;
import wifi.key.show.master.show.all.wifi.password.databinding.ActivityWifiSpeedTestNewBinding;

public class WifiTestSpeedActivity extends AppCompatActivity {
    DataBaseWifiHistory dataBaseWifiHistory;
    public String mymobile;
//    private final Handler mHandler = new Handler();
//    long t;
//    long t2;
    long mStartRX = 0;
    long mStartTX = 0;
    WifiManager wifiManager;
    WifiInfo wifiInfo;
//    private Runnable mRunnable;
    MyPreference preference;
    MyPrefHelper prefHelper;
    ActivityWifiSpeedTestNewBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityWifiSpeedTestNewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        displayWifiName();
        prefHelper = new MyPrefHelper(this);



//        AdRequest adRequest = new AdRequest.Builder().build();
//        binding.adView.loadAd(adRequest);
        preference = new MyPreference(WifiTestSpeedActivity.this);
        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        setClickListeners();

        wifiInfo = wifiManager.getConnectionInfo();
        dataBaseWifiHistory = new DataBaseWifiHistory(WifiTestSpeedActivity.this);

        mStartRX = TrafficStats.getTotalRxBytes();
        mStartTX = TrafficStats.getTotalTxBytes();

//        isOnline();

        setData();
    }

    private void setData() {
        int signal_strength = wifiInfo.getRssi();
        String signal = null;
        if (signal_strength > -50) {
            signal = "Excellent";
        } else if (signal_strength < -50 && signal_strength > -60) {
            signal = "Good";
        } else if (signal_strength < -60 && signal_strength > -70) {
            signal = "Fair";
        } else if (signal_strength < -70 && signal_strength > -100) {
            signal = "Weak";
        }

        String strength = wifiInfo.getRssi() + "dBm";
        String Signal = WifiManager.calculateSignalLevel(wifiInfo.getRssi(), 5) + "/5";
        String Speed = wifiInfo.getLinkSpeed() + "Mbps";


        String Signalstrenght = signal;
        String name = wifiInfo.getSSID();


        ConnectivityManager conMan = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

//        try {
//            //mobile
//            NetworkInfo.State mobile = conMan.getNetworkInfo(0).getState();
//
////wifi
//            NetworkInfo.State wifi = conMan.getNetworkInfo(1).getState();
//
//            if (mobile == NetworkInfo.State.CONNECTED || mobile == NetworkInfo.State.CONNECTING) {
//                mymobile = "Mobile Data";
//            } else if (wifi == NetworkInfo.State.CONNECTED || wifi == NetworkInfo.State.CONNECTING) {
//                mymobile = "Wifi ";
//
//            }
//        }catch (Exception e){
//            e.printStackTrace();
//        }


        Calendar callForDate = Calendar.getInstance();

        java.text.SimpleDateFormat currentDate = new java.text.SimpleDateFormat("dd-MMMM-yyyy");

        final String saveCurrentDate = currentDate.format(callForDate.getTime());


        dataBaseWifiHistory.savehistory("0", saveCurrentDate, name, String.valueOf((signal_strength + 127)), Speed, mymobile, Signal);

    }


    private void setClickListeners() {
        binding.butSpeedHistory.setOnClickListener(v -> {
            startActivity(new Intent(WifiTestSpeedActivity.this, NewDisplayWifiHistoryActivity.class));
        });
        binding.butSpeedTest.setOnClickListener(v -> {
            ConnectivityManager connManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
            NetworkInfo wifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (wifi.isConnected()) {

                setupSpeedoMeters();
                displayDetails();

                binding.butSpeedTest.setVisibility(View.INVISIBLE);

//                Toasty.success(WifiTestSpeedActivity.this, "Start!", Toast.LENGTH_SHORT, true).show();
//                mRunnable = () -> {
//                    setupSpeedoMeters();
////                    mypingfunction();
////                    displayDetails();
//                    mHandler.postDelayed(mRunnable, 1000);
//                };
//                mHandler.postDelayed(mRunnable, 1000);
            } else {
                Toasty.success(WifiTestSpeedActivity.this, getString(R.string.connect_wifi_first), Toast.LENGTH_SHORT, true).show();

            }

        });
    }

    public void displayWifiName() {
        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        String name = "";
        name = wifiInfo.getSSID();

        if (name.equals("<unknown ssid>")) {
            binding.wifiName.setText(R.string.no_wifi_connected);
        } else {
            binding.wifiName.setText(name);
        }

    }


    public void displayDetails() {
        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        if (wifiInfo != null) {
            int signal_strength = wifiInfo.getRssi();
            String name = "";
            name = wifiInfo.getSSID();
            if (!binding.wifiName.getText().equals(name)) {
                if (name.equals("<unknown ssid>")) {
                    binding.wifiName.setText(R.string.no_wifi_connected);
                } else {
                    binding.wifiName.setText(name);
                }
            }
            if (Math.abs(signal_strength) == 127) {
                binding.signalText.setText("0%");
            } else {
                binding.signalText.setText("" + Math.abs(signal_strength));
            }
        }
//        mypingfunction();
//        setupSpeedoMeters();
    }

//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//        mHandler.removeCallbacks(mRunnable);
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        mHandler.removeCallbacks(mRunnable);
//    }

    @SuppressLint("SetTextI18n")
    public void setupSpeedoMeters() {
        binding.deluxeSpeedView.setUnit("Mbps");
        float t = wifiInfo.getLinkSpeed();
        binding.deluxeSpeedView.speedTo(t);
        binding.pingText.setText("" + t);
        long rxBytes = TrafficStats.getTotalRxBytes() - mStartRX;
        if (rxBytes >= 1024) {
            long rxKb = rxBytes / 1024;
            binding.downloadText.setText("" + rxKb);
        }
        long txBytes = TrafficStats.getTotalTxBytes() - mStartTX;
        if (txBytes >= 1024) {
            final long txkb = txBytes / 1024;
            binding.uploadText.setText("" + txkb);
        }
    }
//    public boolean mypingfunction() {
//        /*Just to check Time delay*/
//        t = Calendar.getInstance().getTimeInMillis();
//        Runtime runtime = Runtime.getRuntime();
//        try {
//            /*Pinging to Google server*/
//            Process ipProcess = runtime.exec("ping -c 1 google.com");
//            int exitValue = ipProcess.waitFor();
//            return (exitValue == 0);
//        } catch (IOException | InterruptedException e) {
//            e.printStackTrace();
//        } finally {
//            t2 = Calendar.getInstance().getTimeInMillis();
//        }
//        return false;
//    }

//    public boolean isOnline() {
//        /*Just to check Time delay*/
//        t = Calendar.getInstance().getTimeInMillis();
//
//        Runtime runtime = Runtime.getRuntime();
//        try {
//            /*Pinging to Google server*/
//            Process ipProcess = runtime.exec("ping -c 1 google.com");
//            int exitValue = ipProcess.waitFor();
//            return (exitValue == 0);
//        } catch (IOException | InterruptedException e) {
//            e.printStackTrace();
//        } finally {
//            t2 = Calendar.getInstance().getTimeInMillis();
//
//        }
//        return false;
//    }

}
