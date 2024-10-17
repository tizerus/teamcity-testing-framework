package com.example.teamcity.api.rest.requests.unchecked;

import com.example.teamcity.api.rest.enums.Endpoint;
import com.example.teamcity.api.rest.models.BaseModel;
import com.example.teamcity.api.rest.requests.CrudInterface;
import com.example.teamcity.api.rest.requests.Request;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.util.List;

public class UncheckedBase extends Request implements CrudInterface {

    public UncheckedBase(RequestSpecification spec, Endpoint endpoint) {
        super(spec, endpoint);
    }

    @Override
    public Response create(BaseModel model) {
        return RestAssured
                .given()
                .spec(spec)
                .body(model)
                .post(endpoint.getUrl());
    }

    @Override
    public Response read(String locator) {
        if (locator != null) {
            return RestAssured
                    .given()
                    .spec(spec)
                    .get(endpoint.getUrl() + "/" + locator);
        } else {
            return RestAssured
                    .given()
                    .spec(spec)
                    .get(endpoint.getUrl());
        }
    }

    @Override
    public Response update(String locator, BaseModel model) {
        return RestAssured
                .given()
                .spec(spec)
                .body(model)
                .put(endpoint.getUrl() + "/" + locator);
    }

    @Override
    public Response delete(String locator) {
        return RestAssured
                .given()
                .spec(spec)
                .delete(endpoint.getUrl() + "/" + locator);
    }

}
