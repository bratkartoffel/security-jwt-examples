/*
 * MIT Licence
 * Copyright (c) 2020 Simon Frankenberger
 *
 * Please see LICENCE.md for complete licence text.
 */
package eu.fraho.spring.example.starter_swagger;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Secured("ROLE_USER")
public class PrivateController {
    @Operation(summary = "Sample endpoint",
            description = "This operation is protected by an api key",
            security = {@SecurityRequirement(name = "apiKey")}
    )
    @RequestMapping("/private")
    public String index() {
        return "Hello world!";
    }
}
