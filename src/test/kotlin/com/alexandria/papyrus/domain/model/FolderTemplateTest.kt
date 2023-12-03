package com.alexandria.papyrus.domain.model

import com.alexandria.papyrus.fakes.aDocumentCategory
import com.alexandria.papyrus.fakes.aFolderTemplate
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class FolderTemplateTest {
    @Test
    fun `a folderTemplate can be renamed`() {
        val folderTemplate = aFolderTemplate(name = "oldName")

        folderTemplate.rename("newName")

        assertThat(folderTemplate.name).isEqualTo("newName")
    }

    @Test
    fun `a folderTemplate's associated document category can be changed`() {
        val oldDocumentCategory = aDocumentCategory(identifier = "oldDocumentCategoryIdentifier")
        val folderTemplate = aFolderTemplate(associatedDocumentCategory = oldDocumentCategory)

        val newDocumentCategory = aDocumentCategory(identifier = "newDocumentCategoryIdentifier")
        folderTemplate.changeAssociatedDocumentCategory(newDocumentCategory)

        assertThat(folderTemplate.associatedDocumentCategory).isEqualTo(newDocumentCategory)
    }

    @Test
    fun `a folderTemplate can be added to another folderTemplate`() {
        val folderTemplate = aFolderTemplate()
        val subFolderTemplate = aFolderTemplate()

        folderTemplate.addSubFolder(subFolderTemplate)

        assertThat(folderTemplate.subFolders).contains(subFolderTemplate)
        assertThat(subFolderTemplate.parentFolder).isEqualTo(folderTemplate)
    }
}
