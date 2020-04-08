package game.entity

import game.GameController
import game.entity.graphics.Circle
import game.entity.graphics.Point
import kotlin.random.Random

class Bot(
    circle: Circle,
    speed: Double,
    name: String,
    state: MutableMap<String, Any> = mutableMapOf(),
    controller: GameController
) : GameObject(circle, speed, name, state, controller) {
    init {
        val point = state["point"] as Point?
        if (point == null) {
            state["point"] = Point(400.0, 400.0)
        }
    }


    override fun iterate() {
        val point = state["point"] as Point? ?: return
        if (circle.isInside(point))
            state["point"] =
                Point(Random.nextDouble(controller.screenWidth - 20), Random.nextDouble(controller.screenHeight - 20))

        move(point)
    }

    override fun copy(circle: Circle, speed: Double, state: MutableMap<String, Any>) =
        Bot(circle, speed, name, state, controller)
}