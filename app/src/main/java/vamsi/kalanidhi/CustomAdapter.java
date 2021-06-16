package vamsi.kalanidhi;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

import vamsi.app_boutique.R;

public class CustomAdapter extends BaseAdapter {
    Context context;
    List<Images> images;
    LayoutInflater inflter;
    String id;

    public CustomAdapter(Context applicationContext, List<Images> list, String id) {
        this.context = applicationContext;
        this.images = list;
        this.id = id;
        inflter = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflter.inflate(R.layout.activity_gridview, null); // inflate the layout
        ImageView icon = (ImageView) view.findViewById(R.id.icon); // get the reference of ImageView
        Glide.with(this.context)
                .load(images.get(i).image)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.mipmap.ic_loading)
                .into(icon);
        icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailsActivity.class);
                intent.putExtra("product_id", id);
                context.startActivity(intent);
            }
        });
        //icon.setImageResource(logos[i]); // set logo images
        return view;
    }
}