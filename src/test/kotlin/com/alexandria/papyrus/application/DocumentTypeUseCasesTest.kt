package com.alexandria.papyrus.application

import com.alexandria.papyrus.domain.DocumentTypeNotFoundException
import com.alexandria.papyrus.domain.IdGenerator
import com.alexandria.papyrus.domain.repositories.DocumentTypeRepository
import com.alexandria.papyrus.fakes.aDocumentType
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
class DocumentTypeUseCasesTest {
    @MockK
    private lateinit var idGenerator: IdGenerator

    @MockK
    private lateinit var documentTypeRepository: DocumentTypeRepository

    @InjectMockKs
    private lateinit var documentTypeUseCases: DocumentTypeUseCases

    @Test
    fun `document type can be found by identifier`() {
        val documentType = aDocumentType(identifier = "documentTypeIdentifier")
        every { documentTypeRepository.findByIdentifier("documentTypeIdentifier") } returns documentType

        val foundDocumentType = documentTypeUseCases.findByIdentifier("documentTypeIdentifier")

        assertThat(foundDocumentType).isEqualTo(documentType)
    }

    @Test
    fun `an exception is thrown when looking up for an documentType by an identifier that does not exist`() {
        every { documentTypeRepository.findByIdentifier(any()) } returns null

        assertThatThrownBy {
            documentTypeUseCases.findByIdentifier("documentTypeIdentifier")
        }.isInstanceOf(
            DocumentTypeNotFoundException::class.java,
        ).hasMessage("DocumentType with identifier 'documentTypeIdentifier' not found")
    }

    @Test
    fun `document type can be created`() {
        val documentType = aDocumentType(identifier = "documentTypeIdentifier")
        every { idGenerator.generate() } returns documentType.identifier
        every { documentTypeRepository.save(any()) } returns documentType

        val createdDocumentTypeIdentifier = documentTypeUseCases.create("documentTypeName")

        assertThat(createdDocumentTypeIdentifier).isEqualTo(documentType.identifier)
    }
}
