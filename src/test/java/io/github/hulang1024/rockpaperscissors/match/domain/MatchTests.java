package io.github.hulang1024.rockpaperscissors.match.domain;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class MatchTests {
    @Test
    void testCreate() {
        MatchFactory matchFactory = new MatchFactory();
        Match match = matchFactory.startMatch(1, 2, 3);

        assertNotNull(match.getId());
        assertSame(Match.State.Ready, match.getState());
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
        MatchFactory matchFactory = new MatchFactory();
        Match match = matchFactory.startMatch(1, 2, 3);
        assertSame(Match.State.Ready, match.getState());
        assertNull(match.getStartTime());
        assertNull(match.getEndTime());
        match.startRound();
        LocalDateTime firstRoundStartTime = match.getNowRound().getStartTime();
        assertSame(firstRoundStartTime, match.getStartTime());
        assertNull(match.getEndTime());
        assertSame(Match.State.RoundInProgress, match.getState());
        match.onParticipantChoice(1, Option.Scissors);
        assertSame(firstRoundStartTime, match.getStartTime());
        assertNull(match.getEndTime());
        match.onParticipantChoice(2, Option.Rock);
        assertSame(Match.State.RoundEnd, match.getState());
        assertSame(firstRoundStartTime, match.getStartTime());
        assertNull(match.getEndTime());
        assertSame(1, match.getNowRound().getNo());
        assertSame(1, match.getHistoryRounds().size());
        match.startRound();
        assertSame(Match.State.RoundInProgress, match.getState());
        match.onParticipantChoice(1, Option.Paper);
        match.onParticipantChoice(2, Option.Rock);
        assertSame(Match.State.RoundEnd, match.getState());
        assertSame(firstRoundStartTime, match.getStartTime());
        assertNull(match.getEndTime());
        assertSame(2, match.getNowRound().getNo());
        assertSame(2, match.getHistoryRounds().size());
        match.startRound();
        assertSame(Match.State.RoundInProgress, match.getState());
        match.onParticipantChoice(1, Option.Rock);
        match.onParticipantChoice(2, Option.Rock);
        LocalDateTime lastRoundEndTime = match.getNowRound().getEndTime();
        assertSame(Match.State.End, match.getState());
        assertSame(firstRoundStartTime, match.getStartTime());
        assertSame(lastRoundEndTime, match.getEndTime());
        assertSame(3, match.getNowRound().getNo());
        assertSame(3, match.getHistoryRounds().size());

        assertThrowsExactly(IllegalStateException.class, () -> match.startRound());
    }

    @Test
    void testCallRoundOnParticipantChoice() {
        MatchFactory matchFactory = new MatchFactory();
        Match match = matchFactory.startMatch(1, 2, 3);

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
