package com.david_glez.seccion9_proyecto_stores.Interfaces

import com.david_glez.seccion9_proyecto_stores.Entities.StoreEntity

interface OnClickListener {
    fun onClick(storeEntity: StoreEntity)
    fun onFavoriteStore(storeEntity: StoreEntity)
    fun onDeleteStore(storeEntity: StoreEntity)
}