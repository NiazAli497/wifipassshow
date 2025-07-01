package wifi.key.show.master.show.all.wifi.password.Ads;

import android.content.Context;
import android.util.Log;
import android.widget.RelativeLayout;
import androidx.annotation.NonNull;
import com.cleversolutions.ads.AdError;
import com.cleversolutions.ads.AdSize;
import com.cleversolutions.ads.AdStatusHandler;
import com.cleversolutions.ads.AdViewListener;
import com.cleversolutions.ads.MediationManager;
import com.cleversolutions.ads.android.CASBannerView;

public class AdManager {
    public static AdManager adManager;
    public static AdManager getAdManager() {
        if (adManager == null) {
            adManager = new AdManager();
        }
        return adManager;
    }
    public void createBanner(Context context, MediationManager manager, RelativeLayout container) {
        CASBannerView bannerView = new CASBannerView(context, manager);
        bannerView.setSize(AdSize.BANNER);
        bannerView.setAdListener(new AdViewListener() {
            @Override
            public void onAdViewPresented(@NonNull CASBannerView casBannerView, @NonNull AdStatusHandler adStatusHandler) {
                Log.d(ApplicationClass.TAG, "Banner Ad presented from " + adStatusHandler.getNetwork());
            }
            @Override
            public void onAdViewLoaded(@NonNull CASBannerView casBannerView) {
                Log.d(ApplicationClass.TAG, "Banner Ad loaded and ready to present");
            }
            @Override
            public void onAdViewFailed(@NonNull CASBannerView casBannerView, @NonNull AdError adError) {
                Log.e(ApplicationClass.TAG, "Banner Ad received error: " + adError.getMessage());
            }
            @Override
            public void onAdViewClicked(@NonNull CASBannerView casBannerView) {
                Log.d(ApplicationClass.TAG, "Banner Ad received Click action");
            }
        });
        container.addView(bannerView);
    }
}