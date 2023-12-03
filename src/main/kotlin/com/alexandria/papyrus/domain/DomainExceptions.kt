package com.alexandria.papyrus.domain

class FolderNotFoundException(private val identifier: String) : Exception() {
    override val message: String
        get() = "Folder with identifier '$identifier' not found"
}

class FolderTemplateNotFoundException(private val identifier: String) : Exception() {
    override val message: String
        get() = "Folder Template with identifier '$identifier' not found"
}

class DocumentNotFoundException(private val identifier: String) : Exception() {
    override val message: String
        get() = "Document with identifier '$identifier' not found"
}

class DocumentCategoryNotFoundException(private val identifier: String) : Exception() {
    override val message: String
        get() = "DocumentCategory with identifier '$identifier' not found"
}

class FileNotFoundException(private val identifier: String) : Exception() {
    override val message: String
        get() = "File with identifier '$identifier' not found"
}
