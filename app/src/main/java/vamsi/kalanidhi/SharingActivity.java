package vamsi.kalanidhi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Date;

import vamsi.app_boutique.R;

public class SharingActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener {

    ArrayList<String> arrayList;

    HorizontalScrollView horizontal;
    ArrayList<Uri> imageUriArray = new ArrayList<Uri>();
    int aa;
    private ViewPager intro_images;
    private LinearLayout pager_indicator;

    private int dotsCount;
    private ImageView[] dots;

    private SharedPreferences sharedPreferences;
    private String uid;
    private Bitmap mbitmap;
    private LinearLayout layout;
    private View myView;
    private LinearLayout layMain;
    private int screenWidth, c;
    private int perc;
    String pName, design_no, rate, fabric;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_images);
        this.setFinishOnTouchOutside(false);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ImageView imageView = (ImageView) findViewById(R.id.imageView);
//        imageView.setVisibility(View.VISIBLE);
        aa = 0;
        pName = getIntent().getStringExtra("name");
        design_no = getIntent().getStringExtra("design_no");
        rate = getIntent().getStringExtra("rate");
        fabric = getIntent().getStringExtra("fabric");
        horizontal = (HorizontalScrollView) findViewById(R.id.horizontal);
        imageView.setImageResource(R.mipmap.ic_launcher);
        layout = (LinearLayout) findViewById(R.id.lay);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //imageUriArray = new ArrayList<Uri>();
                toolbar.setVisibility(View.GONE);

                for (int i = 0; i < arrayList.size(); i++) {
                    Date now = new Date();
                    android.text.format.DateFormat.format("yyyy-MM-dd_hh:mm:ss", now);
                    aa = screenWidth * i;
                    horizontal.scrollTo(aa, 0);


                    try {
                        // image naming and path  to include sd card  appending name you choose for file
                        String mPath = Environment.getExternalStorageDirectory().toString() + "/" + now + i + ".jpg";

                        // create bitmap screen capture
                        View v1 = myView.getRootView();
                        v1.setDrawingCacheEnabled(true);
                        Bitmap bitmap = Bitmap.createBitmap(v1.getDrawingCache());
                        v1.setDrawingCacheEnabled(false);

                        File imageFile = new File(mPath);

                        FileOutputStream outputStream = new FileOutputStream(imageFile);
                        int quality = 100;
                        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
                        outputStream.flush();
                        outputStream.close();
                        imageUriArray.add(Uri.fromFile(new File(String.valueOf(imageFile))));
                        //openScreenshot(imageFile);
                    } catch (Throwable e) {
                        // Several error may come out with file handling or OOM
                        e.printStackTrace();
                    }
                }
                toolbar.setVisibility(View.VISIBLE);
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, imageUriArray);
                intent.setType("image/jpeg");
                startActivity(intent);
            }
        });

        arrayList = new ArrayList<>();
        arrayList.add("https://trinitytuts.com/wp-content/uploads/2015/02/trinitylogo.png");
        arrayList.add("http://lh4.googleusercontent.com/-1hHevfC4VTQ/AAAAAAAAAAI/AAAAAAAAAGs/Bi_dipj31f4/photo.jpg?sz=104");

        for (int ii = 0; ii < arrayList.size(); ii++) {
            LayoutInflater inflaters = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            myView = inflaters.inflate(R.layout.item, null);
            layMain = (LinearLayout) myView.findViewById(R.id.layMain);
            DisplayMetrics metrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(metrics);
            //int screenHeight = metrics.heightPixels;
            screenWidth = metrics.widthPixels;
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(screenWidth, LinearLayout.LayoutParams.MATCH_PARENT);
            ImageView imageViews = (ImageView) myView.findViewById(R.id.img_pager_item);
            Picasso.get().load(arrayList.get(ii)).placeholder(R.mipmap.ic_launcher).into(imageViews);
            layMain.setLayoutParams(layoutParams);
            layMain.setTag("" + ii);
            layout.addView(myView);
        }
    }

    private void setUiPageViewController() {

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

}

