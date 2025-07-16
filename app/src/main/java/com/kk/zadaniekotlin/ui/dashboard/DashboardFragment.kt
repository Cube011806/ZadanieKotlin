package com.kk.zadaniekotlin.ui.dashboard

import com.kk.zadaniekotlin.model.Item
import ItemAdapter
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
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
        val itemList = mutableListOf<Item>()
        adapter = ItemAdapter(itemList)
        binding.recyclerView.adapter = adapter
        loadItemsFromFirebase(adapter, itemList)

        return binding.root
    }

    override fun showItems(items: List<Item>) {
        adapter = ItemAdapter(items)
        binding.recyclerView.adapter = adapter
    }
    fun loadItemsFromFirebase(adapter: ItemAdapter, itemList: MutableList<Item>) {
        val database = FirebaseDatabase.getInstance()
        val itemsRef = database.getReference("items")

        itemsRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                itemList.clear()
                for (child in snapshot.children) {
                    val item = child.getValue(Item::class.java)
                    item?.let { itemList.add(it) }
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Firebase", "Błąd podczas ładowania danych: ${error.message}")
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
