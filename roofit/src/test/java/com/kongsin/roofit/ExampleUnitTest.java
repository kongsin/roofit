package com.kongsin.roofit;

import org.junit.Test;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {

    ExampleUnitTest(){

    }

    @Test
    public void addition_isCorrect() throws Exception {
        RooFit myFit = new RooFit();
        TESTAPIInterface test = myFit.create(TESTAPIInterface.class, "http://me-catalogsvc.azurewebsites.net/");
        test.loadData(null).enqueue(new Caller.RooFitCallBack() {
            @Override
            public void onResponse(Object object) {

            }

            @Override
            public void onFailed(String msg) {

            }
        });
    }
}