package com.example.placartenismesa

import adapters.CustomAdapter
import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import data.Constantes
import data.Placar
import java.io.ByteArrayInputStream
import java.io.ObjectInputStream

class HistoricoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_historico)
        supportActionBar?.setTitle("HISTÓRICO")

        // getting the recyclerview by its id
        val recyclerview = findViewById<RecyclerView>(R.id.rvHistorico)

        // this creates a vertical layout Manager
        recyclerview.layoutManager = LinearLayoutManager(this)

        // ArrayList of class ItemsViewModel
        val data = ArrayList<Placar>()

        // This loop will create 20 Views containing
        // the image with the count of view

        // ------------ PEGA OS ULTIMOS ------------

        val sp: SharedPreferences = getSharedPreferences(Constantes.HISTORICO, Context.MODE_PRIVATE)

        var resultAnterior:Boolean = sp.getBoolean(Constantes.TEM_RESULTADO_ANTERIOR, false)

        if (resultAnterior){
            var obj_vetor:String = sp.getString(Constantes.VETOR_NOMES_PLACAR, "").toString()

            if (obj_vetor.length >= 1) {
                var dis = ByteArrayInputStream(obj_vetor.toByteArray(Charsets.ISO_8859_1))
                var oos = ObjectInputStream(dis)
                var nomes_placar: ArrayList<String> = oos.readObject() as ArrayList<String>

                for (i in 0..nomes_placar.size-1) {

                    Log.v("SMDDEBUG", i.toString())

                    var obj_placar:String = sp.getString(nomes_placar.get(i), "").toString()

                    if (obj_placar.length >= 1) {
                        var dis = ByteArrayInputStream(obj_placar.toByteArray(Charsets.ISO_8859_1))
                        var oos = ObjectInputStream(dis)
                        var placar: Placar = oos.readObject() as Placar

                        data.add(placar)
                    }

                }


            }

        } else {
            setContentView(R.layout.activity_historico_vazio)

        }

        // This will pass the ArrayList to our Adapter
        val adapter = CustomAdapter(data)

        // Setting the Adapter with the recyclerview
        recyclerview.adapter = adapter

    }
}

