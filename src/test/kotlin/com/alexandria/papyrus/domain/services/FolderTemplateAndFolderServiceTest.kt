package com.alexandria.papyrus.domain.services

import com.alexandria.papyrus.domain.IdGenerator
import com.alexandria.papyrus.domain.repositories.DocumentCategoryRepository
import com.alexandria.papyrus.domain.repositories.FolderRepository
import com.alexandria.papyrus.domain.repositories.FolderTemplateRepository
import com.alexandria.papyrus.fakes.aDocumentCategory
import com.alexandria.papyrus.fakes.aFolder
import com.alexandria.papyrus.fakes.aFolderTemplate
import com.alexandria.papyrus.fakes.aUser
import io.github.serpro69.kfaker.Faker
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
class FolderTemplateAndFolderServiceTest {
    @MockK
    private lateinit var folderTemplateRepository: FolderTemplateRepository

    @MockK
    private lateinit var folderRepository: FolderRepository

    @MockK
    private lateinit var documentCategoryRepository: DocumentCategoryRepository

    @MockK
    private lateinit var idGenerator: IdGenerator

    @InjectMockKs
    private lateinit var folderTemplateAndFolderService: FolderTemplateAndFolderService

    @Test
    fun `renaming a folderTemplate causes all folders created from that template to be renamed`() {
        val folderTemplate = aFolderTemplate(identifier = "folderTemplateIdentifier")
        val folder1 = aFolder(template = folderTemplate, name = "oldName")
        val folder2 = aFolder(name = "oldName")
        every { folderRepository.findAllByTemplate("folderTemplateIdentifier") } returns listOf(folder1)
        every { folderTemplateRepository.save(any()) } returns Unit
        every { folderRepository.saveAll(any()) } returns Unit

        folderTemplateAndFolderService.rename(folderTemplate, "newName")

        Assertions.assertThat(folder1.name).isEqualTo("newName")
        Assertions.assertThat(folder2.name).isEqualTo("oldName")
    }

    @Test
    fun `adding a SubFolder to FolderTemplate propagates to all the Folders created from that FolderTemplate`() {
        val folderTemplate = aFolderTemplate(identifier = "folderTemplateIdentifier")
        val folder1 = aFolder(template = folderTemplate)
        val folder2 = aFolder()
        val user = aUser()
        every { folderRepository.findAllByTemplate("folderTemplateIdentifier") } returns listOf(folder1)
        every { folderTemplateRepository.save(any()) } returns Unit
        every { folderRepository.saveAll(any()) } returns Unit
        every { idGenerator.generate() } returns Faker().random.nextUUID()

        folderTemplateAndFolderService.addSubFolderTemplate(folderTemplate, "subFolderTemplateName", user)

        Assertions.assertThat(folder1.subFolders).hasSize(1)
        Assertions.assertThat(folder1.subFolders.first().name).isEqualTo("subFolderTemplateName")
        Assertions.assertThat(folder2.subFolders).isEmpty()
    }

    @Test
    fun `changing the associated document category of a FolderTemplate propagates to all the Folders created from that FolderTemplate`() {
        val folderTemplate = aFolderTemplate(identifier = "folderTemplateIdentifier")
        val user = aUser()
        val folder1 = aFolder(template = folderTemplate)
        val folder2 = aFolder()
        val documentCategory = aDocumentCategory(identifier = "documentCategoryIdentifier")
        every { folderRepository.findAllByTemplate("folderTemplateIdentifier") } returns listOf(folder1)
        every { folderTemplateRepository.findByIdentifier("folderTemplateIdentifier") } returns folderTemplate
        every { documentCategoryRepository.findByIdentifier("documentCategoryIdentifier") } returns documentCategory
        every { folderTemplateRepository.save(any()) } returns Unit
        every { folderRepository.saveAll(any()) } returns Unit

        folderTemplateAndFolderService.changeAssociatedDocumentCategory(folderTemplate.identifier, documentCategory.identifier, user)

        Assertions.assertThat(folder1.associatedDocumentCategory).isEqualTo(documentCategory)
        Assertions.assertThat(folder2.associatedDocumentCategory).isNull()
    }

    @Test
    fun `creating a folder from a folderTemple creates the entire folder structure`() {
        val user = aUser()
        val rootTemplateFolder = aFolderTemplate(name = "root")
        val verstappenTemplate = aFolderTemplate(name = "Verstappen")
        val leclercTemplate = aFolderTemplate(name = " Leclerc", associatedDocumentCategory = aDocumentCategory("LeclercType"))
        val perezTemplate = aFolderTemplate(name = "Pérez", associatedDocumentCategory = aDocumentCategory("PérezType"))
        rootTemplateFolder.addSubFolder(verstappenTemplate)
        rootTemplateFolder.addSubFolder(leclercTemplate)
        leclercTemplate.addSubFolder(perezTemplate)
        every { idGenerator.generate() } returns Faker().random.nextUUID()

        val rootFolder = folderTemplateAndFolderService.createFolderFromTemplate(rootTemplateFolder, user)

        // Let's poop 💩 all over that 'one assert per test' rule
        Assertions.assertThat(rootFolder.subFolders).hasSize(2)
        Assertions.assertThat(rootFolder.identifier).isNotEqualTo(rootTemplateFolder.identifier)
        val verstappenFolder = rootFolder.subFolders[0]
        Assertions.assertThat(verstappenFolder.identifier).isNotEqualTo(verstappenTemplate.identifier)
        Assertions.assertThat(verstappenFolder.parentFolder).isEqualTo(rootFolder)
        Assertions.assertThat(verstappenFolder.subFolders).hasSize(0)
        Assertions.assertThat(verstappenFolder.name).isEqualTo("Verstappen")
        val leclercFolder = rootFolder.subFolders[1]
        Assertions.assertThat(leclercFolder.identifier).isNotEqualTo(leclercTemplate.identifier)
        Assertions.assertThat(leclercFolder.parentFolder).isEqualTo(rootFolder)
        Assertions.assertThat(leclercFolder.subFolders).hasSize(1)
        Assertions.assertThat(leclercFolder.name).isEqualTo(" Leclerc")
        Assertions.assertThat(leclercFolder.associatedDocumentCategory?.identifier).isEqualTo("LeclercType")
        val perezFolder = leclercFolder.subFolders[0]
        Assertions.assertThat(perezFolder.identifier).isNotEqualTo(perezTemplate.identifier)
        Assertions.assertThat(perezFolder.parentFolder).isEqualTo(leclercFolder)
        Assertions.assertThat(perezFolder.subFolders).hasSize(0)
        Assertions.assertThat(perezFolder.name).isEqualTo("Pérez")
        Assertions.assertThat(perezFolder.associatedDocumentCategory?.identifier).isEqualTo("PérezType")
    }
}
