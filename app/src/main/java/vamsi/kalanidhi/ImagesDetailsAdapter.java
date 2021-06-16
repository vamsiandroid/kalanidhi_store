package vamsi.kalanidhi;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import vamsi.app_boutique.BuildConfig;
import vamsi.app_boutique.R;

public class ImagesDetailsAdapter extends RecyclerView.Adapter<ImagesDetailsAdapter.ViewHolder> {
    Context context;
    int resID;
    List<Images> images;
    RecyclerViewClickListener recyclerViewClickListener;

    public ImagesDetailsAdapter(Context context, int resID, List<Images> images, RecyclerViewClickListener recyclerViewClickListener) {
        this.context = context;
        this.resID = resID;
        this.images = images;
        this.recyclerViewClickListener = recyclerViewClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(resID, parent, false));
    }

    private Uri getLocalBitmapUri(Bitmap bmp, Context context) {
        Uri bmpUri = null;
        try {
            File file = new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "share_image_" + System.currentTimeMillis() + ".png");
            FileOutputStream out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.close();
            bmpUri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".fileprovider", file);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return bmpUri;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        Picasso.get().load(images.get(position).getImage()).placeholder(R.mipmap.ic_loading).into(holder.image_item);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerViewClickListener.recyclerViewListClicked(v, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image_item, share_item;
        TextView details;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image_item = itemView.findViewById(R.id.image);
            share_item = itemView.findViewById(R.id.catalog);
            details = itemView.findViewById(R.id.detail);
        }
    }
}
