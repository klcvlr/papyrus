package com.alexandria.papyrus.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "papyrus.cloud")
data class CloudEndpoints(
    val topic: String,
    val queue: String,
    val bucket: String,
)
