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
import kotlinx.coroutines.launch

class MutableSharedFlowActivity : AppCompatActivity() {
    private val TAG = "MutableSharedFlowActivi"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_mutable_shared_flow)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        // TODO: Mutable Shared Flow is hot in nature.
        //YouTube Link: https://www.youtube.com/watch?v=GiogvwMmrvk&list=PLRKyZvuMYSIPJ84lXQSHMn8P-0J8jW5YT&index=5
        GlobalScope.launch(Dispatchers.Main) {
            val result = producer()
            result.collect{
                Log.e(TAG, "MutableSharedFlow_1: $it")
            }
        }

        GlobalScope.launch(Dispatchers.Main) {
            val result = producer()
            delay(2500)
            result.collect{
                Log.e(TAG, "MutableSharedFlow_2: $it")
            }
        }
    }

    private fun producer(): Flow<Int> {
        val mutableSharedFlow = MutableSharedFlow<Int>(3)
        GlobalScope.launch {
            val list = listOf<Int>(1,2,3,4,5,6,7,8,9,10)
            list.forEach{
                mutableSharedFlow.emit(it)
                delay(1000)
            }
        }
        return mutableSharedFlow
    }
}