package de.aoc.tasks.days

import de.aoc.tasks.TimeCapturingTask
import java.io.InputStream

class Day22(override val day: Int = 22) : TimeCapturingTask<Day22.InputWrapper, Day22.InputWrapper> {

    private companion object {
        private val RX = Regex("(\\d+)([RL]?)")
    }

    override fun preparePart1Input(input: InputStream): InputWrapper =
        input.bufferedReader()
            .lines()
            .toList()
            .map { it }
            .let {
                val board = it.dropLast(2)
                val width = board.maxOf(String::length) //filling up to max width

                InputWrapper(board.map { it.padEnd(width, ' ') }, RX.findAll(it.last()).map {
                    val (steps, dir) = it.destructured
                    steps.toInt() to dir.first()
                }.toList())
            }

    override fun preparePart2Input(input: InputStream): InputWrapper = preparePart1Input(input)

    override fun executePart1(input: InputWrapper): Long {
        val board = input.map
        val bordHeight = board.size
        val bordWidth = board.first().length
        var row = 0
        var column = board.first().indexOf('.')
        var dirRow = 0
        var dirColumn = 1


        input.sequence
            .forEach { (steps, dir) ->
                repeat(steps) {
                    var newRow = row
                    var newCol = column

                    while (true) {
                        newRow = newRow.plus(dirRow).mod(bordHeight)
                        newCol = newCol.plus(dirColumn).mod(bordWidth)
                        if (board[newRow][newCol] != ' ')
                            break
                    }

                    if (board[newRow][newCol] == '#')
                        return@repeat

                    row = newRow
                    column = newCol
                }

                if (dir == 'R') {
                    val temp = dirRow
                    dirRow = dirColumn
                    dirColumn = -temp
                } else if (dir == 'L') {
                    val temp = dirRow
                    dirRow = -dirColumn
                    dirColumn = temp
                }
            }


        val k =
            if (dirRow == 0)
                if (dirColumn == 1)
                    0
                else
                    2
            else
                if (dirRow == 1)
                    1
                else
                    3

        return 1000L * (row + 1) + 4 * (column + 1) + k
    }

    override fun executePart2(input: InputWrapper): Long {
        //dear lord forgive me for what I am about to do
        //this works for the actual input but not the example :(
        //something is wrong about the face-size calculation
        val board = input.map
        val faceLength = if (input.map.size == 12)
            4
        else
            50

        var row = 0
        var column = board.first().indexOf('.')
        var dirRow = 0
        var dirColumn = 1


        input.sequence
            .forEach { (steps, dir) ->
                repeat(steps) {
                    val cubeDirRow = dirRow
                    val cubeDirCol = dirColumn

                    var newRow = row + dirRow
                    var newCol = column + dirColumn

                    if (newRow < 0 && newCol in faceLength until faceLength * 2 && dirRow == -1) {
                        dirRow = 0
                        dirColumn = 1
                        newRow = newCol + 2 * faceLength
                        newCol = 0
                    } else if (newCol < 0 && newRow in faceLength * 3 until faceLength * 4 && dirColumn == -1) {
                        dirRow = 1
                        dirColumn = 0
                        newCol = newRow - 2 * faceLength
                        newRow = 0
                    } else if (newRow < 0 && newCol in faceLength * 2 until faceLength * 3 && dirRow == -1) {
                        newRow = 4 * faceLength - 1
                        newCol -= 2 * faceLength
                    } else if (newRow >= 4 * faceLength && newCol in 0 until faceLength && dirRow == 1) {
                        newRow = 0
                        newCol += 2 * faceLength
                    } else if (newCol >= 3 * faceLength && newRow in 0 until faceLength && dirColumn == 1) {
                        dirColumn = -1
                        newRow = 3 * faceLength - 1 - newRow
                        newCol = 2 * faceLength - 1
                    } else if (newCol == 2 * faceLength && newRow in faceLength * 2 until faceLength * 3 && dirColumn == 1) {
                        dirColumn = -1
                        newRow = 3 * faceLength - 1 - newRow
                        newCol = 3 * faceLength - 1
                    } else if (newRow == faceLength && newCol in faceLength * 2 until faceLength * 3 && dirRow == 1) {
                        dirRow = 0
                        dirColumn = -1
                        newRow = newCol - faceLength
                        newCol = 2 * faceLength - 1
                    } else if (newCol == 2 * faceLength && newRow in faceLength until faceLength * 2 && dirColumn == 1) {
                        dirRow = -1
                        dirColumn = 0
                        newCol = newRow + faceLength
                        newRow = faceLength - 1
                    } else if (newRow == 3 * faceLength && newCol in faceLength until faceLength * 2 && dirRow == 1) {
                        dirRow = 0
                        dirColumn = -1
                        newRow = newCol + faceLength * 2
                        newCol = faceLength - 1
                    } else if (newCol == faceLength && newRow in faceLength * 3 until faceLength * 4 && dirColumn == 1) {
                        dirRow = -1
                        dirColumn = 0
                        newCol = newRow - 2 * faceLength
                        newRow = (3 * faceLength) - 1
                    } else if (newRow == 2 * faceLength - 1 && newCol in 0 until faceLength && dirRow == -1) {
                        dirRow = 0
                        dirColumn = 1
                        newRow = newCol + faceLength
                        newCol = faceLength
                    } else if (newCol == faceLength - 1 && newRow in faceLength until faceLength * 2 && dirColumn == -1) {
                        dirRow = 1
                        dirColumn = 0
                        newCol = newRow - faceLength
                        newRow = 2 * faceLength
                    } else if (newCol == faceLength - 1 && newRow in 0 until faceLength && dirColumn == -1) {
                        dirColumn = 1
                        newRow = (3 * faceLength - 1) - newRow
                        newCol = 0
                    } else if (newCol < 0 && newRow in faceLength * 2 until faceLength * 3 && dirColumn == -1) {
                        dirColumn = 1
                        newRow = (3 * faceLength - 1) - newRow
                        newCol = faceLength
                    }


                    if (board[newRow][newCol] == '#') {
                        dirRow = cubeDirRow
                        dirColumn = cubeDirCol
                        return@repeat
                    }

                    row = newRow
                    column = newCol
                }

                if (dir == 'R') {
                    val temp = dirRow
                    dirRow = dirColumn
                    dirColumn = -temp
                } else if (dir == 'L') {
                    val temp = dirRow
                    dirRow = -dirColumn
                    dirColumn = temp
                }
            }


        val k =
            if (dirRow == 0)
                if (dirColumn == 1)
                    0
                else
                    2
            else
                if (dirRow == 1)
                    1
                else
                    3

        return 1000L * (row + 1) + 4 * (column + 1) + k
    }

    data class InputWrapper(val map: List<String>, val sequence: List<Pair<Int, Char>>)

    private fun <T> swap(one: T, other: T): Pair<T, T> = other to one
}
