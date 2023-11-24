package com.alexandria.papyrus.domain.notification

interface NotificationPublisher {
    fun sendUploadNotification(documentId: String)
}
