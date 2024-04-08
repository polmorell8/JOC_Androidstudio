package com.projecte.fruitcrush2

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.forEach
import androidx.lifecycle.lifecycleScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.projecte.fruitcrush2.databinding.LvlBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

class Lvl : AppCompatActivity(),OnClickListener {

    private lateinit var binding: LvlBinding
    private var score = 0
    private var result : String = ""
    private var userAnswer : String = ""
    private lateinit var layout_menu : LinearLayout
    private lateinit var botoSi : Button

    private var NOM: String =""
    private var PUNTUACIO: String=""
    private var UID: String=""
    private var NIVELL: String=""
    private var NEXT_LVL: String=""

    private var delay = 0
    private var min = 0
    private var max = 0

    lateinit var auth: FirebaseAuth
    var user: FirebaseUser? = null;


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //enableEdgeToEdge()
        auth = FirebaseAuth.getInstance()
        user = auth.currentUser
        binding = LvlBinding.inflate(layoutInflater)
        setContentView(binding.root)
        layout_menu = findViewById(R.id.container)
        botoSi = findViewById(R.id.boto_si)

        layout_menu.visibility = View.INVISIBLE

        var intent:Bundle? = getIntent().extras
        UID = intent?.getString("UID").toString()
        NOM = intent?.getString("NOM").toString()
        PUNTUACIO = intent?.getString("PUNTUACIO").toString()
        NIVELL = intent?.getString("NIVELL").toString()

        if (NIVELL =="1"){
            delay = 600
            min = 2
            max = 3
            NEXT_LVL = "2"
        }
        else if (NIVELL =="2"){
            delay = 400
            min = 3
            max = 4
            NEXT_LVL = "3"
        }
        else if (NIVELL =="3"){
            delay = 200
            min = 4
            max = 5
            NEXT_LVL = "3"
        }


        binding.apply {
            panel1.setOnClickListener(this@Lvl)
            panel2.setOnClickListener(this@Lvl)
            panel3.setOnClickListener(this@Lvl)
            panel4.setOnClickListener(this@Lvl)
            iniciJoc()
        }

    }


    private fun desactivarBotons(){
        binding.root.forEach { view ->
            if (view is Button)
            {
                view.isEnabled = false
            }
        }
    }
    private fun activarBotons(){
        binding.root.forEach { view ->
            if (view is Button)
            {
                view.isEnabled = true
            }
        }
    }

    private fun iniciJoc(){
        layout_menu.visibility = View.INVISIBLE

        result = ""
        userAnswer = ""
        desactivarBotons()
        lifecycleScope.launch {
            val round = Random.nextInt(min,max)
            repeat(round){
                delay(delay.toLong())
                val randomPanel = Random.nextInt(1,5)
                result += randomPanel
                val panel = when(randomPanel){
                    1 -> binding.panel1
                    2 -> binding.panel2
                    3 -> binding.panel3
                    else -> binding.panel4
                }
                val drawableYellow = ActivityCompat.getDrawable(this@Lvl, R.drawable.btn_yellow)
                val drawableDefault = ActivityCompat.getDrawable(this@Lvl, R.drawable.btn_state)
                panel.background = drawableYellow
                delay (delay.toLong())
                panel.background = drawableDefault
            }
            activarBotons()
        }
    }
    private fun animacioGuanyat()
    {
        val botonPanel1 = findViewById<Button>(R.id.panel1)
        val botonPanel2 = findViewById<Button>(R.id.panel2)
        val botonPanel3 = findViewById<Button>(R.id.panel3)
        val botonPanel4 = findViewById<Button>(R.id.panel4)

        botonPanel1.isEnabled = false
        botonPanel2.isEnabled = false
        botonPanel3.isEnabled = false
        botonPanel4.isEnabled = false

        var database: FirebaseDatabase = FirebaseDatabase.getInstance("https://fruitcrush-94085-default-rtdb.europe-west1.firebasedatabase.app/")
        var reference: DatabaseReference = database.getReference("DATA BASE JUGADORS")
        if(reference!=null) {
            if (score > PUNTUACIO.toInt())
                reference.child(UID).child("Puntuacio").setValue(score)
            reference.child(UID).child("Nivell").setValue(NEXT_LVL)
            Toast.makeText(this, "Puntuació guardada",
                Toast.LENGTH_SHORT).show()
        }
        else{
            Toast.makeText(this, "ERROR BD", Toast.LENGTH_SHORT).show()
        }

        val drawableWin = ActivityCompat.getDrawable(this@Lvl, R.drawable.btn_yellow)
        binding.root.forEach { view ->
            if (view is Button){
                view.background = drawableWin
            }
        }
        layout_menu.visibility = View.VISIBLE



        botoSi.setOnClickListener {
            val intent = Intent(this@Lvl, Menu::class.java)
            startActivity(intent)
            finish()
        }


    }

    private fun animacioPerdut()
    {
        binding.apply {
            score = 0
            tvScore.text = "0"
            desactivarBotons()
            val drawableLose = ActivityCompat.getDrawable(this@Lvl, R.drawable.btn_lose)
            val drawableDefault = ActivityCompat.getDrawable(this@Lvl, R.drawable.btn_state)
            lifecycleScope.launch {
                binding.root.forEach { view ->
                    if (view is Button){
                        view.background = drawableLose
                        delay(300)
                        view.background = drawableDefault
                    }
                }
                binding.root.forEach { view ->
                    if (view is Button){
                        view.background = drawableLose
                    }
                }
                delay(500)
                binding.root.forEach { view ->
                    if (view is Button){
                        view.background = drawableDefault
                    }
                }
                delay(1000)

                iniciJoc()

            }
        }
    }
    override fun onClick(view: View?) {
        view?.let{
            userAnswer +=  when(it.id)
            {
                R.id.panel1 -> "1"
                R.id.panel2 -> "2"
                R.id.panel3 -> "3"
                R.id.panel4 -> "4"
                else -> ""
            }
            if (userAnswer == result){
                score++
                binding.tvScore.text = score.toString()
                iniciJoc()
            }
            else if (userAnswer.length >= result.length){
                animacioPerdut()
            }

            if (score == 5) {
                animacioGuanyat()
            }
        }
    }
}