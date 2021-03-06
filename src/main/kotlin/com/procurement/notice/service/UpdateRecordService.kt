package com.procurement.notice.service

import com.procurement.notice.application.model.parseCpid
import com.procurement.notice.application.model.parseOcid
import com.procurement.notice.application.model.record.update.UpdateRecordParams
import com.procurement.notice.application.service.GenerationService
import com.procurement.notice.domain.fail.Fail
import com.procurement.notice.domain.utils.MaybeFail
import com.procurement.notice.infrastructure.dto.entity.Record
import com.procurement.notice.infrastructure.service.Transform
import com.procurement.notice.infrastructure.service.record.updateRelease
import com.procurement.notice.utils.toJson
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class UpdateRecordService(
    private val releaseService: ReleaseService,
    private val jacksonJsonTransform: Transform,
    private val generationService: GenerationService
) {
    companion object {
        private val log = LoggerFactory.getLogger(UpdateRecordService::class.java)
    }

    fun updateRecord(params: UpdateRecordParams): MaybeFail<Fail> {

        val data = params.data

        val ocid = parseOcid(data.ocid)
            .doReturn { error -> return MaybeFail.error(error) }

        val cpid = parseCpid(data.cpid)
            .doReturn { error -> return MaybeFail.error(error) }

        val recordEntity = releaseService.tryGetRecordEntity(cpid, ocid)
            .doOnError { error -> return MaybeFail.error(error) }
            .get
            ?: return MaybeFail.error(Fail.Incident.Database.NotFound("Record not found."))

        val recordData = recordEntity.jsonData
        val record = jacksonJsonTransform.tryDeserialization(recordData, Record::class.java)
            .doOnError { error: Fail.Incident.Transform.Deserialization ->
                return MaybeFail.error(
                    Fail.Incident.Database.InvalidData(data = recordData, exception = error.exception )
                )
            }
            .get

        val releaseId = generationService.generateReleaseId(ocid = ocid.toString())
        val updatedRelease = record.updateRelease(releaseId = releaseId, params = params)
            .doReturn { e -> return MaybeFail.error(e) }
            .also {
                log.debug("UPDATED RELEASE (id: '${releaseId}'): '${toJson(it)}'.")
            }

        releaseService.saveRecord(
            cpid = cpid,
            ocid = ocid,
            record = updatedRelease,
            publishDate = recordEntity.publishDate
        )

        return MaybeFail.ok()
    }
}
