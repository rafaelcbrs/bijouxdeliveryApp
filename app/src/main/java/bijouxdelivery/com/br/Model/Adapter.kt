package bijouxdelivery.com.br

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.os.StrictMode
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import bijouxdelivery.com.br.Model.Dados
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.items_layout.view.*
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import java.text.DecimalFormat
import java.util.concurrent.TimeUnit


class Adapter(private val dados: ArrayList<Dados>, private val ctx: Context) : RecyclerView.Adapter<Adapter.VH>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Adapter.VH {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.items_layout, parent, false)
        val vh = VH(v)
        return vh
    }

    override fun onBindViewHolder(holder: Adapter.VH, position: Int) {
        val (id, cod_produto, produto, quantidade, valor_produto, fornecedor, codestrela, estado, cidade, cpf_vendedor) = dados[position]

        holder.txtId.id = id.toInt()
        holder.txtId.setOnClickListener {

            val policy = StrictMode.ThreadPolicy.Builder()
                    .detectAll()
                    .penaltyLog()
                    .build()
            StrictMode.setThreadPolicy(policy)


            val key = ctx.getSharedPreferences("key", Context.MODE_PRIVATE)

            val client = OkHttpClient.Builder()
                    .readTimeout(10, TimeUnit.SECONDS)
                    .writeTimeout(10, TimeUnit.SECONDS)
                    .connectTimeout(10, TimeUnit.SECONDS).build()

            val codi = key.getString("key", "")!!.toString()
            val ids = id.toString()


            val formBody = FormBody.Builder()
                    .add("id", ids).add("codcliente", codi!!.toString())
                    .build()
            val request = Request.Builder()
                    .url("https://www.bijouxdelivery.com.br/home/addsacola")
                    .post(formBody)
                    .build()

            val cli = client.newCall(request).execute()

            val resposta = cli.body?.string()

            if(resposta == "true"){

                val diagconfirmacao = AlertDialog.Builder(ctx)
                diagconfirmacao.setTitle("CESTA")
                diagconfirmacao.setMessage("Produto colocado na cesta.")
                diagconfirmacao.setNeutralButton("Entendi",
                        DialogInterface.OnClickListener { dialog, id ->
                            // User clicked OK button
                        })
                diagconfirmacao.show()


            }else if(resposta == "false"){
                val diagconfirmacao = AlertDialog.Builder(ctx)
                diagconfirmacao.setTitle("CESTA")
                diagconfirmacao.setMessage("Produto não está disponível no momento.")
                diagconfirmacao.setNeutralButton("Entendi",
                        DialogInterface.OnClickListener { dialog, id ->
                            // User clicked OK button
                        })
                diagconfirmacao.show()

            }else {

                val diagconfirmacao = AlertDialog.Builder(ctx)
                diagconfirmacao.setTitle("CESTA")
                diagconfirmacao.setMessage("Erro ao tentar, tente mais tarde..")
                diagconfirmacao.setNeutralButton("Entendi",
                        DialogInterface.OnClickListener { dialog, id ->
                            // User clicked OK button
                        })
                diagconfirmacao.show()
            }
        }
        

        holder.txtProduto.text = produto.toString()
        holder.txtFornecedor.text = fornecedor.toString()
        holder.txtEstadoCidade.text = "Entrega em" + " " + estado.toString() + "-" + cidade

        if(codestrela == 1){
            Glide.with(ctx).load("https://www.bijouxdelivery.com.br/imgestrela/" + codestrela + ".png").into(
                    holder.itemView.imageEstrela
            )
        }else if(codestrela == 2){
            Glide.with(ctx).load("https://www.bijouxdelivery.com.br/imgestrela/" + codestrela + ".png").into(
                    holder.itemView.imageEstrela
            )
        }else if(codestrela == 3){
            Glide.with(ctx).load("https://www.bijouxdelivery.com.br/imgestrela/" + codestrela + ".png").into(
                    holder.itemView.imageEstrela
            )
        }else if(codestrela == 4){
            Glide.with(ctx).load("https://www.bijouxdelivery.com.br/imgestrela/" + codestrela + ".png").into(
                    holder.itemView.imageEstrela
            )
        }else if(codestrela == 5){
            Glide.with(ctx).load("https://www.bijouxdelivery.com.br/imgestrela/" + codestrela + ".png").into(
                    holder.itemView.imageEstrela
            )
        }


        val dec = DecimalFormat("#,###.00")
        holder.txtValor.text = "R$" + dec.format(valor_produto.toDouble())
        val urlimg = dados[position].ids
        Glide.with(ctx).load("https://firebasestorage.googleapis.com/v0/b/bijouxdelivery-a2350.appspot.com/o/"+urlimg+".jpg?alt=media&token=bbef6523-e73b-4e15-b384-4ea251544fbc").into(
                holder.itemView.imgProduto
        )
        holder.itemView.imgProduto.setOnClickListener {

            val layoutFoto = LinearLayout(ctx)
            layoutFoto.orientation = LinearLayout.VERTICAL
            val imgampliada = ImageView(ctx)
            Glide.with(ctx).load("https://firebasestorage.googleapis.com/v0/b/bijouxdelivery-a2350.appspot.com/o/"+urlimg+".jpg?alt=media&token=bbef6523-e73b-4e15-b384-4ea251544fbc").into(
                    imgampliada
            )
            layoutFoto.addView(imgampliada)
            val diagfoto = AlertDialog.Builder(ctx)
            diagfoto.setTitle(fornecedor)
            diagfoto.setMessage(produto)
            diagfoto.setView(layoutFoto)
            diagfoto.show()

        }

    }
    override fun getItemCount(): Int = dados.size

    class VH(itemView: View) : RecyclerView.ViewHolder(itemView){

        val txtId : ImageButton = itemView.btoID
        val txtProduto : TextView = itemView.txtProduto
        val txtValor : TextView = itemView.txtValor
        val txtFornecedor : TextView = itemView.txtFornecedor
        val txtEstadoCidade : TextView = itemView.txtCidadeEstado

    }

}