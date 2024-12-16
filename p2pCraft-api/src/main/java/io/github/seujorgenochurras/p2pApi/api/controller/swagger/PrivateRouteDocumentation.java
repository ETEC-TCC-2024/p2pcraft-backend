package io.github.seujorgenochurras.p2pApi.api.controller.swagger;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@ApiResponses(value = {
    @ApiResponse(responseCode = "401",
        description = "Unauthorized - Access token missing or invalid",
        content = @Content(mediaType = "application/json")), @ApiResponse(responseCode = "403",
            description = "Forbidden - Access denied",
            content = @Content(mediaType = "application/json"))})
@Target({
    ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface PrivateRouteDocumentation {
}
