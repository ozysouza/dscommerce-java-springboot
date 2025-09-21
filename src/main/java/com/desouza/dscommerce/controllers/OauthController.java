package com.desouza.dscommerce.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.desouza.dscommerce.api.docs.StandardApiResponses.StandardOauthResponse;
import com.desouza.dscommerce.dto.email.EmailDTO;
import com.desouza.dscommerce.dto.user.NewPasswordDTO;
import com.desouza.dscommerce.services.OauthService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping(value = "/oauth")
@Tag(name = "Oauth", description = "Controller for Authentication")
public class OauthController {

    @Autowired
    private OauthService oauthService;

    @Operation(summary = "Recover Token by Email", description = "Returns token link on Email")
    @StandardOauthResponse
    @PostMapping(value = "/recover-token", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> createRecoverToken(@Valid @RequestBody EmailDTO emailDTO) {
        oauthService.createRecoverToken(emailDTO);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Save new Password", description = "Returns no Content")
    @StandardOauthResponse
    @PutMapping(value = "/new-password", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> saveNewPassword(@Valid @RequestBody NewPasswordDTO newPasswordDTO) {
        oauthService.saveNewPassword(newPasswordDTO);
        return ResponseEntity.noContent().build();
    }

}
