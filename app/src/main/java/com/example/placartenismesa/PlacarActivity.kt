package com.example.placartenismesa

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import data.*
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.nio.charset.StandardCharsets

class PlacarActivity : AppCompatActivity() {
    lateinit var placar:Placar
    lateinit var configuracoes: Configuracoes

    var saque = Constantes.INDEFINIDO
    var pontosMarcados = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_placar)
        supportActionBar?.hide()

        configuracoes = getIntent().getExtras()?.getSerializable(Constantes.CONFIGURACOES) as Configuracoes

        // Seta as variáveis iniciais
        initPlacar()

    }


    fun initPlacar(){

        val jogador1 = Jogador(configuracoes.nome_jogador_1, mutableListOf(0, 0, 0, 0, 0, 0, 0, 0, 0), 0, configuracoes.lado_inicial_jogador1)
        val jogador2 = Jogador(configuracoes.nome_jogador_2, mutableListOf(0, 0, 0, 0, 0, 0, 0, 0, 0), 0, configuracoes.lado_inicial_jogador2)

        placar = Placar(configuracoes.nome_partida,configuracoes.descricao, jogador1, jogador2, 1, configuracoes.inicia_sacando)

        saque = configuracoes.inicia_sacando

        configInterface()

    }

    fun configInterface() {
        val tvJogadorEsquerda = findViewById<TextView>(R.id.tvJogadorEsquerda)
        val tvJogadorDireita = findViewById<TextView>(R.id.tvJogadorDireita)
        val tvSaqueDireita = findViewById<TextView>(R.id.tvSaqueDireita)
        val tvSaqueEsquerda = findViewById<TextView>(R.id.tvSaqueEsquerda)
        val tvPlacarDireita = findViewById<TextView>(R.id.tvPlacarDireita)
        val tvPlacarEsquerda = findViewById<TextView>(R.id.tvPlacarEsquerda)
        val tvSetJogadorEsquerda = findViewById<TextView>(R.id.tvSetJogadorEsquerda)
        val tvSetJogadorDireita = findViewById<TextView>(R.id.tvSetJogadorDireita)

        // --------- ZERA O PLACAR --------- //
        tvPlacarDireita.text = "0"
        tvPlacarEsquerda.text = "0"

        // --------- SETA OS NOMES DE CADA JOGADOR --------- //
        if (placar.jogador_1.lado_mesa == Constantes.ESQUERDA) {
            tvJogadorEsquerda.setText(placar.jogador_1.nome)
            tvJogadorDireita.setText(placar.jogador_2.nome)
        } else {
            tvJogadorEsquerda.setText(placar.jogador_2.nome)
            tvJogadorDireita.setText(placar.jogador_1.nome)
        }

        // --------- CONFIGURA QUEM DEVE SACAR --------- //
        if ((configuracoes.inicia_sacando == Constantes.JOGADOR_1 && placar.jogador_1.lado_mesa == Constantes.ESQUERDA) ||
            (configuracoes.inicia_sacando == Constantes.JOGADOR_2 && placar.jogador_2.lado_mesa == Constantes.ESQUERDA)) {

            tvSaqueDireita.text = ""
            tvSaqueEsquerda.text = "DEVE SACAR"

        } else {

            tvSaqueDireita.text = "DEVE SACAR"
            tvSaqueEsquerda.text = ""

        }

        // --------- CONFIGURA QUANTOS SETS GANHOU --------- //
        if (placar.jogador_1.lado_mesa == Constantes.ESQUERDA) {
            tvSetJogadorEsquerda.setText(placar.jogador_1.cont_set_ganho.toString())
            tvSetJogadorDireita.setText(placar.jogador_2.cont_set_ganho.toString())
        } else {
            tvSetJogadorEsquerda.setText(placar.jogador_2.cont_set_ganho.toString())
            tvSetJogadorDireita.setText(placar.jogador_1.cont_set_ganho.toString())
        }

    }

    fun checkGanhador(index: Int) {
        var houve_ganhador = false

        if (placar.jogador_1.pontos.get(index) == 11) {
            placar.jogador_1.cont_set_ganho += 1
            houve_ganhador = true

        } else if (placar.jogador_2.pontos.get(index) == 11) {
            placar.jogador_2.cont_set_ganho += 1
            houve_ganhador = true

        }

        if (houve_ganhador) {

            if (checkFimPartida()) {

                salvaResultado()
                val intent = Intent(this, FimPartidaActivity::class.java).apply {
                    putExtra(Constantes.PLACAR, placar)
                }
                startActivity(intent)

            } else {

                placar.set_atual += 1

                setContentView(R.layout.activity_placar_intervalo)
            }
        }
    }

    fun salvaResultado() {
        val sp:SharedPreferences = getSharedPreferences(Constantes.HISTORICO, Context.MODE_PRIVATE)
        var edShared = sp.edit()

        var resultAnterior:Boolean = sp.getBoolean(Constantes.TEM_RESULTADO_ANTERIOR, false)

        var list_nomes = ArrayList<String>()
        var nome_placar = ""

        if (resultAnterior){
            var obj_vetor:String = sp.getString(Constantes.VETOR_NOMES_PLACAR, "").toString()

            if (obj_vetor.length >= 1) {
                var dis = ByteArrayInputStream(obj_vetor.toByteArray(Charsets.ISO_8859_1))
                var oos = ObjectInputStream(dis)
                var list_nomes_antiga:ArrayList<String> = oos.readObject() as ArrayList<String>

                if (list_nomes_antiga.size == 5) {
                    nome_placar = list_nomes_antiga.get(0)
                    for (i in 0..list_nomes_antiga.size - 2) {
                        list_nomes.add(list_nomes_antiga.get(i + 1))
                    }
                    list_nomes.add(nome_placar)

                } else {
                    nome_placar = Constantes.PLACAR + list_nomes_antiga.size.toString()
                    list_nomes_antiga.add(nome_placar)
                    list_nomes = list_nomes_antiga
                }
            }

        } else {
            nome_placar = Constantes.PLACAR + "0"
            list_nomes.add(nome_placar)

            edShared.putBoolean(Constantes.TEM_RESULTADO_ANTERIOR, true)
        }

        var dt = ByteArrayOutputStream()
        var oos = ObjectOutputStream(dt)

        oos.writeObject(list_nomes)

        edShared.putString(Constantes.VETOR_NOMES_PLACAR, dt.toString(StandardCharsets.ISO_8859_1.name()))

        var dt2 = ByteArrayOutputStream()
        var oos2 = ObjectOutputStream(dt2)
        oos2.writeObject(placar)

        edShared.putString(nome_placar, dt2.toString(StandardCharsets.ISO_8859_1.name()))
        edShared.commit()

    }

    fun checkFimPartida():Boolean {
        val sets_jogador_1 = placar.jogador_1.cont_set_ganho
        val sets_jogador_2 = placar.jogador_2.cont_set_ganho

        val setAtual = placar.set_atual

        Log.v("SMD", "set: "+ setAtual.toString() +"  " +sets_jogador_1.toString() + "  " + sets_jogador_2.toString())

        if (setAtual == configuracoes.sets) {
            if (sets_jogador_1 > sets_jogador_2 ){
                placar.ganhador = Constantes.JOGADOR_1
                return true
            } else {
                placar.ganhador = Constantes.JOGADOR_2
                return true
            }
        }
        return false
    }

    fun addPlacarEsquerda(v: View) {
        pontosMarcados += 1
        setSaque()

        val tvPlacarEsquerda = findViewById<TextView>(R.id.tvPlacarEsquerda)
        val index = placar.set_atual-1

        if (placar.jogador_1.lado_mesa == Constantes.ESQUERDA) {
            val valor = placar.jogador_1.pontos.get(index) + 1
            placar.jogador_1.pontos.set(index,  valor)
            tvPlacarEsquerda.text = valor.toString()

        } else {
            val valor = placar.jogador_2.pontos.get(index) + 1
            placar.jogador_2.pontos.set(index,  valor)
            tvPlacarEsquerda.text = valor.toString()
        }

        checkGanhador(index)
    }

    fun addPlacarDireita(v: View) {
        pontosMarcados += 1
        setSaque()

        val tvPlacarDireita = findViewById<TextView>(R.id.tvPlacarDireita)
        val index = placar.set_atual-1

        if (placar.jogador_1.lado_mesa == Constantes.DIREITA) {
            val valor = placar.jogador_1.pontos.get(index) + 1
            placar.jogador_1.pontos.set(index,  valor)
            tvPlacarDireita.text = valor.toString()

        } else {
            val valor = placar.jogador_2.pontos.get(index) + 1
            placar.jogador_2.pontos.set(index,  valor)
            tvPlacarDireita.text = valor.toString()
        }

        checkGanhador(index)
    }

    fun setSaque() {
        if (pontosMarcados%2 == 0) {
            val tvSaqueDireita = findViewById<TextView>(R.id.tvSaqueDireita)
            val tvSaqueEsquerda = findViewById<TextView>(R.id.tvSaqueEsquerda)

            if (saque == Constantes.JOGADOR_1) {
                saque = Constantes.JOGADOR_2
                if (placar.jogador_1.lado_mesa == Constantes.ESQUERDA) {
                    tvSaqueDireita.text = "DEVE SACAR"
                    tvSaqueEsquerda.text = ""

                } else {
                    tvSaqueDireita.text = ""
                    tvSaqueEsquerda.text = "DEVE SACAR"
                }
            } else {
                saque = Constantes.JOGADOR_1
                if (placar.jogador_2.lado_mesa == Constantes.ESQUERDA) {
                    tvSaqueDireita.text = "DEVE SACAR"
                    tvSaqueEsquerda.text = ""

                } else {
                    tvSaqueDireita.text = ""
                    tvSaqueEsquerda.text = "DEVE SACAR"
                }
            }
        }
    }

    fun voltaIntervalo(v:View) {
        setContentView(R.layout.activity_placar)

        if (configuracoes.inicia_sacando == Constantes.JOGADOR_1) {
            configuracoes.inicia_sacando = Constantes.JOGADOR_2
        } else {
            configuracoes.inicia_sacando = Constantes.JOGADOR_1
        }

        saque = configuracoes.inicia_sacando

        val lado_jogador_1 = placar.jogador_1.lado_mesa
        placar.jogador_1.lado_mesa = placar.jogador_2.lado_mesa
        placar.jogador_2.lado_mesa = lado_jogador_1

        configInterface()
    }
}