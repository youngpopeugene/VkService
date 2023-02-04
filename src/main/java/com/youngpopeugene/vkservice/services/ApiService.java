package com.youngpopeugene.vkservice.services;

import com.youngpopeugene.vkservice.model.api.*;
import com.youngpopeugene.vkservice.model.api.Error;
import com.youngpopeugene.vkservice.util.Validator;
import jakarta.validation.ValidationException;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import jakarta.persistence.EntityNotFoundException;

import java.util.ArrayList;
import java.util.List;


@Service
public class ApiService {
    @Cacheable("responses")
    public BaseResponse getUserAndMemberStatus(String vkServiceToken, BaseRequest request) {

        ArrayList<String> errors = Validator.validateUserId(request.getUserId());
        errors.addAll(Validator.validateGroupId(request.getGroupId()));
        if (!errors.isEmpty())
            throw new ValidationException(Validator.prepareValidationErrorMessage(errors));

        GetUserResponse getUserResponse = getUser(vkServiceToken, request);
        if (getUserResponse.getError() != null)
            throw new RuntimeException(prepareErrorMessage(getUserResponse.getError()));

        List<ApiUser> apiUserList = getUserResponse.getApiUserList();
        if (apiUserList.isEmpty())
            throw new EntityNotFoundException("Error: user with id = " + request.getUserId() + " wasn't found");

        if (apiUserList.get(0).getFirstName().equals("DELETED") && apiUserList.get(0).getLastName().equals("")){
            throw new EntityNotFoundException("Error: user with id = " + request.getUserId() + " was deleted");
        }

        IsMemberResponse isMemberResponse = isMember(vkServiceToken, request);
        if (isMemberResponse.getError() != null)
            throw new RuntimeException(prepareErrorMessage(isMemberResponse.getError()));

        return prepareBaseResponse(apiUserList.get(0), isMemberResponse);
    }

    public GetUserResponse getUser(String vkServiceToken, BaseRequest request) {
        return WebClient.create("https://api.vk.com/")
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path("/method/users.get")
                        .queryParam("access_token", vkServiceToken)
                        .queryParam("user_ids", request.getUserId())
                        .queryParam("v", "5.131")
                        .build())
                .retrieve()
                .bodyToMono(GetUserResponse.class).share().block();
    }

    public IsMemberResponse isMember(String vkServiceToken, BaseRequest request) {
        return WebClient.create("https://api.vk.com/")
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path("/method/groups.isMember")
                        .queryParam("access_token", vkServiceToken)
                        .queryParam("group_id", request.getGroupId())
                        .queryParam("user_id", request.getUserId())
                        .queryParam("v", "5.131")
                        .build())
                .retrieve()
                .bodyToMono(IsMemberResponse.class).share().block();
    }

    private String prepareErrorMessage(Error error) {
        return ("API error: code : " + error.getErrorCode() + ", message: " + error.getErrorMsg());
    }

    private BaseResponse prepareBaseResponse(ApiUser apiUser, IsMemberResponse isMemberResponse) {
        BaseResponse response = new BaseResponse();
        response.setFirstName(apiUser.getFirstName());
        response.setLastName(apiUser.getLastName());
        response.setMember(isMemberResponse.getMember() == 1);
        return response;
    }


}
