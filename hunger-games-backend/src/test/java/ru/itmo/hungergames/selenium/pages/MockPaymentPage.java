package ru.itmo.hungergames.selenium.pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class MockPaymentPage {
    @FindBy(xpath = "//button//*[contains(text(),'Провести')]")
    private WebElement approveButton;

    @FindBy(xpath = "//button//*[contains(text(),'Отклонить')]")
    private WebElement denyButton;

    public void approve() {
        this.approveButton.click();
    }

    public void deny() {
        this.denyButton.click();
    }
}
