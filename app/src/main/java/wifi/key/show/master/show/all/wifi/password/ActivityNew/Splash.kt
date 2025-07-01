package wifi.key.show.master.show.all.wifi.password.ActivityNew

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.os.CountDownTimer
import androidx.appcompat.app.AppCompatActivity
import com.cleversolutions.ads.AdCallback
import com.cleversolutions.ads.AdError
import com.cleversolutions.ads.AdStatusHandler
import com.cleversolutions.ads.CASAppOpen
import com.cleversolutions.ads.LoadAdCallback
import com.cleversolutions.ads.MediationManager
import wifi.key.show.master.show.all.wifi.password.Ads.ApplicationClass
import wifi.key.show.master.show.all.wifi.password.databinding.ActivitySplashNewBinding
import java.util.concurrent.TimeUnit

class Splash : AppCompatActivity() {
    private lateinit var binding: ActivitySplashNewBinding
    private var loadingAppResInProgress = false
    private var appOpenAdVisible = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashNewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        splashTimer()
        createAppOpenAd()
    }
    private fun splashTimer() {
        loadingAppResInProgress = true
        val timer: CountDownTimer = object : CountDownTimer(TimeUnit.SECONDS.toMillis(5), 1000) {
            @SuppressLint("SetTextI18n")
            override fun onTick(millisUntilFinished: Long) {}
            override fun onFinish() {
                loadingAppResInProgress = false
                startNextActivity()
            }
        }
        timer.start()
    }
    private fun createAppOpenAd() {
        val initializedManager: MediationManager = ApplicationClass.adManager
        val appOpenAd: CASAppOpen = CASAppOpen.create(initializedManager)
        appOpenAd.contentCallback = object : AdCallback {
            override fun onShown(ad: AdStatusHandler) {}
            override fun onShowFailed(message: String) {
                appOpenAdVisible = false
                startNextActivity()
            }
            override fun onClosed() {
                appOpenAdVisible = false
                startNextActivity()
            }
            override fun onClicked() {}
            override fun onComplete() {}
        }
        appOpenAd.loadAd(
            this,
            resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE,
            object : LoadAdCallback {
                override fun onAdLoaded() {
                    if (!loadingAppResInProgress) {
                        return
                    }
                    appOpenAdVisible = true
                    appOpenAd.show(this@Splash)
                }
                override fun onAdFailedToLoad(error: AdError) {
                    if (!loadingAppResInProgress) {
                        return
                    }
                    startNextActivity()
                }
            })
    }
    private fun startNextActivity() {
        if (loadingAppResInProgress || appOpenAdVisible) {
            return
        }
        startActivity(Intent(this, GetStartActivity::class.java))
        finish()
    }
}