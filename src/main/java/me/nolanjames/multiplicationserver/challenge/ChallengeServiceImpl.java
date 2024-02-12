package me.nolanjames.multiplicationserver.challenge;

import me.nolanjames.multiplicationserver.user.User;
import org.springframework.stereotype.Service;

@Service
public class ChallengeServiceImpl implements ChallengeService {
    @Override
    public ChallengeAttempt verifyAttempt(ChallengeAttemptDTO resultAttempt) {
        boolean isCorrect = resultAttempt.guess() ==
                resultAttempt.factorA() * resultAttempt.factorB();
        User user = new User(null, resultAttempt.userAlias());

        return new ChallengeAttempt(
                null,
                user.getId(),
                resultAttempt.factorA(),
                resultAttempt.factorB(),
                resultAttempt.guess(),
                isCorrect
        );
    }
}
