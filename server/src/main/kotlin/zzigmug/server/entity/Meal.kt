package zzigmug.server.entity

import zzigmug.server.data.MealRequestDto
import java.time.Instant
import java.time.LocalDateTime
import javax.persistence.*

@Entity
class Meal (
    @ManyToOne
    @JoinColumn(name = "user_id")
    var user: User,

    @Column
    var date: LocalDateTime,

    @Column
    var image: String? = null,

    @OneToMany(cascade = [CascadeType.ALL], mappedBy = "meal")
    var dishList: MutableList<Dish> = mutableListOf()
): BaseEntity() {
    constructor(requestDto: MealRequestDto, user: User): this(user, requestDto.date, requestDto.image)
}