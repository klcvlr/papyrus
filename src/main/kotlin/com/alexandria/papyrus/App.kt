package com.alexandria.papyrus

import com.alexandria.papyrus.config.CloudEndpoints
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication

@SpringBootApplication
@EnableConfigurationProperties(CloudEndpoints::class)
class PapyrusApplication

fun main(args: Array<String>) {
    runApplication<PapyrusApplication>(*args)
}
