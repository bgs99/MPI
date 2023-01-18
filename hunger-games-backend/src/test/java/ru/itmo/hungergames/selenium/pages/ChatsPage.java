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
        public void select() {
            row.findElement(By.tagName("button")).click();
        }
        private String lastMessageTextRaw() {
            return row.findElement(By.tagName("mat-card-content")).getText();
        }
        public String getLastMessage() {
            final var rowStr = this.lastMessageTextRaw();
            return rowStr.substring(rowStr.indexOf(": ") + 2);
        }
        public Set<String> getParticipants() {
            return Arrays.stream(this.row.findElement(By.tagName("mat-card-title")).getText().split(", ")).collect(Collectors.toSet());
        }
    }

    @FindBy(tagName = "mat-card")
    private List<WebElement> cardElements;

    private Stream<ChatRow> getChatRowsStream() {
        return this.cardElements.stream().map(ChatRow::new);
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
        this.getChatRowByParticipants(participants).select();
    }
}
