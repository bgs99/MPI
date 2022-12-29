package ru.itmo.hungergames.selenium.util;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class Utils {
    public static void redirectWait(WebDriver driver, String originalUrl) {
        redirectWait(driver, originalUrl, Duration.ofSeconds(1));
    }

    public static void redirectWait(WebDriver driver, String originalUrl, Duration timeout) {
        new WebDriverWait(driver, timeout).until(
                ExpectedConditions.not(
                        ExpectedConditions.urlToBe(originalUrl)));
    }
}
