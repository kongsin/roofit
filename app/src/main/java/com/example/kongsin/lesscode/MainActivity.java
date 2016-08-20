package com.example.kongsin.lesscode;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.roofit.Caller;
import com.example.roofit.RooFit;
import com.google.gson.Gson;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RooFit myFit = new RooFit();
        APILoader loader = myFit.create(APILoader.class, "http://me-catalogsvc.azurewebsites.net/");
        Caller<A> call = loader.loadData();
        call.setObjectConverter(new Gson());
        call.enqueue(new Caller.RooFitCallBack() {
            @Override
            public void onResponse(Object object) {
                Log.i(TAG, "onResponse: " + ((A) object).getName());
            }

            @Override
            public void onFailed(String msg) {
                Log.i(TAG, "onFailed: " + msg);
            }
        });
    }
}
