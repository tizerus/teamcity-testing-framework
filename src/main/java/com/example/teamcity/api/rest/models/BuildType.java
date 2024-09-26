package com.example.teamcity.api.rest.models;

import com.example.teamcity.api.rest.annotations.Parameterizable;
import com.example.teamcity.api.rest.annotations.Random;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class BuildType extends BaseModel {

    @Random
    @Parameterizable
    private String id;
    @Random
    private String name;
    private Project project;
    private Steps steps;

}
