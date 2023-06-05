package com.deimosdev.frameworkutils.encryption

import android.util.Base64
import com.deimosdev.frameworkutils.pref.CacheStore
import java.security.Key
import javax.crypto.Cipher
import javax.crypto.spec.GCMParameterSpec

class EncryptionDecryptionUtils{

   suspend fun encrypt(key: String, value: String, secreteKey: Key, transformation:String, cacheStore: CacheStore): String {
        val cipher = Cipher.getInstance(transformation)
        cipher.init(Cipher.ENCRYPT_MODE, secreteKey)
        val iv = cipher.iv
        val encrypted = cipher.doFinal(value.toByteArray(Charsets.UTF_8))
        val encoded = Base64.encodeToString(encrypted, Base64.DEFAULT)
       cacheStore.write(key + "_IV",Base64.encodeToString(iv, Base64.DEFAULT))
       return encoded
    }

    suspend fun decrypt(key: String, secreteKey: Key, transformation:String, cacheStore: CacheStore, length:Int = 128): String? {
        val encoded = cacheStore.read(key, "")
        val encrypted = Base64.decode(encoded, Base64.DEFAULT)
        val iv = Base64.decode(cacheStore.read(key + "_IV", ""), Base64.DEFAULT)
        val cipher = Cipher.getInstance(transformation)
        val spec = GCMParameterSpec(length, iv)
        cipher.init(Cipher.DECRYPT_MODE, secreteKey, spec)
        val decrypted = cipher.doFinal(encrypted)
        return  String(decrypted, Charsets.UTF_8)
    }

}