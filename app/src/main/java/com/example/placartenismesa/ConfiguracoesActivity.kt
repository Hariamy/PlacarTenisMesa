package com.example.placartenismesa


import android.content.Context
import android.content.Intent
import android.content.SharedPreferences

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.core.widget.addTextChangedListener
import data.Configuracoes
import data.Constantes
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.nio.charset.StandardCharsets

class ConfiguracoesActivity : AppCompatActivity() {

    lateinit var configuracoes:Configuracoes

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_configuracoes)
        supportActionBar?.setTitle("CONFIGURAÇÕES")

        openConfig()
        setChangeListeners()
        initInterface()

    }
    fun setChangeListeners() {
        val etNomeJoador1 = findViewById<EditText>(R.id.etNomeJoador1)
        etNomeJoador1.addTextChangedListener{
            configuracoes.nome_jogador_1 = etNomeJoador1.text.toString()
        }

        val etNomeJoador2 = findViewById<EditText>(R.id.etNomeJogador2)
        etNomeJoador2.addTextChangedListener {
            configuracoes.nome_jogador_2 = etNomeJoador2.text.toString()
        }

        val etNomePartida = findViewById<EditText>(R.id.etNamePartida)
        etNomePartida.addTextChangedListener{
            configuracoes.nome_partida = etNomePartida.text.toString()
        }

        val etDescricao = findViewById<EditText>(R.id.etDescricao)
        etDescricao.addTextChangedListener{
            configuracoes.descricao = etDescricao.text.toString()
        }


        // -------------- CONGIGURA OS BOTOES DE SELEÇÃO DE QUEM INICIA SACANDO -------------- //

        val bcIniciaSaqueJodador1 = findViewById<CheckBox>(R.id.bcIniciaSaqueJodador1)
        val bcIniciaSaqueJodador2 = findViewById<CheckBox>(R.id.bcIniciaSaqueJodador2)

        bcIniciaSaqueJodador1.setOnCheckedChangeListener{ buttonView, isChecked ->
            if (isChecked) {
                bcIniciaSaqueJodador2.isChecked = false
                configuracoes.inicia_sacando = Constantes.JOGADOR_1
            }
        }

        bcIniciaSaqueJodador2.setOnCheckedChangeListener{ buttonView, isChecked ->
            if (isChecked) {
                bcIniciaSaqueJodador1.isChecked = false
                configuracoes.inicia_sacando = Constantes.JOGADOR_2
            }
        }

        // -------------- CONGIGURA OS BOTOES DE SELEÇÃO DE QUEM É O JOGADOR DA ESQUERDA -------------- //
        val cbIniciaEsquerdaJogador1 = findViewById<CheckBox>(R.id.cbIniciaEsquerdaJogador1)
        val cbIniciaEsquerdaJogador2 = findViewById<CheckBox>(R.id.cbIniciaEsquerdaJogador2)

        cbIniciaEsquerdaJogador1.setOnCheckedChangeListener{ buttonView, isChecked ->
            if (isChecked) {
                cbIniciaEsquerdaJogador2.isChecked = false
                configuracoes.lado_inicial_jogador1 = Constantes.ESQUERDA
                configuracoes.lado_inicial_jogador2 = Constantes.DIREITA

            }
        }

        cbIniciaEsquerdaJogador2.setOnCheckedChangeListener{ buttonView, isChecked ->
            if (isChecked) {
                cbIniciaEsquerdaJogador1.isChecked = false
                configuracoes.lado_inicial_jogador2 = Constantes.ESQUERDA
                configuracoes.lado_inicial_jogador1 = Constantes.DIREITA
            }
        }

        // -------------- CONGIGURA O ADAPTER DA QUANTIDADE DE SETS -------------- //

        val spQuantSets = findViewById<Spinner>(R.id.spQuantSets)
        ArrayAdapter.createFromResource(
            this,
            R.array.app_config_quant_sets,
            android.R.layout.simple_spinner_dropdown_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spQuantSets.adapter = adapter
        }

    }

    fun saveConfig() {
        val sp: SharedPreferences = getSharedPreferences(Constantes.CONFIGURACOES, Context.MODE_PRIVATE)
        var edShared = sp.edit()

        var dt = ByteArrayOutputStream()
        var oos = ObjectOutputStream(dt)
        oos.writeObject(configuracoes)

        edShared.putString(Constantes.CONFIGURACOES, dt.toString(StandardCharsets.ISO_8859_1.name()))
        edShared.commit()

    }

    fun openConfig() {
        val sp: SharedPreferences = getSharedPreferences(Constantes.CONFIGURACOES, Context.MODE_PRIVATE)

        var objString:String = sp.getString(Constantes.CONFIGURACOES, "").toString()

        if (objString.length >= 1) {
            var dis = ByteArrayInputStream(objString.toByteArray(Charsets.ISO_8859_1))
            var oos = ObjectInputStream(dis)
            configuracoes = oos.readObject() as Configuracoes
            Log.v("SMD", configuracoes.nome_jogador_1)
        } else {
            configuracoes = Configuracoes("", "","", "", Constantes.INDEFINIDO, Constantes.INDEFINIDO, Constantes.INDEFINIDO, Constantes.INDEFINIDO )
        }

    }
    fun initInterface(){
        val etNomeJoador1 = findViewById<EditText>(R.id.etNomeJoador1)
        etNomeJoador1.setText(configuracoes.nome_jogador_1)

        val etNomeJoador2 = findViewById<EditText>(R.id.etNomeJogador2)
        etNomeJoador2.setText(configuracoes.nome_jogador_2)

        val etNomePartida = findViewById<EditText>(R.id.etNamePartida)
        etNomePartida.setText(configuracoes.nome_partida)

        val etDescricao = findViewById<EditText>(R.id.etDescricao)
        etDescricao.setText(configuracoes.descricao)

        // -------------- CONGIGURA OS BOTOES DE SELEÇÃO DE QUEM INICIA SACANDO -------------- //

        val bcIniciaSaqueJodador1 = findViewById<CheckBox>(R.id.bcIniciaSaqueJodador1)
        val bcIniciaSaqueJodador2 = findViewById<CheckBox>(R.id.bcIniciaSaqueJodador2)

        if (configuracoes.inicia_sacando == Constantes.JOGADOR_1) {
            bcIniciaSaqueJodador1.isChecked = true
        } else if (configuracoes.inicia_sacando == Constantes.JOGADOR_2){
            bcIniciaSaqueJodador2.isChecked = true
        }


        // -------------- CONGIGURA OS BOTOES DE SELEÇÃO DE QUEM É O JOGADOR DA ESQUERDA -------------- //
        val cbIniciaEsquerdaJogador1 = findViewById<CheckBox>(R.id.cbIniciaEsquerdaJogador1)
        val cbIniciaEsquerdaJogador2 = findViewById<CheckBox>(R.id.cbIniciaEsquerdaJogador2)

        if (configuracoes.lado_inicial_jogador1 == Constantes.ESQUERDA) {
            cbIniciaEsquerdaJogador1.isChecked = true

        } else if (configuracoes.lado_inicial_jogador2 == Constantes.ESQUERDA){
            cbIniciaEsquerdaJogador2.isChecked = true
        }

        // -------------- CONGIGURA O ADAPTER DA QUANTIDADE DE SETS -------------- //
        if (configuracoes.sets != Constantes.INDEFINIDO) {
            val spQuantSets = findViewById<Spinner>(R.id.spQuantSets)
            spQuantSets.setSelection((configuracoes.sets-1) / 2)
        }

    }

    fun iniciarPlacar(v: View) {
        val spQuantSets = findViewById<Spinner>(R.id.spQuantSets)
        configuracoes.sets = spQuantSets.selectedItem.toString().toInt()
        Log.v("SMD", configuracoes.sets.toString())
        saveConfig()
        val janelaPlacar = Intent(this, PlacarActivity::class.java).apply {
            putExtra(Constantes.CONFIGURACOES, configuracoes)
        }
        startActivity(janelaPlacar)
    }
}