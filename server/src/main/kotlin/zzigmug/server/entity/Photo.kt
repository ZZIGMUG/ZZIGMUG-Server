package zzigmug.server.entity

import zzigmug.server.data.type.MealType
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

    @Column
    var mealType: MealType,

    @OneToMany(cascade = [CascadeType.ALL], mappedBy = "photo")
    var dishList: MutableList<Dish> = mutableListOf()
): BaseEntity()