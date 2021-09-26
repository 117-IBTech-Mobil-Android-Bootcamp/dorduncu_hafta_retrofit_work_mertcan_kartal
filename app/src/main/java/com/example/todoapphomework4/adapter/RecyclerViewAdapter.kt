package com.example.todoapphomework4.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapphomework4.R
import com.example.todoapphomework4.base.BaseRecyclerItemClickListener
import com.example.todoapphomework4.modelsResponses.Task
import ru.rambler.libs.swipe_layout.SwipeLayout

class RecyclerViewAdapter(var taskList: MutableList<Task>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var itemClickListener: BaseRecyclerItemClickListener<Task>? = null

    constructor(
        list: MutableList<Task>,
        itemClickListener: BaseRecyclerItemClickListener<Task>
    ) : this(list) {
        this.itemClickListener = itemClickListener
    }

    companion object {
        const val VIEW_TYPE_ONE = 1
        const val VIEW_TYPE_TWO = 2
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_ONE) {
            TaskViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.recycler_row, parent, false)
            )
        } else {
            BlankRowViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.row_blank, parent, false)
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val task_todo = this.taskList[position]
        if (taskList[position].viewType == VIEW_TYPE_ONE && holder is TaskViewHolder) {
            if (!task_todo.swipedOrNot) holder.swipeLayout.reset()
            else holder.swipeLayout.animateSwipeRight()
            holder.setData(task_todo)
            holder.setOnItemClickListener(task_todo, this.itemClickListener!!)

            holder.swipeLayout.setOnSwipeListener(object : SwipeLayout.OnSwipeListener {
                override fun onBeginSwipe(swipeLayout: SwipeLayout?, moveToRight: Boolean) {
                    task_todo.swipedOrNot = !task_todo.swipedOrNot
                }

                override fun onSwipeClampReached(swipeLayout: SwipeLayout?, moveToRight: Boolean) {
                    Log.d("Operation: ", "skipped.")
                }

                override fun onLeftStickyEdge(swipeLayout: SwipeLayout?, moveToRight: Boolean) {
                    Log.d("Operation: ", "skipped.")
                }

                override fun onRightStickyEdge(swipeLayout: SwipeLayout?, moveToRight: Boolean) {
                    Log.d("Operation: ", "skipped.")
                }

            })
        } else {
            this.taskList[position]
        }
    }

    override fun getItemViewType(position: Int): Int {
        return taskList[position].viewType
    }

    override fun getItemCount(): Int = taskList.size
}


    // views of recycler row's components
class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        //task status(complete or not)
    var completionIcon: ImageView = itemView.findViewById(R.id.row_task_complete_image)

        //task text
    var taskText: TextView = itemView.findViewById(R.id.row_task_text)

        //swipe Recycler
        var swipeLayout: SwipeLayout = itemView.findViewById(R.id.swipeRecycler)

    fun setData(task: Task) {
        changeCompletion(task)
        taskText.text = task.description
    }

    //swipe to left recycler row and click to complete task or delete task
    fun setOnItemClickListener(
        task: Task,
        itemClickListener: BaseRecyclerItemClickListener<Task>?
    ) {
        val completeTask = itemView.findViewById<FrameLayout>(R.id.complete_bg)
        completeTask.setOnClickListener {
            itemClickListener!!.onItemClicked(task, it.id)
            changeCompletion(task)
        }
        val deleteTask = itemView.findViewById<FrameLayout>(R.id.delete_bg)
        deleteTask.setOnClickListener {
            itemClickListener!!.onItemClicked(task, it.id)
        }
    }

    //change task completable icon
    private fun changeCompletion(task: Task) {
        if (task.completed) completionIcon.setImageResource(R.drawable.ic_marked)
        else completionIcon.setImageResource(R.drawable.ic_blank)
    }
}

class BlankRowViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)