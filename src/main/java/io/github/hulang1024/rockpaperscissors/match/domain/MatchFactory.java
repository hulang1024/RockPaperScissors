package io.github.hulang1024.rockpaperscissors.match.domain;

import org.springframework.lang.NonNull;

import java.util.HashSet;
import java.util.UUID;

public class MatchFactory {
    public Match startMatch(@NonNull Object participant1Id, @NonNull Object participant2Id, int roundNumber) {
        return new Match(
            UUID.randomUUID().toString(),
            Match.State.Ready,
            participant1Id,
            participant2Id,
            roundNumber,
            null,
            new HashSet<>());
    }
}
