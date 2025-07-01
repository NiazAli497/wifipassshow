package wifi.key.show.master.show.all.wifi.password.utils

import android.graphics.Bitmap
import android.os.Environment
import android.util.Log
import java.io.File
import java.io.FileOutputStream

object Utils {

    fun saveImage(finalBitmap: Bitmap, name:String): String? {
        val root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).absolutePath
        val myDir = File("$root/WifiAnalyzer")
        myDir.mkdirs()
        if (!myDir.exists()) {
            myDir.mkdirs()
        }

        val fileName = "${name}.png"
        val file = File(myDir, fileName)
        Log.i("path", "" + fileName)
        try {
            finalBitmap.compress(Bitmap.CompressFormat.PNG, 100, FileOutputStream(file))
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        return file.toString()
    }
}