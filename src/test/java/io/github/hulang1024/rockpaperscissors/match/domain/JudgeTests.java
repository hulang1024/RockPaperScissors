package io.github.hulang1024.rockpaperscissors.match.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class JudgeTests {
    @Test
    void testJudge() {
        Judge judge = new Judge();
        assertSame(Option.Rock, judge.judgeWinner(Option.Rock, Option.Scissors));
        assertSame(Option.Rock, judge.judgeWinner(Option.Scissors, Option.Rock));
        assertSame(Option.Scissors, judge.judgeWinner(Option.Scissors, Option.Paper));
        assertSame(Option.Scissors, judge.judgeWinner(Option.Paper, Option.Scissors));
        assertSame(Option.Paper, judge.judgeWinner(Option.Paper, Option.Rock));
        assertSame(Option.Paper, judge.judgeWinner(Option.Rock, Option.Paper));

        for (Option option : Option.values()) {
            assertSame(null, judge.judgeWinner(option, option));
        }
    }
}
