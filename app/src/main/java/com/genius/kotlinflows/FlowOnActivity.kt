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
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class FlowOnActivity : AppCompatActivity() {
    private val TAG = "FlowOnActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_flow_on)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        /** TODO:
         * Flows preserve their coroutine context.
         *
         * By default, a flow runs in the context where it is collected — meaning both the emission and collection happen in the same coroutine context.
         *
         * But if you want the flow to emit values in one context (like Dispatchers.IO)
         * and collect them in another (like Dispatchers.Main), you need to explicitly
         * switch the context using the flowOn operator.
         * The flowOn operator tells the flow to execute the emission part in a different
         * coroutine context.
         *
         * flowOn is working upstream.
         * */

        GlobalScope.launch(Dispatchers.Main) {
            producer()
                .map {
                    delay(1000)
                    it * 2
                    Log.d(TAG, "MAP - Collector Thread:- ${Thread.currentThread().name}")
                    // Ran on IO thread
                }
                .flowOn(Dispatchers.IO) //TODO: All operator define above the "flowOn" operator run on - IO thread
                .filter {
                    delay(500)
                    Log.d(TAG, "FILTER - Collector Thread:- ${Thread.currentThread().name}")
                    it < 8
                    // Ran on main thread
                }
                .flowOn(Dispatchers.Main)
                .collect{
                    Log.d(TAG, "Collector Thread:- ${Thread.currentThread().name}" )
                    //TODO: Run on Main Thread
                }
        }
    }

    private fun producer(): Flow<Int> {
        return flow <Int>{
            val list = listOf(1,2,3,4,5)
            list.forEach{
                delay(1000)
                Log.d(TAG, "Emitter Thread- ${Thread.currentThread().name}")
                //TODO: Run on IO thread
                emit(it)
            }
        }
    }
}