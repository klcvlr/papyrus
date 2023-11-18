package com.alexandria.papyrus.domain

import com.alexandria.papyrus.fakes.aDocument
import com.alexandria.papyrus.fakes.aFolder
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.just
import io.mockk.runs
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
        val document = aDocument()
        val folder = aFolder()
        every { folderRepository.save(any()) } just runs
        every { documentRepository.save(any()) } just runs

        folderAndDocumentService.addDocumentToFolder(document, folder)

        assertThat(document.parentFolder).isEqualTo(folder)
        assertThat(folder.documents).contains(document)
    }

}