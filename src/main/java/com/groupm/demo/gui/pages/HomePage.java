package com.groupm.demo.gui.pages;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

import com.qaprosoft.carina.core.foundation.webdriver.decorator.ExtendedWebElement;
import com.qaprosoft.carina.core.gui.AbstractPage;

public class HomePage extends AbstractPage {
    Logger LOGGER = Logger.getLogger(HomePage.class);

    @FindBy(xpath = "//a[text()=\"GroupM Privacy Policy\"]")
    private ExtendedWebElement pivatePolicyLink;

    public HomePage(WebDriver driver) {
        super(driver);
    }

    public PrivacyPolicyPage openPrivatePolicy() {
        LOGGER.info("going to thr Private Policy page");
        this.pivatePolicyLink.click();
        return new PrivacyPolicyPage(driver);
    }
}
