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
    // GET ALL
    // =====================
    @Target({ ElementType.METHOD })
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @ApiResponses({
            @ApiResponse(description = "OK", responseCode = "200"),
    })
    public @interface StandardGetAllResponse {
    }

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

    // =====================
    // POST WITH AUTH (Create)
    // =====================
    @Target({ ElementType.METHOD })
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @ApiResponses({
            @ApiResponse(description = "Created", responseCode = "201"),
            @ApiResponse(description = "Bad Request", responseCode = "400"),
            @ApiResponse(description = "Unauthorized", responseCode = "401"),
            @ApiResponse(description = "Forbidden", responseCode = "403"),
            @ApiResponse(description = "Unprocessable Entity", responseCode = "422")
    })
    public @interface StandardPostAuthResponse {
    }

    // =====================
    // POST NO AUTH (Create)
    // =====================
    @Target({ ElementType.METHOD })
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @ApiResponses({
            @ApiResponse(description = "Created", responseCode = "201"),
            @ApiResponse(description = "Bad Request", responseCode = "400"),
            @ApiResponse(description = "Unprocessable Entity", responseCode = "422")
    })
    public @interface StandardPostNoAuthResponse {
    }

    // =====================
    // PUT WITH AUTH
    // =====================
    @Target({ ElementType.METHOD })
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @ApiResponses({
            @ApiResponse(description = "OK", responseCode = "200"),
            @ApiResponse(description = "Bad Request", responseCode = "400"),
            @ApiResponse(description = "Unauthorized", responseCode = "401"),
            @ApiResponse(description = "Forbidden", responseCode = "403"),
            @ApiResponse(description = "Not Found", responseCode = "404"),
            @ApiResponse(description = "Unprocessable Entity", responseCode = "422")
    })

    public @interface StandardPutAuthResponse {
    }

    // =====================
    // DELETE
    // =====================
    @Target({ ElementType.METHOD })
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @ApiResponses({
            @ApiResponse(description = "No Content", responseCode = "204"),
            @ApiResponse(description = "Unauthorized", responseCode = "401"),
            @ApiResponse(description = "Forbidden", responseCode = "403"),
            @ApiResponse(description = "Not Found", responseCode = "404")
    })
    public @interface StandardDeleteResponse {
    }

    // =====================
    // RECOVER TOKEN
    // =====================
    @Target({ ElementType.METHOD })
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @ApiResponses({
            @ApiResponse(description = "No Content", responseCode = "204"),
            @ApiResponse(description = "Not Found", responseCode = "404")
    })
    public @interface StandardOauthResponse {
    }

}
