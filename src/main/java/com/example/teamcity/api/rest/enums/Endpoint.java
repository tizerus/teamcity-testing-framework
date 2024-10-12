package com.example.teamcity.api.rest.enums;

import com.example.teamcity.api.rest.models.BaseModel;
import com.example.teamcity.api.rest.models.BuildType;
import com.example.teamcity.api.rest.models.Project;
import com.example.teamcity.api.rest.models.Roles;
import com.example.teamcity.api.rest.models.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Endpoint {

    BUILD_TYPES("app/rest/buildTypes", BuildType .class),
    PROJECTS("/app/rest/projects", Project.class),
    USERS("/app/rest/users", User.class),
    ROLES("/app/rest/users/id:{id}/roles", Roles.class);

    private final String url;
    private final Class<? extends BaseModel> modelClass;

}
