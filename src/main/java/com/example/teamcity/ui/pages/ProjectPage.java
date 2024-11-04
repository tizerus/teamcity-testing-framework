package com.example.teamcity.ui.pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.Selectors;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import com.example.teamcity.api.rest.models.BuildType;

import java.time.Duration;
import java.util.List;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class ProjectPage extends BasePage {

    private static final String PROJECT_URL = "/project/%s";
    public static final SelenideElement ERROR_BUILD_TYPE_NAME = $("#error_buildTypeName");

    private SelenideElement urlInput = $("#url");
    private SelenideElement proceedButton = $(Selectors.byAttribute("value", "Proceed"));
    private SelenideElement connectionSuccessful = $(Selectors.byClassName("connectionSuccessful"));
    private SelenideElement buildName = $("#buildTypeName");
    private ElementsCollection buildConfigurationElements = $$(Selectors.byXpath("//table[@id = 'configurations']//tr[position() > 1]"));
    public SelenideElement createBuildConfigurationButton = $("a[href *= 'createBuildTypeMenu']");
    public SelenideElement editProjectButton = $("a[href *= 'editProject']");
    public SelenideElement title = $("span[class *= 'ProjectPageHeader']");

    public static ProjectPage open(String projectId) {
        return Selenide.open(PROJECT_URL.formatted(projectId), ProjectPage.class);
    }

    public ProjectPage editProject() {
        editProjectButton.shouldBe(Condition.visible).click();
        return this;
    }

    public ProjectPage createBuildType(String repo, BuildType buildType) {
        createBuildConfigurationButton.shouldBe(Condition.visible).click();
        urlInput.shouldBe(Condition.visible).val(repo);
        proceedButton.click();
        connectionSuccessful.should(Condition.appear, Duration.ofSeconds(30));
        buildName.val(buildType.getName());
        proceedButton.click();
        return this;
    }

    public List<SelenideElement> getBuilds() {
        return buildConfigurationElements.stream().toList();
    }

}
