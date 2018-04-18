package com.procurement.notice.service

import com.fasterxml.jackson.databind.JsonNode
import com.procurement.notice.dao.ReleaseDao
import com.procurement.notice.exception.ErrorException
import com.procurement.notice.exception.ErrorType
import com.procurement.notice.model.bpe.ResponseDto
import com.procurement.notice.model.tender.dto.UnsuspendTenderDto
import com.procurement.notice.model.tender.enquiry.RecordEnquiry
import com.procurement.notice.model.tender.record.Record
import com.procurement.notice.utils.createObjectNode
import com.procurement.notice.utils.milliNowUTC
import com.procurement.notice.utils.toJson
import com.procurement.notice.utils.toObject
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
interface EnquiryService {

    fun createEnquiry(cpid: String, stage: String, releaseDate: LocalDateTime, data: JsonNode): ResponseDto<*>

    fun addAnswer(cpid: String, stage: String, releaseDate: LocalDateTime, data: JsonNode): ResponseDto<*>

    fun unsuspendTender(cpid: String, stage: String, releaseDate: LocalDateTime, data: JsonNode): ResponseDto<*>
}

@Service
class EnquiryServiceImpl(private val releaseService: ReleaseService,
                         private val releaseDao: ReleaseDao) : EnquiryService {

    companion object {
        private val SEPARATOR = "-"
        private val ENQUIRY_JSON = "enquiry"
    }

    override fun createEnquiry(cpid: String, stage: String, releaseDate: LocalDateTime, data: JsonNode): ResponseDto<*> {
        val entity = releaseDao.getByCpIdAndStage(cpid, stage) ?: throw  ErrorException(ErrorType.DATA_NOT_FOUND)
        val enquiry = toObject(RecordEnquiry::class.java, toJson(data.get(ENQUIRY_JSON)))
        val record = toObject(Record::class.java, entity.jsonData)
        val ocId = record.ocid ?: throw ErrorException(ErrorType.OCID_ERROR)
        addEnquiryToTender(record, enquiry)
        record.id = getReleaseId(ocId)
        record.date = releaseDate
        releaseDao.saveRelease(releaseService.getReleaseEntity(cpid, stage, record))
        return getResponseDto(cpid, ocId)
    }

    override fun addAnswer(cpid: String, stage: String, releaseDate: LocalDateTime, data: JsonNode): ResponseDto<*> {
        val entity = releaseDao.getByCpIdAndStage(cpid, stage) ?: throw  ErrorException(ErrorType.DATA_NOT_FOUND)
        val enquiry = toObject(RecordEnquiry::class.java, toJson(data.get(ENQUIRY_JSON)))
        val record = toObject(Record::class.java, entity.jsonData)
        val ocId = record.ocid ?: throw ErrorException(ErrorType.OCID_ERROR)
        addAnswerToEnquiry(record, enquiry)
        record.id = getReleaseId(ocId)
        record.date = releaseDate
        releaseDao.saveRelease(releaseService.getReleaseEntity(cpid, stage, record))
        return getResponseDto(cpid, ocId)
    }

    override fun unsuspendTender(cpid: String, stage: String, releaseDate: LocalDateTime, data: JsonNode): ResponseDto<*> {
        val entity = releaseDao.getByCpIdAndStage(cpid, stage)
                ?: throw  ErrorException(ErrorType.DATA_NOT_FOUND)
        val record = toObject(Record::class.java, entity.jsonData)
        val ocId = record.ocid ?: throw ErrorException(ErrorType.OCID_ERROR)
        val dto = toObject(UnsuspendTenderDto::class.java, toJson(data))
        addAnswerToEnquiry(record, dto.enquiry)
        record.date = releaseDate
        record.id = getReleaseId(ocId)
        record.tender.statusDetails = dto.tender.statusDetails
        record.tender.tenderPeriod = dto.tenderPeriod
        record.tender.enquiryPeriod = dto.enquiryPeriod
        releaseDao.saveRelease(releaseService.getReleaseEntity(cpid, stage, record))
        return getResponseDto(cpid, ocId)
    }

    private fun addEnquiryToTender(release: Record, enquiry: RecordEnquiry) {
        if (release.tender.enquiries != null) {
            val index = release.tender.enquiries!!.indexOfFirst { it.id == enquiry.id }
            if (index != -1) release.tender.enquiries!![index] = enquiry
            else release.tender.enquiries!!.add(enquiry)
        }
    }

    private fun addAnswerToEnquiry(release: Record, enquiry: RecordEnquiry) {
        if (release.tender.enquiries != null) {
            val index = release.tender.enquiries!!.indexOfFirst { it.id == enquiry.id }
            if (index != -1) release.tender.enquiries!![index].answer = enquiry.answer
            else throw ErrorException(ErrorType.DATA_NOT_FOUND)
        }
    }

    private fun getReleaseId(ocId: String): String {
        return ocId + SEPARATOR + milliNowUTC()
    }

    private fun getResponseDto(cpid: String, ocid: String): ResponseDto<*> {
        val jsonForResponse = createObjectNode()
        jsonForResponse.put("cpid", cpid)
        jsonForResponse.put("ocid", ocid)
        return ResponseDto(true, null, jsonForResponse)
    }
}