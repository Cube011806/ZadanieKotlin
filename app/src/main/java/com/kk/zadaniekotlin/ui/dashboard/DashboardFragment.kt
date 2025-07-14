package com.kk.zadaniekotlin.ui.dashboard

import Item
import ItemAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.kk.zadaniekotlin.R
import com.kk.zadaniekotlin.databinding.FragmentDashboardBinding

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)

        val items = listOf(
            Item("Kurtka MERIDA", "189,99 zł", R.drawable.kurtka1),
            Item("Bluza LUX", "119,99 zł", R.drawable.kurtka1),
            Item("Sneakersy ROKA", "229,99 zł", R.drawable.kurtka1)
        )

        binding.recyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.recyclerView.adapter = ItemAdapter(items)

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
