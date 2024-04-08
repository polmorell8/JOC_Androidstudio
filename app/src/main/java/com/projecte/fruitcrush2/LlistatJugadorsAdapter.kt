package com.projecte.fruitcrush2

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.squareup.picasso.Picasso

class JugadorsViewHolder (view: View):RecyclerView.ViewHolder(view) {
    //afegim les variables que apunten als continguts del layout
    val nomJugador=view.findViewById<TextView>(R.id.usuari)
    val puntuacioJugador=view.findViewById<TextView>(R.id.puntuacio)
    val foto=view.findViewById<ImageView>(R.id.imatgeJugador)

    fun render(jugadorModel: Jugador){
        //la cridara per a cada jugador
        nomJugador.text=jugadorModel.nom_jugador
        puntuacioJugador.text=jugadorModel.puntuacio.toString() //recordem que és un int
        //utilitzant picasso: https://rubentejera.com/picasso-libreria-gestion-imagenes-para-android/
        Picasso.get().load(jugadorModel.foto).resize(150,150).into(foto)
        //aqui agafarem quan pica sobre una imatge
        foto.setOnClickListener(){
            Toast.makeText(foto.context, nomJugador.text,Toast.LENGTH_LONG).show()
        }
    }
}

class JugadorsAdapter(val jugadors:List<Jugador>): RecyclerView.Adapter<JugadorsViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JugadorsViewHolder {
        //infla el layout i crida al Jugadors View Holder
        val layoutInflater=LayoutInflater.from(parent.context)
        return JugadorsViewHolder(layoutInflater.inflate(R.layout.jugador, parent, false))
    }
    override fun getItemCount(): Int {
        //retorna el tamany del la taula
        return jugadors.size
    }
    override fun onBindViewHolder(holder: JugadorsViewHolder, position: Int) {
        //aquest mètode és que va passant per cada un dels items i crida al render
        val item=jugadors[position]
        holder.render(item)
    }
}
