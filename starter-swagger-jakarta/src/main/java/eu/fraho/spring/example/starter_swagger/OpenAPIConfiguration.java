package eu.fraho.spring.example.starter_swagger;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityScheme;

@OpenAPIDefinition(
        info = @Info(
                title = "Fraho securityJwt example API",
                description = "This is just a sample application with swagger2 documentation enabled",
                contact = @Contact(
                        name = "Simon Frankenberger",
                        email = "simon-ossrh-release@fraho.eu",
                        url = "https://www.fraho.eu/"
                ),
                license = @License(
                        name = "MIT Licence",
                        url = "https://opensource.org/licenses/MIT"
                )
        )
)
@SecurityScheme(
        name = "apiKey",
        scheme = "bearer",
        bearerFormat = "JWT",
        type = SecuritySchemeType.HTTP,
        in = SecuritySchemeIn.HEADER
)
class OpenAPIConfiguration {
}
