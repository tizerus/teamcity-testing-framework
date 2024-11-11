package com.example.teamcity.ui;

import com.example.teamcity.api.rest.enums.Endpoint;
import com.example.teamcity.api.rest.generators.RandomData;
import com.example.teamcity.api.rest.models.Project;
import com.example.teamcity.ui.pages.ProjectPage;
import com.example.teamcity.ui.pages.admin.CreateProjectPage;
import org.assertj.core.api.Assertions;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import static com.example.teamcity.ui.pages.ProjectPage.ERROR_BUILD_TYPE_NAME;

public class CreateBuildConfigurationTest extends BaseUiTest {

    private static final String REPO_URL_2 = "https://github.com/tizerus/teamcity-testing-framework";
    public static final String BUILD_CONFIGURATION_TYPE_NAME_ERROR_MSG = "Build configuration name must not be empty";

    private Project createdProject;

    @Test(description = "User shouldn't be able to create build configuration without name", groups = {"Negative"})
    public void createBuildConfigurationWithoutNameTest() {
        loginAs(testData.getUser());
        CreateProjectPage.open("_Root")
                .createForm(REPO_URL)
                .setupProject(testData.getProject().getName(), testData.getBuildType().getName());
        createdProject = superUserCheckRequests.<Project>getRequest(Endpoint.PROJECTS).read("name:" + testData.getProject().getName());
        testData.getBuildType().setName("");
        var currentPage = ProjectPage.open(createdProject.getId())
                .editProject();
        var expectedNumberOfBuilds = currentPage.getBuilds().size();
        currentPage
                .createBuildType(REPO_URL, testData.getBuildType())
                .checkErrorText(ERROR_BUILD_TYPE_NAME, BUILD_CONFIGURATION_TYPE_NAME_ERROR_MSG);
        ProjectPage.open(createdProject.getId())
                .editProject();
        var currentNumberOfBuilds = currentPage.getBuilds().size();
        Assertions.assertThat(currentNumberOfBuilds).isEqualTo(expectedNumberOfBuilds);
    }

    @Test(description = "User should be able to create build configuration", groups = {"Positive"})
    public void createBuildConfigurationTest() {
        loginAs(testData.getUser());
        CreateProjectPage.open("_Root")
                .createForm(REPO_URL)
                .setupProject(testData.getProject().getName(), testData.getBuildType().getName());
        createdProject = superUserCheckRequests.<Project>getRequest(Endpoint.PROJECTS).read("name:" + testData.getProject().getName());
        String buildTypeNewName = RandomData.getString();
        testData.getBuildType().setName(buildTypeNewName);
        var currentPage = ProjectPage.open(createdProject.getId())
                .editProject();
        var expectedNumberOfBuilds = currentPage.getBuilds().size() + 1;
        currentPage
                .createBuildType(REPO_URL_2, testData.getBuildType());
        ProjectPage.open(createdProject.getId())
                .editProject();
        var currentNumberOfBuilds = currentPage.getBuilds().size();
        Assertions.assertThat(currentNumberOfBuilds).isEqualTo(expectedNumberOfBuilds);
        Assertions.assertThat(currentPage.getBuilds().stream().anyMatch(conf -> conf.getText().contains(buildTypeNewName)))
                .withFailMessage("Cannot find '%s'", buildTypeNewName)
                .isTrue();
    }

    @AfterMethod
    public void tearDown() {
        superUserCheckRequests.<Project>getRequest(Endpoint.PROJECTS).delete(createdProject.getId());
    }

}
