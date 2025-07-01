package wifi.key.show.master.show.all.wifi.password.Activities;

import android.app.Activity;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.IntentSenderRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import wifi.key.show.master.show.all.wifi.password.DatabaseHelper.DataBaseWifiHistory;
import wifi.key.show.master.show.all.wifi.password.Adapters.ImageAdapter;
import wifi.key.show.master.show.all.wifi.password.Adapters.RecyclerViewMain;
import wifi.key.show.master.show.all.wifi.password.DatabaseHelper.ImagesDatabaseHelper;
import wifi.key.show.master.show.all.wifi.password.R;
import wifi.key.show.master.show.all.wifi.password.databinding.QrMainBinding;
import wifi.key.show.master.show.all.wifi.password.model.ImagesModel;
import wifi.key.show.master.show.all.wifi.password.model.WifiModel;
import wifi.key.show.master.show.all.wifi.password.utils.AppUtils;

public class QrImageDisplayMain extends AppCompatActivity {
    ArrayList<ImagesModel> imageFiles = new ArrayList<>();
    WifiModel wifiModel;

    Handler handler=new Handler(Looper.getMainLooper());
    Dialog helpDialog;
    Dialog qrReaderDialog;
     public static String TAG= "QrImageDisplayMainDATAAA";

    ImageAdapter imageAdapter;


    ActivityResultLauncher<IntentSenderRequest> requestLauncher;
    int deletedPos = -1;
    File deletedFile;
    private QrMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= QrMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        wifiModel = new WifiModel();
        setUpDeleteRequestLauncher();
        setClickListeners();

        querySavedImages();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(AppUtils.isSavedItem){
            if(imageFiles!=null){
                imageFiles.clear();
            }
            querySavedImages2();
            AppUtils.isSavedItem=false;
        }
    }

    void noImagesFound(){
        TextView tv_msg = findViewById(R.id.tv_msg);
        tv_msg.setVisibility(View.VISIBLE);
        LottieAnimationView animationView = findViewById(R.id.animation_view);
        animationView.setVisibility(View.VISIBLE);
    }

    void setAdapter(){
        Log.e("TAG","images: "+imageFiles.toString());
        binding.savedImageRecycleView.setLayoutManager(new LinearLayoutManager(QrImageDisplayMain.this));
         imageAdapter = new ImageAdapter(imageFiles, AppUtils.DIR_PATH+File.separator);
        if (imageAdapter.getItemCount() == 0) {
        noImagesFound();
        }

        else {
            binding.tvMsg.setVisibility(View.GONE);
            binding.animationView.setVisibility(View.GONE);
            GridLayoutManager gridLayoutManager = new GridLayoutManager(QrImageDisplayMain.this, 2);
            binding.savedImageRecycleView.setLayoutManager(gridLayoutManager);
            binding.savedImageRecycleView.setAdapter(imageAdapter);
            binding.savedImageRecycleView.addOnItemTouchListener(new RecyclerViewMain(QrImageDisplayMain.this,
                    binding.savedImageRecycleView, new RecyclerViewMain.ClickListener() {
                @Override
                public void onClick(View view, int position) {
                    Log.d(TAG, "Executed Click");
                    Intent i = new Intent(QrImageDisplayMain.this, DisplayQrImageActivity.class);
                    i.putExtra("path", imageFiles.get(position).getFilePath());
                    startActivity(i);
                }

                public void onLongClick(View view, final int position) {

                }
            }));
        }
    }


    void setUpDeleteRequestLauncher(){
        requestLauncher = registerForActivityResult(new ActivityResultContracts.
                StartIntentSenderForResult(), result -> {
            if (result.getResultCode() == Activity.RESULT_OK) {
                AppUtils.notifyMediaScannerItemRemoved(deletedFile,this);
                imageFiles.remove(deletedPos);
                if ( imageAdapter!= null) {
                    imageAdapter.notifyItemRemoved(deletedPos);
                }
                Toast.makeText(this, getString(R.string.file_deleted), Toast.LENGTH_SHORT).show();
                if (imageFiles.isEmpty()){
                    noImagesFound();
                }
            }
        });
    }


    @Override
    protected void onDestroy() {
        if (helpDialog!=null && helpDialog.isShowing()){
            helpDialog.dismiss();
        }

        if (qrReaderDialog!=null && qrReaderDialog.isShowing()){
            qrReaderDialog.dismiss();
        }
        super.onDestroy();
    }
    //resolving deleting issue
    private void querySavedImages(){
        ExecutorService executorService= Executors.newSingleThreadExecutor();
        executorService.execute(() -> {

            File dir = new File(AppUtils.DIR_PATH);

            if (dir.exists()){

                File[] files = dir.listFiles();
                if (files!=null){

                    try {
                        for (File file : files) {
                            String Name = file.getName();
                            imageFiles.add(new ImagesModel(file.getAbsolutePath(),Name,false));
                        }
                    } catch (Exception ee) {
                        ee.printStackTrace();
                    }
                }

            }

            Looper.prepare();
            handler.post(() -> setAdapter());
            executorService.shutdown();
        });
    }
    private void querySavedImages2(){
        ExecutorService executorService= Executors.newSingleThreadExecutor();
        executorService.execute(() -> {

            File dir = new File(AppUtils.DIR_PATH);

            if (dir.exists()){

                File[] files = dir.listFiles();
                if (files!=null){

                    try {
                        for (File file : files) {
                            String Name = file.getName();
                            imageFiles.add(new ImagesModel(file.getAbsolutePath(),Name,false));
                        }
                    } catch (Exception ee) {
                        ee.printStackTrace();
                    }
                }

            }

            Looper.prepare();
           handler.post(() -> {
               Log.e("TAG","images: "+imageFiles.toString());
               binding.savedImageRecycleView.setLayoutManager(new LinearLayoutManager(QrImageDisplayMain.this));
               imageAdapter = new ImageAdapter(imageFiles, AppUtils.DIR_PATH+File.separator);
               if (imageAdapter.getItemCount() == 0) {
                   noImagesFound();
               }

               else {
                   binding.tvMsg.setVisibility(View.GONE);
                   binding.animationView.setVisibility(View.GONE);
                   GridLayoutManager gridLayoutManager = new GridLayoutManager(QrImageDisplayMain.this, 2);
                   binding.savedImageRecycleView.setLayoutManager(gridLayoutManager);
                   binding.savedImageRecycleView.setAdapter(imageAdapter);
               }
           });
            executorService.shutdown();
        });
    }

    private void helpDialog(){
        helpDialog = new Dialog(QrImageDisplayMain.this);
        helpDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        helpDialog.setContentView(R.layout.wifiscannerqrhelp);
        helpDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        CardView cancel;
        cancel = helpDialog.findViewById(R.id.okButton);
        cancel.setOnClickListener(v1 -> helpDialog.cancel());
        helpDialog.show();
    }

    private void setClickListeners(){
        binding.help.setOnClickListener(view -> {
            helpDialog();
        });


        binding.btnScanQR.setOnClickListener(view -> {
            qrReaderDialog();
        });



       binding.myfiledelete.setOnClickListener(v -> {
            boolean isSelected=false;
            for (int i=0;i<imageFiles.size();i++){
                ImagesModel model=imageFiles.get(i);
                if (model.getSelected()){
                    deleteItemFunction(i);
                    isSelected=true;
                    break;
                }
            }

            if (!isSelected){
                Toast.makeText(QrImageDisplayMain.this, getString(R.string.select_photo_to_delete), Toast.LENGTH_SHORT).show();
            }

        });

        binding.share.setOnClickListener(v -> {
            boolean isSelected=false;
            for (int i=0;i<imageFiles.size();i++){
                ImagesModel model=imageFiles.get(i);
                if (model.getSelected()){
                    shareFile(new File(model.getFilePath()));
                    isSelected=true;
                    break;
                }
            }
            if (!isSelected){
                Toast.makeText(QrImageDisplayMain.this, getString(R.string.select_photo_to_share), Toast.LENGTH_SHORT).show();
            }
        });

    }



    private void qrReaderDialog(){
        qrReaderDialog = new Dialog(QrImageDisplayMain.this);
        qrReaderDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        qrReaderDialog.setContentView(R.layout.qrreader);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(qrReaderDialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        qrReaderDialog.getWindow().setAttributes(lp);

        qrReaderDialog.findViewById(R.id.gotit).setOnClickListener(v12 -> {
            Intent i = new Intent(QrImageDisplayMain.this, QRScannerActivity.class);
            startActivity(i);
            qrReaderDialog.dismiss();
        });
        qrReaderDialog.show();
    }


    private Uri getFileUri(String path){

        Uri uri=null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            uri = MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL);
        } else {
            uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        }
        String selection=MediaStore.Images.Media.DATA+"=?";
        String[] args={path};
        Cursor cursor=getContentResolver().query(uri,null,selection,args,null);
        if (cursor!=null && cursor.moveToFirst()){
            long id=cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID));
            cursor.close();
            return ContentUris.withAppendedId(uri,id);
        }
        return null;
    }


    void deleteItemFunction(int pos){
        File fd = new File(imageFiles.get(pos).getFilePath());
        if (fd.exists()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                Uri uri=getFileUri(fd.getAbsolutePath());
                if (uri==null){
                    Toast.makeText(this, getString(R.string.file_delete_failed), Toast.LENGTH_SHORT).show();
                }
                else{
                    PendingIntent pendingIntent;
                    pendingIntent = MediaStore.createDeleteRequest(getContentResolver(),
                            Collections.singleton(uri));
                    deletedPos = pos;
                    deletedFile = fd;
                    IntentSenderRequest senderRequest = new IntentSenderRequest.Builder(pendingIntent.getIntentSender()).build();
                    requestLauncher.launch(senderRequest);
                }

            } else {
                boolean b = fd.delete();
                if (b) {
                    AppUtils.notifyMediaScannerItemRemoved(fd,this);
                    imageFiles.remove(pos);
                    if (imageAdapter != null) {
                        imageAdapter.notifyItemRemoved(pos);
                    }
                    Toast.makeText(this, getString(R.string.file_deleted), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, getString(R.string.file_delete_failed), Toast.LENGTH_SHORT).show();
                }
                if (imageFiles.isEmpty()){
                    noImagesFound();
                }
            }
        }
    }

    private void shareFile(File file){
        try {
            Uri uri=FileProvider.getUriForFile(QrImageDisplayMain.this,
                    getApplicationContext().getPackageName() + ".fileprovider", file);
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_SEND);
            intent.setType("*/*");
            intent.putExtra(Intent.EXTRA_SUBJECT, "");
            intent.putExtra(Intent.EXTRA_TEXT, "");
            intent.putExtra(Intent.EXTRA_STREAM, uri);
            startActivity(Intent.createChooser(intent, "Share via"));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
