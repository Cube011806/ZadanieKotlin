package com.kk.zadaniekotlin.ui.basket

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.kk.zadaniekotlin.databinding.FragmentBasketBinding
import com.kk.zadaniekotlin.model.Item
import BasketItemAdapter

class BasketFragment : Fragment() {

    private var _binding: FragmentBasketBinding? = null
    private val binding get() = _binding!!

    private val viewModel: BasketViewModel by activityViewModels()
    private lateinit var adapter: BasketItemAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentBasketBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        adapter = BasketItemAdapter(mutableListOf()) { item ->
            viewModel.removeItem(item)
            //adapter.removeItemFromList(item)

        }

        binding.recyclerView.layoutManager = GridLayoutManager(requireContext(), 1)
        binding.recyclerView.adapter = adapter

        viewModel.cartItems.observe(viewLifecycleOwner) { items ->
            adapter.updateData(items)
            binding.emptyTextView.visibility = if (items.isEmpty()) View.VISIBLE else View.GONE
            binding.recyclerView.visibility = if (items.isNotEmpty()) View.VISIBLE else View.GONE
        }

        viewModel.uiState.observe(viewLifecycleOwner) { state ->
            binding.progressBar.visibility = if (state is BasketUiState.Loading) View.VISIBLE else View.GONE
        }
        viewModel.cartSum.observe(viewLifecycleOwner) { sum ->
            binding.sumView.text = String.format("%.2f zł", sum)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
