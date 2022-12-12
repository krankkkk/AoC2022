package de.aoc.tasks.days

import de.aoc.model.CubeCoordinate
import de.aoc.model.CubeDirection
import de.aoc.tasks.TimeCapturingTask
import java.io.InputStream

class Day18(override val day: Int = 18) : TimeCapturingTask<Set<CubeCoordinate>, Set<CubeCoordinate>> {

    override fun preparePart1Input(input: InputStream): Set<CubeCoordinate> =
        input.bufferedReader()
            .lines()
            .map { it.split(',').map(String::toInt) }
            .map { CubeCoordinate(it.component1(), it.component2(), it.component3()) }
            .toList()
            .toSet()

    override fun preparePart2Input(input: InputStream): Set<CubeCoordinate> =
        preparePart1Input(input)

    override fun executePart1(input: Set<CubeCoordinate>): Long {
        val context = Context(input)

        return input.flatMap { coord -> CubeDirection.cardinal().map { it.move(coord) } }
            .count { isOutside1(it, context) }
            .toLong()
    }

    override fun executePart2(input: Set<CubeCoordinate>): Long {
        val context = Context(input)

        return input.flatMap { coord -> CubeDirection.cardinal().map { it.move(coord) } }
            .count { isOutside2(it, context) }
            .toLong()
    }

    private fun isOutside1(coord: CubeCoordinate, context: Context): Boolean {
        if (coord in context.outside) return true
        if (coord in context.inside) return false
        if (coord in context.input) {
            context.inside.add(coord)
            return false
        }
        context.outside.add(coord)
        return true
    }

    private fun isOutside2(coord: CubeCoordinate, context: Context): Boolean {
        if (coord in context.outside) return true
        if (coord in context.inside) return false

        val seen = mutableSetOf<CubeCoordinate>()
        val queue = ArrayDeque(listOf(coord))
        while (queue.isNotEmpty()) {
            val next = queue.removeFirst()
            if (next in context.input || next in seen) continue

            seen.add(next)
            if (seen.size > 5000) {//if we came that far we are way outside the cube
                context.outside.addAll(seen)
                return true
            }

            CubeDirection.cardinal()
                .map { it.move(next) }
                .let(queue::addAll)
        }


        context.inside.addAll(seen)
        return false
    }

    private data class Context(
        val input: Set<CubeCoordinate>,
        val outside: MutableSet<CubeCoordinate> = mutableSetOf(),
        val inside: MutableSet<CubeCoordinate> = mutableSetOf(),
    )

}
