package com.example.stopwatch

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.os.SystemClock
import android.widget.Button
import android.widget.Chronometer

class MainActivity : AppCompatActivity() {

    lateinit var stopwatch: Chronometer // Хронометр
    var running = false // Хронометр работает?
    var offset: Long = 0 // Базовое смещение


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        fun setBaseTime() { // Обновляет время stopwatch.base
            stopwatch.base = SystemClock.elapsedRealtime() - offset
        }

        fun saveOffset() { // Сохраняет offset
            offset = SystemClock.elapsedRealtime() - stopwatch.base
        }
        stopwatch = findViewById<Chronometer>(R.id.stopwatch) // Получение ссылки на секундомер

        val startButton = findViewById<Button>(R.id.button1)
        startButton.setOnClickListener {
            if (!running) {
                setBaseTime()
                stopwatch.start()
                running = true
            }
        }

        val pauseButton = findViewById<Button>(R.id.button2)
        pauseButton.setOnClickListener {
            saveOffset() // сохранить значение на секундомере
            stopwatch.stop() // остановить его
            running = false
        }

        val resetButton = findViewById<Button>(R.id.button3)
        resetButton.setOnClickListener {

        }
    }
}