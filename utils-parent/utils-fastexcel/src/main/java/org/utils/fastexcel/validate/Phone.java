package org.utils.fastexcel.validate;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import org.utils.fastexcel.validate.imp.PhoneValidate;

@Documented
@Constraint(validatedBy = { PhoneValidate.class })
@Target({ ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR,
		ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface Phone {
	
	String message() default "电话号码格式不正确";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
