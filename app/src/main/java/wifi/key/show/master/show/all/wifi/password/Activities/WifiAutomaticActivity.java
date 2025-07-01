package wifi.key.show.master.show.all.wifi.password.Activities;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.cardview.widget.CardView;

import com.suke.widget.SwitchButton;

import java.util.GregorianCalendar;

import wifi.key.show.master.show.all.wifi.password.ActivityNew.SetTimeActivity;
import wifi.key.show.master.show.all.wifi.password.MyPrefHelper;
import wifi.key.show.master.show.all.wifi.password.R;
import wifi.key.show.master.show.all.wifi.password.Recievers.AlarmRecieverFiveMintues;
import wifi.key.show.master.show.all.wifi.password.Recievers.AlarmRecieverThirtyMintues;
import wifi.key.show.master.show.all.wifi.password.SharedPreference.MyPreference;
import wifi.key.show.master.show.all.wifi.password.SharedPreference.SharedPreference;
import wifi.key.show.master.show.all.wifi.password.SpecificWifiReminder.DatabaseHelper;
import wifi.key.show.master.show.all.wifi.password.databinding.ActivityAutomaticwifiSettingNewBinding;
import wifi.key.show.master.show.all.wifi.password.utils.AppUtils;

public class WifiAutomaticActivity extends AppCompatActivity {
    SQLiteDatabase mydb;
    DatabaseHelper mDbHelper;
    SharedPreference sharedPreference;
    MyPreference MyPreference;
    private ActivityAutomaticwifiSettingNewBinding binding;
    MyPrefHelper prefHelper;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAutomaticwifiSettingNewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        MyPreference = new MyPreference(WifiAutomaticActivity.this);
        prefHelper= new MyPrefHelper(this);


        mDbHelper = new DatabaseHelper(this);
        mydb = mDbHelper.getWritableDatabase();

        String[] from = {DatabaseHelper.TITLE, DatabaseHelper.DETAIL, DatabaseHelper.TYPE, DatabaseHelper.TIME, DatabaseHelper.DATE};
        final String[] column = {DatabaseHelper.C_ID, DatabaseHelper.TITLE, DatabaseHelper.DETAIL, DatabaseHelper.TYPE, DatabaseHelper.TIME, DatabaseHelper.DATE};
        int[] to = {R.id.title, R.id.type, R.id.type, R.id.time, R.id.date};

        final Cursor cursor = mydb.query(DatabaseHelper.TABLE_NAME, column, null, null, null, null, null);
        final SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.item_auto_wifi_entry_new, cursor, from, to, 0);
        if (adapter.isEmpty()) {
            adapterIsEmpty();
        } else {
            adapterNotEmpty(adapter);
        }
    }



    void adapterNotEmpty(SimpleCursorAdapter adapter) {
        binding.mytext.setVisibility(View.GONE);
        sharedPreference = new SharedPreference(this);
        Button specific = findViewById(R.id.but_set_time);
        specific.setOnClickListener(v -> {
            startActivity(new Intent(WifiAutomaticActivity.this, SetTimeActivity.class));
        });

        int temp4 = 0;
        temp4 = sharedPreference.get("fivemins");
        if (temp4 == 2) {
            binding.switchTurnOffFiveMinutes.setChecked(true);
            binding.switchTurnOnThirtyMinutes.setEnabled(false);
            binding.switchTurnOnFiveMinutes.setEnabled(false);
            binding.switchTurnOffOneHour.setEnabled(false);

        } else {
            binding.switchTurnOffFiveMinutes.setChecked(false);


        }
        binding.switchTurnOffFiveMinutes.setOnCheckedChangeListener((view, isChecked) -> {
            if (binding.switchTurnOffFiveMinutes.isChecked()) {
                binding.switchTurnOffFiveMinutes.setChecked(true);
                binding.switchTurnOffOneHour.setEnabled(false);
                binding.switchTurnOnFiveMinutes.setEnabled(false);
                binding.switchTurnOnThirtyMinutes.setEnabled(false);

                long time = new GregorianCalendar().getTimeInMillis() + 300000;

                Intent intentAlarm = new Intent(WifiAutomaticActivity.this, AlarmRecieverFiveMintues.class);
                intentAlarm.setAction("aa");

                AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);


                alarmManager.set(AlarmManager.RTC_WAKEUP, time, PendingIntent.getBroadcast(
                        WifiAutomaticActivity.this, 1, intentAlarm, PendingIntent.FLAG_IMMUTABLE));

                sharedPreference.setint("fivemins", 2);

                WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);

                wifiManager.setWifiEnabled(true);

                Toast.makeText(WifiAutomaticActivity.this, getString(R.string.on), Toast.LENGTH_SHORT).show();


            } else {
                binding.switchTurnOffFiveMinutes.setChecked(false);
                binding.switchTurnOffOneHour.setEnabled(true);
                binding.switchTurnOnFiveMinutes.setEnabled(true);
                binding.switchTurnOnThirtyMinutes.setEnabled(true);
                // create the object
                AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

                Intent myIntent = new Intent(WifiAutomaticActivity.this, AlarmRecieverFiveMintues.class);
                PendingIntent pendingIntent;

                pendingIntent = PendingIntent.getActivity(WifiAutomaticActivity.this,
                        0, myIntent, PendingIntent.FLAG_IMMUTABLE);

                pendingIntent.cancel();
                alarmManager.cancel(pendingIntent);
                Toast.makeText(WifiAutomaticActivity.this, getString(R.string.off), Toast.LENGTH_SHORT).show();
                sharedPreference.setint("fivemins", 0);
            }
        });


        int temp1 = 0;
        temp1 = sharedPreference.get("thirtymintues");
        if (temp1 == 2) {
            binding.switchTurnOnThirtyMinutes.setChecked(true);
            binding.switchTurnOffFiveMinutes.setEnabled(false);
            binding.switchTurnOnFiveMinutes.setEnabled(false);
            binding.switchTurnOffOneHour.setEnabled(false);

        } else {
            binding.switchTurnOnThirtyMinutes.setChecked(false);


        }
        binding.switchTurnOnThirtyMinutes.setOnCheckedChangeListener((view, isChecked) -> {
            if (binding.switchTurnOnThirtyMinutes.isChecked()) {
                binding.switchTurnOnThirtyMinutes.setChecked(true);
                binding.switchTurnOffFiveMinutes.setEnabled(false);
                binding.switchTurnOnFiveMinutes.setEnabled(false);
                binding.switchTurnOffOneHour.setEnabled(false);
                long time = new GregorianCalendar().getTimeInMillis() + 1800000;

                Intent intentAlarm = new Intent(WifiAutomaticActivity.this, AlarmRecieverThirtyMintues.class);

                // create the object
                AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);


                alarmManager.set(AlarmManager.RTC_WAKEUP, time, PendingIntent.getBroadcast(
                        WifiAutomaticActivity.this, 1, intentAlarm, PendingIntent.FLAG_IMMUTABLE));


                sharedPreference.setint("thirtymintues", 2);

                WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);

                wifiManager.setWifiEnabled(true);

                Toast.makeText(WifiAutomaticActivity.this, getString(R.string.on), Toast.LENGTH_SHORT).show();


            } else {
                binding.switchTurnOnThirtyMinutes.setChecked(false);
                binding.switchTurnOffFiveMinutes.setEnabled(true);
                binding.switchTurnOnFiveMinutes.setEnabled(true);
                binding.switchTurnOffOneHour.setEnabled(true);
                // create the object
                AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);


                Intent myIntent = new Intent(WifiAutomaticActivity.this, AlarmRecieverThirtyMintues.class);
                PendingIntent pendingIntent;

                pendingIntent = PendingIntent.getActivity(WifiAutomaticActivity.this,
                        0, myIntent, PendingIntent.FLAG_IMMUTABLE);

                pendingIntent.cancel();
                alarmManager.cancel(pendingIntent);
                Toast.makeText(WifiAutomaticActivity.this, getString(R.string.off), Toast.LENGTH_SHORT).show();
                sharedPreference.setint("thirtymintues", 0);
            }


        });


        int temp2 = 0;
        temp2 = sharedPreference.get("fivemintues");
        if (temp2 == 2) {
            binding.switchTurnOnFiveMinutes.setChecked(true);
            binding.switchTurnOffFiveMinutes.setEnabled(false);
            binding.switchTurnOnThirtyMinutes.setEnabled(false);
            binding.switchTurnOffOneHour.setEnabled(false);

        } else {
            binding.switchTurnOnFiveMinutes.setChecked(false);


        }
        binding.switchTurnOnFiveMinutes.setOnCheckedChangeListener((view, isChecked) -> {
            if (binding.switchTurnOnFiveMinutes.isChecked()) {
                binding.switchTurnOnFiveMinutes.setChecked(true);
                binding.switchTurnOffFiveMinutes.setEnabled(false);
                binding.switchTurnOnThirtyMinutes.setEnabled(false);
                binding.switchTurnOffOneHour.setEnabled(false);
                Long time = new GregorianCalendar().getTimeInMillis() + 300000;

                Intent intentAlarm = new Intent(WifiAutomaticActivity.this, AlarmRecieverFiveMintues.class);
                intentAlarm.setAction("bb");
                // create the object
                AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);


                alarmManager.set(AlarmManager.RTC_WAKEUP, time, PendingIntent.getBroadcast(
                        WifiAutomaticActivity.this, 1, intentAlarm, PendingIntent.FLAG_IMMUTABLE));


                sharedPreference.setint("fivemintues", 2);

                WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);

                wifiManager.setWifiEnabled(true);

                Toast.makeText(WifiAutomaticActivity.this, getString(R.string.on), Toast.LENGTH_SHORT).show();


            } else {
                binding.switchTurnOnFiveMinutes.setChecked(false);
                binding.switchTurnOffFiveMinutes.setEnabled(true);
                binding.switchTurnOnThirtyMinutes.setEnabled(true);
                binding.switchTurnOffOneHour.setEnabled(true);
                // create the object
                AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);


                Intent myIntent = new Intent(WifiAutomaticActivity.this, AlarmRecieverFiveMintues.class);
                PendingIntent pendingIntent;
                pendingIntent = PendingIntent.getActivity(WifiAutomaticActivity.this,
                        0, myIntent, PendingIntent.FLAG_IMMUTABLE);

                pendingIntent.cancel();
                alarmManager.cancel(pendingIntent);
                Toast.makeText(WifiAutomaticActivity.this, getString(R.string.off), Toast.LENGTH_SHORT).show();
                sharedPreference.setint("fivemintues", 0);
            }


        });

        int temp3 = 0;
        temp3 = sharedPreference.get("onehour");
        if (temp3 == 2) {
            binding.switchTurnOffOneHour.setChecked(true);
            binding.switchTurnOffFiveMinutes.setEnabled(false);
            binding.switchTurnOnFiveMinutes.setEnabled(false);
            binding.switchTurnOnThirtyMinutes.setEnabled(false);

        } else {
            binding.switchTurnOffOneHour.setChecked(false);


        }
        binding.switchTurnOffOneHour.setOnCheckedChangeListener((view, isChecked) -> {
            if (binding.switchTurnOffOneHour.isChecked()) {
                binding.switchTurnOffOneHour.setChecked(true);
                binding.switchTurnOffFiveMinutes.setEnabled(false);
                binding.switchTurnOnFiveMinutes.setEnabled(false);
                binding.switchTurnOnThirtyMinutes.setEnabled(false);
                Long time = new GregorianCalendar().getTimeInMillis() + 3600000;

                Intent intentAlarm = new Intent(WifiAutomaticActivity.this, AlarmRecieverFiveMintues.class);
                intentAlarm.setAction("hh");
                // create the object
                AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);


                alarmManager.set(AlarmManager.RTC_WAKEUP, time, PendingIntent.getBroadcast(
                        WifiAutomaticActivity.this, 1, intentAlarm, PendingIntent.FLAG_IMMUTABLE));


                sharedPreference.setint("onehour", 2);

                WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);

                wifiManager.setWifiEnabled(true);

                Toast.makeText(WifiAutomaticActivity.this, getString(R.string.on), Toast.LENGTH_SHORT).show();


            } else {
                binding.switchTurnOffOneHour.setChecked(false);
                binding.switchTurnOffFiveMinutes.setEnabled(true);
                binding.switchTurnOnFiveMinutes.setEnabled(true);
                binding.switchTurnOnThirtyMinutes.setEnabled(true);
                // create the object
                AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                Intent myIntent = new Intent(WifiAutomaticActivity.this, AlarmRecieverFiveMintues.class);
                PendingIntent pendingIntent;

                pendingIntent = PendingIntent.getActivity(WifiAutomaticActivity.this,
                        0, myIntent, PendingIntent.FLAG_IMMUTABLE);
                pendingIntent.cancel();
                alarmManager.cancel(pendingIntent);
                Toast.makeText(WifiAutomaticActivity.this, getString(R.string.off), Toast.LENGTH_SHORT).show();
                sharedPreference.setint("onehour", 0);
            }
        });

        binding.commentlist.setAdapter(adapter);
    }

    void adapterIsEmpty() {

        binding.mytext.setVisibility(View.VISIBLE);
        sharedPreference = new SharedPreference(this);
        binding.butSetTime.setOnClickListener(v -> {
            startActivity(new Intent(WifiAutomaticActivity.this, SetTimeActivity.class));
        });

        int temp1 = 0;
        temp1 = sharedPreference.get("thirtymintues");
        if (temp1 == 2) {
            binding.switchTurnOnThirtyMinutes.setChecked(true);
            binding.switchTurnOffFiveMinutes.setEnabled(false);
            binding.switchTurnOnFiveMinutes.setEnabled(false);
            binding.switchTurnOffOneHour.setEnabled(false);

        } else {
            binding.switchTurnOnThirtyMinutes.setChecked(false);
        }
        binding.switchTurnOnThirtyMinutes.setOnCheckedChangeListener((view, isChecked) -> {
            if (binding.switchTurnOnThirtyMinutes.isChecked()) {
                binding.switchTurnOnThirtyMinutes.setChecked(true);
                binding.switchTurnOffFiveMinutes.setEnabled(false);
                binding.switchTurnOnFiveMinutes.setEnabled(false);
                binding.switchTurnOffOneHour.setEnabled(false);

                long time = new GregorianCalendar().getTimeInMillis() + 1800000;

                Intent intentAlarm = new Intent(WifiAutomaticActivity.this, AlarmRecieverThirtyMintues.class);

                // create the object
                AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

                //set the alarm for particular time
                alarmManager.set(AlarmManager.RTC_WAKEUP, time, PendingIntent.getBroadcast(
                        WifiAutomaticActivity.this, 1, intentAlarm, PendingIntent.FLAG_IMMUTABLE));

                sharedPreference.setint("thirtymintues", 2);

                WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);

                wifiManager.setWifiEnabled(true);

                Toast.makeText(WifiAutomaticActivity.this, getString(R.string.on), Toast.LENGTH_SHORT).show();


            } else {
                binding.switchTurnOnThirtyMinutes.setChecked(false);
                binding.switchTurnOffFiveMinutes.setEnabled(true);
                binding.switchTurnOnFiveMinutes.setEnabled(true);
                binding.switchTurnOffOneHour.setEnabled(true);
                // create the object
                AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);


                Intent myIntent = new Intent(WifiAutomaticActivity.this, AlarmRecieverThirtyMintues.class);
                PendingIntent pendingIntent;

                pendingIntent = PendingIntent.getActivity(WifiAutomaticActivity.this,
                        0, myIntent, PendingIntent.FLAG_IMMUTABLE);


                pendingIntent.cancel();
                alarmManager.cancel(pendingIntent);
                Toast.makeText(WifiAutomaticActivity.this, getString(R.string.off), Toast.LENGTH_SHORT).show();
                sharedPreference.setint("thirtymintues", 0);
            }


        });


        int temp2 = 0;
        temp2 = sharedPreference.get("fivemintues");
        if (temp2 == 2) {
            binding.switchTurnOnFiveMinutes.setChecked(true);
            binding.switchTurnOffFiveMinutes.setEnabled(false);
            binding.switchTurnOffOneHour.setEnabled(false);
            binding.switchTurnOnThirtyMinutes.setEnabled(false);

        } else {
            binding.switchTurnOnFiveMinutes.setChecked(false);
        }
        binding.switchTurnOnFiveMinutes.setOnCheckedChangeListener((view, isChecked) -> {
            if (binding.switchTurnOnFiveMinutes.isChecked()) {
                binding.switchTurnOnFiveMinutes.setChecked(true);
                binding.switchTurnOffFiveMinutes.setEnabled(false);
                binding.switchTurnOnThirtyMinutes.setEnabled(false);
                binding.switchTurnOffOneHour.setEnabled(false);
                Long time = new GregorianCalendar().getTimeInMillis() + 300000;

                Intent intentAlarm = new Intent(WifiAutomaticActivity.this, AlarmRecieverFiveMintues.class);
                intentAlarm.setAction("bb");
                // create the object
                AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);


                alarmManager.set(AlarmManager.RTC_WAKEUP, time, PendingIntent.getBroadcast(
                        WifiAutomaticActivity.this, 1, intentAlarm, PendingIntent.FLAG_IMMUTABLE));


                sharedPreference.setint("fivemintues", 2);

//                        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
//
//                        wifiManager.setWifiEnabled(true);

                Toast.makeText(WifiAutomaticActivity.this, getString(R.string.on), Toast.LENGTH_SHORT).show();


            } else {
                binding.switchTurnOnFiveMinutes.setChecked(false);
                binding.switchTurnOffFiveMinutes.setEnabled(true);
                binding.switchTurnOnThirtyMinutes.setEnabled(true);
                binding.switchTurnOffOneHour.setEnabled(true);
                // create the object
                AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);


                Intent myIntent = new Intent(WifiAutomaticActivity.this, AlarmRecieverFiveMintues.class);
                PendingIntent pendingIntent;

                pendingIntent = PendingIntent.getActivity(WifiAutomaticActivity.this,
                        0, myIntent, PendingIntent.FLAG_IMMUTABLE);

                pendingIntent.cancel();
                alarmManager.cancel(pendingIntent);
                Toast.makeText(WifiAutomaticActivity.this, getString(R.string.off), Toast.LENGTH_SHORT).show();
                sharedPreference.setint("fivemintues", 0);

            }


        });

        int temp3 = 0;
        temp3 = sharedPreference.get("onehour");
        if (temp3 == 2) {
            binding.switchTurnOffOneHour.setChecked(true);
            binding.switchTurnOffFiveMinutes.setEnabled(false);
            binding.switchTurnOnFiveMinutes.setEnabled(false);
            binding.switchTurnOnThirtyMinutes.setEnabled(false);
        } else {
            binding.switchTurnOffOneHour.setChecked(false);
        }
        binding.switchTurnOffOneHour.setOnCheckedChangeListener((view, isChecked) -> {
            if (binding.switchTurnOffOneHour.isChecked()) {
                binding.switchTurnOffOneHour.setChecked(true);

                binding.switchTurnOffFiveMinutes.setEnabled(false);
                binding.switchTurnOnFiveMinutes.setEnabled(false);
                binding.switchTurnOnThirtyMinutes.setEnabled(false);
                Long time = new GregorianCalendar().getTimeInMillis() + 3600000;

                Intent intentAlarm = new Intent(WifiAutomaticActivity.this, AlarmRecieverFiveMintues.class);
                intentAlarm.setAction("hh");

                AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

                alarmManager.set(AlarmManager.RTC_WAKEUP, time, PendingIntent.getBroadcast(WifiAutomaticActivity.this,
                        1, intentAlarm, PendingIntent.FLAG_IMMUTABLE));


                sharedPreference.setint("onehour", 2);

                WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);

                wifiManager.setWifiEnabled(true);

                Toast.makeText(WifiAutomaticActivity.this, getString(R.string.on), Toast.LENGTH_SHORT).show();


            } else {
                binding.switchTurnOffOneHour.setChecked(false);
                binding.switchTurnOffFiveMinutes.setEnabled(true);
                binding.switchTurnOnFiveMinutes.setEnabled(true);
                binding.switchTurnOnThirtyMinutes.setEnabled(true);
                // create the object
                AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);


                Intent myIntent = new Intent(WifiAutomaticActivity.this, AlarmRecieverFiveMintues.class);
                PendingIntent pendingIntent;
                pendingIntent = PendingIntent.getActivity(WifiAutomaticActivity.this,
                        0, myIntent, PendingIntent.FLAG_IMMUTABLE);

                pendingIntent.cancel();
                alarmManager.cancel(pendingIntent);
                sharedPreference.setint("onehour", 0);
                Toast.makeText(WifiAutomaticActivity.this, getString(R.string.off), Toast.LENGTH_SHORT).show();
                sharedPreference.setint("onehour", 0);
            }


        });

        int temp4 = 0;
        temp4 = sharedPreference.get("fivemins");
        if (temp4 == 2) {
            binding.switchTurnOffFiveMinutes.setChecked(true);
            binding.switchTurnOffOneHour.setEnabled(false);
            binding.switchTurnOnFiveMinutes.setEnabled(false);
            binding.switchTurnOnThirtyMinutes.setEnabled(false);

        } else {
            binding.switchTurnOffFiveMinutes.setChecked(false);


        }
        binding.switchTurnOffFiveMinutes.setOnCheckedChangeListener((view, isChecked) -> {
            if (binding.switchTurnOffFiveMinutes.isChecked()) {
                binding.switchTurnOffFiveMinutes.setChecked(true);
                binding.switchTurnOffOneHour.setEnabled(false);
                binding.switchTurnOnFiveMinutes.setEnabled(false);
                binding.switchTurnOnThirtyMinutes.setEnabled(false);
                Long time = new GregorianCalendar().getTimeInMillis() + 300000;

                Intent intentAlarm = new Intent(WifiAutomaticActivity.this, AlarmRecieverFiveMintues.class);
                intentAlarm.setAction("aa");

                AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

                alarmManager.set(AlarmManager.RTC_WAKEUP, time, PendingIntent.getBroadcast(
                        WifiAutomaticActivity.this, 1, intentAlarm, PendingIntent.FLAG_IMMUTABLE));

                sharedPreference.setint("fivemins", 2);

                WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);

                wifiManager.setWifiEnabled(true);

                Toast.makeText(WifiAutomaticActivity.this, getString(R.string.on), Toast.LENGTH_SHORT).show();


            } else {
                binding.switchTurnOffFiveMinutes.setChecked(false);
                binding.switchTurnOffOneHour.setEnabled(true);
                binding.switchTurnOnFiveMinutes.setEnabled(true);
                binding.switchTurnOnThirtyMinutes.setEnabled(true);
                // create the object
                AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);


                Intent myIntent = new Intent(WifiAutomaticActivity.this, AlarmRecieverFiveMintues.class);
                PendingIntent pendingIntent;

                pendingIntent = PendingIntent.getActivity(WifiAutomaticActivity.this,
                        0, myIntent, PendingIntent.FLAG_IMMUTABLE);


                pendingIntent.cancel();
                alarmManager.cancel(pendingIntent);
                Toast.makeText(WifiAutomaticActivity.this, getString(R.string.off), Toast.LENGTH_SHORT).show();
                sharedPreference.setint("fivemins", 0);
            }


        });
    }

}
