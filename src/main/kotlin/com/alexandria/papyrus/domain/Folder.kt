package com.alexandria.papyrus.domain

class Folder(
    identifier: String,
    templateIdentifier: String,
    name: String,
    parentFolder: Folder? = null,
    associatedDocumentType: DocumentType? = null
) {
    private val _identifier: String = identifier
    private val _templateIdentifier: String = templateIdentifier
    private var _name: String = name
    private var _parentFolder: Folder? = parentFolder
    private var _associatedDocumentType: DocumentType? = associatedDocumentType
    private var _subFolders: List<Folder> = mutableListOf()
    private var _documents: List<Document> = mutableListOf()


    fun rename(newName: String) {
        _name = newName
    }

    fun changeAssociatedDocumentType(documentType: DocumentType) {
        _associatedDocumentType = documentType
    }

    fun addDocument(document: Document) {
        this._documents = _documents.plus(document)
    }

    fun addSubFolder(folder: Folder) {
        folder._parentFolder = this
        this._subFolders = _subFolders.plus(folder)
    }


    // ------------------ GETTERS ------------------
    val identifier: String get() = _identifier
    val templateIdentifier: String get() = _templateIdentifier
    val parentFolder: Folder? get() = _parentFolder
    val name: String get() = _name
    val associatedDocumentType: DocumentType? get() = _associatedDocumentType
    val subFolders: List<Folder> get() = _subFolders
    val documents: List<Document> get() = _documents

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Folder

        return _identifier == other._identifier
    }

    override fun hashCode(): Int {
        return _identifier.hashCode()
    }

    override fun toString(): String {
        return "Folder(_identifier='$_identifier', _templateIdentifier='$_templateIdentifier', _name='$_name', _parentFolder=$_parentFolder, _associatedDocumentType=$_associatedDocumentType, _subFolders=$_subFolders, _documents=$_documents)"
    }

}