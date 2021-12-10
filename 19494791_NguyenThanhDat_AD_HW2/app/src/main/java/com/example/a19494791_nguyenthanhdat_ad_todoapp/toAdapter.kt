package com.example.a19494791_nguyenthanhdat_ad_todoapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.item_todo.view.*


class toAdapter(
    var todos:List<toDo>
) : RecyclerView.Adapter<toAdapter.todoViewHolder>() {
    inner class todoViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): todoViewHolder {
    var view = LayoutInflater.from(parent.context).inflate(R.layout.item_todo,parent,false)
        return todoViewHolder(view)
    }

    override fun getItemCount(): Int {
        return todos.size
    }

    override fun onBindViewHolder(holder: todoViewHolder, position: Int) {
        holder.itemView.apply {
            textView.text = todos[position].context

        }
    }

}