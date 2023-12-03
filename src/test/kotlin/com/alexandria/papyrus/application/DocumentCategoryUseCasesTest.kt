package com.alexandria.papyrus.application

import com.alexandria.papyrus.domain.DocumentCategoryNotFoundException
import com.alexandria.papyrus.domain.IdGenerator
import com.alexandria.papyrus.domain.repositories.DocumentCategoryRepository
import com.alexandria.papyrus.fakes.aDocumentCategory
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
class DocumentCategoryUseCasesTest {
    @MockK
    private lateinit var idGenerator: IdGenerator

    @MockK
    private lateinit var documentCategoryRepository: DocumentCategoryRepository

    @InjectMockKs
    private lateinit var documentCategoryUseCases: DocumentCategoryUseCases

    @Test
    fun `document category can be found by identifier`() {
        val documentCategory = aDocumentCategory(identifier = "documentCategoryIdentifier")
        every { documentCategoryRepository.findByIdentifier("documentCategoryIdentifier") } returns documentCategory

        val foundDocumentCategory = documentCategoryUseCases.findByIdentifier("documentCategoryIdentifier")

        assertThat(foundDocumentCategory).isEqualTo(documentCategory)
    }

    @Test
    fun `an exception is thrown when looking up for an documentCategory by an identifier that does not exist`() {
        every { documentCategoryRepository.findByIdentifier(any()) } returns null

        assertThatThrownBy {
            documentCategoryUseCases.findByIdentifier("documentCategoryIdentifier")
        }.isInstanceOf(
            DocumentCategoryNotFoundException::class.java,
        ).hasMessage("DocumentCategory with identifier 'documentCategoryIdentifier' not found")
    }

    @Test
    fun `document category can be created`() {
        val documentCategory = aDocumentCategory(identifier = "documentCategoryIdentifier", user = "user")
        every { idGenerator.generate() } returns documentCategory.identifier
        every { documentCategoryRepository.save(any()) } returns documentCategory

        val createdDocumentCategoryIdentifier = documentCategoryUseCases.create("documentCategoryName", "user")

        assertThat(createdDocumentCategoryIdentifier).isEqualTo(documentCategory.identifier)
    }
}
