package zzigmug.server.entity

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.Instant
import javax.persistence.*

@MappedSuperclass
@EntityListeners(AuditingEntityListener::class)
abstract class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    @CreatedDate
    lateinit var createAt: Instant

    @LastModifiedDate
    lateinit var updateAt: Instant

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        val otherEntity = (other as BaseEntity) ?: return false
        return this.id == otherEntity.id
    }

    override fun hashCode(): Int {
        val prime = 59
        val result = 1

        return result * prime + (id?.hashCode() ?: 43)
    }


}