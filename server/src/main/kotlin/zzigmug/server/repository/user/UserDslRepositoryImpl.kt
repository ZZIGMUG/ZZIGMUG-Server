package zzigmug.server.repository.user

import com.querydsl.core.types.dsl.BooleanExpression
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.support.PageableExecutionUtils
import org.springframework.stereotype.Repository
import zzigmug.server.entity.QUser
import zzigmug.server.entity.User
import zzigmug.server.repository.QuerydslCustomRepositorySupport

@Repository
class UserDslRepositoryImpl: QuerydslCustomRepositorySupport(User::class.java), UserDslRepository {
    private val user: QUser = QUser("user")

    override fun findAllUserBySearch(pageable: Pageable, keyword: String?): Page<User> {
        val users = selectFrom(user)
            .where(
                nicknameLike(keyword),
                emailLike(keyword),
            )
            .offset(pageable.offset)
            .limit(pageable.pageSize.toLong())
            .orderBy(user.id.desc())
            .fetch()

        val countQuery = selectFrom(user)
            .where(
                nicknameLike(keyword),
                emailLike(keyword)
            )

        return PageableExecutionUtils.getPage(users, pageable, countQuery::fetchCount)
    }

    private fun nicknameLike(keyword: String?): BooleanExpression? {
        return if (!keyword.isNullOrEmpty()) {
            user.nickname.like("%$keyword%")
        }
        else null
    }

    private fun emailLike(keyword: String?): BooleanExpression? {
        return if (!keyword.isNullOrEmpty()) {
            user.email.like("%$keyword%")
        }
        else null
    }
}