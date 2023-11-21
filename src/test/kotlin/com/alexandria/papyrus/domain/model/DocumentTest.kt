package com.alexandria.papyrus.domain.model

import com.alexandria.papyrus.fakes.aDocument
import com.alexandria.papyrus.fakes.aDocumentType
import com.alexandria.papyrus.fakes.aFolder
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class DocumentTest {
    @Test
    fun `a document can be created`() {
        // I usually don't test that a constructor constructs... but this one has some logic about the root folder
        val rootFolder = aFolder(identifier = "rootFolderIdentifier")
        val folder = aFolder(identifier = "folderIdentifier", parentFolder = rootFolder)
        val documentType = aDocumentType(identifier = "documentTypeIdentifier")
        val predictedDocumentType = aDocumentType(identifier = "predictedDocumentTypeIdentifier")

        val document =
            aDocument(
                identifier = "documentIdentifier",
                name = "documentName",
                parentFolder = folder,
                type = documentType,
                predictedType = predictedDocumentType,
            )

        assertThat(document.identifier).isEqualTo("documentIdentifier")
        assertThat(document.name).isEqualTo("documentName")
        assertThat(document.parentFolder).isEqualTo(folder)
        assertThat(document.rootFolder).isEqualTo(rootFolder)
        assertThat(document.type).isEqualTo(documentType)
        assertThat(document.predictedType).isEqualTo(predictedDocumentType)
    }

    @Test
    fun `a document can be renamed`() {
        val document = aDocument(name = "oldName")

        document.rename("newName")

        assertThat(document.name).isEqualTo("newName")
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
    fun `a document's type can be changed`() {
        val oldDocumentType = aDocumentType(identifier = "oldDocumentTypeIdentifier")
        val newDocumentType = aDocumentType(identifier = "newDocumentTypeIdentifier")
        val document = aDocument(type = oldDocumentType)

        document.changeType(newDocumentType)

        assertThat(document.type).isEqualTo(newDocumentType)
    }

    @Test
    fun `a document's predicted type can be changed`() {
        val oldDocumentType = aDocumentType(identifier = "oldDocumentType")
        val newDocumentType = aDocumentType(identifier = "newDocumentType")
        val document = aDocument(predictedType = oldDocumentType)

        document.changePredictedType(newDocumentType)

        assertThat(document.predictedType).isEqualTo(newDocumentType)
    }
}
