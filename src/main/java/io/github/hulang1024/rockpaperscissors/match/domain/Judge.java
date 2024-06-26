package io.github.hulang1024.rockpaperscissors.match.domain;

import org.springframework.lang.NonNull;

public class Judge {
    public Option judgeWinner(@NonNull Option a, @NonNull Option b) {
        if (a == null || b == null) {
            throw new IllegalArgumentException();
        }
        if (a == b) {
            return null;
        }
        if (a == Option.Rock && b == Option.Scissors) {
            return a;
        }
        if (a == Option.Scissors && b == Option.Paper) {
            return a;
        }
        if (a == Option.Paper && b == Option.Rock) {
            return a;
        }
        return b;
    }
}
