package cz.vavrecka.todolist.domain

import com.fasterxml.jackson.annotation.JsonIgnore
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.PersistenceCreator
import org.springframework.data.annotation.Transient
import org.springframework.data.domain.Persistable
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.util.*

@Table("to_do_list_user")
data class User(
    @Id @Column("user_id") @JvmField val id: UUID,
    val name: String,
    val email: String,
    @JvmField @Transient @JsonIgnore val isNew: Boolean
) : Persistable<UUID> {

    @PersistenceCreator
    constructor(id: UUID, name: String, email: String) : this(id, name, email, false)

    override fun getId() = this.id

    override fun isNew() = this.isNew
}



