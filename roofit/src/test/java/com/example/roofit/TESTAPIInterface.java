package com.example.roofit;

/**
 * Created by kongsin on 8/21/16.
 */

public interface TESTAPIInterface {
    @GET(url = "/book/{id}")
    Caller<A> loadData(@PATH("id") String id);

    @GET(url = "/book/{id}")
    Caller<A> loadData1(@PATH("id") String name);

    @GET(url = "/book/{id}")
    Caller<A> loadData2(@PATH("id") String age);

    @GET(url = "/book/{id}")
    Caller<A> loadData3(String b, @PATH("id") String id);

    @GET(url = "/book/{id}")
    Caller<A> loadData4(@PATH("id") String id);
}
