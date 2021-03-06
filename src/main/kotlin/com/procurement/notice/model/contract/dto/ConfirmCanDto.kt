package com.procurement.notice.model.contract.dto

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude
import com.procurement.notice.model.contract.Can
import com.procurement.notice.model.ocds.Lot

data class ConfirmCanDto @JsonCreator constructor(
    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    val lots: List<Lot>,

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    val cans: List<Can>
)
