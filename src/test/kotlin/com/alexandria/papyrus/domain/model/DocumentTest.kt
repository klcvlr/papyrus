package com.alexandria.papyrus.domain.model

import com.alexandria.papyrus.fakes.aDocument
import com.alexandria.papyrus.fakes.aDocumentType
import com.alexandria.papyrus.fakes.aFolder
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

class DocumentTest {
    @Test
    fun `a document can be renamed`() {
        val document = aDocument(name = "oldName")

        document.rename("newName")

        Assertions.assertThat(document.name).isEqualTo("newName")
    }

    @Test
    fun `a document can be added to a folder`() {
        val folder = aFolder()
        val document = aDocument()

        document.addToFolder(folder)

        Assertions.assertThat(document.parentFolder).isEqualTo(folder)
    }

    @Test
    fun `a document's type can be changed`() {
        val oldDocumentType = aDocumentType(identifier = "oldDocumentTypeIdentifier")
        val newDocumentType = aDocumentType(identifier = "newDocumentTypeIdentifier")
        val document = aDocument(type = oldDocumentType)

        document.changeType(newDocumentType)

        Assertions.assertThat(document.type).isEqualTo(newDocumentType)
    }

    @Test
    fun `a document's predicted type can be changed`() {
        val oldDocumentType = aDocumentType(identifier = "oldDocumentType")
        val newDocumentType = aDocumentType(identifier = "newDocumentType")
        val document = aDocument(predictedType = oldDocumentType)

        document.changePredictedType(newDocumentType)

        Assertions.assertThat(document.predictedType).isEqualTo(newDocumentType)
    }
}
