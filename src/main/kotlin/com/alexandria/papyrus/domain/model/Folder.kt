package com.alexandria.papyrus.domain.model

import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToMany

@Entity
class Folder internal constructor() {
    @Id
    @Column(name = "identifier")
    private var _identifier: String = ""

    @ManyToOne
    @JoinColumn(name = "template_identifier", referencedColumnName = "identifier")
    private var _template: FolderTemplate? = null

    @Column(name = "name")
    private var _name: String = ""

    @ManyToOne
    @JoinColumn(name = "parent_folder_identifier", referencedColumnName = "identifier")
    private var _parentFolder: Folder? = null

    @ManyToOne
    @JoinColumn(name = "root_folder_identifier", referencedColumnName = "identifier")
    private var _rootFolder: Folder? = null

    @ManyToOne
    @JoinColumn(name = "associated_document_type_identifier", referencedColumnName = "identifier")
    private var _associatedDocumentType: DocumentType? = null

    @OneToMany(mappedBy = "_parentFolder", cascade = [CascadeType.ALL])
    private var _subFolders: MutableList<Folder> = mutableListOf()

    @OneToMany(mappedBy = "_parentFolder", cascade = [CascadeType.ALL])
    private var _documents: MutableList<Document> = mutableListOf()

    internal constructor(
        identifier: String,
        template: FolderTemplate,
        name: String,
        parentFolder: Folder? = null,
        associatedDocumentType: DocumentType? = null,
    ) : this() {
        this._identifier = identifier
        this._template = template
        this._name = name
        this._parentFolder = parentFolder
        this._associatedDocumentType = associatedDocumentType
        this._rootFolder = parentFolder?._rootFolder ?: this
    }

    fun rename(newName: String) {
        _name = newName
    }

    fun changeAssociatedDocumentType(documentType: DocumentType) {
        _associatedDocumentType = documentType
    }

    fun addDocument(document: Document) {
        _documents.add(document)
    }

    fun addSubFolder(folder: Folder) {
        folder._parentFolder = this
        folder._rootFolder = this._rootFolder
        _subFolders.add(folder)
    }

    // ------------------ GETTERS ------------------
    val identifier: String get() = _identifier
    val template: FolderTemplate get() = _template!!
    val parentFolder: Folder? get() = _parentFolder
    val rootFolder: Folder? get() = _rootFolder
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
}
