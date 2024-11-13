package cz.vavrecka.todolist.domain

import org.springframework.data.annotation.Id
import org.springframework.data.annotation.PersistenceCreator
import org.springframework.data.annotation.Transient
import org.springframework.data.domain.Persistable
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.util.UUID

@Table("list")
data class List(@Id @Column("list_id") @JvmField val id: UUID, val name: String, @JvmField @Transient val isNew: Boolean) :
    Persistable<UUID> {

    @PersistenceCreator
    constructor(id: UUID, name: String) : this(id, name, false)

    override fun getId() = id

    override fun isNew() = isNew
}
