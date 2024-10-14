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
                    .append("Field ").append(fieldError.getField())
                    .append(" contains an error - ").append(fieldError.getDefaultMessage())
                    .append("; ");
        }
        return errors.toString();
    }
}
