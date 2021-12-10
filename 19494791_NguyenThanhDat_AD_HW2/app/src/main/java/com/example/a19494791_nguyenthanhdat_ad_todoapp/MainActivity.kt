package com.example.a19494791_nguyenthanhdat_ad_todoapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.lang.Exception

class MainActivity : AppCompatActivity() {
    val db = Firebase.firestore.collection("toDo")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var todolist = mutableListOf(
            toDo("làm bài tập","now",false) ,
            toDo("nộp bài","tomorow",false)

        )
        val addContactDialog = AlertDialog.Builder(this)
            .setTitle("add firebase")

            .setPositiveButton("SAVE"){dialogInterface,i->
                Toast.makeText(this,"success",Toast.LENGTH_SHORT).show()
            }

            .setNegativeButton("CANCEL"){dialogInterface,i->
                Toast.makeText(this,"no success",Toast.LENGTH_SHORT).show()
            }.create()


        val adapter = toAdapter(todolist)
        ryview.adapter = adapter
        ryview.layoutManager = LinearLayoutManager(this)


        bntUploadData.setOnClickListener(){
            addContactDialog.show()
            val title = input.text.toString()
            val todo = toDo(title,title,false)
            todolist.add(todo )
            adapter.notifyItemInserted(todolist.size-1)
            val context = input.text.toString()
            savetoDo(todo)
        }
    }
    private fun savetoDo(todo: toDo)= CoroutineScope(Dispatchers.IO).launch {
        try {
            db.add(todo).await()
            withContext(Dispatchers.Main){
                Toast.makeText(this@MainActivity, "success", Toast.LENGTH_SHORT).show()
            }
        }catch (e:Exception){
            withContext(Dispatchers.Main){
                Toast.makeText(this@MainActivity, e.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

}

