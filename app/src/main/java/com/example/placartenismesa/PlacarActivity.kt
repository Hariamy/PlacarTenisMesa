package com.example.placartenismesa

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.*
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import android.widget.Button
import android.widget.Chronometer
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.content.getSystemService
import data.Configuracoes
import data.Constantes
import data.Jogador
import data.Placar
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.nio.charset.StandardCharsets

class PlacarActivity : AppCompatActivity() {
    lateinit var placar:Placar
    lateinit var configuracoes: Configuracoes

    var placaresAnteriores = ArrayList<Placar>()

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

        placar = Placar(configuracoes.nome_partida,configuracoes.descricao, jogador1, jogador2, 1, configuracoes.inicia_sacando, configuracoes.sets, configuracoes.inicia_sacando, 0)
        salvaPlacar()
        configInterface()

    }

    fun configInterface() {
        val tvJogadorEsquerda = findViewById<TextView>(R.id.tvJogadorEsquerda)
        val tvJogadorDireita = findViewById<TextView>(R.id.tvJogadorDireita)
        val ivDeveSacarDireita = findViewById<ImageView>(R.id.ivDeveSacarDireita)
        val ivDeveSacarEsquerda = findViewById<ImageView>(R.id.ivDeveSacarEsquerda)
        val tvPlacarDireita = findViewById<TextView>(R.id.tvPlacarDireita)
        val tvPlacarEsquerda = findViewById<TextView>(R.id.tvPlacarEsquerda)
        val tvSetJogadorEsquerda = findViewById<TextView>(R.id.tvSetJogadorEsquerda)
        val tvSetJogadorDireita = findViewById<TextView>(R.id.tvSetJogadorDireita)
        val btDesfazer = findViewById<Button>(R.id.btDesfazerJodada)


        // --------- SETA OS NOMES DE CADA JOGADOR E PLACAR--------- //
        if (placar.jogador_1.lado_mesa == Constantes.ESQUERDA) {
            tvJogadorEsquerda.setText(placar.jogador_1.nome)
            tvPlacarEsquerda.text = placar.jogador_1.pontos.get(placar.set_atual-1).toString()

            tvJogadorDireita.setText(placar.jogador_2.nome)
            tvPlacarDireita.text = placar.jogador_2.pontos.get(placar.set_atual-1).toString()
        } else {
            tvJogadorEsquerda.setText(placar.jogador_2.nome)
            tvPlacarEsquerda.text = placar.jogador_2.pontos.get(placar.set_atual-1).toString()

            tvJogadorDireita.setText(placar.jogador_1.nome)
            tvPlacarDireita.text = placar.jogador_1.pontos.get(placar.set_atual-1).toString()

        }

        // --------- CONFIGURA QUEM DEVE SACAR --------- //
        if ((placar.saque == Constantes.JOGADOR_1 && placar.jogador_1.lado_mesa == Constantes.ESQUERDA) ||
            (placar.saque == Constantes.JOGADOR_2 && placar.jogador_2.lado_mesa == Constantes.ESQUERDA)) {

            ivDeveSacarDireita.visibility = View.INVISIBLE
            ivDeveSacarEsquerda.visibility = View.VISIBLE

        } else {
            ivDeveSacarDireita.visibility = View.VISIBLE
            ivDeveSacarEsquerda.visibility = View.INVISIBLE

        }

        // --------- CONFIGURA QUANTOS SETS GANHOU --------- //
        if (placar.jogador_1.lado_mesa == Constantes.ESQUERDA) {
            tvSetJogadorEsquerda.setText(placar.jogador_1.cont_set_ganho.toString())
            tvSetJogadorDireita.setText(placar.jogador_2.cont_set_ganho.toString())
        } else {
            tvSetJogadorEsquerda.setText(placar.jogador_2.cont_set_ganho.toString())
            tvSetJogadorDireita.setText(placar.jogador_1.cont_set_ganho.toString())
        }


        btDesfazer.visibility = View.INVISIBLE

        if (placaresAnteriores.size > 1) {
            btDesfazer.visibility = View.VISIBLE
        }
    }

    fun copyJogador(j: Jogador): Jogador {
        return Jogador(
            j.nome,
            mutableListOf(
                j.pontos.get(0),
                j.pontos.get(1),
                j.pontos.get(2),
                j.pontos.get(3),
                j.pontos.get(4),
                j.pontos.get(5),
                j.pontos.get(6),
                j.pontos.get(7),
                j.pontos.get(8)
            ),
            j.cont_set_ganho,
            j.lado_mesa
        )
    }
    fun copyPlacar(p: Placar) : Placar {
        val novoPlacar =  Placar(
            p.nome_partida,
            p.descricao,
            copyJogador(p.jogador_1),
            copyJogador(p.jogador_2),
            p.set_atual, p.ganhador, p.sets, p.saque, p.pontosMarcados
        )
        return novoPlacar
    }

    fun salvaPlacar() {

        val btDesfazer = findViewById<Button>(R.id.btDesfazerJodada)
        if (placaresAnteriores.size > 0) {
            btDesfazer.visibility = View.VISIBLE
        }

        if (placaresAnteriores.size == 5){
            placaresAnteriores.removeAt(0)
        }
        placaresAnteriores.add(
            copyPlacar(placar)
        )

    }

    fun desfazerJodada(v: View) {
        if (placaresAnteriores.size > 0) {

            placaresAnteriores.removeAt(placaresAnteriores.size-1)
            placar = copyPlacar(placaresAnteriores.get(placaresAnteriores.size-1))

            configInterface()

        } else {
            v.visibility = View.INVISIBLE

        }

    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun checkGanhador(index: Int) {
        salvaPlacar()

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
                configFimPartida()

            } else {
                placar.set_atual += 1
                configIntervalo()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun configIntervalo() {
        setContentView(R.layout.activity_placar_intervalo)

        var cronometro = findViewById<Chronometer>(R.id.crContIntervalo)

        val btVoltarIntervalo = findViewById<Button>(R.id.btVoltarIntervalo)
        btVoltarIntervalo.visibility = View.INVISIBLE

        cronometro.setCountDown(true)
        cronometro.setBase(SystemClock.elapsedRealtime() +  Constantes.UM_MINUTO)
        cronometro.setOnChronometerTickListener {
            if (cronometro.text.toString() ==  "00:00") {
                cronometro.stop()
                vibrar()
                val intervalo = findViewById<TextView>(R.id.tvIntervalo)
                intervalo.setText("FIM DO INTERVALO")

                btVoltarIntervalo.visibility = View.VISIBLE

            }

        }
        cronometro.start()

    }

    fun vibrar (){
        val buzzer = this.getSystemService<Vibrator>()
        val pattern = longArrayOf(0, 200, 100, 300)
        buzzer?.let {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                buzzer.vibrate(VibrationEffect.createWaveform(pattern, -1))
            } else {
                buzzer.vibrate(pattern, -1)
            }
        }

    }

    fun configFimPartida() {
        salvaResultado()

        setContentView(R.layout.activity_placar_final)
        var ganhador = placar.jogador_1
        if (placar.ganhador == Constantes.JOGADOR_2) ganhador = placar.jogador_2

        val tvGanhador = findViewById<TextView>(R.id.tvGanhador)
        tvGanhador.setText(ganhador.nome)

        val tvResultado = findViewById<TextView>(R.id.tvResultado)
        val text = ganhador.cont_set_ganho.toString() + " X " + (placar.set_atual - ganhador.cont_set_ganho).toString()
        tvResultado.setText(text)

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

        val ganhou_mais = if (sets_jogador_1 > sets_jogador_2) sets_jogador_1 else sets_jogador_2

        if (setAtual == configuracoes.sets || ganhou_mais > configuracoes.sets/2) {
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

    @RequiresApi(Build.VERSION_CODES.N)
    fun addPlacarEsquerda(v: View) {
        placar.pontosMarcados += 1
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

    @RequiresApi(Build.VERSION_CODES.N)
    fun addPlacarDireita(v: View) {
        placar.pontosMarcados += 1
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

    fun voltarInicio(v: View) {
        val intent = Intent(this, ConfiguracoesActivity::class.java).apply {}
        startActivity(intent)
    }

    fun setSaque() {
        if (placar.pontosMarcados%2 == 0) {

            val ivDeveSacarDireita = findViewById<ImageView>(R.id.ivDeveSacarDireita)
            val ivDeveSacarEsquerda = findViewById<ImageView>(R.id.ivDeveSacarEsquerda)

            if (placar.saque == Constantes.JOGADOR_1) {
                placar.saque = Constantes.JOGADOR_2
                if (placar.jogador_1.lado_mesa == Constantes.ESQUERDA) {
                    ivDeveSacarDireita.visibility = View.VISIBLE
                    ivDeveSacarEsquerda.visibility = View.INVISIBLE
                } else {
                    ivDeveSacarDireita.visibility = View.INVISIBLE
                    ivDeveSacarEsquerda.visibility = View.VISIBLE
                }
            } else {
                placar.saque = Constantes.JOGADOR_1
                if (placar.jogador_2.lado_mesa == Constantes.ESQUERDA) {
                    ivDeveSacarDireita.visibility = View.VISIBLE
                    ivDeveSacarEsquerda.visibility = View.INVISIBLE

                } else {
                    ivDeveSacarDireita.visibility = View.INVISIBLE
                    ivDeveSacarEsquerda.visibility = View.VISIBLE

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

        placar.saque = configuracoes.inicia_sacando

        val lado_jogador_1 = placar.jogador_1.lado_mesa
        placar.jogador_1.lado_mesa = placar.jogador_2.lado_mesa
        placar.jogador_2.lado_mesa = lado_jogador_1

        placaresAnteriores = ArrayList()
        placaresAnteriores.add(placar)

        configInterface()
    }
}