package com.example.kotlintest.screens.ecg.model

import com.example.kotlintest.screens.ecg.views.ReviewWave
import java.util.concurrent.ConcurrentLinkedQueue
import javax.inject.Inject

interface ReviewWaveController {
    fun attach(view: ReviewWave)
    fun detach()
    fun setRenderColor()
    fun setEcgDataBuf()
    fun startRenderer()
    fun stopRender()

}

class ReviewWaveControllerImpl@Inject constructor(private val mEcgQueue: ConcurrentLinkedQueue<Short>) : ReviewWaveController {
    private var view: ReviewWave? = null

    override fun setEcgDataBuf() {
        view?.setEcgDataBuf(mEcgQueue)
    }
    override fun attach(view: ReviewWave) {
        this.view = view
    }

    override fun detach() {
        view = null
    }

    override fun setRenderColor() {
        view?.setRendererColor(0, 1.0f, 0, 0)
    }

    override fun startRenderer() {
        view?.startRenderer()
    }

    override fun stopRender() {
        view?.stopRenderer()
        if(mEcgQueue!= null&& mEcgQueue.isNotEmpty())
        mEcgQueue.clear()
        if(mEcgQueue!= null&& mEcgQueue.isNotEmpty())
         mEcgQueue.remove()
    }
}
