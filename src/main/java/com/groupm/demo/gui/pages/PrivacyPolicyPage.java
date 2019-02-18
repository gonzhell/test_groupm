package com.groupm.demo.gui.pages;

import java.util.LinkedList;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

import com.qaprosoft.carina.core.gui.AbstractPage;
import com.qaprosoft.carina.core.foundation.webdriver.decorator.ExtendedWebElement;

public class PrivacyPolicyPage extends AbstractPage {
    @FindBy(xpath = "//table[@class=\"MsoTableGrid\"]//a")
    private List<ExtendedWebElement> policyLinks;
    String url;

    public PrivacyPolicyPage(WebDriver driver) {
        super(driver);
        url = getUrl();
    }

    public String getUrl() {
        return driver.getCurrentUrl();
    }

    public List<String> getAllNameLinks() {
        LinkedList<String> res = new LinkedList<>();
        for (ExtendedWebElement elem : policyLinks) {
            res.add(elem.getAttribute("text"));
        }
        return res;
    }

    public PrivacyPolicyPageLinks clickPolicyLink(String linkName) {
        for (ExtendedWebElement elem : policyLinks) {
            if (elem.getAttribute("text").equals(linkName)) {
                elem.click();
                return new PrivacyPolicyPageLinks(driver);
            }
        }
        return null;
    }

    public void restore() {
        driver.get(url);
    }

}
