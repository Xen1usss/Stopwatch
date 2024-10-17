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

    val OFFSET_KEY = "offset" // ключи для Bundle
    val RUNNING_KEY = "running"
    val BASE_KEY = "base"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        stopwatch = findViewById<Chronometer>(R.id.stopwatch) // Получение ссылки на секундомер

        fun setBaseTime() { // Обновляет время stopwatch.base
            stopwatch.base = SystemClock.elapsedRealtime() - offset
        }

        fun saveOffset() { // Сохраняет offset
            offset = SystemClock.elapsedRealtime() - stopwatch.base
        }

        if (savedInstanceState != null) { // восстановление предыдущего состояния
            offset = savedInstanceState.getLong(OFFSET_KEY)
            running = savedInstanceState.getBoolean(RUNNING_KEY)
            if (running) {
                stopwatch.base = savedInstanceState.getLong(BASE_KEY)
                stopwatch.start()
            } else setBaseTime()
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val startButton = findViewById<Button>(R.id.button1)
        startButton.setOnClickListener {
            if (!running) {
                setBaseTime() // установить правильное время
                stopwatch.start() // запустить секундомер
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
            offset = 0
            setBaseTime() // обнулить показания секундомера
        }
    }

    override fun onSaveInstanceState(savedInstanceState: Bundle) { // сохранение значений на случай перезапуска активити
        savedInstanceState.putLong(OFFSET_KEY, offset)
        savedInstanceState.putBoolean(RUNNING_KEY, running)
        savedInstanceState.putLong(BASE_KEY, stopwatch.base)
        super.onSaveInstanceState(savedInstanceState)
    }
}