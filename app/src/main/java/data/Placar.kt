package data

import java.io.Serializable

data class Placar(
    var nome_partida:String ,
    var descricao:String,

    var jogador_1:Jogador,
    var jogador_2:Jogador,

    var set_atual:Int,
    var ganhador:Int,

    var sets:Int,
    var saque:Int,
    var pontosMarcados:Int

    ):Serializable
