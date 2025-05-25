package com.genius.kotlinflows

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class StateFlowActivity : AppCompatActivity() {
    private val TAG = "StateFlowActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_state_flow)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        // State Flow is hot flow
        // Video link: https://www.youtube.com/watch?v=kocXkln-F4Q&list=PLRKyZvuMYSIPJ84lXQSHMn8P-0J8jW5YT&index=6
        GlobalScope.launch(Dispatchers.Main) {
            val result = producer()
            Log.e(TAG, "result.value: "+result.value.toString())
            delay(6000)
            result.collect{
                Log.e(TAG, "Consumer: =>>>> $it")
            }
        }
    }

    private fun producer(): StateFlow<Int> {
        val mutableStateFlow = MutableStateFlow(10)
        GlobalScope.launch {
            delay(2000)
            mutableStateFlow.emit(20)
            delay(2000)
            mutableStateFlow.emit(30)
        }
        return mutableStateFlow
    }
}