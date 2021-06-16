package vamsi.kalanidhi;

import android.content.Context;

import retrofit2.Call;

public class ProductsController extends BaseController {
    public ProductsController(Context context) {
        super(context);
    }

    public void getProducts(BaseCallback<ProdResults> callback) {
        Call<ProdResults> call = roots.getProducts();
        call.enqueue(new RetrofitBaseCallBack<>(callback));

    }
    public void getProducts(int i,BaseCallback<ProdResults> callback) {
        Call<ProdResults> call = roots.getProducts(i);
        call.enqueue(new RetrofitBaseCallBack<>(callback));

    }

    public void getImages(BaseCallback<ImgResults> callback) {
        Call<ImgResults> call = roots.getImages();
        call.enqueue(new RetrofitBaseCallBack<>(callback));

    }
    public void getImages(int i,BaseCallback<ImgResults> callback) {
        Call<ImgResults> call = roots.getImages(i);
        call.enqueue(new RetrofitBaseCallBack<>(callback));

    }
}
