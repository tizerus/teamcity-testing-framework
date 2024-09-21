package com.example.teamcity.api;

import com.example.teamcity.api.rest.models.User;
import com.example.teamcity.api.rest.spec.Specifications;
import io.restassured.RestAssured;
import org.apache.http.HttpStatus;
import org.testng.annotations.Test;

public class BuildConfigurationTest extends BaseApiTest {

    @Test
    public void buildConfigurationTest(){
        var user = User.builder()
                .username(USER_NAME)
                .password(PASSWORD)
                .build();

        var token = RestAssured
                .given()
                .spec(Specifications.getSpec().authSpec(user))
                .get(AUTHENTICATION_CSRF_TOKEN)
                .then().assertThat().statusCode(HttpStatus.SC_OK)
                .extract().asString();

        System.out.println(token);
    }

}
