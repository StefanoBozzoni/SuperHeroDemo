package com.example.superheroesdemo.data.setup

import com.example.superheroesdemo.BuildConfig
import okhttp3.Interceptor
import okhttp3.logging.HttpLoggingInterceptor
import java.nio.charset.StandardCharsets
import java.security.MessageDigest

object NetworkModule {

    fun createHttpLoggingInterceptor(): HttpLoggingInterceptor {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        return loggingInterceptor
    }

    fun basicHeaderInterceptor() = Interceptor {
        val apiKey = BuildConfig.MARVEL_API_KEY
        val timeStamp = (System.currentTimeMillis() / 1000).toString()
        val hash = getHashMd5(timeStamp, apiKey)

        val modifiedUrl = it.request().url
            .newBuilder()
            .addQueryParameter("ts",timeStamp)
            .addQueryParameter("apikey",apiKey)
            .addQueryParameter("hash",hash)
            .build()

        val request = it.request().newBuilder()
            .addHeader("Content-Type", "application/json")
            .url(modifiedUrl)
            .build()
        it.proceed(request)
    }

    private fun getHashMd5(timeStamp: String, publicKey:String):String {
            val privateKey = BuildConfig.MARVEL_PRIVATE_KEY //private key
            // Concatenate the timestamp, key1, and key2
            val dataToHash = "$timeStamp$privateKey$publicKey"

            val md = MessageDigest.getInstance("MD5")
            val md5Bytes = md.digest(dataToHash.toByteArray(StandardCharsets.UTF_8))

            // Convert the MD5 hash to a hexadecimal string
            val md5Hash = md5Bytes.joinToString("") { "%02x".format(it) }

        return md5Hash
    }

}