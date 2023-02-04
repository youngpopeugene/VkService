package com.youngpopeugene.vkservice.model.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class BaseRequest {
    @JsonProperty("user_id")
    private String userId;

    @JsonProperty("group_id")
    private String groupId;
}
