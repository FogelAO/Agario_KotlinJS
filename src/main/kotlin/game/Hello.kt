package game

import game.GameController.Companion.PLAYER
import game.entity.Player
import game.entity.graphics.Circle
import game.entity.graphics.Point
import org.w3c.dom.CanvasRenderingContext2D
import org.w3c.dom.HTMLCanvasElement
import org.w3c.dom.events.KeyboardEvent
import kotlin.browser.document
import kotlin.browser.window
import kotlin.math.PI

/**
 * Example main function. Started at script body.
 * At first time you have to run `mvn package`.
 * Open example index.html in browser once you compile it.
 */

val WIDTH = 1450.0
val HEIGHT = 740.0

val canvas = initalizeCanvas()

val circle = Circle(
    Point(300.0, 300.0),
    13.0
)

var ALIVE = true

val gameController = GameController(
    WIDTH,
    HEIGHT,
    playersCount = 30
)

var player: Player = Player(circle, 3.0, PLAYER, controller = gameController)

var INTERVAL_ID = 0

fun main(args: Array<String>) {
//    window.alert("Hello, World!")
    gameController.addPlayer(player)
    document.addEventListener("keydown", { keyDownHandler(it as KeyboardEvent) })
    document.addEventListener("keyup", { keyUpHandler(it as KeyboardEvent) })

    INTERVAL_ID = window.setInterval({
        if (ALIVE)
            run()
        else
            gameOver()
    }, 10)

}

fun run() {
    getCanvas()?.run {
        clearCanvas(this)
        drawField()
        player = (gameController.players[PLAYER] as Player)

        gameController.iterate(player)
        gameController.players.forEach {
            drawCircle(it.value.circle)
        }
    }
}

fun drawCircle(circle: Circle) {
    getCanvas()?.run {
        beginPath()
        val (center, radius, color) = circle
        arc(center.x, center.y, radius, 0.0, PI * 2)
        fillStyle = color
        fill()
        closePath()
    }
}


fun initalizeCanvas(): HTMLCanvasElement {
    val canvas = document.createElement("canvas") as HTMLCanvasElement
    val context = canvas.getContext("2d") as CanvasRenderingContext2D
    context.canvas.width = WIDTH.toInt()
    context.canvas.height = HEIGHT.toInt()
    document.body?.appendChild(canvas)
    return canvas
}

private fun gameOver() {
    window.clearInterval(INTERVAL_ID)
    clearCanvas()
    window.alert("Game over!")
}

private fun getCanvas() = (canvas.getContext("2d") as CanvasRenderingContext2D?)

private fun clearCanvas(canvas: CanvasRenderingContext2D? = getCanvas()) {
    canvas?.clearRect(0.0, 0.0, window.innerWidth.toDouble(), window.innerWidth.toDouble())
}

private fun drawField() {
    getCanvas()?.run {
        beginPath()
        rect(0.0, 0.0, WIDTH, HEIGHT)
        strokeStyle = "#FF0000"
        stroke()
        closePath()
    }
}

private fun keyDownHandler(e: KeyboardEvent) {
    when (e.key) {
        "Right", "ArrowRight" -> player.setDirection(Player.RIGHT, true)
        "Left", "ArrowLeft"   -> player.setDirection(Player.LEFT, true)
        "Up", "ArrowUp"       -> player.setDirection(Player.UP, true)
        "Down", "ArrowDown"   -> player.setDirection(Player.DOWN, true)
    }
}

private fun keyUpHandler(e: KeyboardEvent) {
    when (e.key) {
        "Right", "ArrowRight" -> player.setDirection(Player.RIGHT, false)
        "Left", "ArrowLeft"   -> player.setDirection(Player.LEFT, false)
        "Up", "ArrowUp"       -> player.setDirection(Player.UP, false)
        "Down", "ArrowDown"   -> player.setDirection(Player.DOWN, false)
    }
}