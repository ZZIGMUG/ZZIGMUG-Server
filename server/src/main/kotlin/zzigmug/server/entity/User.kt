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
    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        TODO("Not yet implemented")
    }

    override fun getPassword(): String {
        TODO("Not yet implemented")
    }

    override fun getUsername(): String {
        TODO("Not yet implemented")
    }

    override fun isAccountNonExpired(): Boolean {
        TODO("Not yet implemented")
    }

    override fun isAccountNonLocked(): Boolean {
        TODO("Not yet implemented")
    }

    override fun isCredentialsNonExpired(): Boolean {
        TODO("Not yet implemented")
    }

    override fun isEnabled(): Boolean {
        TODO("Not yet implemented")
    }
}