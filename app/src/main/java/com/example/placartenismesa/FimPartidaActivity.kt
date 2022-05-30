package com.example.placartenismesa

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import data.Constantes
import data.Jogador
import data.Placar

class FimPartidaActivity : AppCompatActivity() {
    lateinit var placar: Placar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fim_partida)
        supportActionBar?.hide()

        placar = getIntent().getExtras()?.getSerializable(Constantes.PLACAR) as Placar


        var ganhador = placar.jogador_1
        if (placar.ganhador == Constantes.JOGADOR_2) ganhador = placar.jogador_2

        val tvGanhador = findViewById<TextView>(R.id.tvGanhador)
        tvGanhador.setText(ganhador.nome)

        val tvResultado = findViewById<TextView>(R.id.tvResultado)
        val text = ganhador.cont_set_ganho.toString() + " X " + (placar.set_atual - ganhador.cont_set_ganho).toString()
        tvResultado.setText(text)

    }

    fun voltarInicio(v: View) {
        val intent = Intent(this, MainActivity::class.java).apply {}
        startActivity(intent)
    }
}