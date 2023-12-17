import java.io.File

class SymbolMatch(
    val x: Int,
    val y: Int,
    val symbol: Char,
    val numbers: MutableList<Int> = mutableListOf(),
){
    companion object {
        fun isSymbol(char: Char): Boolean {
            val isDigit  = char.isDigit()
            val isDot    = char.compareTo('.') == 0
            return !isDigit && !isDot
        }
    }
}

fun main(args: Array<String>) {
    var lines = """
467..114..
...*......
..35..633.
......#...
617*......
.....+.58.
..592.....
......755.
...$.*....
.664.598..
    """.trimIndent().lines()
//    lines = File("src/main/resources/day3.txt").readLines()

    val numbers = mutableListOf<Int>()
    val symbols = mutableListOf<SymbolMatch>()

    fun getSymbolByXandY(x: Int, y: Int): SymbolMatch? = symbols.find { it.x == x && it.y == y }

    fun addSymbol(x: Int, y: Int, char: Char): SymbolMatch {
        var symbol = getSymbolByXandY(x, y)
        if (symbol == null) {
            symbol = SymbolMatch(x, y, char).also {
                symbols.add(it)
            }
        }
        return symbol
    }

    lines.forEachIndexed { lineIndex, line ->
        val numberMatch = StringBuilder()
        line.forEachIndexed { i, lineChar ->
            val isDigit  = lineChar.isDigit()
            val isLastCharNumber = i == line.length - 1 && isDigit
            if (isDigit) {
                numberMatch.append(lineChar)
            }
            if ((!isDigit || isLastCharNumber) && numberMatch.isNotEmpty()) {
                val x1 = (i - numberMatch.length).let { if (isLastCharNumber) it + 1 else it }
                val x2 = x1 + (numberMatch.length - 1)

                val adjacentSymbols = mutableListOf<SymbolMatch>()

                line.getOrNull(x1 - 1)
                    ?.takeIf { SymbolMatch.isSymbol(it) }
                    ?.let { addSymbol(x1 - 1, lineIndex, it) }
                    ?.also { adjacentSymbols.add(it) }

                line.getOrNull(x2 + 1)
                    ?.takeIf { SymbolMatch.isSymbol(it) }
                    ?.let { addSymbol(x2 + 1, lineIndex, it) }
                    ?.also { adjacentSymbols.add(it) }

                val xRange = x1 -1 .. x2 + 1
                for (i in xRange) {
                    lines.getOrNull(lineIndex - 1)
                        ?.getOrNull(i)
                        ?.takeIf { SymbolMatch.isSymbol(it) }
                        ?.let { addSymbol(i, lineIndex - 1, it) }
                        ?.also { adjacentSymbols.add(it) }

                    lines.getOrNull(lineIndex + 1)
                        ?.getOrNull(i)
                        ?.takeIf { SymbolMatch.isSymbol(it) }
                        ?.let { addSymbol(i, lineIndex + 1, it) }
                        ?.also { adjacentSymbols.add(it) }
                }

                val number = numberMatch.toString().toInt()
                numberMatch.clear()

                adjacentSymbols
                    .takeIf { it.isNotEmpty() }
                    ?.also { numbers.add(number) }
                    ?.forEach { it.numbers.add(number) }
            }
        }
    }

    println(numbers.sum())

    val sum = symbols
        .filter { it.numbers.size == 2 && it.symbol == '*' }.sumOf { it.numbers[0] * it.numbers[1] }

    println(sum)
}