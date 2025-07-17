package com.kk.zadaniekotlin.ui.categories

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.Navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kk.zadaniekotlin.model.Category
import com.kk.zadaniekotlin.R
class CategoryAdapter(private val categories: List<Category>) :
    RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    inner class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image: ImageView = itemView.findViewById(R.id.itemImage)
        val title: TextView = itemView.findViewById(R.id.itemTitle)
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
            .placeholder(ColorDrawable(Color.LTGRAY))
            .error(ColorDrawable(Color.RED))
            .into(holder.image)

        holder.itemView.setOnClickListener {
            val navController = findNavController(holder.itemView)
            val action = CategoryFragmentDirections
                .actionCategoryFragmentToDashboardFragment(category.catId)
            navController.navigate(action)
        }

    }

    override fun getItemCount(): Int = categories.size
}
