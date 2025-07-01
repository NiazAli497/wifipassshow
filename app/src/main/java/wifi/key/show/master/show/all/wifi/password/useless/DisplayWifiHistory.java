//package wifi.key.show.master.show.all.wifi.password.Activities;
//
//import android.content.Context;
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.AdapterView;
//import android.widget.BaseAdapter;
//import android.widget.ListView;
//import android.widget.TextView;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//import wifi.key.show.master.show.all.wifi.password.DatabaseHelper.DataBaseWifiHistory;
//import wifi.key.show.master.show.all.wifi.password.R;
//
//
//import java.util.ArrayList;
//
//public class DisplayWifiHistory extends AppCompatActivity implements AdapterView.OnItemClickListener {
//
//    DataBaseWifiHistory db;
//    ListView listView;
//    private LogAdapter adapter;
//    TextView time, name, strength, speed, type, signals;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_speed_test_history);
//
//        db = new DataBaseWifiHistory(this);
//
//        listView = (ListView) findViewById(R.id.History_listview);
//        adapter = new LogAdapter(this);
//        listView.setAdapter(adapter);
//
//        listView.setOnItemClickListener(this);
//
//
//    }
//
//    @Override
//    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//    }
//
//
//    private class LogAdapter extends BaseAdapter {
//        private Context context;
//        private ArrayList<String> messages;
//
//        public LogAdapter(Context context) {
//            this.context = context;
//            db = new DataBaseWifiHistory(context);
//            messages = db.getHistory();
//            db.close();
//        }
//
//        @Override
//        public int getCount() {
//            return messages.size();
//        }
//
//        @Override
//        public Object getItem(int position) {
//            return messages.get(position);
//        }
//
//        @Override
//        public long getItemId(int position) {
//            return position;
//        }
//
//        @Override
//        public View getView(int position, View convertView, ViewGroup parent) {
//            if (convertView == null)
//                convertView = LayoutInflater.from(context).inflate(R.layout.wifi_history_item, null, false);
//            time = convertView.findViewById(R.id.date);
//            name = convertView.findViewById(R.id.name);
//            strength = convertView.findViewById(R.id.type);
//            speed = convertView.findViewById(R.id.strength);
//            type = convertView.findViewById(R.id.speed);
//            signals = convertView.findViewById(R.id.signals);
//            final String[] split = messages.get(position).split("\n");
//            time.setText("" + split[1]);
//            name.setText("" + split[2]);
//            strength.setText("" + split[3]);
//            speed.setText("" + split[4]);
//            type.setText("" + split[5]);
//            if (split.length>6) {
//                signals.setText("" + split[6]);
//            }
//            return convertView;
//        }
//
//
//    }
//
//    @Override
//    public void onBackPressed() {
//        Intent i = new Intent(this, MainActivity.class);
//        startActivity(i);
//    }
//}
