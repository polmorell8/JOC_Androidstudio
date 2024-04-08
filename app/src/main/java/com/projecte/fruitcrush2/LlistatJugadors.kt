package com.projecte.fruitcrush2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage

class LlistatJugadors : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var databaseReference: DatabaseReference
    private lateinit var jugadors: MutableList<Jugador>
    private lateinit var jugadorsAdapter: JugadorsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.llistat_jugadors)

        recyclerView = findViewById(R.id.recycler)
        recyclerView.layoutManager = LinearLayoutManager(this)

        databaseReference = FirebaseDatabase.getInstance("https://fruitcrush-94085-default-rtdb.europe-west1.firebasedatabase.app/").reference.child("DATA BASE JUGADORS")
        if (databaseReference == null) {
            Log.d("LlistatJugadors", "onCreate: ${databaseReference.toString()}")
        }


        jugadors = mutableListOf()
        loadDataFromFirebase()
        Log.d("LlistatJugadors", "onCreate: ${jugadors.size}")

        jugadorsAdapter = JugadorsAdapter(jugadors)
        recyclerView.adapter = jugadorsAdapter
    }

    private fun loadDataFromFirebase() {
        databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (childSnapshot in dataSnapshot.children) {
                    var nom = childSnapshot.child("nom").value.toString()
                    var puntuacio = childSnapshot.child("puntuacio").value.toString()
                    var imatge = childSnapshot.child("Imatge").value.toString()
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle potential errors here
            }
    })
}}
