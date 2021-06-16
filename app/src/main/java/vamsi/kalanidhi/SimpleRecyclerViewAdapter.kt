package vamsi.app_boutique

import android.app.Activity
import android.app.AlertDialog
import android.app.DownloadManager
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import vamsi.app_boutique.databinding.ItemListBinding
import java.io.File
import java.io.FileOutputStream
import androidx.core.content.FileProvider
import java.io.IOException
import android.os.Environment
import android.os.Parcelable
import android.widget.Toast
import android.widget.ViewSwitcher
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.GlideDrawable
import com.google.gson.Gson
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType
import com.smarteist.autoimageslider.SliderAnimations
import com.squareup.picasso.Target
import vamsi.kalanidhi.*
import java.io.FileNotFoundException
import java.net.MalformedURLException
import java.net.URI
import java.net.URISyntaxException
import java.net.URL
import java.util.*
import kotlin.collections.ArrayList


class SimpleRecyclerViewAdapter(var context: Context, var DataList: List<Product>, var imagesList: List<Images>?) : RecyclerView.Adapter<SimpleRecyclerViewAdapter.ViewHolder>() {
    var arrayList: java.util.ArrayList<String> = ArrayList()
    internal var imageUriArray = java.util.ArrayList<Uri>()
    var view_switcher: ViewSwitcher? = null
    private lateinit var adapter: ImageSliderAdapter
    var images: List<Images>? = ArrayList<Images>()
    var urls: ArrayList<String>? = ArrayList<String>()
    public var loaded: Boolean = false
    var customAdapter: CustomAdapter? = null
    val photoThumbnails = HashMap<String, Bitmap>()
    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {

        val view = LayoutInflater.from(context).inflate(R.layout.item_list, null)
        // view.setLayoutParams(RecyclerView.LayoutParams(1080, 1000))
        val bi = DataBindingUtil.bind<ItemListBinding>(view)
        urls!!.add(imagesList!!.get(i).image)

        return ViewHolder(bi!!)
    }

    override fun onBindViewHolder(holder: ViewHolder, i: Int) {
        holder.bi.name.text = "Title: " + DataList[i].title
        holder.bi.cost.text = "Cost: " + DataList[i].price.toString()
        holder.bi.description.text = "Description: " + DataList[i].description.toString()
        //customAdapter = CustomAdapter(context, imagesList, DataList[i].id)
        //holder.bi.simpleGridView.setAdapter(customAdapter);
        var id: String = DataList[i].id
        var toDownloadImages: ArrayList<URL> = java.util.ArrayList()

        var filteredImages: ArrayList<Images> = java.util.ArrayList()
        var slidingImages: ArrayList<Images> = java.util.ArrayList()
        var filteredImage: Images? = null
        for (i in imagesList!!.indices) {
            if (imagesList!!.get(i).product == id) {
                filteredImage = imagesList!!.get(i)
                filteredImages.add(filteredImage)
            }
        }
        adapter = ImageSliderAdapter(filteredImages, context)
        holder.bi.imageSlider.setSliderAdapter(adapter)
        holder.bi.imageSlider.setIndicatorAnimation(IndicatorAnimationType.WORM);
        holder.bi.imageSlider.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        try {
            if (filteredImages.size > 1) {
                if (filteredImages.get(0) != null) {
                    //RunTask().execute()
                    val thumbnailRequest = Glide.with(this.context).load(filteredImages.get(0).image)

                    //  holder.bi.image.setImageBitmap(bitmaps.get(0))
//                    holder.bi.image.setImageURI(filteredImages.get(0).image)
                    Glide.with(this.context)
                            .load(filteredImages.get(0).image)
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .thumbnail(thumbnailRequest)
                            .into(holder.bi.image)
                } else {
                    holder.bi.image.setBackgroundResource(R.mipmap.ic_loading)
                }
                if (filteredImages.get(1) != null) {
                    // holder.bi.imageThree.setImageBitmap(bitmaps.get(1))
//                    holder.bi.imageThree.setImageURI(filteredImages.get(1).image)
                    Glide.with(this.context)
                            .load(filteredImages.get(1).image)
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .placeholder(R.mipmap.ic_loading)
                            .into(holder.bi.imageThree)
                } else {
                    holder.bi.imageThree.setBackgroundResource(R.mipmap.ic_loading)
                }
                if (filteredImages.get(2) != null) {
                    //  holder.bi.imageTwo.setImageBitmap(bitmaps.get(2))
//                    holder.bi.imageTwo.setImageURI(filteredImages.get(2).image)
                    Glide.with(this.context)
                            .load(filteredImages.get(2).image)
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .placeholder(R.mipmap.ic_loading)
                            .into(holder.bi.imageTwo)
                } else {
                    holder.bi.imageTwo.setBackgroundResource(R.mipmap.ic_loading)
                }
                if (filteredImages.get(3) != null) {
                    // holder.bi.imageFour.setImageBitmap(bitmaps.get(3))

//                    holder.bi.imageFour.setImageURI(filteredImages.get(3).image)
                    Glide.with(this.context)
                            .load(filteredImages.get(3).image)
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .placeholder(R.mipmap.ic_loading)
                            .into(holder.bi.imageFour)
                } else {
                    holder.bi.imageFour.setBackgroundResource(R.mipmap.ic_loading)
                }
            }
        } catch (e: Exception) {

        }
        /* Picasso.get().load(filteredImages.get(0).image).placeholder(R.mipmap.ic_launcher).into(holder.bi.image)
         Picasso.get().load(filteredImages.get(1).image).placeholder(R.mipmap.ic_launcher).into(holder.bi.imageTwo)
         Picasso.get().load(filteredImages.get(2).image).placeholder(R.mipmap.ic_launcher).into(holder.bi.imageThree)
         Picasso.get().load(filteredImages.get(3).image).placeholder(R.mipmap.ic_launcher).into(holder.bi.imageFour)
        */ loaded = true
        holder.bi.share.setOnClickListener(View.OnClickListener {
            var intent: Intent = Intent(context, ShareImagesActivity::class.java)
            var gson: Gson? = Gson()
            var bundle: Bundle = Bundle()
            bundle.putParcelableArrayList("images", filteredImages)
            val list: String = gson!!.toJson(filteredImage)
            intent.putExtra("description", DataList[i].description)
            intent.putExtras(bundle)
            context.startActivity(intent)

            //   arrayList.add("https://trinitytuts.com/wp-content/uploads/2015/02/trinitylogo.png")
            //     arrayList.add("https://content.linkedin.com/content/dam/me/learning/blog/2017/Junepics/Money.jpg")
            /*    for (i in filteredImages!!.indices) {
                    arrayList.add(filteredImages!!.get(i).image)
                }
                shareImage(arrayList, context)
            */    /* val drawable = holder.bi.image.getDrawable()
             var bmp: Bitmap? = null
             if (drawable is BitmapDrawable) {
                 bmp = (holder.bi.image.getDrawable() as BitmapDrawable).bitmap
             }
             if (bmp != null) {
                 shareImage(bmp)
             }*/
            /* val sharingIntent = Intent(Intent.ACTION_SEND)
             //  val screenshotUri = Uri.parse(Images.Media.EXTERNAL_CONTENT_URI.toString() + "/" + imageIDs)
             sharingIntent.putExtra(Intent.EXTRA_TEXT, "Sample text")
             context.startActivity(Intent.createChooser(sharingIntent, "Share image using"))
        */
        })
        try {
            holder.itemView.setOnClickListener {
                var intent: Intent = Intent(context, DetailsActivity::class.java)
                var gson: Gson? = Gson()
                var bundle: Bundle = Bundle()
                bundle.putParcelableArrayList("images", filteredImages)
                val list: String = gson!!.toJson(filteredImage)
                intent.putExtra("product_id", DataList[i].id)
                intent.putExtras(bundle)
                context.startActivity(intent)
            }
        } catch (e: Exception) {
        }
        holder.bi.enquiry.setOnClickListener(View.OnClickListener {
            /*  var number: Uri = Uri.parse("tel:9154211571")
              var intent: Intent = Intent(Intent.ACTION_DIAL, number)
              context.startActivity(intent)*/
            if (appInstalledOrNot("com.whatsapp")) {
                var number: Uri = Uri.parse("tel:9154211571")
                try {
                    var url: String = "https://api.whatsapp.com/send?phone=" + "+91 9154211571"
                    var intent: Intent = Intent(Intent.ACTION_VIEW)
                    intent.setData(Uri.parse(url))
                    if (intent.resolveActivity(context.packageManager) != null) {
                        context.startActivity(intent)
                    }
                    context.startActivity(intent)
                } catch (e: Exception) {
                }
            } else {
                holder.bi.enquiryText.setText("Enquiry")
                holder.bi.enquiryIcon.setImageDrawable(context.resources.getDrawable(R.mipmap.ic_enquiry))
                var number: Uri = Uri.parse("tel:9154211571")
                var intent: Intent = Intent(Intent.ACTION_DIAL, number)
                context.startActivity(intent)
            }
        })
        holder.bi.download.setOnClickListener(View.OnClickListener {
            if (checkPermission()) {

                // DownloadTask(context,)
                for (i in filteredImages!!.indices) {
                    downloadFile(filteredImages!!.get(i).image.toString(), context)
                    // DownloadTask().execute(filteredImages!!.get(i).image.toString())
                    //toDownloadImages.add(URL(filteredImages!!.get(i).image.toString()));
                }
            } else {
                requestPermission()
            }
        })

    }

    fun appInstalledOrNot(uri: String): Boolean {
        var pm: PackageManager = context.getPackageManager();
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (e: PackageManager.NameNotFoundException) {
        }

        return false;
    }

    fun checkPermission(): Boolean {
        var result: Int = ContextCompat.checkSelfPermission(context, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    fun requestPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(context as Activity, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            Toast.makeText(context, "Write External Storage permission allows us to save files. Please allow this permission in App Settings.", Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(context as Activity, arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE), 0);
        }
    }

    fun downloadFile(uRl: String, context: Context) {
        Toast.makeText(context, "Download started", Toast.LENGTH_LONG).show()
        val myDir = File(Environment.getExternalStorageDirectory(), "Kalandihi/")
        if (!myDir.exists()) {
            myDir.mkdirs()
        }

        val mgr = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager

        val downloadUri = Uri.parse(uRl)
        val request = DownloadManager.Request(
                downloadUri)

        try {
            request.setAllowedNetworkTypes(
                    DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE).setAllowedOverMetered(true)
                    .setAllowedOverRoaming(true).setTitle("Kalanidhi - Downloading $uRl").setVisibleInDownloadsUi(true)
                    .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "/" + "Kalanidhi" + Date().toString() + ".png");
            Toast.makeText(context, "Downloading files", Toast.LENGTH_LONG).show()
        } catch (e: Exception) {
            // Toast.makeText(context, "Download failed", Toast.LENGTH_LONG).show()
        }

        mgr!!.enqueue(request)

    }

    fun showErrorDialog(title: String, message: String, pbt: String?, nubt: String?, ntbt: String?, callback: AlertCallback?) {
        val builder = AlertDialog.Builder(context)
        builder.setMessage(message)
        builder.setCancelable(false)
        builder.setTitle(title)
        if (pbt != null)
            builder.setPositiveButton(pbt) { dialog, which ->
                if (callback != null)
                    callback!!.onAlertOptionClicked(1, pbt)
                dialog.dismiss()
            }
        if (ntbt != null)
            builder.setNegativeButton(ntbt) { dialog, which ->
                if (callback != null)
                    callback!!.onAlertOptionClicked(-1, ntbt)
                dialog.dismiss()
            }
        if (nubt != null)
            builder.setNeutralButton(nubt) { dialog, which ->
                if (callback != null)
                    callback!!.onAlertOptionClicked(0, nubt)
                dialog.dismiss()
            }
        val alertDialog = builder.create()
        alertDialog.show()

    }


    fun shareImage(url: java.util.ArrayList<String>, context: Context) {
        Picasso.get().load(url.get(0)).into(object : Target {
            override fun onBitmapLoaded(bitmap: Bitmap, from: Picasso.LoadedFrom) {
                var intent: Intent = Intent(Intent.ACTION_SEND)
                intent.setType("image/*")
                intent.putExtra(Intent.EXTRA_STREAM, getLocalBitmapUri(bitmap, context))
                context.startActivity(Intent.createChooser(intent, "Share image using"))
            }

            override fun onBitmapFailed(e: Exception, errorDrawable: Drawable) {

            }

            override fun onPrepareLoad(placeHolderDrawable: Drawable?) {

            }
        })

    }


    fun getURI(urlStr: String): URI? {
        var url: URL? = null
        var uri: URI? = null
        try {
            url = URL(urlStr);
        } catch (e: MalformedURLException) {
        }

        // Convert a URL to a URI
        try {
            uri = url!!.toURI();
        } catch (e: URISyntaxException) {
        }
        return uri
    }

    fun getLocalBitmapUri(bmp: Bitmap, context: Context): Uri? {
        var bmpUri: Uri? = null
        try {
            val file = File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "share_image_" + System.currentTimeMillis() + ".png")
            val out = FileOutputStream(file)
            bmp.compress(Bitmap.CompressFormat.PNG, 90, out)
            out.close()
            bmpUri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".fileprovider", file);
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return bmpUri
    }

    private fun shareImage(bitmap: Bitmap) {
        // save bitmap to cache directory
        try {
            val cachePath = File(context.cacheDir, "imageview")
            cachePath.mkdirs() // don't forget to make the directory
            val stream = FileOutputStream("$cachePath /image.jpg") // overwrites this image every time
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
            stream.close()

        } catch (e: IOException) {
            e.printStackTrace()
        }

        val imagePath = File(context.cacheDir, "imageview")
        val newFile = File(imagePath, "image.jpg")
        val contentUri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".fileprovider", newFile)

        if (contentUri != null) {
            val shareIntent = Intent()
            shareIntent.action = Intent.ACTION_SEND
            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION) // temp permission for receiving app to read this file
            shareIntent.setDataAndType(contentUri, context.contentResolver.getType(contentUri))
            shareIntent.putExtra(Intent.EXTRA_STREAM, contentUri)
            shareIntent.type = "image/*"
            context.startActivity(Intent.createChooser(shareIntent, "Choose an app"))
        }
    }


    override fun getItemCount(): Int {
        return DataList.size
    }

    var p: ProgressDialog = ProgressDialog(context)

    inner class ViewHolder(var bi: ItemListBinding) : RecyclerView.ViewHolder(bi.root)

    private fun getBitmap(bitmap: Bitmap, context: Context): Uri? {
        var uri: Uri? = null
        val file = File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "share_image_" + System.currentTimeMillis() + ".png")
        try {
            val out = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, out)
            out.close()
            uri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".fileprovider", file)
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return uri
    }

}