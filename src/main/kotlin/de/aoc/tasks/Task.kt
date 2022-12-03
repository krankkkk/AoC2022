package de.aoc.tasks

interface Task<T> {

    val day: Int

    fun solvePart1(): T

    fun solvePart2(): T

}
