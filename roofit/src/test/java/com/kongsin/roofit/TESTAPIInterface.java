package com.kongsin.roofit;

/**
 * Created by kongsin on 8/21/16.
 */

public interface TESTAPIInterface {
    @GET(url = "/book/{id}")
    Caller<A> loadData(@Param("id") String id);

    @GET(url = "/book/{id}")
    Caller<A> loadData1(@Param("id") String name);

    @GET(url = "/book/{id}")
    Caller<A> loadData2(@Param("id") String age);

    @GET(url = "/book/{id}")
    Caller<A> loadData3(String b, @Param("id") String id);

    @GET(url = "/book/{id}")
    Caller<A> loadData4(@Param("id") String id);
}
