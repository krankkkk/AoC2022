package de.aoc.tasks.days

import de.aoc.model.Coordinate
import de.aoc.model.Direction
import de.aoc.tasks.TimeCapturingTask
import java.io.InputStream

class Day23(override val day: Int = 23) : TimeCapturingTask<MutableSet<Coordinate>, MutableSet<Coordinate>> {
    private val directions = listOf(
        (Direction.DOWN) to listOf(Direction.LEFT_DOWN, Direction.DOWN, Direction.RIGHT_DOWN),
        (Direction.UP) to listOf(Direction.LEFT_UP, Direction.UP, Direction.RIGHT_UP),
        (Direction.LEFT) to listOf(Direction.LEFT_DOWN, Direction.LEFT, Direction.LEFT_UP),
        (Direction.RIGHT) to listOf(Direction.RIGHT_DOWN, Direction.RIGHT, Direction.RIGHT_UP)
    )

    override fun preparePart1Input(input: InputStream): MutableSet<Coordinate> {
        return input.bufferedReader()
            .lineSequence()
            .flatMapIndexed { row, line ->
                line.mapIndexedNotNull { col, c ->
                    if (c == '#')
                        Coordinate(row, col)
                    else
                        null
                }
            }.toMutableSet()
    }

    override fun preparePart2Input(input: InputStream): MutableSet<Coordinate> =
        preparePart1Input(input)

    override fun executePart1(input: MutableSet<Coordinate>): Long =
        input.run {
            repeat(10) { move(this, it) }
            (maxOf { it.row } - minOf { it.row } + 1) * (maxOf { it.column } - minOf { it.column } + 1) - size
        }.toLong()

    override fun executePart2(input: MutableSet<Coordinate>): Long =
        input.run {
            var res = 0
            while (move(this, res)) res++
            res + 1
        }.toLong()

    private fun move(elves: MutableSet<Coordinate>, round: Int): Boolean {
        val move = mutableMapOf<Coordinate, MutableList<Coordinate>>()
        elves.forEach { elf ->
            (round until round + directions.size)
                .map { directions[it % directions.size] }
                .filter { (_, directions) -> directions.all { dir -> dir.move(elf) !in elves } }
                .takeIf { it.size in 1 until directions.size }
                ?.let {
                    val (dir, _) = it.first()
                    move.getOrPut(dir.move(elf), ::mutableListOf).add(elf)
                }
        }
        move.filter { it.value.size == 1 }
            .forEach { (k, v) ->
                elves.remove(v.single())
                elves.add(k)
            }
        return move.any { it.value.size == 1 }//did anyone move?
    }
}
