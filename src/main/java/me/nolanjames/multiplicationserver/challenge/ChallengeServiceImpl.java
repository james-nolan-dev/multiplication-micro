package me.nolanjames.multiplicationserver.challenge;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.nolanjames.multiplicationserver.user.User;
import me.nolanjames.multiplicationserver.user.UserRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class ChallengeServiceImpl implements ChallengeService {

    private final UserRepository userRepository;
    private final ChallengeAttemptRepository attemptRepository;

    @Override
    public ChallengeAttempt verifyAttempt(ChallengeAttemptDTO resultAttempt) {

        User user = userRepository.findByAlias(resultAttempt.userAlias()).orElseGet(() -> {
            log.info("Creating new user with alias {}", resultAttempt.userAlias());
            return userRepository.save(new User(resultAttempt.userAlias()));
        });

        boolean isCorrect = resultAttempt.guess() ==
                resultAttempt.factorA() * resultAttempt.factorB();

        ChallengeAttempt challengeAttempt = new ChallengeAttempt(
                null,
                user,
                resultAttempt.factorA(),
                resultAttempt.factorB(),
                resultAttempt.guess(),
                isCorrect
        );
        return attemptRepository.save(challengeAttempt);
    }

    @Override
    public List<ChallengeAttempt> getLastTenAttemptsByUser(String alias) {
        return attemptRepository.findTop10ByUserAliasOrderByIdDesc(alias);
    }
}
