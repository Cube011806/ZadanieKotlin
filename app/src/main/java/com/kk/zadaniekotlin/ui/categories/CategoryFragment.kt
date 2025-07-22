package com.kk.zadaniekotlin.ui.categories

import SharedViewModel
import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.kk.zadaniekotlin.R
import com.kk.zadaniekotlin.databinding.FragmentCategoryBinding

class CategoryFragment : Fragment() {

    private var _binding: FragmentCategoryBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CategoryViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private lateinit var adapter: CategoryAdapter

    private val categoryIdToName = mapOf(
        1 to "Kobiety",
        2 to "Mężczyźni",
        3 to "Niemowlak",
        4 to "Dziewczynka",
        5 to "Chłopiec",
        0 to "Wszystkie"
    )
    private val categoryNameToId = categoryIdToName.entries.associate { (id, name) -> name to id }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCategoryBinding.inflate(inflater, container, false)

        adapter = CategoryAdapter(mutableListOf(), sharedViewModel)
        binding.recyclerView2.layoutManager = GridLayoutManager(requireContext(), 3)
        binding.recyclerView2.adapter = adapter

        val args = CategoryFragmentArgs.fromBundle(requireArguments())
        val initialId = args.categoryId
        val initialName = categoryIdToName[initialId] ?: "Wszystkie"

        sharedViewModel.setCatId(initialId)

        if (initialId == 0) {
            sharedViewModel.setSubCatId(0)
            findNavController().navigate(R.id.navigation_dashboard)
        } else {
            if (viewModel.getCurrentCategoryName() == null) {
                viewModel.setCurrentCategory(initialId, initialName)
                viewModel.loadCategories(initialId)
            } else {
                viewModel.loadCategories(viewModel.getCurrentCategoryId())
            }
        }

        viewModel.uiState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is CategoryUiState.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.recyclerView2.visibility = View.GONE
                    binding.emptyTextView.visibility = View.GONE
                }
                is CategoryUiState.Success -> {
                    binding.progressBar.visibility = View.GONE
                    binding.recyclerView2.visibility = View.VISIBLE
                    binding.emptyTextView.visibility = View.GONE
                    adapter.updateData(state.categories)
                }
                is CategoryUiState.Empty -> {
                    binding.progressBar.visibility = View.GONE
                    binding.recyclerView2.visibility = View.GONE
                    binding.emptyTextView.visibility = View.VISIBLE
                }
                is CategoryUiState.Error -> {
                    binding.progressBar.visibility = View.GONE
                    binding.recyclerView2.visibility = View.VISIBLE
                    binding.emptyTextView.visibility = View.GONE
                    Toast.makeText(requireContext(), "Błąd: ${state.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.button.setOnClickListener {
            val categories = categoryNameToId.keys.toList()
            val selectedIndex = categories.indexOf(viewModel.getCurrentCategoryName() ?: "Wszystkie")
            var tempSelection = selectedIndex
            val cat = getString(R.string.filter_chooseCat)
            val confirmBut = getString(R.string.filter_confirm)
            val cancelBut = getString(R.string.filter_cancel)
            AlertDialog.Builder(requireContext())
                .setTitle(cat)
                .setSingleChoiceItems(categories.toTypedArray(), selectedIndex) { _, which ->
                    tempSelection = which
                }
                .setPositiveButton(confirmBut) { _, _ ->
                    val selectedName = categories[tempSelection]
                    val selectedId = categoryNameToId[selectedName] ?: return@setPositiveButton

                    sharedViewModel.setCatId(selectedId)

                    if (selectedId == 0) {
                        sharedViewModel.setSubCatId(0)
                        findNavController().navigate(R.id.navigation_dashboard)
                    } else {
                        viewModel.setCurrentCategory(selectedId, selectedName)
                        viewModel.loadCategories(selectedId)
                    }
                }
                .setNegativeButton(cancelBut, null)
                .show()
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
