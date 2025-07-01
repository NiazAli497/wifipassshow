//package wifi.key.show.master.show.all.wifi.password.Activities;
//
//import android.annotation.SuppressLint;
//import android.content.Intent;
//import android.os.Bundle;
//import android.os.CountDownTimer;
//import android.os.Handler;
//import android.os.Looper;
//import android.util.Log;
//import android.view.Window;
//import android.view.WindowManager;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.google.android.gms.ads.AdError;
//import com.google.android.gms.ads.AdRequest;
//import com.google.android.gms.ads.FullScreenContentCallback;
//import com.google.android.gms.ads.LoadAdError;
//import com.google.android.gms.ads.appopen.AppOpenAd;
//import com.hura.allvideodownloaderapp.interfaces.AdLoadListener;
//
//import wifi.key.show.master.show.all.wifi.password.ActivityNew.GetStartActivity;
//import wifi.key.show.master.show.all.wifi.password.R;
//import wifi.key.show.master.show.all.wifi.password.SharedPreference.MyPreference;
//import wifi.key.show.master.show.all.wifi.password.utils.AdLoadingDialog;
//import wifi.key.show.master.show.all.wifi.password.utils.AdmobUtils;
//import wifi.key.show.master.show.all.wifi.password.utils.AdmobUtilsAdvance;
//import wifi.key.show.master.show.all.wifi.password.utils.AppOpenManager;
//import wifi.key.show.master.show.all.wifi.password.utils.AppUtils;
//import wifi.key.show.master.show.all.wifi.password.utils.DashboardEnums;
//
//@SuppressLint("CustomSplashScreen")
//public class WelcomeActivity extends AppCompatActivity {
//    MyPreference MyPreference;
////    NumberProgressBar bnp;
//    CountDownTimer splashTimer;
//
//    AdLoadingDialog adLoadingDialog;
//
//    Handler adLoadingHandler = new Handler(Looper.getMainLooper());
//
//    boolean timerFinished = false;
//
//    boolean appMinimizeAfterAdRequest = true;
//
//    boolean screenVisible = true;
//
//    CountDownTimer countDownTimer;
//
//    String TAG="WelcomeActivity";
//
//    AppOpenAd appOpenAd;
//    private AppOpenAd.AppOpenAdLoadCallback loadCallback;
//
//    boolean isAdLoaded=false;
//
//
//    private void runAdLoadCountDown() {
//
//        countDownTimer=new CountDownTimer(20*1000,1000) {
//            @Override
//            public void onTick(long l) {
//                Log.e(TAG, "Timer running:"+l);
//
//            }
//
//            @Override
//            public void onFinish() {
//                timerFinished = true;
//                runOnUiThread(() -> {
//                    if (adLoadingDialog.isShowing()){
//                        adLoadingDialog.dismiss();
//                    }
//                });
//                onStopFunction();
//                startActivity(new Intent(WelcomeActivity.this,GetStartActivity.class));
//                finish();
//            }
//        }.start();
//
//
//    }
//
//
//
//    private void initDialog() {
//        adLoadingDialog = new AdLoadingDialog(this, android.R.style.ThemeOverlay);
//        adLoadingDialog.setCancelable(false);
//    }
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        AppOpenManager.userIsOnSplashScreen = true;
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        setContentView(R.layout.activity_splash_new);
//        MyPreference = new MyPreference(this);
//        initDialog();
//        AdmobUtilsAdvance.Companion.load3InterSameTime(this,getString(R.string.interstitial_high),
//                getString(R.string.interstitial_medium),getString(R.string.interstitial_default));
//
////        ImageView img = findViewById(R.id.img);
////        bnp = (NumberProgressBar) findViewById(R.id.myprogress);
////        bnp.setOnProgressBarListener(WelcomeActivity.this);
////        Glide.with(WelcomeActivity.this)
////                .load(R.drawable.mysplshscren)
////                .into(img);
//
////        SharedPreferences preferences = getSharedPreferences("policy", MODE_PRIVATE);
//    }
//
//
//    void splashProgress(){
//        splashTimer=new CountDownTimer(2000,50) {
//            @Override
//            public void onTick(long l) {
//
//            }
//
//            @Override
//            public void onFinish() {
//                if (AppOpenManager.isShowingAd){
//                    onStopFunction();
//                    startActivity(new Intent(WelcomeActivity.this, GetStartActivity.class));
//                    finish();
//                }
//                else{
//                    if (AppUtils.isInternetConnected(WelcomeActivity.this)){
//                        if (!isAdLoaded){
//                            appOpenLoadingProgram();
//                            interstitialLoadingProgram();
//                        }
//                        else{
//                            if (AdmobUtils.Companion.isInterstitialReady()){
//                                interstitialLoadingProgram();
//                            }
//                            else if (appOpenAd!=null){
//                                appOpenLoadingProgram();
//                            }
//                        }
//                    }
//                    else{
//                        onStopFunction();
//                        startActivity(new Intent(WelcomeActivity.this,GetStartActivity.class));
//                        finish();
//                    }
//
//                }
//            }
//        }.start();
//    }
//
//
//    private final Runnable interstitialRunnable = this::showAdmobInterstitial;
//    private final Runnable appOpenRunnable = this::showAppOpenAd;
//
//
//    public void showAppOpenAd() {
//        FullScreenContentCallback fullScreenContentCallback =
//                new FullScreenContentCallback() {
//                    @Override
//                    public void onAdDismissedFullScreenContent() {
//                        // Set the reference to null so isAdAvailable() returns false.
//                        if (adLoadingDialog.isShowing()){
//                            adLoadingDialog.dismiss();
//                        }
//                        appOpenAd = null;
//                        AppOpenManager.isInterstitialShowing = false;
//                        onStopFunction();
//                        startActivity(new Intent(WelcomeActivity.this,GetStartActivity.class));
//                        finish();
//                    }
//
//                    @Override
//                    public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
//                        if (adLoadingDialog.isShowing()){
//                            adLoadingDialog.dismiss();
//                        }
//                        AppOpenManager.isInterstitialShowing = false;
//                        onStopFunction();
//                        startActivity(new Intent(WelcomeActivity.this,GetStartActivity.class));
//                        finish();
//                    }
//
//                    @Override
//                    public void onAdShowedFullScreenContent() {
//                        AppOpenManager.isInterstitialShowing = false;
//                    }
//                };
//
//        appOpenAd.setFullScreenContentCallback(fullScreenContentCallback);
//        appOpenAd.show(this);
//    }
//
//
//    private void showAdmobInterstitial() {
//        if (!isDestroyed()) {
//            if (adLoadingDialog.isShowing()) {
//                adLoadingDialog.dismiss();
//            }
//
//
//            AdmobUtils.Companion.showSplashInterstitial(
//                    this,
//                    DashboardEnums.WIFI_SETTINGS, message -> {
//                        onStopFunction();
//                        startActivity(new Intent(WelcomeActivity.this,GetStartActivity.class));
//                        finish();
//                    });
//
//        }
//    }
//
//
//    public void appOpenLoadingProgram() {
//        if (AppUtils.isInternetConnected(this)) {
//            if (appOpenAd!=null){
//                Log.e(TAG, "App open already loaded adding fake delay");
//                adLoadingDialog.show();
//                if (countDownTimer!=null){
//                    countDownTimer.cancel();
//                }
//                splashTimer.cancel();
//                adLoadingHandler.postDelayed(appOpenRunnable,AdmobUtils.Companion.getAdShowDelay());
//            }
//            else{
//                AppOpenManager.isInterstitialShowing = true;
//                appMinimizeAfterAdRequest = false;
//                Log.e(TAG, "going to load app open ad");
//                loadCallback = new AppOpenAd.AppOpenAdLoadCallback() {
//
//                    @Override
//                    public void onAdLoaded(@NonNull AppOpenAd ad) {
//                        appOpenAd = ad;
//                        if (!isAdLoaded){
//                            isAdLoaded=true;
//                            if (countDownTimer!=null){
//                                countDownTimer.cancel();
//                            }
//                            splashTimer.cancel();
//                            appOpenRunnable.run();
//                        }
//                    }
//
//
//
//                    @Override
//                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
//                        // Handle the error.
//                    }
//
//                };
//                AdRequest request = new AdRequest.Builder().build();
//                AppOpenAd.load(this, getResources().getString(R.string.open_ad), request,
//                        AppOpenAd.APP_OPEN_AD_ORIENTATION_PORTRAIT, loadCallback);
//            }
//        }
//        else{
//            onStopFunction();
//            startActivity(new Intent(WelcomeActivity.this,GetStartActivity.class));
//            finish();
//        }
//    }
//
//
//    private void loadAdmobInterstitial() {
//        AdmobUtils.Companion.loadSplashInterstitial(this, new AdLoadListener() {
//            @Override
//            public void adLoadSuccess() {
//                if (!timerFinished && screenVisible && !appMinimizeAfterAdRequest && !isAdLoaded) {
//                    isAdLoaded=true;
//                    if (countDownTimer!=null){
//                        countDownTimer.cancel();
//                    }
//                    splashTimer.cancel();
//                    interstitialRunnable.run();
//                }
//            }
//
//            @Override
//            public void adLoadFailed() {
//                if (!timerFinished && screenVisible && !appMinimizeAfterAdRequest && !isAdLoaded) {
//                    handleAdLoadFailed();
//                }
//            }
//        });
//    }
//
//
//    int retryCounter = 1;
//
//
//    void handleAdLoadFailed() {
//       /* switch (retryCounter) {
//            //first retry
//            //second retry
//            case 1:
//            case 2:
//                retryCounter++;
//                loadAdmobInterstitial();
//                break;
//            default:*/
//        if (countDownTimer!=null){
//            countDownTimer.cancel();
//        }
//                retryCounter = 1;
//        onStopFunction();
//                startActivity(new Intent(this,GetStartActivity.class));
//                finish();
//    }
//
//
//    private void interstitialLoadingProgram(){
//            //if internet is connected
//            if (AppUtils.isInternetConnected(this)) {
//                //to prevent app open overlay on interstitial
//                AppOpenManager.isInterstitialShowing = true;
//
//                appMinimizeAfterAdRequest = false;
//                adLoadingDialog.show();
//
//                //check if admob interstitial is already loaded
//                if (AdmobUtils.Companion.isInterstitialReady()) {
//                        Log.e(TAG, "Admob already loaded adding fake delay");
//                        adLoadingHandler.postDelayed(interstitialRunnable, AdmobUtils.Companion.getAdShowDelay());
//                }
//
//                //else start interstitial loading program
//                else {
//                    if (!isAdLoaded){
//                        Log.e(TAG, "going to load admob ad");
//                        loadAdmobInterstitial();
//                        runAdLoadCountDown();
//                    }
//                }
//            }
//            else{
//                onStopFunction();
//                startActivity(new Intent(this,GetStartActivity.class));
//                finish();
//            }
//    }
//
//
//
//    /**
//     * life cycle methods to deal with interstitial ads
//     */
//    @Override
//    protected void onResume() {
//        screenVisible = true;
//        splashProgress();
//        super.onResume();
//
//    }
//
//    @Override
//    protected void onPause() {
//        screenVisible = false;
//        appMinimizeAfterAdRequest = true;
//        super.onPause();
//    }
//
//
//    void onStopFunction(){
//        splashTimer.cancel();
//
//        adLoadingHandler.removeCallbacks(interstitialRunnable);
//        adLoadingHandler.removeCallbacks(appOpenRunnable);
//        screenVisible = false;
//        appMinimizeAfterAdRequest = true;
//
//        if (adLoadingDialog.isShowing()) {
//
//            if (countDownTimer!=null){
//                countDownTimer.cancel();
//            }
//            Log.e(TAG, "ON_STOP App open ad enabled again as Ad loading dialog is dismissed");
//            //Fragment is not alive to show interstitial
//            // enable app open ads again
//            AppOpenManager.isInterstitialShowing = false;
//
//            adLoadingDialog.dismiss();
//        }
//    }
//
//    @Override
//    protected void onStop() {
//        onStopFunction();
//        super.onStop();
//    }
//
//
//
//}
//
