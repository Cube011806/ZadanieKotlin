package com.kk.zadaniekotlin.ui.dashboard

import SharedViewModel
import com.kk.zadaniekotlin.model.Item
import ItemAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.kk.zadaniekotlin.databinding.FragmentDashboardBinding

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!

    private val viewModel: DashboardViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private lateinit var adapter: ItemAdapter

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

        adapter = ItemAdapter(mutableListOf())
        binding.recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.recyclerView.adapter = adapter

        val catId = sharedViewModel.catId.value ?: return
        val subCatId = sharedViewModel.subCatId.value ?: return
        viewModel.loadItems(catId, subCatId)
        


        viewModel.items.observe(viewLifecycleOwner) { itemList ->
            adapter.updateData(itemList)
            if (itemList.isEmpty()) {
                Toast.makeText(requireContext(), "Brak produktów w tej kategorii", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
