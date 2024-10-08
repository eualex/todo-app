package com.example.todoapp.presentation.screens.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todoapp.R
import com.example.todoapp.databinding.FragmentSearchBinding
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchFragment : Fragment() {
    private val viewModel: SearchViewModel by viewModel()
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        val view = binding.root

        setupBackButton()
        setupFilterSection()
        setupSearchResult()

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupSearchResult() {
        binding.rvSearchResult.layoutManager = LinearLayoutManager(requireActivity())

        lifecycleScope.launch {
            viewModel.result.collect { searchResult ->
                when(searchResult) {
                    is SearchResult.TaskResult -> {
                        binding.rvSearchResult.adapter = SearchResultTaskListAdapter(searchResult.taskList)
                        binding.tvSearchResult.text = "Tarefas"
                        binding.tvSearchResult.visibility = View.VISIBLE
                        binding.tvSearchResult.setBackgroundResource(0)
                    }
                    is SearchResult.CategoryResult -> {
                        binding.rvSearchResult.adapter = SearchResultCategoryListAdapter(searchResult.categoryList)
                        binding.tvSearchResult.text = "Categorias"
                        binding.tvSearchResult.visibility = View.VISIBLE
                        binding.tvSearchResult.setBackgroundResource(R.drawable.bottom_border)
                    }
                    is SearchResult.Loading -> {
                        binding.tvSearchResult.visibility = View.INVISIBLE
                    }
                }
            }
        }
    }

    private fun setupBackButton() {
        binding.btBack.setOnClickListener {
            findNavController()
                .navigate(R.id.action_searchFragment_to_homeFragment)
        }
    }

    private fun setupFilterSection() {
        fun changeCardState(filterType: FilterType, isActive: Boolean) {
            val (card, cardText, cardImage) = when (filterType) {
                FilterType.Task -> Triple(
                    binding.cvTaskCard,
                    binding.tvTaskCard,
                    binding.imgTaskCard
                )

                FilterType.Category -> Triple(
                    binding.cvCategoryCard,
                    binding.tvCategoryCard,
                    binding.imgCategoryCard
                )

                FilterType.Empty -> Triple(null, null, null)
            }

            val cardBackgroundColor = ContextCompat.getColor(
                requireContext(),
                if (isActive) R.color.green_200 else R.color.gray_250
            )

            val cardItemColor = ContextCompat.getColor(
                requireContext(),
                if (isActive) R.color.white else R.color.gray_500
            )

            card?.setCardBackgroundColor(cardBackgroundColor)
            cardText?.setTextColor(cardItemColor)
            cardImage?.setColorFilter(cardItemColor)
        }

        binding.cvTaskCard.setOnClickListener {
            viewModel.setFilterType(FilterType.Task)
            viewModel.toggleResult()

        }

        binding.cvCategoryCard.setOnClickListener {
            viewModel.setFilterType(FilterType.Category)
            viewModel.toggleResult()
        }

        lifecycleScope.launch {
            viewModel.filterBy.collect { filterTypeState ->
                // TODO: turn simple
                when (filterTypeState) {
                    FilterType.Task -> {
                        changeCardState(FilterType.Task, true)
                        changeCardState(FilterType.Category, false)
                    }

                    FilterType.Category -> {
                        changeCardState(FilterType.Task, false)
                        changeCardState(FilterType.Category, true)
                    }

                    FilterType.Empty -> {}
                }
            }
        }
    }
}