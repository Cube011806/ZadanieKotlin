package com.kk.zadaniekotlin.ui.categories

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
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
        val args = CategoryFragmentArgs.fromBundle(requireArguments())
        var categoryId = args.categoryId
        binding.recyclerView2.layoutManager = GridLayoutManager(requireContext(), 3)
        binding.recyclerView2.adapter = adapter

      val categoryIdToName = mapOf(
            1 to "Kobiety",
            2 to "Mężczyźni",
            3 to "Niemowlak",
            4 to "Dziewczynka",
            5 to "Chłopiec",
            6 to "Wszystkie"
        )

        var currentSelection = categoryIdToName[categoryId] ?: "Wszystkie"
        binding.button.setOnClickListener {
            val categoryMap = mapOf(
                "Kobiety" to 1,
                "Mężczyźni" to 2,
                "Niemowlak" to 3,
                "Dziewczynka" to 4,
                "Chłopiec" to 5,
                "Wszystkie" to 6
            )
            val categories = listOf("Kobiety", "Mężczyźni", "Niemowlak", "Dziewczynka", "Chłopiec", "Wszystkie")
                val selectedIndex = categories.indexOf(currentSelection)
                var tempSelection = selectedIndex
                AlertDialog.Builder(requireContext())
                    .setTitle("Wybierz kategorię")
                    .setSingleChoiceItems(categories.toTypedArray(), selectedIndex) { _, which ->
                        tempSelection = which
                    }
                    .setPositiveButton("Zatwierdź") { _, _ ->
                        val selectedName = categories[tempSelection]
                        currentSelection = selectedName

                        val categoryId = categoryMap[selectedName] ?: return@setPositiveButton
                        loadCategoriesFromFirebase(categoryId)
                    }
                    .show()
            }
            /*
            val database = FirebaseDatabase.getInstance()
            val categoryRef = database.getReference("BabyCategories")
            val categoryList = listOf(
                Category(
                    title = "odzież wierzchnia",
                    imageUrl = "https://fazymazy.com/userdata/public/assets/fotki%20do%20BLOGA/ubrania%20dla%20niemowl%C4%85t%20ch%C5%82opc%C3%B3w.png"
                ),
                Category(
                    title = "odzież elegancka",
                    imageUrl = "https://www.lilen.store/wp-content/uploads/2023/10/komplet-na-chrzest-dla-chlopca-lilen-scaled.jpg"
                ),
                Category(
                    title = "swetry, swetry rozpinane",
                    imageUrl = "https://gfx.51015kids.pl/pub/products/680/24680/900x1327/pdp-6c4701_6d470b.jpg"
                ),
                Category(
                    title = "jeansy, spodnie, szorty",
                    imageUrl = "https://i.etsystatic.com/7818429/r/il/3d809f/2237004274/il_570xN.2237004274_jwsd.jpg"
                ),
                Category(
                    title = "Placeholder",
                    imageUrl = "Test test"
                )
            )
            categoryList.forEach { category ->
                val key = categoryRef.push().key
                key?.let {
                    categoryRef.child(it).setValue(category)
                }
            }
            Log.d("Firebase", "Wstawiono ${categoryList.size} kategorii do bazy")*/
        //}

        loadCategoriesFromFirebase(categoryId)
        return binding.root
    }

    private fun loadCategoriesFromFirebase(categoryId: Int) {
        val database = FirebaseDatabase.getInstance()
        var categoryRef: DatabaseReference
        when (categoryId) {
            1 -> categoryRef = database.getReference("FemCategories")
            2 -> categoryRef = database.getReference("MaleCategories")
            3 -> categoryRef = database.getReference("BabyCategories")
            4 -> categoryRef = database.getReference("GirlCategories")
            5 -> categoryRef = database.getReference("BoyCategories")
            else -> categoryRef = database.getReference("categories")
        }

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
