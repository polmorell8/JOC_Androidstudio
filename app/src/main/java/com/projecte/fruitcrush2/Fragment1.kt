package com.projecte.fruitcrush2

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

class Fragment1 : Fragment() {
    private lateinit var view: View

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val layout = inflater.inflate(R.layout.fragments1, container, false)
        layout?.let {
            view = it
            return view
        }
        // Manejar el caso en el que el layout sea nulo
        // Puedes devolver otro layout o null según sea necesario
        return null
    }
}
