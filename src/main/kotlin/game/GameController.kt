package game

import game.entity.GameObject
import game.entity.Bot
import game.entity.Player
import game.entity.graphics.Circle
import game.entity.graphics.Point
import kotlin.math.abs
import kotlin.random.Random

class GameController(
    val screenWidth: Double,
    val screenHeight: Double,
    playersCount: Int = 10,
    private val maxSpeed: Double = 10.0
) {
    companion object {
        const val PLAYER = "player"
    }

    val players = mutableMapOf<String, GameObject>()

    init {
        for (i in 0 until playersCount) {
            val player = generatePlayer()
            players[player.name] = player
        }
    }

    fun getPlayer() = players[PLAYER] as Player

    fun iterate(player: GameObject) {
        players[PLAYER] = player

        checkKilled()

        if (ALIVE)
            players
                .values
                .sortedBy { it.circle.radius }
                .forEach {
                    it.iterate()
                }

    }

    fun move(gameObject: GameObject, point: Point) {
        val (x, y) = gameObject.circle.center
        val speed = gameObject.speed

        val xDir = if (point.x > x) 1 else -1
        val yDir = if (point.y > y) 1 else -1

        val xDiff = abs(point.x - x)
        val yDiff = abs(point.y - y)

        val xStep = if (xDiff < speed) xDiff else speed
        val yStep = if (yDiff < speed) yDiff else speed

        val (newX, newY) = fixedCoordinates(gameObject, xStep * xDir, yStep * yDir)
        val circle = gameObject.circle

        players[gameObject.name] = gameObject.copy(
            circle.copy(center = Point(newX, newY))
        )
    }

    fun addPlayer(player: Player) {
        players[PLAYER] = player
    }

    fun offset(gameObject: GameObject, x: Int, y: Int) {
        val (newX, newY) = fixedCoordinates(
            gameObject,
            getOffset(x, gameObject),
            getOffset(y, gameObject)
        )

        players[gameObject.name] = gameObject.copy(
            circle.copy(center = Point(newX, newY), radius = gameObject.circle.radius)
        )
    }

    private fun getOffset(value: Int, gameObject: GameObject) = when {
        value < 0 -> -gameObject.speed
        value > 0 -> gameObject.speed
        else      -> 0.0
    }

    private fun fixedCoordinates(gameObject: GameObject, dx: Double, dy: Double): Pair<Double, Double> {
        val newX = when {
            gameObject.circle.center.x + gameObject.circle.radius + dx > screenWidth -> screenWidth - gameObject.circle.radius
            gameObject.circle.center.x - gameObject.circle.radius + dx < 0.0         -> gameObject.circle.radius
            else                                                                     -> gameObject.circle.center.x + dx
        }

        val newY = when {
            gameObject.circle.center.y + gameObject.circle.radius + dy > screenHeight -> screenHeight - gameObject.circle.radius
            gameObject.circle.center.y - gameObject.circle.radius + dy < 0.0          -> gameObject.circle.radius
            else                                                                      -> gameObject.circle.center.y + dy
        }

        return newX to newY

    }

    private fun checkKilled() {
        val list = players.values.sortedByDescending { it.circle.radius }
        val listToDelete = mutableSetOf<String>()

        for (i in 0 until players.size) {
            val player = list[i]

            if (listToDelete.contains(player.name))
                break

            for (j in 0 until players.size) {
                val victim = list[j]

                if (player == victim)
                    continue

                if (isKilled(player, victim)) {
                    players[player.name] = player.copy(circle = player.circle.copy(radius = player.circle.radius * 1.3))
                    listToDelete.add(victim.name)
                    println("killed:${victim.name} by ${player.name}")
                    if (victim.name == PLAYER) {
                        ALIVE = false
                        return
                    }
                }
            }
        }

        listToDelete.forEach {
            players.remove(it)
        }
    }

    private fun isKilled(killer: GameObject, victim: GameObject) = killer.circle.isInside(victim.circle.center)

    private fun generatePlayer(): Bot {
        val circle = Circle(
            Point(Random.nextDouble(screenWidth), Random.nextDouble(screenHeight)),
            radius = Random.nextDouble(10.0, 20.0),
            color = generateColor()
        )

        return Bot(
            circle,
            speed = calculateSpeed(circle.radius),
            name = circle.hashCode().toString(),
            controller = this
        )
    }

    private fun generateColor(): String {
        val red = Random.nextInt(256)
        val green = Random.nextInt(256)
        val blue = Random.nextInt(256)

        return "#${red.toString(16)}${green.toString(16)}${blue.toString(16)}".toUpperCase()
    }

    private fun calculateSpeed(radius: Double) = maxSpeed / radius
}
