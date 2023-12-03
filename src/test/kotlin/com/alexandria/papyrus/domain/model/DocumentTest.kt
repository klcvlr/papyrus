package com.alexandria.papyrus.domain.model

import com.alexandria.papyrus.fakes.aDocument
import com.alexandria.papyrus.fakes.aDocumentCategory
import com.alexandria.papyrus.fakes.aFolder
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class DocumentTest {
    @Test
    fun `a document can be created`() {
        // I usually don't test that a constructor constructs... but this one has some logic about the root folder
        val rootFolder = aFolder(identifier = "rootFolderIdentifier")
        val folder = aFolder(identifier = "folderIdentifier", parentFolder = rootFolder)
        val documentCategory = aDocumentCategory(identifier = "documentCategoryIdentifier")
        val predictedDocumentCategory = aDocumentCategory(identifier = "predictedDocumentCategoryIdentifier")

        val document =
            aDocument(
                identifier = "documentIdentifier",
                name = "documentName",
                parentFolder = folder,
                category = documentCategory,
                predictedCategory = predictedDocumentCategory,
                fileIdentifier = "fileIdentifier",
            )

        assertThat(document.identifier).isEqualTo("documentIdentifier")
        assertThat(document.name).isEqualTo("documentName")
        assertThat(document.parentFolder).isEqualTo(folder)
        assertThat(document.rootFolder).isEqualTo(rootFolder)
        assertThat(document.category).isEqualTo(documentCategory)
        assertThat(document.predictedCategory).isEqualTo(predictedDocumentCategory)
        assertThat(document.status).isEqualTo("CREATED")
        assertThat(document.fileIdentifier).isEqualTo("fileIdentifier")
    }

    @Test
    fun `a document can be renamed`() {
        val document = aDocument(name = "oldName")

        document.rename("newName")

        assertThat(document.name).isEqualTo("newName")
    }

    @Test
    fun `a document's category can be changed`() {
        val oldDocumentCategory = aDocumentCategory(identifier = "oldDocumentCategoryIdentifier")
        val newDocumentCategory = aDocumentCategory(identifier = "newDocumentCategoryIdentifier")
        val document = aDocument(category = oldDocumentCategory)

        document.changeCategory(newDocumentCategory)

        assertThat(document.category).isEqualTo(newDocumentCategory)
    }

    @Test
    fun `a document's predicted category can be changed`() {
        val oldDocumentCategory = aDocumentCategory(identifier = "oldDocumentCategory")
        val newDocumentCategory = aDocumentCategory(identifier = "newDocumentCategory")
        val document = aDocument(predictedCategory = oldDocumentCategory)

        document.changePredictedCategory(newDocumentCategory)

        assertThat(document.predictedCategory).isEqualTo(newDocumentCategory)
    }

    @Test
    fun `a document can be added to a folder`() {
        val rootFolder = aFolder(identifier = "rootFolderIdentifier")
        val folder = aFolder(identifier = "folderIdentifier", parentFolder = rootFolder)
        val document = aDocument("documentIdentifier")

        document.addToFolder(folder)

        assertThat(document.parentFolder).isEqualTo(folder)
        assertThat(document.rootFolder).isEqualTo(rootFolder)
    }

    @Test
    fun `a document's status can be changed`() {
        val document = aDocument()

        document.changeStatus("newStatus")

        assertThat(document.status).isEqualTo("newStatus")
    }
}
