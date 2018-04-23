package com.procurement.notice.model.tender.dto

import com.fasterxml.jackson.annotation.JsonProperty
import com.procurement.notice.model.ocds.Period
import com.procurement.notice.model.ocds.TenderStatus
import com.procurement.notice.model.ocds.TenderStatusDetails

data class TenderDto(

        @JsonProperty("status")
        val status: TenderStatus,

        @JsonProperty("statusDetails")
        val statusDetails: TenderStatusDetails,

        @JsonProperty("tenderPeriod")
        val tenderPeriod: Period,

        @JsonProperty("enquiryPeriod")
        val enquiryPeriod: Period
)
