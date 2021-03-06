package com.procurement.notice.controller

import com.procurement.notice.application.service.Logger
import com.procurement.notice.config.properties.GlobalProperties
import com.procurement.notice.domain.fail.Fail
import com.procurement.notice.domain.utils.Result.Failure
import com.procurement.notice.domain.utils.Result.Success
import com.procurement.notice.infrastructure.dto.ApiResponse2
import com.procurement.notice.infrastructure.dto.ApiVersion2
import com.procurement.notice.model.bpe.NaN
import com.procurement.notice.model.bpe.errorResponse
import com.procurement.notice.model.bpe.tryGetId
import com.procurement.notice.model.bpe.tryGetVersion
import com.procurement.notice.service.CommandService2
import com.procurement.notice.utils.toJson
import com.procurement.notice.utils.toNode
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
@RequestMapping("/command2")
class CommandController2(
    private val commandService: CommandService2,
    private val logger: Logger
) {
    companion object {
        private val log = LoggerFactory.getLogger(CommandController2::class.java)
    }

    @PostMapping
    fun command(@RequestBody requestBody: String): ResponseEntity<ApiResponse2> {
        if (log.isDebugEnabled)
            log.debug("RECEIVED COMMAND: '$requestBody'.")

        val node = requestBody.toNode()
            .doOnError { error -> return createErrorResponseEntity(fail = error) }
            .get

        val version = when (val versionResult = node.tryGetVersion()) {
            is Success -> versionResult.get
            is Failure -> {
                when (val idResult = node.tryGetId()) {
                    is Success -> return createErrorResponseEntity(fail = versionResult.error, id = idResult.get)
                    is Failure -> return createErrorResponseEntity(fail = versionResult.error)
                }
            }
        }

        val id = node.tryGetId()
            .doOnError { error -> return createErrorResponseEntity(fail = error, version = version) }
            .get

        val response = commandService.execute(request = node)
            .also { response ->
                if (log.isDebugEnabled)
                    log.debug("RESPONSE (id: '${id}'): '${toJson(response)}'.")
            }

        return ResponseEntity(response, HttpStatus.OK)
    }

    private fun createErrorResponseEntity(
        fail: Fail,
        id: UUID = NaN,
        version: ApiVersion2 = GlobalProperties.App.apiVersion
    ): ResponseEntity<ApiResponse2> {
        val response = errorResponse(fail = fail, version = version, id = id, logger = logger)
        return ResponseEntity(response, HttpStatus.OK)
    }
}
