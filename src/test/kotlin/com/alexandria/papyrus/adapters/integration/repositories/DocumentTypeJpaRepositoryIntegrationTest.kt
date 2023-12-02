package com.alexandria.papyrus.adapters.integration.repositories

import com.alexandria.papyrus.domain.repositories.DocumentTypeRepository
import com.alexandria.papyrus.domain.repositories.FileRepository
import com.alexandria.papyrus.fakes.aDocumentType
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
class DocumentTypeJpaRepositoryIntegrationTest {
    @Autowired
    private lateinit var documentTypeRepository: DocumentTypeRepository

    @MockkBean
    private lateinit var fileRepository: FileRepository

    @Test
    fun `a new documentType can be saved`() {
        val documentType = aDocumentType(identifier = "documentTypeId", name = "picture")

        documentTypeRepository.save(documentType)

        val savedDocumentType = documentTypeRepository.findByIdentifier("documentTypeId")
        assertThat(savedDocumentType?.identifier).isEqualTo("documentTypeId")
        assertThat(savedDocumentType?.name).isEqualTo("picture")
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
