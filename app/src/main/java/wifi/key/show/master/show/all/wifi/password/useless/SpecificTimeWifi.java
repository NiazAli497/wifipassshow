//package wifi.key.show.master.show.all.wifi.password.Activities;
//
//import android.app.AlarmManager;
//import android.app.Dialog;
//import android.app.PendingIntent;
//import android.app.ProgressDialog;
//import android.content.ContentValues;
//import android.content.Context;
//import android.content.Intent;
//import android.database.sqlite.SQLiteDatabase;
//import android.net.wifi.ScanResult;
//import android.net.wifi.WifiConfiguration;
//import android.net.wifi.WifiInfo;
//import android.net.wifi.WifiManager;
//import android.os.Bundle;
//import android.os.Handler;
//import android.text.TextUtils;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.BaseAdapter;
//import android.widget.Button;
//import android.widget.DatePicker;
//import android.widget.EditText;
//import android.widget.ListView;
//import android.widget.TextView;
//import android.widget.TimePicker;
//import android.widget.Toast;
//
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.cardview.widget.CardView;
//
//
//import wifi.key.show.master.show.all.wifi.password.R;
//import wifi.key.show.master.show.all.wifi.password.Recievers.WifiReciever;
//import wifi.key.show.master.show.all.wifi.password.SpecificWifiReminder.DatabaseHelper;
//
//import java.text.SimpleDateFormat;
//import java.util.Calendar;
//import java.util.Date;
//import java.util.List;
//
//public class SpecificTimeWifi extends AppCompatActivity {
////    ListView listView;
//
//    public static WifiManager wifiManager;
//
//    String wifis[];
//    public static EditText pass;
//    wifi.key.show.master.show.all.wifi.password.DatabaseHelper.DatabaseHelper databaseHelper;
//
//    SQLiteDatabase sqLiteDatabase;
//    DatabaseHelper mydb;
//    EditText Et_Title;
//
//    DatePicker pickerDate;
//    TimePicker pickerTime;
////    TextView txt_time;
////    TextView txt_date;
//    CardView Date, Time;
//    public static String ssid;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.specific_wifiset);
//        mydb = new DatabaseHelper(this);
//        sqLiteDatabase = mydb.getWritableDatabase();
//
//        Et_Title = findViewById(R.id.txttitle);
//
//        pickerDate = findViewById(R.id.datePicker);
//        pickerTime = findViewById(R.id.timePicker);
////        txt_time = findViewById(R.id.txtTime);
////        txt_date = findViewById(R.id.date);
//
//        Date = findViewById(R.id.pickdate);
//        Time = findViewById(R.id.picktime);
//        final CardView SaveAlarm = findViewById(R.id.savealarm);
//
//        Date.setOnClickListener(v -> {
//
//            pickerDate.setVisibility(View.VISIBLE);
//            pickerTime.setVisibility(View.INVISIBLE);
//            SaveAlarm.setVisibility(View.GONE);
//
//        });
//
////        listView = findViewById(R.id.list_view);
//
//        databaseHelper = new wifi.key.show.master.show.all.wifi.password.DatabaseHelper.DatabaseHelper(this);
//
//        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
//
//        List<ScanResult> wifiScanList = wifiManager.getScanResults();
//        wifis = new String[wifiScanList.size()];
//        for (int i = 0; i < wifiScanList.size(); i++) {
//            wifis[i] = ((wifiScanList.get(i)).toString());
//        }
//        String filtered[] = new String[wifiScanList.size()];
//        int counter = 0;
//        for (String eachWifi : wifis) {
//            String[] temp = eachWifi.split(",");
//
//            filtered[counter] = temp[0].substring(5).trim();
//            counter++;
//
//        }
//
////        listView.setAdapter(new LogAdapter1(getApplicationContext(), filtered));
////        listView.setOnItemClickListener((parent, view, position, id) -> {
////
////            ssid = (String) listView.getItemAtPosition(position);
////            connectToWifi(ssid);
////        });
//
//
//        Time.setOnClickListener(v -> {
//
//            pickerDate.setVisibility(View.INVISIBLE);
//            pickerTime.setVisibility(View.VISIBLE);
////            txt_date.setVisibility(View.INVISIBLE);
////            txt_time.setVisibility(View.VISIBLE);
//            SaveAlarm.setVisibility(View.VISIBLE);
//
//        });
//
//        SaveAlarm.setOnClickListener(v -> {
//
//            ContentValues cv = new ContentValues();
//
//            cv.put(mydb.TIME, getString(R.string.Not_Set_Alert));
//
//            Calendar calender = Calendar.getInstance();
//            calender.clear();
//            calender.set(Calendar.MONTH, pickerDate.getMonth());
//            calender.set(Calendar.DAY_OF_MONTH, pickerDate.getDayOfMonth());
//            calender.set(Calendar.YEAR, pickerDate.getYear());
//            calender.set(Calendar.HOUR, pickerTime.getCurrentHour());
//            calender.set(Calendar.MINUTE, pickerTime.getCurrentMinute());
//            calender.set(Calendar.SECOND, 00);
//
//            SimpleDateFormat formatter = new SimpleDateFormat(getString(R.string.hour_minutes));
//            String timeString = formatter.format(new Date(calender.getTimeInMillis()));
//            SimpleDateFormat dateformatter = new SimpleDateFormat(getString(R.string.dateformate));
//            String dateString = dateformatter.format(new Date(calender.getTimeInMillis()));
//
//            AlarmManager alarmMgr = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
//            Intent intent = new Intent(SpecificTimeWifi.this, WifiReciever.class);
//
//            PendingIntent pendingIntent;
//            pendingIntent = PendingIntent.getBroadcast(SpecificTimeWifi.this, 0, intent,  PendingIntent.FLAG_IMMUTABLE);
//
//            alarmMgr.set(AlarmManager.RTC_WAKEUP, calender.getTimeInMillis(), pendingIntent);
//            cv.put(DatabaseHelper.TIME, timeString);
//            cv.put(DatabaseHelper.TITLE, Et_Title.getText().toString());
//            cv.put(DatabaseHelper.DATE, dateString);
//
//            sqLiteDatabase.insert(DatabaseHelper.TABLE_NAME, null, cv);
//
//            Intent openMainScreen = new Intent(SpecificTimeWifi.this, WifiAutomaticActivity.class);
//            openMainScreen.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            startActivity(openMainScreen);
//            Toast.makeText(SpecificTimeWifi.this, "Alarm has Set", Toast.LENGTH_SHORT).show();
//
//        });
//
//    }
//
////    private void connectToWifi(final String wifiSSID) {
////        final Dialog dialog = new Dialog(this);
////        dialog.setContentView(R.layout.connectwifi);
////        dialog.setTitle("Connect to Network");
////        TextView textSSID = (TextView) dialog.findViewById(R.id.textSSID1);
////
////        Button dialogButton = (Button) dialog.findViewById(R.id.okButton);
////        pass = (EditText) dialog.findViewById(R.id.textPassword);
////        textSSID.setText(wifiSSID);
////
////        // if button is clicked, connect to the network;
////        dialogButton.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View v) {
////
////                String checkPassword = pass.getText().toString();
////                if (!TextUtils.isEmpty(checkPassword)) {
////                    finallyConnect(checkPassword, wifiSSID);
////                }
////                dialog.dismiss();
////            }
////        });
////        dialog.show();
////    }
//
////    private void finallyConnect(final String networkPass, final String networkSSID) {
////        if (databaseHelper.getSSId().equals(networkSSID)) {
////
////            Toast.makeText(this, "Network already added.", Toast.LENGTH_SHORT).show();
////
////        } else {
////            WifiConfiguration wifiConfig = new WifiConfiguration();
////
////            wifiConfig.SSID = String.format("\"%s\"", networkSSID);
////            wifiConfig.preSharedKey = String.format("\"%s\"", networkPass);
////
////            int netId = wifiManager.addNetwork(wifiConfig);
////            wifiManager.disconnect();
////            wifiManager.enableNetwork(netId, true);
////            wifiManager.reconnect();
////
////            /*
////             */
////
////            WifiConfiguration conf = new WifiConfiguration();
////            conf.SSID = "\"\"" + networkSSID + "\"\"";
////            conf.preSharedKey = "\"" + networkPass + "\"";
////            wifiManager.addNetwork(conf);
////
////            final ProgressDialog progressDialog = new ProgressDialog(SpecificTimeWifi.this);
////            progressDialog.setTitle("Authenticating");
////            progressDialog.show();
////
////            Handler handler = new Handler();
////            handler.postDelayed(new Runnable() {
////                @Override
////                public void run() {
////                    checkWifi(networkSSID, networkPass);
////
////                }
////            }, 120);
////            progressDialog.dismiss();
////            if (netId > 0) {
////
////
////            } else {
////
////            }
////
////        }
////    }
//
////    private void checkWifi(String networkSSID, String networkPass) {
////        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
////        if (String.valueOf(wifiInfo.getSupplicantState()).equals("COMPLETED")) {
////            if (wifiInfo.getSSID().equals(networkSSID)) {
////                Toast.makeText(this, "Connected to: " + networkSSID, Toast.LENGTH_SHORT).show();
////                databaseHelper.addData(networkSSID, networkPass);
////            }
////
////        } else {
////
////        }
////    }
//
////    private class LogAdapter1 extends BaseAdapter {
////        String[] filtered;
////        private Context context;
////
////        public LogAdapter1(Context context, String[] filtered) {
////            this.context = context;
////            this.filtered = filtered;
////
////        }
////
////        @Override
////        public int getCount() {
////            return filtered.length;
////        }
////
////        @Override
////        public Object getItem(int position) {
////            return filtered[position];
////        }
////
////        @Override
////        public long getItemId(int position) {
////            return position;
////        }
////
////        @Override
////        public View getView(int position, View convertView, ViewGroup parent) {
////            if (convertView == null)
////                convertView = LayoutInflater.from(context).inflate(R.layout.listitem_wifi, null, false);
////            TextView phn = (TextView) convertView.findViewById(R.id.phn);
////
////
////            phn.setText(filtered[position]);
////            return convertView;
////        }
////
////
////    }
//
//
//}
