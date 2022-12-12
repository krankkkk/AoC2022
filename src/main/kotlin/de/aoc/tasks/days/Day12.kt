package de.aoc.tasks.days

import de.aoc.model.Coordinate
import de.aoc.model.Direction
import de.aoc.model.Grid
import de.aoc.tasks.TimeCapturingTask
import java.io.InputStream

class Day12(override val day: Int = 12) : TimeCapturingTask<Day12.InputWrapper, Day12.InputWrapper> {

    override fun preparePart1Input(input: InputStream): InputWrapper =
        input.bufferedReader()
            .lines()
            .map { it.toCharArray().asIterable() }
            .toList()
            .let(::Grid)
            .let { grid ->
                val start = grid.coordinates().first { grid[it] == 'S' }
                val end = grid.coordinates().first { grid[it] == 'E' }
                grid[start] = 'a'
                grid[end] = 'z'
                InputWrapper(grid, start, end)
            }

    override fun preparePart2Input(input: InputStream): InputWrapper =
        preparePart1Input(input)

    override fun executePart1(input: InputWrapper): Long {
        val grid = input.grid

        val queue = ArrayDeque<Step>()
        queue.add(Step(input.start, 0))
        val visited = mutableSetOf(input.start)

        while (queue.isNotEmpty()) {
            val step = queue.removeFirst()

            Direction.cardinal()
                .map { it.move(step.current) }
                .filter(grid::isValid)//In Bounds
                .filterNot(visited::contains)//don't go back
                .filter { (grid[it] - grid[step.current]) <= 1 }//max 1 height diff
                .onEach {
                    if (it == input.end)
                        return step.steps + 1L
                    else
                        visited.add(it)
                }
                .map { Step(current = it, steps = step.steps + 1) }
                .forEach(queue::addLast)
        }

        error("Did not find the way to the end.")
    }

    override fun executePart2(input: InputWrapper): Long {

        val grid = input.grid
        val queue = ArrayDeque<Step>()
        queue.add(Step(input.end, 0))//Start at the end and work backwards
        val visited = mutableSetOf(input.end)

        while (queue.isNotEmpty()) {
            val step = queue.removeFirst()
            Direction.cardinal()
                .map { it.move(step.current) }
                .filter(grid::isValid)//In Bounds
                .filterNot(visited::contains)//don't go back
                .filterNot { (grid[it] - grid[step.current]) < -1 }//max 1 height diff
                .onEach {
                    if (grid[it] == 'a')
                        return step.steps + 1L
                    else
                        visited.add(it)
                }
                .map { Step(current = it, steps = step.steps + 1) }
                .forEach(queue::addLast)

        }

        error("Did not find the way to the end.")
    }

    data class InputWrapper(val grid: Grid<Char>, val start: Coordinate, val end: Coordinate)

    private data class Step(val current: Coordinate, val steps: Long)
}
