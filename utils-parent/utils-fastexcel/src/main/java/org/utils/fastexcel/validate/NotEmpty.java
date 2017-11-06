package org.utils.fastexcel.validate;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import org.utils.fastexcel.validate.imp.NotEmptyValidate;

@Documented
@Constraint(validatedBy = { NotEmptyValidate.class })
@Target({ ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR,
		ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface NotEmpty {
	
	String message() default "电话号码格式不正确";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
