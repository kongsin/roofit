package com.example.kongsin.lesscode;

import com.example.roofit.Caller;
import com.example.roofit.URL;
import com.example.roofit.PATH;

/**
 * Created by kongsin on 8/20/16.
 */

public interface APILoader {

    @URL(url = "/catalog/1044/books?c=TH")
    Caller<A> loadData();

    @URL(url = "/book/{id}/{name}")
    Caller<A> getDetail(@PATH("id") String id, @PATH("name") String name);
}
