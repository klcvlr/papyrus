package com.alexandria.papyrus.adapters.exposition.graphql

import com.alexandria.papyrus.application.DocumentUseCases
import com.alexandria.papyrus.domain.model.Document
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.stereotype.Controller

@Controller
class GraphQLDocumentCategoryController(private val documentUseCases: DocumentUseCases) {
    @QueryMapping
    fun documentCategoryByIdentifier(
        @Argument identifier: String,
    ): Document {
        return documentUseCases.findByIdentifier(identifier)
    }
}
