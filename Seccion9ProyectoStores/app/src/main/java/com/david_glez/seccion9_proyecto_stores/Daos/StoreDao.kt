package com.david_glez.seccion9_proyecto_stores.Daos

import androidx.room.*
import com.david_glez.seccion9_proyecto_stores.Entities.StoreEntity

@Dao
interface StoreDao {
    @Query("SELECT * FROM StoreEntity")
    fun getAllStores(): MutableList<StoreEntity>

    @Insert
    fun addStore(storeEntity: StoreEntity)

    @Update
    fun updateStore(storeEntity: StoreEntity)

    @Delete
    fun deleteStore(storeEntity: StoreEntity)
}