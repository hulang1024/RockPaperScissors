package io.github.hulang1024.rockpaperscissors.room.domain;

import io.github.hulang1024.rockpaperscissors.match.domain.Match;
import io.github.hulang1024.rockpaperscissors.room.domain.Room;
import io.github.hulang1024.rockpaperscissors.room.domain.RoomMatchSettings;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class RoomTests {
    @Test
    void test() {
        Room room = new Room("1", new RoomMatchSettings(5));
        assertNotNull(room.getId());
        assertThrowsExactly(IllegalArgumentException.class, () -> room.ready(101));
        room.join(101);
        room.join(101);
        assertSame(1, room.getParticipantIdIds().size());
        assertSame(101, room.getParticipantIdIds().get(0));
        assertSame(true, room.getParticipantToReadyMap().get(101));
        room.join(102);
        assertSame(2, room.getParticipantIdIds().size());
        assertSame(101, room.getParticipantIdIds().get(0));
        assertSame(102, room.getParticipantIdIds().get(1));
        assertSame(false, room.getParticipantToReadyMap().get(102));
        assertThrowsExactly(IllegalArgumentException.class, () -> room.join(103));

        Match match = room.createMatch();
        assertSame(room.getId(), match.getId());
        assertSame(Match.State.Ready, match.getState());
        assertSame(room.getMatchSettings().getRoundNumber(), match.getRoundNumber());
        assertSame(room.getParticipantIdIds().get(0), match.getFirstParticipantId());
        assertSame(room.getParticipantIdIds().get(1), match.getSecondParticipantId());
    }
}
