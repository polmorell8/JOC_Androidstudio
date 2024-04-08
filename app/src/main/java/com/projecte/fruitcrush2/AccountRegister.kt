package com.projecte.fruitcrush2

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.projecte.fruitcrush2.ui.theme.Fruitcrush2Theme
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.Calendar

class AccountRegister<FirebaseUser> : ComponentActivity() {

    lateinit var correoEt : EditText
    lateinit var passEt :EditText
    lateinit var nombreEt :EditText
    lateinit var fechaTxt : TextView
    lateinit var Registrar : Button
    lateinit var edatEt :EditText
    lateinit var poblacioEt :EditText
    lateinit var auth: FirebaseAuth //FIREBASE AUTENTIFICACIO
    lateinit var storageReference: StorageReference



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.acc_register)
        correoEt =findViewById<EditText>(R.id.correoEt)
        passEt =findViewById<EditText>(R.id.passEt)
        nombreEt =findViewById<EditText>(R.id.nombreEt)
        fechaTxt =findViewById<TextView>(R.id.fechaEt)
        Registrar =findViewById<Button>(R.id.Registrar)
        edatEt =findViewById<EditText>(R.id.edatEt)
        poblacioEt =findViewById<EditText>(R.id.poblacioEt)


        storageReference = FirebaseStorage.getInstance().getReference()
        auth = FirebaseAuth.getInstance()

        val date = Calendar.getInstance().time
        val formatter = SimpleDateFormat.getDateInstance()
        val formatedDate = formatter.format(date)
        fechaTxt.setText(formatedDate)

        Registrar.setOnClickListener(){
            var email:String = correoEt.getText().toString()
            var pass:String = passEt.getText().toString()

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                correoEt.setError("Invalid Mail")
            }
            else if (pass.length <6) {
                passEt.setError("Password less than 6 chars")
            }
            else
            {
                RegistrarJugador(email, pass)
            }
        }
    }

    fun RegistrarJugador(email:String, passw:String){
        auth.createUserWithEmailAndPassword(email, passw)
            .addOnCompleteListener(this) { task ->
                34
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Toast.makeText(
                        this,"createUserWithEmail:success", Toast.LENGTH_SHORT).show()
                    val user = auth.currentUser
                    updateUI(user)
                } else {
                    Toast.makeText(baseContext, "Authentication failed.", Toast.LENGTH_SHORT).show()
                    //updateUI(null)
                }
            }



    }
    fun updateUI(user: com.google.firebase.auth.FirebaseUser?){
        if (user!=null)
        {
            var puntuacio: Int = 0
            var uidString: String = user.uid
            var correoString: String = correoEt.getText().toString()
            var passString: String = passEt.getText().toString()
            var nombreString: String = nombreEt.getText().toString()
            var fechaString: String= fechaTxt.getText().toString()
            var nivell: String = "1"
            var edatString = edatEt.getText().toString()
            var poblacioString = poblacioEt.getText().toString()


            var dadesJugador : HashMap<String,String> = HashMap<String, String>()
            dadesJugador.put ("Uid",uidString)
            dadesJugador.put ("Email",correoString)
            dadesJugador.put ("Password",passString)
            dadesJugador.put ("Nom",nombreString)
            dadesJugador.put ("Data",fechaString)
            dadesJugador.put ("Puntuacio",puntuacio.toString())
            dadesJugador.put ("Nivell", nivell)
            dadesJugador.put ("Edat",edatString)
            dadesJugador.put ("Poblacio",poblacioString)
            dadesJugador.put ("Imatge",uidString+".jpg")

            pujarFotoBitMap((resources.getDrawable(R.drawable.cereza) as BitmapDrawable).bitmap,uidString)


            var database: FirebaseDatabase = FirebaseDatabase.getInstance("https://fruitcrush-94085-default-rtdb.europe-west1.firebasedatabase.app/")
            var reference: DatabaseReference = database.getReference("DATA BASE JUGADORS")
            if(reference!=null) {
                reference.child(uidString).setValue(dadesJugador)
                Toast.makeText(this, "USUARI BEN REGISTRAT",
                    Toast.LENGTH_SHORT).show()
            }
            else{
                Toast.makeText(this, "ERROR BD", Toast.LENGTH_SHORT).show()
            }
            finish()
            }
            else
            {
                Toast.makeText( this,"ERROR CREATE USER",Toast.LENGTH_SHORT).show()
            }
    }

    private fun pujarFotoBitMap(imatgeBitmap: Bitmap, Uids: String) {
        var folderReference: StorageReference = storageReference.child("FotosPerfil")
        val baos = ByteArrayOutputStream()
        imatgeBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()
        var uploadTask = folderReference.child(Uids).putBytes(data)
        uploadTask.addOnFailureListener {
            Toast.makeText(
                this, "Error enviant imatge a Storage",
                Toast.LENGTH_SHORT
            ).show()
        }.addOnSuccessListener { taskSnapshot ->
            Log.i("DEBUG", "IMATGE PUJADA")
        }
    }
}
