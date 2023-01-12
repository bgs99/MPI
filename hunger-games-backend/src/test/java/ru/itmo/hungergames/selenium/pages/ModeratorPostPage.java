package ru.itmo.hungergames.selenium.pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import lombok.Getter;

public class ModeratorPostPage {
    @Getter
    @FindBy(xpath = "//div[label[mat-label[text()='Заголовок']]]/input")
    private WebElement titleInput;

    @Getter
    @FindBy(tagName = "angular-editor")
    private WebElement postEditorElement;

    @Getter
    @FindBy(xpath = "//button//*[contains(text(),'Опубликовать')]")
    private WebElement publishButton;
}
