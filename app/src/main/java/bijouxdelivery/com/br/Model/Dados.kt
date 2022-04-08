package bijouxdelivery.com.br.Model
import com.google.gson.annotations.SerializedName


 data class Dados (
       @SerializedName("id")
       val ids : Int,
       @SerializedName("cod_categoria")
       val categoria : Int,
       @SerializedName("produto")
       val produtos : String,
       @SerializedName("quantidade")
       val quantidades : Int,
       @SerializedName("valor_produto")
       val valor : Double,
       @SerializedName("fornecedor")
       val fornecedores : String,
       @SerializedName("cod_estrela")
       val codestrela : Int,
       @SerializedName("estado")
       val estado : String,
       @SerializedName("cidade")
       val cidade : String,
       @SerializedName("cpf_vendedor")
       val cpfvendedor : String

)