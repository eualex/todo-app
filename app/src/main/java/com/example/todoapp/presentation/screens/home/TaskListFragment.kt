import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todoapp.databinding.FragmentTaskGroupListBinding
import com.example.todoapp.presentation.screens.home.HomeViewModel
import com.example.todoapp.presentation.screens.home.TaskGroupListAdapter
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.activityViewModel

class TaskListFragment: Fragment() {
    private lateinit var binding: FragmentTaskGroupListBinding
    private val viewModel by activityViewModel<HomeViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTaskGroupListBinding.inflate(inflater, container, false)
        val view = binding.root

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val taskGroupListAdapter = TaskGroupListAdapter(viewModel.filteredTaskListGroup.value)

        binding.rvTaskGroupList.adapter = taskGroupListAdapter
        binding.rvTaskGroupList.layoutManager = LinearLayoutManager(view.context)

        lifecycleScope.launch {
            viewModel.filteredTaskListGroup.collect { filteredTaskListGroup ->
                taskGroupListAdapter.updateTaskListGroup(filteredTaskListGroup)
                binding.tvEmptyTask.visibility = if(filteredTaskListGroup.isEmpty()) {
                    View.VISIBLE
                } else  {
                    View.GONE
                }
            }
        }
    }
}