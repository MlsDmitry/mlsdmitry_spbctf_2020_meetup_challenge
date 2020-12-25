package com.example.rozetkin_ctf_challenge

import android.content.Intent
import android.nfc.NfcAdapter
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    private lateinit var nfcAdapter: NfcAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))
//        try {
//            val registerIntent = Intent(this, AuthActivity::class.java)
//            registerIntent.putExtra("uid", "041D589AF96380")
//            registerIntent.putExtra("card_number", 36138720706436356)
//            startActivity(registerIntent)
//        } catch (e: Exception) {
//            e.printStackTrace()
//            Log.d("MainActivity", e.message.toString())
//        }

        sign_up_button.setOnClickListener {
            val intent = Intent(this, AuthActivity::class.java)
            intent.putExtra("authType", AuthType.AUTH_SIGN_UP_ACTION)
            startActivity(intent)
        }
        init_card_activity_btn.setOnClickListener {
            val intent = Intent(this, InitCard::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}