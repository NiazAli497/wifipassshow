package wifi.key.show.master.show.all.wifi.password.Activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.DhcpInfo;
import android.net.TrafficStats;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.text.format.Formatter;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;
import com.github.lzyzsd.circleprogress.ArcProgress;
import wifi.key.show.master.show.all.wifi.password.MyPrefHelper;
import wifi.key.show.master.show.all.wifi.password.R;
import wifi.key.show.master.show.all.wifi.password.databinding.ActivityWifiInfoNewBinding;
import wifi.key.show.master.show.all.wifi.password.utils.AdLoadingDialog;
import wifi.key.show.master.show.all.wifi.password.utils.Constants;
import com.cleversolutions.ads.MediationManager;
import com.cleversolutions.ads.AdPaidCallback;
import com.cleversolutions.ads.AdStatusHandler;
import wifi.key.show.master.show.all.wifi.password.Ads.ApplicationClass;

public class WifiInformationActivity extends AppCompatActivity {
    public String dns_server2,gatewa,dns_server;
    public String ipAddress,s_leaseDuration;
    public String net_mask,Server_Address;
    long mStartRX = 0;
    long mStartTX = 0;
    TextView  nameT;
    long t;
    ArcProgress arcProgress1;
    long t2;
    TextView tvPing;
    DhcpInfo d;
    WifiManager wifii;
    WifiConfiguration configuration;
    MyPrefHelper prefHelper;
    private MediationManager adManager;
    private AdLoadingDialog adLoadingDialog;
    private AdPaidCallback adCallback;

//    private MutableLiveData<Integer> myLiveData= new MutableLiveData<>();

//    private final Handler mHandler = new Handler();
//    private final Runnable mRunnable = new Runnable() {
//        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
//        public void run() {
//            displayDetails();
//            mHandler.postDelayed(mRunnable, 1000);
//        }
//    };

//    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
//        public void onReceive(Context context, Intent intent) {
//            ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
//            NetworkInfo wifi = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
//            boolean isConnected = wifi != null && wifi.isConnectedOrConnecting();
//            if(isConnected){
//                displayDetails();
//            }
//            else {
//                displayDetails();
//            }
//        }
//    };


    ActivityWifiInfoNewBinding binding;

    private int calculateSignalStrength(){
        WifiManager wifii = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
       WifiInfo wifiInfo=  wifii.getConnectionInfo();
       int strength= wifiInfo.getRssi();
       int s=  WifiManager.calculateSignalLevel(strength, 100);
       int percentage=  (int)(s / 100.0 * 100);
       return percentage;

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityWifiInfoNewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.a.setSelected(true);
        wifii = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        prefHelper= new MyPrefHelper(this);

        arcProgress1 = findViewById(R.id.signalProgressBar);

        configuration = new WifiConfiguration();

        nameT = findViewById(R.id.a);

        tvPing = findViewById(R.id.tvPing);

//        PingChecker();

        mStartRX = TrafficStats.getTotalRxBytes();
        mStartTX = TrafficStats.getTotalTxBytes();
//        mHandler.postDelayed(mRunnable, 1000);

        d = wifii.getDhcpInfo();

        dns_server = String.valueOf(Formatter.formatIpAddress(d.dns1));
        dns_server2 = String.valueOf(Formatter.formatIpAddress(d.dns2));
        gatewa = String.valueOf(Formatter.formatIpAddress(d.gateway));
        ipAddress = String.valueOf(Formatter.formatIpAddress(d.ipAddress));
        s_leaseDuration = String.valueOf(Formatter.formatIpAddress(d.leaseDuration));
        net_mask = String.valueOf(Formatter.formatIpAddress(d.netmask));
        Server_Address = String.valueOf(Formatter.formatIpAddress(d.serverAddress));

        binding.tvDNS1.setText(dns_server);
        binding.tvDNS2.setText(dns_server2);
        binding.tvDefaultGateway.setText(gatewa);
        binding.tvIpAddress.setText(ipAddress);
        binding.tvLeaseTime.setText(s_leaseDuration);
        binding.tvSubnetMask.setText(net_mask);
        binding.tvServerIp.setText(Server_Address);


//        myLiveData.setValue(calculateSignalStrength());
//        myLiveData.observe(this, integer -> {
//            new Thread(() -> {
//                 myLiveData.postValue(calculateSignalStrength());
//                runOnUiThread(() -> {
                    binding.textSignalStrengthPercentage.setText(calculateSignalStrength()+"%");
                    arcProgress1.setProgress(calculateSignalStrength());
//                });
//            }).start();
//
//        });

        displayDetails();

        adManager = ApplicationClass.adManager;
        adLoadingDialog = new AdLoadingDialog(this);
        adCallback = new AdPaidCallback() {
            @Override public void onShown(AdStatusHandler ad) { adLoadingDialog.dismiss(); }
            @Override public void onAdRevenuePaid(AdStatusHandler ad) {}
            @Override public void onShowFailed(String message) { adLoadingDialog.dismiss(); WifiInformationActivity.super.onBackPressed(); }
            @Override public void onClicked() {}
            @Override public void onComplete() {}
            @Override public void onClosed() { adLoadingDialog.dismiss(); WifiInformationActivity.super.onBackPressed(); }
        };

    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//        if(mReceiver!=null) {
//            IntentFilter filter = new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");
//            registerReceiver(mReceiver, filter);
//        }
//    }

//    public boolean PingChecker() {
//        t = Calendar.getInstance().getTimeInMillis();
//        Runtime runtime = Runtime.getRuntime();
//        try {
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


    @SuppressLint("SetTextI18n")
    public void displayDetails() {
        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        if (wifiInfo!=null){
            int signal_strength = wifiInfo.getRssi();
            int level= WifiManager.calculateSignalLevel(signal_strength,100);
            int percentage = (int) ((level/100.0)*100);
            String signalStr = "";
            if (signal_strength > -50) {
                signalStr = "Excellent";
            } else if (signal_strength < -50 && signal_strength > -60) {
                signalStr = "Good";
            } else if (signal_strength < -60 && signal_strength > -70) {
                signalStr = "Fair";
            } else if (signal_strength < -70 && signal_strength > -100) {
                signalStr = "Weak";
            }

            if(Math.abs(signal_strength)==127){
                binding.textSignalStrengthPercentage.setText("0%");
            }
            else {
                binding.textSignalStrengthPercentage.setText(""+percentage+"%");
            }

            arcProgress1.setProgress(percentage);
            arcProgress1.setBottomText("");
            String name = "";
            name=wifiInfo.getSSID();

            String strength = "";
            strength=wifiInfo.getRssi() + "dBm";

            String signal = "";
            signal=WifiManager.calculateSignalLevel(wifiInfo.getRssi(), 5) + "/5";
            String signalStrength = signalStr;

            String speed = "";
            speed=wifiInfo.getLinkSpeed() + "Mbps";
            String ipAddress = Formatter.formatIpAddress(wifiInfo.getIpAddress());
            String frequency = (float) wifiInfo.getFrequency() / 1000 + "GHz";
            nameT.setText(name);
            binding.tvLevel.setText(strength);
            binding.tvStrength.setText(signal);
            binding.tvSignalStrength.setText("" + signalStrength);
            binding.tvWifiSpeed.setText(speed);
            binding.tvIpAddressTop.setText(ipAddress);
            binding.tvSignalFrequency.setText(frequency);
            tvPing.setText(String.valueOf(t2 - t));
        }
    }

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

//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        if(mReceiver!=null) {
//            try {
//                unregisterReceiver(mReceiver);
//            } catch (RuntimeException e) {
//                e.printStackTrace();
//            }
//        }
//
//    }
//    @Override
//    protected void onPause() {
//        super.onPause();
//        if(mReceiver!=null) {
//            try {
//                unregisterReceiver(mReceiver);
//            } catch (RuntimeException e) {
//                e.printStackTrace();
//            }
//        }
//    }

}

