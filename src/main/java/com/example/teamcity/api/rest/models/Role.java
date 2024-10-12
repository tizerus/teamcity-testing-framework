package com.example.teamcity.api.rest.models;

import com.example.teamcity.api.rest.annotations.Parameterizable;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import static com.example.teamcity.api.rest.enums.UserRoles.SYSTEM_ADMIN;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Role extends BaseModel {

    @Builder.Default
    @Parameterizable
    private String roleId = SYSTEM_ADMIN.name();
    @Builder.Default
    @Parameterizable
    private String scope = "g";

}
