package com.desouza.dscommerce.dto;

import java.time.Instant;

public class CustomError {

    private String error;
    private Integer status;
    private String path;
    private Instant timestamp;

    public CustomError(String error, Integer status, String path, Instant timestamp) {
        this.error = error;
        this.status = status;
        this.path = path;
        this.timestamp = timestamp;
    }

    public String getError() {
        return error;
    }

    public String getPath() {
        return path;
    }

    public Integer getStatus() {
        return status;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

}
