package com.example.rozetkin_ctf_challenge

import android.app.AlertDialog
import android.app.PendingIntent
import android.content.Intent
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.nfc.tech.MifareClassic
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.rozetkin_ctf_challenge.extensions.*
import kotlinx.android.synthetic.main.activity_n_f_c.*
import java.io.IOException
import kotlin.random.Random


class NfcActivity : AppCompatActivity() {
    private var mNfcAdapter: NfcAdapter? = null
    private var keyA = arrayOf(
        byteArrayOf(
            0xff.toByte(),
            0xff.toByte(),
            0xff.toByte(),
            0xff.toByte(),
            0xff.toByte(),
            0xff.toByte(),
            0xff.toByte(),
            0xff.toByte(),
            0xff.toByte(),
            0xff.toByte(),
            0xff.toByte(),
            0xff.toByte()
        )
//        byteArrayOf(0xff.toByte(), 0xff.toByte(),0xff.toByte(), 0xff.toByte(), 0xff.toByte(), 0xff.toByte()),
//        byteArrayOf(0xff.toByte(), 0xff.toByte(),0xff.toByte(), 0xff.toByte(), 0xff.toByte(), 0xff.toByte()),
//        byteArrayOf(0xa0.toByte(), 0xa1.toByte(),0xa2.toByte(), 0xa3.toByte(), 0xa4.toByte(), 0xa5.toByte()),
//        byteArrayOf(0xa0.toByte(), 0xa1.toByte(),0xa2.toByte(), 0xa3.toByte(), 0xa4.toByte(), 0xa5.toByte()),
//        byteArrayOf(0xe5.toByte(), 0x6a.toByte(),0xc1.toByte(), 0x27.toByte(), 0xdd.toByte(), 0x45.toByte()),
//        byteArrayOf(0x77.toByte(), 0xda.toByte(),0xbc.toByte(), 0x98.toByte(), 0x25.toByte(), 0xe1.toByte()),
//        byteArrayOf(0xff.toByte(), 0xff.toByte(),0xff.toByte(), 0xff.toByte(), 0xff.toByte(), 0xff.toByte()),
//        byteArrayOf(0x20.toByte(), 0x66.toByte(),0xf4.toByte(), 0x72.toByte(), 0x71.toByte(), 0x29.toByte()),
//        byteArrayOf(0x26.toByte(), 0x97.toByte(),0x3e.toByte(), 0xa7.toByte(), 0x43.toByte(), 0x21.toByte()),
//        byteArrayOf(0xeb.toByte(), 0x0a.toByte(),0x8f.toByte(), 0xf8.toByte(), 0x8a.toByte(), 0xde.toByte()),
//        byteArrayOf(0xea.toByte(), 0x0f.toByte(),0xd7.toByte(), 0x3c.toByte(), 0xb1.toByte(), 0x49.toByte()),
//        byteArrayOf(0xc7.toByte(), 0x6b.toByte(),0xf7.toByte(), 0x1a.toByte(), 0x25.toByte(), 0x09.toByte()),
//        byteArrayOf(0xac.toByte(), 0xff.toByte(),0xff.toByte(), 0xff.toByte(), 0xff.toByte(), 0xff.toByte()),
//        byteArrayOf(0xac.toByte(), 0x70.toByte(),0xac.toByte(), 0x32.toByte(), 0x7a.toByte(), 0x04.toByte()),
//        byteArrayOf(0x51.toByte(), 0x04.toByte(),0x4e.toByte(), 0xfb.toByte(), 0x5a.toByte(), 0xab.toByte()),
//        byteArrayOf(0xa0.toByte(), 0xa1.toByte(),0xa2.toByte(), 0xa3.toByte(), 0xa4.toByte(), 0xa5.toByte())
    )

    //    private val keyA = arrayOf(
//        byteArrayOf(0x2c.toByte(), 0xad.toByte(), 0x3a.toByte(), 0x75.toByte(), 0xf0.toByte(), 0xdb.toByte()),
//        byteArrayOf(0xff.toByte(), 0x02.toByte(), 0x0f.toByte(), 0x6f.toByte(), 0xcd.toByte(), 0xe2.toByte()),
//        byteArrayOf(0x38.toByte(), 0xd3.toByte(), 0x97.toByte(), 0x7b.toByte(), 0x3e.toByte(), 0x37.toByte()),
//        byteArrayOf(0x2e.toByte(), 0x87.toByte(), 0x65.toByte(), 0x74.toByte(), 0x97.toByte(), 0x94.toByte()),
//        byteArrayOf(0x45.toByte(), 0x96.toByte(), 0x1b.toByte(), 0xbd.toByte(), 0x9b.toByte(), 0xbe.toByte()),
//        byteArrayOf(0xf7.toByte(), 0x01.toByte(), 0x05.toByte(), 0x1f.toByte(), 0x8c.toByte(), 0x34.toByte()),
//        byteArrayOf(0xc3.toByte(), 0xdd.toByte(), 0x0b.toByte(), 0x79.toByte(), 0x59.toByte(), 0x08.toByte()),
//        byteArrayOf(0x87.toByte(), 0x84.toByte(), 0x36.toByte(), 0x77.toByte(), 0xf1.toByte(), 0xc1.toByte()),
//        byteArrayOf(0x82.toByte(), 0xb4.toByte(), 0x01.toByte(), 0xf3.toByte(), 0xea.toByte(), 0xdb.toByte()),
//        byteArrayOf(0x65.toByte(), 0xee.toByte(), 0xe2.toByte(), 0x46.toByte(), 0xa7.toByte(), 0x62.toByte()),
//        byteArrayOf(0x93.toByte(), 0xcb.toByte(), 0x6f.toByte(), 0x36.toByte(), 0x78.toByte(), 0x75.toByte()),
//        byteArrayOf(0xab.toByte(), 0x76.toByte(), 0xe3.toByte(), 0xe4.toByte(), 0x8e.toByte(), 0x01.toByte()),
//        byteArrayOf(0xca.toByte(), 0x21.toByte(), 0xd7.toByte(), 0xd1.toByte(), 0xe9.toByte(), 0xbd.toByte()),
//        byteArrayOf(0xe3.toByte(), 0x5a.toByte(), 0xe2.toByte(), 0xd6.toByte(), 0x11.toByte(), 0x87.toByte()),
//        byteArrayOf(0xc7.toByte(), 0x65.toByte(), 0x06.toByte(), 0x02.toByte(), 0xd3.toByte(), 0xf5.toByte())
//    )
    private val keyB = arrayOf(
        byteArrayOf(
            0x68.toByte(),
            0x61.toByte(),
            0x23.toByte(),
            0x3a.toByte(),
            0xe8.toByte(),
            0x37.toByte()
        ),
        byteArrayOf(
            0x1a.toByte(),
            0x02.toByte(),
            0xcb.toByte(),
            0x8e.toByte(),
            0x08.toByte(),
            0x20.toByte()
        ),
        byteArrayOf(
            0xe2.toByte(),
            0x83.toByte(),
            0xd6.toByte(),
            0x45.toByte(),
            0x9a.toByte(),
            0xac.toByte()
        ),
        byteArrayOf(
            0x28.toByte(),
            0x75.toByte(),
            0x2b.toByte(),
            0xbb.toByte(),
            0xae.toByte(),
            0xea.toByte()
        ),
        byteArrayOf(
            0x8b.toByte(),
            0x30.toByte(),
            0xa3.toByte(),
            0xca.toByte(),
            0xf2.toByte(),
            0x57.toByte()
        ),
        byteArrayOf(
            0xab.toByte(),
            0xf0.toByte(),
            0x5d.toByte(),
            0x81.toByte(),
            0x8a.toByte(),
            0x70.toByte()
        ),
        byteArrayOf(
            0xda.toByte(),
            0x8a.toByte(),
            0xe2.toByte(),
            0xf6.toByte(),
            0xe8.toByte(),
            0xdc.toByte()
        ),
        byteArrayOf(
            0x23.toByte(),
            0x8b.toByte(),
            0x91.toByte(),
            0xc4.toByte(),
            0x40.toByte(),
            0xc8.toByte()
        ),
        byteArrayOf(
            0x7e.toByte(),
            0xd9.toByte(),
            0x63.toByte(),
            0x43.toByte(),
            0xad.toByte(),
            0xd6.toByte()
        ),
        byteArrayOf(
            0xd0.toByte(),
            0x2e.toByte(),
            0xee.toByte(),
            0x5f.toByte(),
            0x5e.toByte(),
            0xa8.toByte()
        ),
        byteArrayOf(
            0x71.toByte(),
            0x75.toByte(),
            0xf3.toByte(),
            0x11.toByte(),
            0xea.toByte(),
            0x5c.toByte()
        ),
        byteArrayOf(
            0x5f.toByte(),
            0x97.toByte(),
            0x97.toByte(),
            0x30.toByte(),
            0x98.toByte(),
            0x38.toByte()
        ),
        byteArrayOf(
            0xfe.toByte(),
            0x0e.toByte(),
            0x72.toByte(),
            0xe9.toByte(),
            0x40.toByte(),
            0x0d.toByte()
        ),
        byteArrayOf(
            0x67.toByte(),
            0x6c.toByte(),
            0x01.toByte(),
            0x4d.toByte(),
            0x39.toByte(),
            0xb8.toByte()
        ),
        byteArrayOf(
            0xe3.toByte(),
            0x7b.toByte(),
            0xea.toByte(),
            0x55.toByte(),
            0x3c.toByte(),
            0x68.toByte()
        )
    )

    private var mNfcPendingIntent: PendingIntent? = null
    private val NFC_TAG = "android.nfc.extra.TAG"
    private var nfcAction = NfcAction.READ_FOR_LOGIN
    private lateinit var loginExtra: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_n_f_c)

        nfcAction = intent.getIntExtra("nfcAction", NfcAction.READ_FOR_LOGIN)
        loginExtra = intent.getStringExtra("login").toString()
//        hardCodedLogin()
//        hardCodedRegister3()
//        return
        // TODO enable dialog
//        showNfcCardDialog()
        mNfcAdapter = NfcAdapter.getDefaultAdapter(this)

        if (mNfcAdapter == null) {
            log.append("Doesn't support nfc\n")
            Log.d("NfcActivity", "Doesn't support nfc")
        } else {
            Log.d("NfcActivity", "NFC supported")
        }

        mNfcPendingIntent = PendingIntent.getActivity(
            this,
            0,
            Intent(this, javaClass).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP),
            0
        )
        if (intent.action == NfcAdapter.ACTION_TAG_DISCOVERED) {
            processIntent(intent)
        }
    }

    fun showNfcCardDialog() {
        val dialog = AlertDialog.Builder(this)
        dialog.setTitle("AuthActivity")
        dialog.setMessage(getString(R.string.insert_card_notif))
        dialog.setCancelable(true)
        dialog.setNeutralButton("Ok") { dialogContext, which ->
            Toast.makeText(applicationContext, "Ok", Toast.LENGTH_SHORT).show()
        }
        dialog.show()
    }

    override fun onPause() {
        super.onPause()
        disableNfcForegroundDispatch()
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        if (intent != null) {
            Log.e("NfcActivity", "intent action: " + intent.action.toString())
            if (NfcAdapter.ACTION_TAG_DISCOVERED == intent.action) {
                processIntent(intent)
            }
        }
    }

    private fun getNfcTag(intent: Intent): Tag? {
        var tag = intent.getParcelableExtra<Tag>(NFC_TAG)
        if (tag == null) {
            Log.d("test tag", "Nope")
            return null
        }
        return tag
    }

    private fun bigEndianConversion(bytes: ByteArray): Long {
        var value: Long = 0
        for (element in bytes) {
            value = (value shl 8) + (element.toInt() and 0xff)
        }
        return value
    }

    fun readLogin(mfc: MifareClassic): String {
        try {
            if (!mfc.authenticateSectorWithKeyA(5, keyA[0]))
                throw WrongSectorKeyException()
            val sector5 = mfc.getSector(5)
            if (sector5[0][0] == 0x00.toByte()) return "null"
            return mfc.readUntilNull(sector5[0]).toString(Charsets.UTF_8).replace("\u0000", "")

        } catch (e: Exception) {
            Log.e("NfcActivity", "Error: $e")
        }
        return "null"
    }

    fun readCardNumber(mfc: MifareClassic): Long {
        try {
            if (!mfc.authenticateSectorWithKeyA(15, keyA[0]))
                throw WrongSectorKeyException()
            val sector15 = mfc.getSector(15)
            Log.d("NfcActivity", mfc.readUntilNull(sector15[0]).toHexString())
            return bigEndianConversion(mfc.readUntilNull(sector15[0]))
        } catch (e: Exception) {
            Log.e("NfcActivity", "Error: $e")
        }
        return 0
    }

    fun hardCodedLogin() {
        val login = "mlsdmitry"
//        DAB71F56
        val uid = byteArrayOf(0xDA.toByte(), 0xB7.toByte(), 0x1F.toByte(), 0x56.toByte())
        val cardNumber = 12323871444919258
        val fakeIntent = Intent()
        fakeIntent.putExtra("uid", uid)
        fakeIntent.putExtra("login", login)
        fakeIntent.putExtra("cardNumber", cardNumber)
        setResult(RESULT_OK, fakeIntent)
        finish()
    }

    fun hardCodedRegister1() {
        Log.d("NfcActivity", "hardCodedRegister1: $nfcAction")
        val uid = byteArrayOf(0xDA.toByte(), 0xB7.toByte(), 0x1F.toByte(), 0x56.toByte())
        val cardNumber = 12323871444919258
        val fakeIntent = Intent()
        fakeIntent.putExtra("nfcAction", nfcAction)
        fakeIntent.putExtra("uid", uid)
        fakeIntent.putExtra("cardNumber", cardNumber)
        fakeIntent.putExtra("success", true)
        if (nfcAction == NfcAction.WRITE_LOGIN) {
            fakeIntent.putExtra("login", "mlsdmitry")
        }
        setResult(RESULT_OK, fakeIntent)
        finish()
    }

    @ExperimentalUnsignedTypes
    fun hardCodedRegister2() {
        // Invalid Card number
        val uid = byteArrayOf(0xDA.toByte(), 0xB7.toByte(), 0x1F.toByte(), 0x56.toByte())
        val cardNumber = Random.nextLong(11111111111111111, 99999999999999999)
        val fakeIntent = Intent()
        fakeIntent.putExtra("uid", uid)
        fakeIntent.putExtra("cardNumber", cardNumber)
        if (nfcAction == NfcAction.READ_ONLY_UID) {
            fakeIntent.putExtra("checkUid", true)
        }
        setResult(RESULT_OK, fakeIntent)
        finish()
    }

    fun ByteArray.toHexString(): String {
        return this.joinToString("") {
            java.lang.String.format("%02x", it)
        }
    }

    fun hardCodedRegister3() {
        // Uid changed
        Log.d("NfcActivity", "hardCodedRegister1: $nfcAction")
        var uid: ByteArray
        uid = byteArrayOf(0xDA.toByte(), 0xB7.toByte(), 0x1F.toByte(), 0x56.toByte())
        if (nfcAction == NfcAction.READ_ONLY_UID) {
            uid = byteArrayOf(0xDA.toByte(), 0xB7.toByte(), 0x1F.toByte(), 0x55.toByte())
        }
        Log.d("NfcActivity:240", uid.toHexString())
        val cardNumber = 12323871444919258
        val fakeIntent = Intent()
        fakeIntent.putExtra("uid", uid)
        fakeIntent.putExtra("cardNumber", cardNumber)
        fakeIntent.putExtra("nfcAction", nfcAction)
        fakeIntent.putExtra("success", true)
        if (nfcAction == NfcAction.WRITE_LOGIN) {
            fakeIntent.putExtra("login", "mlsdmitry")
        }
        setResult(RESULT_OK, fakeIntent)
        finish()
    }

    fun writeLogin(mfc: MifareClassic, login: String) {
        // 5 sector - login field
        try {
            if (!mfc.authenticateSectorWithKeyA(5, keyA[0]))
                throw WrongSectorKeyException()
//            if (!mfc.authenticateSectorWithKeyB(5, keyA[0]))
//                throw WrongSectorKeyException()
            mfc.writeLogin(5, login.padEnd(16, 0x00.toChar()).toByteArray(Charsets.UTF_8))

            Log.d("NfcActivity:367", "Written to card")
        } catch (e: IOException) {
            Log.d("NfcActivity", "Failed to write login data")
        }
    }

    private fun processIntent(intent: Intent) {
        val tag = getNfcTag(intent)
        var uid = intent.getByteArrayExtra("android.nfc.extra.ID")
        Log.d("NfcActivity", "in processIntent()")
        Log.d("NfcActivity:376", "Action: $nfcAction")
        val mfc: MifareClassic? = tag?.getMifareTagOr {
            MCUtils().patchTag(it)
        }

        if (mfc == null) {
            Log.d("NfcActivity", "Couldn't read, try next time")
            return
        }
        try {
            mfc.connect()
        } catch (e: IOException) {
            Log.d("NfcActivity", "Couldn't connect")
        }
        val fakeIntent = Intent()
        var login = readLogin(mfc)
        val cardNumber: Long = readCardNumber(mfc)
        // TODO add other switches ex: READ_ONLY_UID
        fakeIntent.putExtra("nfcAction", nfcAction)
        fakeIntent.putExtra("uid", uid)
        Log.d("NfcActivity:398", intent.getStringExtra("login").toString())

        when (nfcAction) {
            NfcAction.READ_FOR_LOGIN -> {
                // login: String, cardNumber: Long
                fakeIntent.putExtra("login", login)
                fakeIntent.putExtra("cardNumber", cardNumber)
            }
            NfcAction.READ_FOR_REGISTRATION -> {
                // uid: ByteArray, login: String, cardNumber: Long

                fakeIntent.putExtra("login", login)
                fakeIntent.putExtra("cardNumber", cardNumber)
            }
            NfcAction.WRITE_LOGIN -> {
                Log.d("NfcActivity:412", "Login: $loginExtra")
                try {
                    writeLogin(mfc, loginExtra)
                    fakeIntent.putExtra("success", true)
                    fakeIntent.putExtra("login", readLogin(mfc))
                    fakeIntent.putExtra("cardNumber", cardNumber)
                } catch (e: Exception) {
                    Log.e("NfcActivity:415", "Error: $e")
                    fakeIntent.putExtra("success", false)
                }
            }
        }
        Log.d("NfcActivity", "Login: ${readLogin(mfc)}; cardNumber: $cardNumber")

        if (cardNumber == 0.toLong()) return
        setResult(RESULT_OK, fakeIntent)
        mfc.close()
        finish()
    }


    override fun onResume() {
        super.onResume()
        if (mNfcAdapter != null) {
            if (mNfcAdapter!!.isEnabled) {
                mNfcAdapter!!.enableForegroundDispatch(this, mNfcPendingIntent, null, null)
            }
        }

    }

    private fun disableNfcForegroundDispatch() {
        try {
            mNfcAdapter?.disableForegroundDispatch(this)
        } catch (ex: IllegalStateException) {
            Log.e("NfcActivity", "Error disabling NFC foreground dispatch", ex)
        }
    }

    private fun checkNfcEnabled() {
        val nfcEnabled = mNfcAdapter?.isEnabled
        if (nfcEnabled != null && !nfcEnabled) {
            startActivity(Intent(Settings.ACTION_WIRELESS_SETTINGS))
        }
    }

}