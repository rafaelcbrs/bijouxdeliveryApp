package bijouxdelivery.com.br.Model

import com.google.gson.annotations.SerializedName

data class DadosRastreio (

    @SerializedName("id")
    val ids : Int,
    @SerializedName("nome_produto")
    val produto : String,
    @SerializedName("valor")
    val valorproduto: Double,
    @SerializedName("cod_cliente")
    val codcliente : String,
    @SerializedName("cod_produto")
    val codproduto : Int,
    @SerializedName("codvendedor")
    val codvendedor : String,
    @SerializedName("data_pedido")
    val datapedido : String,
    @SerializedName("status")
    val statusrastreio : String,
    @SerializedName("estado")
    val estado : String,
    @SerializedName("cidade")
    val cidade : String,
    @SerializedName("fornecedor")
    val fornecedor : String
    )
