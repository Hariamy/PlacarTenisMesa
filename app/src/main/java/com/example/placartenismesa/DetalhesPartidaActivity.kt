package com.example.placartenismesa

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import data.Configuracoes
import data.Constantes
import data.Placar

class DetalhesPartidaActivity : AppCompatActivity() {
    lateinit var placar: Placar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalhes_partida)
        supportActionBar?.setTitle("DETALHES")

        placar = getIntent().getExtras()?.getSerializable(Constantes.PLACAR) as Placar

        var ganhador = placar.jogador_1
        var perdedor = placar.jogador_2
        if (placar.ganhador == Constantes.JOGADOR_2) {
            ganhador = placar.jogador_2
            perdedor = placar.jogador_1
        }

        val tvPartida = findViewById<TextView>(R.id.tvPartidaDetalhes)
        val tvDescricao = findViewById<TextView>(R.id.tvDescricaoDetalhes)
        val tvGanhador = findViewById<TextView>(R.id.tvGanhadorDetalhes)
        val tvPerdedor = findViewById<TextView>(R.id.tvPerdedorDetalhes)
        val tvPlacar = findViewById<TextView>(R.id.tvPlacarDetalhes)
        val tvGanhadorNomePontos = findViewById<TextView>(R.id.tvGanhadorNomeDetalhes)
        val tvGanhadorPontos = findViewById<TextView>(R.id.tvGanhadorPontosDetalhes)
        val tvPerdedorNomePontos = findViewById<TextView>(R.id.tvPerdedorNomesDetalhes)
        val tvPerdedorPontos = findViewById<TextView>(R.id.tvPerdedorPontosDetalhes)
        val tvSets = findViewById<TextView>(R.id.tvSetDetalhes)
        val tvSetsTotais = findViewById<TextView>(R.id.tvSetsTotaisDetalhes)

        tvPartida.text = placar.nome_partida
        tvDescricao.text = placar.descricao
        tvGanhador.text = ganhador.nome
        tvPerdedor.text = perdedor.nome
        tvPlacar.text = ganhador.cont_set_ganho.toString() +" X "+ perdedor.cont_set_ganho.toString()
        tvGanhadorNomePontos.text = ganhador.nome
        tvPerdedorNomePontos.text = perdedor.nome
        tvSetsTotais.text = placar.sets.toString()

        var pontosGanhador = ""
        var setDetalhes = ""
        var pontosPerdedor = ""

        for (i in 0..placar.set_atual-1) {
            setDetalhes += "Set "+(i+1).toString() + "\n"
            pontosGanhador += ganhador.pontos.get(i).toString() + "\n"
            pontosPerdedor +=  perdedor.pontos.get(i).toString() + "\n"
        }

        tvGanhadorPontos.text = pontosGanhador
        tvPerdedorPontos.text = pontosPerdedor
        tvSets.text = setDetalhes

    }
}