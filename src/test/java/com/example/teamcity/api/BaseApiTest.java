package com.example.teamcity.api;

import com.example.teamcity.BaseTest;
import com.example.teamcity.api.rest.enums.UserRoles;
import com.example.teamcity.api.rest.generators.DataGenerator;
import com.example.teamcity.api.rest.models.Role;
import com.example.teamcity.api.rest.models.Roles;
import com.example.teamcity.api.rest.models.User;
import com.example.teamcity.api.rest.models.Users;
import com.example.teamcity.api.rest.spec.Specifications;
import io.restassured.RestAssured;
import lombok.NonNull;
import org.apache.commons.text.RandomStringGenerator;

import java.util.ArrayList;
import java.util.List;

import static com.example.teamcity.api.rest.enums.Endpoint.USERS;

public class BaseApiTest extends BaseTest {

    protected static final String USER_NAME = "admin";
    protected static final String PASSWORD = "admin";
    protected static final String AUTHENTICATION_CSRF_TOKEN = "/authenticationTest.html?crsf";

    public User getUserByName(String userName) {
        return RestAssured
                .given()
                .spec(Specifications.superUserSpec())
                .get(USERS.getUrl())
                .getBody()
                .as(Users.class)
                .getUsers()
                .stream()
                .filter(user -> user.getUsername().equalsIgnoreCase(userName))
                .findFirst()
                .orElse(null);
    }

    public User getUserWithRoles(@NonNull UserRoles... userRoles) {
        RandomStringGenerator name = DataGenerator.getRandomAsciiChars(Character::isLetter, Character::isUpperCase);
        RandomStringGenerator password = DataGenerator.getRandomAsciiChars(Character::isLetter, Character::isUpperCase, Character::isDigit);

        List<Role> roleList = new ArrayList<>();
        for (UserRoles role : userRoles) {
            Role newRole = Role.builder().roleId(role.name()).build();
            roleList.add(newRole);
        }
        var roles = Roles.builder().role(roleList).build();
        return User.builder()
                .username(userRoles[0] + "_" + name.generate(8))
                .password(password.generate(8, 16))
                .roles(roles)
                .build();
    }

}
