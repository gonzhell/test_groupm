package com.groupm.demo;

import com.groupm.demo.gui.pages.PrivacyPolicyPageLinks;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.qaprosoft.carina.core.foundation.AbstractTest;
import com.groupm.demo.gui.pages.PrivacyPolicyPage;
import com.groupm.demo.gui.pages.HomePage;
import org.testng.asserts.SoftAssert;

public class WebSampleTest extends AbstractTest {

    @Test
    public void testPolicyPages() {
        SoftAssert softAssertion= new SoftAssert();
        WebDriver driver = getDriver();
        HomePage homePage = new HomePage(driver);
        homePage.open();
        Assert.assertTrue(homePage.isPageOpened(), "Home page is not opened");
        PrivacyPolicyPage privatePolicyPage = homePage.openPrivatePolicy();
        for (String policyLinkName : privatePolicyPage.getAllNameLinks()) {
            PrivacyPolicyPageLinks privacyPolicyPageLink = privatePolicyPage.clickPolicyLink(policyLinkName);
            softAssertion.assertEquals(privacyPolicyPageLink.getUrl(), policyLinkName);
            privatePolicyPage.restore();
        }
        softAssertion.assertAll();
    }

}
