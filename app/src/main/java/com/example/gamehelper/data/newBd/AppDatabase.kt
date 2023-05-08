package com.example.gamehelper.data.newBd

import android.app.Application
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

abstract class AppDatabase {

    abstract fun gameHelperFire(): GameHelperFireBase

    companion object {

        private var INSTANCE: DatabaseReference? = null
        private val LOCK = Any()

        fun getInstance(application: Application): DatabaseReference {
            INSTANCE?.let {
                return it
            }
            synchronized(LOCK) {
                INSTANCE?.let {
                    return it
                }
                val db: DatabaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://gamehelper-af012-default-rtdb.firebaseio.com/")
                INSTANCE = db
                return db
            }
        }
    }
}