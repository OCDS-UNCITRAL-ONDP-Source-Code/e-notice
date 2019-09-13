package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty

data class AcceleratedProcedure @JsonCreator constructor(

    @get:JsonProperty("isAcceleratedProcedure")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    val isAcceleratedProcedure: Boolean?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val acceleratedProcedureJustification: String?
)

