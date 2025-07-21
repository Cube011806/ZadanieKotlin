import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.kk.zadaniekotlin.R
import com.kk.zadaniekotlin.model.Item

class ItemAdapter(
    private val items: MutableList<Item>,
    private val onAddToCart: (Item) -> Unit
) : RecyclerView.Adapter<ItemAdapter.ItemViewHolder>() {

    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val itemTitle: TextView = itemView.findViewById(R.id.itemTitle)
        val itemPrice: TextView = itemView.findViewById(R.id.itemPrice)
        val itemImage: ImageView = itemView.findViewById(R.id.itemImage)
        val itemButton: ImageButton = itemView.findViewById(R.id.itemButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_card, parent, false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = items[position]

        holder.itemTitle.text = item.title
        holder.itemPrice.text = item.price.toString() + " zł"

        Glide.with(holder.itemView.context)
            .load(item.imageUrl)
            .placeholder(ColorDrawable(Color.WHITE))
            .error(ColorDrawable(Color.RED))
            .into(holder.itemImage)
        val currentUser = FirebaseAuth.getInstance().currentUser
        holder.itemButton.visibility = if (currentUser == null) View.GONE else View.VISIBLE

        holder.itemButton.setImageResource(R.drawable.add_shopping_cart_24)

        holder.itemButton.setOnClickListener {
            holder.itemButton.setImageResource(R.drawable.shopping_cart)
            onAddToCart(item)
        }
    }

    override fun getItemCount(): Int = items.size

    fun updateData(newItems: List<Item>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }
}
