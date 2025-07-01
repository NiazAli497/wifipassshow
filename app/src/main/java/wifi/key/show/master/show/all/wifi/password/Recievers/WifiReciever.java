package wifi.key.show.master.show.all.wifi.password.Recievers;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;


import wifi.key.show.master.show.all.wifi.password.R;


public class WifiReciever extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent)
    {

        String Title = intent.getStringExtra(context.getString(R.string.titttle));
//        Intent x = new Intent(context, WifiTurnOffShowAlert.class);
//        x.putExtra(context.getString(R.string.titttle), Title);
//        x.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        context.startActivity(x);
    }
}

