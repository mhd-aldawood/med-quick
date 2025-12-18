package com.example.kotlintest.di

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class TonometerQualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class StethoScopeQualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class PulseOximeterQualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class ThermometerQualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class BloodAnalyzerQualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class UrineAnalyzerQualifier