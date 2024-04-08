package com.projecte.fruitcrush2

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.projecte.fruitcrush2.Fragment1
import com.projecte.fruitcrush2.Fragment2
import com.projecte.fruitcrush2.R
import kotlinx.coroutines.*

class CreditsMain : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.credits_main)

        // Mostrar Fragment1 inicialmente
        supportFragmentManager.beginTransaction()
            .replace(R.id.Fragment1, Fragment1(), "Fragment1")
            .commit()

        // Utilizar una corrutina para realizar la espera antes de mostrar Fragment2
        CoroutineScope(Dispatchers.Main).launch {
            delay(1000) // Espera de 1 segundo (corregido)

            // Ocultar Fragment1
            val fragment1 = supportFragmentManager.findFragmentByTag("Fragment1")
            fragment1?.let { supportFragmentManager.beginTransaction().hide(it).commit() }

            // Mostrar Fragment2
            supportFragmentManager.beginTransaction()
                .add(R.id.Fragment2, Fragment2(), "Fragment2")
                .addToBackStack(null)
                .commit()
        }
    }
}
