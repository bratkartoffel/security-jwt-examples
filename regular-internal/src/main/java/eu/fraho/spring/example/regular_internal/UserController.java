/*
 * MIT Licence
 * Copyright (c) 2020 Simon Frankenberger
 *
 * Please see LICENCE.md for complete licence text.
 */
package eu.fraho.spring.example.regular_internal;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    @PreAuthorize("principal.username == 'userA'")
    @RequestMapping("/user/a")
    public String userA() {
        return "Hello a!";
    }

    @PreAuthorize("principal.username == 'userB'")
    @RequestMapping("/user/b")
    public String userB() {
        return "Hello b!";
    }
}
