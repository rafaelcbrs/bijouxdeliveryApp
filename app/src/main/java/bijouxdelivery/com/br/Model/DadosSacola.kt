package bijouxdelivery.com.br.Model

import com.google.gson.annotations.SerializedName

data class DadosSacola (
        @SerializedName("id")
        val id : Int,
        @SerializedName("nome_produto")
        val nomeproduto : String,
        @SerializedName("valor")
        val valorproduto : Double,
        @SerializedName("cod_cliente")
        val codcliente : String,
        @SerializedName("cod_produto")
        val codproduto : Int,
        @SerializedName("fornecedor")
        val fornecedores : String,
        @SerializedName("cod_estrela")
        val codestrela : Int,
        @SerializedName("estado")
        val estado : String,
        @SerializedName("cidade")
        val cidade : String


)