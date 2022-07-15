package zzigmug.server.entity

import zzigmug.server.data.DishRequestDto
import javax.persistence.*

@Entity
class Dish (
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "meal_id")
    var meal: Meal,

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "food_id")
    var food: Food,

    @Column
    var amount: Float,
): BaseEntity()