package wifi.key.show.master.show.all.wifi.password.Activities

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import me.dm7.barcodescanner.zxing.ZXingScannerView
import android.os.Bundle
import android.widget.Toast
import wifi.key.show.master.show.all.wifi.password.R
import android.graphics.drawable.ColorDrawable
import android.widget.LinearLayout
import android.widget.EditText
import android.content.Intent
import android.content.ClipData
import android.content.ClipboardManager
import android.graphics.Bitmap
import android.graphics.Color
import android.view.View
import android.view.Window
import android.widget.Button
import androidx.core.content.FileProvider
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.Result
import com.google.zxing.Writer
import com.google.zxing.oned.Code128Writer
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import wifi.key.show.master.show.all.wifi.password.databinding.QrresultBinding
import wifi.key.show.master.show.all.wifi.password.utils.AppUtils
import wifi.key.show.master.show.all.wifi.password.utils.Utils
import java.io.File
import java.util.*

class QRScannerActivity : AppCompatActivity(), ZXingScannerView.ResultHandler {
    private var mScannerView: ZXingScannerView? = null
    private var dialog: Dialog? = null
    public override fun onCreate(state: Bundle?) {
        super.onCreate(state)
        mScannerView = ZXingScannerView(this)
        setContentView(mScannerView)
    }

    public override fun onResume() {
        super.onResume()
        mScannerView?.setResultHandler(this)
        mScannerView?.startCamera()
    }

    public override fun onPause() {
        super.onPause()
        mScannerView?.stopCamera()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (dialog != null && dialog!!.isShowing) {
            dialog?.dismiss()
        }
    }

    override fun handleResult(rawResult: Result) {
        Toast.makeText(this, "" + rawResult.text, Toast.LENGTH_SHORT).show()
        dialog = Dialog(this)
        val binding: QrresultBinding = QrresultBinding.inflate(layoutInflater)
        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog?.setContentView(binding.root)
        dialog?.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window!!.setLayout(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        dialog?.setTitle(getString(R.string.genrated_pass))
        binding.result.setText(rawResult.text)
        binding.share.setOnClickListener { v: View? ->
            val shareBody = rawResult.text
            val sharingIntent = Intent(Intent.ACTION_SEND)
            sharingIntent.type = "text/plain"
            sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody)
            startActivity(
                Intent.createChooser(
                    sharingIntent,
                    resources.getString(R.string.share_using)
                )
            )
        }
        binding.copy.setOnClickListener { v: View? ->
            val clipboard = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("label", rawResult.text)
            clipboard.setPrimaryClip(clip)
            Toast.makeText(this@QRScannerActivity, getString(R.string.copy), Toast.LENGTH_SHORT).show()
        }
        binding.save.setOnClickListener{
            CoroutineScope(Dispatchers.IO).launch {
                generateBarcode(rawResult.text)
            }
        }

        dialog?.show()
    }

    private suspend fun generateBarcode(text: String) {
        try {
            val hintMap = Hashtable<EncodeHintType, ErrorCorrectionLevel?>()
            hintMap[EncodeHintType.ERROR_CORRECTION] = ErrorCorrectionLevel.L
            val codeWriter: Writer
            codeWriter = Code128Writer()
            val byteMatrix = codeWriter.encode(text, BarcodeFormat.CODE_128, 400, 200, hintMap)
            val width = byteMatrix.width
            val height = byteMatrix.height
            val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
            for (i in 0 until width) {
                for (j in 0 until height) {
                    bitmap.setPixel(i, j, if (byteMatrix[i, j]) Color.BLACK else Color.WHITE)
                }
            }

            withContext(Dispatchers.Main) {
                displayGeneratedBarCode(bitmap, System.currentTimeMillis().toString())
            }

        } catch (e: Exception) {
            e.printStackTrace()
            withContext(Dispatchers.Main) {
                Toast.makeText(
                    this@QRScannerActivity, getString(R.string.something_wrong),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }


    private fun displayGeneratedBarCode(bitmap: Bitmap, contactName: String) {

        var savePath: String? = null
        savePath= Utils.saveImage(bitmap,contactName)
        Toast.makeText(this, "Saved at location: " + savePath, Toast.LENGTH_LONG)
            .show()
        dialog?.dismiss()
        AppUtils.isSavedItem= true
        finish()
    }

}