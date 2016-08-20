package com.example.kongsin.lesscode;


import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by kongsin on 8/20/16.
 */

public class CallReq<T> {

    private String mUrl;
    private String mBaseUrl;
    private Call mCall;
    private Object mConverterObject;
    private Class<T> mTypeClass;
    private static final String TAG = "CallReq";

    private CallReq(String url, String baseUrl, Class<T> typeClass) {
        this.mUrl = url;
        this.mBaseUrl = baseUrl;
        this.mTypeClass = typeClass;
    }

    public void enqueue(final RooFitCallBack callback) {
        OkHttpClient client = new OkHttpClient();
        Request.Builder builder = new Request.Builder();
        builder.url(mBaseUrl + mUrl);
        final Request request = builder.build();
        mCall = client.newCall(request);
        mCall.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (callback != null) callback.onFailed(e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String res = response.body().string();
                if (mConverterObject != null && mConverterObject instanceof com.google.gson.Gson) {
                    if (callback != null) {
                        try {
                            JSONObject jsonObject = new JSONObject(res);
                            T t = ((com.google.gson.Gson) mConverterObject).fromJson(jsonObject.toString(), mTypeClass);
                            callback.onResponse(t);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    callback.onResponse(res);
                }
            }
        });
    }

    public void setObjectConverter(com.google.gson.Gson gsonConverter) {
        this.mConverterObject = gsonConverter;
    }

    public void cancelRequest() {
        if (mCall != null && mCall.isExecuted() && !mCall.isCanceled()) {
            mCall.cancel();
        }
    }

    public interface RooFitCallBack<T> {
        void onResponse(T object);

        void onFailed(String msg);
    }
}
