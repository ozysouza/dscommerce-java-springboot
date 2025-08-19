package com.desouza.dscommerce.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.desouza.dscommerce.dto.email.EmailDTO;
import com.desouza.dscommerce.service.OauthService;

import jakarta.validation.Valid;

@RestController
@RequestMapping(value = "/oauth")
public class OauthController {

    @Autowired
    private OauthService oauthService;

    @PostMapping(value = "/recover-token")
    public ResponseEntity<Void> createRecoverToken(@Valid @RequestBody EmailDTO emailDTO) {
        oauthService.createRecoverToken(emailDTO);
        return ResponseEntity.noContent().build();
    }

}
