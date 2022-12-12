package de.aoc.model


enum class LDirection(
    private val mover: (LCoordinate, Long) -> LCoordinate
) {
    UP({ coordinate, steps -> coordinate.copy(row = coordinate.row + steps) }),
    DOWN({ coordinate, steps -> coordinate.copy(row = coordinate.row - steps) }),
    RIGHT({ coordinate, steps -> coordinate.copy(column = coordinate.column + steps) }),
    LEFT({ coordinate, steps -> coordinate.copy(column = coordinate.column - steps) }),

    RIGHT_UP({ coordinate, steps -> UP.mover(coordinate, steps).let { RIGHT.mover(it, steps) } }),
    RIGHT_DOWN({ coordinate, steps -> DOWN.mover(coordinate, steps).let { RIGHT.mover(it, steps) } }),
    LEFT_UP({ coordinate, steps -> UP.mover(coordinate, steps).let { LEFT.mover(it, steps) } }),
    LEFT_DOWN({ coordinate, steps -> DOWN.mover(coordinate, steps).let { LEFT.mover(it, steps) } });

    fun move(coordinate: LCoordinate, distance: Long = 1): LCoordinate {
        check(distance >= 0) { "Distance was <0: $distance" }
        return if (distance == 0L) coordinate else this.mover(coordinate, distance)
    }
}

fun LDirection?.combine(direction: LDirection): LDirection {
    return when (this) {
        null -> direction
        LDirection.UP -> when (direction) {
            LDirection.LEFT -> LDirection.LEFT_UP
            LDirection.RIGHT -> LDirection.RIGHT_UP

            else -> error("Cannot combine $this with $direction.")
        }

        LDirection.DOWN -> when (direction) {
            LDirection.LEFT -> LDirection.LEFT_DOWN
            LDirection.RIGHT -> LDirection.RIGHT_DOWN

            else -> error("Cannot combine $this with $direction.")
        }

        LDirection.RIGHT -> when (direction) {
            LDirection.UP -> LDirection.RIGHT_UP
            LDirection.DOWN -> LDirection.RIGHT_DOWN

            else -> error("Cannot combine $this with $direction.")
        }

        LDirection.LEFT -> when (direction) {
            LDirection.UP -> LDirection.LEFT_UP
            LDirection.DOWN -> LDirection.LEFT_DOWN

            else -> error("Cannot combine $this with $direction.")
        }

        else -> error("Cannot combine with base $this, it is already a combined Direction.")
    }

}
