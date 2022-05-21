package com.david_glez.seccion9_proyecto_stores

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import com.david_glez.seccion9_proyecto_stores.Adapters.StoreAdapter
import com.david_glez.seccion9_proyecto_stores.Entities.StoreEntity
import com.david_glez.seccion9_proyecto_stores.Interfaces.OnClickListener
import com.david_glez.seccion9_proyecto_stores.databinding.ActivityMainBinding
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class MainActivity : AppCompatActivity(), OnClickListener {

    private lateinit var mBinding: ActivityMainBinding
    private lateinit var mAdapter: StoreAdapter
    private lateinit var mGridLayout: GridLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        mBinding.btnSave.setOnClickListener {
            val store = StoreEntity(name = mBinding.etName.text.toString().trim()) // Argumentos nombrados

            Thread{
                StoreApplication.dataBase.storeDao().addStore(store)
            }.start()

            mAdapter.add(store)
        }

        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        mAdapter = StoreAdapter(mutableListOf(), this)
        mGridLayout = GridLayoutManager(this, 2)
        getStores()

        mBinding.recyclerView.apply {
            setHasFixedSize(true) // Optimizacion de recursos
            layoutManager = mGridLayout
            adapter = mAdapter
        }
    }

    private fun getStores(){ // Ejecutar de forma asincrona en un hilo secundario
        doAsync {
            val stores = StoreApplication.dataBase.storeDao().getAllStores()
            uiThread {
                mAdapter.setStores(stores)
            }
        }
    }

    /*
    * OnClickListener
    * */
    override fun onClick(storeEntity: StoreEntity) {

    }

    override fun onFavoriteStore(storeEntity: StoreEntity) {
        storeEntity.isFavorite = !storeEntity.isFavorite
        doAsync {
             StoreApplication.dataBase.storeDao().updateStore(storeEntity)
            uiThread {
                mAdapter.update(storeEntity)
            }
        }
    }

    override fun onDeleteStore(storeEntity: StoreEntity) {
        doAsync{
            StoreApplication.dataBase.storeDao().deleteStore(storeEntity)
            uiThread {
                mAdapter.delete(storeEntity)
            }
        }
    }

}