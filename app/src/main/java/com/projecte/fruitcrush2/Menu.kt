package com.projecte.fruitcrush2

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Typeface
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso
import java.io.ByteArrayOutputStream
import java.util.UUID

class Menu : AppCompatActivity() {
    lateinit var auth: FirebaseAuth

    lateinit var tancarSessio: Button
    lateinit var CreditsBtn: Button
    lateinit var PuntuacionsBtn: Button
    lateinit var jugarBtn: Button
    lateinit var editarBtn: Button


    lateinit var miPuntuaciotxt: TextView
    lateinit var puntuacio: TextView
    lateinit var uid: TextView
    lateinit var correo: TextView
    lateinit var nom: TextView
    lateinit var edat: TextView
    lateinit var poblacio: TextView
    lateinit var imatgeUri: Uri
    lateinit var storageReference: StorageReference

    lateinit var imatgePerfil: ImageView


    lateinit var reference: DatabaseReference
    private var nivell = "1"


    var user: FirebaseUser? = null;

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.menu)
        tancarSessio = findViewById<Button>(R.id.tancarSessio)
        tancarSessio.setOnClickListener() {
            tancalaSessio()
        }
        storageReference = FirebaseStorage.getInstance().getReference()

        auth = FirebaseAuth.getInstance()
        user = auth.currentUser

        tancarSessio = findViewById<Button>(R.id.tancarSessio)
        CreditsBtn = findViewById<Button>(R.id.CreditsBtn)
        PuntuacionsBtn = findViewById<Button>(R.id.PuntuacionsBtn)
        jugarBtn = findViewById<Button>(R.id.jugarBtn)

        editarBtn = findViewById<Button>(R.id.editarBtn)
        edat = findViewById(R.id.edat)
        poblacio = findViewById(R.id.poblacio)
        imatgePerfil = findViewById(R.id.imatgePerfil)


        CreditsBtn.setOnClickListener {
            Toast.makeText(this, "Credit1s", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, CreditsMain::class.java)
            startActivity(intent)
        }

        PuntuacionsBtn.setOnClickListener() {
            var intent = Intent(this, LlistatJugadors::class.java)
            startActivity(intent)
        }
        jugarBtn.setOnClickListener() {
            var Uids: String = uid.getText().toString()
            var noms: String = nom.getText().toString()
            var puntuacios: String = puntuacio.getText().toString()
            var nivells: String = nivell
            val intent = Intent(this, LevelMenu::class.java)
            intent.putExtra("UID", Uids)
            intent.putExtra("NOM", noms)
            intent.putExtra("PUNTUACIO", puntuacios)
            intent.putExtra("NIVELL", nivells)
            startActivity(intent)
            finish()
        }
        editarBtn.setOnClickListener() {
            Toast.makeText(this, "EDITAR", Toast.LENGTH_SHORT).show()
            canviaLaImatge()
        }



        miPuntuaciotxt = findViewById(R.id.miPuntuaciotxt)
        puntuacio = findViewById(R.id.puntuacio)
        uid = findViewById(R.id.uid)
        correo = findViewById(R.id.correo)
        nom = findViewById(R.id.nom)

        val tf = Typeface.createFromAsset(assets, "font/font.ttf")
        miPuntuaciotxt.typeface = tf
        puntuacio.typeface = tf
        uid.typeface = tf
        correo.typeface = tf
        nom.typeface = tf
        tancarSessio.typeface = tf
        CreditsBtn.typeface = tf
        PuntuacionsBtn.typeface = tf
        jugarBtn.typeface = tf
        editarBtn.typeface = tf
    }

    override fun onStart() {
        usuariLogejat()
        consulta()
        super.onStart()
    }

    private fun tancalaSessio() {
        auth.signOut()
        val intent = Intent(this, AccountMenu::class.java)
        startActivity(intent)
    }

    private fun usuariLogejat() {
        if (user == null) {
            val intent = Intent(this, Menu::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun consulta() {
        //Amb Firebase no fem consultes realment, el que fem en connectar-nos a una referencia
        // i aquesta anirà canviant automàticament quan canvii la base de dades
        // reference apunta a "DATA BASE JUGADORS"
        // sempre es crea un referencia a un punt del arbre de la base de dades
        // Per exemple si tenim la base de dades
        // arrel
        // - nivell dos
        // - nivell dos.1
        // - nom: "pepe"
        // - dni: "1231212121"
        // - nivell dos.2: "34"
        // - nivell dos.3: "36"
        //var bdasereference:DatabaseReference =
        FirebaseDatabase.getInstance().getReference()
        // .child("nivell dos")
        // .child("nivell dos.1")
        // Ara estariem al novell dos.1 del arbre
        // Ens podem subscriure amb un listener que té dos métodes
        // onDataChange (es crida si s'actualitza les dades o és la primera vegada que ens suscribim)
        // onCancelled Es crida si hi ha un error o es cancel·la la lectura
        // al primer métode rebrem un objecte json que és la branca sobre la que demanem actualització
        // getkey retorna la clave del objecte
        // getValue retorna el valor
        // els subnodes (fills) es recuperen amb getChildren
        // es poden llegir com un llistat d'objectes Datasnapshots
        // o navegar a subnodes concrets amb child("nomdelsubnode")
        var database: FirebaseDatabase =
            FirebaseDatabase.getInstance("https://fruitcrush-94085-default-rtdb.europe-west1.firebasedatabase.app/")
        var bdreference: DatabaseReference = database.getReference("DATA BASE JUGADORS")
        bdreference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                Log.i(
                    "DEBUG", "arrel value" +
                            snapshot.getValue().toString()
                )
                Log.i("DEBUG", "arrel key" + snapshot.key.toString())
                // ara capturem tots els fills
                var trobat: Boolean = false
                for (ds in snapshot.getChildren()) {
                    Log.i("DEBUG", "DS key:" + ds.child("Uid").key.toString())
                    Log.i("DEBUG", "DS value:" + ds.child("Uid").getValue().toString())
                    Log.i("DEBUG", "DS data:" + ds.child("Data").getValue().toString())
                    Log.i("DEBUG", "DS mail:" + ds.child("Email").getValue().toString())
                    //mirem si el mail és el mateix que el del 67 jugador
                    //si és així, mostrem les dades als textview corresponents
                    if (ds.child("Email").getValue().toString().equals(user?.email)) {
                        trobat = true
                        puntuacio.setText(ds.child("Puntuacio").getValue().toString())
                        uid.setText(ds.child("Uid").getValue().toString())
                        correo.setText(ds.child("Email").getValue().toString())
                        nom.setText(ds.child("Nom").getValue().toString())
                        nivell = ds.child("Nivell").getValue().toString()
                        poblacio.setText(ds.child("Poblacio").getValue().toString())
                        edat.setText(ds.child("Edat").getValue().toString())
                        var imatge: String = ds.child("Imatge").getValue().toString()
                        try {
                            Picasso.get().load(imatge).into(imatgePerfil)
                        } catch (e: Exception) {
                            Picasso.get().load(R.drawable.cereza).into(imatgePerfil)
                        }
                    }
                    if (!trobat) {
                        Log.e("ERROR", "ERROR NO TROBAT MAIL")
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("ERROR", "ERROR DATABASE CANCEL")
            }
        })
    }

    fun isPermissionsAllowed(): Boolean {
        return if (ContextCompat.checkSelfPermission(this,android.Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED) {
            false
        } else true
    }
    fun isPermissionsAllowedCamera(): Boolean {
        return if (ContextCompat.checkSelfPermission(this,android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            false
        } else true
    }
    fun askForPermissions(): Boolean {
        val REQUEST_CODE=201
        if (!isPermissionsAllowed()) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this ,android.Manifest.permission.READ_MEDIA_IMAGES)) {
                showPermissionDeniedDialog()
            } else {
                ActivityCompat.requestPermissions(this
                    ,arrayOf(android.Manifest.permission.READ_MEDIA_IMAGES),REQUEST_CODE)
            }
            return false
        }
        return true
    }

    fun askForPermissionsCamera(): Boolean {
        val REQUEST_CODE=101
        if (!isPermissionsAllowedCamera()) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this ,android.Manifest.permission.CAMERA)) {
                showPermissionDeniedDialog()
            } else {
                ActivityCompat.requestPermissions(this
                    ,arrayOf(android.Manifest.permission.CAMERA),REQUEST_CODE)
            }
            return false
        }
        return true
    }
    override fun onRequestPermissionsResult(requestCode: Int,permissions: Array<String>,grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        val REQUEST_CODE_MEDIA = 201
        val REQUEST_CODE_CAMERA = 101
        when (requestCode) {
            REQUEST_CODE_MEDIA -> {
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission is granted, you can perform your operation here
                } else {
                    // permission is denied, you can ask for permission again, if you want
                    // askForPermissions()
                }
                return
            }
            REQUEST_CODE_CAMERA -> {
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission is granted, you can perform your operation here
                } else {
                    // permission is denied, you can ask for permission again, if you want
                    // askForPermissions()
                }
                return
            }
        }
    }
    private fun showPermissionDeniedDialog() {
        AlertDialog.Builder(this)
            .setTitle("Permission Denied")
            .setMessage("Permission is denied, Please allow permissions from App Settings.")
                .setPositiveButton("App Settings", DialogInterface.OnClickListener { dialogInterface, i ->
                        // send to app settings if permission is denied permanently
                        val intent = Intent()
                        intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                        val uri = Uri.fromParts("package", getPackageName(), null)
                        intent.data = uri
                        startActivity(intent)
                    })
                .setNegativeButton("Cancel",null)
                .show()
    }
    private fun canviaLaImatge() {
        //utilitzarem un alertdialog que seleccionara de galeria o agafar una foto
        // Si volem fer un AlertDialog amb més de dos elements (amb una llista),
        // Aixó ho fariem amb fragments (que veurem més endevant)
        // Aquí hi ha un tutorial per veure com es fa:
        // https://www.codevscolor.com/android-kotlin-list-alert-dialog
        //Veiem com es crea un de dues opcions (habitualment acceptar o cancel·lar:
        val dialog = AlertDialog.Builder(this)
            .setTitle("CANVIAR IMATGE")
            .setMessage("Seleccionar imatge de: ")
            .setNegativeButton("Galeria") { view, _ ->
                Toast.makeText(this, "De galeria",
                    Toast.LENGTH_SHORT).show()
                //mirem primer si tenim permisos per a accedir a Read External Storage
                    if (askForPermissions()) {
                        // Permissions are already granted, do your stuff
                        // Ara agafarem una imatge de la galeria
                        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
                        val REQUEST_CODE=201 //Aquest codi és un número que faremservir per
                        // a identificar el que hem recuperat del intent
                        // pot ser qualsevol número
                            intent.type = "image/*"
                        intent.addFlags(FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
                        startActivityForResult(intent, REQUEST_CODE)
                    }
                        else{
                            Toast.makeText(this, "ERROR PERMISOS",
                                Toast.LENGTH_SHORT).show()
                        }
            }
            .setPositiveButton("Càmera") { view, _ ->
                if (askForPermissionsCamera()) {
                    val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    val REQUEST_CODE = 101
                    startActivityForResult(intent, REQUEST_CODE)
                }
                else{
                    Toast.makeText(this, "ERROR PERMISOS",
                        Toast.LENGTH_SHORT).show()
                }
            }
            .setCancelable(false)
            .create()
        dialog.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 201 && resultCode == Activity.RESULT_OK) {
            val imageUri: Uri? = data?.data
            imageUri?.let {
                Log.i("DEBUG", "URI: $it")
                runOnUiThread(Runnable {
                    imatgePerfil.setImageURI(it)
                })
                pujarFoto(it)
            }
        } else if (requestCode == 101 && resultCode == Activity.RESULT_OK) {
            val imageBitmap: Bitmap? = data?.extras?.get("data") as Bitmap?
            imageBitmap?.let {
                Log.i("DEBUG", "URI: $it")
                runOnUiThread(Runnable {
                    imatgePerfil.setImageBitmap(it)
                })
                pujarFotoBitMap(it)
            }
        } else {
            Toast.makeText(
                this, "Error recuperant imatge",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

        private fun pujarFoto(imatgeUri: Uri) {
            var folderReference: StorageReference = storageReference.child("FotosPerfil")
            var Uids: String = uid.getText().toString()
            imatgePerfil.isDrawingCacheEnabled = true
            imatgePerfil.buildDrawingCache()
            val bitmap = (imatgePerfil.drawable as BitmapDrawable).bitmap
            val baos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
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
    private fun pujarFotoBitMap(imatgeBitmap: Bitmap) {
        var folderReference: StorageReference = storageReference.child("FotosPerfil")
        var Uids: String = uid.getText().toString()
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

