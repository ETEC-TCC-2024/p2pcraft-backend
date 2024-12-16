package io.github.seujorgenochurras.p2pApi.api.controller.swagger;

import io.github.seujorgenochurras.p2pApi.domain.exception.GenericErrorResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

@Target(value = {
    ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@ApiResponse(responseCode = "404",
    content = @Content(mediaType = "application/json",
        schema = @Schema(implementation = GenericErrorResponse.class)))
@Inherited
public @interface NotFoundDocumentation {
    @AliasFor(value = "description",
        annotation = ApiResponse.class)
    String value();
}
