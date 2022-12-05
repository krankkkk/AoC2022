package de.aoc.tasks.days

import de.aoc.tasks.TimeCapturingTask
import java.io.InputStream
import java.util.*

class Day5(override val day: Int = 5) : TimeCapturingTask<Pair<String, String>, Pair<String, String>> {

    override fun preparePart1Input(input: InputStream): Pair<String, String> {
        val (a, b) = input.bufferedReader().readText().split("\n\n")
        return a to b
    }

    override fun preparePart2Input(input: InputStream): Pair<String, String> =
        preparePart1Input(input)

    override fun executePart1(input: Pair<String, String>): String {
        val (craneInit, moves) = input

        val cranes = createCranes(craneInit)

        val moveRx = Regex("^move (\\d+) from (\\d+) to (\\d+)$")
        moves.split("\n")
            .filter(String::isNotBlank)
            .forEach { move ->
                val (amount, from, to) = moveRx.find(move)!!
                    .groupValues
                    .drop(1)

                val fromCrane = cranes[from.toInt()]!!
                val toCrane = cranes[to.toInt()]!!

                for (i in 0 until amount.toInt())
                    toCrane.addLast(fromCrane.pollLast())
            }

        return IntRange(1, cranes.size)
            .map { cranes[it]!!.last() ?: ' ' }
            .joinToString(separator = "")
            .trim()
    }

    private fun createCranes(craneInit: String): MutableMap<Int, Deque<Char>> {
        val cranes = mutableMapOf<Int, Deque<Char>>()

        val lines = craneInit.split("\n")
        lines.last()
            .split(Regex("\\s*"))
            .filter(String::isNotBlank)
            .forEach { cranes.computeIfAbsent(it.trim().toInt()) { ArrayDeque() } }

        lines.subList(0, lines.size - 1)
            .reversed()
            .forEach { line ->
                for (i in 0..line.length step 4) {
                    val substring = line.substring(i, i + 3)
                    if (substring.isBlank()) {
                        continue
                    }

                    val crane = i / 4 + 1
                    cranes[crane]!!.offer(substring[1])
                }

            }
        return cranes
    }

    override fun executePart2(input: Pair<String, String>): String {
        val (craneInit, moves) = input

        val cranes = createCranes(craneInit)

        val moveRx = Regex("^move (\\d+) from (\\d+) to (\\d+)$")
        moves.split("\n")
            .filter(String::isNotBlank)
            .forEach { move ->
                val (amount, from, to) = moveRx.find(move)!!
                    .groupValues
                    .drop(1)

                val fromCrane = cranes[from.toInt()]!!
                val toCrane = cranes[to.toInt()]!!
                val temp = ArrayDeque<Char>()

                for (i in 0 until amount.toInt())
                    temp.addFirst(fromCrane.pollLast())

                while (temp.isNotEmpty())
                    toCrane.addLast(temp.pollFirst())
            }

        return IntRange(1, cranes.size)
            .map { cranes[it]!!.last() ?: "" }
            .joinToString(separator = "")
            .trim()
    }

}
