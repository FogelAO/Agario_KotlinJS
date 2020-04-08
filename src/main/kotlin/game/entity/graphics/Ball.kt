package game.entity.graphics

data class Ball(var circle: Circle) {

    fun offset(x: Double, y: Double) {
        val center = circle.center
        circle = circle.copy(center = Point(center.x + x, center.y + y))
    }
}