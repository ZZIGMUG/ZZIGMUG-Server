package zzigmug.server.entity

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
class Food (
    @Column
    var name: String,

    @Column
    var calories: Long,

    @Column
    var calbo: Long,

    @Column
    var fat: Long,

    @Column
    var protein: Long,

    ): BaseEntity()