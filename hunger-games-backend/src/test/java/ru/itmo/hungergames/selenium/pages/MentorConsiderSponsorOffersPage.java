package ru.itmo.hungergames.selenium.pages;

import lombok.Getter;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class MentorConsiderSponsorOffersPage {
    private final WebDriver driver;

    public MentorConsiderSponsorOffersPage(WebDriver driver) {
        this.driver = driver;
    }

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

        public String getResources() {
            return processResourcesText(this.row.findElements(By.tagName("td")).get(2).getText());
        }

        private String processResourcesText(String resourcesText) {
            return "[" + resourcesText.replace("\n", ", ") + "]";
        }

//        public WebElement getApproveButton() {
//            return this.row.findElement(By.xpath("//button//*[contains(text(),'Одобрить')]"));
//        } REDO
//
//        public WebElement getDenyButton() {
//            return this.row.findElement(By.xpath("//button//*[contains(text(),'Отклонить')]"));
//        } REDO
    }

    public List<OrderRow> getOrderRows() {
        return this.driver.findElements(By.tagName("tr")).stream().skip(1).map(OrderRow::new).collect(Collectors.toList());
    }
}
