package zzigmug.server.entity

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import zzigmug.server.data.EmailJoinRequestDto
import zzigmug.server.data.type.GenderType
import zzigmug.server.data.type.LoginType
import zzigmug.server.data.type.RoleType
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated

@Entity
class User(
    @Column
    var nickname: String? = null,

    @Column(unique = true)
    var email: String,

    @Column
    var pw: String = "",

    @Column
    var height: Int? = null,

    @Column
    var weight: Int? = null,

    @Column
    var goal: Int? = null,

    @Column
    var numberOfDays: Int = 0,

    @Enumerated(EnumType.STRING)
    @Column
    var gender: GenderType? = null,

    @Column
    var agree_marketing: Boolean = false,

    @Column
    var exp: Long = 0,

    @Enumerated(EnumType.STRING)
    @Column
    var role: RoleType,

    @Enumerated(EnumType.STRING)
    @Column
    var loginType: LoginType,

    ): BaseEntity(), UserDetails {

    constructor(requestDto: EmailJoinRequestDto) : this(
        email = requestDto.email,
        pw = requestDto.password,
        role = RoleType.ROLE_GUEST,
        loginType = LoginType.EMAIL,
        nickname = requestDto.nickname,
        gender = requestDto.gender,
        goal = requestDto.goal,
        height = requestDto.height,
        weight = requestDto.weight
    )

    fun setNickname(nickname: String) {
        this.nickname = nickname
    }

    override fun getAuthorities(): MutableCollection<out GrantedAuthority>? {
        return null
    }

    override fun getPassword(): String {
        return pw
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