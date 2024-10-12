package com.example.teamcity.api.rest.models;

import lombok.Data;

@Data
public class TestData {

    private Project project;
    private User user;
    private BuildType buildType;

}
