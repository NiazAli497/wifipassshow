package wifi.key.show.master.show.all.wifi.password.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;


import java.util.ArrayList;

import wifi.key.show.master.show.all.wifi.password.R;
import wifi.key.show.master.show.all.wifi.password.model.ImagesModel;


public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.MyViewHolder> {

    private final ArrayList<ImagesModel> images;
    private final String path;
    private Context image_context;

    public ImageAdapter(ArrayList<ImagesModel> images, String path) {
        this.images = images;
        this.path = path;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View itemLayoutView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.listitems, null);
        MyViewHolder viewHolder = new MyViewHolder(itemLayoutView);
        image_context = viewGroup.getContext();


        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {
        int pos=holder.getAdapterPosition();
        Glide.with(image_context).load(images.get(pos).getFilePath())
                .into(holder.imageView);

        String fileName =  images.get(pos).getFileName();
        fileName = fileName.substring(0, fileName.length() - 4);
        holder.ssids.setText(fileName);

        if (images.get(position).getSelected()){
            holder.mylinear.setBackgroundColor(Color.BLUE);
        }
        else{
            holder.mylinear.setBackgroundColor(Color.WHITE);
        }


    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView ssids;
        RelativeLayout mylinear;


        @SuppressLint("NotifyDataSetChanged")
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            mylinear = itemView.findViewById(R.id.mylinear);
            imageView = itemView.findViewById(R.id.screen_imageview);
            ssids = itemView.findViewById(R.id.ssids);
            ssids.setSelected(true);

            imageView.setOnClickListener(view -> {
                int pos=getAdapterPosition();
                if (images.get(pos).getSelected()){
                    images.get(getAdapterPosition()).setSelected(false);
                    notifyItemChanged(getAdapterPosition());
                }
            });

            imageView.setOnLongClickListener(v -> {
                int adapterPosition=getAdapterPosition();

                ImagesModel model=images.get(adapterPosition);

                for (ImagesModel model1:images){
                    if (model1!=model){
                        model1.setSelected(false);
                    }
                }

                images.get(adapterPosition).setSelected(!images.get(adapterPosition).getSelected());

                notifyDataSetChanged();
                return true;
            });
        }
    }
}
