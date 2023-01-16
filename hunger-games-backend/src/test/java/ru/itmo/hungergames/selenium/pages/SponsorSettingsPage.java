package ru.itmo.hungergames.selenium.pages;

import lombok.Getter;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.math.BigDecimal;

public class SponsorSettingsPage {

    @FindBy(xpath = "//input[@name='oldEmail']")
    private WebElement oldEmailInput;


    public String getOldEmail() {
        return this.oldEmailInput.getAttribute("value");
    }

    @Getter
    @FindBy(xpath = "//input[@name='newEmail']")
    private WebElement newEmailInput;

    @Getter
    @FindBy(xpath = "//button//*[contains(text(), 'Обновить')]")
    private WebElement changeEmailButton;


    public final static String subscriptionBlockId = "subscriptionPrice";

    @FindBy(id = subscriptionBlockId)
    private WebElement subscriptionPriceElement;

    public BigDecimal getSubscriptionPrice() {
        final var sumStr = this.subscriptionPriceElement.getText();
        return new BigDecimal(Integer.parseInt(sumStr.substring(0, sumStr.length() - 1)));
    }

    public boolean canSubscribe() {
        try {
            return this.subscriptionPriceElement.isDisplayed();
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    @FindBy(xpath = "//button//*[contains(text(), 'Подписаться')]")
    private WebElement subscribeButton;

    public void subscribe() {
        this.subscribeButton.click();
    }

    public void updateEmail(String newEmail) {
        this.newEmailInput.clear();
        this.newEmailInput.sendKeys(newEmail);
        this.changeEmailButton.click();
    }
}
