package com.alexandria.papyrus.adapters.exposition.graphql

import com.alexandria.papyrus.application.DocumentUseCases
import com.alexandria.papyrus.domain.model.Document
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Controller

@Controller
class GraphQLDocumentController(private val documentUseCases: DocumentUseCases) {
    @QueryMapping
    fun documentByIdentifier(
        @Argument identifier: String,
        authentication: Authentication,
    ): Document {
        return documentUseCases.findByIdentifier(identifier)
    }
}
