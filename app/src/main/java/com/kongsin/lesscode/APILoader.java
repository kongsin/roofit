package com.kongsin.lesscode;

import com.kongsin.roofit.Caller;
import com.kongsin.roofit.POST;
import com.kongsin.roofit.ReqObject;
import com.kongsin.roofit.GET;

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
