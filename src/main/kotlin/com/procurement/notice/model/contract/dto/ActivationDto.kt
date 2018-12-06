package com.procurement.notice.model.contract.dto

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude
import com.procurement.notice.model.ocds.Milestone

data class ActivationDto @JsonCreator constructor(

    val contract: ContractActivation,
    val lot: ContractActivationTenderLot
)

@JsonInclude(JsonInclude.Include.NON_NULL)
data class ContractActivation @JsonCreator constructor(
    var status: String,
    var statusDetails: String,
    val milestones: List<Milestone>
)

@JsonInclude(JsonInclude.Include.NON_NULL)
data class ContractActivationTenderLot @JsonCreator constructor(
    val id:String,
    val status: String,
    val statusDetails: String
)

