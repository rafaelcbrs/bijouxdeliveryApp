package bijouxdelivery.com.br

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import bijouxdelivery.com.br.Model.DadosRastreio
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.items_layout.view.*
import kotlinx.android.synthetic.main.items_rastreio.view.*
import org.w3c.dom.Text
import java.text.DecimalFormat


class AdapterRastreio(private val dados: ArrayList<DadosRastreio>, private val ctx: Context) : RecyclerView.Adapter<AdapterRastreio.VH>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterRastreio.VH {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.items_rastreio, parent, false)
        val vh = VH(v)
        return vh
    }

    override fun onBindViewHolder(holder: AdapterRastreio.VH, position: Int) {
        val (id, nome_produto, valor, cod_cliente, cod_produto, codvendedor, data_pedido, status, estado, cidade, fornecedor) = dados[position]






        holder.txtFornecedorRastreio.text = fornecedor.toString()
        holder.txtProdutoRastreio.text = nome_produto.toString()
        holder.txtDataPedidoRastreio.text =  "Data do pedido:" + data_pedido.toString()
        holder.txtEstadoCidadeRastreio.text = "Entrega em" + " " + estado.toString() + "-" + cidade
        holder.txtStatusRastreio.text = status.toString()


        val dec = DecimalFormat("#,###.00")
        holder.txtValorRastreio.text = "R$" + dec.format(valor.toDouble())
        val urlimg = dados[position].codproduto
        Glide.with(ctx).load("https://firebasestorage.googleapis.com/v0/b/bijouxdelivery-a2350.appspot.com/o/"+urlimg+".jpg?alt=media&token=bbef6523-e73b-4e15-b384-4ea251544fbc").into(
            holder.itemView.imgProdutoRastreio
        )

        holder.itemView.imgProdutoRastreio.setOnClickListener {

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


        val txtProdutoRastreio : TextView = itemView.txtProdutoRastreio
        val txtValorRastreio : TextView = itemView.txtValorRastreio
        val txtFornecedorRastreio : TextView = itemView.txtFornecedorRastreio
        val txtEstadoCidadeRastreio : TextView = itemView.txtCidadeEstadoRastreio
        val txtStatusRastreio : TextView = itemView.txtStatusRastreio
        val txtDataPedidoRastreio : TextView = itemView.txtDataPedidoRastreio
    }

}