/*
 * MIT Licence
 * Copyright (c) 2020 Simon Frankenberger
 *
 * Please see LICENCE.md for complete licence text.
 */
package eu.fraho.spring.example.starter_swagger;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PublicController {
    @Operation(summary = "Sample endpoint",
            description = "This operation is publicly available, no key required")
    @RequestMapping("/public")
    public String hello() {
        return "Public area";
    }
}
