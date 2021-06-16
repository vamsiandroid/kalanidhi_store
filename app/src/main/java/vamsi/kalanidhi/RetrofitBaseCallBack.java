package vamsi.kalanidhi;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by Vamsikrishna Nelli on 11/14/2017.
 * Updated by Vamsikrishna Nelli on 11/14/2017.
 */

public class RetrofitBaseCallBack<T> extends ErrorHandlerCallBack<T> {
    private BaseCallback<T> callback;

    public RetrofitBaseCallBack(BaseCallback<T> callback) {
        this.callback = callback;
    }


    @Override
    protected void onHandleError(int error_code, String message) {
        callback.onComplete(false, error_code, message, null);
    }

    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        callback.onComplete(response.isSuccessful(), response.code(), getMessage(response), response.isSuccessful() ? response.body() : null);
    }
}
