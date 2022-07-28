package zzigmug.server.entity

import javax.persistence.*

@Entity
class Dish (
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "photo_id")
    var photo: Photo,

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "food_id")
    var food: Food,

    @Column
    var amount: Float,
): BaseEntity()