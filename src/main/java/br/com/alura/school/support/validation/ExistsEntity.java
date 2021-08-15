package br.com.alura.school.support.validation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.LOCAL_VARIABLE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import javax.validation.Constraint;
import javax.validation.Payload;

@Constraint(validatedBy = ExistsEntityValidator.class)
@Target(value = { FIELD, LOCAL_VARIABLE })
@Retention(RUNTIME)
public @interface ExistsEntity {

	String message() default "The {classpath} in {field} is unique";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

	String classpath();

	String field();

}
