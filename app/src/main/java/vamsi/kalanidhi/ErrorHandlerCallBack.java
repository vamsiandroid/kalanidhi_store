package vamsi.kalanidhi;

import com.google.gson.JsonSyntaxException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/************************************************
 * Created by Naresh on 04/07/17.
 * Updated by Naresh on : 12:27 PM 04 07 2017.
 ***********************************************/

public abstract class ErrorHandlerCallBack<T> implements Callback<T> {

    @Override
    public void onFailure(Call<T> call, Throwable t) {
        if (t != null)
            t.printStackTrace();
        if (t != null && t.getMessage() != null && t.getMessage().toLowerCase().contains("Failed to connect".toLowerCase()))
            onHandleError(404/*Constants.code.response.CONNECTION_ERROR*/, t.getMessage());
        else if (t != null) {
            if (t instanceof java.net.SocketTimeoutException)
                onHandleError(404/*Constants.code.response.TIME_OUT*/, "Time out");
            else if (t instanceof JsonSyntaxException)
                onHandleError(/*Constants.code.response.JSON_PARSE_EXCEPTION*/404, "Unable to parse the response\nPlease check for updates");
            else
                onHandleError(404/*Constants.code.response.UN_KNOWN_ERROR*/, t.getMessage() == null ? "Unknown" : t.getMessage());
        } else
            onHandleError(502/*Constants.code.response.UN_KNOWN_ERROR*/, "Unknown");
    }

    protected String getMessage(Response<T> response) {
        /*try {
            ResponseMessage responseMessage = new Gson().fromJson(response.errorBody().string(), ResponseMessage.class);
            return responseMessage.getMessage() == null ? response.message() : responseMessage.getMessage();
        } catch (Exception ignored) {
            return response.message();
        }

*/
        return String.valueOf(response);
    }

    protected abstract void onHandleError(int error_code, String message);
}
