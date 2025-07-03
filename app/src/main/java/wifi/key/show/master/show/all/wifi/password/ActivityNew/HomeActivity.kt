package wifi.key.show.master.show.all.wifi.password.ActivityNew

import android.Manifest
import android.animation.Animator
import android.animation.Animator.AnimatorListener
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.ComponentName
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.net.wifi.WifiManager
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.provider.Settings
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.cleversolutions.ads.AdCallback
import com.cleversolutions.ads.AdPaidCallback
import com.cleversolutions.ads.AdStatusHandler
import com.cleversolutions.ads.MediationManager
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.google.android.material.progressindicator.LinearProgressIndicator
import wifi.key.show.master.show.all.wifi.password.Activities.DataUsageActivity
import wifi.key.show.master.show.all.wifi.password.Activities.HotSpotShareActivity
import wifi.key.show.master.show.all.wifi.password.Activities.OpenWifiScanActivity
import wifi.key.show.master.show.all.wifi.password.Activities.QrImageDisplayMain
import wifi.key.show.master.show.all.wifi.password.Activities.WifiAutomaticActivity
import wifi.key.show.master.show.all.wifi.password.Activities.WifiInformationActivity
import wifi.key.show.master.show.all.wifi.password.Activities.WifiPasswordGeneratorActivity
import wifi.key.show.master.show.all.wifi.password.Activities.WifiScanActivity
import wifi.key.show.master.show.all.wifi.password.Activities.WifiSettingsActivity
import wifi.key.show.master.show.all.wifi.password.Activities.WifiTestSpeedActivity
import wifi.key.show.master.show.all.wifi.password.ActivityNew.HomeActivity
import wifi.key.show.master.show.all.wifi.password.Adapters.HomeAdapter
import wifi.key.show.master.show.all.wifi.password.Ads.AdManager
import wifi.key.show.master.show.all.wifi.password.Ads.ApplicationClass
import wifi.key.show.master.show.all.wifi.password.MyPrefHelper
import wifi.key.show.master.show.all.wifi.password.R
import wifi.key.show.master.show.all.wifi.password.SharedPreference.MyPreference
import wifi.key.show.master.show.all.wifi.password.databinding.ActivityMainNewBinding
import wifi.key.show.master.show.all.wifi.password.databinding.DialogOverlayPermissionBinding
import wifi.key.show.master.show.all.wifi.password.utils.Constants
import wifi.key.show.master.show.all.wifi.password.utils.DashboardEnums
import wifi.key.show.master.show.all.wifi.password.utils.MainRecyclerData
import wifi.key.show.master.show.all.wifi.password.utils.AdLoadingDialog
import java.util.Objects


class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainNewBinding
    var sharedPref: MyPreference? = null
    var overlayPeramsDialog: Dialog? = null
    private lateinit var homeAdapter: HomeAdapter
    private var dataList = ArrayList<MainRecyclerData>()
    private var dialog: AlertDialog.Builder? = null

    //    val myLiveData: MutableLiveData<Int> = MutableLiveData()
    private var adManager: MediationManager? = null
    private var contentCallback: AdCallback? = null
    private var adButton = 0
    private val textArray by lazy {
        arrayOf(
            getString(R.string.wifi_info),
            getString(R.string.wifi_speed_test),
            getString(
                R.string.data_usage
            ),
            getString(R.string.wifi_automation),
            getString(R.string.pass_generator),
            getString(
                R.string.wifi_scan
            ),
            getString(R.string.hotspot),
            getString(R.string.show_wifi_pass),
            getString(R.string.my_wifi)
        )
    }
    lateinit var prefHelper: MyPrefHelper
    private var iconArray = arrayOf(
        R.drawable.ic_wifi_info,
        R.drawable.ic_speed_test,
        R.drawable.ic_data_usage,
        R.drawable.ic_wifi_automation,
        R.drawable.ic_pass_fill,
        R.drawable.ic_qr_scan,
        R.drawable.ic_hotspot,
        R.drawable.ic_show_password,
        R.drawable.ic_wifi_icon
    )

    //    var adLoadingDialog: AdLoadingDialog? = null
//    var adLoadingHandler = Handler(Looper.getMainLooper())
    var data = false
    var TAG = "MainActivity"
    var openMessage: DashboardEnums? = null

    //    private val admobRunnable = Runnable {
//        openMessage?.let { processMessage(it) }
//    }
//    private val mReceiver: BroadcastReceiver = object : BroadcastReceiver() {
//        override fun onReceive(context: Context, intent: Intent) {
//            val connMgr = context.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
//            val wifi = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
//            val isConnected = wifi != null && wifi.isConnectedOrConnecting
//            if (isConnected) {
//                displayDetails()
//            } else {
//                displayDetails()
//            }
//        }
//    }
    private fun displayDetails() {
        val wifiManager = applicationContext.getSystemService(WIFI_SERVICE) as WifiManager
        val wifiInfo = wifiManager.connectionInfo
        if (wifiInfo != null) {
            val signal_strength = wifiInfo.rssi
            val level = WifiManager.calculateSignalLevel(signal_strength, 100)
            val percentage = (level / 100.0 * 100).toInt()
            var name: String? = ""
            name = wifiInfo.ssid

            Log.d(TAG, "Wifi Name: " + level)
            Log.d(TAG, "Wifi Name: " + percentage)

            if (!binding.wifiName.text.equals(name)) {

                if (name.equals("<unknown ssid>")) {
                    binding.wifiConnectedText.text = "Not Connected"
                    binding.wifiName.text = "No Wifi Connected"
                } else {
                    binding.wifiName.text = name
                    binding.wifiConnectedText.text = "Connected"
                }
            }
            binding.signalProgressBar.setProgress(percentage)
            if (Math.abs(signal_strength) == 127) {
                binding.signalProgress.setText("0%")
            } else {
                binding.signalProgress.setText("$percentage%")
            }
        }
    }

    fun calculateSignalStrength(): Int {
        val wifiManager = applicationContext.getSystemService(WIFI_SERVICE) as WifiManager
        val wifiInfo = wifiManager.connectionInfo
        val signal_strength = wifiInfo.rssi
        val level = WifiManager.calculateSignalLevel(signal_strength, 100)
        val percentage = (level / 100.0 * 100).toInt()
        return percentage
    }

    private fun requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this@HomeActivity,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    private val permissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { _: Boolean? -> }

    private var adLoadingDialog: AdLoadingDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainNewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.wifiName.isSelected = true

        adLoadingDialog = AdLoadingDialog(this)

        requestPermission()

        prefHelper = MyPrefHelper(this@HomeActivity)
//        myLiveData.value = calculateSignalStrength()
//        myLiveData.observe(this) {
//            CoroutineScope(Dispatchers.IO).launch {
//                myLiveData.postValue(calculateSignalStrength())
//            }
//            Log.d("APPDATAYYY", "value : " + it)

        binding.signalProgress.setText("${calculateSignalStrength()}%")
        binding.signalProgressBar.setProgress(calculateSignalStrength())

//        }


//        mHandler.postDelayed(mRunnable, 1000)
        val adsStatus = arrayOf(false, false, false)

        binding.mainRecycler.setHasFixedSize(true)
        binding.mainRecycler.layoutManager =
            GridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false)


        for (i in 0 until textArray.size) {
            dataList.add(MainRecyclerData(textArray[i], iconArray[i]))
        }

        displayDetails()

        createBanner()
        getMediationManager()
        createInterstitial()

        homeAdapter = HomeAdapter(this, dataList)
        binding.mainRecycler.adapter = homeAdapter
        homeAdapter.onClick = {
            Constants.personalInterCounter++
            when (it) {
                0 -> {
                    if (ContextCompat.checkSelfPermission(
                            this@HomeActivity,
                            Manifest.permission.ACCESS_FINE_LOCATION
                        ) == PackageManager.PERMISSION_GRANTED
                    ) {
                        adButton = 1
                        if (Objects.requireNonNull<String?>(Constants.ADS_VALUES[Constants.PERSONAL_INTER])
                                .equals("true", ignoreCase = true)
                            && Constants.personalInterCounter % 2 == 1) {
                            personalAdDialog()
                        } else if (Objects.requireNonNull<String?>(Constants.ADS_VALUES[Constants.WIFI_INFO_INTER])
                                .equals("true", ignoreCase = true)
                            && Constants.personalInterCounter % 2 == 1) {
                            showInterstitial()
                        } else {
                            startActivity(
                                Intent(
                                    this@HomeActivity,
                                    WifiInformationActivity::class.java
                                )
                            )
                        }
                    } else {
                        ActivityCompat.requestPermissions(
                            this@HomeActivity, arrayOf(
                                Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION
                            ), 10
                        )
                    }
                }
                1 -> {
                    if (ContextCompat.checkSelfPermission(
                            this@HomeActivity,
                            Manifest.permission.ACCESS_FINE_LOCATION
                        ) == PackageManager.PERMISSION_GRANTED
                    ) {
                        adButton = 2
                        if (Objects.requireNonNull<String?>(Constants.ADS_VALUES[Constants.WIFI_SPEED_TEST_INTER])
                                .equals("true", ignoreCase = true)
                            && Constants.personalInterCounter % 2 == 1) {
                            showInterstitial()
                        } else {
                            startActivity(
                                Intent(
                                    this@HomeActivity,
                                    WifiTestSpeedActivity::class.java
                                )
                            )
                        }
                    } else {
                        ActivityCompat.requestPermissions(
                            this@HomeActivity, arrayOf(
                                Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION
                            ), 10
                        )
                    }
                }
                2 -> {
                    adButton = 5
                    if (Objects.requireNonNull<String?>(Constants.ADS_VALUES[Constants.WIFI_SCAN_INTER])
                            .equals("true", ignoreCase = true)
                        && Constants.personalInterCounter % 2 == 1) {
                        showInterstitial()
                    } else {
                        openDataUsageScreen()
                    }
                }
                3 -> {
                    if (ContextCompat.checkSelfPermission(
                            this@HomeActivity,
                            Manifest.permission.ACCESS_FINE_LOCATION
                        ) == PackageManager.PERMISSION_GRANTED
                    ) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                            if (!Settings.canDrawOverlays(this)) {
                                peeramsDialog()
                            } else {
                                adButton = 6
                                if (Objects.requireNonNull<String?>(Constants.ADS_VALUES[Constants.WIFI_SPEED_TEST_INTER])
                                        .equals("true", ignoreCase = true)
                                    && Constants.personalInterCounter % 2 == 1) {
                                    showInterstitial()
                                } else {
                                    val intent = Intent(
                                        this@HomeActivity,
                                        WifiAutomaticActivity::class.java
                                    )
                                    startActivity(intent)
                                }
                            }
                        } else {
                            adButton = 6
                            if (Objects.requireNonNull<String?>(Constants.ADS_VALUES[Constants.WIFI_SPEED_TEST_INTER])
                                    .equals("true", ignoreCase = true)
                                && Constants.personalInterCounter % 2 == 1) {
                                showInterstitial()
                            } else {
                                val intent = Intent(
                                    this@HomeActivity,
                                    WifiAutomaticActivity::class.java
                                )
                                startActivity(intent)
                            }
                        }
                    } else {
                        ActivityCompat.requestPermissions(
                            this@HomeActivity, arrayOf(
                                Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION
                            ), 10
                        )
                    }
                }
                4 -> {
                    adButton = 7
                    if (Objects.requireNonNull<String?>(Constants.ADS_VALUES[Constants.WIFI_SPEED_TEST_INTER])
                            .equals("true", ignoreCase = true)
                        && Constants.personalInterCounter % 2 == 1) {
                        showInterstitial()
                    } else {
                        startActivity(Intent(this, WifiPasswordGeneratorActivity::class.java))
                    }
                }
                5 -> {
                    if (ContextCompat.checkSelfPermission(
                            this@HomeActivity,
                            Manifest.permission.ACCESS_FINE_LOCATION
                        ) == PackageManager.PERMISSION_GRANTED
                    ) {
                        adButton = 4
                        if (Objects.requireNonNull<String?>(Constants.ADS_VALUES[Constants.WIFI_SCAN_INTER])
                                .equals("true", ignoreCase = true)
                            && Constants.personalInterCounter % 2 == 1) {
                            showInterstitial()
                        } else {
                            val wifiManager =
                                applicationContext.getSystemService(WIFI_SERVICE) as WifiManager
                            wifiManager.isWifiEnabled = true
                            startActivity(Intent(this@HomeActivity, WifiScanActivity::class.java))
                        }
                    } else {
                        ActivityCompat.requestPermissions(
                            this@HomeActivity, arrayOf(
                                Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION
                            ), 10
                        )
                    }
                }
                6 -> {
                    if (ContextCompat.checkSelfPermission(
                            this@HomeActivity,
                            Manifest.permission.ACCESS_FINE_LOCATION
                        ) == PackageManager.PERMISSION_GRANTED
                    ) {
                        adButton = 8
                        if (Objects.requireNonNull<String?>(Constants.ADS_VALUES[Constants.WIFI_SCAN_INTER])
                                .equals("true", ignoreCase = true)
                            && Constants.personalInterCounter % 2 == 1) {
                            showInterstitial()
                        } else {
                            val intent = Intent(
                                this@HomeActivity,
                                HotSpotShareActivity::class.java
                            )
                            startActivity(intent)
                        }
                    } else {
                        ActivityCompat.requestPermissions(
                            this@HomeActivity, arrayOf(
                                Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION
                            ), 10
                        )
                    }
                }
                7 -> {
                    if (ContextCompat.checkSelfPermission(
                            this@HomeActivity,
                            Manifest.permission.CAMERA
                        ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                            this@HomeActivity,
                            Manifest.permission.READ_EXTERNAL_STORAGE
                        ) == PackageManager.PERMISSION_GRANTED
                    ) {
                        val intent = Intent(
                            this@HomeActivity,
                            QrImageDisplayMain::class.java
                        )
                        startActivity(intent)
                    } else {
                        ActivityCompat.requestPermissions(
                            this@HomeActivity, arrayOf(
                                Manifest.permission.CAMERA,
                                Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE
                            ), 20
                        )
                    }
                }
                8 -> {
                    if (ContextCompat.checkSelfPermission(
                            this@HomeActivity,
                            Manifest.permission.ACCESS_FINE_LOCATION
                        ) == PackageManager.PERMISSION_GRANTED
                    ) {
                        adButton = 0
                        if (Objects.requireNonNull<String?>(Constants.ADS_VALUES[Constants.MY_WIFI_INTER])
                                .equals("true", ignoreCase = true)
                            && Constants.personalInterCounter % 2 == 1) {
                            showInterstitial()
                        } else {
                            startActivity(
                                Intent(
                                    this@HomeActivity,
                                    OpenWifiScanActivity::class.java
                                )
                            )
                        }
                    } else {
                        ActivityCompat.requestPermissions(
                            this@HomeActivity, arrayOf(
                                Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION
                            ), 10
                        )
                    }
                }
            }
        }

        binding.settingIcon.setOnClickListener {
            startActivity(Intent(this@HomeActivity, WifiSettingsActivity::class.java))
        }

//        initDialog()
        sharedPref = MyPreference(this@HomeActivity)


        val firstrun =
            getSharedPreferences("PREFERENCE", MODE_PRIVATE).getBoolean("mycustomdialog", true)
        if (firstrun) {
            termsDialog()
        }
    }

    private fun peeramsDialog() {

        overlayPeramsDialog = Dialog(this)
        val binding = DialogOverlayPermissionBinding.inflate(layoutInflater)
        overlayPeramsDialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        overlayPeramsDialog?.setContentView(binding.root)
        overlayPeramsDialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        overlayPeramsDialog?.window?.setLayout(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        binding.butSetting.setOnClickListener {
            try {
                val intent = Intent(
                    Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse(
                        "package:$packageName"
                    )
                )
                startActivityForResult(intent, 101)
            } catch (e: ActivityNotFoundException) {
                e.printStackTrace()
            } catch (e: Exception) {
                e.printStackTrace()
            }
            overlayPeramsDialog?.dismiss()
        }
        binding.butDismiss.setOnClickListener {
            overlayPeramsDialog?.dismiss()
        }
        overlayPeramsDialog?.show()

    }


//    private fun adLoadingProgram(message: DashboardEnums) {
//        openMessage = message
//        processMessage(message)
//        if (prefHelper.getConsentValue() && AppUtils.lastInterstitialTime == 0L || System.currentTimeMillis() > AppUtils.lastInterstitialTime) {
//            if (AdmobUtilsAdvance.checkIntersAvailable()) {
//                if (adLoadingDialog != null && !adLoadingDialog!!.isShowing) {
//                    adLoadingDialog!!.show()
//                }
//                adLoadingHandler.postDelayed(admobRunnable, 1000)
//            } else {
//                processMessage(message)
//            }
//        } else {
//            processMessage(message)
//        }
//    }

//    private fun initDialog() {
//        adLoadingDialog = AdLoadingDialog(this, android.R.style.ThemeOverlay)
//        adLoadingDialog?.setCancelable(false)
//    }

    private fun openDataUsageScreen() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            try {
                val intent = Intent()
                intent.component = ComponentName(
                    "com.android.settings",
                    "com.android.settings.Settings\$DataUsageSummaryActivity"
                )
                startActivity(intent)
            } catch (exception: ActivityNotFoundException) {
                exception.printStackTrace()
                Toast.makeText(this, R.string.unable_to_open, Toast.LENGTH_SHORT).show()
            }
        } else {
            startActivity(Intent(this@HomeActivity, DataUsageActivity::class.java))
        }
    }


    fun termsDialog() {
        Log.d("termsDialog", "termsDialog: termsDialog")
        val alert = Dialog(this@HomeActivity, R.style.AppTheme)
        alert.requestWindowFeature(Window.FEATURE_NO_TITLE)
        alert.setContentView(R.layout.terms_condition_new)
        alert.setCancelable(false)
        val btnAgree = alert.findViewById<Button>(R.id.accept)
        val btnDissagree = alert.findViewById<Button>(R.id.Decline)
        btnAgree.setOnClickListener { v: View? ->
            getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                .edit()
                .putBoolean("mycustomdialog", false)
                .apply()
            alert.dismiss()
        }
        btnDissagree.setOnClickListener { v: View? ->
            alert.dismiss()
            finishAffinity()
        }
        alert.show()
        alert.window!!.setLayout(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT
        )
    }


    override fun onResume() {
        super.onResume()
        adManager!!.loadInterstitial()
    }


//    override fun onResume() {
//        screenVisible = true
//        super.onResume()
//        if (adLoadingDialog != null && adLoadingDialog!!.isShowing) {
//            adLoadingDialog!!.dismiss()
//        }
//        val filter = IntentFilter("android.net.conn.CONNECTIVITY_CHANGE")
//        registerReceiver(mReceiver, filter)
//    }

//    override fun onPause() {
////        screenVisible = false
////        appMinimizeAfterAdRequest = true
//        super.onPause()
////        adLoadingHandler.removeCallbacks(admobRunnable)
//        try {
//            unregisterReceiver(mReceiver)
//        } catch (e: RuntimeException) {
//            e.printStackTrace()
//        }
//    }

//    override fun onStop() {
//        onStopFunction()
//        super.onStop()
//        adLoadingHandler.removeCallbacks(admobRunnable)
//        if (adLoadingDialog != null && adLoadingDialog!!.isShowing) {
//            adLoadingDialog!!.dismiss()
//        }
//    }


    var backPressTime = 0L

    @SuppressLint("MissingSuperCall")
    override fun onBackPressed() {
        if (backPressTime < System.currentTimeMillis()) {
            backPressTime = System.currentTimeMillis() + 2000
            Toast.makeText(
                this, "Press Back again to exit!!",
                Toast.LENGTH_SHORT
            ).show()
        } else {
            finishAffinity()
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            10 -> {

                try {
                    if (grantResults.isNotEmpty() && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                        if (ActivityCompat.shouldShowRequestPermissionRationale(
                                this,
                                Manifest.permission.ACCESS_FINE_LOCATION
                            ) || ActivityCompat.shouldShowRequestPermissionRationale(
                                this,
                                Manifest.permission.ACCESS_COARSE_LOCATION
                            )
                        ) {
                            Toast.makeText(
                                this,
                                getString(R.string.allow_location_permission),
                                Toast.LENGTH_SHORT
                            ).show()

                        } else {
                            explain(getString(R.string.allow_location_permission), 10)
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }

            20 -> {

                try {
                    if (grantResults.isNotEmpty() && grantResults[0] != PackageManager.PERMISSION_GRANTED || grantResults[1] != PackageManager.PERMISSION_GRANTED) {
                        if (ActivityCompat.shouldShowRequestPermissionRationale(
                                this,
                                Manifest.permission.CAMERA
                            ) || ActivityCompat.shouldShowRequestPermissionRationale(
                                this,
                                Manifest.permission.READ_EXTERNAL_STORAGE
                            )
                        ) {
                            Toast.makeText(
                                this,
                                getString(R.string.allow_both_camera_storage_permission),
                                Toast.LENGTH_SHORT
                            ).show()

                        } else {
                            explain(getString(R.string.allow_both_camera_storage_permission), 20)
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }

        }
    }

    override fun onDestroy() {
        super.onDestroy()
//        adLoadingHandler.removeCallbacks(admobRunnable)
        if (overlayPeramsDialog != null && overlayPeramsDialog!!.isShowing) {
            overlayPeramsDialog?.dismiss()
        }
        adLoadingDialog?.dismiss()
    }

    private fun explain(msg: String, code: Int) {
        dialog = AlertDialog.Builder(this)
        dialog?.setMessage(msg)?.setPositiveButton(
            "Go Settings"
        ) { paramDialogInterface, paramInt ->
            try {
                startActivityForResult(
                    Intent(
                        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                        Uri.parse("package:" + applicationContext.packageName)
                    ), code
                )

            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
        }
            ?.setNegativeButton(
                "Cancel"
            ) { paramDialogInterface, paramInt -> dialog?.create()?.dismiss() }
        dialog?.show()
    }

    private fun createBanner() {
        if (Objects.requireNonNull<String?>(Constants.ADS_VALUES[Constants.MAIN_BANNER])
                .equals("true", ignoreCase = true)
        ) {
            val manager = ApplicationClass.adManager
            AdManager.getAdManager().createBanner(this@HomeActivity, manager, binding.bannerHome)
        }
    }

    private fun getMediationManager() {
        adManager = ApplicationClass.adManager
    }

    private fun createInterstitial() {
        contentCallback = object : AdPaidCallback {
            override fun onShown(ad: AdStatusHandler) {
                adLoadingDialog?.dismiss()
                Log.d(ApplicationClass.TAG, "Interstitial Ad shown from " + ad.network)
            }
            override fun onAdRevenuePaid(ad: AdStatusHandler) {
                Log.d(ApplicationClass.TAG, "Rewarded Ad revenue paid from " + ad.network)
            }
            override fun onShowFailed(message: String) {
                adLoadingDialog?.dismiss()
                Log.e(ApplicationClass.TAG, "Interstitial Ad show failed: $message")
            }
            override fun onClicked() {
                Log.d(ApplicationClass.TAG, "Interstitial Ad received Click")
            }
            override fun onComplete() {}
            override fun onClosed() {
                adLoadingDialog?.dismiss()
                Log.d(ApplicationClass.TAG, "Interstitial Ad received Close")
                when (adButton) {
                    0 -> {
                        startActivity(Intent(this@HomeActivity, OpenWifiScanActivity::class.java))
                    }
                    1 -> {
                        startActivity(
                            Intent(
                                this@HomeActivity,
                                WifiInformationActivity::class.java
                            )
                        )
                    }
                    2 -> {
                        startActivity(Intent(this@HomeActivity, WifiTestSpeedActivity::class.java))
                    }
                    3 -> {
                        startActivity(
                            Intent(
                                this@HomeActivity,
                                WifiPasswordGeneratorActivity::class.java
                            )
                        )
                    }
                    4 -> {
                        val wifiManager =
                            applicationContext.getSystemService(WIFI_SERVICE) as WifiManager
                        wifiManager.isWifiEnabled = true
                        startActivity(Intent(this@HomeActivity, WifiScanActivity::class.java))
                    }
                    5 -> {
                        openDataUsageScreen()
                    }
                    6 -> {
                        startActivity(
                            Intent(
                                this@HomeActivity,
                                WifiAutomaticActivity::class.java
                            )
                        )
                    }
                    7->{
                        startActivity(Intent(this@HomeActivity,WifiPasswordGeneratorActivity::class.java))
                    }
                    8->{
                        startActivity(Intent(
                            this@HomeActivity,
                            HotSpotShareActivity::class.java
                        ))
                    }
                }
            }
        }
    }


    private fun showInterstitial() {
        if (adManager!!.isInterstitialReady) {
            adLoadingDialog?.show()
            adManager!!.showInterstitial(this@HomeActivity, contentCallback)
        } else {
            adLoadingDialog?.dismiss()
            when (adButton) {
                0 -> {
                    startActivity(Intent(this@HomeActivity, OpenWifiScanActivity::class.java))
                }
                1 -> {
                    startActivity(Intent(this@HomeActivity, WifiInformationActivity::class.java))
                }
                2 -> {
                    startActivity(Intent(this@HomeActivity, WifiTestSpeedActivity::class.java))
                }
                3 -> {
                    startActivity(Intent(this, WifiPasswordGeneratorActivity::class.java))
                }
                4 -> {
                    val wifiManager =
                        applicationContext.getSystemService(WIFI_SERVICE) as WifiManager
                    wifiManager.isWifiEnabled = true
                    startActivity(Intent(this@HomeActivity, WifiScanActivity::class.java))
                }
                5 -> {
                    openDataUsageScreen()
                }
                6 -> {
                    startActivity(
                        Intent(
                            this@HomeActivity,
                            WifiAutomaticActivity::class.java
                        )
                    )
                }
                7->{
                    startActivity(Intent(this, WifiPasswordGeneratorActivity::class.java))
                }
                8->{
                    startActivity(Intent(
                        this@HomeActivity,
                        HotSpotShareActivity::class.java
                    ))
                }
            }
        }
    }
//    private fun processMessage(message: DashboardEnums) {
//        when (message) {
//            DashboardEnums.MY_WIFI ->{
//                adButton = 0
//                showInterstitial()
//            }
//            DashboardEnums.WIFI_INFO -> {
//                adButton = 1
//                showInterstitial()
//            }
//            DashboardEnums.WIFI_TEST_SPEED -> {
//                adButton = 2
//                showInterstitial()
//            }
//            DashboardEnums.WIFI_SETTINGS -> startActivity(
//                Intent(
//                    this@HomeActivity,
//                    WifiSettingsActivity::class.java
//                )
//            )
//            DashboardEnums.WIFI_AUTOMATIC -> startActivity(
//                Intent(
//                    this@HomeActivity,
//                    WifiAutomaticActivity::class.java
//                )
//            )
//            DashboardEnums.PASSWORD_GENERATOR -> {
//                adButton = 3
//                showInterstitial()
//            }
//            DashboardEnums.WIFI_SCAN -> {
//                adButton = 4
//                showInterstitial()
//            }
//            DashboardEnums.HOTSPOT -> startActivity(
//                Intent(
//                    this@HomeActivity,
//                    HotSpotShareActivity::class.java
//                )
//            )
//            DashboardEnums.SHOW_WIFI_PASSWORD -> startActivity(
//                Intent(
//                    this@HomeActivity,
//                    QrImageDisplayMain::class.java
//                )
//            )
//            else -> {}
//        }
//    }

    private fun personalAdDialog() {
        val personalDialog = Dialog(this@HomeActivity, R.style.dialogTheme)
        personalDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        personalDialog.setContentView(R.layout.personal_inter_dialog)
        personalDialog.setCancelable(false)
        personalDialog.setCanceledOnTouchOutside(false)
        val layoutParams = WindowManager.LayoutParams()
        personalDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        layoutParams.copyFrom(personalDialog.window!!.attributes)
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT
        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT
        layoutParams.gravity = Gravity.CENTER
        personalDialog.window!!.attributes = layoutParams
        val adCloseButton = personalDialog.findViewById<ImageView>(R.id.personal_inter_close_button)
        val adCloseProgress =
            personalDialog.findViewById<CircularProgressIndicator>(R.id.personal_inter_close_progress)
        val installButton =
            personalDialog.findViewById<AppCompatButton>(R.id.personal_inter_ad_button)
        val icon = personalDialog.findViewById<ImageView>(R.id.personal_inter_ad_img)
        val title = personalDialog.findViewById<TextView>(R.id.personal_inter_ad_title)
        val description = personalDialog.findViewById<TextView>(R.id.personal_inter_ad_description)
        Glide.with(this@HomeActivity)
            .load(Objects.requireNonNull<String?>(Constants.ADS_VALUES[Constants.AD_IMG]))
            .into(icon)
        title.text = Objects.requireNonNull(Constants.ADS_VALUES[Constants.AD_TITLE])
        description.text = Objects.requireNonNull(Constants.ADS_VALUES[Constants.AD_DESCRIPTION])
        installButton.setOnClickListener {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse(
                        "market://details?id=" + Objects.requireNonNull(
                            Constants.ADS_VALUES[Constants.AD_PACKAGE_NAME]
                        )
                    )
                )
            )
        }
        personalDialog.show()
        val animator = ValueAnimator.ofInt(0, adCloseProgress.max)
        animator.setDuration(8000)
        animator.addUpdateListener { animation: ValueAnimator ->
            adCloseProgress.setProgress(
                animation.animatedValue as Int
            )
        }
        animator.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                adCloseButton.setOnClickListener {
                    personalDialog.dismiss()
                    startActivity(Intent(this@HomeActivity, WifiInformationActivity::class.java))
                }
            }
        })
        animator.start()
    }
}

