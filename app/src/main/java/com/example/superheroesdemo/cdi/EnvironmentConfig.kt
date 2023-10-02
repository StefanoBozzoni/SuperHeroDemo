package com.example.superheroesdemo.cdi

object EnvironmentConfig {
    const val BASE_DOMAIN = "gateway.marvel.com:443"
    const val BASE_URL = "https://$BASE_DOMAIN/"
    val allowedSSlFingerprints = emptyList<String>()
}