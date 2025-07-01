//package com.example.myapplication.Service;
//
//import android.app.Notification;
//import android.app.NotificationChannel;
//import android.app.NotificationManager;
//import android.app.PendingIntent;
//import android.app.Service;
//import android.content.Context;
//import android.content.Intent;
//import android.content.res.Resources;
//import android.graphics.BitmapFactory;
//import android.hardware.Camera;
//import android.net.NetworkInfo;
//import android.net.wifi.ScanResult;
//import android.net.wifi.WifiManager;
//import android.os.Build;
//import android.os.Handler;
//import android.os.IBinder;
//import android.widget.Toast;
//
//import androidx.annotation.Nullable;
//import androidx.annotation.RequiresApi;
//import androidx.core.app.NotificationCompat;
//
//import OpenWifiScanActivity;
//import com.example.myapplication.R;
//
//import java.util.List;
//
//
//public class WifiCheckingConnection extends Service {
//
//
//    @Override
//    public int onStartCommand(Intent intent, int flags, int startId) {
//        super.onStartCommand(intent, flags, startId);
//        String action = "MyAction";
//
//        if (WifiManager.NETWORK_STATE_CHANGED_ACTION.equals(action)) {
//
//            NetworkInfo info = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
//
//            switch (info.getState()) {
//                case CONNECTING:
//                    Toast.makeText(getBaseContext(), "Connecting", Toast.LENGTH_SHORT).show();
//                    break;
//                case CONNECTED:
//                    Toast.makeText(getBaseContext(), "Connected", Toast.LENGTH_SHORT).show();
//
//                    break;
//                case DISCONNECTING:
//                    Toast.makeText(getBaseContext(), "Connected", Toast.LENGTH_SHORT).show();
//
//                    break;
//                case DISCONNECTED:
//                    Toast.makeText(getBaseContext(), "Connected", Toast.LENGTH_SHORT).show();
//
//                    break;
//                case SUSPENDED:
//                    break;
//            }
//        }
//
//
//        return START_STICKY;
//    }
//
//    @Override
//    public void onCreate() {
//        super.onCreate();
//        String action = "MyAction";
//
//        if (WifiManager.NETWORK_STATE_CHANGED_ACTION.equals(action)) {
//
//            NetworkInfo info = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
//
//            switch (info.getState()) {
//                case CONNECTING:
//                    Toast.makeText(getBaseContext(), "Connecting", Toast.LENGTH_SHORT).show();
//                    break;
//                case CONNECTED:
//                    Toast.makeText(getBaseContext(), "Connected", Toast.LENGTH_SHORT).show();
//
//                    break;
//                case DISCONNECTING:
//                    Toast.makeText(getBaseContext(), "Connected", Toast.LENGTH_SHORT).show();
//
//                    break;
//                case DISCONNECTED:
//                    Toast.makeText(getBaseContext(), "Connected", Toast.LENGTH_SHORT).show();
//
//                    break;
//                case SUSPENDED:
//                    break;
//            }
//        }
//
//
//    }
//
//    @RequiresApi(Build.VERSION_CODES.O)
//    private String makenotification(NotificationManager notificationManager) {
//        String channelId = "my_service_channelid";
//        String channelName = "My Foreground Service";
//        NotificationChannel channel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_MIN);
//        notificationManager.createNotificationChannel(channel);
//
//
//        return channelId;
//
//
//    }
//
//    Runnable mStatusChecker = new Runnable() {
//        @Override
//        public void run() {
//            try {
//                try {
//                    context = WifiCheckingConnection.this;
//
//                    WifiManager wifimanger = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
//                    List<ScanResult> networkList = wifimanger.getScanResults();
//                    if (networkList != null) {
//                        for (ScanResult network : networkList) {
//                            String capabilities = network.capabilities;
//                            if (capabilities.toUpperCase().contains("WEP")) {
//                            } else if (capabilities.toUpperCase().contains("WPA")
//                                    || capabilities.toUpperCase().contains("WPA2")) {
//
//                                // WPA or WPA2 Network
//                            } else {
//
//                                // Open Network
//                            }
//                        }
//                    }
//                } catch (Exception ee) {
//
//                }
//
//            } finally {
//                mHandler.postDelayed(mStatusChecker, nooftimes);
//            }
//
//        }
//    };
//
//    void startRepeatingTask() {
//        mStatusChecker.run();
//    }
//
//    void stopRepeatingTask() {
//
//        try {
//            mHandler.removeCallbacks(mStatusChecker);
//        } catch (NullPointerException ee) {
//
//
//        }
//    }
//
//
//    @Nullable
//    @Override
//    public IBinder onBind(Intent intent) {
//        return null;
//    }
//
//    @Nullable
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//
//        if (t != null) {
//            stopRunning = true;
//            t = null;
//
//        }
//
//        stopRepeatingTask();
//    }
//}
