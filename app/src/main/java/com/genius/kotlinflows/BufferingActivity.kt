package com.genius.kotlinflows

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.genius.kotlinflows.databinding.ActivityBufferingBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlin.system.measureTimeMillis

class BufferingActivity : AppCompatActivity() {
    private val TAG = "BufferingActivity"
    lateinit var binding:ActivityBufferingBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityBufferingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        GlobalScope.launch(Dispatchers.Main) {
            val time = measureTimeMillis {
                producer()
                    .buffer(3)
                    .collect{
                    delay(1500)
                    Log.d(TAG, "onCreate: "+it.toString())
                }
            }
            Log.d(TAG, "Time: "+time.toString())
        }
    }

    private fun producer(): Flow<Int> {
        return flow <Int>{
            val list = listOf(1,2,3,4,5)
            list.forEach{
                delay(1000)
                emit(it)
            }
        }
    }
}