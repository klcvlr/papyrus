package com.alexandria.papyrus.adapters.integration.repositories

import com.alexandria.papyrus.domain.repositories.DocumentCategoryRepository
import com.alexandria.papyrus.domain.repositories.FileRepository
import com.alexandria.papyrus.fakes.aDocumentCategory
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
class DocumentCategoryJpaRepositoryIntegrationTest {
    @Autowired
    private lateinit var documentCategoryRepository: DocumentCategoryRepository

    @MockkBean
    private lateinit var fileRepository: FileRepository

    @Test
    fun `a new documentCategory can be saved`() {
        val documentCategory = aDocumentCategory(identifier = "documentCategoryId", name = "picture")

        documentCategoryRepository.save(documentCategory)

        val savedDocumentCategory = documentCategoryRepository.findByIdentifier("documentCategoryId")
        assertThat(savedDocumentCategory?.identifier).isEqualTo("documentCategoryId")
        assertThat(savedDocumentCategory?.name).isEqualTo("picture")
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
