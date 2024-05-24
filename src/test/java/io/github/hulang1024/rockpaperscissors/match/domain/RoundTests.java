package io.github.hulang1024.rockpaperscissors.match.domain;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class RoundTests {
    @Test
    void testNewRound() {
        Round round = new Round(1);
        assertThrowsExactly(IllegalArgumentException.class, () -> new Round(0));
        assertSame(1, round.getNo());
        assertNotNull(round.getStartTime());
        assertNull(round.getEndTime());
        assertFalse(round.isDraw());
        assertFalse(round.isEnd());
        assertNull(round.getWinnerId());
        assertNull(round.getFirstChoice());
        assertNull(round.getSecondChoice());
    }

    @Test
    void testParticipantOrder() {
        Round round;
        round = new Round(2);
        round.onParticipantChoice("zh", Option.Rock, true);
        round.onParticipantChoice("li", Option.Scissors, false);
        assertSame("zh", round.getFirstChoice().getParticipantId());
        assertSame("li", round.getSecondChoice().getParticipantId());

        round = new Round(2);
        round.onParticipantChoice("zh", Option.Rock, false);
        round.onParticipantChoice("li", Option.Scissors, true);
        assertSame("li", round.getFirstChoice().getParticipantId());
        assertSame("zh", round.getSecondChoice().getParticipantId());
    }

    @Test
    void testOnParticipantChoice() {
        Round round = new Round(2);

        round.onParticipantChoice(1, Option.Rock, true);
        assertSame(2, round.getNo());
        assertNotNull(round.getStartTime());
        assertNull(round.getEndTime());
        assertFalse(round.isEnd());
        assertNull(round.getWinnerId());
        assertSame(1, round.getFirstChoice().getParticipantId());
        assertSame(Option.Rock, round.getFirstChoice().getChoice());
        assertNull(round.getSecondChoice());

        assertThrowsExactly(IllegalStateException.class, () -> round.onParticipantChoice(1, Option.Scissors, false));
        assertThrowsExactly(IllegalStateException.class, () -> round.onParticipantChoice(2, Option.Scissors, true));

        round.onParticipantChoice(2, Option.Scissors, false);
        assertSame(2, round.getNo());
        assertNotNull(round.getStartTime());
        assertNotNull(round.getEndTime());
        assertTrue(round.isEnd());
        assertSame(1, round.getWinnerId());
        assertSame(1, round.getFirstChoice().getParticipantId());
        assertSame(Option.Rock, round.getFirstChoice().getChoice());
        assertSame(2, round.getSecondChoice().getParticipantId());
        assertSame(Option.Scissors, round.getSecondChoice().getChoice());

        assertThrowsExactly(IllegalStateException.class, () -> round.onParticipantChoice(2, Option.Scissors, true));
    }
}
