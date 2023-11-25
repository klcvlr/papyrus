package com.alexandria.papyrus

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

@SpringBootApplication
@ConfigurationPropertiesScan
class PapyrusApplication

fun main(args: Array<String>) {
    runApplication<PapyrusApplication>(*args)
}
