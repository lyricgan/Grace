package com.lyricgan.grace.network.okhttp;

import okhttp3.Response;

public class HttpResponse {
    private Response response;

    public HttpResponse(Response response) {
        this.response = response;
    }

    public Response getResponse() {
        return response;
    }
}
