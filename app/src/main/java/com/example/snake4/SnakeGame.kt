package com.example.snake4
import kotlin.random.Random

class SnakeGame(private val matrizAncho: Int, private val matrizAlto: Int) {
    var snake = mutableListOf(
        Pair(matrizAlto / 2, matrizAncho / 2),  // cabeza
        Pair(matrizAlto / 2, matrizAncho / 2 - 1),  // segundo segmento detrás
        Pair(matrizAlto / 2, matrizAncho / 2 - 2) // 3ro
    )
    var direccion = Direccion.RIGHT
    var comida = generarComida()

    fun moverSnake() { // mover la serpiente en la dirección actual
        val cabeza = snake.first()
        val nuevaCabeza = when (direccion) {
            Direccion.UP -> Pair((cabeza.first - 1 + matrizAlto) % matrizAlto, cabeza.second)
            Direccion.DOWN -> Pair((cabeza.first + 1) % matrizAlto, cabeza.second)
            Direccion.LEFT -> Pair(cabeza.first, (cabeza.second - 1 + matrizAncho) % matrizAncho)
            Direccion.RIGHT -> Pair(cabeza.first, (cabeza.second + 1) % matrizAncho)
        }

        snake.add(0, nuevaCabeza)

        if (nuevaCabeza == comida) { // Si comió la comida
            comida = generarComida() // Generar nueva comida
        } else {
            snake.removeLast() //eliminar la cola
        }
    }

    fun generarComida(): Pair<Int, Int> { // Generar nueva comida en una posición aleatoria
        var nuevaComida: Pair<Int, Int>
        do {
            nuevaComida = Pair(Random.nextInt(matrizAlto), Random.nextInt(matrizAncho)) // genera posicion
        } while (snake.contains(nuevaComida))  // Evitar que la comida aparezca sobre la serpiente
        return nuevaComida
    }
}