package com.example.teamcity.api;

import com.example.teamcity.api.rest.models.BuildType;
import com.example.teamcity.api.rest.models.Project;
import com.example.teamcity.api.rest.models.Roles;
import com.example.teamcity.api.rest.models.User;
import com.example.teamcity.api.rest.requests.CheckedRequests;
import com.example.teamcity.api.rest.requests.UncheckedRequests;
import com.example.teamcity.api.rest.requests.unchecked.UncheckedBase;
import com.example.teamcity.api.rest.spec.Specifications;
import org.apache.http.HttpStatus;
import org.hamcrest.Matchers;
import org.testng.annotations.Test;

import java.util.Arrays;

import static com.example.teamcity.api.rest.enums.Endpoint.BUILD_TYPES;
import static com.example.teamcity.api.rest.enums.Endpoint.PROJECTS;
import static com.example.teamcity.api.rest.enums.Endpoint.USERS;
import static com.example.teamcity.api.rest.generators.TestDataGenerator.generate;
import static io.qameta.allure.Allure.step;

@Test(groups = {"Regression"})
public class BuildTypeTest extends BaseApiTest {

    @Test(description = "User should be able to create build type", groups = {"Positive", "CRUD"})
    public void userCreatesBuildTypeTest() {
        superUserCheckRequests.getRequest(USERS).create(testData.getUser());
        var userCheckRequests = new CheckedRequests(Specifications.authSpec(testData.getUser()));
        userCheckRequests.<Project>getRequest(PROJECTS).create(testData.getProject());
        userCheckRequests.getRequest(BUILD_TYPES).create(testData.getBuildType());
        var createdBuildType = userCheckRequests.<BuildType>getRequest(BUILD_TYPES)
                .read("id:" + testData.getBuildType().getId());
        softy.assertEquals(testData.getBuildType()
                .getName(), createdBuildType.getName(), "Build type name is not correct");
    }

    @Test(description = "User should not be able to create two build types with the same id", groups = {"Negative",
            "CRUD"})
    public void userCreatesTwoBuildTypesWithTheSameIdTest() {
        var buildTypeWithSameId = generate(Arrays.asList(testData.getProject()), BuildType.class,
                testData.getBuildType()
                .getId());
        superUserCheckRequests.getRequest(USERS).create(testData.getUser());
        var userCheckRequests = new CheckedRequests(Specifications.authSpec(testData.getUser()));
        userCheckRequests.<Project>getRequest(PROJECTS).create(testData.getProject());
        userCheckRequests.getRequest(BUILD_TYPES).create(testData.getBuildType());
        new UncheckedBase(Specifications.authSpec(testData.getUser()), BUILD_TYPES)
                .create(buildTypeWithSameId)
                .then().assertThat().statusCode(HttpStatus.SC_BAD_REQUEST)
                .body(Matchers.containsString(("The build configuration / template ID \"%s\" is already used by "
                                                       + "another configuration or template").formatted(testData.getBuildType()
                        .getId())));
    }

    @Test(description = "Project admin should be able to create build type for their project",
          groups = {"Positive", "Roles"})
    public void projectAdminCreatesBuildTypeTest() {
        step("Create project");
        superUserCheckRequests.getRequest(PROJECTS).create(testData.getProject());
        testData.getUser().setRoles(generate(Roles.class, "PROJECT_ADMIN", "p:" + testData.getProject().getId()));
        step("Assign user on the project");
        superUserCheckRequests.<User>getRequest(USERS).create(testData.getUser());
        var projectAdmin = new CheckedRequests(Specifications.authSpec(testData.getUser()));
        step("Create Build_Type as Project_Admin");
        projectAdmin.getRequest(BUILD_TYPES).create(testData.getBuildType());
        step("Check that build type was created");
        var createdBuildType = superUserCheckRequests.<BuildType>getRequest(BUILD_TYPES)
                .read("id:" + testData.getBuildType().getId());
        softy.assertEquals(testData.getBuildType()
                .getName(), createdBuildType.getName(), "Build type name is not correct");
    }

    @Test(description = "Project admin should not be able to create build type for not their project", groups = {
            "Negative", "Roles"})
    public void projectAdminCreatesBuildTypeForAnotherUserProjectTest() {
        step("Create project1");
        superUserCheckRequests.<Project>getRequest(PROJECTS).create(testData.getProject());
        testData.getUser().setRoles(generate(Roles.class, "PROJECT_ADMIN", "p:" + testData.getProject().getId()));
        step("Assign user1 on the project1");
        superUserCheckRequests.<User>getRequest(USERS).create(testData.getUser());
        step("Create project2");
        var anotherProject = superUserCheckRequests.<Project>getRequest(PROJECTS).create(generate(Project.class));
        step("Assign user2 on the project2");
        var user2 = generate(User.class);
        user2.setRoles(generate(Roles.class, "PROJECT_ADMIN", "p:" + anotherProject.getId()));
        superUserCheckRequests.<User>getRequest(USERS).create(user2);
        step("Trying to create Build Type for another project");
        var user1Request = new UncheckedRequests(Specifications.authSpec(testData.getUser()));
        testData.getBuildType().getProject().setId(anotherProject.getId());
        testData.getBuildType().getProject().setName(anotherProject.getName());
        user1Request.getRequest(BUILD_TYPES).create(testData.getBuildType())
                .then()
                .assertThat()
                .statusCode(403);
    }

}
