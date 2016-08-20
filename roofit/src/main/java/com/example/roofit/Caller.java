package com.example.roofit;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by kongsin on 8/20/16.
 */

public class Caller<T> {

    private String mUrl;
    private String mBaseUrl;
    private okhttp3.Call mCall;
    private Object mConverterObject;
    private Class<T> mTypeClass;
    private static final String TAG = "Caller";

    private Caller(String url, String baseUrl, Class<T> typeClass) {
        this.mUrl = url;
        this.mBaseUrl = baseUrl;
        this.mTypeClass = typeClass;
    }

    public void enqueue(final RooFitCallBack callback) {
        okhttp3.OkHttpClient client = new okhttp3.OkHttpClient();
        okhttp3.Request.Builder builder = new okhttp3.Request.Builder();
        builder.url(mBaseUrl + mUrl);
        final okhttp3.Request request = builder.build();
        mCall = client.newCall(request);
        mCall.enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                if (callback != null) callback.onFailed(e.getMessage());
            }

            @Override
            public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {
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
