package com.example.placartenismesa

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val btPlacar = findViewById<Button>(R.id.btPlacar)

        btPlacar.setOnClickListener {
            val janelaPlacar = Intent(this, ConfiguracoesActivity::class.java).apply {  }
            startActivity(janelaPlacar)
        }

        val btHistorico = findViewById<Button>(R.id.btHistorico)
            btHistorico.setOnClickListener {
            val intent = Intent(this, HistoricoActivity::class.java).apply {}
            startActivity(intent)
        }
    }

    fun abrirHistorico(v: View) {
        val intent = Intent(this, HistoricoActivity::class.java).apply {}
        startActivity(intent)
    }
}