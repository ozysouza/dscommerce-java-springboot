package com.desouza.dscommerce.dto.error;

public class FieldMessageError {

    private String fieldName;
    private String message;

    public FieldMessageError(String fieldName, String message) {
        this.fieldName = fieldName;
        this.message = message;
    }

    public String getFieldName() {
        return fieldName;
    }

    public String getMessage() {
        return message;
    }

}
