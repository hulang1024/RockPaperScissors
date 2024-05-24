package io.github.hulang1024.rockpaperscissors.match.domain;

public class Judge {
    public Option judgeWinner(Option a, Option b) {
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
