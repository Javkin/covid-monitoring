package com.covid.monitoring.util;

import org.apache.http.HttpHeaders;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;

import java.util.Map;
import java.util.Objects;

public class HttpUtil {

    public HttpUriRequest createRequest(String url, Map<String, String> params) {
        RequestBuilder requestBuilder = RequestBuilder.get()
                .setUri(url)
                .setHeader(HttpHeaders.CONTENT_TYPE, "application/json");

        if (Objects.nonNull(params)) {
            params.forEach(requestBuilder::addParameter);
        }

        return requestBuilder.build();
    }
}
