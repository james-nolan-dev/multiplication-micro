package me.nolanjames.multiplicationserver.challenge;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Java6BDDAssertions.then;
import static org.junit.jupiter.api.Assertions.*;

class ChallengeServiceTest {

    private ChallengeService challengeService;

    @BeforeEach
    public void setUp() {
        challengeService = new ChallengeServiceImpl();
    }

    @Test
    public void checkCorrectAttemptTest() {
        ChallengeAttemptDTO challengeAttemptDTO =
                new ChallengeAttemptDTO(50, 60, "john_doe", 3000);
        ChallengeAttempt resultAttempt = challengeService.verifyAttempt(challengeAttemptDTO);
        then(resultAttempt.isCorrect()).isTrue();
    }

    @Test
    public void checkWrongAttemptTest() {
        ChallengeAttemptDTO challengeAttemptDTO =
                new ChallengeAttemptDTO(50, 60, "john_doe", 5000);
        ChallengeAttempt resultAttempt = challengeService.verifyAttempt(challengeAttemptDTO);
        then(resultAttempt.isCorrect()).isFalse();
    }
}