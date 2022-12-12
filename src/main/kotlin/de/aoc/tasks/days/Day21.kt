package de.aoc.tasks.days

import de.aoc.tasks.TimeCapturingTask
import java.io.InputStream


class Day21(override val day: Int = 21) : TimeCapturingTask<Day21.Monkeys, Day21.Monkeys> {

    private companion object {
        private const val HUMAN_MONKEY_NAME = "humn"
    }

    override fun preparePart1Input(input: InputStream): Monkeys =
        input.bufferedReader()
            .lineSequence()
            .associate {
                it.split(": ")
                    .let { (name, job) ->
                        name to job.split(" ")
                    }
            }
            .let(::Monkeys)


    override fun preparePart2Input(input: InputStream): Monkeys =
        preparePart1Input(input)
            .also {
                it.getMonkeyValue("root")//init
            }

    override fun executePart1(input: Monkeys): Long =
        input.getMonkeyValue("root")

    override fun executePart2(input: Monkeys): Long =
        input.getHumanMonkeyValue()


    class Monkeys(private val data: Map<String, List<String>>) {
        private val cache = mutableMapOf<String, Long>()
        private val monkeyWithHumanPart = mutableSetOf(HUMAN_MONKEY_NAME)

        fun getMonkeyValue(name: String): Long =
            cache[name] ?: data.getValue(name)
                .let { expression ->
                    if (expression.size == 1) {
                        expression.first().toLong()//literal number
                    } else {
                        val (left, right) = expression.component1() to expression.component3()
                        when (expression.component2()) {
                            "+" -> getMonkeyValue(left) + getMonkeyValue(right)
                            "-" -> getMonkeyValue(left) - getMonkeyValue(right)
                            "*" -> getMonkeyValue(left) * getMonkeyValue(right)
                            "/" -> getMonkeyValue(left) / getMonkeyValue(right)
                            else -> error("Unknown Symbol ${expression.component2()}")
                        }.also {
                            if (left in monkeyWithHumanPart || right in monkeyWithHumanPart)
                                monkeyWithHumanPart.add(name)
                        }
                    }.also { cache[name] = it }
                }

        fun getHumanMonkeyValue(): Long {
            fun getValue(name: String, expectValue: Long): Long =
                if (name == HUMAN_MONKEY_NAME) {
                    expectValue
                } else {
                    val expression = data.getValue(name)
                    val (left, right) = expression.component1() to expression.component3()
                    when (left) {
                        in monkeyWithHumanPart ->
                            when (expression.component2()) {
                                "+" -> getValue(left, expectValue - getMonkeyValue(right))
                                "-" -> getValue(left, expectValue + getMonkeyValue(right))
                                "*" -> getValue(left, expectValue / getMonkeyValue(right))
                                "/" -> getValue(left, expectValue * getMonkeyValue(right))
                                else -> error("Unknown Symbol ${expression.component2()}")
                            }

                        else -> when (expression.component2()) {
                            "+" -> getValue(right, expectValue - getMonkeyValue(left))
                            "-" -> getValue(right, getMonkeyValue(left) - expectValue)
                            "*" -> getValue(right, expectValue / getMonkeyValue(left))
                            "/" -> getValue(right, getMonkeyValue(left) / expectValue)
                            else -> error("Unknown Symbol ${expression.component2()}")
                        }
                    }
                }

            val expression = data.getValue("root")
            val (left, right) = expression.component1() to expression.component3()
            return if (left in monkeyWithHumanPart)
                getValue(left, getMonkeyValue(right))
            else
                getValue(right, getMonkeyValue(left))
        }
    }
}
