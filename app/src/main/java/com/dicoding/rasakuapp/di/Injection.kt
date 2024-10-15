package com.dicoding.rasakuapp.di

import com.dicoding.rasakuapp.data.RasakuRepository

object Injection {
    fun provideRepository(): RasakuRepository {
        return RasakuRepository.getInstance()
    }
}