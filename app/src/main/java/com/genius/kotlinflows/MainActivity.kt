package com.genius.kotlinflows

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.genius.kotlinflows.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private val TAG = "Kotlin_Flows"
    lateinit var binding: ActivityMainBinding
    val channel = Channel<Int>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        binding.btnSecond.setOnClickListener {
            val intent = Intent(MainAcitvity@this, SecondActivity::class.java)
            startActivity(intent)
        }
        binding.btnBuffering.setOnClickListener{
            val intent = Intent(MainAcitvity@this, BufferingActivity::class.java)
            startActivity(intent)
        }
        binding.btnFlowOn.setOnClickListener{
            val intent = Intent(MainAcitvity@this, FlowOnActivity::class.java)
            startActivity(intent)
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
           /* val data:Flow<Int> =  producer()
            data.collect{
                Log.e(TAG, "COLLECT - 1: "+it.toString())
            }*/
             producer()
                 .onStart {
                     emit(0) // "emit()" - method is used manually,here we  emit - ZERO manually
                     Log.d(TAG, "onStart: called");
                 }.onCompletion {
                     emit(-11) // "emit()" - method is used manually,here we  emit - -11 manually
                     Log.d(TAG, "onCompletion: called");
                 }.onEach {
                     Log.d(TAG, "onEach: About to emit - this is called when an item is emit");
                 }.collect{
                     Log.d(TAG, "collect data: ${it}")
                 }
             /**
              *  In a Flow we have two types of operator - 1) terminal operator, 2) Non-terminal operator
              *  1) Terminal operator - are start our flow, meanse conjumtion of Flow is happen for  Terminal operator
              *   for example:  "collect" is a Terminal operator
              * */
             val result = producer().first() // first() - is used to pick first element of our flow.
             Log.d(TAG, "first(): the first element is: "+result)
             val list = producer().toList() // toList() - it convert our flow in a list
             Log.d(TAG, "list: "+list)

        }

        GlobalScope.launch {
            producer()
                .map {
                    // map - operator is help to map one object to another object
                    it * 2
                }
                .filter {
                    it < 8
                }.collect{
                    Log.e(TAG, "FLOW: "+it.toString())
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