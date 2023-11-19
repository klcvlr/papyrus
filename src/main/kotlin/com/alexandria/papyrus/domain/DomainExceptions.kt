package com.alexandria.papyrus.domain

class FolderNotFoundException(private val identifier: String) : Exception() {
    override val message: String
        get() = "Folder Template with identifier $identifier not found"
}

class FolderTemplateNotFoundException(private val identifier: String) : Exception() {
    override val message: String
        get() = "Folder Template with identifier $identifier not found"
}
