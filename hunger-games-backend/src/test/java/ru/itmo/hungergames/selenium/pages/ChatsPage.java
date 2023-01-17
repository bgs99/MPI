package ru.itmo.hungergames.selenium.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ChatsPage {
    public static class ChatRow {
        private final WebElement row;

        public ChatRow(WebElement row) {
            this.row = row;
        }
        public WebElement getSelectButton() {
            return row.findElement(By.tagName("button"));
        }
        public String getLastMessage() {
            return row.findElements(By.tagName("p")).get(0).getText();
        }
        public Set<String> getParticipants() {
            return Arrays.stream(this.getSelectButton().getText().split(", ")).collect(Collectors.toSet());
        }
    }

    @FindBy(tagName = "mat-list-item")
    private List<WebElement> messageElements;

    private Stream<ChatRow> getChatRowsStream() {
        return this.messageElements.stream().map(ChatRow::new);
    }

    public List<ChatRow> getChatRows() {
        return this.getChatRowsStream().collect(Collectors.toList());
    }

    public ChatRow getChatRowByParticipants(Set<String> participants) {
        return this.getChatRowsStream()
                .filter(row -> participants.containsAll(row.getParticipants()))
                .findFirst().orElseThrow();
    }

    public void selectChatByParticipants(Set<String> participants) {
        this.getChatRowByParticipants(participants).getSelectButton().click();
    }
}
