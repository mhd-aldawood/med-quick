package com.example.kotlintest.screens.ecg.model

import com.example.kotlintest.screens.ecg.views.ReviewWaveNew
import java.util.concurrent.ConcurrentLinkedQueue
import javax.inject.Inject

interface ReviewWaveController {
    fun attach(view: ReviewWaveNew)

    //    fun attach(view: ReviewWave_)
    fun detach()
    fun setRenderColor()
    fun setEcgDataBuf()
    fun startRenderer()
    fun stopRender()

}

class ReviewWaveControllerImpl@Inject constructor(private val mEcgQueue: ConcurrentLinkedQueue<Short>) : ReviewWaveController {
    //    private var view: ReviewWave_? = null
    private var view: ReviewWaveNew? = null

    override fun setEcgDataBuf() {
        view?.setEcgDataBuf(mEcgQueue)
    }
    override fun attach(view: ReviewWaveNew) {
        this.view = view
    }
//    override fun attach(view: ReviewWave_) {
//        this.view = view
//    }

    override fun detach() {
        view = null
    }

    override fun setRenderColor() {
//        view?.setRendererColor(0, 1.0f, 0, 0)
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
