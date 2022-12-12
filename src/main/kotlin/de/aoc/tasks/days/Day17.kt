package de.aoc.tasks.days

import de.aoc.model.LCoordinate
import de.aoc.model.LDirection
import de.aoc.tasks.TimeCapturingTask
import java.io.InputStream


private const val FIGURE_COUNT = 5

class Day17(override val day: Int = 17) : TimeCapturingTask<List<LDirection>, List<LDirection>> {

    override fun preparePart1Input(input: InputStream): List<LDirection> =
        input.bufferedReader()
            .lines().toList()
            .first()
            .toCharArray()
            .map(::fromChar)

    override fun preparePart2Input(input: InputStream): List<LDirection> =
        preparePart1Input(input)

    override fun executePart1(input: List<LDirection>): Long {
        val rocks = mutableSetOf<LCoordinate>()
        var top = 0L
        var i = 0//iterate over input
        for (t in 0 until 2022L) {
            var newRock = generateRock(t, top + 4)
            //debugPrint(rocks, newRock)
            while (true) {
                if (input[i] == LDirection.LEFT) {
                    if (newRock.minOf { it.column } > 0) {
                        newRock = moveLeft(newRock)
                        if (newRock.any(rocks::contains)) {
                            newRock = moveRight(newRock)
                        }
                    }
                } else {
                    if (newRock.maxOf { it.column } < 6) {
                        newRock = moveRight(newRock)
                        if (newRock.any(rocks::contains)) {
                            newRock = moveLeft(newRock)
                        }
                    }
                }

                i = (i + 1) % input.size
                newRock = moveDown(newRock)
                if (newRock.any(rocks::contains) || newRock.any { it.row == 0L }) {
                    newRock = moveUp(newRock)
                    rocks += newRock
                    top = maxOf(top, newRock.maxOf { it.row })
                    break
                }
            }
        }
        return top
    }

    override fun executePart2(input: List<LDirection>): Long {
        val rocks = mutableSetOf<LCoordinate>()
        var top = 0L
        var i = 0//iterate over input
        var t = 0L
        var added = 0L
        val seenPatterns = mutableMapOf<Triple<Int, Long, Set<LCoordinate>>, Pair<Long, Long>>()

        val maxRocks = 1_000_000_000_000L
        while (t in 0 until maxRocks) {
            var newRock = generateRock(t, top + 4L)
            //debugPrint(rocks, newRock)
            while (true) {
                if (input[i] == LDirection.LEFT) {
                    if (newRock.minOf { it.column } > 0) {
                        newRock = moveLeft(newRock)
                        if (newRock.any(rocks::contains)) {
                            newRock = moveRight(newRock)
                        }
                    }
                } else {
                    if (newRock.maxOf { it.column } < 6) {
                        newRock = moveRight(newRock)
                        if (newRock.any(rocks::contains)) {
                            newRock = moveLeft(newRock)
                        }
                    }
                }

                i = (i + 1) % input.size
                newRock = moveDown(newRock)
                if (newRock.any(rocks::contains) || newRock.any { it.row == 0L }) {
                    newRock = moveUp(newRock)
                    rocks += newRock
                    top = maxOf(top, newRock.maxOf { it.row })
                    val patterSignature = Triple(i, t % FIGURE_COUNT, sig(rocks))
                    if (patterSignature in seenPatterns) {
                        //Since the figures and the input repeats itself, we its reasonable to think that we have repeating patterns
                        //If a pattern exists, we can skip the recalculation and just apply the diff again
                        val (oldt, oldy) = seenPatterns[patterSignature]!!
                        val dy = top - oldy
                        val dt = t - oldt
                        val amt = (maxRocks - t) / dt
                        added += amt * dy
                        t += amt * dt
                    }

                    seenPatterns[patterSignature] = Pair(t, top)

                    break
                }
            }
            t++
        }
        return top + added
    }
    private fun fromChar(char: Char): LDirection =
        when (char) {
            '<' -> LDirection.LEFT
            '>' -> LDirection.RIGHT
            else -> error("Unmapped Char '$char'")
        }

    private fun sig(rock: Set<LCoordinate>): Set<LCoordinate> =
        rock.maxOf { it.row }
            .let { maxRow ->
                rock.mapNotNull {
                    if (maxRow - it.row <= 30)
                        it.copy(row = maxRow - it.row)
                    else null
                }
            }.toSet()

    private fun moveLeft(rock: Set<LCoordinate>): Set<LCoordinate> =
        rock.map(LDirection.LEFT::move).toSet()

    private fun moveRight(rock: Set<LCoordinate>): Set<LCoordinate> =
        rock.map(LDirection.RIGHT::move).toSet()

    private fun moveDown(rock: Set<LCoordinate>): Set<LCoordinate> =
        rock.map(LDirection.DOWN::move).toSet()

    private fun moveUp(rock: Set<LCoordinate>): Set<LCoordinate> =
        rock.map(LDirection.UP::move).toSet()

    private fun generateRock(number: Long, y: Long): Set<LCoordinate> =
        when (number % FIGURE_COUNT) {
            0L -> setOf(LCoordinate(y, 2), LCoordinate(y, 3), LCoordinate(y, 4), LCoordinate(y, 5))
            1L -> setOf(LCoordinate(y + 2, 3), LCoordinate(y + 1, 2), LCoordinate(y + 1, 3), LCoordinate(y + 1, 4), LCoordinate(y, 3))
            2L -> setOf(LCoordinate(y, 2), LCoordinate(y, 3), LCoordinate(y, 4), LCoordinate(y + 1, 4), LCoordinate(y + 2, 4))
            3L -> setOf(LCoordinate(y, 2), LCoordinate(y + 1, 2), LCoordinate(y + 2, 2), LCoordinate(y + 3, 2))
            4L -> setOf(LCoordinate(y + 1, 2), LCoordinate(y, 2), LCoordinate(y + 1, 3), LCoordinate(y, 3))
            else -> error("this should never occur: ${number % FIGURE_COUNT}")
        }
}
