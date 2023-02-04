package com.youngpopeugene.vkservice.model.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class IsMemberResponse {
    @JsonProperty("response")
    private int member;

    private Error error;
}
