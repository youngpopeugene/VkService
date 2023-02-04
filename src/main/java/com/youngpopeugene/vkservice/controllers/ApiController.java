package com.youngpopeugene.vkservice.controllers;

import com.youngpopeugene.vkservice.model.api.BaseRequest;
import com.youngpopeugene.vkservice.model.api.BaseResponse;
import com.youngpopeugene.vkservice.model.api.ErrorResponse;
import com.youngpopeugene.vkservice.services.ApiService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;


@RestController
@Tag(name = "API controller")
@RequestMapping("/api/v1/vk")
public class ApiController {
    private ApiService apiService;

    @Autowired
    public ApiController(ApiService apiService) {
        this.apiService = apiService;
    }

    @Operation(summary = "Get information about user and membership status")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Bad request - VK API error", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
            @ApiResponse(responseCode = "404", description = "Not found - User wasn't found or was deleted", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
            @ApiResponse(responseCode = "422", description = "Unprocessable entity - Validation error", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))})
    })
    @PostMapping(value = "/getInfo", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public BaseResponse getUserAndMemberStatus(@RequestHeader("vk_service_token") String vkServiceToken, @RequestBody BaseRequest request) {
        return apiService.getUserAndMemberStatus(vkServiceToken, request);
    }

}
