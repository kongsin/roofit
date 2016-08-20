package com.example.kongsin.lesscode;

/**
 * Created by kongsin on 8/20/16.
 */

public interface APILoader {
    @URL(url = "/catalog/1044/books?c=TH")
    CallReq<A> loadData();
}
