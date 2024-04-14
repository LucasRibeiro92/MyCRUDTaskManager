package com.example.mycrudtaskmanager

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView

class TasksAdapter(private var tasks: MutableList<Task>, context: Context) :
    RecyclerView.Adapter<TasksAdapter.TaskViewHolder>() {

        private val db: TaskDatabaseHelper = TaskDatabaseHelper(context)

        class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val titleTextView: TextView =  itemView.findViewById(R.id.titleTextView)
            val contentTextView: TextView = itemView.findViewById(R.id.contentTextView)
            val updateButton: ImageView = itemView.findViewById(R.id.updateButton)
            val completedCheckBox: CheckBox = itemView.findViewById(R.id.completedCheckBox)
            //val deleteButton: ImageView = itemView.findViewById(R.id.deleteButton)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.task_item, parent, false)
        return TaskViewHolder(view)
    }

    override fun getItemCount(): Int = tasks.size

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = tasks[position]
        holder.titleTextView.text = task.title
        holder.contentTextView.text = task.content
        //holder.completedCheckBox.setOnCheckedChangeListener(null)
        Log.d("TaskAdapter", "${task.completed}")
        holder.completedCheckBox.isChecked = task.completed

        holder.updateButton.setOnClickListener {
            val intent = Intent(holder.itemView.context, UpdateTaskActivity::class.java).apply {
                putExtra("task_id", task.id)
            }
            holder.itemView.context.startActivity(intent)
        }

        holder.completedCheckBox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked != task.completed) {
                task.completed = isChecked
                db.updateTask(task)
                Toast.makeText(holder.itemView.context, "Task Updated", Toast.LENGTH_SHORT).show()
            }
        }
        /*
        holder.completedCheckBox.setOnCheckedChangeListener { _, isChecked ->
            task.completed = isChecked
            db.updateTask(task)
            Toast.makeText(holder.itemView.context, "Task Updated", Toast.LENGTH_SHORT).show()
        }
        */
    }

    @SuppressLint("NotifyDataSetChanged")
    fun refreshData(newTasks: MutableList<Task>) {
        tasks = newTasks.toMutableList()
        notifyDataSetChanged()
    }

    fun getTaskAtPosition(position: Int): Task {
        return tasks[position]
    }

    fun removeItem(position: Int) {
        tasks.removeAt(position)
        notifyItemRemoved(position)
    }

}