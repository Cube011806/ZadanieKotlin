package com.kk.zadaniekotlin.ui.dashboard

import com.kk.zadaniekotlin.model.Item
import ItemAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.kk.zadaniekotlin.R
import com.kk.zadaniekotlin.databinding.FragmentDashboardBinding
import com.kk.zadaniekotlin.model.ItemModelImpl
import com.kk.zadaniekotlin.presenter.ItemPresenter
import com.kk.zadaniekotlin.view.ItemView

class DashboardFragment : Fragment(), ItemView {

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!

    private lateinit var presenter: ItemPresenter
    private lateinit var adapter: ItemAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)

        presenter = ItemPresenter(this, ItemModelImpl())
        binding.recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)

        presenter.loadItems()

        return binding.root
    }

    override fun showItems(items: List<Item>) {
        adapter = ItemAdapter(items)
        binding.recyclerView.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
