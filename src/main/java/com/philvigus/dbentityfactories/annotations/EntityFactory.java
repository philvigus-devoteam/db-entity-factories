package com.philvigus.dbentityfactories.annotations;

import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;


@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
@Inherited
public @interface EntityFactory {
    /**
     * A convenience annotation used to mark
     * factories as managed entities by Spring
     * Boot.
     */
    @AliasFor(
            annotation = Component.class
    )
    String value() default "";
}
