package com.david_glez.seccion9_proyecto_stores.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.david_glez.seccion9_proyecto_stores.Entities.StoreEntity
import com.david_glez.seccion9_proyecto_stores.Interfaces.OnClickListener
import com.david_glez.seccion9_proyecto_stores.R
import com.david_glez.seccion9_proyecto_stores.databinding.ItemStoreBinding

class StoreAdapter(private var stores: MutableList<StoreEntity>, private var listener: OnClickListener):
    RecyclerView.Adapter<StoreAdapter.ViewHolder>(){

    private lateinit var mContext: Context // La 'm' se refiere a que es un miembro de la clase

    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        val binding = ItemStoreBinding.bind(view) // Creamos en binding para el item

        fun setListener(storeEntity: StoreEntity){
            with(binding.root){
                setOnClickListener { listener.onClick(storeEntity) }
                setOnLongClickListener{ listener.onDeleteStore(storeEntity)
                    true }
                setOnClickListener {
                    listener.onFavoriteStore(storeEntity)
                }
            }
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
            binding.cbFavorite.isChecked = store.isFavorite
        }
    }

    override fun getItemCount(): Int = stores.size

    fun add(storeEntity: StoreEntity) {
        stores.add(storeEntity)
        notifyDataSetChanged()
    }

    fun setStores(stores: MutableList<StoreEntity>){
        this.stores = stores
        notifyDataSetChanged()
    }

    fun update(storeEntity: StoreEntity){
        val index = stores.indexOf(storeEntity)
        if (index != -1){
            stores.set(index, storeEntity)
            notifyItemChanged(index)
        }
    }

    fun delete(storeEntity: StoreEntity){
        val index = stores.indexOf(storeEntity)
        if (index != -1){
            stores.removeAt(index)
            notifyItemRemoved(index)
        }
    }
}