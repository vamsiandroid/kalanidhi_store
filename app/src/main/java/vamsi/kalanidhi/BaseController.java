package vamsi.kalanidhi;

import android.content.Context;

import retrofit2.Retrofit;
import vamsi.app_boutique.BuildConfig;

/**
 * Created by Vamsikrishna Nelli on 11/14/2017.
 * Updated by Vamsikrishna Nelli on 11/14/2017.
 */

public class BaseController {
    Context context;
    Retrofit retrofitWithoutHeader;
    Retrofit retrofit;
    Roots roots;

    public BaseController(Context context) {
        this.context = context;
        retrofitWithoutHeader = GetRetrofit2.getRetrofit("http://34.213.48.254/", BuildConfig.DEBUG);
        roots = retrofitWithoutHeader.create(Roots.class);
        //        String header_value = localStorageData.getSessionToken();
        /*if (header_value != null) {
            Logger.d(MyConstants.my_param.TOKEN, header_value);
            retrofit = GetRetrofit2.getRetrofitWithHeader(storage_ip, MyConstants.my_param.TOKEN, header_value, BuildConfig.DEBUG, context.getResources().getStringArray(R.array.pinners));
            roots = retrofit.create(Roots.class);
        } else {*/

//        }
    }

}
