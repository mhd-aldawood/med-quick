package com.example.kotlintest.di

import android.content.Context
import android.graphics.PixelFormat
import android.util.AttributeSet
import com.example.kotlintest.core.PermissionManager
import com.example.kotlintest.screens.ecg.model.ReviewWaveController
import com.example.kotlintest.screens.ecg.model.ReviewWaveControllerImpl
import com.example.kotlintest.screens.ecg.views.ReviewWaveNew
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ActivityContext
import java.util.concurrent.ConcurrentLinkedQueue

@dagger.Module
@InstallIn(ActivityComponent::class)//TODO check for ViewmodelComponent
object SdkModuleActivity {
    @Provides
    fun provideReviewWaveFactory(
        @ActivityContext context: Context
    ): ReviewWaveFactory = object : ReviewWaveFactory {
        override fun create(attrs: AttributeSet?): ReviewWaveNew {
            return ReviewWaveNew(context, attrs).apply {
                setZOrderOnTop(true)
                holder.setFormat(PixelFormat.TRANSLUCENT)
            }
        }
//        override fun create(attrs: AttributeSet?): ReviewWave_ {
//            return ReviewWave_(context, attrs).apply {
//                setZOrderOnTop(true)
//                holder.setFormat(PixelFormat.TRANSLUCENT)
//            }
//        }
    }
    @Provides
    fun provideReviewWaveController(mEcgQueue: ConcurrentLinkedQueue<Short>): ReviewWaveController {
        return ReviewWaveControllerImpl(mEcgQueue)
    }
    @Provides
    fun providePermissionManager(@ActivityContext context: Context): PermissionManager =PermissionManager(context)
}

interface ReviewWaveFactory {
    fun create(attrs: AttributeSet? = null): ReviewWaveNew
//    fun create(attrs: AttributeSet? = null): ReviewWave_
}