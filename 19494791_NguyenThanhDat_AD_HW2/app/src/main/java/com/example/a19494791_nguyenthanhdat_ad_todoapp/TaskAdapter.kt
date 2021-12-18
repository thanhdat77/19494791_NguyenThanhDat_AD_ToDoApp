package com.example.a19494791_nguyenthanhdat_ad_todoapp

import android.content.ContentValues
import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView


import android.R.id
import android.content.ContentValues.TAG
import android.view.MenuInflater
import androidx.core.view.isVisible
import android.widget.Toast
import com.example.a19494791_nguyenthanhdat_ad_todoapp.databinding.ActivityMainBinding
import com.google.firebase.firestore.FieldValue


class TaskAdapter(val context: Context,
                  val layoutId: Int,
                  val tasks: ArrayList<Task>,
                  private val mainBinding: ActivityMainBinding,
                  val mainThis:MainActivity
) :
    RecyclerView.Adapter<TaskAdapter.ViewHolder>() {

    var isShow : Boolean? = true

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view: View = LayoutInflater.from(parent.context)
            .inflate(layoutId, parent, false)
        return ViewHolder(layoutId, view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val task = tasks[position]
        try {
            holder.deadline!!.text = task.deadline
            holder.desc!!.text = task.desc
            holder.itemView.setOnClickListener {
                OnclickItems(holder, it, position)
                val inflater = MenuInflater(mainBinding.root.context)
                inflater.inflate(R.menu.contextual_action_bar, mainBinding.topAppBar.menu)
                if (isShow == true) {
                    holder.checkBox?.setVisibility(View.INVISIBLE);
                    arrayOf(
                        R.id.btnCheck,
                        R.id.btnDelete,
                        R.id.btnEdit
                    ).map {
                        mainBinding
                            .topAppBar
                            .menu
                            .findItem(it)
                            .isVisible = false
                    }
                    isShow = false
                } else {
                    holder.checkBox?.setVisibility(View.VISIBLE);
                    arrayOf(
                        R.id.btnCheck,
                        R.id.btnDelete,
                        R.id.btnEdit
                    ).map {
                        mainBinding
                            .topAppBar
                            .menu
                            .findItem(it)
                            .isVisible = true
                    }
                    isShow = true
                }
            }
        } catch (ignored: Exception) {
        }
        val textView = holder.itemView.findViewById<TextView>(R.id.status)
        if (task.status == false) {
            textView
            textView
                .setText("Not complete")
            textView
                .setTextColor(
                    Color.parseColor("#FF1700")
                )
        } else {
            textView
                .setText("Complete")
            textView
                .setTextColor(
                    Color.parseColor("#9AE66E")
                )
        }
    }

    private fun OnclickItems(holder: ViewHolder, view: View, position: Int){
        mainBinding.topAppBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.btnEdit -> {
                    holder.checkBox?.setEnabled(false); // disable checkbox
                    mainThis.showDialog("Edit Task")
                    mainThis.refresh()
                    true
                }
                R.id.btnDelete -> {
                    if(holder.checkBox!!.isChecked) {
                        FirebaseUtils.db
                            .collection("task")
                            .document(tasks[position].id.toString())
                            .delete()
                            .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully deleted!") }
                            .addOnFailureListener { e -> Log.w(TAG, "Error deleting document", e) }
                    }
                    mainThis.refresh()
                    true
                }
                R.id.btnCheck -> {
                    if(holder.checkBox!!.isChecked) {
                        FirebaseUtils.db
                            .collection("task")
                            .document(tasks[position].id.toString())
                            .update("status",  true)
                            .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully deleted!") }
                            .addOnFailureListener { e -> Log.w(TAG, "Error deleting document", e) }
                    }
                    mainThis.refresh()
                    true
                }
                else -> false
            }
        }
    }

    override fun getItemCount(): Int {
        return tasks.size
    }

    class ViewHolder(layoutId: Int, itemView: View) : RecyclerView.ViewHolder(itemView) {
        var deadline: TextView? = null
        var desc: TextView? = null
        var status: TextView? =null
        var checkBox : CheckBox? = null
        init {
            deadline = itemView.findViewById(R.id.deadline)
            desc = itemView.findViewById(R.id.desc)
            status = itemView.findViewById(R.id.status)
            checkBox = itemView.findViewById(R.id.checkBox)
        }
    }
}