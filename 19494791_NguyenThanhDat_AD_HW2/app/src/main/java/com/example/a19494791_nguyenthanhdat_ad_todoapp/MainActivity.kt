package com.example.a19494791_nguyenthanhdat_ad_todoapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.ActionMode
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.RecyclerView
import com.example.a19494791_nguyenthanhdat_ad_todoapp.databinding.ActivityMainBinding
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity(), DialogAddTask.DialogAddItemListener {

    private lateinit var binding: ActivityMainBinding
    lateinit var adapter : TaskAdapter
    var actionMode: ActionMode? = null
    val tasks = ArrayList<Task>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.floatingActionButton.setOnClickListener {
            showDialog("Add New Task")
        }
        adapter = TaskAdapter(this,R.layout.task ,tasks, binding, this)
        val recyclerView = findViewById<View>(R.id.recycler_view) as RecyclerView
        recyclerView.adapter = adapter

        Task.getRecent()
            .addOnSuccessListener { querySnapshot ->
                val documents = querySnapshot.documents
                for (doc in documents) {
                    val task = Task(doc)
                    tasks.add(task)
                    adapter!!.notifyDataSetChanged()
                }
            }
            .addOnFailureListener { e ->
                Log.e(
                    "",
                    "fromCloudFirestore: Error loading ContactInfo data from Firestore - " + e.message
                );
            };
        adapter?.notifyDataSetChanged()
    }

    override fun onDialogPositiveClick(dialog: DialogFragment) {
        val cal: Calendar = Calendar.getInstance()
        val format = SimpleDateFormat("dd-MM-yyyy", Locale.ROOT)
        val taskDesc: EditText = dialog.dialog!!.findViewById(R.id.add_task)
        val deadline: EditText = dialog.dialog!!.findViewById(R.id.add_deadline)
        val uniqueID = UUID.randomUUID().toString()
        val task = Task(uniqueID, taskDesc.text.toString(), false, deadline.text.toString())
        writeNewTask(task)
        refresh()
        Toast.makeText(
            baseContext,
            "save sucessfuly!!",
            Toast.LENGTH_SHORT
        ).show()
    }

    override fun onDialogNegativeClick(dialog: DialogFragment) {
        Toast.makeText(
            baseContext,
            "cancel",
            Toast.LENGTH_SHORT
        ).show()
    }

    fun writeNewTask(task :Task) {
        Firebase.firestore.collection("task").document(task.id.toString())
            .set(task)
    }

    fun refresh() {
        tasks.clear()
        adapter.notifyDataSetChanged()
        Task.getRecent()
            .addOnSuccessListener { query ->
                val documents = query.documents
                for (doc: DocumentSnapshot in documents) {
                    val task = Task(doc)
                    tasks.add(task)
                    adapter.notifyItemInserted(tasks.size - 1)
                }
            }
    }

    fun showDialog(titleDialog:String) {
        val addItemFragment = DialogAddTask(titleDialog)
        addItemFragment.show(supportFragmentManager, "AddItemTag")
    }

}