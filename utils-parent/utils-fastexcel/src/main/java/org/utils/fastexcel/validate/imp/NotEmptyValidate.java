package org.utils.fastexcel.validate.imp;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.utils.fastexcel.validate.Phone;

public class NotEmptyValidate implements ConstraintValidator<Phone, String> {

	@Override
	public void initialize(Phone constraintAnnotation) {
	}

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		return value == null || "".equals(value.trim());
	}
}
