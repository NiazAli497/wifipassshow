package wifi.key.show.master.show.all.wifi.password.Activities;

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.tasks.Task;

import wifi.key.show.master.show.all.wifi.password.MyPrefHelper;
import wifi.key.show.master.show.all.wifi.password.R;

public class HotSpotShareActivity extends AppCompatActivity {
    private WifiManager.LocalOnlyHotspotReservation mReservation;
    private boolean isHotspotEnabled = false;
    private final int REQUEST_ENABLE_LOCATION_SYSTEM_SETTINGS = 101;
    private String TAG = "hellow";
    MyPrefHelper prefHelper;
    private boolean isLocationPermissionEnable() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 2);
            return false;
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotspot_new);
        setClickListeners();
        prefHelper= new MyPrefHelper(this);
    }

    void setClickListeners(){
        ConstraintLayout share = findViewById(R.id.share);
//        share.setVisibility(View.VISIBLE);

        findViewById(R.id.btnHotSpotSettings).setOnClickListener(v -> openHotSpot());
//        share.setOnClickListener(v -> {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                try {
//                    enableLocationSettings();
//                }catch (Exception e){
//                    e.printStackTrace();
//                }
//            }
//        });
    }


    void openHotSpot(){
        try {
            final Intent intent = new Intent(Intent.ACTION_MAIN, null);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            intent.setComponent(new ComponentName("com.android.settings", "com.android.settings.TetherSettings"));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
        catch (ActivityNotFoundException exception){
            exception.printStackTrace();
            Toast.makeText(this, R.string.unable_to_open_hotspot, Toast.LENGTH_SHORT).show();
        }
    }

//    @RequiresApi(api = Build.VERSION_CODES.O)
//    private void turnOffHotspot() {
//        if (!isLocationPermissionEnable()) {
//            return;
//        }
//        if (mReservation != null) {
//            mReservation.close();
//            isHotspotEnabled = false;
//        }
//    }

//    @RequiresApi(api = Build.VERSION_CODES.O)
//    private void toggleHotspot() {
//        if (!isHotspotEnabled) {
//            try {
//                turnOnHotspot();
//            } catch (Exception ee) {
//                ee.printStackTrace();
//            }
//            Toast.makeText(this, getString(R.string.hotspot_on), Toast.LENGTH_SHORT).show();
//        } else {
//            try {
//                turnOffHotspot();
//            } catch (Exception ee) {
//                ee.printStackTrace();
//            }
//            Toast.makeText(this, getString(R.string.hot_spot_off), Toast.LENGTH_SHORT).show();
//
//        }
//    }
//
//    @RequiresApi(api = Build.VERSION_CODES.O)
//    private void turnOnHotspot() {
//        if (!isLocationPermissionEnable()) {
//            return;
//        }
//        WifiManager manager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
//
//        if (manager != null) {
//            // Don't start when it started (existed)
//            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                return;
//            }
//
//            manager.startLocalOnlyHotspot(new WifiManager.LocalOnlyHotspotCallback() {
//                @Override
//                public void onStarted(WifiManager.LocalOnlyHotspotReservation reservation) {
//                    super.onStarted(reservation);
//                    //Log.d(TAG, "Wifi Hotspot is on now");
//                    mReservation = reservation;
//                    isHotspotEnabled = true;
//                }
//
//                @Override
//                public void onStopped() {
//                    super.onStopped();
//                    //Log.d(TAG, "onStopped: ");
//                    isHotspotEnabled = false;
//                }
//
//                @Override
//                public void onFailed(int reason) {
//                    super.onFailed(reason);
//                    //Log.d(TAG, "onFailed: ");
//                    isHotspotEnabled = false;
//                }
//            }, new Handler());
//        }
//    }
//
//    @RequiresApi(api = Build.VERSION_CODES.O)
//    private void enableLocationSettings() {
//        LocationRequest mLocationRequest = new LocationRequest();
//        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
//        builder.addLocationRequest(mLocationRequest)
//                .setAlwaysShow(false); // Show dialog
//
//        Task<LocationSettingsResponse> task = LocationServices.getSettingsClient(this).checkLocationSettings(builder.build());
//
//        task.addOnCompleteListener(task1 -> {
//            try {
//                LocationSettingsResponse response = task1.getResult(ApiException.class);
//                toggleHotspot();
//            } catch (ApiException exception) {
//                switch (exception.getStatusCode()) {
//                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
//                        // Location settings are not satisfied. But could be fixed by showing the
//                        // user a dialog.
//                        try {
//                            // Cast to a resolvable exception.
//                            ResolvableApiException resolvable = (ResolvableApiException) exception;
//                            // Show the dialog by calling startResolutionForResult(),
//                            // and check the result in onActivityResult().
//                            resolvable.startResolutionForResult(HotSpotShareActivity.this,
//                                    REQUEST_ENABLE_LOCATION_SYSTEM_SETTINGS);
//                        } catch (IntentSender.SendIntentException e) {
//                            // Ignore the error.
//                        } catch (ClassCastException e) {
//                            // Ignore, should be an impossible error.
//                        }
//                        break;
//                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
//                        // Location settings are not satisfied. However, we have no way to fix the
//                        // settings so we won't show the dialog.
//                        break;
//                }
//            }
//        });
//    }
//
//    @RequiresApi(api = Build.VERSION_CODES.O)
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        final LocationSettingsStates states = LocationSettingsStates.fromIntent(data);
//        if (requestCode == REQUEST_ENABLE_LOCATION_SYSTEM_SETTINGS) {
//            switch (resultCode) {
//                case Activity.RESULT_OK:
//                    // All required changes were successfully made
//                    toggleHotspot();
//                    Toast.makeText(HotSpotShareActivity.this, states.isLocationPresent() + "", Toast.LENGTH_SHORT).show();
//                    break;
//                case Activity.RESULT_CANCELED:
//                    // The user was asked to change settings, but chose not to
//                    Toast.makeText(HotSpotShareActivity.this, "Canceled", Toast.LENGTH_SHORT).show();
//                    break;
//                default:
//                    break;
//            }
//        }
//    }

}
