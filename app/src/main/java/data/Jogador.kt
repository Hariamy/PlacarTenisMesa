package data

import java.io.Serializable

data class Jogador(

    var nome:String,
    var pontos: MutableList<Int>,
    var cont_set_ganho: Int,
    var lado_mesa: Int,

    ):Serializable
