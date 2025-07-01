package wifi.key.show.master.show.all.wifi.password.Ads;

import android.app.Application;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.cleversolutions.ads.AdLoadCallback;
import com.cleversolutions.ads.AdType;
import com.cleversolutions.ads.ConsentFlow;
import com.cleversolutions.ads.MediationManager;
import com.cleversolutions.ads.android.CAS;
import com.google.firebase.FirebaseApp;
import wifi.key.show.master.show.all.wifi.password.utils.Constants;

public class ApplicationClass extends Application {
    public static final String TAG = "CAS_TAG";
    public static MediationManager adManager;
    @Override
    public void onCreate() {
        super.onCreate();
        adValuesDefault();
        FirebaseApp.initializeApp(this);
        CAS.settings.setDebugMode(false);
        adManager = CAS.buildManager()
                .withManagerId(getPackageName())
                .withAdTypes(AdType.Banner, AdType.Interstitial,AdType.AppOpen,AdType.Rewarded)
                .withTestAdMode(false)
                .withConsentFlow(
                        new ConsentFlow(true)
                                .withDismissListener(status -> Log.d(TAG, "Consent Flow dismissed"))
                )
                .withCompletionListener(config -> {
                    if (config.getError() == null) {
                        Log.d(TAG, "initialized");
                    } else {
                        Log.d(TAG, "initialization failed: " + config.getError());
                    }
                })
                .initialize(this);
        ApplicationClass.adManager.getOnAdLoadEvent().add(new AdLoadCallback() {
            @Override
            public void onAdLoaded(@NonNull AdType adType) {
                if (adType == AdType.Interstitial) {
                    Log.d(ApplicationClass.TAG, "Interstitial Ad loaded and ready to show");
                }
            }
            @Override
            public void onAdFailedToLoad(@NonNull AdType adType, @Nullable String s) {
                if (adType == AdType.Interstitial) {
                    Log.d(ApplicationClass.TAG, "Interstitial Ad received error: " + s);
                }
            }
        });
    }
    private void adValuesDefault(){
        Constants.ADS_VALUES.put(Constants.APP_OPEN,"true");
        Constants.ADS_VALUES.put(Constants.WIFI_INFO_INTER,"true");
        Constants.ADS_VALUES.put(Constants.WIFI_SPEED_TEST_INTER,"true");
        Constants.ADS_VALUES.put(Constants.WIFI_SCAN_INTER,"true");
        Constants.ADS_VALUES.put(Constants.MY_WIFI_INTER,"true");
        Constants.ADS_VALUES.put(Constants.MAIN_BANNER,"true");
        Constants.ADS_VALUES.put(Constants.PASS_BANNER,"true");
        Constants.ADS_VALUES.put(Constants.PERSONAL_INTER,"true");
        Constants.ADS_VALUES.put(Constants.PERSONAL_BANNER,"true");
        Constants.ADS_VALUES.put(Constants.AD_IMG,"null");
        Constants.ADS_VALUES.put(Constants.AD_TITLE,"null");
        Constants.ADS_VALUES.put(Constants.AD_DESCRIPTION,"null");
        Constants.ADS_VALUES.put(Constants.AD_PACKAGE_NAME,"null");
    }
}