package de.aoc.model

class Grid<T>(list: Collection<Collection<T>>) : ArrayList<List<T>>(list.map(Collection<T>::toList)), List<List<T>> {

    private val innerSize = first().size

    fun coordinates(): List<Coordinate> =
        IntRange(0, size - 1)
            .map { it to IntRange(0, innerSize - 1) }
            .flatMap { (row, columns) -> columns.map { Coordinate(row, it) } }

    fun isAtEdge(coordinate: Coordinate): Boolean =
        coordinate.x == 0 || coordinate.x == size - 1
                || coordinate.y == 0 || coordinate.y == innerSize - 1

    operator fun get(coordinate: Coordinate): T =
        this[coordinate.x][coordinate.y]

}
