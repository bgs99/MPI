package ru.itmo.hungergames.selenium.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.html5.LocalStorage;
import org.openqa.selenium.html5.WebStorage;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.Ordered;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.support.AbstractTestExecutionListener;


import java.time.Duration;

import static org.springframework.core.annotation.AnnotationUtils.findAnnotation;

public class SeleniumTestExecutionListener extends AbstractTestExecutionListener {

    private WebDriver webDriver;

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }

    @Override
    public void prepareTestInstance(TestContext testContext) {
        if (webDriver != null) {
            return;
        }
        ApplicationContext context = testContext.getApplicationContext();
        if (context instanceof ConfigurableApplicationContext) {
            var options = new FirefoxOptions().setHeadless(true); // TODO: separate profile (debug?) for non-headless mode
            webDriver = new FirefoxDriver(options);
            webDriver.manage().timeouts().implicitlyWait(Duration.ofSeconds(3));

            ConfigurableApplicationContext configurableApplicationContext = (ConfigurableApplicationContext) context;
            ConfigurableListableBeanFactory bf = configurableApplicationContext.getBeanFactory();
            bf.registerResolvableDependency(WebDriver.class, webDriver);
        }
    }

    @Override
    public void beforeTestMethod(TestContext testContext) {
        webDriver.get("http://localhost:42322/"); // To get to the localStorage for the site
        TestAuthData authAnnotation = findAnnotation(testContext.getTestMethod(), TestAuthData.class);
        if (authAnnotation != null) {
            LocalStorage local = ((WebStorage) webDriver).getLocalStorage();
            ObjectMapper mapper = new ObjectMapper();
            var node = mapper.createObjectNode();
            node.put("id", authAnnotation.id());
            node.put("role", authAnnotation.role().asInt());
            node.put("token", authAnnotation.token());
            node.put("name", authAnnotation.name());
            try {
                local.setItem("auth", mapper.writeValueAsString(authAnnotation));
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }

        SeleniumTest annotation = findAnnotation(
                testContext.getTestClass(), SeleniumTest.class);
        webDriver.get("http://localhost:42322/#" + annotation.relativeUrl());
    }

    @Override
    public void afterTestClass(TestContext testContext) {
        if (webDriver != null) {
            webDriver.quit();
        }
    }
}
