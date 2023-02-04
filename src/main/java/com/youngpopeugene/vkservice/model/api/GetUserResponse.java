package com.youngpopeugene.vkservice.model.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class GetUserResponse {
    @JsonProperty("response")
    private List<ApiUser> apiUserList;

    private Error error;
}
