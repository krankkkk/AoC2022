package de.aoc.model

import java.time.Duration

data class TimedResult<T>(val result: T, val time: Duration)
