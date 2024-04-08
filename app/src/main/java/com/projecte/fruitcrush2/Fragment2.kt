package com.projecte.fruitcrush2

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button

class Fragment2 : Fragment() {
    private lateinit var view: View

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        view = inflater.inflate(R.layout.fragments2, container, false)

        // Configurar el OnClickListener para el botón
        view.findViewById<Button>(R.id.tornarInici).setOnClickListener {
            // Obtener la actividad actual
            val activity = activity
            if (activity != null) {
                // Mostrar la vista de la actividad principal
                activity.findViewById<View>(R.id.main)?.visibility = View.VISIBLE

                // Volver a la actividad principal (MainActivity)
                activity.finish()
            }
        }

        return view
    }
}
