package com.example.kotlintest.screens.ecg.model

import com.example.kotlintest.screens.ecg.EcgAction
import kotlinx.coroutines.channels.SendChannel
import serial.jni.NativeCallBack
import java.util.concurrent.ConcurrentLinkedQueue

// NativeMsgBridge.kt
class NativeMsgBridge(
    private val ecgQueue: ConcurrentLinkedQueue<Short>, // optional if you still buffer
    private val onWaveStable: ((Boolean) -> Unit)? = null,
    private val onAutoSave: (() -> Unit)? = null // if you later want to trigger save
) : NativeCallBack() {

    private var sendChannel: SendChannel<EcgAction> ?=null

    fun setEventsChannel(channel: SendChannel<EcgAction>) {
        this.sendChannel = channel
    }
    override fun callHRMsg(hr: Short) {
        sendChannel?.trySend(EcgAction.HeartRate(hr.toInt()))
    }

    override fun callLeadOffMsg(flagOff: String) {
        sendChannel?.trySend(EcgAction.LeadOff(flagOff))
    }

    override fun callProgressMsg(progress: Short) {
    }

    override fun callCaseStateMsg(state: Short) {
        // 0=start, else=end
    }

    override fun callHBSMsg(hbs: Short) {
        // hbs = 1 means a heartbeat detected
    }

    override fun callBatteryMsg(per: Short) {
    }

    override fun callCountDownMsg(per: Short) {
    }

    override fun callWaveColorMsg(flag: Boolean) {
        // original: set renderer to green if stable
        onWaveStable?.invoke(flag)
        if (flag) {
            // onAutoSave?.invoke() // wire this if you want auto-save later
        }
    }
    private val TAG = "NativeMsgBridge"
    override fun callEcgWaveDataMsg(wave: ShortArray) {
        // Original code offered samples[48..59]. Keep your vendor quirk if you must.
        val slice = if (wave.size >= 60) wave.copyOfRange(48, 60) else wave.copyOf()
        ecgQueue?.addAll(slice.toList())
//        Logger.i(TAG, "callEcgWaveDataMsg" + ecgQueue)
    }

    override fun callEcg18WaveDataMsg(wave: ShortArray) {
        val slice = if (wave.size >= 90) wave.copyOfRange(72, 90) else wave.copyOf()
        ecgQueue?.addAll(slice.toList())
//        Logger.i(TAG, "callEcg18WaveDataMsg" + ecgQueue)

    }

    override fun callEcg15WaveDataMsg(wave: ShortArray) {
        val slice = if (wave.size >= 75) wave.copyOfRange(60, 75) else wave.copyOf()
        ecgQueue?.addAll(slice.toList())
//        Logger.i(TAG, "callEcg15WaveDataMsg" + ecgQueue)

    }

    override fun callVcgWaveDataMsg(wave: ShortArray) {
        // no-op for now. Add a new EcgEvent if you actually need this.
    }

    override fun callVcgWaveRPosMsg(flag: IntArray) {
        // no-op. Same deal as above.
    }

    override fun callNibpStateMsg(flag: Byte, type: Byte) {
        super.callNibpStateMsg(flag, type)
    }

    override fun callNibpResultMsg(sys: Short, dia: Short, mea: Short, pr: Short, err: Byte) {

        // TODO Auto-generated method stub
        super.callNibpResultMsg(sys, dia, mea, pr, err)

        sendChannel?.trySend(EcgAction.NibpResult(sys.toInt(), dia.toInt(), mea.toInt(), pr.toInt(), err))

        // original had: data.BluNIBPConfirmCmd(); do that where you own `data`, not here.
    }

    override fun callSpO2ResultMsg(spo2: Short, pr: Short, state: Byte) {

        // TODO Auto-generated method stub
        super.callSpO2ResultMsg(spo2, pr, state)

    }

    override fun callNibpCuffMsg(val_: Short) {
        // add an event if you actually render cuff pressure
        // events.trySend(EcgEvent.NibpCuff(val_.toInt()))
    }

    override fun callQueryParamsMsg(index: Byte, val_: Byte) {
        super.callQueryParamsMsg(index, val_)
    }

    override fun callStepResetMsg() {
        super.callStepResetMsg()
    }

    override fun callStepDataMsg(steps: Int, turn: Short, percent: Short) {
        super.callStepDataMsg(steps, turn, percent)
    }
}