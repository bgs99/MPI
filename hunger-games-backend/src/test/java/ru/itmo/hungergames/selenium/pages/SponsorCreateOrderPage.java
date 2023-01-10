package ru.itmo.hungergames.selenium.pages;

import lombok.Getter;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class SponsorCreateOrderPage extends ResourcesPage {
    @Getter
    @FindBy(xpath = "//h2[contains(text(), 'Получатель: ')]")
    private WebElement recipient;

    @FindBy(xpath = "//div[contains(@class, 'payment-field')]")
    private WebElement total;

    public int getTotal() {
        final var sumStr = this.total.getText();
        return Integer.parseInt(sumStr.substring(0, sumStr.length() - 1));
    }

    @Getter
    @FindBy(id = "status")
    private WebElement paymentStatus;

    @Getter
    @FindBy(xpath = "//button//*[contains(text(), 'Оплатить')]")
    private WebElement payButton;

    @Getter
    @FindBy(xpath = "//button//*[contains(text(), 'Вернуться')]")
    private WebElement goBackButton;

    @Getter
    @FindBy(xpath = "//button//*[contains(text(), 'Попробовать еще раз')]")
    private WebElement retryButton;

    @Getter
    @FindBy(xpath = "//button//*[contains(text(), 'Выбрать другие ресурсы')]")
    private WebElement reselectResourcesButton;

    @Getter
    @FindBy(xpath = "//button//*[contains(text(), 'Выбрать другого трибута')]")
    private WebElement reselectTributeButton;

    public SponsorCreateOrderPage(WebDriver driver) {
        super(driver);
    }
}
