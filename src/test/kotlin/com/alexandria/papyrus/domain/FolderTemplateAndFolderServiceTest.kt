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
        val folder1 = aFolder(template = folderTemplate, name = "oldName")
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
        val folder1 = aFolder(template = folderTemplate)
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
        val folder1 = aFolder(template = folderTemplate)
        val folder2 = aFolder()
        val documentType = aDocumentType(identifier = "documentTypeIdentifier")
        every { folderRepository.findAll() } returns listOf(folder1, folder2)
        every { folderTemplateRepository.save(any()) } just runs
        every { folderRepository.saveAll(any()) } just runs

        folderTemplateAndFolderService.changeAssociatedDocumentType(folderTemplate, documentType)

        assertThat(folder1.associatedDocumentType).isEqualTo(documentType)
        assertThat(folder2.associatedDocumentType).isNull()
    }

    @Test
    fun `creating a folder from a folderTemple creates the entire folder structure`() {
        val rootTemplateFolder = aFolderTemplate(name = "root")
        val verstappenTemplate = aFolderTemplate(name = "Verstappen")
        val leclercTemplate = aFolderTemplate(name = " Leclerc", associatedDocumentType = aDocumentType("LeclercType"))
        val perezTemplate = aFolderTemplate(name = "Pérez", associatedDocumentType = aDocumentType("PérezType"))
        rootTemplateFolder.addSubFolder(verstappenTemplate)
        rootTemplateFolder.addSubFolder(leclercTemplate)
        leclercTemplate.addSubFolder(perezTemplate)
        every { idGenerator.generate() } returns Faker().random.nextUUID()

        val rootFolder = folderTemplateAndFolderService.createFolderFromTemplate(rootTemplateFolder)

        // Let's poop 💩 all over that 'one assert per test' rule
        assertThat(rootFolder.subFolders).hasSize(2)
        assertThat(rootFolder.identifier).isNotEqualTo(rootTemplateFolder.identifier)
        val verstappenFolder = rootFolder.subFolders[0]
        assertThat(verstappenFolder.identifier).isNotEqualTo(verstappenTemplate.identifier)
        assertThat(verstappenFolder.parentFolder).isEqualTo(rootFolder)
        assertThat(verstappenFolder.subFolders).hasSize(0)
        assertThat(verstappenFolder.name).isEqualTo("Verstappen")
        val leclercFolder = rootFolder.subFolders[1]
        assertThat(leclercFolder.identifier).isNotEqualTo(leclercTemplate.identifier)
        assertThat(leclercFolder.parentFolder).isEqualTo(rootFolder)
        assertThat(leclercFolder.subFolders).hasSize(1)
        assertThat(leclercFolder.name).isEqualTo(" Leclerc")
        assertThat(leclercFolder.associatedDocumentType?.identifier).isEqualTo("LeclercType")
        val perezFolder = leclercFolder.subFolders[0]
        assertThat(perezFolder.identifier).isNotEqualTo(perezTemplate.identifier)
        assertThat(perezFolder.parentFolder).isEqualTo(leclercFolder)
        assertThat(perezFolder.subFolders).hasSize(0)
        assertThat(perezFolder.name).isEqualTo("Pérez")
        assertThat(perezFolder.associatedDocumentType?.identifier).isEqualTo("PérezType")
    }

}