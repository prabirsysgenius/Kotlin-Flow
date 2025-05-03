package com.genius.kotlinflows

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private val TAG = "Kotlin_Flows"
    val channel = Channel<Int>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        /*CoroutineScope(Dispatchers.Main).launch{
            //TODO: First
            getUserNames().forEach {
                Log.d(TAG, it)
            }
        }*/
        //producer()
        //consumer()
         GlobalScope.launch {
            //Todo: this code is our consumer
            val data:Flow<Int> =  producer()
            data.collect{
                Log.e(TAG, "COLLECT - 1: "+it.toString())
            }
        }

        GlobalScope.launch {
            //Todo: this code is our consumer
            val data:Flow<Int> =  producer()
            delay(2500)
            data.collect{
                Log.e(TAG, "COLLECT - 2: "+it.toString())
            }
        }

    }
    fun producer() = flow<Int>{
        //TODO: the block is suspend block,because by default the flow's are create a coroutine scope
        //TODO: Flow is cold in nature, which means that if no consumer is present, the producer will not produce or execute.
        val list = listOf(1,2,3,4,5,6,7,8,9,10)
        list.forEach {
            delay(1000)
            //Log.e(TAG, "producer: "+it)
            emit(it)
        }
    }
    /*private fun producer(){
        //TODO: Second
        CoroutineScope(Dispatchers.Main).launch {
            channel.send(1)
            channel.send(2)
        }
    }*/

    /*private fun consumer(){
        //TODO: Second
        CoroutineScope(Dispatchers.Main).launch {
            Log.d(TAG, "FLOWS: "+channel.receive().toString())
            Log.d(TAG, "FLOWS: "+channel.receive().toString())
        }
    }*/
    private suspend fun getUserNames():List<String>{
        //TODO: First
        val list = mutableListOf<String>()
        list.add(getUser(1))
        list.add(getUser(2))
        list.add(getUser(3))
        return list
    }
    private suspend fun getUser(id:Int):String{
        //TODO: First
        delay(1000)
        return "User_${id}"
    }
}