package com.david_glez.seccion9_proyecto_stores


import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.david_glez.seccion9_proyecto_stores.Adapters.StoreAdapter
import com.david_glez.seccion9_proyecto_stores.Entities.StoreEntity
import com.david_glez.seccion9_proyecto_stores.Fragments.EditStoreFragment
import com.david_glez.seccion9_proyecto_stores.Interfaces.MainAux
import com.david_glez.seccion9_proyecto_stores.Interfaces.OnClickListener
import com.david_glez.seccion9_proyecto_stores.databinding.ActivityMainBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.uiThread

class MainActivity : AppCompatActivity(), OnClickListener, MainAux {

    //Clase 132
    private lateinit var mBinding: ActivityMainBinding
    private lateinit var mAdapter: StoreAdapter
    private lateinit var mGridLayout: GridLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        /*mBinding.btnSave.setOnClickListener {
            val store = StoreEntity(name = mBinding.etName.text.toString().trim()) // Argumentos nombrados

            Thread{
                StoreApplication.dataBase.storeDao().addStore(store)
            }.start()

            mAdapter.add(store)
        }*/

        mBinding.fabAddStore.setOnClickListener {
            launchEditFragment()
        }

        setupRecyclerView()
    }

    private fun launchEditFragment(args: Bundle? = null) {
        val fragment = EditStoreFragment()
        if (args != null) fragment.arguments = args

        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()

        fragmentTransaction.add(R.id.containerMain, fragment)
        fragmentTransaction.addToBackStack(null) // Destruir el fragment del stack
        fragmentTransaction.commit()

        hideFab()
        //mBinding.fabAddStore.hide()
    }

    private fun setupRecyclerView() {
        mAdapter = StoreAdapter(mutableListOf(), this)
        mGridLayout = GridLayoutManager(this, resources.getInteger(R.integer.main_columns))
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
    * OnClickListener Interface
    * */
    override fun onClick(storeId: Long) {
        val args = Bundle()
        args.putLong(getString(R.string.arg_id), storeId)

        launchEditFragment(args)
    }

    override fun onFavoriteStore(storeEntity: StoreEntity) {
        storeEntity.isFavorite = !storeEntity.isFavorite
        doAsync {
            StoreApplication.dataBase.storeDao().updateStore(storeEntity)
            uiThread {
               updateStore(storeEntity)
            }
        }
    }

    override fun onDeleteStore(storeEntity: StoreEntity) {
        val items = resources.getStringArray(R.array.array_option_item)
        MaterialAlertDialogBuilder(this)
            .setTitle(R.string.dialog_options_title)
            .setItems(items) { _, i ->
                when(i){
                    0 -> confirmDelete(storeEntity)
                    1 -> dial(storeEntity.phone)
                    2 -> goToWebSIte(storeEntity.webSite)
                }
            }.show()
    }

    private fun confirmDelete(storeEntity: StoreEntity){
        MaterialAlertDialogBuilder(this)
            .setTitle(R.string.dilaog_delete_title)
            .setPositiveButton(R.string.dialog_delete_confirm)  { _, _ ->
                doAsync{
                    StoreApplication.dataBase.storeDao().deleteStore(storeEntity)
                    uiThread {
                        mAdapter.delete(storeEntity)
                    }
                }
            }
            .setNegativeButton(R.string.dialog_delete_cancel, null).show()
    }

    private fun dial(phone: String){
        val callIntent = Intent().apply {
            action = Intent.ACTION_DIAL
            data = Uri.parse("tel: $phone")
        }
        //A partir del API 30 es necesario agregar un queri en el manifest, por seguridad.
        startIntent(callIntent)
    }

    private fun goToWebSIte(webSite: String) {
        if (webSite.isEmpty()){
            Toast.makeText(this, R.string.main_error_no_website, Toast.LENGTH_SHORT).show()
        } else {
            val webSiteIntent = Intent().apply {
                action = Intent.ACTION_VIEW
                data = Uri.parse(webSite)
            }
            startIntent(webSiteIntent)
        }
    }

    private fun startIntent(intent: Intent){
        if (intent.resolveActivity(packageManager) != null) startActivity(intent)
        else Toast.makeText(this, R.string.main_error_no_result, Toast.LENGTH_SHORT).show()
    }

    /*
    * MainAux Interface
    * */
    override fun hideFab(isVisible: Boolean) {
        if (isVisible) mBinding.fabAddStore.show() else mBinding.fabAddStore.hide()
    }

    override fun addStore(storeEntity: StoreEntity) {
        mAdapter.add(storeEntity)
    }

    override fun updateStore(storeEntity: StoreEntity) {
        mAdapter.update(storeEntity)
    }

}