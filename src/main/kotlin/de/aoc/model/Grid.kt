package de.aoc.model

class Grid<T>(list: Collection<Collection<T>>) : ArrayList<List<T>>(list.map(Collection<T>::toList)), List<List<T>> {

    private val innerSize = first().size

    fun coordinates(): List<Coordinate> =
        IntRange(0, size - 1)
            .map { it to IntRange(0, innerSize - 1) }
            .flatMap { (row, columns) -> columns.map { Coordinate(row, it) } }

    fun isAtEdge(coordinate: Coordinate): Boolean =
        coordinate.row == 0 || coordinate.row == size - 1
                || coordinate.column == 0 || coordinate.column == innerSize - 1

    operator fun get(coordinate: Coordinate): T =
        this[coordinate.row][coordinate.column]

}
