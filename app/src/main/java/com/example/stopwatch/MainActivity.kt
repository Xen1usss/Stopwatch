package com.example.stopwatch

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.os.SystemClock
import android.widget.Chronometer
import com.example.stopwatch.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding // добавляет свойство binding
    lateinit var stopwatch: Chronometer // Хронометр
    var running = false // Хронометр работает?
    var offset: Long = 0 // Базовое смещение

    val OFFSET_KEY = "offset" // ключи для Bundle
    val RUNNING_KEY = "running"
    val BASE_KEY = "base"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater) // объявление свойства
        val view = binding.root // view присваивается корневое представление
        setContentView(view) // корневое представление передается setContentView
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        stopwatch = findViewById<Chronometer>(R.id.stopwatch) // Получение ссылки на секундомер

        if (savedInstanceState != null) { // восстановление предыдущего состояния
            offset = savedInstanceState.getLong(OFFSET_KEY)
            running = savedInstanceState.getBoolean(RUNNING_KEY)
            if (running) {
                binding.stopwatch.base = savedInstanceState.getLong(BASE_KEY)
                binding.stopwatch.start()
            } else setBaseTime()
        }

        binding.button1.setOnClickListener {
            if (!running) {
                setBaseTime() // установить правильное время
                binding.stopwatch.start() // запустить секундомер
                running = true
            }
        }

        binding.button2.setOnClickListener {
            saveOffset() // сохранить значение на секундомере
            binding.stopwatch.stop() // остановить его
            running = false
        }

        binding.button3.setOnClickListener {
            offset = 0
            setBaseTime() // обнулить показания секундомера
        }
    }

    override fun onPause() {
        super.onPause()
        if (running) {
            saveOffset()
            binding.stopwatch.stop()
        }
    }

    override fun onResume() {
        super.onResume()
        if (running) {
            setBaseTime()
            binding.stopwatch.start()
            offset = 0
        }
    }

    override fun onSaveInstanceState(savedInstanceState: Bundle) { // сохранение значений на случай перезапуска активити
        savedInstanceState.putLong(OFFSET_KEY, offset)
        savedInstanceState.putBoolean(RUNNING_KEY, running)
        savedInstanceState.putLong(BASE_KEY, binding.stopwatch.base)
        super.onSaveInstanceState(savedInstanceState)
    }

    fun setBaseTime() { // Обновляет время stopwatch.base
        stopwatch.base = SystemClock.elapsedRealtime() - offset
    }

    fun saveOffset() { // Сохраняет offset
        offset = SystemClock.elapsedRealtime() - binding.stopwatch.base
    }
}