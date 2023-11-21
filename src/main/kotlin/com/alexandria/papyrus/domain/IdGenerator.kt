package com.alexandria.papyrus.domain

import java.util.*

class IdGenerator {
    fun generate(): String = UUID.randomUUID().toString()
}
