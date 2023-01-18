package ru.itmo.hungergames.selenium.pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class ModeratorReviewPage {
    @FindBy(xpath = "//h2[contains(@class, 'status')]")
    private WebElement statusElement;

    public String getStatus() {
        return this.statusElement.getText();
    }

    @FindBy(id = "ad")
    private WebElement adElement;

    public String getAdHTML() {
        return this.adElement.getAttribute("innerHTML");
    }

    @FindBy(xpath = "//button//*[contains(text(),'Одобрить')]")
    private WebElement approveButton;

    public void approve() {
        this.approveButton.click();
    }

    @FindBy(xpath = "//button//*[contains(text(),'Отклонить')]")
    private WebElement denyButton;

    public void deny() {
        this.denyButton.click();
    }
}
