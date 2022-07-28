package zzigmug.server.entity

import zzigmug.server.data.PhotoRequestDto
import java.time.LocalDateTime
import javax.persistence.*

@Entity
class Photo (
    @ManyToOne
    @JoinColumn(name = "user_id")
    var user: User,

    @Column
    var date: LocalDateTime,

    @Column
    var image: String? = null,

    @OneToMany(cascade = [CascadeType.ALL], mappedBy = "photo")
    var dishList: MutableList<Dish> = mutableListOf()
): BaseEntity() {
    constructor(requestDto: PhotoRequestDto, user: User): this(user, requestDto.date, requestDto.image)
}