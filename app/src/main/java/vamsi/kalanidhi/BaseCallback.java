package vamsi.kalanidhi;

/**
 * Created by Vamsikrishna Nelli on 11/14/2017.
 * Updated by Vamsikrishna Nelli on 11/14/2017.
 */

public interface BaseCallback<T> {
    void onComplete(boolean status, int status_code, String message, T response);

}
