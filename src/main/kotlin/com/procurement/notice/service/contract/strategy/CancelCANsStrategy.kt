package com.procurement.notice.service.contract.strategy

import com.fasterxml.jackson.databind.JsonNode
import com.procurement.notice.application.service.GenerationService
import com.procurement.notice.infrastructure.dto.can.CancelCANRequest
import com.procurement.notice.model.bpe.DataResponseDto
import com.procurement.notice.model.bpe.ResponseDto
import com.procurement.notice.model.ocds.Amendment
import com.procurement.notice.model.ocds.Contract
import com.procurement.notice.model.ocds.Document
import com.procurement.notice.model.ocds.Lot
import com.procurement.notice.model.ocds.Tag
import com.procurement.notice.model.tender.record.Release
import com.procurement.notice.service.ReleaseService
import com.procurement.notice.utils.toObject
import java.time.LocalDateTime

class CancelCANsStrategy(
    private val releaseService: ReleaseService,
    private val generationService: GenerationService
) {

    fun cancelCan(cpid: String, ocid: String, stage: String, releaseDate: LocalDateTime, data: JsonNode): ResponseDto {
        val request = toObject(CancelCANRequest::class.java, data)
        val recordEntity = releaseService.getRecordEntity(cpId = cpid, ocId = ocid)
        val releaseEV: Release = releaseService.getRelease(recordEntity.jsonData)

        val cancelledCAN: CancelCANRequest.CancelledCAN = request.cancelledCan
        val amendment: Amendment = cancelledCAN.createAmendment(releaseEV, releaseDate)

        val updatedReleaseEV = releaseEV.copy(
            /** BR-2.8.3.1 */
            tag = listOf(Tag.AWARD_CANCELLATION),
            /** BR-2.8.3.3 */
            date = releaseDate,
            /** BR-2.8.3.4 */
            id = generationService.generateReleaseId(ocid),
            /** BR-2.8.3.8 */
            contracts = releaseEV.contracts.updateContracts(cancelledCAN, amendment),
            /** BR-2.8.3.12 */
            tender = releaseEV.tender.copy(
                /** BR-2.8.3.13 */
                lots = releaseEV.tender.lots.updateLots(request.lot)
            )
        )

        releaseService.saveRecord(
            cpId = cpid,
            stage = stage,
            release = updatedReleaseEV,
            publishDate = recordEntity.publishDate
        )
        return ResponseDto(data = DataResponseDto(cpid = cpid, ocid = ocid))
    }

    /**
     * BR-2.8.3.9
     * BR-2.8.3.10
     * BR-2.8.3.11
     */
    private fun CancelCANRequest.CancelledCAN.createAmendment(release: Release, releaseDate: LocalDateTime): Amendment {
        return this.amendment.let { amendment ->
            Amendment(
                /**BR-2.8.3.9 */
                id = generationService.generateAmendmentId().toString(),
                /**BR-2.8.3.10 */
                amendsReleaseID = release.id,
                /**BR-2.8.3.11 */
                date = releaseDate,
                description = amendment.description,
                rationale = amendment.rationale,
                documents = amendment.documents?.map { document ->
                    Document(
                        id = document.id,
                        documentType = document.documentType,
                        url = document.url,
                        datePublished = document.datePublished,
                        title = document.title,
                        description = document.description,
                        dateModified = null,
                        format = null,
                        language = null,
                        relatedLots = null,
                        relatedConfirmations = null
                    )
                },
                changes = null,
                relatedLots = null,
                releaseID = null
            )
        }
    }

    /**
     * BR-2.8.3.8
     */
    private fun List<Contract>.updateContracts(
        cancelledCAN: CancelCANRequest.CancelledCAN,
        amendment: Amendment
    ): List<Contract> {
        return this.asSequence()
            .map { contract ->
                if (contract.id == cancelledCAN.id) {
                    contract.copy(
                        status = cancelledCAN.status,
                        statusDetails = cancelledCAN.statusDetails,
                        amendments = contract.amendments?.plus(amendment) ?: listOf(amendment)
                    )
                } else
                    contract
            }
            .toList()
    }

    /**
     * BR-2.8.3.13
     */
    private fun List<Lot>.updateLots(lot: CancelCANRequest.Lot): List<Lot> {
        return this.asSequence()
            .map {
                if (it.id == lot.id)
                    it.copy(
                        status = lot.status,
                        statusDetails = lot.statusDetails
                    )
                else
                    it

            }.toList()
    }



}