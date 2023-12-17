import java.io.File

data class Game(
    val id: Int,
    val draws: List<Draw> = emptyList()
) {

    fun isGameValid(bag: Bag): Boolean =
        this.draws.none {
            it.blue > bag.blue
                    ||
                    it.red > bag.red
                    ||
                    it.green > bag.green
        }

    fun findFewestDrawPower(): Int {
        var red = 0; var green = 0; var blue = 0
        draws.forEach {
            red = if (it.red > red) it.red else red
            green = if (it.green > green) it.green else green
            blue = if (it.blue > blue) it.blue else blue
        }
        return red * green * blue
    }
}

data class Draw(
    val blue: Int = 0,
    val red: Int = 0,
    val green: Int = 0,
)

data class Bag(
    val blue: Int,
    val red: Int,
    val green: Int,
)

fun main(args: Array<String>) {
    val input = File("src/main/resources/day2.txt").readLines()

    val gameRegex = "^Game (\\d+):|(\\d+) (red|green|blue)([,;])?".toRegex()

    val bag = Bag(blue = 14, green = 13, red = 12)

    val games = input
        .map {
            gameRegex.findAll(it)
        }
        .map {
            it.toList()
        }
        .map {
            val gameId = it[0].groupValues[1].toInt()
            val draws = mutableListOf<Draw>()
            var draw = Draw()
            it.subList(1, it.size).forEach {
                val separator = it.groupValues[4]
                val qty = it.groupValues[2].toInt()
                val colour = it.groupValues[3]
                draw = Draw(
                    red = if (colour == "red") qty else draw.red,
                    green = if (colour == "green") qty else draw.green,
                    blue = if (colour == "blue") qty else draw.blue
                )
                if (separator.isEmpty() || separator == ";") {
                    draws.add(draw)
                    draw = Draw()
                }
            }
            Game(
                id = gameId,
                draws = draws
            )
        }

    val total1 = games.filter {
        it.isGameValid(bag)
    }.sumOf { it.id }

    val total2 = games.sumOf {
        it.findFewestDrawPower()
    }

    println(total1)
    println(total2)
}