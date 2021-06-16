package vamsi.kalanidhi

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import vamsi.app_boutique.SimpleRecyclerViewAdapter

import android.app.AlertDialog
import android.widget.ViewSwitcher
import android.app.ProgressDialog
import android.content.Intent
import android.view.*
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView


class MainActivity : AppCompatActivity() {
    protected var dialog: ProgressDialog? = null
    var view_switcher: ViewSwitcher? = null
    private lateinit var adapter: SimpleRecyclerViewAdapter
    private var products: List<Product> = java.util.ArrayList()
    private var images: List<Images> = java.util.ArrayList()
    lateinit var filteredList: ArrayList<Product>
    private var context: Context? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(vamsi.app_boutique.R.layout.activity_main)
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        context = this
        init()

    }

    /*
       inti method to initilize recyclerView and list
     */
    private fun init() {
        hasLoadingView()
        getProducts()
        view_switcher = findViewById(vamsi.app_boutique.R.id.viewSwitcher)

    }

    protected fun hasLoadingView() {
        try {
            view_switcher = findViewById(vamsi.app_boutique.R.id.viewSwitcher) as ViewSwitcher
        } catch (ignored: Exception) {
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(vamsi.app_boutique.R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        var id: Int = item.itemId
        if (id == vamsi.app_boutique.R.id.item1) {
            var intent: Intent = Intent(context, InfoActivity::class.java)
            startActivity(intent)
        }
        if (id == vamsi.app_boutique.R.id.item2) {
            showLoadingDialog("Reloading...!")
            init()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setupRecyclerView(list: List<Product>) {
        var images: List<Images> = java.util.ArrayList()
        ProductsController(context).getImages { status, status_code, message, response ->
            if (status) {
                images = response.getResults()
                adapter = SimpleRecyclerViewAdapter(this, list, images)
                var layoutManager: LinearLayoutManager = object : LinearLayoutManager(context) {
                    override fun generateDefaultLayoutParams(): RecyclerView.LayoutParams {
                        return RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT)
                    }
                };
                //setting up layout manager to recyclerView
                simpleList.layoutManager = layoutManager
                //setting up layout manager to recyclerView
                // simpleList.layoutManager = LinearLayoutManager(this)
                simpleList.setHasFixedSize(true)
                simpleList.setItemViewCacheSize(20)
                simpleList.setDrawingCacheEnabled(true)
                simpleList.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH)
                //setting adapter to recyclerView
                simpleList.adapter = adapter

                switchToContentPage()

            } else {
                showErrorDialog("Oops!!!", "Something went wrong contact Admin", "Ok", null, null, null)
            }
        }
        //initlizing adapter

    }

    protected fun switchToContentPage() {
        try {
            view_switcher!!.setDisplayedChild(1)
        } catch (ignored: Exception) {
        }

    }

    private fun setupRecyclerView(list: List<Product>, imagesList: List<Images>) {

        //initlizing adapter
        adapter = SimpleRecyclerViewAdapter(this, list, imagesList)
        var layoutManager: LinearLayoutManager = object : LinearLayoutManager(context) {
            override fun generateDefaultLayoutParams(): RecyclerView.LayoutParams {
                return RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT)
            }
        };
        //setting up layout manager to recyclerView
        simpleList.layoutManager = layoutManager
        simpleList.setHasFixedSize(true)
        simpleList.setHasFixedSize(true)
        simpleList.setItemViewCacheSize(20)
        simpleList.setDrawingCacheEnabled(true)
        simpleList.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH)
        //setting adapter to recyclerView
        simpleList.adapter = adapter
        switchToContentPage()


    }

    public fun showLoadingDialog(message: String) {
        try {
            if (dialog == null)
                dialog = ProgressDialog(context)
            dialog!!.setMessage(message)
            dialog!!.setCancelable(false)
            if (!dialog!!.isShowing())
                dialog!!.show()
        } catch (e: Exception) {
        }

    }

    public fun closeLoadingDialog() {
        try {
            if (dialog != null && dialog!!.isShowing()) {
                dialog!!.dismiss()
            }
        } catch (e: Exception) {
        }

    }

    fun getData(context: Context): ArrayList<Data> {

        val people = ArrayList<Data>()
        //getting array from resource which I have implemented under values folder,you can have your dummy data
        val data = context.resources.getStringArray(vamsi.app_boutique.R.array.people)

        //same as data I have put dummy images as well in drawable and put those images in an array.
        val images = context.resources.obtainTypedArray(vamsi.app_boutique.R.array.images)

        //looping through all dummy data
        for (i in data.indices) {
            val d = Data(data[i], "", images.getResourceId(i, -1))
            people.add(d)
        }

        return people

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

    var productList: MutableList<Product>? = null
    var imagesList: MutableList<Images>? = null
    private fun getProducts() {
        //showLoadingDialog("Loading")
        if (NetworkChangeReceiver.isNetworkConnected(context)) {
            ProductsController(context).getProducts { status, status_code, message, response ->
                closeLoadingDialog()
                if (status) {
                    if (response.getResults().size > 0) {
                        productList = response.getResults()
                        if (response.getNext() == null) {
                            getImages(response.getResults())
                        } else {
                            val a = Integer.valueOf(response.getCount()) / 10
                            for (i in 2..a + 1) {
                                getNextList(i)
                            }
                        }

                    } else {
                        showErrorDialog("Oops!!!", "There is no data available", "Retry", null, "Let me exit", object : AlertCallback {
                            override fun onAlertOptionClicked(option: Int, message: String) {
                                if (option == 1) {
                                    init()
                                } else {
                                    finish()
                                }
                            }
                        })
                    }
                } else {
                    showErrorDialog("Oops!!!", "Something went wrong contact Admin", "Ok", null, null, null)
                }
            }
        } else {
            Toast.makeText(context, "Oops!!!, Internet connection lost", Toast.LENGTH_LONG).show()
        }
    }

    private fun getNextList(i: Int) {
        ProductsController(context).getProducts(i, BaseCallback { status, status_code, message, response ->
            if (status) {
                productList!!.addAll(response.getResults())
                getImages(productList!!)
            }
        })
    }

    private fun getNextImages(i: Int) {
        ProductsController(context).getImages(i, BaseCallback { status, status_code, message, response ->
            if (status) {
                imagesList!!.addAll(response.getResults())
                setData(productList!!, imagesList!!)
            }
        })
    }

    private fun getImages(results: MutableList<Product>) {
        if (NetworkChangeReceiver.isNetworkConnected(context)) {

            ProductsController(context).getImages { status, status_code, message, response ->
                //  closeLoadingDialog()
                if (status) {

                    if (response.getResults().size > 0) {
                        imagesList = response.getResults()
                        if (response.getNext() == null) {
                            setData(results, response.getResults())
                        } else {
                            val a = Integer.valueOf(response.getCount()) / 10
                            for (i in 2..a + 1) {
                                getNextImages(i)
                            }
                        }
                        //setData(results, response.getResults())
                    } else {
                        showErrorDialog("Oops!!!", "There is no data available", "Retry", null, "Let me exit", object : AlertCallback {
                            override fun onAlertOptionClicked(option: Int, message: String) {
                                if (option == 1) {
                                    init()
                                } else {
                                    finish()
                                }
                            }
                        })
                    }
                } else {
                    showErrorDialog("Oops!!!", "Something went wrong contact Admin", "Ok", null, null, null)
                }
            }
        } else {
            Toast.makeText(context, "Oops!!!, Internet connection lost", Toast.LENGTH_LONG).show()
        }

    }

    private fun setData(results: MutableList<Product>, images: MutableList<Images>) {
        var list: List<Product> = results
        setupRecyclerView(results, images)
        mSearchName.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                hasLoadingView()
                filteredList = ArrayList()
                if (p0.toString() != "") {
                    for (item in list) {
                        if (item.title.toLowerCase().contains(p0.toString().toLowerCase())) {
                            filteredList.add(item)
                        }
                    }
                    setupRecyclerView(filteredList)
                } else {
                    setupRecyclerView(list)
                }
            }

        })
    }

}