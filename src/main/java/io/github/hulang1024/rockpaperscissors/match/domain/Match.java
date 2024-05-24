package io.github.hulang1024.rockpaperscissors.match.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;

import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@Getter
public class Match {
    private String id;
    private MatchState state;
    private Object firstParticipantId;
    private Object secondParticipantId;
    private int roundNumber;
    private Round nowRound;
    private Set<Round> historyRounds;

    public void startRound() {
        if (state != MatchState.MatchReady && state != MatchState.RoundEnd) {
            throw new IllegalStateException("比赛不在准备状态或上一回合结束状态");
        }
        state = MatchState.RoundInProgress;
        nowRound = new Round(nowRound == null ? 1 : nowRound.getNo() + 1);
    }

    public void onParticipantChoice(@NonNull Object participantId, @NonNull Option choice) {
        if (state != MatchState.RoundInProgress) {
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

    public LocalDateTime getStartTime() {
        if (state == MatchState.MatchReady) {
            return null;
        }
        return historyRounds.isEmpty()
            ? nowRound.getStartTime()
            : historyRounds.iterator().next().getStartTime();
    }

    public LocalDateTime getEndTime() {
        if (state != MatchState.RoundEnd) {
            return null;
        }
        Round lastRound = null;
        for (Iterator<Round> iterator = historyRounds.iterator(); iterator.hasNext();) {
            lastRound = iterator.next();
        }
        return lastRound.getEndTime();
    }

    private void endRound() {
        state = MatchState.RoundEnd;
        historyRounds.add(nowRound);
        if (historyRounds.size() == roundNumber) {
            state = MatchState.MatchEnd;
        }
    }

    public enum MatchState {
        MatchReady,
        RoundInProgress,
        RoundEnd,
        MatchEnd,
    }
}
