package com.kk.zadaniekotlin.ui.categories

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.graphics.drawable.toDrawable
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kk.zadaniekotlin.R
import com.kk.zadaniekotlin.SharedViewModel
import com.kk.zadaniekotlin.model.Category
import androidx.navigation.findNavController

class CategoryAdapter(
    private val sharedViewModel: SharedViewModel
) : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    private val categories = mutableListOf<Category>()

    inner class CategoryViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val image: ImageView = view.findViewById(R.id.itemImage)
        val title: TextView = view.findViewById(R.id.itemTitle)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.category_card, parent, false)
        return CategoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val category = categories[position]
        holder.title.text = category.title

        Glide.with(holder.itemView.context)
            .load(category.imageUrl)
            .placeholder(Color.LTGRAY.toDrawable())
            .error(Color.RED.toDrawable())
            .into(holder.image)

        holder.itemView.setOnClickListener {
            sharedViewModel.setSubCatId(category.subCatId)
            val action = CategoryFragmentDirections
                .actionCategoryFragmentToDashboardFragment()
            holder.itemView.findNavController().navigate(action)
        }
    }

    override fun getItemCount(): Int = categories.size

    fun updateData(newCategories: List<Category>) {
        categories.clear()
        categories.addAll(newCategories)
        notifyDataSetChanged()
    }
}
