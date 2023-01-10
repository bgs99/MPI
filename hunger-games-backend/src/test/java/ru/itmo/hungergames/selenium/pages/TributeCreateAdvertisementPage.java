package ru.itmo.hungergames.selenium.pages;

import lombok.Getter;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

@Getter
public class TributeCreateAdvertisementPage {

    @FindBy(xpath = "//div[contains(@class, 'angular-editor-textarea')]")
    private WebElement textArea;

    @FindBy(xpath = "//button//*[contains(text(),'Оплатить')]")
    private WebElement payButton;

    @FindBy(xpath = "//div[contains(@class, 'payment-field')]")
    private WebElement total;

    public int getTotal() {
        final var sumStr = this.total.getText();
        return Integer.parseInt(sumStr.substring(0, sumStr.length() - 1));
    }

}
