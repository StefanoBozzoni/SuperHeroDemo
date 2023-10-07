package com.example.superheroesdemo.cdi

import com.example.superheroesdemo.cdi.EnvironmentConfig.BASE_DOMAIN
import com.example.superheroesdemo.cdi.EnvironmentConfig.allowedSSlFingerprints
import com.example.superheroesdemo.data.repository.IRepository
import com.example.superheroesdemo.data.repository.Repository
import com.example.superheroesdemo.setup.AppServiceFactory
import com.example.superheroesdemo.setup.HttpClientFactory
import com.example.superheroesdemo.setup.ServiceFactory
import org.koin.core.qualifier.named
import org.koin.dsl.module

val remoteModule = module {
    single(named("HTTP_CLIENT")) { HttpClientFactory(BASE_DOMAIN, allowedSSlFingerprints) }
    single(named("SERVICE_FACTORY")) { ServiceFactory(EnvironmentConfig.BASE_URL) }
    single(named("APP_SERVICE")) {
        AppServiceFactory(get(named("HTTP_CLIENT"))).getAppService(get(named("SERVICE_FACTORY")))
    }
    single<IRepository> { Repository(
        remoteDataSource = get(named("REMOTE_DATA_SOURCE")),
        localDataSource = get(named("LOCAL_DATA_SOURCE")))
    }
}