package com.projecte.fruitcrush2

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class AccountLogin : ComponentActivity() {
    lateinit var correoLogin : EditText
    lateinit var passLogin : EditText
    lateinit var BtnLogin : Button
    lateinit var auth: FirebaseAuth //FIREBASE AUTENTIFICACIO

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.acc_login)

        correoLogin =findViewById<EditText>(R.id.correoLogin)
        passLogin =findViewById<EditText>(R.id.passLogin)
        BtnLogin =findViewById<Button>(R.id.Login)

        auth = FirebaseAuth.getInstance()

        BtnLogin.setOnClickListener(){
            //Abans de fer el registre validem les dades
            var email:String = correoLogin.getText().toString()
            var passw:String = passLogin.getText().toString()
            // validació del correu
            // si no es de tipus correu
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                correoLogin.setError("Invalid Mail")
            }
            else if (passw.length<6) {
                passLogin.setError("Password less than 6 chars")
            }
            else
            {
                LogindeJugador(email, passw)
            }
        }

    }

    private fun LogindeJugador(email: String, passw: String) {
        auth.signInWithEmailAndPassword(email, passw)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    updateUI(user)
                } else {
                    Toast.makeText(baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
//                    updateUI(null)
                }
            }
    }
    fun updateUI(user: FirebaseUser?)
    {
        val intent= Intent(this, Menu::class.java)
        startActivity(intent)
        finish()
    }

}