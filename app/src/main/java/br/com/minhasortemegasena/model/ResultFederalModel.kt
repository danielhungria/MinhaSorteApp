package br.com.minhasortemegasena.model

data class ResultFederalModel(
    val descricaoFaixa: String,
    val faixa: Int,
    val numeroDeGanhadores: Int,
    val valorPremio: Double,
    var listaDezenas: List<String>
)