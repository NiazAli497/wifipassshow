package wifi.key.show.master.show.all.wifi.password.Activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import wifi.key.show.master.show.all.wifi.password.DatabaseHelper.DatabaseHelper;
import wifi.key.show.master.show.all.wifi.password.R;
import wifi.key.show.master.show.all.wifi.password.databinding.ActivityOpenWifiScanBinding;
import wifi.key.show.master.show.all.wifi.password.databinding.OpenwifiListitemBinding;
import wifi.key.show.master.show.all.wifi.password.utils.Constants;

public class OpenWifiScanActivity extends AppCompatActivity {
    WifiManager wifiManager;
    DatabaseHelper databaseHelper;
    RecyclerView recyclerView;
    public List<ScanResult> networkList;

    ActivityOpenWifiScanBinding binding;

    private void createBannerAd(){
        if (Objects.requireNonNull(Constants.ADS_VALUES.get(Constants.PERSONAL_BANNER)).equalsIgnoreCase("true")){

            ImageView defaultImg = findViewById(R.id.personal_banner_ad_img_1);

            defaultImg.setVisibility(View.INVISIBLE);

            ImageView adImg = findViewById(R.id.personal_banner_ad_img_2);
            TextView adTitle = findViewById(R.id.personal_banner_ad_title);
            TextView adDesc = findViewById(R.id.personal_banner_ad_description);
            AppCompatButton adButton = findViewById(R.id.personal_banner_ad_button);

            adImg.setVisibility(View.VISIBLE);
            adTitle.setVisibility(View.VISIBLE);
            adDesc.setVisibility(View.VISIBLE);
            adButton.setVisibility(View.VISIBLE);

            adTitle.setSelected(true);
            adDesc.setSelected(true);

            Glide.with(OpenWifiScanActivity.this).load(Objects.requireNonNull(Constants.ADS_VALUES.get(Constants.AD_IMG))).into(adImg);
            adTitle.setText(Objects.requireNonNull(Constants.ADS_VALUES.get(Constants.AD_TITLE)));
            adDesc.setText(Objects.requireNonNull(Constants.ADS_VALUES.get(Constants.AD_DESCRIPTION)));
            adButton.setOnClickListener(view -> startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id="+Objects.requireNonNull(Constants.ADS_VALUES.get(Constants.AD_PACKAGE_NAME))))));

        }
    }

    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOpenWifiScanBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        createBannerAd();

        databaseHelper = new DatabaseHelper(this);

        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        networkList = wifiManager.getScanResults();

        recyclerView = binding.recyclerview;

        ArrayList<String> ssidList = new ArrayList<>();

        if (networkList != null && networkList.size() > 0) {
            binding.tvNoWifiAvailable.setVisibility(View.GONE);
            Log.e("TAG", "Networks list: " + networkList.toString());
            for (ScanResult result : networkList) {
                ssidList.add(result.SSID);
            }
            binding.recyclerview.setLayoutManager(new LinearLayoutManager(this));
            binding.recyclerview.setAdapter(new RecyclerAdapter(ssidList));
        }

    }


    public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder> {

        ArrayList<String> ssidList;

        public RecyclerAdapter(ArrayList<String> ssidList) {
            this.ssidList = ssidList;
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new MyViewHolder(OpenwifiListitemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            holder.ssid.setText(ssidList.get(position));
        }

        @Override
        public int getItemCount() {
            return ssidList.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            TextView ssid;

            public MyViewHolder(@NonNull OpenwifiListitemBinding binding) {
                super(binding.getRoot());
                ssid = binding.tvSSID;
                ssid.setSelected(true);
                itemView.setOnClickListener(view -> {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        Intent panelIntent = new Intent(Settings.Panel.ACTION_INTERNET_CONNECTIVITY);
                        panelIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(panelIntent);
                    } else {
                        String ssid = ssidList.get(getAdapterPosition());
                        connectToWifi(OpenWifiScanActivity.this, ssid);
                    }
                });
            }
        }
    }





    public static void connectToWifi(Context context, String networkSSID) {
        Toast.makeText(context, R.string.connect_free_wifi, Toast.LENGTH_SHORT).show();
        WifiConfiguration conf = new WifiConfiguration();
        conf.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
        WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        wifiManager.addNetwork(conf);
        @SuppressLint("MissingPermission") List<WifiConfiguration> list = wifiManager.getConfiguredNetworks();
        for (WifiConfiguration i : list) {
            if (i.SSID != null && i.SSID.equals("\"" + networkSSID + "\"")) {
                wifiManager.disconnect();
                wifiManager.enableNetwork(i.networkId, true);
                wifiManager.reconnect();
                break;
            } else {
            }
        }
        if (list.size() == 0) {
            Toast.makeText(context,  R.string.connection_not_available, Toast.LENGTH_SHORT).show();
        }
    }
}
