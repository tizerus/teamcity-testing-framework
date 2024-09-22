package com.example.teamcity.api.rest.models;

import com.example.teamcity.api.rest.annotations.Random;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BuildType extends BaseModel {

    @Random
    private String id;
    @Random
    private String name;
    private Project project;
    private Steps steps;

}
