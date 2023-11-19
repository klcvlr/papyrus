package com.alexandria.papyrus.infrastructure.repositories

import com.alexandria.papyrus.fakes.aDocument
import com.alexandria.papyrus.fakes.aFolder
import com.alexandria.papyrus.fakes.aFolderTemplate
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class DocumentJpaRepositoryTest {
    @Autowired
    private lateinit var documentRepository: DocumentJpaRepository

    @Autowired
    private lateinit var folderRepository: FolderJpaRepository

    @Autowired
    private lateinit var folderTemplateRepository: FolderTemplateJpaRepository

    @Test
    fun `a new document can be saved`() {
        // I know! I'll fix this later ^^' ...
        val folderTemplate = aFolderTemplate()
        folderTemplateRepository.save(folderTemplate)
        val folder = aFolder(template = folderTemplate)
        folderRepository.save(folder)
        val document = aDocument(identifier = "documentId", parentFolder = folder)
        documentRepository.save(document)
        println(document)

        documentRepository.save(document)

        val savedDocument = documentRepository.findByIdentifier("documentId")
        assertThat(savedDocument?.identifier).isEqualTo("documentId")
        assertThat(savedDocument?.name).isEqualTo(document.name)
        assertThat(savedDocument?.parentFolder).isEqualTo(document.parentFolder)
        assertThat(savedDocument?.type).isEqualTo(document.type)
        assertThat(savedDocument?.predictedType).isEqualTo(document.predictedType)
    }
}
