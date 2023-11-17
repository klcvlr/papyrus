package com.alexandria.papyrus.domain

class DocumentType(identifier: String, name: String) {
    private val _identifier: String = identifier
    private val _name: String = name

    val identifier: String get() = _identifier
    val name: String get() = _name


    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as DocumentType

        return _identifier == other._identifier
    }

    override fun hashCode(): Int {
        return _identifier.hashCode()
    }

    override fun toString(): String {
        return "DocumentType(_identifier='$_identifier', _name='$_name')"
    }

}