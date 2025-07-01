package wifi.key.show.master.show.all.wifi.password.Activities

import android.Manifest
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Point
import android.graphics.drawable.BitmapDrawable
import android.net.ConnectivityManager
import android.net.Uri
import android.net.wifi.WifiConfiguration
import android.net.wifi.WifiEnterpriseConfig
import android.net.wifi.WifiManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.preference.PreferenceManager
import android.provider.MediaStore
import android.provider.Settings
import android.text.InputType
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.*
import android.widget.AdapterView.OnItemClickListener
import androidmads.library.qrgenearator.QRGContents
import androidmads.library.qrgenearator.QRGEncoder
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.zxing.WriterException
import wifi.key.show.master.show.all.wifi.password.Adapters.DeviceAdapter
import wifi.key.show.master.show.all.wifi.password.DatabaseHelper.DataBaseWifiHistory
import wifi.key.show.master.show.all.wifi.password.DatabaseHelper.DatabaseHelper
import wifi.key.show.master.show.all.wifi.password.R
import wifi.key.show.master.show.all.wifi.password.Recievers.WifiTestReciever
import wifi.key.show.master.show.all.wifi.password.SharedPreference.MyPreference
import wifi.key.show.master.show.all.wifi.password.utils.AppUtils
import wifi.key.show.master.show.all.wifi.password.utils.VegaLayoutManager
import java.io.File
import java.io.FileOutputStream

class WifiScanActivity : AppCompatActivity() {
    var listView: ListView? = null
    var wifiManager: WifiManager? = null
    var pass: EditText? = null
    var databaseHelper: DatabaseHelper? = null
    var flag = 0
    var recyclerView: RecyclerView? = null
    private val ssidList = ArrayList<String>()
    private val bssidList = ArrayList<String>()
    private val wepTxKeyIndexArray = ArrayList<Int>()
    private val networkIDS = ArrayList<Int>()
    private val priority = ArrayList<Int>()
    private val hashcode = ArrayList<Int>()
    private val hiddenSsid = ArrayList<WifiEnterpriseConfig>()
    private val wepKey = ArrayList<Array<String>>()
    private var bitmap: Bitmap? = null
    var MyPreference: MyPreference? = null
    var dataBaseWifiHistory: DataBaseWifiHistory? = null
    var ssid: String? = null
    var checkWifiConnected = false
    var connectWifiDialog: Dialog? = null
    var generateQRDialog: Dialog? = null
    var showPasswordDialog: Dialog? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wifi_scan)
        listView = findViewById(R.id.list_view)
        MyPreference = MyPreference(this)
        databaseHelper = DatabaseHelper(this)
        wifiManager = applicationContext.getSystemService(WIFI_SERVICE) as WifiManager
        recyclerView = findViewById(R.id.recycler)
        dataBaseWifiHistory = DataBaseWifiHistory(this@WifiScanActivity)
        setupRecyclerView()
    }

    override fun onStop() {
        if (connectWifiDialog != null && connectWifiDialog!!.isShowing) {
            connectWifiDialog!!.dismiss()
        }
        if (generateQRDialog != null && generateQRDialog!!.isShowing) {
            generateQRDialog!!.dismiss()
        }
        if (showPasswordDialog != null && showPasswordDialog!!.isShowing) {
            showPasswordDialog!!.dismiss()
        }
        super.onStop()
    }

    fun setupRecyclerView() {
        recyclerView?.visibility = View.VISIBLE
        try {
            queryWifiManager()
        } catch (ee: Exception) {
            ee.printStackTrace()
        }
        recyclerView?.setHasFixedSize(true)
        recyclerView?.layoutManager = VegaLayoutManager()
        val mAdapter = DeviceAdapter(
            ssidList,
            hashcode,
            bssidList,
            wepTxKeyIndexArray,
            networkIDS,
            priority,
            hiddenSsid,
            wepKey
        )
        recyclerView?.adapter = mAdapter
        val wifiScanList = wifiManager?.scanResults
        val ssidsList = ArrayList<String>()
        if (wifiScanList != null && wifiScanList.size > 0) {
            for (result in wifiScanList) {
                ssidsList.add(result.SSID)
            }
            listView?.adapter = ListViewAdapter(applicationContext, ssidsList)
            listView?.onItemClickListener =
                OnItemClickListener { parent: AdapterView<*>?, view: View?, position: Int, id: Long ->
                    ssid = listView?.getItemAtPosition(position).toString()
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        try {
                            val panelIntent = Intent(Settings.Panel.ACTION_INTERNET_CONNECTIVITY)
                            panelIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            startActivity(panelIntent)
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    } else {
                        connectToWifi(ssid)
                    }
                }
        }
        findViewById<View>(R.id.showpassword).setOnClickListener { v: View? ->
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.CAMERA
                ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                val i = Intent(this@WifiScanActivity, QrImageDisplayMain::class.java)
                startActivity(i)
            } else {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(
                        Manifest.permission.CAMERA,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    ),
                    20
                )
            }
        }
    }

    var permissions = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )

    private fun wifiConnected(name: String?, dialog: Dialog, wifiSSID: String?) {
        val textSSID = dialog.findViewById<TextView>(R.id.textSSID1)
        val btnGenerateQR = dialog.findViewById<Button>(R.id.generateqr)
        val dialogButton = dialog.findViewById<Button>(R.id.okButton)
        pass = dialog.findViewById(R.id.textPassword)
        if (!name.equals("", ignoreCase = true)) {
            pass?.setText(name)
            btnGenerateQR.visibility = View.VISIBLE
            dialogButton.text = "Connected"
            dialogButton.isEnabled = false
            btnGenerateQR.setOnClickListener { v: View? ->
                val mydialge = Dialog(this@WifiScanActivity)
                mydialge.setContentView(R.layout.showpassword)
                mydialge.setTitle("Connect to Network")
                val textSSID1 = mydialge.findViewById<ImageView>(R.id.myimage)
                val resultant = mydialge.findViewById<Button>(R.id.resultant)
                val preferences1 =
                    PreferenceManager.getDefaultSharedPreferences(this@WifiScanActivity)
                val editor = preferences1.edit()
                editor.putString("Name", name)
                editor.apply()
                resultant.setOnClickListener { v12: View? ->
                    val bitmapPath = MediaStore.Images.Media.insertImage(
                        contentResolver, bitmap, "title", null
                    )
                    val bitmapUri = Uri.parse(bitmapPath)
                    val intent = Intent(Intent.ACTION_SEND)
                    intent.type = "image/png"
                    intent.putExtra(Intent.EXTRA_STREAM, bitmapUri)
                    startActivity(Intent.createChooser(intent, "Share"))
                }
                val mysaveok = mydialge.findViewById<Button>(R.id.mysaveok)
                mysaveok.setOnClickListener { v1: View? ->
                    if (isStoragePermissionsGranted) {
                        val bitmap = (textSSID1.drawable as BitmapDrawable).bitmap
                        Toast.makeText(
                            this@WifiScanActivity,
                            "" + dataBaseWifiHistory!!.ssid,
                            Toast.LENGTH_SHORT
                        ).show()
                        saveImage(bitmap)
                        Toast.makeText(
                            this@WifiScanActivity,
                            getString(R.string.password_saved),
                            Toast.LENGTH_SHORT
                        ).show()
                        mydialge.dismiss()
                    } else {
                        ActivityCompat.requestPermissions(this, permissions, 242)
                        Toast.makeText(this, getString(R.string.allow_storage_permission), Toast.LENGTH_SHORT).show()
                    }
                }
                val manager = getSystemService(WINDOW_SERVICE) as WindowManager
                val display = manager.defaultDisplay
                val point = Point()
                display.getSize(point)
                val width = point.x
                val height = point.y
                var smallerDimension = Math.min(width, height)
                smallerDimension = smallerDimension * 3 / 4
                val qrgEncoder = QRGEncoder(
                    """
                      $wifiSSID
                      $name
                      """.trimIndent(), null,
                    QRGContents.Type.TEXT,
                    smallerDimension
                )
                try {
                    bitmap = qrgEncoder.getBitmap(0)
                    textSSID1.setImageBitmap(bitmap)
                } catch (e: WriterException) {
                    e.printStackTrace()
                }
                mydialge.show()
            }
        } else {
            btnGenerateQR.visibility = View.GONE
            dialogButton.setText(R.string.connect)
            dialogButton.isEnabled = true
        }
        textSSID.text = wifiSSID
        val hide = dialog.findViewById<ImageView>(R.id.hide)
        hide.setOnClickListener { v: View? ->
            if (flag == 0) {
                hide.setImageResource(R.drawable.eye)
                pass?.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD)
                flag = 1
            } else if (flag == 1) {
                hide.setImageResource(R.drawable.eyeoff)
                pass?.setInputType(InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD)
                flag = 0
            }
        }
        dialogButton.setOnClickListener { v: View? ->
            val checkPassword = pass?.getText().toString()
            if (!TextUtils.isEmpty(checkPassword)) {
                finallyConnect(checkPassword, wifiSSID)
                sendBroadcast(
                    Intent(this@WifiScanActivity, WifiTestReciever::class.java).setAction(
                        "MyAction"
                    )
                )
                Handler().postDelayed({
                    checkWifiConnected = isWifiConnected
                    if (checkWifiConnected) {
                        Toast.makeText(
                            this@WifiScanActivity,
                            "Connected Successfully",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        Toast.makeText(this@WifiScanActivity, getString(R.string.not_connected), Toast.LENGTH_SHORT)
                            .show()
                    }
                }, 4000)
                val handler = Handler()
                handler.postDelayed({
                    if (checkWifiConnected) {
                        val mydialge = Dialog(this@WifiScanActivity)
                        mydialge.setContentView(R.layout.showpassword)
                        mydialge.setTitle("Connect to Network")
                        val textSSID13 = mydialge.findViewById<ImageView>(R.id.myimage)
                        val resultant = mydialge.findViewById<Button>(R.id.resultant)
                        val preferences13 =
                            PreferenceManager.getDefaultSharedPreferences(this@WifiScanActivity)
                        val editor = preferences13.edit()
                        editor.putString("Name", checkPassword)
                        editor.apply()
                        resultant.setOnClickListener { v16: View? ->
                            val bitmapPath = MediaStore.Images.Media.insertImage(
                                contentResolver, bitmap, "title", null
                            )
                            val bitmapUri = Uri.parse(bitmapPath)
                            val intent = Intent(Intent.ACTION_SEND)
                            intent.type = "image/png"
                            intent.putExtra(Intent.EXTRA_STREAM, bitmapUri)
                            startActivity(Intent.createChooser(intent, "Share"))
                        }
                        val mysaveok = mydialge.findViewById<Button>(R.id.mysaveok)
                        mysaveok.setOnClickListener { v15: View? ->
                            if (isStoragePermissionsGranted) {
                                val bitmap = (textSSID13.drawable as BitmapDrawable).bitmap
                                Toast.makeText(
                                    this@WifiScanActivity,
                                    "" + dataBaseWifiHistory!!.ssid,
                                    Toast.LENGTH_SHORT
                                ).show()
                                saveImage(bitmap)
                                Toast.makeText(
                                    this@WifiScanActivity,
                                    getString(R.string.pass_saved),
                                    Toast.LENGTH_SHORT
                                ).show()
                                mydialge.dismiss()
                            } else {
                                ActivityCompat.requestPermissions(this, permissions, 242)
                                Toast.makeText(
                                    this,
                                    getString(R.string.allow_storage_permission),
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                        val manager = getSystemService(WINDOW_SERVICE) as WindowManager
                        val display = manager.defaultDisplay
                        val point = Point()
                        display.getSize(point)
                        val width = point.x
                        val height = point.y
                        var smallerDimension = Math.min(width, height)
                        smallerDimension = smallerDimension * 3 / 4
                        val qrgEncoder = QRGEncoder(
                            """
                          $wifiSSID
                          $checkPassword
                          """.trimIndent(), null,
                            QRGContents.Type.TEXT,
                            smallerDimension
                        )
                        try {
                            bitmap = qrgEncoder.getBitmap(0)
                            textSSID13.setImageBitmap(bitmap)
                        } catch (e: WriterException) {
                        }
                        mydialge.show()
                    } else {
                        Toast.makeText(
                            this@WifiScanActivity,
                            "Sorry Password is Wrong",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }, 4000)
            }
            dialog.dismiss()
        }
    }

    private fun connectToWifi(wifiSSID: String?) {
        connectWifiDialog = Dialog(this)
        connectWifiDialog!!.setContentView(R.layout.connectwifi)
        connectWifiDialog!!.setTitle("Connect to Network")
        pass = connectWifiDialog!!.findViewById(R.id.textPassword)
        val wifiConnected = isWifiConnected
        val preferences = PreferenceManager.getDefaultSharedPreferences(this)
        val name = preferences.getString("Name", "")
        if (wifiConnected) {
            wifiConnected(name, connectWifiDialog!!, wifiSSID)
        } else {
            wifiNotConnected(name, connectWifiDialog!!, wifiSSID)
        }
        connectWifiDialog!!.show()
    }

    private fun generateQRDialog(name: String?, wifiSSID: String?) {
        generateQRDialog = Dialog(this@WifiScanActivity)
        generateQRDialog!!.setContentView(R.layout.showpassword)
        generateQRDialog!!.setTitle("Connect to Network")
        val textSSID12 = generateQRDialog!!.findViewById<ImageView>(R.id.myimage)
        val resultant = generateQRDialog!!.findViewById<Button>(R.id.resultant)
        val preferences12 = PreferenceManager.getDefaultSharedPreferences(this@WifiScanActivity)
        val editor = preferences12.edit()
        editor.putString("Name", name)
        editor.apply()
        resultant.setOnClickListener { v14: View? ->
            val bitmapPath = MediaStore.Images.Media.insertImage(
                contentResolver, bitmap, "title", null
            )
            val bitmapUri = Uri.parse(bitmapPath)
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "image/png"
            intent.putExtra(Intent.EXTRA_STREAM, bitmapUri)
            startActivity(Intent.createChooser(intent, "Share"))
        }
        val mysaveok = generateQRDialog!!.findViewById<Button>(R.id.mysaveok)
        mysaveok.setOnClickListener { v13: View? ->
            if (isStoragePermissionsGranted) {
                val bitmap = (textSSID12.drawable as BitmapDrawable).bitmap
                Toast.makeText(
                    this@WifiScanActivity, "" + dataBaseWifiHistory!!.ssid, Toast.LENGTH_SHORT
                ).show()
                saveImage(bitmap)
                Toast.makeText(
                    this@WifiScanActivity, "Your Password has been saved!", Toast.LENGTH_SHORT
                ).show()
                generateQRDialog!!.dismiss()
            } else {
                ActivityCompat.requestPermissions(this, permissions, 242)
                Toast.makeText(this, "Allow Storage Permissions", Toast.LENGTH_SHORT).show()
            }
        }
        val manager = getSystemService(WINDOW_SERVICE) as WindowManager
        val display = manager.defaultDisplay
        val point = Point()
        display.getSize(point)
        val width = point.x
        val height = point.y
        var smallerDimension = Math.min(width, height)
        smallerDimension = smallerDimension * 3 / 4
        val qrgEncoder = QRGEncoder(
            """
                  $wifiSSID
                  $name
                  """.trimIndent(), null,
            QRGContents.Type.TEXT,
            smallerDimension
        )
        try {
            bitmap = qrgEncoder.getBitmap(0)
            textSSID12.setImageBitmap(bitmap)
        } catch (e: WriterException) {
            e.printStackTrace()
        }
        generateQRDialog!!.show()
    }

    val isStoragePermissionsGranted: Boolean
        get() = ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED

    private fun wifiNotConnected(name: String?, dialog: Dialog, wifiSSID: String?) {
        val textSSID = dialog.findViewById<TextView>(R.id.textSSID1)
        val btnGenerateQR = dialog.findViewById<Button>(R.id.generateqr)
        val dialogButton = dialog.findViewById<Button>(R.id.okButton)
        pass = dialog.findViewById(R.id.textPassword)
        if (!name.equals("", ignoreCase = true)) {
            pass?.setText(name)
            btnGenerateQR.visibility = View.VISIBLE
            dialogButton.text = "Connect"
            btnGenerateQR.setOnClickListener { v: View? -> generateQRDialog(name, wifiSSID) }
        }
        textSSID.text = wifiSSID
        val hide = dialog.findViewById<ImageView>(R.id.hide)
        hide.setOnClickListener { v: View? ->
            if (flag == 0) {
                hide.setImageResource(R.drawable.eye)
                pass?.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD)
                flag = 1
            } else if (flag == 1) {
                hide.setImageResource(R.drawable.eyeoff)
                pass?.setInputType(InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD)
                flag = 0
            }
        }
        dialogButton.setOnClickListener { v: View? ->
            val checkPassword = pass?.getText().toString()
            if (!TextUtils.isEmpty(checkPassword)) {
                finallyConnect(checkPassword, wifiSSID)
                sendBroadcast(
                    Intent(this@WifiScanActivity, WifiTestReciever::class.java).setAction(
                        "MyAction"
                    )
                )
                Handler().postDelayed({
                    checkWifiConnected = isWifiConnected
                    if (checkWifiConnected) {
                        Toast.makeText(
                            this@WifiScanActivity,
                            "Connected Successfully",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        Toast.makeText(this@WifiScanActivity, "Not connected", Toast.LENGTH_SHORT)
                            .show()
                    }
                }, 4000)
                val handler = Handler()
                handler.postDelayed({
                    if (checkWifiConnected) {
                        showPasswordDialog(checkPassword, wifiSSID)
                    } else {
                        Toast.makeText(
                            this@WifiScanActivity,
                            "Sorry Password is Wrong",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }, 4000)
            }
            dialog.dismiss()
        }
    }

    private fun showPasswordDialog(checkPassword: String, wifiSSID: String?) {
        showPasswordDialog = Dialog(this@WifiScanActivity)
        showPasswordDialog!!.setContentView(R.layout.showpassword)
        showPasswordDialog!!.setTitle("Connect to Network")
        val textSSID14 = showPasswordDialog!!.findViewById<ImageView>(R.id.myimage)
        val resultant = showPasswordDialog!!.findViewById<Button>(R.id.resultant)
        val preferences14 = PreferenceManager.getDefaultSharedPreferences(this@WifiScanActivity)
        val editor = preferences14.edit()
        editor.putString("Name", checkPassword)
        editor.apply()
        resultant.setOnClickListener { v18: View? ->
            val bitmapPath = MediaStore.Images.Media.insertImage(
                contentResolver, bitmap, "title", null
            )
            val bitmapUri = Uri.parse(bitmapPath)
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "image/png"
            intent.putExtra(Intent.EXTRA_STREAM, bitmapUri)
            startActivity(Intent.createChooser(intent, "Share"))
        }
        val mysaveok = showPasswordDialog!!.findViewById<Button>(R.id.mysaveok)
        mysaveok.setOnClickListener { v17: View? ->
            if (isStoragePermissionsGranted) {
                val bitmap = (textSSID14.drawable as BitmapDrawable).bitmap
                Toast.makeText(
                    this@WifiScanActivity,
                    "" + dataBaseWifiHistory!!.ssid,
                    Toast.LENGTH_SHORT
                ).show()
                saveImage(bitmap)
                Toast.makeText(
                    this@WifiScanActivity,
                    "Your Password has been saved!",
                    Toast.LENGTH_SHORT
                ).show()
                showPasswordDialog!!.dismiss()
            } else {
                ActivityCompat.requestPermissions(this, permissions, 242)
                Toast.makeText(this, "Allow Storage Permissions", Toast.LENGTH_SHORT).show()
            }
        }
        val manager = getSystemService(WINDOW_SERVICE) as WindowManager
        val display = manager.defaultDisplay
        val point = Point()
        display.getSize(point)
        val width = point.x
        val height = point.y
        var smallerDimension = Math.min(width, height)
        smallerDimension = smallerDimension * 3 / 4
        val qrgEncoder = QRGEncoder(
            """
                  $wifiSSID
                  $checkPassword
                  """.trimIndent(), null,
            QRGContents.Type.TEXT,
            smallerDimension
        )
        try {
            bitmap = qrgEncoder.getBitmap(0)
            textSSID14.setImageBitmap(bitmap)
        } catch (e: WriterException) {
            e.printStackTrace()
        }
        showPasswordDialog!!.show()
    }

    private fun finallyConnect(networkPass: String, networkSSID: String?) {
        if (databaseHelper!!.ssId == networkSSID) {
            Toast.makeText(this, "Network already added.", Toast.LENGTH_SHORT).show()
        } else {
            val wifiConfig = WifiConfiguration()
            wifiConfig.SSID = String.format("\"%s\"", networkSSID)
            wifiConfig.preSharedKey = String.format("\"%s\"", networkPass)
            val netId = wifiManager!!.addNetwork(wifiConfig)
            wifiManager!!.disconnect()
            wifiManager!!.enableNetwork(netId, true)
            val conf = WifiConfiguration()
            conf.SSID = "\"\"" + networkSSID + "\"\""
            conf.preSharedKey = "\"" + networkPass + "\""
            wifiManager!!.addNetwork(conf)
        }
    }

    val isWifiConnected: Boolean
        get() {
            val connManager = getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
            val mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
            return mWifi!!.isConnected
        }

    private fun saveImage(finalBitmap: Bitmap) {
        val myDir = File(AppUtils.DIR_PATH)
        if (!myDir.exists()) {
            val created = myDir.mkdirs()
        }
        val destPath = File(myDir.toString() + File.separator + ssid + ".png")
        try {
            finalBitmap.compress(Bitmap.CompressFormat.PNG, 100, FileOutputStream(destPath))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun queryWifiManager() {
        val wifiManager = applicationContext.getSystemService(WIFI_SERVICE) as WifiManager
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        val configuredList = wifiManager.configuredNetworks
        if (configuredList != null && configuredList.size > 0) {
            for (config in configuredList) {
                ssidList.add(config.SSID)
                //hashcode.add(config.hashCode());
                bssidList.add(config.BSSID)
                hiddenSsid.add(config.enterpriseConfig)
                wepKey.add(config.wepKeys)
                wepTxKeyIndexArray.add(config.wepTxKeyIndex)
                networkIDS.add(config.networkId)
                priority.add(config.priority)
                hashcode.add(config.hashCode())
            }
        }
    }

    private class ListViewAdapter(private val context: Context, var filtered: ArrayList<String>) :
        BaseAdapter() {
        override fun getCount(): Int {
            return filtered.size
        }

        override fun getItem(position: Int): Any {
            return filtered[position]
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View? {
            var convertView = convertView
            if (convertView == null) convertView =
                LayoutInflater.from(context).inflate(R.layout.listitem_wifi, null, false)
            val phn = convertView?.findViewById<TextView>(R.id.phn)
            phn?.text = filtered[position]
            return convertView
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
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
                                "Please allow both Camera and Storage Permission to continue",
                                Toast.LENGTH_SHORT
                            ).show()

                        } else {
                            explain("Please allow both Camera and Storage Permission to continue", 20)
                        }
                    }


                }catch (e:Exception){
                    e.printStackTrace()
                }



            }
        }
    }


    private fun explain(msg: String, code: Int) {
        val dialog = AlertDialog.Builder(this)
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

}