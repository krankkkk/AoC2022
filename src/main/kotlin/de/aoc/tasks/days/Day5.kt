package de.aoc.tasks.days

import de.aoc.tasks.TimeCapturingTask
import java.io.InputStream
import java.util.*

class Day5(override val day: Int = 5) : TimeCapturingTask<Pair<String, String>, Pair<String, String>> {

    private companion object {
        private val moveRx = Regex("^move (\\d+) from (\\d+) to (\\d+)$")
    }

    override fun preparePart1Input(input: InputStream): Pair<String, String> {
        val (a, b) = input.bufferedReader().readText().split("\n\n")
        return a to b
    }

    override fun preparePart2Input(input: InputStream): Pair<String, String> =
        preparePart1Input(input)

    override fun executePart1(input: Pair<String, String>): String {
        val (craneInit, moves) = input

        val cranes = createCranes(craneInit)

        moves.split("\n")
            .filter(String::isNotBlank)
            .forEach { move ->
                val (amount, from, to) = parseMove(move)

                val fromCrane = cranes[from.toInt()]!!
                val toCrane = cranes[to.toInt()]!!

                repeat(amount.toInt()) { toCrane.addLast(fromCrane.pollLast()) }
            }

        return popLast(cranes)
    }

    private fun createCranes(craneInit: String): Map<Int, Deque<Char>> {
        val cranes = mutableMapOf<Int, Deque<Char>>()

        val lines = craneInit.split("\n")
        lines.last()
            .split(Regex("\\s*"))
            .filter(String::isNotBlank)
            .forEach { cranes.computeIfAbsent(it.trim().toInt()) { ArrayDeque() } }

        lines.subList(0, lines.size - 1)
            .forEach { line ->
                line.chunked(4)
                    .forEachIndexed { index, cargo ->
                        if (cargo.isNotBlank())
                            cranes[index + 1]!!.offerFirst(cargo[1])
                    }
            }
        return cranes
    }

    override fun executePart2(input: Pair<String, String>): String {
        val (craneInit, moves) = input

        val cranes = createCranes(craneInit)

        moves.split("\n")
            .filter(String::isNotBlank)
            .forEach { move ->
                val (amount, from, to) = parseMove(move)

                val fromCrane = cranes[from.toInt()]!!
                val toCrane = cranes[to.toInt()]!!
                val temp = ArrayDeque<Char>()

                repeat(amount.toInt()) {
                    temp.addFirst(fromCrane.pollLast())
                }

                while (temp.isNotEmpty())
                    toCrane.addLast(temp.pollFirst())
            }

        return popLast(cranes)
    }

    private fun parseMove(move: String) =
        moveRx.find(move)!!
            .groupValues
            .drop(1)

    private fun popLast(cranes: Map<Int, Deque<Char>>) = IntRange(1, cranes.size)
        .map { cranes[it]!!.last() ?: "" }
        .joinToString(separator = "")
        .trim()

}
