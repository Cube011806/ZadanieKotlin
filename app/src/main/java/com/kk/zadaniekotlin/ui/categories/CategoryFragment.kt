package com.kk.zadaniekotlin.ui.categories

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.kk.zadaniekotlin.MyApplication
import com.kk.zadaniekotlin.R
import com.kk.zadaniekotlin.SharedViewModel
import com.kk.zadaniekotlin.databinding.FragmentCategoryBinding
import javax.inject.Inject

class CategoryFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private var _binding: FragmentCategoryBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CategoryViewModel by viewModels {
        CategoryViewModelFactory()
    }
    private val sharedViewModel: SharedViewModel by activityViewModels {
        viewModelFactory
    }

    private lateinit var adapter: CategoryAdapter

    override fun onAttach(context: Context) {
        (requireActivity().application as MyApplication).appComponent.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentCategoryBinding.inflate(inflater, container, false)

        adapter = CategoryAdapter(sharedViewModel)
        binding.recyclerView2.layoutManager = GridLayoutManager(requireContext(), 3)
        binding.recyclerView2.adapter = adapter

        val args = CategoryFragmentArgs.fromBundle(requireArguments())
        val initialId = args.categoryId
        val initialName = viewModel.categoryIdToName[initialId] ?: "Wszystkie"

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

        viewModel.navigateToDashboard.observe(viewLifecycleOwner, Observer { navigate ->
            navigate?.let {
                if (it) findNavController().navigate(R.id.navigation_dashboard)
                viewModel.resetNavigationFlag()
            }
        })

        binding.button.setOnClickListener {
            val categories = viewModel.categoryNameToId.keys.toList()
            val selectedIndex = categories.indexOf(viewModel.getCurrentCategoryName() ?: "Wszystkie")
            var tempSelection = selectedIndex

            AlertDialog.Builder(requireContext())
                .setTitle(getString(R.string.filter_chooseCat))
                .setSingleChoiceItems(categories.toTypedArray(), selectedIndex) { _, which ->
                    tempSelection = which
                }
                .setPositiveButton(getString(R.string.filter_confirm)) { _, _ ->
                    val selectedName = categories[tempSelection]
                    viewModel.onCategorySelected(selectedName, sharedViewModel)
                }
                .setNegativeButton(getString(R.string.filter_cancel), null)
                .show()
        }
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
