package com.kk.zadaniekotlin.ui.basket

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.kk.zadaniekotlin.R
import com.kk.zadaniekotlin.model.Item

class BasketItemAdapter(
    private val items: MutableList<Item>,
    private val onRemoveFromCart: (Item) -> Unit
) : RecyclerView.Adapter<BasketItemAdapter.BasketViewHolder>() {

    inner class BasketViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val itemImage: ImageView = itemView.findViewById(R.id.basketItemImage)
        val itemTitle: TextView = itemView.findViewById(R.id.basketItemTitle)
        val itemPrice: TextView = itemView.findViewById(R.id.basketItemPrice)
        val removeButton: ImageButton = itemView.findViewById(R.id.removeButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BasketViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.basketitem_card, parent, false)
        return BasketViewHolder(view)
    }

    override fun onBindViewHolder(holder: BasketViewHolder, position: Int) {
        val item = items[position]
        holder.itemTitle.text = item.title
        holder.itemPrice.text = String.format("%.2f zł", item.price ?: 0.0)

        holder.removeButton.setOnClickListener {
            onRemoveFromCart(item)
        }

        Glide.with(holder.itemView.context)
            .load(item.imageUrl)
            .placeholder(ContextCompat.getDrawable(holder.itemView.context, R.color.white))
            .error(ContextCompat.getDrawable(holder.itemView.context, R.color.white))
            .into(holder.itemImage)
    }

    override fun getItemCount(): Int = items.size

    fun updateData(newItems: List<Item>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }
}
