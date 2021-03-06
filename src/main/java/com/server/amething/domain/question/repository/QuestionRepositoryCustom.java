package com.server.amething.domain.question.repository;

import com.server.amething.domain.question.Question;
import com.server.amething.domain.question.dto.QuestionAndAnswerDto;
import com.server.amething.domain.question.dto.QuestionDto;
import com.server.amething.domain.user.User;

import java.util.List;
import java.util.Optional;

public interface QuestionRepositoryCustom{
    List<QuestionDto> findUnReplyDescriptionByUser(User user);
    List<QuestionAndAnswerDto> findPinDescriptionByUser(User user);
}
