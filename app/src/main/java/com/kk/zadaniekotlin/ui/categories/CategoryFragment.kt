package com.kk.zadaniekotlin.ui.categories

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
import com.kk.zadaniekotlin.databinding.FragmentCategoryBinding
import com.kk.zadaniekotlin.model.Category

class CategoryFragment : Fragment() {

    private var _binding: FragmentCategoryBinding? = null
    private val binding get() = _binding!!

    private val categoryList = mutableListOf<Category>()
    private lateinit var adapter: CategoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCategoryBinding.inflate(inflater, container, false)

        adapter = CategoryAdapter(categoryList)
        binding.recyclerView2.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.recyclerView2.adapter = adapter
        binding.button.setOnClickListener {
            val database = FirebaseDatabase.getInstance()
            val categoryRef = database.getReference("FemCategories")

            val categoryList = listOf(
                Category(
                    title = "odzież wierzchnia",
                    imageUrl = "https://img.kwcdn.com/product/fancy/55be9495-38b4-467a-8087-6ea4d4af55fd.jpg?imageMogr2/auto-orient%7CimageView2/2/w/800/q/70/format/webp"
                ),
                Category(
                    title = "odzież sportowa",
                    imageUrl = "https://m.media-amazon.com/images/I/613HQTj3j8L._UF1000,1000_QL80_.jpg"
                ),
                Category(
                    title = "swetry, swetry rozpinane",
                    imageUrl = "https://m.media-amazon.com/images/I/51T+kGq4LOL._SY1000_.jpg"
                ),
                Category(
                    title = "jeansy, spodnie, szorty",
                    imageUrl = "https://cp.bigstar.pl/files/sc_staging_images/product/normal_img_600392.webp"
                ),
                Category(
                    title = "sukienki, spódnice",
                    imageUrl = "https://sarex-moda.pl/hpeciai/bc5e86c9d304ccfd22488f912fbedcc7/pol_pl_Sukienka-Victoria-czarna-74_1.jpg"
                )
            )

            categoryList.forEach { category ->
                val key = categoryRef.push().key
                key?.let {
                    categoryRef.child(it).setValue(category)
                }
            }

            Log.d("Firebase", "Wstawiono ${categoryList.size} kategorii do bazy 🔥")
        }

        loadCategoriesFromFirebase()

        return binding.root
    }

    private fun loadCategoriesFromFirebase() {
        val database = FirebaseDatabase.getInstance()
        val categoryRef = database.getReference("categories")

        categoryRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                categoryList.clear()
                for (child in snapshot.children) {
                    val category = child.getValue(Category::class.java)
                    category?.let { categoryList.add(it) }
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Firebase", "Błąd ładowania kategorii: ${error.message}")
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
