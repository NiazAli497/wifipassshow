package wifi.key.show.master.show.all.wifi.password.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import wifi.key.show.master.show.all.wifi.password.ActivityNew.HomeActivity;
import wifi.key.show.master.show.all.wifi.password.DatabaseHelper.DataBaseWifiHistory;
import wifi.key.show.master.show.all.wifi.password.databinding.ActivitySpeedTestHistoryBinding;
import wifi.key.show.master.show.all.wifi.password.databinding.WifiHistoryItemBinding;
import wifi.key.show.master.show.all.wifi.password.model.DisplayHistoryModel;

public class NewDisplayWifiHistoryActivity extends
        AppCompatActivity {

    DataBaseWifiHistory database;

    ActivitySpeedTestHistoryBinding binding;
    ArrayList<DisplayHistoryModel> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySpeedTestHistoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        database = new DataBaseWifiHistory(this);
        list = new ArrayList<>();

        ArrayList<String> record = database.getHistory();
        if (record != null && record.size() > 0) {
            for (String history : record) {
                addDataIntoList(history);
            }
            binding.recyclerview.setLayoutManager(new LinearLayoutManager(this));
            binding.recyclerview.setAdapter(new RecyclerAdapter(list,this));
        }
    }

    void addDataIntoList(String history) {
        String time="",name="",strength="",speed="",type="",signals="";
        String[] split = history.split("\n");
        if (split!=null && split.length>1){
            time=split[1];

            if (split.length>2){
                name=split[2];
            }

            if (split.length>3){
               strength=split[3];
            }

            if (split.length>4){
                speed=split[4];
            }

            if (split.length>5){
                type=split[5];
            }

            if (split.length>6){
                signals=split[6];
            }

            list.add(new DisplayHistoryModel(time,name,speed,strength,type,signals));
        }
    }

    public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder> {

        private ArrayList<DisplayHistoryModel> list;
        private Context context;

        public RecyclerAdapter(ArrayList<DisplayHistoryModel> list, Context context) {
            this.list = list;
            this.context = context;
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new MyViewHolder(WifiHistoryItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            DisplayHistoryModel model = list.get(position);
            holder.time.setText(model.getTime());
            holder.name.setText(model.getName());
            holder.strength.setText(model.getStrength());
            holder.speed.setText(model.getSpeed());
            holder.type.setText(model.getType());
            holder.signals.setText(model.getSignals());
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            TextView time, name, strength, speed, type, signals;

            public MyViewHolder(@NonNull WifiHistoryItemBinding binding) {
                super(binding.getRoot());
                time = binding.date;
                name = binding.name;
                strength = binding.type;
                speed = binding.strength;
                type = binding.speed;
                signals = binding.signals;

                time.setSelected(true);
                signals.setSelected(true);


            }
        }
    }
}
