package ru.itmo.hungergames.selenium.pages;

import lombok.Getter;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import ru.itmo.hungergames.selenium.util.OrderRepresentation;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class MentorConsiderSponsorOffersPage {

    public static class OrderRow {
        private final WebElement row;

        public OrderRow(WebElement row) {
            this.row = row;
        }

        public String getTributeName() {
            return this.row.findElements(By.tagName("td")).get(0).getText();
        }

        public String getSponsorName() {
            return this.row.findElements(By.tagName("td")).get(1).getText();
        }

        private String getResourcesText() {
            return this.row.findElements(By.tagName("td")).get(2).getText();
        }

        public String getResources() {
            return processResourcesText(this.getResourcesText());
        }

        public WebElement getApproveButton() {
            return this.row.findElement(By.xpath(".//button//*[contains(text(),'Одобрить')]"));
        }

        public WebElement getDenyButton() {
            return this.row.findElement(By.xpath(".//button//*[contains(text(),'Отклонить')]"));
        }

        private String processResourcesText(String resourcesText) {
            return "[" + resourcesText.replace("\n", ", ") + "]";
        }
    }

    @FindBy(tagName = "tr")
    private List<WebElement> orderElements;

    public List<OrderRow> getOrderRows() {
        return this.orderElements.stream().skip(1).map(OrderRow::new).collect(Collectors.toList());
    }

    public OrderRow getOrder(OrderRepresentation order) {
        return this.orderElements.stream()
                .skip(1)
                .map(OrderRow::new)
                .filter(row -> row.getResourcesText().equals(order.toString()))
                .findFirst().orElseThrow();
    }

    public void approveOrder(OrderRepresentation order) {
        this.getOrder(order).getApproveButton().click();
    }
}
