package com.alexandria.papyrus

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class PapyrusApplication

fun main(args: Array<String>) {
    runApplication<PapyrusApplication>(*args)
}
