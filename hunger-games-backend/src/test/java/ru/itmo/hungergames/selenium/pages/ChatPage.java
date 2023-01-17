package ru.itmo.hungergames.selenium.pages;

import lombok.Getter;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

public class ChatPage {
    public static class Message {
        private final WebElement element;

        public Message(WebElement element) {
            this.element = element;
        }

        public String getText() {
            return this.element.findElement(By.tagName("p")).getText();
        }

        public String getSender() {
            return this.element.findElement(By.tagName("mat-card-title")).getText();
        }

        public List<String> orderDetails() {
            var paragraphs = this.element.findElements(By.tagName("p"));
            return paragraphs.stream().limit(paragraphs.size() - 1).map(WebElement::getText).collect(Collectors.toList());
        }

        public String orderStatus() {
            var paragraphs = this.element.findElements(By.tagName("p"));
            return paragraphs.get(paragraphs.size() - 1).getText();
        }

        public BigDecimal getOrderSum() {
            var sumStr = this.element.findElement(By.xpath(".//div[contains(@class, 'message-order-sum')]")).getText();
            return new BigDecimal(Integer.parseInt(sumStr.substring(0, sumStr.length() - 1)));
        }

        public void pay() {
            var payButton = this.element.findElement(By.xpath(".//button//*[contains(text(), 'Оплатить')]"));
            payButton.click();
        }

        public void approve() {
            var payButton = this.element.findElement(By.xpath(".//button//*[contains(text(), 'Одобрить')]"));
            payButton.click();
        }

        public void deny() {
            var payButton = this.element.findElement(By.xpath(".//button//*[contains(text(), 'Отклонить')]"));
            payButton.click();
        }
    }

    @Getter
    @FindBy(xpath = "//input[@name='msg']")
    private WebElement messageInput;

    @Getter
    @FindBy(xpath = "//button[contains(text(), 'Отправить')]")
    private WebElement messageSendButton;

    @Getter
    @FindBy(xpath = "//button[contains(text(), 'Запросить ресурсы')]")
    private WebElement requestResourcesButton;

    @Getter
    @FindBy(xpath = "//button[contains(text(), 'Предложить ресурсы')]")
    private WebElement sendResourcesButton;

    @FindBy(tagName = "li")
    private List<WebElement> chatMessageElements;

    public List<Message> getMessages() {
        return this.chatMessageElements.stream().map(Message::new).collect(Collectors.toList());
    }


}
