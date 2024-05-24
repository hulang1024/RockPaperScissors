package io.github.hulang1024.rockpaperscissors.match.domain;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MatchTests {
    @Test
    void testCreate() {
        MatchService matchService = new MatchService();
        Match match = matchService.startMatch(1, 2, 3);

        assertNotNull(match.getId());
        assertSame(Match.MatchState.MatchReady, match.getState());
        assertSame(3, match.getRoundNumber());
        assertNull(match.getStartTime());
        assertNull(match.getEndTime());
        assertSame(match.getFirstParticipantId(), 1);
        assertSame(match.getSecondParticipantId(), 2);
        assertNull(match.getNowRound());
        assertSame(0, match.getHistoryRounds().size());
    }

    @Test
    void testRoundCreateAndMatchStateAndMatchTime() {
        MatchService matchService = new MatchService();
        Match match = matchService.startMatch(1, 2, 3);
        assertSame(Match.MatchState.MatchReady, match.getState());
        assertNull(match.getStartTime());
        assertNull(match.getEndTime());
        match.startRound();
        assertSame(match.getNowRound().getStartTime(), match.getStartTime());
        assertNull(match.getEndTime());
        assertSame(Match.MatchState.RoundInProgress, match.getState());
        match.onParticipantChoice(1, Option.Scissors);
        assertSame(match.getNowRound().getStartTime(), match.getStartTime());
        assertNull(match.getEndTime());
        match.onParticipantChoice(2, Option.Rock);
        assertSame(Match.MatchState.RoundEnd, match.getState());
        assertSame(match.getNowRound().getStartTime(), match.getStartTime());
        assertNotNull(match.getEndTime());
        assertSame(1, match.getNowRound().getNo());
        assertSame(1, match.getHistoryRounds().size());
        match.startRound();
        assertSame(Match.MatchState.RoundInProgress, match.getState());
        match.onParticipantChoice(1, Option.Paper);
        match.onParticipantChoice(2, Option.Rock);
        assertSame(Match.MatchState.RoundEnd, match.getState());
        assertSame(2, match.getNowRound().getNo());
        assertSame(2, match.getHistoryRounds().size());
        match.startRound();
        assertSame(Match.MatchState.RoundInProgress, match.getState());
        match.onParticipantChoice(1, Option.Rock);
        match.onParticipantChoice(2, Option.Rock);
        assertSame(Match.MatchState.MatchEnd, match.getState());
        assertSame(3, match.getNowRound().getNo());
        assertSame(3, match.getHistoryRounds().size());

        assertThrowsExactly(IllegalStateException.class, () -> match.startRound());
    }

    @Test
    void testCallRoundOnParticipantChoice() {
        MatchService matchService = new MatchService();
        Match match = matchService.startMatch(1, 2, 3);

        match.startRound();
        assertNull(match.getNowRound().getFirstChoice());
        assertNull(match.getNowRound().getSecondChoice());

        match.onParticipantChoice(1, Option.Paper);
        assertSame(1, match.getNowRound().getFirstChoice().getParticipantId());
        assertSame(Option.Paper, match.getNowRound().getFirstChoice().getChoice());

        match.onParticipantChoice(2, Option.Rock);
        assertSame(2, match.getNowRound().getSecondChoice().getParticipantId());
        assertSame(Option.Rock, match.getNowRound().getSecondChoice().getChoice());

        assertThrowsExactly(IllegalStateException.class, () -> match.onParticipantChoice(2, Option.Scissors));
    }

}
