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
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

class ExceptionHandlingActivity : AppCompatActivity() {
    private val TAG = "ExceptionHandling"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exception_handling)
        GlobalScope.launch(Dispatchers.Main) {
            try {
                producer()
                    .collect{
                        Log.d(TAG, "Collector Thread:- ${Thread.currentThread().name}" )
                        //TODO: Run on Main Thread
                        //throw Exception("Error in collector")
                    }
            }catch (e:Exception){
                Log.d(TAG, "exception: "+e.message.toString())
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
                throw Exception("Error in emitter")
            }
        }.catch {
            Log.d(TAG, "Emitter catch - ${it.message}")
            emit(-1)
        }
    }
}