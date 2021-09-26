package com.example.todoapphomework4.activityFragmentUI

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.input.getInputField
import com.afollestad.materialdialogs.input.input
import com.example.todoapphomework4.R
import com.example.todoapphomework4.adapter.RecyclerViewAdapter
import com.example.todoapphomework4.base.BaseFragment
import com.example.todoapphomework4.base.BaseRecyclerItemClickListener
import com.example.todoapphomework4.modelsResponses.Task
import com.example.todoapphomework4.modelsResponses.TaskResponse
import com.example.todoapphomework4.modelsResponses.UpdateResponse
import com.example.todoapphomework4.service.BaseCallBack
import com.example.todoapphomework4.service.ServiceConnector
import com.example.todoapphomework4.utils.gone
import com.example.todoapphomework4.utils.toast
import com.example.todoapphomework4.utils.visible

class HomeFragment : BaseFragment() {
    private lateinit var recyclerView: RecyclerView
    private val taskList = mutableListOf<Task>()
    private var fetchCount = 0
    private var pageCount = 0
    private var pageNo = 1
    private var limitedTaskCount = 0
    private val progressTask = Task()
    private val PAGE_LIMIT = 5
    private var SKIP_COUNT = 5

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = RecyclerViewAdapter(
            taskList,
            object : BaseRecyclerItemClickListener<Task> {
                override fun onItemClicked(clickedObject: Task, id: Int) {
                    when (id) {
                        R.id.complete_bg -> {
                            val flag: Boolean
                            when {
                                clickedObject.completed -> {
                                    clickedObject.completed = false
                                    flag = false
                                }
                                else -> {
                                    clickedObject.completed = true
                                    flag = true
                                }
                            }

                            val param = mutableMapOf<String, Boolean>().apply {
                                put("completed", flag)
                            }
                            ServiceConnector.restInterface.updateTask(clickedObject.id!!, param)
                                .enqueue(object : BaseCallBack<UpdateResponse>() {
                                    override fun onSuccess(updateResponse: UpdateResponse) {
                                        super.onSuccess(updateResponse)
                                        toast(getString(R.string.successfully_completed), 200)
                                    }

                                    override fun onFailure() {
                                        super.onFailure()
                                        toast(getString(R.string.failed))
                                    }
                                })
                        }
                        R.id.delete_bg -> {
                            SKIP_COUNT--
                            val clickedIndex = taskList.indexOf(clickedObject)
                            taskList.removeAt(clickedIndex)
                            recyclerView.adapter?.notifyItemRemoved(clickedIndex)
                            ServiceConnector.restInterface.deleteTask(clickedObject.id!!)
                                .enqueue(object : BaseCallBack<UpdateResponse>() {
                                    override fun onSuccess(updateResponse: UpdateResponse) {
                                        super.onSuccess(updateResponse)
                                        if (taskList.size == 0) {
                                            activity?.findViewById<RecyclerView>(R.id.home_recyclerView)
                                                ?.gone()
                                            activity?.findViewById<TextView>(R.id.home_textView_no_task)
                                                ?.visible()
                                        }
                                        toast(getString(R.string.successfully_completed), 200)
                                    }

                                    override fun onFailure() {
                                        super.onFailure()
                                        toast(getString(R.string.failed))
                                    }
                                })
                        }
                    }
                }
            })

        setPageCount()
        getMyTasks(0, adapter)

        when (pageCount) {
            0 -> toast(getString(R.string.wait_tasks), Toast.LENGTH_SHORT)
        }

        recyclerView =
            activity?.findViewById(R.id.home_recyclerView) ?: RecyclerView(requireContext())

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            var refreshList = false
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                if (dy <= 0) refreshList = true
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == RecyclerView.SCROLL_STATE_IDLE
                    && pageNo < pageCount
                    && !recyclerView.canScrollVertically(1)
                    && refreshList
                    && !taskList.contains(progressTask)
                ) {
                    fetchCount++
                    progressTask.viewType = 2
                    taskList.add(progressTask)
                    scrollToLast()
                    recyclerView.adapter?.notifyItemInserted(taskList.size - 1)
                    getMyTasks(SKIP_COUNT, adapter)
                    SKIP_COUNT += PAGE_LIMIT
                    pageNo++
                } else if (pageNo == pageCount) {
                    Log.d("Task state ", "no more tasks.")
                }
            }
        })

        activity?.findViewById<CardView>(R.id.floatingbtn)?.setOnClickListener {
            //adding task with MD
            MaterialDialog(requireContext()).show {
                val dialog = input { dialog, text ->
                    val param = mutableMapOf<String, String>().apply {
                        put("description", dialog.getInputField().text.toString())
                    }
                    ServiceConnector.restInterface.addTask(param)
                        .enqueue(object : BaseCallBack<UpdateResponse>() {
                            override fun onSuccess(updateResponse: UpdateResponse) {
                                super.onSuccess(updateResponse)
                                activity?.findViewById<RecyclerView>(R.id.home_recyclerView)?.visible()
                                activity?.findViewById<TextView>(R.id.home_textView_no_task)?.gone()
                                toast(getString(R.string.successfully_completed))
                            }

                            override fun onFailure() {
                                super.onFailure()
                                toast(getString(R.string.failed))
                            }
                        })
                }
                positiveButton(R.string.add_task_2)
                negativeButton(R.string.cancel)
            }
        }
    }

    //get tasks
    private fun getMyTasks(skip: Int, adapter: RecyclerViewAdapter) {
        ServiceConnector.restInterface.getTaskByPagination(PAGE_LIMIT, skip)
            .enqueue(object : BaseCallBack<TaskResponse>() {
                override fun onSuccess(taskResponse: TaskResponse) {
                    super.onSuccess(taskResponse)
                    if (fetchCount != 0) {
                        taskList.removeAt(taskList.size - 1)
                        recyclerView.adapter?.notifyItemRemoved(taskList.size - 1)
                    }
                    limitedTaskCount = taskResponse.count
                    if (taskResponse.count == 0) {
                        activity?.findViewById<RecyclerView>(R.id.home_recyclerView)?.gone()
                        activity?.findViewById<TextView>(R.id.home_textView_no_task)?.visible()
                    } else {
                        taskList.addAll(taskResponse.data)
                        scrollToLast()
                        recyclerView.adapter = adapter
                    }
                }

                override fun onFailure() {
                    super.onFailure()
                    toast(getString(R.string.an_error))
                }
            })
    }

    //paging
    private fun setPageCount() {
        ServiceConnector.restInterface.getAllTasks().enqueue(object : BaseCallBack<TaskResponse>() {
            override fun onSuccess(taskResponse: TaskResponse) {
                super.onSuccess(taskResponse)
                val quotient = taskResponse.count / SKIP_COUNT
                pageCount = if (taskResponse.count == quotient * SKIP_COUNT) quotient
                else quotient + 1
            }

            override fun onFailure() {
                super.onFailure()
                toast(getString(R.string.an_error))
            }
        })
    }

    fun scrollToLast() {
        recyclerView.scrollToPosition(taskList.size - 1)
    }

    override fun getLayoutID(): Int = R.layout.fragment_home
}