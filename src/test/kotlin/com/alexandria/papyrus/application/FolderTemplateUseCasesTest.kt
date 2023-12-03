package com.alexandria.papyrus.application

import com.alexandria.papyrus.domain.FolderTemplateNotFoundException
import com.alexandria.papyrus.domain.IdGenerator
import com.alexandria.papyrus.domain.repositories.FolderTemplateRepository
import com.alexandria.papyrus.domain.services.FolderTemplateAndFolderService
import com.alexandria.papyrus.fakes.aFolderTemplate
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
class FolderTemplateUseCasesTest {
    @MockK
    private lateinit var idGenerator: IdGenerator

    @MockK
    private lateinit var folderTemplateRepository: FolderTemplateRepository

    @MockK
    private lateinit var folderTemplateAndFolderService: FolderTemplateAndFolderService

    @InjectMockKs
    private lateinit var folderTemplateUseCases: FolderTemplateUseCases

    @Test
    fun `a folder template can be found by its identifier`() {
        val folderTemplate = aFolderTemplate(identifier = "folderTemplateIdentifier")
        every { folderTemplateRepository.findByIdentifier("folderTemplateIdentifier") } returns folderTemplate

        val foundFolderTemplate = folderTemplateUseCases.findByIdentifier("folderTemplateIdentifier")

        assertThat(foundFolderTemplate).isEqualTo(folderTemplate)
    }

    @Test
    fun `an exception is thrown when looking up for a folder template by an identifier that does not exist`() {
        every { folderTemplateRepository.findByIdentifier("folderTemplateIdentifier") } returns null

        try {
            folderTemplateUseCases.findByIdentifier("folderTemplateIdentifier")
        } catch (e: Exception) {
            assertThat(e).isInstanceOf(FolderTemplateNotFoundException::class.java)
        }
    }

    @Test
    fun `a folder template can be created`() {
        every { idGenerator.generate() } returns "folderTemplateIdentifier"
        every { folderTemplateRepository.save(any()) } returns Unit

        val folderTemplateIdentifier = folderTemplateUseCases.create("folderTemplateName", "userIdentifier")

        assertThat(folderTemplateIdentifier).isEqualTo("folderTemplateIdentifier")
        verify(exactly = 1) { folderTemplateRepository.save(any()) }
    }

    @Test
    fun `a folder template can be renamed`() {
        val folderTemplate = aFolderTemplate(identifier = "folderTemplateIdentifier", name = "folderTemplateName")
        every { folderTemplateRepository.findByIdentifier("folderTemplateIdentifier") } returns folderTemplate
        every { folderTemplateAndFolderService.rename(folderTemplate, "newFolderTemplateName") } returns Unit
        every { folderTemplateRepository.save(any()) } returns Unit

        folderTemplateUseCases.rename("folderTemplateIdentifier", "newFolderTemplateName", "userIdentifier")

        verify(exactly = 1) { folderTemplateRepository.save(folderTemplate) }
    }

    @Test
    fun `a sub folder template can be added to a folder template`() {
        val folderTemplate = aFolderTemplate(identifier = "folderTemplateIdentifier")
        every { folderTemplateRepository.findByIdentifier("folderTemplateIdentifier") } returns folderTemplate
        every {
            folderTemplateAndFolderService.addSubFolderTemplate(folderTemplate, "subFolderTemplateName", "userIdentifier")
        } returns "subFolderTemplateIdentifier"
        every { folderTemplateRepository.save(any()) } returns Unit

        val subFolderTemplateIdentifier =
            folderTemplateUseCases.addSubFolder(
                "folderTemplateIdentifier",
                "subFolderTemplateName",
                "userIdentifier",
            )

        assertThat(subFolderTemplateIdentifier).isEqualTo("subFolderTemplateIdentifier")
    }

    @Test
    fun `a folder template's associated document category can be changed`() {
        every {
            folderTemplateAndFolderService.changeAssociatedDocumentCategory(
                "folderTemplateIdentifier",
                "documentCategoryIdentifier",
                "userIdentifier",
            )
        } returns Unit

        folderTemplateUseCases.changeAssociatedDocumentCategory("folderTemplateIdentifier", "documentCategoryIdentifier", "userIdentifier")

        verify(
            exactly = 1,
        ) {
            folderTemplateAndFolderService.changeAssociatedDocumentCategory(
                "folderTemplateIdentifier",
                "documentCategoryIdentifier",
                "userIdentifier",
            )
        }
    }
}
