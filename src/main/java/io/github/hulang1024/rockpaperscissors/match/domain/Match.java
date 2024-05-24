package io.github.hulang1024.rockpaperscissors.match.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@Getter
public class Match {
    private @NonNull String id;
    private @NonNull State state;
    private @NonNull Object firstParticipantId;
    private @NonNull Object secondParticipantId;
    private int roundNumber;
    private @Nullable Round nowRound;
    private @NonNull Set<Round> historyRounds;

    public void startRound() {
        if (state != State.Ready && state != State.RoundEnd) {
            throw new IllegalStateException("比赛不在准备状态或不在上一回合结束状态");
        }
        state = State.RoundInProgress;
        nowRound = new Round(nowRound == null ? 1 : nowRound.getNo() + 1);
    }

    public void onParticipantChoice(@NonNull Object participantId, @NonNull Option choice) {
        if (state != State.RoundInProgress) {
            throw new IllegalStateException("未有进行中的回合");
        }
        if (!Objects.equals(firstParticipantId, participantId) && !Objects.equals(secondParticipantId, participantId)) {
            throw new IllegalArgumentException("玩家不在该比赛中");
        }
        nowRound.onParticipantChoice(participantId, choice, Objects.equals(participantId, firstParticipantId));
        if (nowRound.isEnd()) {
            this.endRound();
        }
    }

    public @Nullable LocalDateTime getStartTime() {
        if (state == State.Ready) {
            return null;
        }
        return historyRounds.isEmpty()
            ? nowRound.getStartTime()
            : findRound(1).getStartTime();
    }

    public @Nullable LocalDateTime getEndTime() {
        if (state != State.End) {
            return null;
        }
        return findRound(roundNumber).getEndTime();
    }

    private Round findRound(int no) {
        return historyRounds.stream().filter(round -> round.getNo() == no).findFirst().orElse(null);
    }

    private void endRound() {
        state = State.RoundEnd;
        historyRounds.add(nowRound);
        if (historyRounds.size() == roundNumber) {
            state = State.End;
        }
    }

    public enum State {
        Ready,
        RoundInProgress,
        RoundEnd,
        End,
    }
}
