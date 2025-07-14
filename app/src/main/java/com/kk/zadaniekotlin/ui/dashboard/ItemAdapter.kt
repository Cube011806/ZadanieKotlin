import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.kk.zadaniekotlin.R
import com.google.android.material.button.MaterialButton
class ItemAdapter(private val items: List<Item>) :
    RecyclerView.Adapter<ItemAdapter.ItemViewHolder>() {

    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val itemTitle: TextView = itemView.findViewById(R.id.itemTitle)
        val itemPrice: TextView = itemView.findViewById(R.id.itemPrice)
        val itemImage: ImageView = itemView.findViewById(R.id.itemImage)
        val itemButton: ImageButton = itemView.findViewById(R.id.itemButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_card, parent, false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = items[position]
        holder.itemTitle.text = item.title
        holder.itemPrice.text = item.price
        holder.itemImage.setImageResource(item.imageRes)
        holder.itemButton.setImageResource(R.drawable.add_shopping_cart_24)
        holder.itemButton.setOnClickListener {
            holder.itemButton.setImageResource(R.drawable.shopping_cart)
            Toast.makeText(
                holder.itemView.context,
                "Kliknięto: ${item.title}",
                Toast.LENGTH_SHORT
            ).show()

        }
    }

    override fun getItemCount(): Int = items.size
}
