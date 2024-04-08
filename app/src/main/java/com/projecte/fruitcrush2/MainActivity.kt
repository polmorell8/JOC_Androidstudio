package com.projecte.fruitcrush2

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.projecte.fruitcrush2.ui.theme.Fruitcrush2Theme

class MainActivity : ComponentActivity() {
    lateinit var auth: FirebaseAuth
    var user: FirebaseUser? = null;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
        user = auth.currentUser
    }

    override fun onStart() {
        usuariLogejat()
        super.onStart()
    }

    private fun usuariLogejat()
    {
        if (user !=null)
        {
            val intent= Intent(this, Menu::class.java)
            startActivity(intent)
            finish()
        }
        else
        {
            val intent= Intent(this, AccountMenu::class.java)
            startActivity(intent)
            finish()
        }
    }
}
