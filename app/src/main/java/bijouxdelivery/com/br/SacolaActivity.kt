package bijouxdelivery.com.br

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.StrictMode
import android.text.Editable
import android.text.InputFilter
import android.text.InputType
import android.text.TextWatcher
import android.view.Gravity
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import bijouxdelivery.com.br.Model.DadosCep
import bijouxdelivery.com.br.Model.DadosSacola
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_produtos_.*
import kotlinx.android.synthetic.main.activity_sacola.*
import kotlinx.android.synthetic.main.custom_toast.*
import kotlinx.android.synthetic.main.custom_toast.view.*
import kotlinx.android.synthetic.main.toolbarprodutos.*
import kotlinx.android.synthetic.main.toolbarprodutos.icovoltar
import kotlinx.android.synthetic.main.toolbarsacola.*
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import java.text.DecimalFormat
import java.util.concurrent.TimeUnit


class SacolaActivity : AppCompatActivity() {

    private val dadossacola = ArrayList<DadosSacola>()

    val dec = DecimalFormat("#,###.00")

    var totalvalor : Double = 0.00

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sacola)

        window.navigationBarColor = ContextCompat.getColor(this, R.color.headre_color_backgroud)



        txtCestotext.text = "Cesta"

        val policy = StrictMode.ThreadPolicy.Builder()
                .detectAll()
                .penaltyLog()
                .build()
        StrictMode.setThreadPolicy(policy)


        swipesacola.setColorSchemeColors(Color.RED, Color.RED, Color.RED)


        swipesacola.setOnRefreshListener {
            totalvalor = 0.00
            buscar()
            swipesacola.isRefreshing = false
        }


        icovoltar.setOnClickListener {
            val intent = Intent(this, homeLayout::class.java)
            startActivity(intent)
            finish()
        }

        btoEnviarPedido.setOnClickListener {
                 enviar()
        }

        buscar()

    }


         fun String.toEditable(): Editable =  Editable.Factory.getInstance().newEditable(this)

         fun enviar(){

            if (totalvalor != 0.0) {

                val scrollpedido = ScrollView(this)

                val layoutEnviar = LinearLayout(this)
                layoutEnviar.orientation = LinearLayout.VERTICAL

                val diagEnviarPedido = AlertDialog.Builder(this)
                diagEnviarPedido.setTitle("Dados do envio.")
                diagEnviarPedido.setMessage("Total R$" + dec.format(totalvalor.toDouble()))

                val edittextNomeCliente = EditText(this)
                edittextNomeCliente.setInputType(InputType.TYPE_CLASS_TEXT)
                edittextNomeCliente.hint = "Nome completo..."
                edittextNomeCliente.filters = arrayOf(InputFilter.LengthFilter(100))
                layoutEnviar.addView(edittextNomeCliente)


                var edittextEndereco = EditText(this)
                edittextEndereco.setInputType(InputType.TYPE_CLASS_TEXT)
                edittextEndereco.hint = "Endereço completo..."
                edittextEndereco.isEnabled = false
                edittextEndereco.filters = arrayOf(InputFilter.LengthFilter(100))
                layoutEnviar.addView(edittextEndereco)

                var edittextBairro = EditText(this)
                edittextBairro.setInputType(InputType.TYPE_CLASS_TEXT)
                edittextBairro.hint = "Bairro..."
                edittextBairro.filters = arrayOf(InputFilter.LengthFilter(100))
                edittextBairro.isEnabled = false
                layoutEnviar.addView(edittextBairro)

                val edittextNumero = EditText(this)
                edittextNumero.setInputType(InputType.TYPE_CLASS_NUMBER)
                edittextNumero.hint = "Número..."
                edittextNumero.filters = arrayOf(InputFilter.LengthFilter(10))
                layoutEnviar.addView(edittextNumero)

                val edittextComplemento = EditText(this)
                edittextComplemento.setInputType(InputType.TYPE_CLASS_TEXT)
                edittextComplemento.hint = "Complemento..."
                edittextComplemento.filters = arrayOf(InputFilter.LengthFilter(100))
                layoutEnviar.addView(edittextComplemento)

                val edittextUF = EditText(this)
                edittextUF.setInputType(InputType.TYPE_CLASS_TEXT)
                edittextUF.hint = "UF..."
                edittextUF.filters = arrayOf(InputFilter.LengthFilter(5))
                edittextUF.isEnabled = false
                layoutEnviar.addView(edittextUF)

                val edittextCidade = EditText(this)
                edittextCidade.setInputType(InputType.TYPE_CLASS_TEXT)
                edittextCidade.hint = "Cidade..."
                edittextCidade.filters = arrayOf(InputFilter.LengthFilter(100))
                edittextCidade.isEnabled = false
                layoutEnviar.addView(edittextCidade)

                val edittextTelefone = EditText(this)
                edittextTelefone.setInputType(InputType.TYPE_CLASS_NUMBER)
                edittextTelefone.hint = "Telefone..."
                edittextTelefone.filters = arrayOf(InputFilter.LengthFilter(15))
                layoutEnviar.addView(edittextTelefone)

                val edittextCep = EditText(this)
                edittextCep.setInputType(InputType.TYPE_CLASS_NUMBER)
                edittextCep.hint = "CEP..."
                edittextCep.filters = arrayOf(InputFilter.LengthFilter(8))
                edittextCep.addTextChangedListener(object : TextWatcher {
                    override fun afterTextChanged(s: Editable?) {
                    }

                    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                    }

                    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                        if(edittextCep.length() == 8)
                        {
                            val client = OkHttpClient.Builder()
                                    .readTimeout(10, TimeUnit.SECONDS)
                                    .writeTimeout(10, TimeUnit.SECONDS)
                                    .connectTimeout(10, TimeUnit.SECONDS).build()


                            val request = Request.Builder()
                                    .url("https://viacep.com.br/ws/"+edittextCep.text.toString()+"/json")
                                    .build()

                            try {


                                val cli = client.newCall(request).execute()

                                val respostap = cli.body?.string()

                                val gson = Gson()

                                val publisher = gson.fromJson(respostap, DadosCep::class.java)


                                edittextEndereco.text = publisher.logradouros.toEditable()
                                edittextUF.text = publisher.ufs.toEditable()
                                edittextBairro.text = publisher.bairros.toEditable()
                                edittextCidade.text = publisher.localidades.toEditable()
                            }catch (ex:Exception) {

                                toastSacola("CEP não encontrado, digite um CEP válido.")


                            }
                        }

                    }
                })
                layoutEnviar.addView(edittextCep)
                scrollpedido.addView(layoutEnviar)

                diagEnviarPedido.setView(scrollpedido)


                diagEnviarPedido.setNegativeButton("SAIR"){dialog, which ->


                }


                diagEnviarPedido.setPositiveButton("ENVIAR PEDIDO") { dialog, which ->
                    val key = getSharedPreferences("key", Context.MODE_PRIVATE)
                    val d = key.getString("key", "")


                    if (edittextNomeCliente.text.toString() == "" || edittextCep.text.toString() == "" || edittextEndereco.text.toString() == "" || edittextBairro.text.toString() == "" || edittextNumero.text.toString() == "" || edittextComplemento.text.toString() == "" || edittextUF.text.toString() == "" || edittextCidade.text.toString() == "" || edittextTelefone.text.toString() == "") {
                        enviar()
                        toastSacola("Digite todos os dados de envio.")


                    } else {


                        val client = OkHttpClient.Builder()
                                .readTimeout(10, TimeUnit.SECONDS)
                                .writeTimeout(10, TimeUnit.SECONDS)
                                .connectTimeout(10, TimeUnit.SECONDS).build()


                        val formBody = FormBody.Builder()
                                .add("codcliente", d.toString())
                                .add("nomecliente", edittextNomeCliente.text.toString())
                                .add("enderecocliente", edittextEndereco.text.toString())
                                .add("bairro", edittextBairro.text.toString())
                                .add("numerocliente", edittextNumero.text.toString())
                                .add("complementocliente", edittextComplemento.text.toString())
                                .add("ufcliente", edittextUF.text.toString())
                                .add("cep", edittextCep.text.toString())
                                .add("telefone", edittextTelefone.text.toString())
                                .add("cidade", edittextCidade.text.toString())
                                .build()
                        val request = Request.Builder()
                                .url("https://www.bijouxdelivery.com.br/home/enviarpedido")
                                .post(formBody)
                                .build()

                        val cli = client.newCall(request).execute()

                        val respostap = cli.body?.string()





                        val intent = Intent(this, homeLayout::class.java)
                        startActivity(intent)
                        finish()

                        toastSacola(respostap.toString())

                    }
                }


                diagEnviarPedido.show()
            }




            else {

                toastSacola("Você não possui produtos na cesta.")

            }

        }

fun toastSacola(texto : String){
        val layout = layoutInflater.inflate(R.layout.custom_toast, toastlinearinfo)
        layout.txtToast.text = texto
        val myToast = Toast(applicationContext)
        myToast.duration = Toast.LENGTH_LONG
        myToast.setGravity(Gravity.CENTER, 0, 0)
        myToast.view = layout
        myToast.show()

    }


    fun buscar(){

        val key = this.getSharedPreferences("key", Context.MODE_PRIVATE)
        val codi = key.getString("key", "")!!.toString()

        val listView = findViewById<RecyclerView>(R.id.listasacola)
        dadossacola.clear()
        listView.recycledViewPool.clear()

        listView.setLayoutManager(GridLayoutManager(this, 2))

        val client = OkHttpClient.Builder()
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .connectTimeout(10, TimeUnit.SECONDS).build()
        val request = Request.Builder()
                .url("https://www.bijouxdelivery.com.br/home/buscarsacola?codcliente=" + codi)
                .build()


        val response = client.newCall(request).execute()
        val json = response.body?.string()

        val gson = Gson()

        val publisher = gson.fromJson(json, Array<DadosSacola>::class.java)

        val s = publisher.count()
        val total = s - 1


        for (i in 0..total){
            totalvalor = (totalvalor + publisher.get(i).valorproduto).toDouble()
            dadossacola.add(DadosSacola(
                    publisher.get(i).id,
                    publisher.get(i).nomeproduto,
                    publisher.get(i).valorproduto,
                    publisher.get(i).codcliente,
                    publisher.get(i).codproduto,
                    publisher.get(i).fornecedores,
                    publisher.get(i).codestrela,
                    publisher.get(i).estado,
                    publisher.get(i).cidade
            )
            )
            listView.adapter = AdapterSacola(dadossacola, this)
        }

        txtValorTotal.text = "Total R$" + dec.format(totalvalor.toDouble())
    }
}