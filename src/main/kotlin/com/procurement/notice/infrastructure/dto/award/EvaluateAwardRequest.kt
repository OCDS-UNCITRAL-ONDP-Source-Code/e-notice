package com.procurement.notice.infrastructure.dto.award

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.procurement.notice.infrastructure.bind.amount.AmountDeserializer
import com.procurement.notice.infrastructure.bind.amount.AmountSerializer
import com.procurement.notice.infrastructure.bind.date.JsonDateTimeDeserializer
import com.procurement.notice.infrastructure.bind.date.JsonDateTimeSerializer
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.*

class EvaluateAwardRequest(
    @field:JsonProperty("award") @param:JsonProperty("award") val award: Award
) {
    data class Award(
        @field:JsonProperty("id") @param:JsonProperty("id") val id: String,

        @param:JsonDeserialize(using = JsonDateTimeDeserializer::class)
        @field:JsonSerialize(using = JsonDateTimeSerializer::class)
        @field:JsonProperty("date") @param:JsonProperty("date") val date: LocalDateTime,

        @field:JsonInclude(JsonInclude.Include.NON_NULL)
        @field:JsonProperty("description") @param:JsonProperty("description") val description: String?,

        @field:JsonProperty("status") @param:JsonProperty("status") val status: String,
        @field:JsonProperty("statusDetails") @param:JsonProperty("statusDetails") val statusDetails: String,
        @field:JsonProperty("relatedLots") @param:JsonProperty("relatedLots") val relatedLots: List<UUID>,
        @field:JsonProperty("value") @param:JsonProperty("value") val value: Value,
        @field:JsonProperty("suppliers") @param:JsonProperty("suppliers") val suppliers: List<Supplier>,

        @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
        @field:JsonProperty("documents") @param:JsonProperty("documents") val documents: List<Document>?
    ) {

        data class Value(
            @param:JsonDeserialize(using = AmountDeserializer::class)
            @field:JsonSerialize(using = AmountSerializer::class)
            @field:JsonProperty("amount") @param:JsonProperty("amount") val amount: BigDecimal,

            @field:JsonProperty("currency") @param:JsonProperty("currency") val currency: String
        )

        data class Supplier(
            @field:JsonProperty("id") @param:JsonProperty("id") val id: String,
            @field:JsonProperty("name") @param:JsonProperty("name") val name: String
        )

        data class Document(
            @field:JsonProperty("documentType") @param:JsonProperty("documentType") val documentType: String,
            @field:JsonProperty("id") @param:JsonProperty("id") val id: String,

            @param:JsonDeserialize(using = JsonDateTimeDeserializer::class)
            @field:JsonSerialize(using = JsonDateTimeSerializer::class)
            @field:JsonProperty("datePublished") @param:JsonProperty("datePublished") val datePublished: LocalDateTime,

            @field:JsonProperty("url") @param:JsonProperty("url") val url: String,

            @field:JsonInclude(JsonInclude.Include.NON_NULL)
            @field:JsonProperty("title") @param:JsonProperty("title") val title: String?,

            @field:JsonInclude(JsonInclude.Include.NON_NULL)
            @field:JsonProperty("description") @param:JsonProperty("description") val description: String?,

            @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
            @field:JsonProperty("relatedLots") @param:JsonProperty("relatedLots") val relatedLots: List<UUID>?
        )
    }
}