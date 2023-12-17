import java.io.File

data class CalibrationLine(private var text: String) {
    companion object {
        private val numbers = mapOf(
            "one" to "1",
            "two" to "2",
            "three" to "3",
            "four" to "4",
            "five" to "5",
            "six" to "6",
            "seven" to "7",
            "eight" to "8",
            "nine" to "9",
        )
        private val matcher =
            "(?=(\\d|${numbers.keys
                .joinToString("|")}))"
                .toRegex()
    }

    fun getValue(): Int {
        var firstValue = text.first().digitToIntOrNull()?.toString()
        var lastValue = text.last().digitToIntOrNull()?.toString()

        // if the first and last character are numbers we already have the answer
        if (firstValue == null || lastValue == null) {
            val result = matcher.findAll(text)
            firstValue = result.first().groupValues[1].let {
                numbers.getOrDefault(it, it)
            }
            lastValue = result.last().groupValues[1].let {
                numbers.getOrDefault(it, it)
            }
        }

        return (firstValue + lastValue).toInt()
    }
}

fun main(args: Array<String>) {
    val input = File("src/main/resources/day1.txt").readLines()

    val total = input
        .map { CalibrationLine(it) }
        .sumOf { it.getValue() }

    println("Total: $total")
}