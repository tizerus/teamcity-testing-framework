package com.example.teamcity.api.rest.requests;

import io.restassured.specification.RequestSpecification;

public class Request {

    private final RequestSpecification spec;
    private final Endpoint endpoint;

    public Request(RequestSpecification spec, Endpoint endpoint) {
        this.spec = spec;
        this.endpoint = endpoint;
    }

}
