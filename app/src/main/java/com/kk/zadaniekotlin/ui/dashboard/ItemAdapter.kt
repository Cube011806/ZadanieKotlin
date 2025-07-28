package com.kk.zadaniekotlin.ui.dashboard

import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toDrawable
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.kk.zadaniekotlin.NotificationService
import com.kk.zadaniekotlin.R
import com.kk.zadaniekotlin.model.Item
import com.kk.zadaniekotlin.ui.basket.BasketViewModel

class ItemAdapter(
    private val items: MutableList<Item>,
    private val viewModel: BasketViewModel,
    private val onAddToCart: (Item) -> Unit
) : RecyclerView.Adapter<ItemAdapter.ItemViewHolder>() {

    inner class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.itemTitle)
        val price: TextView = view.findViewById(R.id.itemPrice)
        val image: ImageView = view.findViewById(R.id.itemImage)
        val addButton: ImageButton = view.findViewById(R.id.itemButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_card, parent, false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = items[position]
        val context = holder.itemView.context
        val user = FirebaseAuth.getInstance().currentUser
        val isInCart = viewModel.cartItems.value?.contains(item) == true

        holder.title.text = item.title
        holder.price.text = context.getString(R.string.item_price, item.price)

        Glide.with(context)
            .load(item.imageUrl)
            .placeholder(Color.WHITE.toDrawable())
            .error(Color.RED.toDrawable())
            .into(holder.image)

        holder.addButton.visibility = if (user == null) View.GONE else View.VISIBLE
        holder.addButton.setImageResource(
            if (isInCart) R.drawable.shopping_cart else R.drawable.add_shopping_cart_24
        )

        holder.addButton.setOnClickListener {
            val isInCartNow = viewModel.cartItems.value?.contains(item) == true

            if (isInCartNow) {
                viewModel.removeItem(item)
                holder.addButton.setImageResource(R.drawable.add_shopping_cart_24)
            } else {
                onAddToCart(item)
                holder.addButton.setImageResource(R.drawable.shopping_cart)

                ContextCompat.startForegroundService(
                    context,
                    Intent(context, NotificationService::class.java)
                )
            }
        }

    }

    override fun getItemCount(): Int = items.size

    fun updateData(newItems: List<Item>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }
}
