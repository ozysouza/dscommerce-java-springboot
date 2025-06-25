package com.desouza.dscommerce.dto.error;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class ValidationError extends CustomError {

    private List<FieldMessageError> errors = new ArrayList<>();

    public ValidationError(String error, Integer status, String path, Instant timestamp) {
        super(error, status, path, timestamp);
    }

    public List<FieldMessageError> getErros() {
        return errors;
    }

    public void addError(String fieldName, String message) {
        errors.removeIf(x -> x.getFieldName().equals(fieldName));
        errors.add(new FieldMessageError(fieldName, message));
    }

}
