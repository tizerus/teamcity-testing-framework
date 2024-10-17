package com.example.teamcity.ui;

import com.codeborne.selenide.Condition;
import com.example.teamcity.api.rest.enums.Endpoint;
import com.example.teamcity.api.rest.models.Project;
import com.example.teamcity.api.rest.requests.UncheckedRequests;
import com.example.teamcity.api.rest.spec.Specifications;
import com.example.teamcity.ui.pages.ProjectPage;
import com.example.teamcity.ui.pages.ProjectsPage;
import com.example.teamcity.ui.pages.admin.CreateProjectPage;
import org.testng.annotations.Test;

import static com.example.teamcity.api.rest.enums.Endpoint.PROJECTS;
import static com.example.teamcity.ui.pages.admin.CreateProjectPage.PROJECT_NAME_ERROR;

@Test(groups = {"Regression"})
public class CreateProjectTest extends BaseUiTest {

    private static final String REPO_URL = "https://github.com/AlexPshe/spring-core-for-qa";
    private static final String PROJECT_NAME_MUST_NOT_BE_EMPTY = "Project name must not be empty";

    @Test(description = "User should be able to create project", groups = {"Positive"})
    public void userCreatesProject() {
        loginAs(testData.getUser());
        // взаимодействие с UI
        CreateProjectPage.open("_Root")
                .createForm(REPO_URL)
                .setupProject(testData.getProject().getName(), testData.getBuildType().getName());
        // проверка состояния API
        // (корректность отправки данных с UI на API)
        var createdProject = superUserCheckRequests.<Project>getRequest(Endpoint.PROJECTS).read("name:" + testData.getProject().getName());
        softy.assertNotNull(createdProject);
        // проверка состояния UI
        // (корректность считывания данных и отображение данных на UI)
        ProjectPage.open(createdProject.getId())
                .title.shouldHave(Condition.exactText(testData.getProject().getName()));
        var foundProjects = ProjectsPage.open()
                .getProjects().stream()
                .anyMatch(project -> project.getName().getText().equals(testData.getProject().getName()));
        softy.assertTrue(foundProjects);
    }

    @Test(description = "User should not be able to create project without name", groups = {"Negative"})
    public void userCreatesProjectWithoutName() {
        loginAs(testData.getUser());
        var projectListSizeExpected = new UncheckedRequests(Specifications.superUserSpec())
                .<Project>getRequest(PROJECTS)
                .read(null)
                .jsonPath()
                .getList("project.id")
                .size();
        CreateProjectPage.open("_Root")
                .createForm(REPO_URL)
                .setupProject("", testData.getBuildType().getName())
                .checkErrorText(PROJECT_NAME_ERROR, PROJECT_NAME_MUST_NOT_BE_EMPTY);
        var projectListSizeActual = new UncheckedRequests(Specifications.superUserSpec())
                .<Project>getRequest(PROJECTS)
                .read(null)
                .jsonPath()
                .getList("project.id")
                .size();
        softy.assertEquals(projectListSizeActual, projectListSizeExpected);
    }

}
