package com.example.roofit;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by kongsin on 8/20/16.
 */

public class Caller<T> {

    private final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private final OkHttpClient mClient;
    private final Request.Builder mBuilder;
    private String mUrl;
    private String mBaseUrl;
    private okhttp3.Call mCall;
    private Object mConverterObject;
    private Class<T> mTypeClass;
    private static final String TAG = "Caller";
    private RooFitCallBack mCallBack;
    private String mRequestJson;

    private Callback mOkhttpCallback = new Callback() {
        @Override
        public void onFailure(Call call, IOException e) {
            if (mCallBack != null) mCallBack.onFailed(e.getMessage());
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            String res = response.body().string();
            if (mConverterObject != null && mConverterObject instanceof com.google.gson.Gson) {
                if (mCallBack != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(res);
                        T t = ((com.google.gson.Gson) mConverterObject).fromJson(jsonObject.toString(), mTypeClass);
                        mCallBack.onResponse(t);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            } else {
                mCallBack.onResponse(res);
            }
        }
    };

    private Caller(String url, String baseUrl, Class<T> typeClass) {
        this.mUrl = url;
        this.mBaseUrl = baseUrl;
        this.mTypeClass = typeClass;
        this.mClient = new OkHttpClient();
        this.mBuilder = new Request.Builder();
    }

    private Caller(String url, String baseUrl, String requestJson, Class<T> typeClass) {
        this.mUrl = url;
        this.mBaseUrl = baseUrl;
        this.mTypeClass = typeClass;
        this.mClient = new OkHttpClient();
        this.mBuilder = new Request.Builder();
        this.mRequestJson = requestJson;
    }

    public void enqueue(final RooFitCallBack callback) {
        this.mCallBack = callback;
        mBuilder.url(mBaseUrl + mUrl);
        Request request = mBuilder.build();
        mCall = mClient.newCall(request);
        mCall.enqueue(mOkhttpCallback);
    }

    public void enqueuePost(RooFitCallBack callBack) {
        this.mCallBack = callBack;
        try {
            JSONObject jsonObject = new JSONObject(mRequestJson);
            RequestBody body = RequestBody.create(JSON, jsonObject.toString());
            Request request = new Request.Builder()
                    .url(mBaseUrl+mUrl)
                    .post(body)
                    .build();
            mCall = mClient.newCall(request);
            mCall.enqueue(mOkhttpCallback);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void setObjectConverter(Gson gsonConverter) {
        this.mConverterObject = gsonConverter;
    }

    public void cancel() {
        if (mCall != null && mCall.isExecuted() && !mCall.isCanceled()) {
            mCall.cancel();
        }
    }

    public interface RooFitCallBack<T> {
        void onResponse(T object);

        void onFailed(String msg);
    }
}
