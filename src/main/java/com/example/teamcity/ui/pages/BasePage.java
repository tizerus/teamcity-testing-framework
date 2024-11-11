package com.example.teamcity.ui.pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import com.example.teamcity.ui.elements.BasePageElement;
import org.assertj.core.api.Assertions;

import java.time.Duration;
import java.util.List;
import java.util.function.Function;

public class BasePage {

    protected static final Duration BASE_WAITING = Duration.ofSeconds(30);

    public <T extends BasePageElement> List<T> generatePageElements(ElementsCollection collection,
            Function<SelenideElement, T> creator) {
        return collection.stream().map(creator).toList();
    }

    public void checkErrorText(SelenideElement element, String errorText) {
        element.shouldBe(Condition.visible, Duration.ofSeconds(30));
        Assertions.assertThat(element.getText())
                .isEqualTo(errorText);
    }

}
