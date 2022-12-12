package de.aoc.tasks.days

import de.aoc.tasks.TimeCapturingTask
import java.io.InputStream

class Day19(override val day: Int = 19) : TimeCapturingTask<List<Day19.Blueprint>, List<Day19.Blueprint>> {

    private companion object {
        private val RX = Regex("""Blueprint (\d+): Each ore robot costs (\d+) ore. Each clay robot costs (\d+) ore. Each obsidian robot costs (\d+) ore and (\d+) clay. Each geode robot costs (\d+) ore and (\d+) obsidian.""")
    }

    override fun preparePart1Input(input: InputStream): List<Blueprint> =
        input.bufferedReader()
            .lines()
            .map { blueprint ->
                RX.matchEntire(blueprint)!!.destructured.toList().map(String::toInt).let {
                    Blueprint(
                        it.component1(),
                        arrayOf(
                            intArrayOf(it.component2(), 0, 0, 0),
                            intArrayOf(it.component3(), 0, 0, 0),
                            intArrayOf(it.component4(), it.component5(), 0, 0),
                            intArrayOf(it[5], 0, it[6], 0)
                        )
                    )
                }
            }.toList()


    override fun preparePart2Input(input: InputStream): List<Blueprint> =
        preparePart1Input(input)

    override fun executePart1(input: List<Blueprint>): Long =
        input.sumOf { it.maxGeodes(24) * it.id }.toLong()

    override fun executePart2(input: List<Blueprint>): Long =
        input.take(3)
            .fold(1) { acc, blueprint -> acc * blueprint.maxGeodes(32) }

    data class Blueprint(val id: Int, private val costs: Array<IntArray>) {
        fun maxGeodes(totalTime: Int): Int {
            var result = 0

            val resource = intArrayOf(0, 0, 0, 0)
            val robot = intArrayOf(1, 0, 0, 0)
            val robotMax = IntArray(4) { resId -> costs.maxOf { it[resId] } }.also { it[3] = totalTime }

            fun traverse(currTime: Int) {
                if (currTime == totalTime) {
                    result = maxOf(result, resource.last())
                    return
                }

                robot.indices.reversed()
                    .filter { robotId ->
                        costs[robotId]
                            .withIndex()
                            .all { (resId, resNeed) -> resource[resId] >= resNeed }
                                && robot[robotId] < robotMax[robotId]
                    }
                    .forEach { robotId ->
                        resource.indices.forEach { resource[it] += robot[it] - costs[robotId][it] }
                        robot[robotId] += 1

                        traverse(currTime + 1)

                        robot[robotId] -= 1
                        resource.indices.forEach { resource[it] += costs[robotId][it] - robot[it] }

                        if (robotId >= 2) {
                            return
                        }
                    }
                resource.indices.forEach { resource[it] += robot[it] }
                traverse(currTime + 1)
                resource.indices.forEach { resource[it] -= robot[it] }
            }
            traverse(0)

            return result
        }

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is Blueprint) return false

            if (id != other.id) return false
            if (!costs.contentDeepEquals(other.costs)) return false

            return true
        }

        override fun hashCode(): Int =
            31 * id + costs.contentDeepHashCode()
    }
}
