package vamsi.kalanidhi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import java.util.ArrayList;
import java.util.List;

import vamsi.app_boutique.R;

public class DetailsActivity extends AppCompatActivity implements RecyclerViewClickListener {
    RecyclerView images_list, imagesSlider;
    ImagesListAdapter imagesListAdapter;
    ImagesDetailsAdapter imagesSliderAdapter;
    Context context;
    List<Product> products = new ArrayList<>();
    List<Images> images = new ArrayList<>();
    String productID;
    RecyclerViewClickListener recyclerViewClickListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        productID = getIntent().getStringExtra("product_id");
        context = this;
        recyclerViewClickListener = this;
        images_list = findViewById(R.id.imagesList);
        imagesSlider = findViewById(R.id.imagesSlider);
        getImages(productID);

    }

    public void showErrorDialog(String title, String message, final String pbt, final String nubt, final String ntbt, final AlertCallback callback) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message);
        builder.setCancelable(false);
        builder.setTitle(title);
        if (pbt != null)
            builder.setPositiveButton(pbt, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (callback != null)
                        callback.onAlertOptionClicked(1, pbt);
                    dialog.dismiss();
                }
            });
        if (ntbt != null)
            builder.setNegativeButton(ntbt, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (callback != null)
                        callback.onAlertOptionClicked(-1, ntbt);
                    dialog.dismiss();
                }
            });
        if (nubt != null)
            builder.setNeutralButton(nubt, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (callback != null)
                        callback.onAlertOptionClicked(0, nubt);
                    dialog.dismiss();
                }
            });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }

    private void getImages(final String productID) {
        Bundle getBundle = this.getIntent().getExtras();
        images = getBundle.getParcelableArrayList("images");
        //images = (List<Images>) getIntent().getSerializableExtra("images");
        imagesListAdapter = new ImagesListAdapter(context, R.layout.image_item, images);
        images_list.setAdapter(imagesListAdapter);
        images_list.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        imagesSliderAdapter = new ImagesDetailsAdapter(context, R.layout.image_slider, images, recyclerViewClickListener);
        imagesSlider.setAdapter(imagesSliderAdapter);
        imagesSlider.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
/*
        new ProductsController(context).getImages(new BaseCallback<ImgResults>() {
            @Override
            public void onComplete(boolean status, int status_code, String message, ImgResults response) {
                if (status) {
                    for (int i = 0; i < response.getResults().size(); i++) {
                        if (response.getResults().get(i).product.equalsIgnoreCase(productID)) {
                            image = response.getResults().get(i);
                            images.add(image);
                        }
                    }

                } else {
                    showErrorDialog("Oops!!!", "Something went wrong contact Admin", "Ok", null, null, null);
                }
            }
        });
*/
    }

    @Override
    public void recyclerViewListClicked(View v, int position) {
        images_list.getLayoutManager().scrollToPosition(position);
    }
}
