package zzigmug.server.entity

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
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
): BaseEntity(), UserDetails {
    override fun getAuthorities(): MutableCollection<out GrantedAuthority>? {
        return null
    }

    override fun getPassword(): String? {
        return null
    }

    override fun getUsername(): String {
        return this.email
    }

    override fun isAccountNonExpired(): Boolean {
        return true
    }

    override fun isAccountNonLocked(): Boolean {
        return true
    }

    override fun isCredentialsNonExpired(): Boolean {
        return true
    }

    override fun isEnabled(): Boolean {
        return true
    }
}