package com.alexandria.papyrus.application

import com.alexandria.papyrus.domain.DocumentNotFoundException
import com.alexandria.papyrus.domain.FolderNotFoundException
import com.alexandria.papyrus.domain.IdGenerator
import com.alexandria.papyrus.domain.repositories.DocumentRepository
import com.alexandria.papyrus.domain.repositories.DocumentTypeRepository
import com.alexandria.papyrus.domain.repositories.FolderRepository
import com.alexandria.papyrus.fakes.aDocument
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
class DocumentUseCasesTest {
    @MockK
    private lateinit var idGenerator: IdGenerator

    @MockK
    private lateinit var documentRepository: DocumentRepository

    @MockK
    private lateinit var documentTypeRepository: DocumentTypeRepository

    @MockK
    private lateinit var folderRepository: FolderRepository

    @InjectMockKs
    private lateinit var documentUseCases: DocumentUseCases

    @Test
    fun `document can be found by identifier`() {
        val document = aDocument(identifier = "documentIdentifier")
        every { documentRepository.findByIdentifier("documentIdentifier") } returns document

        val foundDocument = documentUseCases.findByIdentifier("documentIdentifier")

        assertThat(foundDocument).isEqualTo(document)
    }

    @Test
    fun `an exception is thrown when looking up for an document by an identifier that does not exist`() {
        every { documentRepository.findByIdentifier(any()) } returns null

        assertThatThrownBy {
            documentUseCases.findByIdentifier("documentIdentifier")
        }.isInstanceOf(
            DocumentNotFoundException::class.java,
        ).hasMessage("Document with identifier 'documentIdentifier' not found")
    }

    @Test
    fun `document can be created`() {
        val document = aDocument(identifier = "documentIdentifier")
        every { idGenerator.generate() } returns document.identifier
        every { documentRepository.save(document) } returns Unit
        every { folderRepository.findByIdentifier("parentFolderIdentifier") } returns document.parentFolder

        val createdDocumentIdentifier = documentUseCases.createDocument("documentName", "parentFolderIdentifier")

        assertThat(createdDocumentIdentifier).isEqualTo(document.identifier)
    }

    @Test
    fun `an exception is thrown when creating a document in a folder that does not exist`() {
        every { folderRepository.findByIdentifier(any()) } returns null

        assertThatThrownBy {
            documentUseCases.createDocument("documentName", "parentFolderIdentifier")
        }.isInstanceOf(
            FolderNotFoundException::class.java,
        ).hasMessage("Folder with identifier 'parentFolderIdentifier' not found")
    }

    @Test
    fun `document type can be changed`() {
        val document = aDocument(identifier = "documentIdentifier")
        val documentType = aDocumentType()
        every { documentRepository.findByIdentifier("documentIdentifier") } returns document
        every { documentTypeRepository.findByIdentifier("documentTypeIdentifier") } returns documentType
        every { documentRepository.save(document) } returns Unit

        documentUseCases.changeType("documentIdentifier", "documentTypeIdentifier")

        assertThat(document.type).isEqualTo(documentType)
    }

    @Test
    fun `document predicted type can be changed`() {
        val document = aDocument(identifier = "documentIdentifier")
        val documentType = aDocumentType()
        every { documentRepository.findByIdentifier("documentIdentifier") } returns document
        every { documentTypeRepository.findByIdentifier("documentTypeIdentifier") } returns documentType
        every { documentRepository.save(document) } returns Unit

        documentUseCases.changePredictedType("documentIdentifier", "documentTypeIdentifier")

        assertThat(document.predictedType).isEqualTo(documentType)
    }
}
