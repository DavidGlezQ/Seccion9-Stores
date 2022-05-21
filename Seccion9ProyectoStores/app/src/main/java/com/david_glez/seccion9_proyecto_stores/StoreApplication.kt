package com.david_glez.seccion9_proyecto_stores

import android.app.Application
import androidx.room.Room
import com.david_glez.seccion9_proyecto_stores.Database.StoreDataBase

class StoreApplication : Application() {

    //Singleton
    companion object{
        lateinit var dataBase: StoreDataBase
    }

    override fun onCreate() {
        super.onCreate()

        dataBase = Room.databaseBuilder(this,
            StoreDataBase::class.java, "StoreDataBase").build()
    }
}