//package wifi.key.show.master.show.all.wifi.password.Service;
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
//import android.net.wifi.ScanResult;
//import android.net.wifi.WifiManager;
//import android.os.Build;
//import android.os.Handler;
//import android.os.IBinder;
//
//import androidx.annotation.Nullable;
//import androidx.annotation.RequiresApi;
//import androidx.core.app.NotificationCompat;
//
//import wifi.key.show.master.show.all.wifi.password.Activities.OpenWifiScanActivity;
//import wifi.key.show.master.show.all.wifi.password.R;
//
//
//import java.util.List;
//
//
//public class OpenWifiCallingService extends Service {
//    Context context;
//
//    private int nooftimes = 700;
//    private Handler mHandler;
//
//    Camera cam;
//    int i = 0;
//
//    private boolean stopRunning = false;
//
//
//    Thread t = null;
//    public NotificationManager manager;
//
//    @Override
//    public int onStartCommand(Intent intent, int flags, int startId) {
//        super.onStartCommand(intent, flags, startId);
//
//
//        mHandler = new Handler();
//        WifiManager wifimanger = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
//        List<ScanResult> networkList = wifimanger.getScanResults();
//        if (networkList != null) {
//            for (ScanResult network : networkList) {
//                String capabilities = network.capabilities;
//                if (capabilities.toUpperCase().contains("WEP")) {
//                } else if (capabilities.toUpperCase().contains("WPA")
//                        || capabilities.toUpperCase().contains("WPA2")) {
//
//                    // WPA or WPA2 Network
//                } else {
//
//                    NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//
//                    Intent intentViewPost = new Intent(getBaseContext(), OpenWifiScanActivity.class);
//                    PendingIntent pendingIntent = PendingIntent.getActivity(getBaseContext(), (int) System.currentTimeMillis(), intentViewPost, 0);
//
//                    Notification notification = new Notification.Builder(getBaseContext())
//                            .setSmallIcon(R.drawable.icon)
//                            .setContentTitle(getResources().getString(R.string.app_name))
//                            .setContentText("we are Searching Open Wifi For You")
//                            .setContentIntent(pendingIntent)
//                            .build();
//
//                    manager.notify(111, notification);
//                }
//            }
//        }
//
//        startRepeatingTask();
//
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//
//
//            Intent notificationIntent = new Intent(getBaseContext(), OpenWifiScanActivity.class);
//
//            PendingIntent contentIntent;
//            if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.R){
//                contentIntent = PendingIntent.getActivity(this, 0, notificationIntent,
//                         PendingIntent.FLAG_MUTABLE);
//            }
//            else {
//                contentIntent = PendingIntent.getActivity(this, 0, notificationIntent,
//                        PendingIntent.FLAG_CANCEL_CURRENT);
//            }
//
//            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//            String channelId = Build.VERSION.SDK_INT >= Build.VERSION_CODES.O ? makenotification(notificationManager) : "Incoming Flash Light";
//
//
//            NotificationCompat.Builder builder =
//                    new NotificationCompat.Builder(getApplicationContext(), channelId)
//                            .setSmallIcon(R.mipmap.ic_launcher)
//                            .setLargeIcon(BitmapFactory.decodeResource(Resources.getSystem(), R.mipmap.ic_launcher))
//                            .setContentTitle("Wifi Open Scanner")
//                            .setContentText("Searching Open Wifi for you")
//                            .addAction(R.drawable.icon, "Stop", contentIntent).setAutoCancel(true);
//
//
//            notificationManager.notify(0, builder.build());
//
//            builder.setContentIntent(contentIntent);
//
//
//            Notification notification = builder.setSmallIcon(R.drawable.ic_launcher_foreground).setContentInfo("Incoming Flash Light notification will be remove automatically.").setCategory(NotificationCompat.CATEGORY_SERVICE).build();
//            startForeground(100, notification);
//
//        }
//
//
//        return START_STICKY;
//    }
//
//    @Override
//    public void onCreate() {
//        super.onCreate();
//        mHandler = new Handler();
//
//
//        startRepeatingTask();
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
//                    context = OpenWifiCallingService.this;
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
