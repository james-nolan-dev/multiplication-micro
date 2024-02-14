package me.nolanjames.multiplicationserver.challenge;

import me.nolanjames.multiplicationserver.user.User;
import me.nolanjames.multiplicationserver.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Java6BDDAssertions.then;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ChallengeServiceTest {

    @InjectMocks
    private ChallengeServiceImpl challengeService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ChallengeAttemptRepository challengeAttemptRepository;

    @Test
    public void checkCorrectAttemptTest() {
        given(challengeAttemptRepository.save(any())).will(returnsFirstArg());
        ChallengeAttemptDTO attemptDTO =
                new ChallengeAttemptDTO(50, 60, "john_doe", 3000);
        // when
        ChallengeAttempt resultAttempt =
                challengeService.verifyAttempt(attemptDTO);
        // then
        then(resultAttempt.isCorrect()).isTrue();
        verify(challengeAttemptRepository).save(resultAttempt);
    }

    @Test
    public void checkWrongAttemptTest() {
        given(challengeAttemptRepository.save(any())).will(returnsFirstArg());
        ChallengeAttemptDTO challengeAttemptDTO =
                new ChallengeAttemptDTO(50, 60, "john_doe", 5000);
        // when
        ChallengeAttempt resultAttempt = challengeService.verifyAttempt(challengeAttemptDTO);
        // then
        then(resultAttempt.isCorrect()).isFalse();
        verify(challengeAttemptRepository).save(resultAttempt);
    }

    @Test
    public void checkExistingUserTest() {
        // given
        User existingUser = new User(1L, "john_doe");
        given(challengeAttemptRepository.save(any())).will(returnsFirstArg());
        given(userRepository.findByAlias("john_doe")).willReturn(Optional.of(existingUser));
        ChallengeAttemptDTO challengeAttemptDTO =
                new ChallengeAttemptDTO(50, 60, "john_doe", 5000);
        // when
        ChallengeAttempt resultAttempt = challengeService.verifyAttempt(challengeAttemptDTO);

        // then
        then(resultAttempt.isCorrect()).isFalse();
        then(resultAttempt.getUser()).isEqualTo(existingUser);
        verify(userRepository, never()).save(any());
        verify(challengeAttemptRepository).save(resultAttempt);
    }

    @Test
    void retrieveLastAttempts() {
        // given
        User existingUser = new User("john_doe");
        ChallengeAttempt attempt1 = new ChallengeAttempt(1L, existingUser, 50, 60, 3010, false);
        ChallengeAttempt attempt2 = new ChallengeAttempt(2L, existingUser, 50, 60, 3051, false);
        List<ChallengeAttempt> lastAttempts = List.of(attempt1, attempt2);
        given(challengeAttemptRepository
                .findTop10ByUserAliasOrderByIdDesc(existingUser.getAlias())).willReturn(lastAttempts);

        // when
        List<ChallengeAttempt> attempts = challengeService
                .getLastTenAttemptsByUser(existingUser.getAlias());

        then(attempts).isEqualTo(lastAttempts);
    }
}