package com.npichuzhkin.javacodetest.utils.builders;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.List;

public class ErrorTextBuilder {
    public static String build(BindingResult bindingResult){
        List<FieldError> errorFields = bindingResult.getFieldErrors();
        StringBuilder errors = new StringBuilder();

        for (FieldError fieldError: errorFields) {
            errors
                    .append("В поле ").append(fieldError.getField())
                    .append(" ошибка - ").append(fieldError.getDefaultMessage())
                    .append("; ");
        }
        return errors.toString();
    }
}
