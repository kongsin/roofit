package com.example.kongsin.lesscode;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.roofit.Caller;
import com.example.roofit.RooFit;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RooFit myFit = new RooFit();
        APILoader loader = myFit.create(APILoader.class, "http://httpbin.org");

        A a = new A();
        a.name = "Kongsin";
        a.lastName = "Pansansou";
        a.age = "20";
        Caller<B> loadPost = loader.loadPost(a);
        loadPost.setReturnAs(Caller.ReturnAs.OBJECT);
        loadPost.enqueuePost(new Caller.RooFitCallBack() {
            @Override
            public void onResponse(Object object) {
                Log.i(TAG, "onResponse: " + object);
            }

            @Override
            public void onFailed(String msg) {
                Log.i(TAG, "onFailed: " + msg);
            }
        });
    }
}
