package com.procurement.notice.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class RequestDto<T> {

    @JsonProperty(value = "data")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T data;

    public RequestDto(@JsonProperty(value = "data") final T data) {
        this.data = data;
    }
}