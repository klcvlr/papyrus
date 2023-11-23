package com.alexandria.papyrus.domain.notification

interface DocumentNotificationPublisher {
    fun publishUploadCompleted(documentId: String)
}
