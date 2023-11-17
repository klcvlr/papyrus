package com.alexandria.papyrus.domain

import com.alexandria.papyrus.domain.utils.IdGenerator
import com.alexandria.papyrus.fakes.aDocumentType
import com.alexandria.papyrus.fakes.aFolder
import com.alexandria.papyrus.fakes.aFolderTemplate
import io.github.serpro69.kfaker.Faker
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
class FolderTemplateAndFolderServiceTest {

    @MockK
    private lateinit var folderTemplateRepository: FolderTemplateRepository

    @MockK
    private lateinit var folderRepository: FolderRepository

    @MockK
    private lateinit var idGenerator: IdGenerator

    @InjectMockKs
    private lateinit var folderTemplateAndFolderService: FolderTemplateAndFolderService

    @Test
    fun `renaming a folderTemplate causes all folders created from that template to be renamed`() {
        val folderTemplate = aFolderTemplate(identifier = "folderTemplateIdentifier")
        val folder1 = aFolder(templateIdentifier = "folderTemplateIdentifier", name = "oldName")
        val folder2 = aFolder(name = "oldName")
        every { folderRepository.findAll() } returns listOf(folder1, folder2)
        every { folderTemplateRepository.save(any()) } just runs
        every { folderRepository.saveAll(any()) } just runs

        folderTemplateAndFolderService.rename(folderTemplate, "newName")

        assertThat(folder1.name).isEqualTo("newName")
        assertThat(folder2.name).isEqualTo("oldName")
    }

    @Test
    fun `adding a SubFolder to FolderTemplate propagates to all the Folders created from that FolderTemplate`() {
        val folderTemplate = aFolderTemplate(identifier = "folderTemplateIdentifier")
        val folder1 = aFolder(templateIdentifier = "folderTemplateIdentifier")
        val folder2 = aFolder()
        every { folderRepository.findAll() } returns listOf(folder1, folder2)
        every { folderTemplateRepository.save(any()) } just runs
        every { folderRepository.saveAll(any()) } just runs
        every { idGenerator.generate() } returns Faker().random.nextUUID()

        folderTemplateAndFolderService.addSubFolderTemplate(folderTemplate, "subFolderTemplateName")

        assertThat(folder1.subFolders).hasSize(1)
        assertThat(folder1.subFolders.first().name).isEqualTo("subFolderTemplateName")
        assertThat(folder2.subFolders).isEmpty()
    }

    @Test
    fun `changing the associated document type of a FolderTemplate propagates to all the Folders created from that FolderTemplate`() {
        val folderTemplate = aFolderTemplate(identifier = "folderTemplateIdentifier")
        val folder1 = aFolder(templateIdentifier = "folderTemplateIdentifier")
        val folder2 = aFolder()
        val documentType = aDocumentType(identifier = "documentTypeIdentifier")
        every { folderRepository.findAll() } returns listOf(folder1, folder2)
        every { folderTemplateRepository.save(any()) } just runs
        every { folderRepository.saveAll(any()) } just runs

        folderTemplateAndFolderService.changeAssociatedDocumentType(folderTemplate, documentType)

        assertThat(folder1.associatedDocumentType).isEqualTo(documentType)
        assertThat(folder2.associatedDocumentType).isNull()
    }

}