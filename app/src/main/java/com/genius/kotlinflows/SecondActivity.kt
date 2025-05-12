package com.genius.kotlinflows

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.genius.kotlinflows.databinding.ActivitySecondBinding
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class SecondActivity : AppCompatActivity() {
    private val TAG = "SecondActivity"
    lateinit var binding:ActivitySecondBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySecondBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        GlobalScope.launch {
            getNotes().map {
                FormattedNotes(it.isActive,it.title.uppercase(),it.description)
            }.filter {
                 it.isActive
            }.collect{
                Log.d(TAG, "NOTE: "+it.toString())
            }
        }
    }

    private fun getNotes(): Flow<Note> {
        val list = listOf(
            Note(1, true, "First","First Description"),
            Note(2, true, "Second","Second Description"),
            Note(3, false, "Third","Third Description"),
            Note(4, true, "Fourth","Fourth Description"),
            Note(5, false, "Fifth","Fifth Description")
        )
        return list.asFlow() // asFlow() is builder, which is convert our list to flow.
    }

    data class Note(val id:Int,val isActive:Boolean,val title:String,val description:String)
    data class FormattedNotes(val isActive:Boolean,val title:String,val description:String)
}