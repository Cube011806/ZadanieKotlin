package com.kk.zadaniekotlin.ui.basket

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kk.zadaniekotlin.R
import com.kk.zadaniekotlin.model.Item

class BasketItemAdapter(
    private val onRemoveFromCart: (Item) -> Unit
) : ListAdapter<Item, BasketItemAdapter.BasketViewHolder>(ItemDiffCallback()) {

    inner class BasketViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val itemImage: ImageView = itemView.findViewById(R.id.basketItemImage)
        val itemTitle: TextView = itemView.findViewById(R.id.basketItemTitle)
        val itemPrice: TextView = itemView.findViewById(R.id.basketItemPrice)
        val removeButton: ImageButton = itemView.findViewById(R.id.removeButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BasketViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.basketitem_card, parent, false)
        return BasketViewHolder(view)
    }

    override fun onBindViewHolder(holder: BasketViewHolder, position: Int) {
        val item = getItem(position)

        holder.itemTitle.text = item.title
        holder.itemPrice.text = holder.itemView.context.getString(R.string.item_price, item.price)

        holder.removeButton.setOnClickListener {
            onRemoveFromCart(item)
        }

        Glide.with(holder.itemView.context)
            .load(item.imageUrl)
            .placeholder(ContextCompat.getDrawable(holder.itemView.context, R.color.white))
            .error(ContextCompat.getDrawable(holder.itemView.context, R.color.white))
            .into(holder.itemImage)
    }
}

class ItemDiffCallback : DiffUtil.ItemCallback<Item>() {
    override fun areItemsTheSame(oldItem: Item, newItem: Item): Boolean =
        oldItem.catId == newItem.catId

    override fun areContentsTheSame(oldItem: Item, newItem: Item): Boolean =
        oldItem == newItem
}
