package com.alexandria.papyrus.domain

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class IdGeneratorTest {

    @Test
    fun `generates unique ids`() {
        val idGenerator = IdGenerator()
        val id1 = idGenerator.generate()
        val id2 = idGenerator.generate()
        assertNotEquals(id1, id2)
    }

    @Test
    fun `generated ids are UUIDs`() {
        val idGenerator = IdGenerator()
        val id = idGenerator.generate()
        assertTrue(id.matches(Regex("[a-f0-9]{8}-[a-f0-9]{4}-[1-5][a-f0-9]{3}-[89ab][a-f0-9]{3}-[a-f0-9]{12}")))
    }
}
