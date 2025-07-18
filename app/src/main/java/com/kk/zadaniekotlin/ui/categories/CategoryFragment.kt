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

        viewModel.categories.observe(viewLifecycleOwner) { list ->
            adapter.updateData(list)
        }

        binding.button.setOnClickListener {
            val categories = categoryNameToId.keys.toList()
            val selectedIndex = categories.indexOf(viewModel.getCurrentCategoryName() ?: "Wszystkie")
            var tempSelection = selectedIndex

            AlertDialog.Builder(requireContext())
                .setTitle("Wybierz kategorię")
                .setSingleChoiceItems(categories.toTypedArray(), selectedIndex) { _, which ->
                    tempSelection = which
                }
                .setPositiveButton("Zatwierdź") { _, _ ->
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
                .setNegativeButton("Anuluj", null)
                .show()
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
