package com.procurement.notice.model.contract

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude
import com.procurement.notice.model.ocds.Award
import com.procurement.notice.model.ocds.Contract
import com.procurement.notice.model.ocds.InitiationType
import com.procurement.notice.model.ocds.Organization
import com.procurement.notice.model.ocds.RelatedProcess
import com.procurement.notice.model.ocds.Tag
import java.time.LocalDateTime
import java.util.*


data class ContractRecord @JsonCreator constructor(

        @JsonInclude(JsonInclude.Include.NON_NULL)
        var ocid: String?,

        @JsonInclude(JsonInclude.Include.NON_NULL)
        var id: String?,

        @JsonInclude(JsonInclude.Include.NON_NULL)
        var date: LocalDateTime?,

        @JsonInclude(JsonInclude.Include.NON_EMPTY)
        var tag: List<Tag>?,

        @JsonInclude(JsonInclude.Include.NON_NULL)
        var initiationType: InitiationType? = null,

        @JsonInclude(JsonInclude.Include.NON_NULL)
        var planning: ContractPlanning? = null,

        @JsonInclude(JsonInclude.Include.NON_NULL)
        var tender: ContractTender? = null,

        @JsonInclude(JsonInclude.Include.NON_EMPTY)
        var awards: HashSet<Award>? = null,

        @JsonInclude(JsonInclude.Include.NON_EMPTY)
        var contracts: HashSet<Contract>? = null,

        @JsonInclude(JsonInclude.Include.NON_EMPTY)
        var parties: HashSet<Organization>? = null,

        @JsonInclude(JsonInclude.Include.NON_EMPTY)
        var relatedProcesses: HashSet<RelatedProcess>? = null
)
