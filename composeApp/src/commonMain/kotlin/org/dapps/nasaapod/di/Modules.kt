package org.dapps.nasaapod.di

import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import org.dapps.nasaapod.data.ApodRepositoryImpl
import org.dapps.nasaapod.data.NasaAPODDataSource
import org.dapps.nasaapod.data.NasaAPODDataSourceImpl
import org.dapps.nasaapod.data.createHttpClient
import org.dapps.nasaapod.data.createHttpClientEngine
import org.dapps.nasaapod.domain.ApodRepository
import org.dapps.nasaapod.domain.usecases.FetchApod
import org.dapps.nasaapod.presentation.ApodViewModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

expect val platformModule: Module

val sharedModule = module {
    singleOf(::createHttpClientEngine).bind<HttpClientEngine>()
    singleOf(::createHttpClient).bind<HttpClient>()
    singleOf(::NasaAPODDataSourceImpl).bind<NasaAPODDataSource>()
    singleOf(::ApodRepositoryImpl).bind<ApodRepository>()

    singleOf(::FetchApod)

    viewModelOf(::ApodViewModel)
}