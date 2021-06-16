package vamsi.kalanidhi;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.FileProvider;
import androidx.viewpager.widget.ViewPager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import vamsi.app_boutique.BuildConfig;
import vamsi.app_boutique.R;

public class ShareImagesActivity extends Activity implements ViewPager.OnPageChangeListener {
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
    TextView shareImages, shareDescription;
    String description;
    Context context;
    String pName, design_no, rate, fabric;
    String product_id;
    List<Images> images = new ArrayList<>();
    ArrayList<Uri> uris = new ArrayList<>();
    List<Bitmap> bitmaps = new ArrayList<>();
    ProgressDialog p;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sharing);
        context = this;
        aa = 0;
        pName = getIntent().getStringExtra("name");
        design_no = getIntent().getStringExtra("design_no");
        rate = getIntent().getStringExtra("rate");
        fabric = getIntent().getStringExtra("fabric");
        horizontal = (HorizontalScrollView) findViewById(R.id.horizontal);
        layout = (LinearLayout) findViewById(R.id.lay);
        shareImages = (TextView) findViewById(R.id.shareImages);
        shareDescription = (TextView) findViewById(R.id.shareDescription);
        product_id = getIntent().getStringExtra("product_id");
        Bundle getBundle = this.getIntent().getExtras();
        images = getBundle.getParcelableArrayList("images");
        description = getIntent().getStringExtra("description");
        new RunTask().execute();
        shareDescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, description);
                sendIntent.setType("text/plain");

                Intent shareIntent = Intent.createChooser(sendIntent, null);
                startActivity(shareIntent);
            }
        });
   /*     shareImages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //imageUriArray = new ArrayList<Uri>();

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
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_SEND);
                //intent.setType("text/plain");
                intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, imageUriArray);
                intent.setType("image/*");
                startActivity(intent);
            }
        });

        arrayList = new ArrayList<>();
       *//* new ProductsController(getBaseContext()).getImages(new BaseCallback<ImgResults>() {
            @Override
            public void onComplete(boolean status, int status_code, String message, ImgResults response) {
                if (status) {
                    if (response.getResults().size() > 0) {
                        for (int i = 0; i < response.getResults().size(); i++) {
                            if (response.getResults().get(i).getProduct().equalsIgnoreCase(product_id)) {
                                arrayList.add(response.getResults().get(i).getImage());
                            }
                        }
                    }
                }
            }
        });*//*
        arrayList.add("https://trinitytuts.com/wp-content/uploads/2015/02/trinitylogo.png");
        arrayList.add("http://lh4.googleusercontent.com/-1hHevfC4VTQ/AAAAAAAAAAI/AAAAAAAAAGs/Bi_dipj31f4/photo.jpg?sz=104");
        for (int ii = 0; ii < arrayList.size(); ii++) {
            LayoutInflater inflaters = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            myView = inflaters.inflate(R.layout.pager_item_multi, null);
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
        Uri uri1 = Uri.parse("android.resource://com.code2care.example.whatsappintegrationexample/drawable/image1");
        Uri uri2 = Uri.parse("android.resource://com.code2care.example.whatsappintegrationexample/drawable/image2");
        Uri uri3 = Uri.parse("android.resource://com.code2care.example.whatsappintegrationexample/drawable/image3");

        ArrayList imageUriArray = new ArrayList();
        imageUriArray.add(uri1);
        imageUriArray.add(uri2);

        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT, "Text caption message!!");
        intent.setType("text/plain");
        intent.setType("image/jpeg");
        intent.setPackage("com.whatsapp");
        intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, imageUriArray);
        startActivity(intent);

*/
    }

    private Uri getBitmap(Bitmap bitmap, Context context) {
        Uri uri = null;
        File file = new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "share_image_" + System.currentTimeMillis() + ".png");
        try {
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.close();
            uri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".fileprovider", file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return uri;
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

    class RunTask extends AsyncTask<String, String, List<Uri>> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            p = new ProgressDialog(context);
            p.setMessage("Please wait...");
            p.setIndeterminate(false);
            p.setCancelable(false);
            p.show();
        }

        @Override
        protected List<Uri> doInBackground(String... strings) {
            for (int i = 0; i < images.size(); i++) {
                try {
                    URL url = new URL(images.get(i).getImage().toString());
                    Bitmap image = BitmapFactory.decodeStream(url.openStream());
                    uris.add(getBitmap(image, context));
                } catch (IOException e) {
                    System.out.println(e);
                }
            }

            return uris;
        }

        @Override
        protected void onPostExecute(final List<Uri> uris) {
            super.onPostExecute(uris);
            p.cancel();
            shareImages.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent shareIntent = new Intent();
                    shareIntent.setAction(Intent.ACTION_SEND_MULTIPLE);
                    shareIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, (ArrayList<? extends Parcelable>) uris);
                    shareIntent.setType("image/*");
                    startActivity(Intent.createChooser(shareIntent, "Share images to.."));
                }
            });

        }
    }
}
