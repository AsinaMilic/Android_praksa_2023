package com.example.feedcraft.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.feedcraft.ImageData
import kotlinx.coroutines.flow.map


//had to make it singleton somehow
class PreferenceDataStore private constructor(context: Context) {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
    var pref = context.dataStore

    companion object {
        private var INSTANCE: PreferenceDataStore? = null

        fun getInstance(context: Context): PreferenceDataStore {
            return INSTANCE ?: synchronized(this) {  //I don't know how exactly this works
                val instance = PreferenceDataStore(context.applicationContext)
                INSTANCE = instance
                instance
            }
        }

        val imageData = stringPreferencesKey("IMAGE_DATA")
    }

    suspend fun setGsonImageData(gsonImageData: String) {
        pref.edit {
            it[imageData] = gsonImageData
        }
    }

    fun getGsonImageData() = pref.data.map{
        it[imageData]
    }
}