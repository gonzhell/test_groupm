package com.groupm.demo.gui.pages;

import com.qaprosoft.carina.core.gui.AbstractPage;
import org.openqa.selenium.WebDriver;

public class PrivacyPolicyPageLinks extends AbstractPage {

    public PrivacyPolicyPageLinks(WebDriver driver) {
        super(driver);
    }

    public String getUrl() {
        return driver.getCurrentUrl();
    }
}
