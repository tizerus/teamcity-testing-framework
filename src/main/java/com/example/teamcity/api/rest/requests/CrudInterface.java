package com.example.teamcity.api.rest.requests;

import com.example.teamcity.api.rest.models.BaseModel;

public interface CrudInterface {

    Object create(BaseModel baseModel);
    Object read(BaseModel baseModel);
    Object update(BaseModel baseModel);
    Object delete(BaseModel baseModel);

}
