package de.aoc.tasks.days

import de.aoc.tasks.TimeCapturingTask
import java.io.InputStream

class Day16(override val day: Int = 16) : TimeCapturingTask<Map<String, Day16.Valve>, Map<String, Day16.Valve>> {

    private companion object {
        private val RX = Regex("Valve ([A-Z][A-Z]) has flow rate=(\\d+); tunnels? leads? to valves? ([A-Z, ]+)")
    }

    override fun preparePart1Input(input: InputStream): Map<String, Valve> =
        input.bufferedReader()
            .lines()
            .toList()
            .mapIndexed { index, line ->
                val (name, rate, ref) = RX.matchEntire(line)!!.groupValues.drop(1)
                name to Valve(index, rate.toLong(), ref.split(", "))
            }.toMap()

    override fun preparePart2Input(input: InputStream): Map<String, Valve> = preparePart1Input(input)

    override fun executePart1(input: Map<String, Valve>): Long {
        val maxTime = 30

        return createPaths(maxTime, input).maxOf { it.value }
    }

    private fun createPaths(maxTime: Int, input: Map<String, Valve>): Map<Step, Long> {
        //time to (next steps, current flow rate)
        val memory = Array(maxTime + 1) { mutableMapOf<Step, Long>() }
        fun put(time: Int, history: Long, valveName: String, totalFlow: Long) {
            val st = Step(history, valveName)
            val cur = memory[time][st]
            if (cur == null || totalFlow > cur) memory[time][st] = totalFlow
        }
        put(0, 0, "AA", 0)

        for (currentTime in 0 until maxTime) {
            for ((step, currentFlowRate) in memory[currentTime]) {
                val (valveIDMask, valveName) = step
                val valve = input[valveName]!!
                val mask = 1L shl valve.id//marker where we already where
                if (valve.flowRate > 0 && (mask and valveIDMask) == 0L) {
                    put(
                        currentTime + 1,
                        valveIDMask or mask,
                        valveName,
                        currentFlowRate + (maxTime - currentTime - 1) * valve.flowRate
                    )
                }
                for (nextValve in valve.connectedValveNames) {
                    put(currentTime + 1, valveIDMask, nextValve, currentFlowRate)
                }
            }
        }
        return memory.last()
    }

    override fun executePart2(input: Map<String, Valve>): Long {
        val maxTime = 26

        val bitMaskToMaxFlowRate = createPaths(maxTime, input)
            .entries
            .groupingBy { it.key.historyMask }
            .fold(0L) { a, b -> maxOf(a, b.value) }

        val valvesWithPosFlowRates = input.values.filter { it.flowRate > 0 }.map { it.id }

        fun find(step: Int, leftPath: Long, rightPath: Long): Long {
            if (step == valvesWithPosFlowRates.size) {
                val b1 = bitMaskToMaxFlowRate[leftPath] ?: 0
                val b2 = bitMaskToMaxFlowRate[rightPath] ?: 0
                return b1 + b2
            }
            val r1 = find(step + 1, leftPath, rightPath)
            val r2 = find(step + 1, leftPath or (1L shl valvesWithPosFlowRates[step]), rightPath)
            val r3 = find(step + 1, leftPath, rightPath or (1L shl valvesWithPosFlowRates[step]))
            return maxOf(r1, r2, r3)
        }
        return find(0, 0, 0)
    }

    data class Valve(val id: Int, val flowRate: Long, val connectedValveNames: List<String>)
    private data class Step(val historyMask: Long, val valveName: String)

}
