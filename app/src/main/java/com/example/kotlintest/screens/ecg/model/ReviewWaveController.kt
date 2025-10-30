package com.example.kotlintest.screens.ecg.model

import com.example.kotlintest.screens.ecg.ReviewWave
import java.util.concurrent.ConcurrentLinkedQueue

interface ReviewWaveController {
    fun attach(view: ReviewWave)
    fun detach()
    fun setRenderColor()
    fun setEcgDataBuf(mEcgQueue:ConcurrentLinkedQueue<Short>)
    fun startRenderer()
    fun stopRender()

}

class ReviewWaveControllerImpl : ReviewWaveController {
    private var view: ReviewWave? = null
    private var mEcgQueue: ConcurrentLinkedQueue<Short>? = null

    override fun setEcgDataBuf(mEcgQueue: ConcurrentLinkedQueue<Short>) {
        view?.setEcgDataBuf(mEcgQueue)
        this.mEcgQueue=mEcgQueue
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
        this.mEcgQueue=null

    }
}
