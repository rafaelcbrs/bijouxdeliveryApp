package bijouxdelivery.com.br

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.StrictMode
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import bijouxdelivery.com.br.Model.DadosSacola
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.items_sacola.view.*
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import java.text.DecimalFormat
import java.util.concurrent.TimeUnit


class AdapterSacola(private val dados: ArrayList<DadosSacola>, private val ctx: Context) : RecyclerView.Adapter<AdapterSacola.VH>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterSacola.VH {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.items_sacola, parent, false)
        val vh = VH(v)
        return vh
    }

    override fun onBindViewHolder(holder: AdapterSacola.VH, position: Int) {
        val (id, nome_produto, valor, cod_cliente, cod_produto, fornecedor, codestrelasacola, estado, cidade) = dados[position]

        holder.txtId.id = id.toInt()
        holder.txtId.setOnClickListener {

            val policy = StrictMode.ThreadPolicy.Builder()
                    .detectAll()
                    .penaltyLog()
                    .build()
            StrictMode.setThreadPolicy(policy)

            // Toast.makeText(this.ctx, id.toString(), Toast.LENGTH_SHORT).show()

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
                 .url("https://www.bijouxdelivery.com.br/home/deletarprodutosacola")
                 .post(formBody)
                 .build()

         val cli = client.newCall(request).execute()

         val resposta = cli.body?.string()

         if(resposta == "true"){
               val intent = Intent(ctx, SacolaActivity::class.java)
               ctx.startActivity(intent)
              (ctx as Activity).finish()


         }else{

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

        holder.txtFornecedorSacola.text = fornecedor.toString()

        holder.txtProdutoSacola.text = nome_produto

        holder.txtEstadoCidadeSacola.text = "Entrega em" + " " + estado.toString() + "-" + cidade


        if(codestrelasacola == 1){
            Glide.with(ctx).load("https://www.bijouxdelivery.com.br/imgestrela/" + codestrelasacola + ".png").into(
                    holder.itemView.imageEstrelaSacola
            )
        }else if(codestrelasacola == 2){
            Glide.with(ctx).load("https://www.bijouxdelivery.com.br/imgestrela/" + codestrelasacola + ".png").into(
                    holder.itemView.imageEstrelaSacola
            )
        }else if(codestrelasacola == 3){
            Glide.with(ctx).load("https://www.bijouxdelivery.com.br/imgestrela/" + codestrelasacola + ".png").into(
                    holder.itemView.imageEstrelaSacola
            )
        }else if(codestrelasacola == 4){
            Glide.with(ctx).load("https://www.bijouxdelivery.com.br/imgestrela/" + codestrelasacola + ".png").into(
                    holder.itemView.imageEstrelaSacola
            )
        }else if(codestrelasacola == 5){
            Glide.with(ctx).load("https://www.bijouxdelivery.com.br/imgestrela/" + codestrelasacola + ".png").into(
                    holder.itemView.imageEstrelaSacola
            )
        }

        val dec = DecimalFormat("#,###.00")
        holder.txtValorSacola.text = "R$" + dec.format(valor.toDouble())
        val urlimg = dados[position].codproduto
        Glide.with(ctx).load("https://firebasestorage.googleapis.com/v0/b/bijouxdelivery-a2350.appspot.com/o/"+urlimg+".jpg?alt=media&token=bbef6523-e73b-4e15-b384-4ea251544fbc").into(
                holder.itemView.imgProdutoSacola

        )

        holder.itemView.imgProdutoSacola.setOnClickListener {

            val layoutFoto = LinearLayout(ctx)
            layoutFoto.orientation = LinearLayout.VERTICAL
            val imgampliada = ImageView(ctx)
            Glide.with(ctx).load("https://firebasestorage.googleapis.com/v0/b/bijouxdelivery-a2350.appspot.com/o/"+urlimg+".jpg?alt=media&token=bbef6523-e73b-4e15-b384-4ea251544fbc").into(
                    imgampliada
            )
            layoutFoto.addView(imgampliada)
            val diagfoto = AlertDialog.Builder(ctx)
            diagfoto.setTitle(fornecedor)
            diagfoto.setMessage(nome_produto)
            diagfoto.setView(layoutFoto)
            diagfoto.show()

        }

    }
    override fun getItemCount(): Int = dados.size

    class VH(itemView: View) : RecyclerView.ViewHolder(itemView){

        val txtId : ImageButton = itemView.btoIDSacola
        val txtProdutoSacola : TextView = itemView.txtProdutoSacola
        val txtValorSacola : TextView = itemView.txtValorSacola
        val txtFornecedorSacola : TextView = itemView.txtFornecedorSacola
        val txtEstadoCidadeSacola : TextView = itemView.txtCidadeEstadoSacola

    }

}