package wifi.key.show.master.show.all.wifi.password.Activities;

import android.content.pm.ApplicationInfo;
import android.net.TrafficStats;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;

import io.github.nikhilbhutani.analyzer.DataAnalyzer;
import wifi.key.show.master.show.all.wifi.password.Adapters.RecyclerViewAdapter;
import wifi.key.show.master.show.all.wifi.password.R;
import wifi.key.show.master.show.all.wifi.password.SharedPreference.MyPreference;

public class DataUsageActivity extends AppCompatActivity {
    DataAnalyzer dataAnalyzer;
    RecyclerView recyclerView;
    RecyclerViewAdapter recyclerViewAdapter;
    MyPreference preference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_usage);
        preference = new MyPreference(DataUsageActivity.this);

        GraphView graph = findViewById(R.id.graph);
        long a = TrafficStats.getMobileRxBytes();
        Log.e("mydata", String.valueOf(a));
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(new DataPoint[]{
                new DataPoint(1, 0),
                new DataPoint(2, 50),
                new DataPoint(3, 100)

        });
        graph.addSeries(series);


        String w = bytesIntoHumanReadable(a);
        TextView hellow = findViewById(R.id.hellow);
        hellow.setText(w);


        Toast.makeText(this, android.net.TrafficStats.getMobileRxBytes() + "Bytes", Toast.LENGTH_SHORT).show();


        dataAnalyzer = new DataAnalyzer(this);

        ArrayList<ApplicationInfo> arrayList=new ArrayList<>();
        try {
            arrayList = (ArrayList<ApplicationInfo>) dataAnalyzer.getApplicationMeta();
        } catch (Exception e) {
            e.printStackTrace();
        }

        recyclerView = findViewById(R.id.recyclerview);


        recyclerViewAdapter = new RecyclerViewAdapter(this, arrayList);
        recyclerView.hasFixedSize();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(recyclerViewAdapter);


    }


    private String bytesIntoHumanReadable(long bytes) {
        long kilobyte = 1024;
        long megabyte = kilobyte * 1024;
        long gigabyte = megabyte * 1024;
        long terabyte = gigabyte * 1024;

        if ((bytes >= 0) && (bytes < kilobyte)) {
            return bytes + " B";

        } else if ((bytes >= kilobyte) && (bytes < megabyte)) {
            return (bytes / kilobyte) + " KB";

        } else if ((bytes >= megabyte) && (bytes < gigabyte)) {
            return (bytes / megabyte) + " MB";

        } else if ((bytes >= gigabyte) && (bytes < terabyte)) {
            return (bytes / gigabyte) + " GB";

        } else if (bytes >= terabyte) {
            return (bytes / terabyte) + " TB";

        } else {
            return bytes + " Bytes";

        }
    }
}