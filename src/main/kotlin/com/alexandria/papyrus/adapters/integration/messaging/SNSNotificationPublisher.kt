package com.alexandria.papyrus.adapters.integration.messaging

import com.alexandria.papyrus.config.CloudEndpoints
import com.alexandria.papyrus.domain.notification.NotificationPublisher
import io.awspring.cloud.sns.core.SnsTemplate
import org.springframework.stereotype.Component

@Component
class SNSNotificationPublisher(
    private val snsTemplate: SnsTemplate,
    private val cloudEndpoints: CloudEndpoints,
) : NotificationPublisher {
    override fun sendUploadNotification(documentId: String) {
        snsTemplate.sendNotification(cloudEndpoints.topic, documentId, "upload-completed")
    }
}
