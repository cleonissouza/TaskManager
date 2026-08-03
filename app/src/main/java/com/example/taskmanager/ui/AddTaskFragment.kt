package com.example.taskmanager.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.taskmanager.R
import com.example.taskmanager.databinding.FragmentAddTaskBinding
import com.example.taskmanager.model.Task
import com.example.taskmanager.viewmodel.TaskViewModel
import kotlinx.coroutines.launch

class AddTaskFragment : Fragment() {

    private var _binding: FragmentAddTaskBinding? = null
    private val binding get() = _binding!!

    private val taskViewModel: TaskViewModel by viewModels()

    private var taskId: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentAddTaskBinding.inflate(
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

        setupPrioritySpinner()
        setupStatusSpinner()

        taskId = arguments?.getInt(TASK_ID) ?: 0

        if (taskId != 0) {
            loadTask()
        }

        setupSaveButton()
    }

    private fun setupPrioritySpinner() {
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.task_priorities,
            android.R.layout.simple_spinner_item
        ).also { adapter ->

            adapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
            )

            binding.spinnerPriority.adapter = adapter
        }
    }

    private fun setupStatusSpinner() {
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.task_statuses,
            android.R.layout.simple_spinner_item
        ).also { adapter ->

            adapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
            )

            binding.spinnerStatus.adapter = adapter
        }
    }

    private fun setupSaveButton() {
        binding.buttonSaveTask.setOnClickListener {
            saveTask()
        }
    }

    private fun loadTask() {
        viewLifecycleOwner.lifecycleScope.launch {

            val task = taskViewModel.getTaskById(taskId)

            if (task != null) {
                binding.editTextTitle.setText(task.title)
                binding.editTextDate.setText(task.date)

                selectSpinnerValue(
                    binding.spinnerPriority,
                    task.priority
                )

                selectSpinnerValue(
                    binding.spinnerStatus,
                    task.status
                )

                binding.buttonSaveTask.text = "Atualizar tarefa"
            }
        }
    }

    private fun selectSpinnerValue(
        spinner: Spinner,
        value: String
    ) {
        val adapter = spinner.adapter

        for (position in 0 until adapter.count) {
            val currentValue =
                adapter.getItem(position).toString()

            if (currentValue == value) {
                spinner.setSelection(position)
                break
            }
        }
    }

    private fun saveTask() {
        val title =
            binding.editTextTitle.text.toString().trim()

        val date =
            binding.editTextDate.text.toString().trim()

        val priority =
            binding.spinnerPriority.selectedItem.toString()

        val status =
            binding.spinnerStatus.selectedItem.toString()

        if (title.isEmpty()) {
            binding.editTextTitle.error = "Digite o título"
            binding.editTextTitle.requestFocus()
            return
        }

        if (date.isEmpty()) {
            binding.editTextDate.error = "Digite a data"
            binding.editTextDate.requestFocus()
            return
        }

        if (taskId == 0) {
            val newTask = Task(
                title = title,
                date = date,
                priority = priority,
                status = status
            )

            taskViewModel.insertTask(newTask)
        } else {
            val updatedTask = Task(
                id = taskId,
                title = title,
                date = date,
                priority = priority,
                status = status
            )

            taskViewModel.updateTask(updatedTask)
        }

        parentFragmentManager.popBackStack()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {

        private const val TASK_ID = "taskId"

        fun newInstance(taskId: Int): AddTaskFragment {
            return AddTaskFragment().apply {
                arguments = Bundle().apply {
                    putInt(TASK_ID, taskId)
                }
            }
        }
    }
}