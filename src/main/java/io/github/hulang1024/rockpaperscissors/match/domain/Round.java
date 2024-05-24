package io.github.hulang1024.rockpaperscissors.match.domain;

import lombok.Getter;
import lombok.Value;
import org.springframework.lang.NonNull;

import java.time.LocalDateTime;
import java.util.Objects;


@Getter
public class Round {
    private int no;
    private ParticipantChoice firstChoice;
    private ParticipantChoice secondChoice;
    private Object winnerId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    Round(int no) {
        if (no < 1) {
            throw new IllegalArgumentException("回合号参数错误");
        }
        this.no = no;
        startTime = LocalDateTime.now();
    }

    void onParticipantChoice(@NonNull Object participantId, @NonNull Option choice, boolean isFirst) {
        if (isEnd()) {
            throw new IllegalStateException("回合已结束");
        }

        if (isFirst) {
            if (firstChoice != null ||
                (secondChoice != null && Objects.equals(secondChoice.participantId, participantId)))
                throw new IllegalStateException();
            firstChoice = new ParticipantChoice(participantId, choice);
        } else {
            if (secondChoice != null ||
                (firstChoice != null && Objects.equals(firstChoice.participantId, participantId)))
                throw new IllegalStateException();
            secondChoice = new ParticipantChoice(participantId, choice);
        }
        if (firstChoice != null && secondChoice != null) {
            judgeAndEnd();
        }
    }

    private void judgeAndEnd() {
        Judge judge = new Judge();
        Option winChoice = judge.judgeWinner(firstChoice.choice, secondChoice.choice);
        if (winChoice == null) {
            winnerId = null;
        } else if (winChoice == firstChoice.choice) {
            winnerId = firstChoice.participantId;
        } else if (winChoice == secondChoice.choice) {
            winnerId = secondChoice.participantId;
        }
        endTime = LocalDateTime.now();
    }

    public boolean isDraw() {
        return isEnd() && winnerId == null;
    }

    public boolean isEnd() {
        return endTime != null;
    }

    @Value
    public static class ParticipantChoice {
        private Object participantId;
        private Option choice;
    }
}
