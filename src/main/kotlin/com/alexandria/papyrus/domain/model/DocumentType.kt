package com.alexandria.papyrus.domain.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id

@Entity
class DocumentType internal constructor() {
    @Id
    @Column(name = "identifier")
    private var _identifier: String = identifier

    @Column(name = "name")
    private var _name: String = name

    @Column(name = "user_identifier")
    private var _user: String = ""

    internal constructor(identifier: String, name: String, user: String) : this() {
        this._identifier = identifier
        this._name = name
        this._user = user
    }

    val identifier: String get() = _identifier
    val name: String get() = _name
    val user: String get() = _user

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as DocumentType

        return _identifier == other._identifier
    }

    override fun hashCode(): Int {
        return _identifier.hashCode()
    }
}
