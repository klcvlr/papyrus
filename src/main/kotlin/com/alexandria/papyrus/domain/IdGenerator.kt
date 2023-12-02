package com.alexandria.papyrus.domain

import java.util.UUID

class IdGenerator {
    fun generate(): String = UUID.randomUUID().toString()
}
