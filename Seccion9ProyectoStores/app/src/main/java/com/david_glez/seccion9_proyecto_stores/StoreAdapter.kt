package com.david_glez.seccion9_proyecto_stores

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.david_glez.seccion9_proyecto_stores.databinding.ItemStoreBinding

class StoreAdapter(private var stores: MutableList<Store>, private var listener: OnClickListener):
    RecyclerView.Adapter<StoreAdapter.ViewHolder>(){

    private lateinit var mContext: Context // La 'm' se refiere a que es un miembro de la clase

    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        val binding = ItemStoreBinding.bind(view) // Creamos en binding para el item

        fun setListener(store: Store){
            binding.root.setOnClickListener { listener.onClick(store) }
        }
    }

    //Methods
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        mContext = parent.context
        val view = LayoutInflater.from(mContext).inflate(R.layout.item_store, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val store = stores.get(position)

        with(holder){
            setListener(store)

            binding.tvName.text = store.name
        }
    }

    override fun getItemCount(): Int = stores.size
    fun add(store: Store) {
        stores.add(store)
        notifyDataSetChanged()
    }
}