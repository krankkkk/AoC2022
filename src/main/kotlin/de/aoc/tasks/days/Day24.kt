package de.aoc.tasks.days

import de.aoc.model.Coordinate
import de.aoc.model.Direction
import de.aoc.tasks.TimeCapturingTask
import java.io.InputStream

class Day24(override val day: Int = 24) : TimeCapturingTask<Day24.InputWrapper, Day24.InputWrapper> {

    override fun preparePart1Input(input: InputStream): InputWrapper =
        input.bufferedReader()
            .lines()
            .toList()
            .let { lines ->
                lines
                    .flatMapIndexed { y, row ->
                        row.mapIndexedNotNull { x, c ->
                            if (c == '.' || c == '#')
                                return@mapIndexedNotNull null
                            else
                                Coordinate(y, x) to when (c) {
                                    '>' -> Direction.RIGHT
                                    '<' -> Direction.LEFT
                                    '^' -> Direction.DOWN
                                    'v' -> Direction.UP
                                    else -> error("What $c")
                                }
                        }
                    }.toMap()
                    .let { InputWrapper(it, lines.size, lines.first().length) }
            }


    override fun preparePart2Input(input: InputStream): InputWrapper = preparePart1Input(input)

    override fun executePart1(input: InputWrapper): Long =
        bfs(input, 0, Coordinate(0, 1), Coordinate(input.height - 1, input.width - 2)).toLong()

    override fun executePart2(input: InputWrapper): Long {
        val start = Coordinate(0, 1)
        val goal = Coordinate(input.height - 1, input.width - 2)
        val trip1 = bfs(input, 0, start, goal)
        val trip2 = bfs(input, trip1, goal, start)
        return bfs(input, trip2, start, goal).toLong()
    }

    private fun bfs(blizzards: InputWrapper, startTime: Int, startPos: Coordinate, endPos: Coordinate): Int {
        val seen = mutableSetOf(startTime to startPos)
        val q = ArrayDeque(seen)
        while (q.isNotEmpty()) {
            val (time, player) = q.removeFirst()
            Direction.cardinal()
                .map(player::move)
                .plus(player)
                .filterNot(blizzards.getPositionsAt(time)::contains)
                .onEach { if (it == endPos) return time }
                .filter { it.inBounds(1, blizzards.height - 2, 1, blizzards.width - 2) || it == startPos }
                .map { time + 1 to it }
                .filter(seen::add)
                .forEach(q::add)
        }
        error("Unsolvable")
    }


    class InputWrapper(private val blizzards: Map<Coordinate, Direction>, val height: Int, val width: Int) {
        private val cache = mutableMapOf<Int, Set<Coordinate>>()
        fun getPositionsAt(minute: Int): Set<Coordinate> =
            cache.getOrPut(minute) {
                blizzards.map { (k, v) ->
                    v.move(k, minute)
                }.map {
                    it.copy(
                        row = (it.row - 1).mod(height - 2) + 1,
                        column = (it.column - 1).mod(width - 2) + 1
                    )
                }.toSet()
            }
    }
}
