package zzigmug.server.entity

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import zzigmug.server.data.LoginType
import zzigmug.server.data.RoleType
import javax.persistence.Column
import javax.persistence.Entity

@Entity
class User (
    @Column
    var nickname: String? = null,

    @Column
    var email: String,

    @Column
    var height: Long? = null,

    @Column
    var weight: Long? = null,

    @Column
    var goal: Long? = null,

    @Column
    var gender: Boolean? = null,

    @Column
    var agree_marketing: Boolean? = null,

    @Column
    var exp: Long? = null,

    @Column
    var role: RoleType,

    @Column
    var loginType: LoginType,

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