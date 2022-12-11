package de.aoc.utils

enum class Operator(val operator: Set<Char>, val operation: (Long, Long) -> Long) {
    PLUS(setOf('+'), { a, b -> a + b }),
    MINUS(setOf('-'), { a, b -> a - b }),
    MULTIPLY(setOf('*'), { a, b -> a * b }),
    DIVIDE(setOf('/', ':'), { a, b -> a / b }),
    MOD(setOf('%'), { a, b -> a % b });

    companion object {
        fun fromOperator(operator: Char): Operator {
            return values()
                .first { it.operator.contains(operator) }
        }
    }
}
