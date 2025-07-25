package com.kk.zadaniekotlin.ui.dashboard
import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.fragment.app.*
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.kk.zadaniekotlin.MyApplication
import com.kk.zadaniekotlin.SharedViewModel
import com.kk.zadaniekotlin.databinding.FragmentDashboardBinding
import com.kk.zadaniekotlin.ui.basket.BasketViewModel
import javax.inject.Inject

class DashboardFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!

    private val dashboardViewModel: DashboardViewModel by viewModels {
        DashboardViewModelFactory()
    }

    private val sharedViewModel: SharedViewModel by activityViewModels {
        viewModelFactory
    }

    private val basketViewModel: BasketViewModel by activityViewModels {
        viewModelFactory
    }

    private lateinit var adapter: ItemAdapter

    override fun onAttach(context: Context) {
        (requireActivity().application as MyApplication).appComponent.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = ItemAdapter(mutableListOf(), basketViewModel) { item ->
            basketViewModel.addItem(item)
        }

        binding.recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.recyclerView.adapter = adapter

        val catId = sharedViewModel.catId.value ?: 0
        val subCatId = sharedViewModel.subCatId.value ?: 0
        dashboardViewModel.loadItems(catId, subCatId)

        dashboardViewModel.uiState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is DashboardUiState.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.recyclerView.visibility = View.GONE
                    binding.emptyTextView.visibility = View.GONE
                }
                is DashboardUiState.Success -> {
                    binding.progressBar.visibility = View.GONE
                    binding.recyclerView.visibility = View.VISIBLE
                    binding.emptyTextView.visibility = View.GONE
                }
                is DashboardUiState.Empty -> {
                    binding.progressBar.visibility = View.GONE
                    binding.recyclerView.visibility = View.GONE
                    binding.emptyTextView.visibility = View.VISIBLE
                }
                is DashboardUiState.Error -> {
                    binding.progressBar.visibility = View.GONE
                    binding.recyclerView.visibility = View.VISIBLE
                    binding.emptyTextView.visibility = View.GONE
                }
            }
        }

        dashboardViewModel.items.observe(viewLifecycleOwner) {
            adapter.updateData(it)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
