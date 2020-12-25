package com.example.rozetkin_ctf_challenge

import android.app.AlertDialog
import android.content.Intent
import android.nfc.tech.NfcA
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.coroutines.*
import kotlin.properties.Delegates

class AuthActivity : AppCompatActivity() {
    lateinit var card: RozetkinCard
    lateinit var uid: ByteArray
    private var cardNumber by Delegates.notNull<Long>()
    lateinit var login: String

    private var authType: Int = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        // get auth type: sign in / sign up
        authType = intent.getIntExtra("authType", 0)
        // show dialog to hold card
        val activity = Intent(this, NfcActivity::class.java)
        when (authType) {
            AuthType.AUTH_SIGN_UP_ACTION -> activity.putExtra(
                "nfcAction",
                NfcAction.READ_FOR_REGISTRATION
            )
            AuthType.AUTH_SIGN_IN_ACTION -> activity.putExtra("nfcAction", NfcAction.READ_FOR_LOGIN)
        }
        startActivityForResult(activity, 1)
//        uid = intent.getStringExtra("uid").toString()
//        cardNumber = intent.getLongExtra("card_number", 0)
//        Log.d("RegisterActivity", uid)
//        Log.d("RegisterActivity", cardNumber.toString())
        register_button.setOnClickListener {
            register()
        }
    }

    fun ByteArray.toHexString(): String {
        return this.joinToString("") {
            java.lang.String.format("%02x", it)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data == null) return
        Log.d("AuthActivity", data.getByteArrayExtra("uid").toString())
        uid = data.getByteArrayExtra("uid")!!
        login = if (data.hasExtra("login")) data.getStringExtra("login")!! else "null"
        Log.d("AuthActivity", login)
        cardNumber = data.getLongExtra("cardNumber", 0)
        log.text = ""
        log.append("Card number: $cardNumber\n")
//        Log.d("AuthActivity", "Activity finished with result code: $resultCode")
        Log.d(
            "AuthActivity",
            "Current action: " + data.getIntExtra("nfcAction", NfcAction.READ_FOR_REGISTRATION)
        )
        when (data.getIntExtra("nfcAction", NfcAction.READ_FOR_REGISTRATION)) {
            NfcAction.READ_ONLY_UID -> {
                Log.d("AuthActivity", "checkUid action")
                writeLoginDataToCard(uid)
                return
            }
            NfcAction.WRITE_LOGIN -> {
                Log.d("AuthActivity", "Entering afterWritingLoginData()")
                Log.d(
                    "AuthActivity",
                    "Result Write_Login: " + data.getBooleanExtra("success", false)
                )
                card.login = data.getStringExtra("login")!!
                card.cardNumber = data.getLongExtra("cardNumber", 0)
                afterWritingLoginData(data.getBooleanExtra("success", false))
                return
            }
            NfcAction.READ_FOR_LOGIN -> {
                card = RozetkinCard(
                    data.getStringExtra("login")!!,
                    uid,
                    data.getLongExtra("cardNumber", 0)
                )
                login()
            }
        }
        login_name.isEnabled = true
        register_button.isEnabled = true
        if (login != "null") login()

    }

    fun showDialogWrongName() {
        val dialog = AlertDialog.Builder(this)
        dialog.setTitle("AuthActivity")
        dialog.setMessage(getString(R.string.wrong_name))
        dialog.setCancelable(true)
        dialog.setNeutralButton("Ok") { dialogContext, which ->
            Toast.makeText(this, "Ok", Toast.LENGTH_SHORT).show()
        }
        dialog.show()
    }

    fun showDialogWrongCardNumber() {
        val dialog = AlertDialog.Builder(this)
        dialog.setTitle("AuthActivity")
        dialog.setMessage(getString(R.string.wrong_card_number))
        dialog.setCancelable(true)
        dialog.setNeutralButton("Ok") { dialogContext, which ->
            Toast.makeText(this, "Ok", Toast.LENGTH_SHORT).show()
        }
        dialog.show()
    }

    private fun validateLoginName(): Boolean {
        if (login_name.text.toString().length < 4) {
            showDialogWrongName()
            return false
        }
        if (login_name.text.toString().length > 15) {
            showDialogWrongName()
            return false
        }
        return true
    }

    private fun register() {
        if (!validateLoginName()) return
        if (!RozetkinCard.checkCardNumber(uid, cardNumber)) {
            showDialogWrongCardNumber()
            return
        }

        card = RozetkinCard(login_name.text.toString(), uid, cardNumber)
        runBlocking {
            launch {
                val response = card.register()
                if (response.error) {
                    Log.e("AuthActivity", "Failed to register; Response: $response")
                    //                log.text = response.message
                    return@launch
                }
            }
            // TODO fix: Animators may only be run on Looper threads because of next line
            register_button.isEnabled = false
            Log.d("AuthActivity:140", "WriteLoginDataToCard uid validation success!")
            val activity = Intent(applicationContext, NfcActivity::class.java)
            activity.putExtra("nfcAction", NfcAction.WRITE_LOGIN)
            activity.putExtra("previousUid", card.previousUid)
            Log.d("AuthActivity:143", "Current login: ${card.login}")
            activity.putExtra("login", card.login)
            startActivityForResult(activity, 1)

        }
//        Log.d("RegisterActivity", "ApiKey: " + card.apiKey)
    }

    fun afterWritingLoginData(success: Boolean) {
        if (!success) {
            runBlocking {
                launch(Dispatchers.Unconfined) {
                    card.sendVerify(true)
                }
            }
            log.text = getString(R.string.write_login_failed)
            log.append(
                "You need to go back to Main Screen pressing back button " +
                        "on your phone if u want to try register again"
            )
            // TODO clean card data
            return
        }
        runBlocking {
            launch(Dispatchers.Unconfined) {
                val response = card.sendVerify(false)
                //            log.text = response.message
                Log.d("AuthActivity", "Verify: $response")
            }
        }
        Log.d("AuthActivity", "Going to login() func")
        login()
    }

    private fun writeLoginDataToCard(uid: ByteArray) {
        if (!validateUidWithPrevious(uid)) {
            Log.e("AuthActivity", "Uid not matches")
            log.text = getString(R.string.different_uid)
            log.append(
                "You need to go back to Main Screen pressing back button " +
                        "on your phone if u want to try register again"
            )
            return
        }
        val activity = Intent(this, NfcActivity::class.java)
        activity.putExtra("nfcAction", NfcAction.READ_ONLY_UID)
        startActivityForResult(activity, 1)
    }

    private fun validateUidWithPrevious(uid: ByteArray): Boolean {
        Log.d("AuthActivity", "Previous uid is: " + card.previousUid.toHexString())
        Log.d("AuthActivity", "Current uid is: " + uid.toHexString())
        return card.previousUid.contentEquals(uid)
    }

    private fun login() {
        card = RozetkinCard(login, uid, cardNumber)
        runBlocking {
            launch(Dispatchers.Unconfined) {
                Log.d("AuthActivity", "getting api key...")
                val response = card.getApiKey()
                if (response == null) {
                    Log.d("AuthActivity", "Request error: askfjasldkfj")
                    return@launch
                }

                Log.d("AuthActivity", "AuthActivity:185; $response")
                Log.d("AuthActivity", "Has error: ${response.error}")
                Log.d("AuthActivity", "Has message: ${response.message}")
                //            if (!response.error) {
                //
                if (!response.error) {
                    val homeActivity = Intent(applicationContext, HomeActivity::class.java)
                    homeActivity.putExtra("apiKey", card.apiKey)
                    startActivity(homeActivity)
                } else {
                    Log.d("AuthActivity", "Response: ${response.message}")
                    log.append("Failed to get api key" + response.message + "\n")
                }
                //            } else {
                ////                    log.text = response.message
                //            }
            }
        }
    }

}