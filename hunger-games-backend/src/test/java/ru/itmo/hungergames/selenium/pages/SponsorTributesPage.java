package ru.itmo.hungergames.selenium.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;
import java.util.stream.Collectors;

public class SponsorTributesPage {
    @FindBy(tagName = "tr")
    private List<WebElement> tributeElements;

    public static class TributeRow {
        private final WebElement row;

        public TributeRow(WebElement row) {
            this.row = row;
        }

        public WebElement getSelectButton() {
            return row.findElement(By.tagName("button"));
        }

        public int getDistrict() {
            return Integer.parseInt(row.findElements(By.tagName("td")).get(1).getText());
        }

        public String getName() {
            return row.findElements(By.tagName("td")).get(0).getText();
        }
    }

    public List<TributeRow> getTributeRows() {
        return this.tributeElements.stream().skip(1).map(TributeRow::new).collect(Collectors.toList());
    }

    public TributeRow getRowByTributeName(String tributeName) {
        return this.tributeElements.stream().skip(1).map(TributeRow::new).filter(row -> row.getName().equals(tributeName)).findFirst().orElseThrow();
    }
}
