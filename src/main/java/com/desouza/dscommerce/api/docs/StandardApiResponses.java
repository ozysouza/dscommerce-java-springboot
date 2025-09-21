package com.desouza.dscommerce.api.docs;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

public interface StandardApiResponses {

    // =====================
    // GET BY ID
    // =====================
    @Target({ ElementType.METHOD })
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @ApiResponses({
            @ApiResponse(description = "OK", responseCode = "200"),
            @ApiResponse(description = "Not Found", responseCode = "404")
    })
    public @interface StandardGetByIdResponse {
    }

    // =============================
    // GET BY ID WITH AUTHORIZATION
    // =============================
    @Target({ ElementType.METHOD })
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @ApiResponses({
            @ApiResponse(description = "OK", responseCode = "200"),
            @ApiResponse(description = "Unauthorized", responseCode = "401"),
            @ApiResponse(description = "Not Found", responseCode = "404")
    })
    public @interface StandardGetByIWithAutorizationdResponse {
    }

}
