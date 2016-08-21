package com.example.kongsin.lesscode;

import com.example.roofit.Caller;
import com.example.roofit.POST;
import com.example.roofit.ReqObject;
import com.example.roofit.GET;
import com.example.roofit.Param;

/**
 * Created by kongsin on 8/20/16.
 */

public interface APILoader {

    @GET(url = "/catalog/1044/books?c=TH")
    Caller<A> loadData();

    @POST(url = "/book")
    Caller<A> getDetail(@ReqObject A reqObject);

    @POST(url = "/post")
    Caller<B> loadPost(@ReqObject A reqObject);
}
