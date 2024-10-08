package com.example.teamcity.api.rest.requests;

import com.example.teamcity.api.rest.models.BaseModel;

import java.util.List;

public interface CrudInterface {

    Object create(BaseModel baseModel);
    Object read(String id);
    Object update(String id, BaseModel baseModel);
    Object delete(String id);

}
