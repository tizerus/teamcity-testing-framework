package com.example.teamcity.api;

import com.example.teamcity.api.rest.requests.CheckedRequests;
import com.example.teamcity.api.rest.spec.Specifications;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.asserts.SoftAssert;

public class BaseTest {

    protected SoftAssert softAssert;
    protected CheckedRequests superUserCheckRequests = new CheckedRequests(Specifications.superUserSpec());

    @BeforeMethod(alwaysRun = true)
    public void beforeTest() {
        softAssert = new SoftAssert();
    }

    @AfterMethod(alwaysRun = true)
    public void afterTest() {
        softAssert.assertAll();
    }

}
