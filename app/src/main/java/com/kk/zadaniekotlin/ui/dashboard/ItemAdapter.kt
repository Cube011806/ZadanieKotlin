import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.kk.zadaniekotlin.R
import com.kk.zadaniekotlin.model.Item

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
        Glide.with(holder.itemView.context) //Wczytanie zdjęcia przedmiotu z pomocą Glide
            .load(item.imageUrl)
            .placeholder(ColorDrawable(Color.WHITE))
            .error(ColorDrawable(Color.RED))
            .into(holder.itemImage)
        holder.itemButton.setImageResource(R.drawable.add_shopping_cart_24)
        holder.itemButton.setOnClickListener {
            holder.itemButton.setImageResource(R.drawable.shopping_cart) //Zmiana ikony koszyka
            /*Toast.makeText(
                holder.itemView.context,
                "Kliknięto: ${item.title}",
                Toast.LENGTH_SHORT
            ).show()*/

        }



    }
    fun updateData(newItems: List<Item>) {
        (items as MutableList).apply {
            clear()
            addAll(newItems)
        }
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = items.size
}
