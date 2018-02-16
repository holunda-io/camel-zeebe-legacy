package io.zeebe.camel.test;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.junit.Test;


@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface CamelZeebeTest {

    String routeBuilderSupplier() default  "";

    String routeBuilder() default "";

    String mockEndpoint() default  "";

}
