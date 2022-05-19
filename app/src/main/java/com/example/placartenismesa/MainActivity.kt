package com.example.placartenismesa

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btConfiguracoes = findViewById<Button>(R.id.btConfiguracoes)

        btConfiguracoes.setOnClickListener {
            val janelaConfiguracoes = Intent(this, Configuracoes::class.java).apply {  }
            startActivity(janelaConfiguracoes)
        }

        val btPlacar = findViewById<Button>(R.id.btPlacar)

        btPlacar.setOnClickListener {
            val janelaPlacar = Intent(this, Placar::class.java).apply {  }
            startActivity(janelaPlacar)
        }
    }
}