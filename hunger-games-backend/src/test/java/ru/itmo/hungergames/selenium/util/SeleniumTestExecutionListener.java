package ru.itmo.hungergames.selenium.util;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.Ordered;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.support.AbstractTestExecutionListener;


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

            ConfigurableApplicationContext configurableApplicationContext = (ConfigurableApplicationContext) context;
            ConfigurableListableBeanFactory bf = configurableApplicationContext.getBeanFactory();
            bf.registerResolvableDependency(WebDriver.class, webDriver);
        }
    }

    @Override
    public void beforeTestMethod(TestContext testContext) {
        if (webDriver != null) {
            SeleniumTest annotation = findAnnotation(
                    testContext.getTestClass(), SeleniumTest.class);
            webDriver.get(annotation.baseUrl());
        }
    }

    @Override
    public void afterTestClass(TestContext testContext) {
        if (webDriver != null) {
            webDriver.quit();
        }
    }

}
