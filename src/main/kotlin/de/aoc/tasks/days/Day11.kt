package de.aoc.tasks.days

import de.aoc.tasks.TimeCapturingTask
import de.aoc.utils.Operator
import java.io.InputStream


class Day11(override val day: Int = 11) : TimeCapturingTask<List<Day11.Monkey>, List<Day11.Monkey>> {

    companion object {
        private const val ROUNDS_PART1 = 20
        private const val ROUNDS_PART2 = 10_000

        private val ID_RX = Regex("^Monkey (\\d):$")

        //regex has no way to capture all results of a group, only last match is saved
        private val ITEMS_RX = Regex("^\\s+Starting items: (\\d+(?:, )?)+$")

        private val OP_RX = Regex("^\\s+Operation: new = old ([\\+\\*\\-\\/\\%]) (\\d+|old)$")

        private val TEST_RX = Regex("^\\s+Test: divisible by (\\d+)$")

        private val NEXT_RX = Regex("^\\s+If (?:true|false): throw to monkey (\\d+)$")
    }

    override fun preparePart1Input(input: InputStream): List<Monkey> =
        input.bufferedReader().readText()
            .split("\n\n")
            .map { monkey ->
                val lines = monkey.split("\n").iterator()

                val id = ID_RX.find(lines.next())!!.destructured.component1().toInt()
                val items = lines.next().substringAfter(':')
                    .split(",")
                    .map(String::trim)
                    .map(String::toLong)
                    .toMutableList()

                val opRes = OP_RX.find(lines.next())!!.destructured
                val op = Operator.fromOperator(opRes.component1().first())
                val isSecondPartOld = opRes.component2() == "old"
                val secondNumber = if (isSecondPartOld) -1L else opRes.component2().toLong()

                val operation: (Long) -> Long = { old ->
                    if (isSecondPartOld)
                        op.operation(old, old)
                    else
                        op.operation(old, secondNumber)
                }

                val divisible = TEST_RX.find(lines.next())!!.destructured.component1().toLong()

                val trueMonkeyID = NEXT_RX.find(lines.next())!!.destructured.component1().toInt()
                val falseMonkeyID = NEXT_RX.find(lines.next())!!.destructured.component1().toInt()

                Monkey(id, items, divisible, operation, trueMonkeyID, falseMonkeyID)
            }.sortedBy(Monkey::id)

    override fun preparePart2Input(input: InputStream): List<Monkey> =
        preparePart1Input(input)

    override fun executePart1(input: List<Monkey>): Long =
        throwItems(input, ROUNDS_PART1) { it / 3 }
            .sortedDescending()
            .take(2)
            .reduce { acc, l -> acc * l }

    override fun executePart2(input: List<Monkey>): Long {
        val mod = input
            .map(Monkey::testValue)
            .reduce(Operator.MULTIPLY.operation)

        return throwItems(input, ROUNDS_PART2) { it % mod }
            .sortedDescending()
            .take(2)
            .reduce { acc, l -> acc * l }
    }

    private fun throwItems(
        input: List<Monkey>,
        rounds: Int,
        postOp: (Long) -> Long
    ): List<Long> {
        val counters = buildList { repeat(input.size) { add(0L) } }.toMutableList()

        repeat(rounds) {
            repeat(input.size) { monkeyID ->
                val monkey = input[monkeyID]
                monkey.items
                    .map(monkey.operation)
                    .map(postOp)
                    .forEach { item ->
                        val id = if (item % monkey.testValue == 0L)
                            monkey.trueMonkeyID
                        else
                            monkey.falseMonkeyID

                        input[id].items.add(item)
                    }

                counters[monkeyID] = counters[monkeyID] + monkey.items.size
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
