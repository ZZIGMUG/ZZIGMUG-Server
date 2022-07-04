package zzigmug.server.entity

import java.time.Instant
import javax.persistence.*

@Entity
class Meal (
    @ManyToOne
    @JoinColumn(name = "user_id")
    var user: User,

    @Column
    var date: Instant,

    @Column
    var image: String? = null,

    @OneToMany(cascade = [CascadeType.ALL], mappedBy = "meal")
    var dishList: MutableList<Dish> = mutableListOf()
): BaseEntity()