package game.entity

import game.GameController
import game.entity.graphics.Circle

class Player(
    circle: Circle,
    speed: Double,
    name: String,
    state: MutableMap<String, Any> = mutableMapOf(),
    controller: GameController
) : GameObject(circle, speed, name, state, controller) {
    companion object {
        const val DOWN = "down"
        const val UP = "up"
        const val LEFT = "left"
        const val RIGHT = "right"
    }

    var rightPressed = false
    var leftPressed = false
    var downPressed = false
    var upPressed = false

    override fun iterate() {
        var x = 0
        var y = 0

        downPressed = (state[DOWN] as Boolean?) == true
        leftPressed = (state[LEFT] as Boolean?) == true
        rightPressed = (state[RIGHT] as Boolean?) == true
        upPressed = (state[UP] as Boolean?) == true

        if (upPressed)
            y = -1

        if (downPressed)
            y = 1

        if (rightPressed)
            x = 1

        if (leftPressed)
            x = -1

//        when {
//            upPressed    -> y = -1
//            downPressed  -> y = 1
//            leftPressed  -> x = 1
//            rightPressed -> x = -1
//        }

        offset(x, y)
    }

    override fun copy(circle: Circle, speed: Double, state: MutableMap<String, Any>) =
        Player(circle, speed, name, state, controller)

    fun setDirection(direction: String, move: Boolean) {
        state[direction] = move
    }

}