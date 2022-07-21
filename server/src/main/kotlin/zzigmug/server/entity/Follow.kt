package zzigmug.server.entity

import javax.persistence.*

@Entity
class Follow (

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "follower_id")
    var follower: User,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "following_id")
    var following: User,
): BaseEntity() {

}