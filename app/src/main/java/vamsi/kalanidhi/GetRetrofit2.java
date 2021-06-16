package vamsi.kalanidhi;

import android.util.Base64;

import androidx.annotation.NonNull;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.CertificatePinner;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Naresh-Katta on 21-05-2016.
 * Updated by Naresh Katta on : 12:31 PM 21 05 2016 .
 */
public class GetRetrofit2 {

    private static Retrofit.Builder getBuilder(String endPoint, boolean needsLog, String... sha256) {
        OkHttpClient.Builder httpClient = getOkHttpBuilder(needsLog, endPoint, sha256);
        httpClient = addContentType(httpClient);
// set your desired log level
        return new Retrofit
                .Builder()
                .baseUrl(endPoint)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build());
    }

    private static OkHttpClient.Builder addContentType(OkHttpClient.Builder httpClient) {
        httpClient.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request.Builder requestBuilder = chain.request().newBuilder();
                requestBuilder.removeHeader("Content-Type");
                requestBuilder.addHeader("Content-Type", "application/json");
                return chain.proceed(requestBuilder.build());
            }
        });
        return httpClient;
    }

    private static String baseUrl(String endPoint) {
        endPoint = endPoint.replace("https://", "");
        endPoint = endPoint.replace("http://", "");
        return endPoint.substring(0, endPoint.indexOf("/"));
    }

    public static Retrofit getRetrofit(String endPoint, boolean needsLog, String... sha256) {
        Retrofit.Builder restAdapterBuilder = getBuilder(endPoint, needsLog, sha256);
        return restAdapterBuilder.build();
    }

    public static Retrofit getRetrofitWIthBasicAuthentication(String endPoint, final String userName, final String password, boolean needsLog, String... sha256) {
        Retrofit.Builder retrofitBuilder = getBuilder(endPoint, needsLog, sha256);
        OkHttpClient.Builder httpClient = getOkHttpBuilder(needsLog, endPoint, sha256);
        httpClient.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                String authorizationCredentials = userName + ":" + password;        // ID:Password
                String authorizationQuery = "Basic " + Base64.encodeToString(authorizationCredentials.getBytes(), Base64.NO_WRAP);
                Request chainRequest = chain.request();
                Request request = chainRequest.newBuilder()
                        .method(chainRequest.method(), chainRequest.body())
                        .header("Authorization", authorizationQuery)
                        .addHeader("Content-Type", "application/json")
                        .build();
                return chain.proceed(request);
            }
        });
        httpClient = addContentType(httpClient);
        OkHttpClient client = httpClient.build();
        return retrofitBuilder.client(client).build();
    }

    public static Retrofit getRetrofitWithHeader(String endPoint, final String header_name, final String header_value, boolean needsLog, String... sha256) {
        Retrofit.Builder retrofitBuilder = getBuilder(endPoint, needsLog, sha256);
        OkHttpClient.Builder httpClient = getOkHttpBuilder(needsLog, endPoint, sha256);
        httpClient.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request chainRequest = chain.request();
                Request request = chainRequest.newBuilder()
                        .method(chainRequest.method(), chainRequest.body())
                        .header(header_name, header_value)
                        .build();
                return chain.proceed(request);
            }
        });
        httpClient = addContentType(httpClient);
        OkHttpClient client = httpClient.build();
        return retrofitBuilder.client(client).build();
    }

    public static Retrofit addHeaders(String endPoint, final Map<String, String> headers, boolean needsLog, String... sha256) {
        Retrofit.Builder retrofitBuilder = getBuilder(endPoint, needsLog, sha256);
        OkHttpClient.Builder httpClient = getOkHttpBuilder(needsLog, endPoint, sha256);
        httpClient.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request chainRequest = chain.request();
                Request.Builder builder = chainRequest.newBuilder()
                        .method(chainRequest.method(), chainRequest.body());
                for (String key : headers.keySet()) {
                    builder.addHeader(key, headers.get(key));
                }
                Request request = builder.build();
                return chain.proceed(request);
            }
        });
        OkHttpClient client = httpClient.build();
        return retrofitBuilder.client(client).build();
    }

    @NonNull
    private static OkHttpClient.Builder getOkHttpBuilder(boolean needsLog, String endPoint, String... sha256) {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        String baseUrl = baseUrl(endPoint);
        httpClient.readTimeout(0, TimeUnit.SECONDS);
        httpClient.connectTimeout(0, TimeUnit.SECONDS);
        if (sha256 != null && sha256.length > 0) {
            CertificatePinner pinner = new CertificatePinner.Builder().add(baseUrl, sha256).build();
            httpClient.certificatePinner(pinner);
        }
        if (needsLog) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            HttpLoggingInterceptor loggingHeaders = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            loggingHeaders.setLevel(HttpLoggingInterceptor.Level.HEADERS);
            httpClient.addInterceptor(logging);
            httpClient.addInterceptor(loggingHeaders);
        }
        return httpClient;
    }
}