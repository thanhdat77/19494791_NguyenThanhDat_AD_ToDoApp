package com.example.a19494791_nguyenthanhdat_ad_todoapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.lang.Exception
import java.lang.StringBuilder
import android.content.DialogInterface

import android.text.Editable
import android.view.Menu
import android.view.MenuItem

import android.widget.EditText




class MainActivity : AppCompatActivity() {
    val db = Firebase.firestore.collection("toDo")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        retrievetodo()
        var todolist = mutableListOf<toDo>()
        val adapter = toAdapter(todolist)
        ryview.adapter = adapter
        ryview.layoutManager = LinearLayoutManager(this)


        bntUploadData.setOnClickListener(){
            var alert = AlertDialog.Builder(this)
            val edittext = EditText(this)
            alert.setTitle("Ask new task")
            alert.setView(edittext)
            alert.setPositiveButton("Save",
                DialogInterface.OnClickListener { dialog, whichButton ->
                    val todo = toDo(edittext.text.toString(),edittext.text.toString(),false)
                    todolist.add(todo)
                    savetoDo(todo)
                    retrievetodo()
                    adapter.notifyItemInserted(todolist.size-1)
                })
            alert.setNegativeButton("Cancel", { dialog, whichButton ->})
            alert.show()
        }


    }



    private fun savetoDo(todo: toDo)= CoroutineScope(Dispatchers.IO).launch {
        try {
            db.add(todo).await()
            withContext(Dispatchers.Main){
//                Toast.makeText(this@MainActivity, "success", Toast.LENGTH_SHORT).show()
            }
        }catch (e:Exception){
            withContext(Dispatchers.Main){
                Toast.makeText(this@MainActivity, e.message, Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun retrievetodo()= CoroutineScope(Dispatchers.IO).launch {
        try {
            val querySnapshot = db.get().await()
            val sb = StringBuilder()
            var todolist = mutableListOf<toDo>()
            for (document in querySnapshot.documents){
                val todos = document.toObject<toDo>()
                if (todos != null) {
                    todolist.add(todos)
                }

            }
            withContext(Dispatchers.Main){
                Toast.makeText(this@MainActivity, "retrieve", Toast.LENGTH_SHORT).show()
            }


        }catch (e:Exception){
            withContext(Dispatchers.Main){
                Toast.makeText(this@MainActivity, e.message, Toast.LENGTH_SHORT).show()
            }
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.bar_menu,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.ic_edit -> Toast.makeText(this, "edit",Toast.LENGTH_SHORT).show()
            R.id.ic_done -> Toast.makeText(this, "done",Toast.LENGTH_SHORT).show()
            R.id.ic_delete -> Toast.makeText(this, "delete",Toast.LENGTH_SHORT).show()
        }
        return super.onOptionsItemSelected(item)
    }

}

