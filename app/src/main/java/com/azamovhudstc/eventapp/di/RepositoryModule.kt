package com.azamovhudstc.eventapp.di

import com.azamovhudstc.eventapp.repository.EventRepository
import com.azamovhudstc.eventapp.repository.impl.EventRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {

    @[Binds Singleton]
    fun bindAppRepository(impl: EventRepositoryImpl): EventRepository

}