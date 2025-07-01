package wifi.key.show.master.show.all.wifi.password.ActivityNew;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import com.cleversolutions.ads.AdCallback;
import com.cleversolutions.ads.AdError;
import com.cleversolutions.ads.AdStatusHandler;
import com.cleversolutions.ads.CASAppOpen;
import com.cleversolutions.ads.LoadAdCallback;
import com.cleversolutions.ads.MediationManager;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import org.json.JSONObject;
import java.util.Objects;
import wifi.key.show.master.show.all.wifi.password.Ads.ApplicationClass;
import wifi.key.show.master.show.all.wifi.password.R;
import wifi.key.show.master.show.all.wifi.password.utils.Constants;

public class Splash2 extends AppCompatActivity {
    private CASAppOpen appOpenAd;
    private boolean appOpenAdVisible = false;
    private Handler handler;
    private Runnable runnable;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_new);
        remoteConfig();
    }
    private void remoteConfig(){
        FirebaseRemoteConfig mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
//                .setMinimumFetchIntervalInSeconds(0)
                .build();
        mFirebaseRemoteConfig.setConfigSettingsAsync(configSettings);
        mFirebaseRemoteConfig.fetchAndActivate().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                boolean updated = task.getResult();
                Log.d(ApplicationClass.TAG, "remote config updated: " + updated);
                String jsonString = mFirebaseRemoteConfig.getString(Constants.JSON_KEY);
                adValues(jsonString);
            } else {
                Log.d(ApplicationClass.TAG, "remote config failed");
            }
        });
    }
    private void adValues(String jsonString){
        Log.e(ApplicationClass.TAG, "adValues: " + jsonString);
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            Constants.ADS_VALUES.put(Constants.APP_OPEN,jsonObject.getString(Constants.APP_OPEN));
            Constants.ADS_VALUES.put(Constants.WIFI_INFO_INTER,jsonObject.getString(Constants.WIFI_INFO_INTER));
            Constants.ADS_VALUES.put(Constants.WIFI_SPEED_TEST_INTER,jsonObject.getString(Constants.WIFI_SPEED_TEST_INTER));
            Constants.ADS_VALUES.put(Constants.WIFI_SCAN_INTER,jsonObject.getString(Constants.WIFI_SCAN_INTER));
            Constants.ADS_VALUES.put(Constants.MY_WIFI_INTER,jsonObject.getString(Constants.MY_WIFI_INTER));
            Constants.ADS_VALUES.put(Constants.MAIN_BANNER,jsonObject.getString(Constants.MAIN_BANNER));
            Constants.ADS_VALUES.put(Constants.PASS_BANNER,jsonObject.getString(Constants.PASS_BANNER));
            Constants.ADS_VALUES.put(Constants.PERSONAL_INTER,jsonObject.getString(Constants.PERSONAL_INTER));
            Constants.ADS_VALUES.put(Constants.PERSONAL_BANNER,jsonObject.getString(Constants.PERSONAL_BANNER));
            Constants.ADS_VALUES.put(Constants.AD_IMG,jsonObject.getString(Constants.AD_IMG));
            Constants.ADS_VALUES.put(Constants.AD_TITLE,jsonObject.getString(Constants.AD_TITLE));
            Constants.ADS_VALUES.put(Constants.AD_DESCRIPTION,jsonObject.getString(Constants.AD_DESCRIPTION));
            Constants.ADS_VALUES.put(Constants.AD_PACKAGE_NAME,jsonObject.getString(Constants.AD_PACKAGE_NAME));
            if (Objects.requireNonNull(Constants.ADS_VALUES.get(Constants.APP_OPEN)).equalsIgnoreCase("true")){
                createAppOpenAd();
            }
        }catch (Exception ignored){}
    }
    private void splashHandler(){
        runnable = () -> {
            if (appOpenAdVisible){
                appOpenAd.show(Splash2.this);
            }else {
                startNextActivity();
            }
        };
        handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(runnable, 9000);
    }
    @Override
    protected void onResume() {
        super.onResume();
        splashHandler();
    }
    @Override
    protected void onPause() {
        super.onPause();
        handler.removeCallbacks(runnable);
    }
    private void createAppOpenAd() {
        MediationManager initializedManager = ApplicationClass.adManager;
        if (initializedManager == null)
            appOpenAd = CASAppOpen.create(getPackageName());
        else {
            appOpenAd = CASAppOpen.create(initializedManager);
        }
        appOpenAd.setContentCallback(new AdCallback() {
            @Override
            public void onShown(@NonNull AdStatusHandler adStatusHandler) {}
            @Override
            public void onShowFailed(@NonNull String message) {
                startNextActivity();
            }
            @Override
            public void onClosed() {
                startNextActivity();
            }
            @Override
            public void onClicked() {}
            @Override
            public void onComplete() {}
        });
        appOpenAd.loadAd(this, new LoadAdCallback() {
            @Override
            public void onAdLoaded() {
                appOpenAdVisible = true;
            }
            @Override
            public void onAdFailedToLoad(@NonNull AdError adError) {}
        });
    }
    private void startNextActivity(){
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

}