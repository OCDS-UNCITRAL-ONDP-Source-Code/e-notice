package com.procurement.notice.model.tender.dto

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude
import com.procurement.notice.model.ocds.Award
import com.procurement.notice.model.ocds.Lot
import com.procurement.notice.model.ocds.TenderStatus
import com.procurement.notice.model.ocds.TenderStatusDetails
import com.procurement.notice.model.tender.record.ElectronicAuctions
import java.util.*

data class TenderPeriodEndAuctionDto @JsonCreator constructor(

    val tenderStatus: TenderStatus,

    val tenderStatusDetails: TenderStatusDetails,

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    val awards: HashSet<Award>,

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    val lots: HashSet<Lot>,

    var electronicAuctions: ElectronicAuctions
)
