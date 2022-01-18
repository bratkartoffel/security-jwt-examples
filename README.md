# Spring Security Addon for JWT - Examples
[![License](http://img.shields.io/:license-mit-blue.svg?style=flat)](http://doge.mit-license.org)

This repository contains various examples on how to use the [security-jwt](https://github.com/bratkartoffel/security-jwt) library.

# Contents
| Module                          | Description                                                                                                                                   |
|---------------------------------|-----------------------------------------------------------------------------------------------------------------------------------------------|
| regular-internal                | This example shows how to use the regular (old) way on how to integrate the library.                                                          |
| regular-no-refresh              | This is the most simple example, without any refresh token store configured.                                                                  |
| starter-cookies                 | With 2.0.0 onwards there is built-in cookie support in the library. This example shows how to enable and configure them.                      |
| starter-custom-endpoints        | This example demonstrates how the endpoints for the Restcontroller can be modified                                                            |
| starter-custom-jwtuser          | With this example you can see how to use an custom JwtUser implementation with custom fields.                                                 |
| starter-custom-login-service    | In this example you can see how to use an custom LoginService implementation. This way you can override the login process with your workflow. |
| starter-custom-password-encoder | This example shows how to explicitly use another PasswordEncoder instead of the libraries default CryptPasswordEncoder.                       |
| starter-custom-security-conf    | In this example the default security config is overwritten and customized.                                                                    |
| starter-files                   | The most simple example with an refresh module.                                                                                               |
| starter-hibernate               | The most simple example with an refresh module.                                                                                               |
| starter-multiple                | An example which includes two refresh tokenstore implementations. You have to manually, explicitly select one in this case.                   |
| starter-swagger                 | This example shows how to use swagger2 to automatically generate an api documentation.                                                        |
