package com.example.do_an_android.Retrofit2;

import com.example.do_an_android.Model.Server;

public class APIUtils {
    public static DataClient getData()
    {
        return RetrofitClient.getClient(Server.urlBase).create(DataClient.class);
    }
}
