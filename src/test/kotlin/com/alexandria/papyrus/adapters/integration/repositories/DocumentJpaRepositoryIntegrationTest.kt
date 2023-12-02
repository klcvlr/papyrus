package com.alexandria.papyrus.adapters.integration.repositories

import com.alexandria.papyrus.domain.repositories.FileRepository
import com.alexandria.papyrus.fakes.aDocument
import com.alexandria.papyrus.fakes.aFolder
import com.alexandria.papyrus.fakes.aFolderTemplate
import com.ninjasquad.springmockk.MockkBean
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.context.annotation.ComponentScan
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers

@DataJpaTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ComponentScan(basePackages = ["com.alexandria.papyrus.adapters.integration.repositories"])
class DocumentJpaRepositoryIntegrationTest {
    @Autowired
    private lateinit var documentRepository: DocumentJpaRepository

    @Autowired
    private lateinit var folderRepository: FolderJpaRepository

    @Autowired
    private lateinit var folderTemplateRepository: FolderTemplateJpaRepository

    @MockkBean
    private lateinit var fileRepository: FileRepository

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

    companion object {
        @Container
        @JvmStatic
        private val postgresqlContainer: PostgreSQLContainer<*> =
            PostgreSQLContainer("postgres:16.1-alpine")
                .withDatabaseName("papyrus")
                .withUsername("toth")
                .withPassword("parchment")

        @DynamicPropertySource
        @JvmStatic
        fun postgresqlProperties(registry: DynamicPropertyRegistry) {
            registry.add("spring.datasource.url", postgresqlContainer::getJdbcUrl)
            registry.add("spring.datasource.username", postgresqlContainer::getUsername)
            registry.add("spring.datasource.password", postgresqlContainer::getPassword)
        }
    }
}
