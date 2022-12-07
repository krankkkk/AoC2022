package de.aoc.tasks.days

import de.aoc.tasks.TimeCapturingTask
import java.io.InputStream
import java.util.*

class Day7(override val day: Int = 7) : TimeCapturingTask<Day7.Dir, Day7.Dir> {
    private companion object {
        private const val MAX_SIZE_PART1 = 100_000
        private const val TOTAL_SPACE = 70_000_000L
        private const val MIN_FREE_SPACE = 30_000_000L
    }

    override fun preparePart1Input(input: InputStream): Dir =
        input.bufferedReader()
            .readText()
            .split("$")
            .map {
                it.split("\n")
                    .map(String::trim)
                    .filter(String::isNotBlank)
            }.filter(List<String>::isNotEmpty)
            .let(::toDirs)


    override fun preparePart2Input(input: InputStream): Dir =
        preparePart1Input(input)

    override fun executePart1(input: Dir): Long =
        discoverDirs(input)
            .filter { it.size <= MAX_SIZE_PART1 }
            .sumOf { it.size }

    private fun discoverDirs(rootDir: Dir): Set<Dir> {
        val discovered = mutableSetOf<Dir>()
        val queue: Queue<Dir> = ArrayDeque<Dir>().also { it.add(rootDir) }

        while (queue.isNotEmpty()) {
            val dir = queue.poll()
            discovered += dir
            queue.addAll(dir.children.filterIsInstance<Dir>())
        }
        return discovered
    }

    private fun toDirs(input: List<List<String>>): Dir {
        val rootDir = Dir(null, "", mutableSetOf())
        var last = rootDir

        for (command in input) {
            val first = command.first()
            if (first.startsWith("cd")) {
                when (val dir = first.substringAfter("cd ")) {
                    ".." -> {
                        last = last.parent as Dir
                    }

                    "/" -> {
                        last = rootDir
                    }

                    else -> {
                        last = last
                            .children
                            .filterIsInstance<Dir>()
                            .firstOrNull { it.dir.endsWith("/$dir") }
                            ?: Dir(last, "/$dir", mutableSetOf())
                                .also { last.children.add(it) }
                    }
                }

            } else {//ls
                command.drop(1)
                    .map { line ->
                        if (line.startsWith("dir")) {
                            last.children.add(
                                Dir(
                                    last,
                                    "/" + line.substringAfter("dir "),
                                    mutableSetOf()
                                )
                            )
                        } else {
                            last.children.add(
                                File(
                                    last,
                                    line.substringAfter(" "),
                                    line.substringBefore(" ").toLong()
                                )
                            )
                        }
                    }

            }
        }
        return rootDir
    }

    override fun executePart2(input: Dir): Long {
        val discovered = discoverDirs(input)

        val maxUsed = TOTAL_SPACE - MIN_FREE_SPACE
        val currentlyUsed = input.size

        return discovered.filter { currentlyUsed - it.size <= maxUsed }
            .minOf { it.size }
    }


    sealed interface Path {
        val parent: Dir?

        val dir: String

        val size: Long
    }

    class Dir(override val parent: Dir?, private val localDir: String, val children: MutableSet<Path>) : Path {
        override val size: Long
            get() = children.sumOf { it.size }

        override val dir: String
            get() = (parent?.dir ?: "") + localDir

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is Dir) return false

            if (dir != other.dir) return false
            if (children != other.children) return false
            if (size != other.size) return false

            return true
        }

        override fun hashCode(): Int {
            return Objects.hash(dir, children, size)
        }

        override fun toString(): String {
            return "Dir(parent=${parent?.dir}, dir='$dir', children=${children.size}, size=$size)"
        }

    }

    class File(override val parent: Dir, private val localDir: String, override val size: Long) : Path {
        override val dir: String
            get() = parent.dir + "/" + localDir

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is File) return false

            if (dir != other.dir) return false
            if (size != other.size) return false

            return true
        }

        override fun hashCode(): Int {
            return Objects.hash(dir, size)
        }

        override fun toString(): String {
            return "File(parent=${parent.dir}, dir='$dir', size=$size)"
        }
    }

}
