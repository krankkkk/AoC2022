package de.aoc.tasks.days

import de.aoc.tasks.TimeCapturingTask
import de.aoc.utils.Operator
import java.io.InputStream
import java.util.concurrent.atomic.AtomicInteger

class Day11(override val day: Int = 11) : TimeCapturingTask<Map<Int, Day11.Monkey>, Map<Int, Day11.Monkey>> {

    override fun preparePart1Input(input: InputStream): Map<Int, Monkey> =
        input.bufferedReader().readText()
            .split("\n\n")
            .map { monkey ->
                val lines = monkey.split("\n")

                val id = lines[0].substringAfter("Monkey ").substringBefore(':').toInt()
                val items = lines[1].substringAfter(':')
                    .split(',').map(String::trim)
                    .map(String::toLong)
                    .toMutableList()

                val secondPartOp = lines[2].substringAfter("= old ")
                val op = Operator.fromOperator(secondPartOp[0])
                val secondPart = secondPartOp.substring(2).trim()
                val isSecondPartOld = secondPart == "old"
                val secondNumber = if (isSecondPartOld) -1L else secondPart.toLong()

                val operation: (Long) -> Long = { old ->
                    if (isSecondPartOld)
                        op.operation(old, old)
                    else
                        op.operation(old, secondNumber)
                }

                val divisible = lines[3].substringAfter("by ").toLong()

                val trueMonkeyID = lines[4].substringAfter("monkey ").toInt()
                val falseMonkeyID = lines[5].substringAfter("monkey ").toInt()

                Monkey(id, items, divisible, operation, trueMonkeyID, falseMonkeyID)
            }.associateBy(Monkey::id)

    override fun preparePart2Input(input: InputStream): Map<Int, Monkey> =
        preparePart1Input(input)

    override fun executePart1(input: Map<Int, Monkey>): Long =
        throwItems(input, 20) { it / 3 }
            .values
            .map { it.get().toLong() }
            .sortedDescending()
            .take(2)
            .reduce { acc, l -> acc * l }

    override fun executePart2(input: Map<Int, Monkey>): Long {
        val mod = input.values
            .map(Monkey::testValue)
            .reduce(Operator.MULTIPLY.operation)

        return throwItems(input, 10_000) { it % mod }
            .values
            .map { it.get().toLong() }
            .sortedDescending()
            .take(2)
            .reduce { acc, l -> acc * l }
    }

    private fun throwItems(
        input: Map<Int, Monkey>,
        rounds: Int,
        postOp: (Long) -> Long
    ): Map<Int, AtomicInteger> {
        val counters = buildMap {
            input.keys.forEach { key ->
                put(key, AtomicInteger(0))
            }
        }

        repeat(rounds) {
            repeat(input.size) { monkeyID ->
                val monkey = input[monkeyID]!!
                counters[monkeyID]!!.addAndGet(monkey.items.size)
                monkey.items
                    .map(monkey.operation)
                    .map(postOp)
                    .onEach { item ->
                        val id = if (item % monkey.testValue == 0L)
                            monkey.trueMonkeyID
                        else
                            monkey.falseMonkeyID

                        input[id]!!.items.add(item)
                    }

                monkey.items.clear()
            }
        }
        return counters
    }

    data class Monkey(
        val id: Int,
        val items: MutableList<Long>,
        val testValue: Long,
        val operation: (Long) -> Long,
        val trueMonkeyID: Int,
        val falseMonkeyID: Int
    )
}
