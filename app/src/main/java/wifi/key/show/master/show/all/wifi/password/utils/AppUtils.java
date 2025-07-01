package wifi.key.show.master.show.all.wifi.password.utils;

import android.content.Context;
import android.media.MediaScannerConnection;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import java.io.File;

public class AppUtils {
    public static long admobAdShowingDelay=3000L;
    public static boolean isSavedItem= false;
    public static long lastInterstitialTime=0L;
   public static boolean advertisementsDisabled=false;
    public static String DIR_PATH= Environment.getExternalStoragePublicDirectory(
            Environment.DIRECTORY_PICTURES).toString() + File.separator+"WifiAnalyzer";


    public static void notifyMediaScannerItemRemoved(File filePath, Context context) {
        MediaScannerConnection.scanFile(context, new String[]{filePath.getAbsolutePath()}, null,
                new MediaScannerConnection.MediaScannerConnectionClient() {
                    @Override
                    public void onMediaScannerConnected() {
                        Log.e("TAG", "Scanner Connected");
                    }

                    @Override
                    public void onScanCompleted(String s, Uri uri) {
                        if (filePath.exists()) {
                            context.getContentResolver().delete(uri, null, null);
                            Log.e("TAG", "File Deleted");
                        }
                    }
                });
    }


    public static boolean isInternetConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetwork() != null && cm.getNetworkCapabilities(cm.getActiveNetwork()) != null;
    }
}
