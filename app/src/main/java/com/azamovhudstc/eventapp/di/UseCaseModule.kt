package com.azamovhudstc.eventapp.di

import com.azamovhudstc.eventapp.usecase.impl.EventsUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import com.azamovhudstc.eventapp.usecase.EventsUseCase
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
interface UseCaseModule {

    @[Binds Singleton]
    fun bindEventsUseCase(impl: EventsUseCaseImpl): EventsUseCase

}