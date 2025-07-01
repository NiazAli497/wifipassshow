package wifi.key.show.master.show.all.wifi.password.Adapters;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;



import java.util.ArrayList;

import io.github.nikhilbhutani.analyzer.DataAnalyzer;
import wifi.key.show.master.show.all.wifi.password.R;


public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private final ArrayList<ApplicationInfo> myDataList;
    private final DataAnalyzer dataAnalyzer;

    public RecyclerViewAdapter(Context context, ArrayList<ApplicationInfo> myDataList) {
        dataAnalyzer = new DataAnalyzer(context);
        this.myDataList = myDataList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.icon.setImageDrawable(dataAnalyzer.getAppIcon(myDataList.get(position)));
        holder.appname.setText(dataAnalyzer.getAppName(myDataList.get(position)));
        holder.dataReceived.setText("Data Received :" + dataAnalyzer.getReceivedData(myDataList.get(position)));
        holder.dataTransmitted.setText("Data Transmitted :" + dataAnalyzer.getDataTransmitted(myDataList.get(position)));
        holder.packetsReceived.setText("Packets Received :" + dataAnalyzer.getPacketsReceived(myDataList.get(position)));
        holder.packetsTransmitted.setText("Packets Transmitted :" + dataAnalyzer.getPacketsTransmitted(myDataList.get(position)));
    }

    @Override
    public int getItemCount() {
        return myDataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        final ImageView icon;
        final TextView appname;
        final TextView dataReceived;
        final TextView dataTransmitted;
        final TextView packetsReceived;
        final TextView packetsTransmitted;

        public ViewHolder(View itemView) {

            super(itemView);
            icon = itemView.findViewById(R.id.app_icon);
            appname = itemView.findViewById(R.id.app_name);
            dataReceived = itemView.findViewById(R.id.data_received);
            dataTransmitted = itemView.findViewById(R.id.data_transmitted);
            packetsReceived = itemView.findViewById(R.id.packets_received);
            packetsTransmitted = itemView.findViewById(R.id.packets_transmitted);
        }
    }
}
