package com.server.amething.domain.question.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.server.amething.domain.question.dto.QuestionDto;
import com.server.amething.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static com.server.amething.domain.question.QQuestion.question;

@RequiredArgsConstructor
public class QuestionRepositoryImpl implements QuestionRepositoryCustom{

    private final JPAQueryFactory queryFactory;


    @Override
    @Transactional(readOnly = true)
    public Optional<List<QuestionDto>> findAllDescriptionByUser(User user) {
        return Optional.ofNullable(queryFactory.from(question)
                .select(Projections.constructor(QuestionDto.class,
                        question.description
                ))
                .where(question.user.eq(user))
                .fetch());
    }
}