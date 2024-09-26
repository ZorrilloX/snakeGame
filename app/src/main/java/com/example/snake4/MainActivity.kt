package com.example.snake4

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import com.example.snake4.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var snakeGame: SnakeGame
    private val gridWidth = 20  // Ancho de la matriz
    private val gridHeight = 30  // Alto de la matriz
    private val handler = Handler()
    private var isGameRunning = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        snakeGame = SnakeGame(gridWidth, gridHeight) // Crear el juego con el tamaño de la matriz ajustado
        setupEventListeners() // escuchar botones
        startGame() // Iniciar el juego
    }

    private fun setupEventListeners() {
        binding.btnArriba.setOnClickListener {
            if (snakeGame.direccion != Direccion.DOWN) {
                snakeGame.direccion = Direccion.UP
            }
        }
        binding.btnAbajo.setOnClickListener {
            if (snakeGame.direccion != Direccion.UP) {
                snakeGame.direccion = Direccion.DOWN
            }
        }
        binding.btnIzquierda.setOnClickListener {
            if (snakeGame.direccion != Direccion.RIGHT) {
                snakeGame.direccion = Direccion.LEFT
            }
        }
        binding.btnDerecha.setOnClickListener {
            if (snakeGame.direccion != Direccion.LEFT) {
                snakeGame.direccion = Direccion.RIGHT
            }
        }
    }

    private fun startGame() {
        isGameRunning = true
        handler.postDelayed(object : Runnable {
            override fun run() {
                if (isGameRunning) {
                    snakeGame.moverSnake()
                    if (verificarColisiones()) {
                        reiniciarJuego()
                        Toast.makeText(this@MainActivity, "¡Game Over!", Toast.LENGTH_SHORT).show()
                    } else {
                        verificarSiHayComida()
                        drawGame()
                    }
                    handler.postDelayed(this, 600)  // velocidad
                }
            }
        }, 600)
    }

    private fun drawGame() {
        val bitmap = Bitmap.createBitmap(binding.canvas.width, binding.canvas.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)

        // Ajusta el tamaño de las celdas basado en el tamaño de la matriz
        val cellWidth = binding.canvas.width / gridWidth
        val cellHeight = binding.canvas.height / gridHeight

        val paintSnake = Paint().apply { color = Color.BLACK }

        snakeGame.snake.forEach { part ->
            val left = part.second * cellWidth
            val top = part.first * cellHeight
            val right = left + cellWidth
            val bottom = top + cellHeight
            canvas.drawRect(left.toFloat(), top.toFloat(), right.toFloat(), bottom.toFloat(), paintSnake)
        }

        val paintFood = Paint().apply { color = Color.RED }

        val foodLeft = snakeGame.comida.second * cellWidth
        val foodTop = snakeGame.comida.first * cellHeight
        val foodRight = foodLeft + cellWidth
        val foodBottom = foodTop + cellHeight
        canvas.drawRect(foodLeft.toFloat(), foodTop.toFloat(), foodRight.toFloat(), foodBottom.toFloat(), paintFood)

        binding.canvas.setImageBitmap(bitmap)
    }

    private fun verificarColisiones(): Boolean {
        val head = snakeGame.snake.first()
        // Verifica colisiones con el cuerpo de la serpientelee
        if (snakeGame.snake.drop(1).contains(head)) {
            return true
        }
        return false
    }

    private fun verificarSiHayComida() {
        if (snakeGame.snake.first() == snakeGame.comida) {
            snakeGame.generarComida()
        }
    }

    private fun reiniciarJuego() {
        snakeGame = SnakeGame(gridWidth, gridHeight)
        drawGame()
    }
}
/* Verifica colisiones con los límites de la matriz
if (head.first < 0 || head.first >= gridHeight || head.second < 0 || head.second >= gridWidth) {
    return true
}*/