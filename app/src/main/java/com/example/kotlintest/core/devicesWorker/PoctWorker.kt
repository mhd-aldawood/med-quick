package com.example.kotlintest.core.devicesWorker

import com.example.kotlintest.screens.poct.models.DeviceResult
import com.example.kotlintest.util.Logger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.ServerSocket
import java.net.Socket
import javax.inject.Inject

class PoctWorker @Inject constructor() {

    suspend fun receiveHl7FromDevice(
        host: String,
        port: Int
    ): String? = withContext(Dispatchers.IO) {

        val START_BLOCK = 0x0B.toChar()
        val END_BLOCK = 0x1C.toChar()
        val CARRIAGE_RETURN = '\r' // as per your spec
//        val CARRIAGE_RETURN = '\n' // as per your spec

        var hl7Message: String? = null

        try {
            Socket(host, port).use { socket ->
                val reader = BufferedReader(
                    InputStreamReader(socket.getInputStream(), Charsets.UTF_8)
                )

                val sb = StringBuilder()
                var insideBlock = false
                var seenEndBlock = false
                var ch: Int

                while (reader.read().also { ch = it } != -1) {
                    val c = ch.toChar()

                    // Wait until we see the start-of-block
                    if (!insideBlock) {
                        if (c == START_BLOCK) {
                            insideBlock = true
                        }
                        continue
                    }

                    // End of HL7 frame: END_BLOCK + CARRIAGE_RETURN
                    if (seenEndBlock && c == CARRIAGE_RETURN) {
                        break
                    }

                    if (c == END_BLOCK) {
                        seenEndBlock = true
                        continue
                    }

                    // Ignore any nested start-block if present
                    if (c == START_BLOCK) continue

                    sb.append(c)
                }

                hl7Message = sb.toString()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        hl7Message
    }

    fun parseDeviceOruMessage(hl7: String): DeviceResult {
        // Normalize line breaks in case it uses \r, \n, or both
        val segments = hl7.trim()
            .split(Regex("[\r\n]+"))
            .filter { it.isNotBlank() }

        fun segment(name: String) =
            segments.firstOrNull { it.startsWith("$name|") }?.split("|")

        val msh = segment("MSH")
        val pid = segment("PID")
        val obr = segment("OBR")
        val obx = segment("OBX")
        val nte1 = segments.firstOrNull { it.startsWith("NTE|1|") }?.split("|")
        val nte2 = segments.firstOrNull { it.startsWith("NTE|2|") }?.split("|")
        val spm = segment("SPM")

        // PID
        val patientId = pid?.getOrNull(3)              // PID-3
        val patientName = pid?.getOrNull(5)            // PID-5, "Doe^John"

        // OBR
        val orderNumber = obr?.getOrNull(3)            // OBR-3: 250423054
        val testComposite = obr?.getOrNull(4) ?: ""    // OBR-4: cTnI/PCT^C
        val testParts = testComposite.split("^")
        val testCode = testParts.getOrNull(0)          // cTnI/PCT
        val testName = testParts.getOrNull(1)          // C

        // OBX
        val obxIdComposite = obx?.getOrNull(3) ?: ""   // OBX-3: cTnI^C
        val obxIdParts = obxIdComposite.split("^")
        val resultValue = obx?.getOrNull(5)            // 3.2
        val resultUnit = obx?.getOrNull(6)             // ng/mL
        val referenceRange = obx?.getOrNull(7)         // Ref-Range:2.0-5.0
        val interpretation = obx?.getOrNull(8)         // Nor.
        val resultStatus = obx?.getOrNull(11)          // F
        val resultDateTime = obx?.getOrNull(14)        // 2025/04/23 09:57:31

        // NTE
        val lotNumber = nte1?.getOrNull(3)
            ?.removePrefix("LOT:")
            ?.trim()
        val serialNumber = nte2?.getOrNull(3)
            ?.removePrefix("SN:")
            ?.trim()

        // SPM
        val specimenType = spm?.getOrNull(2)           // Whole Blood

        logParsedFields(
            patientId = patientId,
            patientName = patientName,
            orderNumber = orderNumber,
            testCode = testCode,
            testName = testName,
            resultValue = resultValue,
            resultUnit = resultUnit,
            referenceRange = referenceRange,
            interpretation = interpretation,
            resultStatus = resultStatus,
            resultDateTime = resultDateTime,
            lotNumber = lotNumber,
            serialNumber = serialNumber,
            specimenType = specimenType
        )

        return DeviceResult(
            patientId = patientId,
            patientName = patientName,
            orderNumber = orderNumber,
            testCode = testCode,
            testName = testName,
            resultValue = resultValue,
            resultUnit = resultUnit,
            referenceRange = referenceRange,
            interpretation = interpretation,
            resultStatus = resultStatus,
            resultDateTime = resultDateTime,
            lotNumber = lotNumber,
            serialNumber = serialNumber,
            specimenType = specimenType
        )
    }

    suspend fun connectToDeviceAndHandle(host: String, port: Int) {
        val socket = Socket(host, port)
//        handleHl7Session(socket, onResult)
    }

    suspend fun startHl7Server(port: Int, onResult: (DeviceResult) -> Unit) =
        withContext(Dispatchers.IO) {//use this one for the device
            Logger.d("HL7", "Starting HL7 server on port $port")
        val serverSocket = ServerSocket(port)
            Logger.d("HL7", "HL7 server started, waiting for connections...")
        while (true) {
            val client = serverSocket.accept() // device connects
            // For simplicity handle in same coroutine; in real app, use a new coroutine
            Logger.d("HL7", "Client connected from: ${client.inetAddress.hostAddress}")
            handleHl7Session(client, onResult)
        }
    }

    suspend fun handleHl7Session(socket: Socket, onResult: (DeviceResult) -> Unit) =
        withContext(Dispatchers.IO) {
        // Optional timeout: e.g., 60s
            socket.soTimeout = 600_000
            Logger.d("HL7", "Handling HL7 session for ${socket.inetAddress.hostAddress}")

        socket.use { s ->
            while (true) {
                val message = readHl7Message(s) ?: break // no more data / closed
                Logger.d("HL7", "Raw HL7 message received:\n$message")

                val parsed = parseOruOrNull(message)
                val ackCode = if (parsed != null) "AA" else "AE"

                val ack = buildAckMessage(message, ackCode)
                Logger.d("HL7", "Sending ACK with code $ackCode:\n$ack")

                // 3) Send ACK back so device can send the next packet
                sendHl7Message(s, ack)
                if (parsed != null) {
                    onResult(parsed)
                }

                // You can also store/use `parsed` if not null
            }
        }
    }

    fun parseOruOrNull(hl7: String): DeviceResult? {
        return try {
            // Normalize line breaks in case it uses \r, \n, or both
            val segments = hl7.trim()
                .split(Regex("[\r\n]+"))
                .filter { it.isNotBlank() }

            fun segment(name: String) =
                segments.firstOrNull { it.startsWith("$name|") }?.split("|")

            val msh = segment("MSH")
            val pid = segment("PID")
            val obr = segment("OBR")
            val obx = segment("OBX")
            val nte1 = segments.firstOrNull { it.startsWith("NTE|1|") }?.split("|")
            val nte2 = segments.firstOrNull { it.startsWith("NTE|2|") }?.split("|")
            val spm = segment("SPM")

            // PID
            val patientId = pid?.getOrNull(3)              // PID-3
            val patientName = pid?.getOrNull(5)            // PID-5, "Doe^John"

            // OBR
            val orderNumber = obr?.getOrNull(3)            // OBR-3: 250423054
            val testComposite = obr?.getOrNull(4) ?: ""    // OBR-4: cTnI/PCT^C
            val testParts = testComposite.split("^")
            val testCode = testParts.getOrNull(0)          // cTnI/PCT
            val testName = testParts.getOrNull(1)          // C

            // OBX
            val obxIdComposite = obx?.getOrNull(3) ?: ""   // OBX-3: cTnI^C
            val obxIdParts = obxIdComposite.split("^")
            val resultValue = obx?.getOrNull(5)            // 3.2
            val resultUnit = obx?.getOrNull(6)             // ng/mL
            val referenceRange = obx?.getOrNull(7)         // Ref-Range:2.0-5.0
            val interpretation = obx?.getOrNull(8)         // Nor.
            val resultStatus = obx?.getOrNull(11)          // F
            val resultDateTime = obx?.getOrNull(14)        // 2025/04/23 09:57:31

            // NTE
            val lotNumber = nte1?.getOrNull(3)
                ?.removePrefix("LOT:")
                ?.trim()
            val serialNumber = nte2?.getOrNull(3)
                ?.removePrefix("SN:")
                ?.trim()

            // SPM
            val specimenType = spm?.getOrNull(2)           // Whole Blood

            logParsedFields(
                patientId = patientId,
                patientName = patientName,
                orderNumber = orderNumber,
                testCode = testCode,
                testName = testName,
                resultValue = resultValue,
                resultUnit = resultUnit,
                referenceRange = referenceRange,
                interpretation = interpretation,
                resultStatus = resultStatus,
                resultDateTime = resultDateTime,
                lotNumber = lotNumber,
                serialNumber = serialNumber,
                specimenType = specimenType
            )

            if (patientId == null || resultValue == null) {
                // treat as parse failure
                null
            } else {

                return DeviceResult(
                    patientId = patientId,
                    patientName = patientName,
                    orderNumber = orderNumber,
                    testCode = testCode,
                    testName = testName,
                    resultValue = resultValue,
                    resultUnit = resultUnit,
                    referenceRange = referenceRange,
                    interpretation = interpretation,
                    resultStatus = resultStatus,
                    resultDateTime = resultDateTime,
                    lotNumber = lotNumber,
                    serialNumber = serialNumber,
                    specimenType = specimenType
                )
            }
        } catch (e: Exception) {
            null
        }
    }

    fun sendHl7Message(socket: Socket, hl7Message: String) {
        val START_BLOCK = 0x0B.toChar()
        val END_BLOCK = 0x1C.toChar()
        val CARRIAGE_RETURN = '\r' // LF
//        val CARRIAGE_RETURN = '\n' // LF

        val framed = buildString {
            append(START_BLOCK)
            append(hl7Message)
            append(END_BLOCK)
            append(CARRIAGE_RETURN)
        }

        val out = socket.getOutputStream()
        out.write(framed.toByteArray(Charsets.UTF_8))
        out.flush()
    }

    fun buildAckMessage(
        originalMessage: String,
        ackCode: String // "AA" or "AE"
    ): String {
        // Normalize and find MSH
        val segments = originalMessage.trim()
            .split(Regex("[\r\n]+"))
            .filter { it.isNotBlank() }

        val mshSegment = segments.firstOrNull { it.startsWith("MSH|") }
            ?: throw IllegalArgumentException("No MSH segment in message")

        val mshFields = mshSegment.split("|")

        val fieldSep = "|"                     // MSH-1 already implied by split
        val encodingChars = mshFields.getOrNull(1) ?: "^~\\&"
        val sendingApp = mshFields.getOrNull(2) ?: ""
        val sendingFacility = mshFields.getOrNull(3) ?: ""
        val receivingApp = mshFields.getOrNull(4) ?: ""
        val receivingFacility = mshFields.getOrNull(5) ?: ""
        val version = mshFields.getOrNull(11) ?: "2.5"
        val msgControlId = mshFields.getOrNull(9) ?: "0"

        // Timestamp for ACK (YYYYMMDDHHMMSS), here just a dummy
        val timestamp = "20250101000000"

        // Swap sender/receiver in the ACK
        val ackMsh = listOf(
            "MSH",
            encodingChars,
            receivingApp,        // now ACK sender
            receivingFacility,
            sendingApp,          // now ACK receiver
            sendingFacility,
            timestamp,
            "",
            "ACK",               // message type
            msgControlId,        // new control ID (can also be different if you want)
            "P",
            version
        ).joinToString(fieldSep)

        val msa = listOf(
            "MSA",
            ackCode,             // "AA" or "AE"
            msgControlId         // original control ID to link ACK to the message
        ).joinToString(fieldSep)

        // Segments separated by \n because your carriage return is '\n'
//        return listOf(ackMsh, msa).joinToString("\n") + "\n"
        val CR = "\r"
        return listOf(ackMsh, msa).joinToString(CR) + CR
    }

    fun readHl7Message(socket: Socket): String? {
        Logger.i(TAG, "readHl7Message")
        val START_BLOCK = 0x0B.toChar()
        val END_BLOCK = 0x1C.toChar()
        val CARRIAGE_RETURN = '\r' // your protocol
//        val CARRIAGE_RETURN = '\n' // your protocol

        val reader = BufferedReader(
            InputStreamReader(socket.getInputStream(), Charsets.UTF_8)
        )

        val sb = StringBuilder()
        var insideBlock = false
        var seenEndBlock = false
        var ch: Int

        while (reader.read().also { ch = it } != -1) {
            Logger.i(TAG, "readHl7Message while reading")

            val c = ch.toChar()
            Logger.i(TAG, "readHl7Message while data ${c}")


            // Wait for start of block
            if (!insideBlock) {
                if (c == START_BLOCK) insideBlock = true
                continue
            }

            // End of block: <END_OF_BLOCK><CARRIAGE_RETURN>
            if (seenEndBlock && c == CARRIAGE_RETURN) {
                break
            }

            if (c == END_BLOCK) {
                seenEndBlock = true
                continue
            }

            // Ignore any stray start block
            if (c == START_BLOCK) continue

            sb.append(c)
        }

        if (sb.isEmpty()) {
            Logger.i(TAG, "readHl7Message empty")
            return null
        }
        return sb.toString()
    }

    private val TAG = "HL7"

    fun logParsedFields(
        patientId: String?,
        patientName: String?,
        orderNumber: String?,
        testCode: String?,
        testName: String?,
        resultValue: String?,
        resultUnit: String?,
        referenceRange: String?,
        interpretation: String?,
        resultStatus: String?,
        resultDateTime: String?,
        lotNumber: String?,
        serialNumber: String?,
        specimenType: String?
    ) {

        Logger.d(TAG, "===== HL7 Parsed Message =====")
        Logger.d(TAG, "Patient ID          : $patientId")
        Logger.d(TAG, "Patient Name        : $patientName")
        Logger.d(TAG, "Order Number        : $orderNumber")
        Logger.d(TAG, "Test Code           : $testCode")
        Logger.d(TAG, "Test Name           : $testName")
        Logger.d(TAG, "Result Value        : $resultValue")
        Logger.d(TAG, "Result Unit         : $resultUnit")
        Logger.d(TAG, "Reference Range     : $referenceRange")
        Logger.d(TAG, "Interpretation      : $interpretation")
        Logger.d(TAG, "Result Status       : $resultStatus")
        Logger.d(TAG, "Result DateTime     : $resultDateTime")
        Logger.d(TAG, "Lot Number          : $lotNumber")
        Logger.d(TAG, "Serial Number       : $serialNumber")
        Logger.d(TAG, "Specimen Type       : $specimenType")
        Logger.d(TAG, "================================")
    }

}