package bijouxdelivery.com.br

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.StrictMode
import android.text.InputFilter
import android.text.InputType
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat.getSystemService
import androidx.recyclerview.widget.RecyclerView
import bijouxdelivery.com.br.Model.Dados
import com.bumptech.glide.Glide
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.android.synthetic.main.custom_toast.*
import kotlinx.android.synthetic.main.custom_toast.view.*
import kotlinx.android.synthetic.main.items_listavendas.view.*
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import java.text.DecimalFormat
import java.util.concurrent.TimeUnit


class AdapterListaVendas(private val dados: ArrayList<Dados>, private val ctx: Context) : RecyclerView.Adapter<AdapterListaVendas.VH>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterListaVendas.VH {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.items_listavendas, parent, false)
        val vh = VH(v)
        return vh
    }

    override fun onBindViewHolder(holder: AdapterListaVendas.VH, position: Int) {
        val (id, cod_produto, produto, quantidade, valor_produto, fornecedor, codestrela, estado, cidade, cpf_vendedor) = dados[position]

        holder.btoAtualizarProduto.id = id.toInt()
        holder.btoAtualizarProduto.setOnClickListener {

            var layout = LinearLayout(ctx)
            layout.orientation = LinearLayout.VERTICAL
            var txtNomeProduto = TextView(ctx)
            txtNomeProduto.text = "Nome do produto."
            layout.addView(txtNomeProduto)
            var editNomeProduto = EditText(ctx)
            editNomeProduto.filters = arrayOf(InputFilter.LengthFilter(100))
            layout.addView(editNomeProduto)
            var txtFornecedor = TextView(ctx)
            txtFornecedor.text = "Nome do fornecedor"
            layout.addView(txtFornecedor)
            var editFornecedor = EditText(ctx)
            editFornecedor.filters = arrayOf(InputFilter.LengthFilter(50))
            layout.addView(editFornecedor)
            var txtquantidades = TextView(ctx)
            txtquantidades.text = "Quantidade"
            layout.addView(txtquantidades)
            var editQuantidade = EditText(ctx)
            editQuantidade.inputType = InputType.TYPE_CLASS_NUMBER
            editQuantidade.filters = arrayOf(InputFilter.LengthFilter(3))
            layout.addView(editQuantidade)
            var txtvalor = TextView(ctx)
            txtvalor.text = "Valor do produto. Ex: R$ 35.50 use o ponto ao invés de (,)"
            layout.addView(txtvalor)
            var editvalor = EditText(ctx)
            editvalor.inputType = InputType.TYPE_NUMBER_FLAG_DECIMAL + InputType.TYPE_CLASS_NUMBER
            editvalor.filters = arrayOf(InputFilter.LengthFilter(6))
            layout.addView(editvalor)
            var viewscrol = ScrollView(ctx)
            viewscrol.addView(layout)


            var dialogatualiza = AlertDialog.Builder(ctx)
            dialogatualiza.setView(viewscrol)
            dialogatualiza.setPositiveButton("ATUALIZAR"){ dialog, which ->


                if(editNomeProduto.text.toString() == ""){
                  toastadpaterListaVendas("Insira todos os dados.")
                }else if(editFornecedor.text.toString() == ""){
                    toastadpaterListaVendas("Insira todos os dados.")
                }else if(editQuantidade.text.toString() == ""){
                    toastadpaterListaVendas("Insira todos os dados.")
                }else if(editvalor.text.toString() == ""){
                    toastadpaterListaVendas("Insira todos os dados.")
                }else{


                    val policy = StrictMode.ThreadPolicy.Builder()
                            .detectAll()
                            .penaltyLog()
                            .build()
                    StrictMode.setThreadPolicy(policy)



                    val client = OkHttpClient.Builder()
                            .readTimeout(10, TimeUnit.SECONDS)
                            .writeTimeout(10, TimeUnit.SECONDS)
                            .connectTimeout(10, TimeUnit.SECONDS).build()

                    val ids = id.toString()


                    val formBody = FormBody.Builder()
                            .add("id", ids)
                            .add("nomeproduto", editNomeProduto.text.toString())
                            .add("nomefornecedor", editFornecedor.text.toString())
                            .add("quantidade", editQuantidade.text.toString())
                            .add("valor", editvalor.text.toString())
                            .build()
                    val request = Request.Builder()
                            .url("https://www.bijouxdelivery.com.br/home/updateproduto")
                            .post(formBody)
                            .build()

                    val cli = client.newCall(request).execute()

                    val resposta = cli.body?.string()

                    if(resposta == "true"){
                        toastadpaterListaVendas("Produto atualizado.")
                        val intent = Intent(ctx, listavendasActivity::class.java)
                        ctx.startActivity(intent)
                        (ctx as Activity).finish()
                    }else{
                        toastadpaterListaVendas("Tente mais tarde.")
                    }



                }


            }
            dialogatualiza.setNegativeButton("SAIR"){ dialog, which ->


            }


            dialogatualiza.show()

        }

        holder.txtIdRemoverprodutoLista.id = id.toInt()
        holder.txtIdRemoverprodutoLista.setOnClickListener {

            val client = OkHttpClient.Builder()
                    .readTimeout(10, TimeUnit.SECONDS)
                    .writeTimeout(10, TimeUnit.SECONDS)
                    .connectTimeout(10, TimeUnit.SECONDS).build()

            val ids = id.toString()


            val formBody = FormBody.Builder()
                    .add("id", ids)
                    .build()
            val request = Request.Builder()
                    .url("https://www.bijouxdelivery.com.br/home/removerproduto")
                    .post(formBody)
                    .build()
            val cli = client.newCall(request).execute()
            val resposta = cli.body?.string()

            if (resposta == "true") {

                val storage = Firebase.storage("gs://bijouxdelivery-a2350.appspot.com")

                var storageRef = storage.reference

                var desertRef = storageRef.child(id.toString() + ".jpg");

                desertRef.delete()

                  val intent = Intent(ctx, listavendasActivity::class.java)
                  ctx.startActivity(intent)
                  (ctx as Activity).finish()

            }else{
                val diagconfirmacao = AlertDialog.Builder(ctx)
                diagconfirmacao.setTitle("PRODUTO")
                diagconfirmacao.setMessage("Erro de sistema tente novamente mais tarde.")
                diagconfirmacao.setNeutralButton("Entendi",
                        DialogInterface.OnClickListener { dialog, id ->
                            // User clicked OK button
                        })
                diagconfirmacao.show()

            }


        }




        holder.txtProdutoLista.text = produto.toString()
        holder.txtFornecedorLista.text = fornecedor.toString()
        holder.txtEstadoCidadeLista.text = "Entrega em" + " " + estado.toString() + "-" + cidade
        holder.txtQuantidadeLista.text = "Quantidade em estoque:" + quantidade.toString()

        if (codestrela == 1) {
            Glide.with(ctx).load("https://www.bijouxdelivery.com.br/imgestrela/" + codestrela + ".png").into(
                    holder.itemView.imageEstrelaLista
            )
        } else if (codestrela == 2) {
            Glide.with(ctx).load("https://www.bijouxdelivery.com.br/imgestrela/" + codestrela + ".png").into(
                    holder.itemView.imageEstrelaLista
            )
        } else if (codestrela == 3) {
            Glide.with(ctx).load("https://www.bijouxdelivery.com.br/imgestrela/" + codestrela + ".png").into(
                    holder.itemView.imageEstrelaLista
            )
        } else if (codestrela == 4) {
            Glide.with(ctx).load("https://www.bijouxdelivery.com.br/imgestrela/" + codestrela + ".png").into(
                    holder.itemView.imageEstrelaLista
            )
        } else if (codestrela == 5) {
            Glide.with(ctx).load("https://www.bijouxdelivery.com.br/imgestrela/" + codestrela + ".png").into(
                    holder.itemView.imageEstrelaLista
            )
        }


        val dec = DecimalFormat("#,###.00")
        holder.txtValorLista.text = "R$" + dec.format(valor_produto.toDouble())
        val urlimg = dados[position].ids
        Glide.with(ctx).load("https://firebasestorage.googleapis.com/v0/b/bijouxdelivery-a2350.appspot.com/o/" + urlimg + ".jpg?alt=media&token=bbef6523-e73b-4e15-b384-4ea251544fbc").into(
                holder.itemView.imgProdutoLista
        )
        holder.itemView.imgProdutoLista.setOnClickListener {

            val layoutFoto = LinearLayout(ctx)
            layoutFoto.orientation = LinearLayout.VERTICAL
            val imgampliada = ImageView(ctx)
            Glide.with(ctx).load("https://firebasestorage.googleapis.com/v0/b/bijouxdelivery-a2350.appspot.com/o/" + urlimg + ".jpg?alt=media&token=bbef6523-e73b-4e15-b384-4ea251544fbc").into(
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

    class VH(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val btoAtualizarProduto : ImageButton = itemView.btoIDlistavendasLista
        val txtIdRemoverprodutoLista: ImageButton = itemView.btoIDremoverprodutoLista
        val txtProdutoLista: TextView = itemView.txtProdutoLista
        val txtValorLista: TextView = itemView.txtValorLista
        val txtFornecedorLista: TextView = itemView.txtFornecedorLista
        val txtEstadoCidadeLista: TextView = itemView.txtCidadeEstadoLista
        val txtQuantidadeLista: TextView = itemView.txtQuantidadeLista

    }

    fun toastadpaterListaVendas(texto: String){

        val lay = LayoutInflater.from(ctx)
        val layout = lay.inflate(R.layout.custom_toast, null)
        layout.txtToast.text = texto
        val myToast = Toast(ctx)
        myToast.duration = Toast.LENGTH_SHORT
        myToast.setGravity(Gravity.CENTER, 0, 0)
        myToast.view = layout
        myToast.show()

    }
}





