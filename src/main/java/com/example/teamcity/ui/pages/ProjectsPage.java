package com.example.teamcity.ui.pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import com.example.teamcity.api.rest.models.Project;
import com.example.teamcity.ui.elements.ProjectElement;

import java.rmi.NoSuchObjectException;
import java.util.List;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class ProjectsPage extends BasePage {

    public static final String PROJECTS_URL = "/favorite/projects";
    private ElementsCollection projectElements = $$("div[class *= 'Subproject__container']");
    private SelenideElement spanFavoriteProjects = $("span[class='ProjectPageHeader']");
    private SelenideElement header = $(".MainPanel__router--gF > div");

    public ProjectsPage() {
        header.shouldBe(Condition.visible, BASE_WAITING);
    }

    public static ProjectsPage open() {
        return Selenide.open(PROJECTS_URL, ProjectsPage.class);
    }

    public List<ProjectElement> getProjects() {
        return generatePageElements(projectElements, ProjectElement::new);
    }

    public ProjectPage editProject(Project project) throws NoSuchObjectException {
        getProjects().stream()
                .filter(p -> p.getName().getText().equals(project.getName()))
                .findFirst()
                .orElseThrow(() -> new NoSuchObjectException("Cannot find project name: %s".formatted(project.getName())))
                .getLink()
                .click();
        return Selenide.page(ProjectPage.class);
    }

}
