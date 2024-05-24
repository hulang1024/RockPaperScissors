package io.github.hulang1024.rockpaperscissors.match.domain;

import java.util.HashSet;
import java.util.UUID;

public class MatchService {
    public Match startMatch(Object participant1Id, Object participant2Id, int roundNumber) {
        return new Match(
            UUID.randomUUID().toString(),
            Match.MatchState.MatchReady,
            participant1Id,
            participant2Id,
            roundNumber,
            null,
            new HashSet<>());
    }
}
