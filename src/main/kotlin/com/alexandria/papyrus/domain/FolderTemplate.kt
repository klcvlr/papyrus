package com.alexandria.papyrus.domain

class FolderTemplate(
    identifier: String,
    name: String,
    parentFolder: FolderTemplate?,
    associatedDocumentType: DocumentType?
) {
    private val _identifier: String = identifier
    private var _name: String = name
    private var _parentFolder: FolderTemplate? = parentFolder
    private var _subFolders: List<FolderTemplate> = mutableListOf()
    private var _associatedDocumentType: DocumentType? = associatedDocumentType

    fun addSubFolder(subFolder: FolderTemplate) {
        subFolder._parentFolder = this
        this._subFolders = this.subFolders.plus(subFolder)
    }

    fun changeAssociatedDocumentType(documentType: DocumentType) {
        _associatedDocumentType = documentType
    }

    // ------------------ GETTERS ------------------
    val identifier: String get() = _identifier
    val parentFolder: FolderTemplate? get() = _parentFolder
    val name: String get() = _name
    val documentType: DocumentType? get() = _associatedDocumentType
    val subFolders: List<FolderTemplate> get() = _subFolders

    fun rename(newName: String) {
        _name = newName
    }



    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as FolderTemplate

        return _identifier == other._identifier
    }

    override fun hashCode(): Int {
        return _identifier.hashCode()
    }

    override fun toString(): String {
        return "FolderTemplate(_identifier='$_identifier', _name='$_name', _parentFolder=$_parentFolder, _subFolders=$_subFolders, _associatedDocumentType=$_associatedDocumentType)"
    }

}