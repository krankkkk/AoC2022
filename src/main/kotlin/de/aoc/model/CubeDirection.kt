package de.aoc.model


enum class CubeDirection(
    private val mover: (CubeCoordinate, Int) -> CubeCoordinate
) {
    UP({ coordinate, steps -> coordinate.copy(height = coordinate.height + steps) }),
    DOWN({ coordinate, steps -> coordinate.copy(height = coordinate.height - steps) }),
    RIGHT({ coordinate, steps -> coordinate.copy(column = coordinate.column + steps) }),
    LEFT({ coordinate, steps -> coordinate.copy(column = coordinate.column - steps) }),
    IN({ coordinate, steps -> coordinate.copy(row = coordinate.row + steps) }),
    OUT({ coordinate, steps -> coordinate.copy(row = coordinate.row - steps) }),


    RIGHT_UP({ coordinate, steps -> UP.mover(coordinate, steps).let { RIGHT.mover(it, steps) } }),
    RIGHT_DOWN({ coordinate, steps -> DOWN.mover(coordinate, steps).let { RIGHT.mover(it, steps) } }),
    LEFT_UP({ coordinate, steps -> UP.mover(coordinate, steps).let { LEFT.mover(it, steps) } }),
    LEFT_DOWN({ coordinate, steps -> DOWN.mover(coordinate, steps).let { LEFT.mover(it, steps) } });

    fun move(coordinate: CubeCoordinate, distance: Int = 1): CubeCoordinate {
        check(distance >= 0) { "Distance was <0: $distance" }
        return if (distance == 0) coordinate else this.mover(coordinate, distance)
    }

    companion object {
        fun cardinal() = listOf(UP, DOWN, RIGHT, LEFT, IN, OUT)
    }
}
