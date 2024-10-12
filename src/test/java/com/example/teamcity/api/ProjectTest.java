package com.example.teamcity.api;

import com.example.teamcity.api.rest.models.Project;
import com.example.teamcity.api.rest.requests.UncheckedRequests;
import com.example.teamcity.api.rest.spec.Specifications;
import org.apache.http.HttpStatus;
import org.assertj.core.api.Assertions;
import org.hamcrest.Matchers;
import org.testng.annotations.Test;

import java.util.List;

import static com.example.teamcity.api.rest.enums.Endpoint.PROJECTS;
import static io.qameta.allure.Allure.step;
import static com.example.teamcity.api.rest.generators.TestDataGenerator.generate;

public class ProjectTest extends BaseApiTest {

    @Test(description = "Checking post, put, delete methods", groups = {
            "smoke", "Project"})
    public void postPutDeleteTest() {
        step("Create project");
        var expectedProject = superUserCheckRequests.<Project>getRequest(PROJECTS).create(testData.getProject());
        step("Read project");
        var currentProject = superUserCheckRequests.<Project>getRequest(PROJECTS).read(expectedProject.getId());
        Assertions.assertThat(currentProject)
                .as("Checking post method")
                .isEqualTo(expectedProject);
        step("Update project");
        var updatedProject = generate(Project.class);
        new UncheckedRequests(Specifications.superUserSpec())
                .getRequest(PROJECTS)
                .update(expectedProject.getId(), updatedProject)
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_METHOD_NOT_ALLOWED);
        step("Delete project");
        new UncheckedRequests(Specifications.superUserSpec())
                .getRequest(PROJECTS)
                .delete(currentProject.getId())
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_NO_CONTENT);
        step("Check that there is no project with deleted id");
        new UncheckedRequests(Specifications.superUserSpec())
                .getRequest(PROJECTS)
                .read(currentProject.getId())
                .then()
                .statusCode(HttpStatus.SC_NOT_FOUND);
    }

    @Test(description = "Creating multiple projects, then deleting them one by one", groups = {
            "smoke", "Project"})
    public void createMultipleProjectsTest() {
        step("Create and check multiple projects");
        var project1 = superUserCheckRequests.<Project>getRequest(PROJECTS).create(testData.getProject());
        var project2 = superUserCheckRequests.<Project>getRequest(PROJECTS).create(generate(Project.class));
        var project3 = superUserCheckRequests.<Project>getRequest(PROJECTS).create(generate(Project.class));
        List<String> expectedProjects = List.of(project1.getId(), project2.getId(), project3.getId());
        List<String> projectIdsList = new UncheckedRequests(Specifications.superUserSpec())
                .<Project>getRequest(PROJECTS)
                .read(null)
                .jsonPath()
                .getList("project.id");
        Assertions.assertThat(projectIdsList).containsAll(expectedProjects);
    }

    @Test(description = "Deleting not existing project", groups = {
            "smoke", "Project"})
    public void deleteNotExistingProject() {
        new UncheckedRequests(Specifications.superUserSpec())
                .getRequest(PROJECTS)
                .delete(testData.getProject().getId())
                .then()
                .statusCode(HttpStatus.SC_NOT_FOUND)
                .body(Matchers.containsString(("Project cannot be found by external id '%s'.\n"
                                                       + "Could not find the entity requested. Check the reference is "
                                                       + "correct and the user has permissions to access the entity.")
                        .formatted(testData.getProject().getId())));
    }

    @Test(description = "Create project with existing Name", groups = {
            "smoke", "Project"})
    public void createProjectWithExistingName() {
        new UncheckedRequests(Specifications.superUserSpec())
                .getRequest(PROJECTS)
                .create(testData.getProject())
                .then()
                .statusCode(HttpStatus.SC_OK);
        new UncheckedRequests(Specifications.superUserSpec())
                .getRequest(PROJECTS)
                .create(testData.getProject())
                .then()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .body(Matchers.containsString(("Project with this name already exists: %s")
                        .formatted(testData.getProject().getName())));
    }

}
