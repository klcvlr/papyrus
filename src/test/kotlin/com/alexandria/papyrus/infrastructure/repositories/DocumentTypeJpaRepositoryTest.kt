package com.alexandria.papyrus.infrastructure.repositories

import com.alexandria.papyrus.fakes.aDocumentType
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
@Transactional
class DocumentTypeJpaRepositoryTest {
    @Autowired
    private lateinit var documentTypeRepository: DocumentTypeJpaRepository

    @Test
    fun `a new documentType can be saved`() {
        val documentType = aDocumentType(identifier = "documentTypeId", name = "picture")

        documentTypeRepository.save(documentType)

        val savedDocumentType = documentTypeRepository.findByIdentifier("documentTypeId")
        assertThat(savedDocumentType.identifier).isEqualTo("documentTypeId")
        assertThat(savedDocumentType.name).isEqualTo("picture")
    }
}
