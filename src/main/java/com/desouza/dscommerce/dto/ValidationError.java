package com.desouza.dscommerce.dto;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class ValidationError extends CustomError {

    private List<FieldMessage> erros = new ArrayList<>();

    public ValidationError(String error, Integer status, String path, Instant timestamp) {
        super(error, status, path, timestamp);
    }

    public List<FieldMessage> getErros() {
        return erros;
    }

    public void addError(String fieldName, String message) {
        erros.add(new FieldMessage(fieldName, message));
    }

}
