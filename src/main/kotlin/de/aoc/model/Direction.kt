package de.aoc.model


enum class Direction(private val filter: (Grid<*>, Coordinate) -> List<*>) {
    TOP({ grid, coordinate ->
        grid.filterIndexed { rowIndex, _ -> rowIndex < coordinate.x }.map { it[coordinate.y] }.reversed()
    }),
    DOWN({ grid, coordinate ->
        grid.filterIndexed { rowIndex, _ -> rowIndex > coordinate.x }.map { it[coordinate.y] }
    }),
    RIGHT({ grid, coordinate ->
        grid[coordinate.x].filterIndexed { colIndex, _ -> colIndex > coordinate.y }
    }),
    LEFT({ grid, coordinate ->
        grid[coordinate.x].filterIndexed { colIndex, _ -> colIndex < coordinate.y }.reversed()
    });

    fun <T> filter(grid: Grid<T>, coordinate: Coordinate): List<T> {
        return filter.invoke(grid, coordinate) as List<T>
    }
}
