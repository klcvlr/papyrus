package com.alexandria.papyrus.adapters.messaging

import com.alexandria.papyrus.domain.notification.DocumentNotificationPublisher
import io.awspring.cloud.sqs.operations.SqsTemplate
import org.springframework.stereotype.Component

@Component
class SQSDocumentNotificationPublisher(private val sqsTemplate: SqsTemplate) : DocumentNotificationPublisher {
    // TODO externalize the queue name. Pre-requirement: think about how we want to architect the queues
    override fun publishUploadCompleted(documentId: String) {
        sqsTemplate.send("uploaded-document-queue", documentId)
    }
}
