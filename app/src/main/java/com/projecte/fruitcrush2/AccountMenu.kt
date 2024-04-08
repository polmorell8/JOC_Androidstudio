package com.projecte.fruitcrush2

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.ComponentActivity

class AccountMenu : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.acc_menu)
        var BTMLOGIN = findViewById<Button>(R.id.btn_login);
        var BTMREGISTRO = findViewById<Button>(R.id.btn_register);
        BTMLOGIN.setOnClickListener(){
            var intent = Intent(this, AccountLogin::class.java)
            startActivity(intent)
        }
        BTMREGISTRO.setOnClickListener(){
            var intent = Intent(this, AccountRegister::class.java)
            startActivity(intent)
        }
}}