package com.alexandria.papyrus.domain.services

import com.alexandria.papyrus.domain.repositories.DocumentRepository
import com.alexandria.papyrus.domain.repositories.FolderRepository
import com.alexandria.papyrus.fakes.aDocument
import com.alexandria.papyrus.fakes.aFolder
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
class FolderAndDocumentServiceTest {
    @MockK
    private lateinit var folderRepository: FolderRepository

    @MockK
    private lateinit var documentRepository: DocumentRepository

    @InjectMockKs
    private lateinit var folderAndDocumentService: FolderAndDocumentService

    @Test
    fun `a document can be added to a folder`() {
        val folder = aFolder(identifier = "folderIdentifier")
        val document = aDocument(identifier = "documentIdentifier")
        every { folderRepository.findByIdentifier(folder.identifier) } returns folder
        every { documentRepository.findByIdentifier(document.identifier) } returns document
        every { folderRepository.save(any()) } returns Unit
        every { documentRepository.save(any()) } returns Unit

        folderAndDocumentService.addDocumentToFolder(document.identifier, folder.identifier)

        assertThat(folder.documents).contains(document)
        assertThat(document.parentFolder).isEqualTo(folder)
        assertThat(document.rootFolder).isEqualTo(folder.rootFolder)
    }
}
