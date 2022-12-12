package de.aoc.tasks.days

import de.aoc.tasks.TimeCapturingTask
import java.io.InputStream

class Day20(override val day: Int = 20) : TimeCapturingTask<MutableList<Long>, MutableList<Long>> {

    override fun preparePart1Input(input: InputStream): MutableList<Long> =
        input.bufferedReader()
            .lineSequence()
            .map(String::toLong)
            .toMutableList()

    override fun preparePart2Input(input: InputStream): MutableList<Long> =
        preparePart1Input(input).map { it * 811589153L }.toMutableList()

    override fun executePart1(input: MutableList<Long>): Long {
        val start = shuffle(input, 1)
        return listOf(1000, 2000, 3000)
            .sumOf { input[(start + it).mod(input.size)] }
    }

    override fun executePart2(input: MutableList<Long>): Long {
        val start = shuffle(input, 10)
        return listOf(1000, 2000, 3000)
            .sumOf { input[(start + it).mod(input.size)] }
    }

    private fun shuffle(numbers: MutableList<Long>, times: Int): Int {
        val indices = numbers.indices.toMutableList()
        repeat(times) {
            numbers.indices
                .forEach { i ->
                    val currentIndex = indices.indexOf(i)
                    val newIndex = (numbers[currentIndex] + currentIndex).mod(numbers.size - 1)
                    numbers.add(newIndex, numbers.removeAt(currentIndex))
                    indices.add(newIndex, indices.removeAt(currentIndex))
                }
        }
        return numbers.indexOf(0)
    }

}
