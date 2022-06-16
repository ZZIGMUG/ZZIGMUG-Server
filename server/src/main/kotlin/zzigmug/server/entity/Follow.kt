package zzigmug.server.entity

import javax.persistence.*

@Entity
@IdClass(FollowId::class)
class Follow (

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "follower_id")
    var follower: User,

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "following_id")
    var following: User,
) {

}

data class FollowId(
    var follower: Long = 0L,
    var following: Long = 0,
): java.io.Serializable