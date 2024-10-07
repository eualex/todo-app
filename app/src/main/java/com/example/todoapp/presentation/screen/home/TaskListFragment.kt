import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.Recycler
import com.example.todoapp.R
import com.example.todoapp.databinding.FragmentTaskGroupListBinding
import com.example.todoapp.domain.model.TaskGroup
import com.example.todoapp.presentation.screen.home.HomeViewModel
import com.example.todoapp.presentation.screen.home.TaskGroupListAdapter
import com.example.todoapp.presentation.screen.home.TaskListRecyclerAdapter
import kotlinx.coroutines.launch

class TaskListFragment: Fragment() {
    private lateinit var binding: FragmentTaskGroupListBinding
    private lateinit var viewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTaskGroupListBinding.inflate(inflater, container, false)
        val view = binding.root

        // TODO: Add koin
        viewModel = ViewModelProvider(requireActivity())[HomeViewModel::class.java]

        val taskGroupListAdapter = TaskGroupListAdapter(viewModel.filteredTaskListGroup.value)

        binding.rvTaskGroupList.adapter = taskGroupListAdapter
        binding.rvTaskGroupList.layoutManager = LinearLayoutManager(view.context)

        lifecycleScope.launch {
            viewModel.filteredTaskListGroup.collect(taskGroupListAdapter::updateTaskListGroup)
        }

        return view
    }
}