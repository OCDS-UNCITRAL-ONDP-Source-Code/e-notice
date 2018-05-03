package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.procurement.notice.databinding.MoneyDeserializer

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder("amount", "currency")
data class Value(

        @JsonProperty("amount")
        @JsonDeserialize(using = MoneyDeserializer::class)
        var amount: Double?,

        @JsonProperty("currency")
        val currency: Currency?
)
