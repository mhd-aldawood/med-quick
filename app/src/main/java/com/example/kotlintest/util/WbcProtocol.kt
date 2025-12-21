package com.example.kotlintest.util

import com.konsung.lib.wbc.KSWbc
import kotlinx.serialization.Serializable

// ---------- Frame building ----------
//TODO move this file from here to appropriate file
private fun checksum(bytes: ByteArray): Byte {
    // Sum bytes[2]..bytes[8] (len to last parameter), & 0xFF
    var sum = 0
    for (i in 2..8) sum = (sum + (bytes[i].toInt() and 0xFF)) and 0xFF
    return sum.toByte()
}

private fun buildCommandFrame(operatorBytes: ByteArray, parameter: Byte): ByteArray {
    val frame = ByteArray(10)

    frame[0] = 0xA5.toByte()
    frame[1] = 0x55.toByte()
    frame[2] = 0x08.toByte()           // len – matches examples in PDF
    frame[3] = 0x00.toByte()           // machine code high
    frame[4] = 0x20.toByte()           // machine code low

    operatorBytes.copyInto(frame, 5)   // 3 bytes
    frame[8] = parameter

    frame[9] = checksum(frame)
    return frame
}

// Public commands
fun wbcHandshakeFrame(): ByteArray =
    buildCommandFrame(byteArrayOf(0x43, 0x4F, 0x4E), 0x54)    // "CON","T"

fun wbcGetResultsFrame(): ByteArray =
    buildCommandFrame(byteArrayOf(0x24, 0x01, 0x00), 0x00)

fun wbcClearDataFrame(): ByteArray =
    buildCommandFrame(byteArrayOf(0x21, 0x23, 0x00), 0x00)


// ---------- Parsing ----------

data class WbcFrame(
    val op1: Int,
    val op2: Int,
    val op3: Int,
    val payload: List<Byte>
)

// This assumes one full frame arrives in one notification.
// If you see partial frames in logs, you'll add buffering logic.
fun parseWbcFrame(bytes: ByteArray): WbcFrame {
    require(bytes.size >= 10) { "Too short" }

    // If not our custom header, reject gracefully
    if (!(bytes[0] == 0xA5.toByte() && bytes[1] == 0x55.toByte())) {
        throw IllegalArgumentException(
            "Bad header: ${"%02X".format(bytes[0])} ${"%02X".format(bytes[1])}"
        )
    }
    val len = bytes[2].toInt() and 0xFF
    // total frame size should be len + 3 (header + len)
    require(bytes.size == len + 3) {
        "Length mismatch: expected ${len + 3}, got ${bytes.size}"
    }

    val op1 = bytes[5].toInt() and 0xFF
    val op2 = bytes[6].toInt() and 0xFF
    val op3 = bytes[7].toInt() and 0xFF

    // Here we treat everything after index 8 (parameter and onward, without checksum) as payload.
    val payload = bytes

    return WbcFrame(op1, op2, op3, payload.toList())
}

@Serializable
data class CellResult(
    val sn: String,
    val deviceSn: String,
    val dateTime: String,

    val wbc: String,
    val lym: String,
    val mon: String,
    val neu: String,
    val eqs: String,
    val bas: String,

    val lymPer: String,
    val monPer: String,
    val neuPer: String,
    val eosPer: String,
    val basPer: String
)

fun parseCellResult(payload: List<Byte>): CellResult {
    val bean = KSWbc.parse(payload.toByteArray())//silly solution -_- WTF

    Logger.i("parseWbcFrame", bean.toString())
    val day = payload[22].toInt() and 0xFF
    val month = payload[23].toInt() and 0xFF
    val year = payload[24].toInt() and 0xFF  // probably 2000 + year
    val hour = payload[25].toInt() and 0xFF
    val min = payload[26].toInt() and 0xFF
    val sec = payload[27].toInt() and 0xFF

    val dateTime = "20%02d-%02d-%02d %02d:%02d:%02d"
        .format(year, month, day, hour, min, sec)
    return CellResult(
        sn = bean.sn,
        deviceSn = bean.deviceSn,
        wbc = bean.wbc,
        lym = bean.lym,
        mon = bean.mon,
        neu = bean.neu,
        eqs = bean.eos,
        bas = bean.bas,
        lymPer = bean.lymPer,// lym%
        monPer = bean.monPer,// mon%
        neuPer = bean.neuPer,// neu%
        eosPer = bean.eosPer,// eos%
        basPer = bean.basPer,// bas% ,
        dateTime = dateTime
    )
}
