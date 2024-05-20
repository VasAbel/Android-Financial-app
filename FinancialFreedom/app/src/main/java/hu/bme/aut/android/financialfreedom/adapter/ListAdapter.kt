package hu.bme.aut.android.financialfreedom.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.annotation.DrawableRes
import androidx.recyclerview.widget.RecyclerView
import hu.bme.aut.android.financialfreedom.R
import hu.bme.aut.android.financialfreedom.data.ListItem
import hu.bme.aut.android.financialfreedom.databinding.ItemListBinding

class ListAdapter(private val listener: OnItemDeleteListener) :
    RecyclerView.Adapter<ListAdapter.ListViewHolder>() {


    private val items = mutableListOf<ListItem>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ListViewHolder(
        ItemListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )


        override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
            val listItem = items[position]

            holder.binding.ivIcon.setImageResource(getImageResource(listItem.category))
            holder.binding.tvCategory.text = listItem.category.name
            holder.binding.tvPrice.text = "${listItem.price} Ft"

        }

        @DrawableRes()
        private fun getImageResource(category: ListItem.Category): Int {
            return when (category) {
                ListItem.Category.Food -> R.drawable.food
                ListItem.Category.Transportation -> R.drawable.transp
                ListItem.Category.Household -> R.drawable.house
                ListItem.Category.Tourism -> R.drawable.tourism
                ListItem.Category.Entertainment -> R.drawable.entertainment
                ListItem.Category.Education -> R.drawable.book
                ListItem.Category.Clothing -> R.drawable.tshirt
                ListItem.Category.Other -> R.drawable.other
                ListItem.Category.Health -> R.drawable.heart
            }
        }


    override fun getItemCount(): Int = items.size



    inner class ListViewHolder(val binding: ItemListBinding) : RecyclerView.ViewHolder(binding.root){
        private val deleteButton: ImageButton = binding.ibRemove

        init {
            deleteButton.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val itemToDelete = items[position]
                    listener.onItemDelete(itemToDelete)
                }
            }
        }
    }

    fun addItem(item: ListItem) {
        items.add(item)
        notifyItemInserted(items.size - 1)
    }

    fun update(listItems: List<ListItem>) {
        items.clear()
        items.addAll(listItems)
        notifyItemInserted(items.size - 1)
    }

    fun clearItems(){
        items.clear()
        notifyDataSetChanged()
    }

    interface OnItemDeleteListener {
        fun onItemDelete(item: ListItem)
    }

    fun removeItem(item: ListItem) {
        val position = items.indexOf(item)
        if (position != -1) {
            items.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    fun getAllItems(): MutableList<ListItem>{return items}


}