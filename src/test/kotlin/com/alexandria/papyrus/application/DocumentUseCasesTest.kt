package com.alexandria.papyrus.application

import com.alexandria.papyrus.domain.DocumentNotFoundException
import com.alexandria.papyrus.domain.FolderNotFoundException
import com.alexandria.papyrus.domain.IdGenerator
import com.alexandria.papyrus.domain.notification.NotificationPublisher
import com.alexandria.papyrus.domain.repositories.DocumentCategoryRepository
import com.alexandria.papyrus.domain.repositories.DocumentRepository
import com.alexandria.papyrus.domain.repositories.FileRepository
import com.alexandria.papyrus.domain.repositories.FolderRepository
import com.alexandria.papyrus.fakes.aDocument
import com.alexandria.papyrus.fakes.aDocumentCategory
import com.alexandria.papyrus.fakes.aFileWrapper
import com.alexandria.papyrus.fakes.aUser
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
    private lateinit var documentCategoryRepository: DocumentCategoryRepository

    @MockK
    private lateinit var folderRepository: FolderRepository

    @MockK
    private lateinit var fileRepository: FileRepository

    @MockK
    private lateinit var notificationPublisher: NotificationPublisher

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
        val document = aDocument(identifier = "documentIdentifier", name = "documentName")
        val fileWrapper = aFileWrapper()
        val user = aUser()
        every { idGenerator.generate() } returns document.identifier
        every { documentRepository.save(document) } returns Unit
        every { folderRepository.findByIdentifier("parentFolderIdentifier") } returns document.parentFolder
        every { fileRepository.save(any(), fileWrapper) } returns Unit
        every { notificationPublisher.sendUploadNotification(document.identifier) } returns Unit

        val createdDocumentIdentifier = documentUseCases.createDocument("parentFolderIdentifier", fileWrapper, user)

        assertThat(createdDocumentIdentifier).isEqualTo(document.identifier)
    }

    @Test
    fun `document file can be downloaded`() {
        val document = aDocument(identifier = "documentIdentifier", name = "documentName.txt", fileIdentifier = "fileIdentifier")
        val fileWrapper = aFileWrapper("documentName.txt", "fileContent".toByteArray(), "text/plain")
        every { documentRepository.findByIdentifier("documentIdentifier") } returns document
        every { fileRepository.findByIdentifier("fileIdentifier") } returns fileWrapper

        val file = documentUseCases.downloadDocumentByIdentifier("documentIdentifier")

        assertThat(file.name).isEqualTo(fileWrapper.name)
        assertThat(file.contentType).isEqualTo(fileWrapper.contentType)
        assertThat(file.content).isEqualTo(fileWrapper.content)
    }

    @Test
    fun `an exception is thrown when creating a document in a folder that does not exist`() {
        val fileWrapper = aFileWrapper()
        val user = aUser()
        every { folderRepository.findByIdentifier(any()) } returns null
        every { fileRepository.save(any(), fileWrapper) } returns Unit

        assertThatThrownBy {
            documentUseCases.createDocument("parentFolderIdentifier", fileWrapper, user)
        }.isInstanceOf(
            FolderNotFoundException::class.java,
        ).hasMessage("Folder with identifier 'parentFolderIdentifier' not found")
    }

    @Test
    fun `document category can be changed`() {
        val document = aDocument(identifier = "documentIdentifier")
        val documentCategory = aDocumentCategory()
        val user = aUser()
        every { documentRepository.findByIdentifier("documentIdentifier") } returns document
        every { documentCategoryRepository.findByIdentifier("documentCategoryIdentifier") } returns documentCategory
        every { documentRepository.save(document) } returns Unit

        documentUseCases.changeType("documentIdentifier", "documentCategoryIdentifier", user)

        assertThat(document.category).isEqualTo(documentCategory)
    }

    @Test
    fun `document predicted category can be changed`() {
        val document = aDocument(identifier = "documentIdentifier")
        val documentCategory = aDocumentCategory()
        val user = aUser()
        every { documentRepository.findByIdentifier("documentIdentifier") } returns document
        every { documentCategoryRepository.findByIdentifier("documentCategoryIdentifier") } returns documentCategory
        every { documentRepository.save(document) } returns Unit

        documentUseCases.changePredictedCategory("documentIdentifier", "documentCategoryIdentifier", user)

        assertThat(document.predictedCategory).isEqualTo(documentCategory)
    }

    @Test
    fun `a document can be renamed`() {
        val document = aDocument(identifier = "documentIdentifier", name = "documentName")
        val user = aUser()
        every { documentRepository.findByIdentifier("documentIdentifier") } returns document
        every { documentRepository.save(document) } returns Unit

        documentUseCases.rename("documentIdentifier", "newName", user)

        assertThat(document.name).isEqualTo("newName")
    }
}
