package com.monstahhh.http;

import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;

public class HttpClient {

    public static String USERAGENT = "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:25.0) Gecko/20100101 Firefox/25.0";
    public static String CHARSET = "UTF-8";

    public HttpResponse request(HttpMethod method, URL url) throws IOException {
        return request(method, url.toString(), null);
    }

    public HttpResponse request(HttpMethod method, String url) throws IOException {
        return request(method, url, null);
    }

    public HttpResponse requestToGateway(HttpMethod method, String url, JSONObject object) throws IOException {
        return requestGateway(method, url, null, object);
    }

    private HttpResponse requestGateway(HttpMethod method, String url,
                                        HttpParameterSet httpParameters, JSONObject json) throws IOException {
        HttpRequest request = new HttpRequestFactory().getRequestGateway(method, url, httpParameters, json);

        return request.doRequest();
    }

    public HttpResponse request(HttpMethod method, String url,
                                HttpParameterSet httpParameters) throws IOException {
        HttpRequest request = new HttpRequestFactory().getRequest(method, url, httpParameters);

        return request.doRequest();
    }

}
