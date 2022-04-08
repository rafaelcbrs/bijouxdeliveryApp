package bijouxdelivery.com.br

import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.StrictMode
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import bijouxdelivery.com.br.Model.DadosRastreio
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_rastreio.*
import kotlinx.android.synthetic.main.activity_sacola.*
import kotlinx.android.synthetic.main.toolbarprodutos.*
import kotlinx.android.synthetic.main.toolbarrastreio.*
import kotlinx.android.synthetic.main.toolbarsacola.*
import okhttp3.OkHttpClient
import okhttp3.Request
import java.util.concurrent.TimeUnit

class rastreio : AppCompatActivity() {




    private val dado = ArrayList<DadosRastreio>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rastreio)
        window.navigationBarColor = ContextCompat.getColor(this, R.color.headre_color_backgroud)



        txtTituloRastreio.text = "Rastreio"

        val policy = StrictMode.ThreadPolicy.Builder()
            .detectAll()
            .penaltyLog()
            .build()
        StrictMode.setThreadPolicy(policy)


        swipeprodutosRastreio.setColorSchemeColors(Color.RED, Color.RED, Color.RED)


        swipeprodutosRastreio.setOnRefreshListener {
            buscar()
            swipeprodutosRastreio.isRefreshing = false
        }


        icovoltarRastreio.setOnClickListener {
            val intent = Intent(this, homeLayout::class.java)
            startActivity(intent)
            finish()
        }

        buscar()
    }

    fun buscar(){

        val key = this.getSharedPreferences("key", Context.MODE_PRIVATE)
        val codi = key.getString("key", "")!!.toString()

        val listView = findViewById<RecyclerView>(R.id.listaprodutosRastreio)
        dado.clear()
        listView.recycledViewPool.clear()

        listView.setLayoutManager(GridLayoutManager(this, 2))

        val client = OkHttpClient.Builder()
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .connectTimeout(10, TimeUnit.SECONDS).build()
        val request = Request.Builder()
                .url("https://www.bijouxdelivery.com.br/home/buscarpedidos?codcliente=" + codi)
                .build()


        val response = client.newCall(request).execute()
        val json = response.body?.string()

        val gson = Gson()

        val publisher = gson.fromJson(json, Array<DadosRastreio>::class.java)

        val s = publisher.count()
        val total = s - 1


        for (i in 0..total){
            dado.add(DadosRastreio(
                    publisher.get(i).ids,
                    publisher.get(i).produto,
                    publisher.get(i).valorproduto,
                    publisher.get(i).codcliente,
                    publisher.get(i).codproduto,
                    publisher.get(i).codvendedor,
                    publisher.get(i).datapedido,
                    publisher.get(i).statusrastreio,
                    publisher.get(i).estado,
                    publisher.get(i).cidade,
                    publisher.get(i).fornecedor

            )
            )
            listView.adapter = AdapterRastreio(dado, this)
        }

    }
}