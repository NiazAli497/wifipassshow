package wifi.key.show.master.show.all.wifi.password.Activities;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.bumptech.glide.Glide;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.Reader;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import wifi.key.show.master.show.all.wifi.password.R;
import wifi.key.show.master.show.all.wifi.password.databinding.QrImageGenerateDisplayBinding;


public class DisplayQrImageActivity extends AppCompatActivity {
    String path="";
    private QrImageGenerateDisplayBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= QrImageGenerateDisplayBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Intent i = getIntent();


        if (i != null) {

            path = i.getExtras().getString("path");

        }

        binding.result.setOnClickListener(v -> {
            ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("label", binding.result.getText());
            clipboard.setPrimaryClip(clip);
            Toast.makeText(DisplayQrImageActivity.this, getString(R.string.copy), Toast.LENGTH_SHORT).show();

        });


        File imgFile = new File(path);
        Glide.with(DisplayQrImageActivity.this).load(path).into(binding.imagee);
        binding.decrypt.setOnClickListener(v -> {
            InputStream is = null;
            try {
                is = new BufferedInputStream(new FileInputStream(imgFile));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            Bitmap bitmap = BitmapFactory.decodeStream(is);
            String decoded = scanQRImage(bitmap);
            binding.result.setText(decoded);

        });


    }

    public static String scanQRImage(Bitmap bMap) {
        String contents = null;

        int[] intArray = new int[bMap.getWidth() * bMap.getHeight()];
        //copy pixel data from the Bitmap into the 'intArray' array
        bMap.getPixels(intArray, 0, bMap.getWidth(), 0, 0, bMap.getWidth(), bMap.getHeight());

        LuminanceSource source = new RGBLuminanceSource(bMap.getWidth(), bMap.getHeight(), intArray);
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

        Reader reader = new MultiFormatReader();
        try {
            Result result = reader.decode(bitmap);
            contents = result.getText();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return contents;
    }
}
