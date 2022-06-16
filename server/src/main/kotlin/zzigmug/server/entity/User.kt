package zzigmug.server.entity

import javax.persistence.Column
import javax.persistence.Entity

@Entity
class User (
    @Column
    var nickname: String,

    @Column
    var email: String,

    @Column
    var height: Long,

    @Column
    var weight: Long,

    @Column
    var goal: Long,

    @Column
    var gender: Boolean? = null,

    @Column
    var agree_marketing: Boolean,

    @Column
    var status: String,

    @Column
    var exp: Long,
): BaseEntity()