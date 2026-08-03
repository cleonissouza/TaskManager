package com.example.taskmanager.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.taskmanager.R
import com.example.taskmanager.databinding.FragmentTaskListBinding
import com.example.taskmanager.model.Task
import com.example.taskmanager.viewmodel.TaskViewModel

class TaskListFragment : Fragment() {

    private var _binding: FragmentTaskListBinding? = null
    private val binding get() = _binding!!

    private lateinit var taskAdapter: TaskAdapter

    private val taskViewModel: TaskViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTaskListBinding.inflate(
            inflater,
            container,
            false
        )

        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        observeTasks()
        setupAddTaskButton()
    }

    private fun setupRecyclerView() {
        taskAdapter = TaskAdapter(
            taskList = emptyList(),

            onTaskClick = { task ->
                openEditTaskScreen(task)
            },

            onDeleteClick = { task ->
                showDeleteConfirmation(task)
            }
        )

        binding.recyclerViewTasks.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = taskAdapter
            setHasFixedSize(true)
        }
    }

    private fun observeTasks() {
        taskViewModel.allTasks.observe(viewLifecycleOwner) { tasks ->
            taskAdapter.updateTasks(tasks)
        }
    }

    private fun setupAddTaskButton() {
        binding.fabAddTask.setOnClickListener {
            parentFragmentManager
                .beginTransaction()
                .replace(
                    R.id.fragmentContainer,
                    AddTaskFragment()
                )
                .addToBackStack(null)
                .commit()
        }
    }

    private fun openEditTaskScreen(task: Task) {
        val fragment = AddTaskFragment.newInstance(task.id)

        parentFragmentManager
            .beginTransaction()
            .replace(
                R.id.fragmentContainer,
                fragment
            )
            .addToBackStack(null)
            .commit()
    }

    private fun showDeleteConfirmation(task: Task) {
        AlertDialog.Builder(requireContext())
            .setTitle("Excluir tarefa")
            .setMessage("Deseja excluir ${task.title}?")
            .setPositiveButton("Excluir") { _, _ ->
                taskViewModel.deleteTask(task)
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}