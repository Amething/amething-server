package com.server.amething.domain.answer.controller;

import com.server.amething.domain.answer.service.AnswerService;
import com.server.amething.domain.question.Question;
import com.server.amething.domain.question.enum_type.QuestionType;
import com.server.amething.domain.question.repository.QuestionRepository;
import com.server.amething.domain.user.User;
import com.server.amething.domain.user.enum_type.Role;
import com.server.amething.domain.user.repository.UserRepository;
import com.server.amething.global.jwt.JwtTokenProvider;
import com.server.amething.global.jwt.config.CustomUserDetails;
import com.server.amething.global.util.UserUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Optional;

import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureRestDocs
@AutoConfigureMockMvc
@Transactional
class AnswerControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    AnswerService answerService;

    @Autowired
    QuestionRepository questionRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    JwtTokenProvider tokenProvider;
    @Autowired
    CustomUserDetails customUserDetails;
    @Autowired
    UserUtil userUtil;

    @BeforeEach
    void createQuestion() {
        //given
        User user = User.builder()
                .bio("")
                .nickname("김태민")
                .oauthId(2249049917L)
                .profilePicture("")
                .roles(Collections.singletonList(Role.ROLE_MEMBER))
                .refreshToken("Bearer refreshToken")
                .build();
        userRepository.save(user);

        // when login session 발급
        UserDetails userDetails = customUserDetails.loadUserByUsername(user.getUsername());

        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                userDetails,
                "",
                userDetails.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(token);

        questionRepository.save(new Question(1L, user, "첫번째 질문", QuestionType.UNREPLY));
    }

    @Test
    void registerAnswer() throws Exception {
        Optional<Question> findQuestion = questionRepository.findById(1L);
        Long questionId = findQuestion.get().getId();

        String accessToken = "Bearer " +  tokenProvider.createAccessToken(userUtil.getCurrentUser().getUsername(), userUtil.getCurrentUser().getRoles());

        this.mockMvc.perform(post("/v1/{questionId}/answer", questionId)
                .content("{\"description\": \"description\"}")
                .header("Authorization", accessToken)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andDo(document("register-answer",
                        pathParameters(
                                parameterWithName("questionId").description("Question Id(PK)")
                        ),
                        requestFields(
                                fieldWithPath("description").description("답변내용").optional()
                        ),
                        requestHeaders(
                                headerWithName("Authorization").description("Bearer 토큰 타입의 AccessToken")
                        ))
                );
        }
}