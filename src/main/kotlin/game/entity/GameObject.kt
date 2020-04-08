package game.entity

import game.GameController
import game.entity.graphics.Circle
import game.entity.graphics.Point

abstract class GameObject(
    val circle: Circle,
    val speed: Double,
    val name: String,
    protected val state: MutableMap<String, Any>,
    protected val controller: GameController
) {

    abstract fun iterate()

    abstract fun copy(
        circle: Circle = this.circle,
        speed: Double = this.speed,
        state: MutableMap<String, Any> = this.state
    ): GameObject

    fun move(point: Point) = controller.move(this, point)

    fun offset(x: Int = 0, y: Int = 0) = controller.offset(this, x, y)

    fun distance(gameObject: GameObject) = circle.center.distance(gameObject.circle.center)

    fun distance(point: Point) = circle.center.distance(point)

}