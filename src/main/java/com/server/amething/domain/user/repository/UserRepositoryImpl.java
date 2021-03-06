package com.server.amething.domain.user.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.server.amething.domain.user.dto.ProfileDto;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.server.amething.domain.user.QUser.user;

@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    @Override
    @Transactional(readOnly = true)
    public Optional<ProfileDto> findProfileByOauthId(Long oauthId) {
        return Optional.ofNullable(queryFactory.from(user)
                .select(Projections.constructor(ProfileDto.class,
                        user.nickname,
                        user.profilePicture,
                        user.bio
                        ))
                .where(user.oauthId.eq(oauthId))
                .fetchOne());
    }
}
