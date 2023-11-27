package com.alexandria.papyrus.adapters.exposition.messaging

import com.alexandria.papyrus.application.DocumentUseCases
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import io.awspring.cloud.sqs.annotation.SqsListener
import io.awspring.cloud.sqs.annotation.SqsListenerAcknowledgementMode.ON_SUCCESS
import org.springframework.stereotype.Component

@Component
class SQSMessageHandler(
    private val documentUseCases: DocumentUseCases,
    private val objectMapper: ObjectMapper,
) {
    @SqsListener("\${papyrus.cloud.queue}", acknowledgementMode = ON_SUCCESS)
    fun changeDocumentStatus(message: String) {
        try {
            val json: JsonNode = objectMapper.readTree(message)
            val documentIdentifier = json["Message"].asText() ?: throw Exception("Document identifier is null")
//            documentUseCases.changeStatus(documentIdentifier, "COMPLETED")
            println(message)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
