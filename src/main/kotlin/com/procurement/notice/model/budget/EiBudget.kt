package com.procurement.notice.model.budget

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder
import com.procurement.notice.model.ocds.Period
import com.procurement.notice.model.ocds.Value

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder("id", "period", "amount")
data class EiBudget(

        @JsonProperty("id")
        val id: String?,

        @JsonProperty("period")
        val period: Period?,

        @JsonProperty("amount")
        val amount: Value?
)