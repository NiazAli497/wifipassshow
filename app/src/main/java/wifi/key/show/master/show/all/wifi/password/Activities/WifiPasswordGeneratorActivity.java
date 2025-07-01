package wifi.key.show.master.show.all.wifi.password.Activities;

import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;


import com.cleversolutions.ads.MediationManager;

import java.util.ArrayList;
import java.util.Objects;

import wifi.key.show.master.show.all.wifi.password.Ads.AdManager;
import wifi.key.show.master.show.all.wifi.password.Ads.ApplicationClass;
import wifi.key.show.master.show.all.wifi.password.MyPrefHelper;
import wifi.key.show.master.show.all.wifi.password.R;
import wifi.key.show.master.show.all.wifi.password.SharedPreference.MyPreference;
import wifi.key.show.master.show.all.wifi.password.databinding.ActivityGeneratePasswordNewBinding;
import wifi.key.show.master.show.all.wifi.password.utils.Constants;


public class WifiPasswordGeneratorActivity extends AppCompatActivity {
//    TextView t1;
//    CardView btnGenerate;
    private Dialog mDialog;
    ClipboardManager myClipboard;
    ClipData myClip;
//    private FrameLayout frameLayout;
    MyPreference MyPreference;
    String selectedList;
    private static final String ALPHA_NUMERIC_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final String ALPHABETS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String NUMERIC_STRING = "0123456789";

    ActivityGeneratePasswordNewBinding binding;
    MyPrefHelper prefHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= ActivityGeneratePasswordNewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSpinner();
        prefHelper= new MyPrefHelper(this);

        MyPreference = new MyPreference(this);
//        frameLayout = findViewById(R.id.adContainer);

        if (!MyPreference.getBoolean()) {
            setTitleColor(13220);
        }

        myClipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);

        createBanner();

        setClickListeners();
    }
    private void createBanner() {
        if (Objects.requireNonNull(Constants.ADS_VALUES.get(Constants.PASS_BANNER)).equalsIgnoreCase("true")){
            MediationManager manager = ApplicationClass.adManager;
            AdManager.getAdManager().createBanner(WifiPasswordGeneratorActivity.this, manager,binding.bannerGen);
        }
    }

    void setSpinner(){
        Spinner spinner = findViewById(R.id.spinner);
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("ABC");
        arrayList.add("abc#");
        arrayList.add("123");

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.spinner_style, arrayList);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedList = parent.getItemAtPosition(position).toString();
                binding.selectPass.setText(selectedList);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }


    public void setClickListeners(){

       binding.butGenPass.setOnClickListener(view -> {
            switch (selectedList) {
                case "ABC":
                    try {
                        binding.generatePass.setText(randomAlphabets(6));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case "abc#":
                    try {
                        binding.generatePass.setText(randomAlphaNumeric(6));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    break;
                case "123":
                    try {
                        binding.generatePass.setText(randomaNumeric(6));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    break;
            }

        });

        findViewById(R.id.help_dialog).setOnClickListener(v -> {
            mDialog = new Dialog(WifiPasswordGeneratorActivity.this);
            mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            mDialog.setContentView(R.layout.help);
            mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            mDialog.getWindow().setLayout(LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            CardView cancel;
            cancel = mDialog.findViewById(R.id.ok);
            cancel.setOnClickListener(v1 -> mDialog.cancel());
            mDialog.show();
        });

        binding.textCopy.setOnClickListener(view -> {

            String text = binding.generatePass.getText().toString();
            myClip = ClipData.newPlainText("text", text);
            myClipboard.setPrimaryClip(myClip);
            Toast.makeText(getApplicationContext(), "Text Copied",
                    Toast.LENGTH_SHORT).show();
        });
    }


    public static String randomAlphaNumeric(int count) {
        StringBuilder builder = new StringBuilder();
        while (count-- != 0) {
            int character = (int) (Math.random() * ALPHA_NUMERIC_STRING.length());
            builder.append(ALPHA_NUMERIC_STRING.charAt(character));
        }
        return builder.toString();
    }

    public static String randomAlphabets(int count) {
        StringBuilder builder = new StringBuilder();
        while (count-- != 0) {
            int character = (int) (Math.random() * ALPHABETS.length());
            builder.append(ALPHABETS.charAt(character));
        }
        return builder.toString();
    }

    public static String randomaNumeric(int count) {
        StringBuilder builder = new StringBuilder();
        while (count-- != 0) {
            int character = (int) (Math.random() * NUMERIC_STRING.length());
            builder.append(NUMERIC_STRING.charAt(character));
        }
        return builder.toString();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mDialog!=null && mDialog.isShowing()){
            mDialog.dismiss();
        }
    }
}

