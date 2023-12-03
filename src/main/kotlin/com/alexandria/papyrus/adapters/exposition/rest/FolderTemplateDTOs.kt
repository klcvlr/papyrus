package com.alexandria.papyrus.adapters.exposition.rest

import com.alexandria.papyrus.adapters.exposition.rest.DocumentCategoryView.Companion.toDocumentCategoryView
import com.alexandria.papyrus.domain.model.FolderTemplate

data class CreateFolderTemplateRequest(val name: String)

data class CreateSubFolderTemplateRequest(val name: String)

data class RenameFolderTemplateRequest(val name: String)

data class ChangeAssociatedDocumentCategoryRequest(val categoryIdentifier: String)

data class FolderTemplateView(
    val identifier: String,
    val name: String,
    val associatedDocumentCategory: String?,
    val parentFolderIdentifier: String?,
) {
    companion object {
        fun toFolderTemplateView(folderTemplate: FolderTemplate): FolderTemplateView {
            return FolderTemplateView(
                identifier = folderTemplate.identifier,
                name = folderTemplate.name,
                associatedDocumentCategory = folderTemplate.associatedDocumentCategory?.identifier,
                parentFolderIdentifier = folderTemplate.parentFolder?.identifier,
            )
        }
    }
}

data class DetailedFolderTemplateView(
    val identifier: String,
    val name: String,
    val associatedDocumentCategory: DocumentCategoryView?,
    val subFolderTemplates: List<DetailedFolderTemplateView> = emptyList(),
    val parentFolderIdentifier: String?,
    val user: String,
) {
    companion object {
        fun toDetailedFolderTemplateView(folderTemplate: FolderTemplate): DetailedFolderTemplateView {
            return DetailedFolderTemplateView(
                identifier = folderTemplate.identifier,
                name = folderTemplate.name,
                associatedDocumentCategory = folderTemplate.associatedDocumentCategory?.let { toDocumentCategoryView(it) },
                parentFolderIdentifier = folderTemplate.parentFolder?.identifier,
                subFolderTemplates = folderTemplate.subFolders.map { toDetailedFolderTemplateView(it) },
                user = folderTemplate.user,
            )
        }
    }
}
