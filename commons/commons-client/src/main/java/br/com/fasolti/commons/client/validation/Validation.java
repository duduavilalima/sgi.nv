package br.com.fasolti.commons.client.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Validation {

	String name();

	boolean notNull() default false;

	boolean notEmpty() default false;

	int max() default 0;

	int min() default 0;

}
