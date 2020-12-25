package com.example.rozetkin_ctf_challenge

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_init_card.*

class InitCard : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_init_card)
        start_writing_card_btn.setOnClickListener {
            startWriting()
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        start_writing_card_btn.isEnabled = true
        log.text = "Success"
    }

    @SuppressLint("SetTextI18n")
    fun startWriting() {
        log.text = "Started to write"
        start_writing_card_btn.isEnabled = false
        val intent = Intent(this, NfcActivity::class.java)
        intent.putExtra("nfcAction", NfcAction.WRITE_ALL)
        startActivityForResult(intent, 1)
    }
}