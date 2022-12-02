package de.aoc.tasks.days

import de.aoc.tasks.TimeCapturingTask
import java.io.InputStream

class Day2(override val day: Int = 2) : TimeCapturingTask<List<Pair<String, String>>, List<Pair<String, String>>> {

    private companion object {
        const val LOOSE = 0L
        const val DRAW = 3L
        const val WIN = 6L

        const val ROCK = 1L
        const val PAPER = 2L
        const val SCISSOR = 3L
    }

    override fun preparePart1Input(input: InputStream): List<Pair<String, String>> {
        return input
            .bufferedReader()
            .lines()
            .filter(String::isNotBlank)
            .map { it.split(Regex("\\s")) }
            .map { it[0] to it[1] }
            .toList()
    }

    override fun preparePart2Input(input: InputStream): List<Pair<String, String>> = preparePart1Input(input)

    override fun executePart1(input: List<Pair<String, String>>): Long {

        fun play(enemy: String, you: String): Long {
            return when (enemy) {
                //Rock
                "A" -> when (you) {
                    //Rock
                    "X" -> DRAW + ROCK
                    //Paper
                    "Y" -> WIN + PAPER
                    //Scissors
                    "Z" -> LOOSE + SCISSOR
                    else -> error("Unknown move $you")
                }
                //Paper
                "B" -> when (you) {
                    //Rock
                    "X" -> LOOSE + ROCK
                    //Paper
                    "Y" -> DRAW + PAPER
                    //Scissors
                    "Z" -> WIN + SCISSOR
                    else -> error("Unknown move $you")
                }
                //Scissor
                "C" -> when (you) {
                    //Rock
                    "X" -> WIN + ROCK
                    //Paper
                    "Y" -> LOOSE + PAPER
                    //Scissors
                    "Z" -> DRAW + SCISSOR
                    else -> error("Unknown move $you")
                }

                else -> error("Unknown move $enemy")
            }
        }

        return input.sumOf { (enemy, you) -> play(enemy, you) }
    }

    override fun executePart2(input: List<Pair<String, String>>): Long {
        fun play(enemy: String, you: String): Long {
            return when (enemy) {
                //Rock
                "A" -> when (you) {
                    //LOOSE
                    "X" -> LOOSE + SCISSOR
                    //DRAW
                    "Y" -> DRAW + ROCK
                    //WIN
                    "Z" -> WIN + PAPER
                    else -> error("Unknown move $you")
                }
                //Paper
                "B" -> when (you) {
                    //LOOSE
                    "X" -> LOOSE + ROCK
                    //DRAW
                    "Y" -> DRAW + PAPER
                    //WIN
                    "Z" -> WIN + SCISSOR
                    else -> error("Unknown move $you")
                }
                //Scissor
                "C" -> when (you) {
                    //LOOSE
                    "X" -> LOOSE + PAPER
                    //DRAW
                    "Y" -> DRAW + SCISSOR
                    //WIN
                    "Z" -> WIN + ROCK
                    else -> error("Unknown move $you")
                }

                else -> error("Unknown move $enemy")
            }
        }

        return input.sumOf { (enemy, you) -> play(enemy, you) }
    }


}
