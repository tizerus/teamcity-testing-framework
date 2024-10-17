package com.example.teamcity.ui.pages;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import com.example.teamcity.api.rest.models.User;

import static com.codeborne.selenide.Selenide.$;

public class LoginPage extends BasePage {

    public static final String LOGIN_HTML = "/login.html";
    private final SelenideElement userNameInput = $("#username");
    private final SelenideElement passwordInput = $("#password");
    private final SelenideElement inputSubmitLogin = $(".loginButton");


    public static LoginPage open() {
        return Selenide.open(LOGIN_HTML, LoginPage.class);
    }

    public ProjectsPage login(User user) {
        userNameInput.val(user.getUsername());
        passwordInput.val(user.getPassword());
        inputSubmitLogin.click();
        return new ProjectsPage();
    }

}
