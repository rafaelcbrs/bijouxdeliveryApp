package bijouxdelivery.com.br.Model

import com.google.gson.annotations.SerializedName

data class DadosCep (
        @SerializedName("cep")
        val ceps : String,
        @SerializedName("logradouro")
        val logradouros : String,
        @SerializedName("complemento")
        val complementos : String,
        @SerializedName("bairro")
        val bairros : String,
        @SerializedName("localidade")
        val localidades : String,
        @SerializedName("uf")
        val ufs : String,
        @SerializedName("ibge")
        val ibges : String,
        @SerializedName("gia")
        val gias : String,
        @SerializedName("ddd")
        val ddds : String,
        @SerializedName("siafi")
        val siafis : String


)