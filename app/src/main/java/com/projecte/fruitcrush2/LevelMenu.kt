package com.projecte.fruitcrush2

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.ComponentActivity

class LevelMenu : ComponentActivity() {
    private var NOM: String =""
    private var PUNTUACIO: String=""
    private var UID: String=""
    private var NIVELL: String=""

    lateinit var imageButton1 :ImageButton
    lateinit var imageButton2 :ImageButton
    lateinit var imageButton3 :ImageButton


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.lvl_menu)
        //ara recuperarem els valors
        var intent:Bundle? = getIntent().extras
        UID = intent?.getString("UID").toString()
        NOM = intent?.getString("NOM").toString()
        PUNTUACIO = intent?.getString("PUNTUACIO").toString()
        NIVELL = intent?.getString("NIVELL").toString()
        //busco els 3 butons
        imageButton1 = findViewById(R.id.level1)
        imageButton2 = findViewById(R.id.level2)
        imageButton3 = findViewById(R.id.level3)
        //deshabilito els 3 buttons
        imageButton1.isEnabled=false
        imageButton2.isEnabled=false
        imageButton3.isEnabled=false
        imageButton1.visibility =View.INVISIBLE
        imageButton2.visibility =View.INVISIBLE
        imageButton3.visibility =View.INVISIBLE
        imageButton1.setOnClickListener(View.OnClickListener {
            val intent= Intent(this, Lvl::class.java)
            intent.putExtra("UID",UID)
            intent.putExtra("NOM",NOM)
            intent.putExtra("PUNTUACIO",PUNTUACIO)
            intent.putExtra("NIVELL",NIVELL)
            intent.putExtra("LEVEL","1")
            startActivity(intent)
            finish()
        })
        imageButton2.setOnClickListener(View.OnClickListener {
            val intent= Intent(this, Lvl::class.java)
            intent.putExtra("UID",UID)
            intent.putExtra("NOM",NOM)
            intent.putExtra("PUNTUACIO",PUNTUACIO)
            intent.putExtra("NIVELL",NIVELL)
            intent.putExtra("LEVEL","2")
            startActivity(intent)
            finish()
        })
        imageButton3.setOnClickListener(View.OnClickListener {
            val intent= Intent(this, Lvl::class.java)
            intent.putExtra("UID",UID)
            intent.putExtra("NOM",NOM)
            intent.putExtra("PUNTUACIO",PUNTUACIO)
            intent.putExtra("NIVELL",NIVELL)
            intent.putExtra("LEVEL","3")
            startActivity(intent)
            finish()
        })

        if (NIVELL =="1") {
            imageButton1.isEnabled = true
            imageButton1.visibility = View.VISIBLE
        }

        if (NIVELL =="2") {
            Toast.makeText(this,"NIVELL 2", Toast.LENGTH_SHORT).show()
            imageButton1.isEnabled = true
            imageButton1.visibility = View.VISIBLE
            imageButton2.isEnabled = true
            imageButton2.visibility = View.VISIBLE
        }

        if (NIVELL =="3") {
            Toast.makeText(this,"NIVELL 3", Toast.LENGTH_SHORT).show()
            imageButton1.isEnabled = true
            imageButton1.visibility = View.VISIBLE
            imageButton2.isEnabled = true
            imageButton2.visibility = View.VISIBLE
            imageButton3.isEnabled = true
            imageButton3.visibility = View.VISIBLE
        }

}}

