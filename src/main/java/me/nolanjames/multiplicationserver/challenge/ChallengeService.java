package me.nolanjames.multiplicationserver.challenge;

public interface ChallengeService {

    ChallengeAttempt verifyAttempt(ChallengeAttemptDTO resultAttempt);
}
