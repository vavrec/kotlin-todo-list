package cz.vavrecka.todolist.domain

import com.fasterxml.jackson.annotation.JsonIgnore
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.PersistenceCreator
import org.springframework.data.annotation.Transient
import org.springframework.data.domain.Persistable
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.util.UUID

@Table("task")
data class Task(
    @Id @Column("task_id") @JvmField val id: UUID,
    val name: String,
    @Column("list_id") val listId: UUID,
    @JvmField @Transient @JsonIgnore val isNew: Boolean
) :
    Persistable<UUID> {

    @PersistenceCreator
    constructor(id: UUID, name: String, listId: UUID) : this(id, name, listId, false)

    override fun getId() = id

    override fun isNew() = isNew
}
