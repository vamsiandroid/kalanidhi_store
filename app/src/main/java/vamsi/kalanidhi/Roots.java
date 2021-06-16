package vamsi.kalanidhi;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface Roots {
    @GET("products")
    Call<ProdResults> getProducts();

    @GET("products")
    Call<ProdResults> getProducts(@Query("page") int page);

    @GET("images")
    Call<ImgResults> getImages();

    @GET("images")
    Call<ImgResults> getImages(@Query("page") int page);
}