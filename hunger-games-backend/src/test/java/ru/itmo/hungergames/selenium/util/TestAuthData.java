package ru.itmo.hungergames.selenium.util;

import ru.itmo.hungergames.model.entity.user.UserRole;

import java.lang.annotation.*;



@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface TestAuthData {
    UserRole role();
    String id() default "";
    String name() default "";
    String token() default "";
}
