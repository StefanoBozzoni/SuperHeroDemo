package com.example.superheroesdemo.cdi

import com.example.superheroesdemo.data.repository.IRepository
import com.example.superheroesdemo.data.repository.Repository
import org.koin.core.qualifier.named
import org.koin.dsl.module

val repositoryModule = module {
    single<IRepository> { Repository(
        remoteDataSource = get(named("REMOTE_DATA_SOURCE")),
        localDataSource = get(named("LOCAL_DATA_SOURCE")))
    }
}