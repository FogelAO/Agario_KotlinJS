package game.entity.graphics

import kotlin.math.abs
import kotlin.math.pow

data class Circle(val center: Point, var radius: Double, val color: String = "#00FF00") {
    private val square: Square = Square(
        Point(center.x - radius, center.y - radius),
        Point(center.x - radius, center.y + radius),
        Point(center.x + radius, center.y + radius),
        Point(center.x + radius, center.y - radius)
    )

    fun isInside(point: Point) = (point.x - center.x).pow(2) + (point.y - center.y).pow(2) < radius.pow(2)

}
