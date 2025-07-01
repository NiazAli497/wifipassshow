package wifi.key.show.master.show.all.wifi.password.Adapters;

import android.net.wifi.WifiEnterpriseConfig;
import android.text.format.Formatter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;

import wifi.key.show.master.show.all.wifi.password.R;

public class DeviceAdapter extends RecyclerView.Adapter<DeviceAdapter.MyViewHolder> {
    private ArrayList<String> ssidListA;
    private ArrayList<Integer> hashcodeA;
    private ArrayList<String> bssidListA;
    private ArrayList<Integer> wepTxKeyIndexArrayA;
    private ArrayList<Integer> networkidsA;
    private ArrayList<Integer> priorityA;
    private ArrayList<WifiEnterpriseConfig> hiddenSsidA;
    private ArrayList<String[]> wepKeyA;
    Formatter f = new Formatter();
    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder


    // Provide a suitable constructor (depends on the kind of dataset)
    public DeviceAdapter(
            ArrayList<String> ssidListC,
            ArrayList<Integer> hashcodeC,
            ArrayList<String> bssidListC,
            ArrayList<Integer> wepTxKeyIndexArrayC,
            ArrayList<Integer> networkidsC,
            ArrayList<Integer> priorityC,
            ArrayList<WifiEnterpriseConfig> hiddenSsidC,

            ArrayList<String[]> wepKeyC) {
        ssidListA = ssidListC;
        bssidListA = bssidListC;
        wepTxKeyIndexArrayA = wepTxKeyIndexArrayC;
        networkidsA = networkidsC;
        priorityA = priorityC;
        hiddenSsidA = hiddenSsidC;

        wepKeyA = wepKeyC;
    }

    // Create new views (invoked by the layout manager)
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView ssidListT, hashcodeT, bssidListT, wepTxKeyIndexArrayT, networkidsT, priorityT, hiddenSsidT, wepKeyT;

        public MyViewHolder(View view) {
            super(view);
            ssidListT = (TextView) view.findViewById(R.id.textview_ssidListA);

            networkidsT = (TextView) view.findViewById(R.id.textview_networkidsA);
            priorityT = (TextView) view.findViewById(R.id.textview_priorityA);
            // hiddenSsidT = (TextView) view.findViewById(R.id.textview_hiddenSsidA);
            //  HomeProviderT = (TextView) view.findViewById(R.id.textview_HomeProviderA);
            // wepKeyT = (TextView) view.findViewById(R.id.textview_wepKeyA);

        }
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.saved_device_item, parent, false);

        return new MyViewHolder(itemView);
    }


    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.ssidListT.setText(ssidListA.get(position));
        //  holder.hashcodeT.setText(hashcodeA.get(position).toString());
        //holder.bssidListT.setText("BSSID "+bssidListA.get(position));
        //  holder.wepTxKeyIndexArrayT.setText("WepTxKey "+wepTxKeyIndexArrayA.get(position).toString());
        holder.networkidsT.setText("Network Id " + networkidsA.get(position).toString());
        holder.priorityT.setText("Priority " + priorityA.get(position).toString());
        //holder.hiddenSsidT.setText("HiddenSSID "+hiddenSsidA.get(position));
        //holder.HomeProviderT.setText(HomeProviderA.get(position).toString());
        String arrayvalues = "";
        //  int a=wepKeyA.get(position).length;
        String[] temp = wepKeyA.get(position);
        for (int i = 0; i < temp.length; i++) {

            arrayvalues = arrayvalues + "" + temp[i];

        }

        Log.e("arrayvalue", arrayvalues);
        // holder.wepKeyT.setText("WepKey "+ arrayvalues);

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return ssidListA.size();
    }
}
