package com.wipro.aboutcanada.model;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by admin on 30-Aug-16.
 */
public class ServiceCallSource{


    /**
     *getResponseFromServiceURL retrieves the Service Response from the service
     * @param url
     * @return Webservice response -- in String Format
     * @throws IOException
     */
    public String getResponseFromServiceURL(String url) throws IOException {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(url)
                .build();

        Response response = client.newCall(request).execute();
        return response.body().string();
    }
}
