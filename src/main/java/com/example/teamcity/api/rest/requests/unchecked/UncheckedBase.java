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
    public Response read(String id) {
        if (id != null) {
            return RestAssured
                    .given()
                    .spec(spec)
                    .get(endpoint.getUrl() + "/id:" + id);
        } else {
            return RestAssured
                    .given()
                    .spec(spec)
                    .get(endpoint.getUrl());
        }
    }

    @Override
    public Response update(String id, BaseModel model) {
        String endpointUrl = endpoint.getUrl().replace("{id}", id);
        return RestAssured
                .given()
                .spec(spec)
                .body(model)
                .put(endpoint.getUrl() + "/id:" + id);
    }

    @Override
    public Response delete(String id) {
        return RestAssured
                .given()
                .spec(spec)
                .delete(endpoint.getUrl() + "/id:" + id);
    }

}
