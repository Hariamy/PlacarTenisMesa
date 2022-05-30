package data

import java.io.Serializable

data class Configuracoes(
    var nome_partida:String,
    var descricao:String,
    var nome_jogador_1:String,
    var nome_jogador_2:String,
    var inicia_sacando:Int,
    var lado_inicial_jogador1:Int,
    var lado_inicial_jogador2:Int,
    var sets:Int

    ):Serializable
