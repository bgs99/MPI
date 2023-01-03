package ru.itmo.hungergames.util.annotation;

import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import org.springframework.format.annotation.DateTimeFormat;

import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@JacksonAnnotationsInside
@Documented
@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
public @interface JsonDateTime {
}

