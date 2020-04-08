package game.entity.graphics

import kotlin.math.sqrt

data class Point(
    val x: Double = 0.0,
    val y: Double = 0.0
) {
    fun distance(point: Point): Double {
        val a = x - point.x
        val b = y - point.y

        return sqrt(a * a + b * b)
    }
}