package de.aoc.utils

import de.aoc.model.TimedResult
import de.aoc.tasks.Task
import de.aoc.tasks.TaskVerifier
import de.aoc.tasks.TimeCapturingTask

object TaskResolver {
    fun getTaskForDay(day: Int): Task<TimedResult> {
        val constructor = Class.forName("de.aoc.tasks.days.Day$day")
            .declaredConstructors
            .find { it.parameterCount == 0 }
            ?: error("Day$day does not have an empty Constructor")

        return TaskVerifier(constructor.newInstance() as TimeCapturingTask<*, *>)
    }
}
