package de.aoc.tasks.days

import de.aoc.tasks.TimeCapturingTask
import org.json.JSONArray
import java.io.InputStream
import kotlin.math.min

class Day13(override val day: Int = 13) : TimeCapturingTask<List<Pair<JSONArray, JSONArray>>, List<Pair<JSONArray, JSONArray>>> {


    override fun preparePart1Input(input: InputStream): List<Pair<JSONArray, JSONArray>> =
        input.bufferedReader()
            .readText()
            .split("\n\n")
            .map {
                //No way I'm parsing that, python's eval is strong here
                val (fL, sL) = it.split('\n')
                JSONArray(fL) to JSONArray(sL)
            }

    override fun preparePart2Input(input: InputStream): List<Pair<JSONArray, JSONArray>> = preparePart1Input(input)

    override fun executePart1(input: List<Pair<JSONArray, JSONArray>>): Long =
        input.mapIndexed { index, (l, r) ->
            if (compare(l, r) <= 0)
                index + 1L
            else
                0
        }.sum()

    override fun executePart2(input: List<Pair<JSONArray, JSONArray>>): Long {
        val dividerPackets = listOf(JSONArray(arrayOf(2)), JSONArray(arrayOf(6)))
        val packets = (input.flatMap { listOf(it.first, it.second) } + dividerPackets).sortedWith(::compare)

        return dividerPackets.map { packets.indexOf(it) + 1L }.reduce { a, b -> a * b }
    }

    private fun compare(left: Any, right: Any): Int {
        return when {
            left is Int && right is Int -> left.compareTo(right)
            left is JSONArray && right is Int -> compare(left, JSONArray(arrayOf(right)))
            left is Int && right is JSONArray -> compare(JSONArray(arrayOf(left)), right)
            left is JSONArray && right is JSONArray -> {
                for (i in 0 until (min(left.length(), right.length()))) {
                    val c = compare(left[i]!!, right[i]!!)
                    if (c != 0)
                        return c
                }
                left.length().compareTo(right.length())
            }

            else -> 0
        }
    }

}
