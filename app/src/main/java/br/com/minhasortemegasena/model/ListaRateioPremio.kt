package br.com.minhasortemegasena.model

data class ListaRateioPremio(
    val descricaoFaixa: String,
    val faixa: Int,
    val numeroDeGanhadores: Int,
    val valorPremio: Double
)