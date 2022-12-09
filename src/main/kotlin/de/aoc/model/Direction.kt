package de.aoc.model


enum class Direction(
    private val filter: (Grid<*>, Coordinate) -> List<*>,
    private val mover: (Coordinate, Int) -> Coordinate
) {
    UP({ grid, coordinate ->
        grid.filterIndexed { rowIndex, _ -> rowIndex < coordinate.row }.map { it[coordinate.column] }.reversed()
    }, { coordinate, steps -> coordinate.copy(row = coordinate.row + steps) }),
    DOWN({ grid, coordinate ->
        grid.filterIndexed { rowIndex, _ -> rowIndex > coordinate.row }.map { it[coordinate.column] }
    }, { coordinate, steps -> coordinate.copy(row = coordinate.row - steps) }),
    RIGHT({ grid, coordinate ->
        grid[coordinate.row].filterIndexed { colIndex, _ -> colIndex > coordinate.column }
    }, { coordinate, steps -> coordinate.copy(column = coordinate.column + steps) }),
    LEFT({ grid, coordinate ->
        grid[coordinate.row].filterIndexed { colIndex, _ -> colIndex < coordinate.column }.reversed()
    }, { coordinate, steps -> coordinate.copy(column = coordinate.column - steps) }),

    RIGHT_UP({ grid, coordinate -> TODO() }, { coordinate, steps -> UP.mover(coordinate, steps).let { RIGHT.mover(it, steps) } }),
    RIGHT_DOWN({ grid, coordinate -> TODO() }, { coordinate, steps -> DOWN.mover(coordinate, steps).let { RIGHT.mover(it, steps) } }),
    LEFT_UP({ grid, coordinate -> TODO() }, { coordinate, steps -> UP.mover(coordinate, steps).let { LEFT.mover(it, steps) } }),
    LEFT_DOWN({ grid, coordinate -> TODO() }, { coordinate, steps -> DOWN.mover(coordinate, steps).let { LEFT.mover(it, steps) } });

    fun <T> filter(grid: Grid<T>, coordinate: Coordinate): List<T> {
        return filter.invoke(grid, coordinate) as List<T>
    }

    fun move(coordinate: Coordinate, distance: Int = 1): Coordinate {
        check(distance >= 0) { "Distance was <0: $distance" }
        return if (distance == 0) coordinate else this.mover(coordinate, distance)
    }

    companion object {
        fun cardinal() = listOf(UP, DOWN, RIGHT, LEFT)
    }
}

fun Direction?.combine(direction: Direction): Direction {
    return when (this) {
        null -> direction
        Direction.UP -> when (direction) {
            Direction.LEFT -> Direction.LEFT_UP
            Direction.RIGHT -> Direction.RIGHT_UP

            else -> error("Cannot combine $this with $direction.")
        }

        Direction.DOWN -> when (direction) {
            Direction.LEFT -> Direction.LEFT_DOWN
            Direction.RIGHT -> Direction.RIGHT_DOWN

            else -> error("Cannot combine $this with $direction.")
        }

        Direction.RIGHT -> when (direction) {
            Direction.UP -> Direction.RIGHT_UP
            Direction.DOWN -> Direction.RIGHT_DOWN

            else -> error("Cannot combine $this with $direction.")
        }

        Direction.LEFT -> when (direction) {
            Direction.UP -> Direction.LEFT_UP
            Direction.DOWN -> Direction.LEFT_DOWN

            else -> error("Cannot combine $this with $direction.")
        }

        else -> error("Cannot combine with base $this, it is already a combined Direction.")
    }

}
