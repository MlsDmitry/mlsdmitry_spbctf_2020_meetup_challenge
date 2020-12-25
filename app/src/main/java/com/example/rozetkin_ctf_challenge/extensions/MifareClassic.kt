package com.example.rozetkin_ctf_challenge.extensions

import android.nfc.Tag
import android.nfc.tech.MifareClassic
import java.io.IOException


//
//inline fun <T> MifareClassic.using(fn: MifareClassic.() -> T): T {
//    connect()
//    val result = fn.invoke(this)
//    close()
//    return result
//}

inline fun Tag.getMifareTagOr(fallback: (Tag) -> Tag?): MifareClassic =
    try {
        MifareClassic.get(this)
    } catch (e: Exception) {
        MifareClassic.get(fallback(this))
    }

@Throws(IOException::class)
fun MifareClassic.getSector(sectorId: Int): Array<ByteArray> {
    val block = sectorToBlock(sectorId)
    val blocksCount = getBlockCountInSector(sectorId)
    val data = Array(blocksCount) { ByteArray(MifareClassic.BLOCK_SIZE) }
    for (i in 0 until blocksCount) {
        data[i] = readBlock(block + i)
    }
    return data
}

fun MifareClassic.readUntilNull(data: ByteArray): ByteArray {
    var bytes = ByteArray(data.size)
    var nullBytesCount = 0
    for (byte in data.asIterable()) {
        if (nullBytesCount == 2) break
        if (byte == 0x00.toByte()) nullBytesCount++
        bytes += byte
    }
    return bytes.copyOfRange(0, bytes.size - 2)
}

@Throws(IOException::class)
fun MifareClassic.writeLogin(sectorId: Int, data: ByteArray) {
    val block = sectorToBlock(sectorId)
//    for (i in 0 until blocksCount - 1) { // last block contains information about keys, excluding it
    writeBlock(block, data)
//    }
}

@Throws(IOException::class)
fun MifareClassic.writeFrom(sectorId: Int, data: Array<ByteArray>) {
    val block = sectorToBlock(sectorId)
    val blocksCount = getBlockCountInSector(sectorId)
    for (i in 0 until blocksCount) { // last block contains information about keys, excluding it
        writeBlock(block + i, data[i])
    }
}
