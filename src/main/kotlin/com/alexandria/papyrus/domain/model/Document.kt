package com.alexandria.papyrus.domain.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne

@Entity(name = "document")
class Document internal constructor() {
    @Id
    @Column(name = "identifier")
    private var _identifier: String = ""

    @Column(name = "name")
    private var _name: String = ""

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_folder_identifier", referencedColumnName = "identifier")
    private var _parentFolder: Folder? = null

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "root_folder_identifier", referencedColumnName = "identifier")
    private var _rootFolder: Folder? = null

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "type_identifier", referencedColumnName = "identifier")
    private var _type: DocumentType? = null

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "predicted_type_identifier", referencedColumnName = "identifier")
    private var _predictedType: DocumentType? = null

    @Column(name = "user_identifier")
    private var _user: String = ""

    @Column(name = "status")
    private var _status: String = ""

    @Column(name = "file_identifier")
    private var _fileIdentifier: String = ""

    internal constructor(
        identifier: String,
        name: String,
        parentFolder: Folder,
        type: DocumentType? = null,
        predictedType: DocumentType? = null,
        user: String,
        fileIdentifier: String,
    ) : this() {
        this._identifier = identifier
        this._name = name
        this._parentFolder = parentFolder
        this._rootFolder = parentFolder.rootFolder
        this._type = type
        this._predictedType = predictedType
        this._user = user
        this._status = "CREATED"
        this._fileIdentifier = fileIdentifier
    }

    fun rename(newName: String) {
        _name = newName
    }

    fun changeType(newType: DocumentType) {
        _type = newType
    }

    fun changePredictedType(newPredictedType: DocumentType) {
        _predictedType = newPredictedType
    }

    fun addToFolder(folder: Folder) {
        _rootFolder = folder.rootFolder
        _parentFolder = folder
    }

    fun changeParentFolder(folder: Folder) {
        _parentFolder = folder
        _rootFolder = folder.rootFolder
    }

    fun changeStatus(status: String) {
        _status = status
    }

    // ------------------ GETTERS ------------------
    val identifier: String get() = _identifier
    val parentFolder: Folder get() = _parentFolder!!
    val rootFolder: Folder get() = _rootFolder!!
    val name: String get() = _name
    val type: DocumentType? get() = _type
    val predictedType: DocumentType? get() = _predictedType
    val user: String get() = _user
    val status: String get() = _status
    val fileIdentifier: String get() = _fileIdentifier

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Document

        return _identifier == other._identifier
    }

    override fun hashCode(): Int {
        return _identifier.hashCode()
    }
}
