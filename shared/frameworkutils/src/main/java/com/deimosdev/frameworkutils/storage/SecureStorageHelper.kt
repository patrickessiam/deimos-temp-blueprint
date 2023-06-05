package com.deimosdev.frameworkutils.storage

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys

open class SecureStorageHelper(context: Context) {

    private val fileName = "secure_storage"
    @RequiresApi(Build.VERSION_CODES.M)
    private val sharedPreferences = EncryptedSharedPreferences.create(
        fileName,
        MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC),
        context,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    @RequiresApi(Build.VERSION_CODES.M)
    fun saveString(key: String, value: String) {
        sharedPreferences.edit().putString(key, value).apply()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun retrieveString(key: String, defaultValue: String? = null): String? {
        return sharedPreferences.getString(key, defaultValue)
    }

    companion object {
        val TOKEN_KEY = "Access_Token"
    }
}
