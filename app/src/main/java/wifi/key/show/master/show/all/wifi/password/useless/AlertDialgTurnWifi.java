//package wifi.key.show.master.show.all.wifi.password.Activities;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.provider.Settings;
//import android.view.View;
//
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.cardview.widget.CardView;
//
//import com.airbnb.lottie.LottieAnimationView;
//
//import wifi.key.show.master.show.all.wifi.password.R;
//
//
//public class AlertDialgTurnWifi extends AppCompatActivity {
//
//
//    @Override
//
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.myturnonwifi);
//        LottieAnimationView animationView = (LottieAnimationView) findViewById(R.id.animation_view);
//        animationView.setAnimation("turonwifi.json");
//        animationView.loop(true);
//        animationView.playAnimation();
//
//        CardView i = findViewById(R.id.turnon);
//        i.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
//                finish();
//
//            }
//        });
//
//    }
//
//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//        Intent i = new Intent(AlertDialgTurnWifi.this, MainActivity.class);
//        startActivity(i);
//    }
//
//    @Override
//
//    public void onDestroy() {
//
//        super.onDestroy();
//
//    }
//
//}