package ru.itmo.hungergames.selenium.pages;

import lombok.Getter;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;
import java.util.stream.Collectors;

public class SponsorPayOrderPage {
    private final WebDriver driver;

    @Getter
    @FindBy(id = "status")
    private WebElement paymentStatus;

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

    public SponsorPayOrderPage(WebDriver driver) {
        this.driver = driver;
    }

    public static class OrderRow {
        private final WebElement row;

        public OrderRow(WebElement row) {
            this.row = row;
        }
        public WebElement getPayButton() {
            return row.findElement(By.tagName("button"));
        }

        public List<String> getDetails() {
            return row.findElements(By.tagName("mat-list-item")).stream().map(WebElement::getText).collect(Collectors.toList());
        }
        public int getSum() {
            final var sumStr = row.findElements(By.tagName("td")).get(1).getText();
            return Integer.parseInt(sumStr.substring(0, sumStr.length() - 1));
        }
    }

    public List<OrderRow> getOrderRows() {
        return this.driver.findElements(By.tagName("tr")).stream().skip(1).map(OrderRow::new).collect(Collectors.toList());
    }
}
