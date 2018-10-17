package com.procurement.notice.model.tender.dto

import com.fasterxml.jackson.annotation.JsonCreator

data class UpdateCnDto @JsonCreator constructor(

        val amendment: AmendmentUpdateCn?
)

data class AmendmentUpdateCn @JsonCreator constructor(

        val relatedLots: Set<String>
)