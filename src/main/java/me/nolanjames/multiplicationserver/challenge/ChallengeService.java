package me.nolanjames.multiplicationserver.challenge;

import java.util.List;

public interface ChallengeService {

    ChallengeAttempt verifyAttempt(ChallengeAttemptDTO resultAttempt);
    List<ChallengeAttempt> getLastTenAttemptsByUser(String alias);
}
