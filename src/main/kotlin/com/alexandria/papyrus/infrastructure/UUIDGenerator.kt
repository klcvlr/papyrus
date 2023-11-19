package com.alexandria.papyrus.infrastructure

import com.alexandria.papyrus.domain.IdGenerator
import org.springframework.stereotype.Component
import java.util.*

@Component
class UUIDGenerator : IdGenerator {
    override fun generate(): String = UUID.randomUUID().toString()
}