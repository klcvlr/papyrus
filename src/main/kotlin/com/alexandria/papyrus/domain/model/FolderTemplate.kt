package com.alexandria.papyrus.domain.model

import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToMany

@Entity
class FolderTemplate internal constructor() {
    @Id
    @Column(name = "identifier")
    private var _identifier: String = ""

    @Column(name = "name")
    private var _name: String = ""

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_folder_identifier", referencedColumnName = "identifier")
    private var _parentFolder: FolderTemplate? = null

    @OneToMany(mappedBy = "_parentFolder", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    private var _subFolders: MutableList<FolderTemplate> = mutableListOf()

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "associated_document_type_identifier", referencedColumnName = "identifier")
    private var _associatedDocumentType: DocumentType? = null

    @Column(name = "user_identifier")
    private var _user: String = ""

    internal constructor(
        identifier: String,
        name: String,
        parentFolder: FolderTemplate? = null,
        associatedDocumentType: DocumentType? = null,
        user: String,
    ) : this() {
        this._identifier = identifier
        this._name = name
        this._parentFolder = parentFolder
        this._associatedDocumentType = associatedDocumentType
        this._user = user
    }

    fun rename(newName: String) {
        _name = newName
    }

    fun changeAssociatedDocumentType(documentType: DocumentType) {
        _associatedDocumentType = documentType
    }

    fun addSubFolder(subFolder: FolderTemplate) {
        subFolder._parentFolder = this
        this._subFolders.add(subFolder)
    }

    // ------------------ GETTERS ------------------
    val identifier: String get() = _identifier
    val parentFolder: FolderTemplate? get() = _parentFolder
    val name: String get() = _name
    val documentType: DocumentType? get() = _associatedDocumentType
    val subFolders: List<FolderTemplate> get() = _subFolders
    val user: String get() = _user

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as FolderTemplate

        return _identifier == other._identifier
    }

    override fun hashCode(): Int {
        return _identifier.hashCode()
    }
}
