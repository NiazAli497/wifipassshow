//package wifi.key.show.master.show.all.wifi.password.Activities;
//
//import android.app.Activity;
//import android.content.Context;
//import android.content.Intent;
//import android.net.wifi.WifiConfiguration;
//import android.net.wifi.WifiManager;
//import android.os.Build;
//import android.os.Bundle;
//import android.provider.Settings;
//import android.view.View;
//
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.cardview.widget.CardView;
//
//import com.airbnb.lottie.LottieAnimationView;
//
//import wifi.key.show.master.show.all.wifi.password.R;
//
//
//public class WifiTurnOffShowAlert extends AppCompatActivity {
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.wifi_turnonalert);
//
//       // bp.purchase(this, "android.test.purchased");
//        CardView exit=findViewById(R.id.exit);
//        exit.setOnClickListener(v -> finishAffinity());
//        if (Build.VERSION.SDK_INT >= 29) {
//                Intent panelIntent = new Intent(Settings.Panel.ACTION_INTERNET_CONNECTIVITY);
//                startActivity(panelIntent);
//                return;
//            }
//
//        LottieAnimationView animationView = (LottieAnimationView) findViewById(R.id.animation_view);
//        animationView.setAnimation("wifi.json");
//        animationView.loop(true);
//        animationView.playAnimation();
//        WifiManager wifiManager = (WifiManager) this.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
//        wifiManager.setWifiEnabled(true);
//        WifiConfiguration wifiConfig = new WifiConfiguration();
//
//        wifiConfig.SSID = String.format("\"%s\"", "");
//        wifiConfig.preSharedKey = String.format("\"%s\"", "");
//
//        int netId = wifiManager.addNetwork(wifiConfig);
//        wifiManager.disconnect();
//        wifiManager.enableNetwork(netId, true);
//        wifiManager.reconnect();
//
//
//        WifiConfiguration conf = new WifiConfiguration();
//        conf.SSID = "\"\"" + "" + "\"\"";
//        conf.preSharedKey = "\"" + "" + "\"";
//        wifiManager.addNetwork(conf);
//    }
//
//
//    @Override
//
//    public void onDestroy() {
//
//        super.onDestroy();
//
//
//    }
//
//
//}