import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.example.todoapp.databinding.FragmentTaskListBinding
import com.example.todoapp.domain.model.Task
import com.example.todoapp.domain.model.TaskGroup
import com.example.todoapp.presentation.screen.home.TaskListRecyclerAdapter

class TaskListFragment(private val taskList: List<TaskGroup>): Fragment() {
    private lateinit var binding: FragmentTaskListBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTaskListBinding.inflate(inflater, container, false)
        val view = binding.root

//        arguments?.apply {
//            binding.textView.text = getString(TITLE)
//        }

        binding.taskList.adapter = TaskListRecyclerAdapter(taskList)
        binding.taskList.layoutManager = LinearLayoutManager(requireContext())

        return view
    }

//    companion object {
//        fun create(taskList: List<TaskGroup>): TaskListFragment {
//            val fg = TaskListFragment()
//
//            val args = Bundle().apply {
//                putArr
//                putString(TITLE, task.title)
//            }
//
//            fg.arguments = args
//
//            return fg
//        }
//
//        const val TITLE = "TASK_NAME"
//        const val DATE = "TASK_DATE"
//        const val CATEGORY = "TASK_CATEGORY"
//    }
}