package com.mlsdmitry.rozetkin_ctf_challenge

import MlsDmitrysChallenge.R
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class HomeActivity : AppCompatActivity() {
    lateinit var apiKey: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        apiKey = intent.getStringExtra("apiKey")!!.toString()
        get_info.setOnClickListener {
            getInfo()
        }
    }

    private fun getInfo() {
        runBlocking {
            launch {
                // TODO clicking button fast -> crash
                // TODO add getInfo
                val response = RozetkinCard.getInfo(apiKey)
                log.text = ""
                log.append("Login: ${response.login}\n")
                log.append("Additional info: ${response.additional_info}\n")
            }
        }
    }

}