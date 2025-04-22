package com.example.appsqlitelesson

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.appsqlitelesson.databinding.ActivityListaBinding
import java.io.File

class ListaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityListaBinding
    private var db: SQLiteDatabase? = null
    private var myFile: File? = null
    private lateinit var itemArraylist: ArrayList<DataSet>
    private var context: Context? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityListaBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        myFile = ConnectionClass.myFile
        db = SQLiteDatabase.openOrCreateDatabase(myFile!!.absolutePath, null,null)
        context = this

        binding.itemLista.layoutManager = LinearLayoutManager(this)
        binding.itemLista.hasFixedSize()
        itemArraylist = arrayListOf()

        showDataList()
    }

    private fun showDataList() {

        try {
            val cursor =
                db!!.rawQuery("SELECT * FROM estudante", null)
            if (cursor.count > 0) {
                itemArraylist.clear()
                while (cursor.moveToNext()) {
                    val estudanteId = cursor.getInt(0)
                    val nome = cursor.getString(1).toString()
                    val endereco = cursor.getString(2).toString()
                    val classe = cursor.getString(3).toString()
                    val idade = cursor.getInt(4)
                    var x: ByteArray? = byteArrayOf(0x00)

                    if (cursor.getBlob(5) != null) {
                        x = cursor.getBlob(5)
                    }

                    val estudante = DataSet(
                        estudanteId,
                        nome,
                        endereco,
                        classe,
                        idade,
                        x
                    )
                    itemArraylist.add(estudante)



                }

            }

            cursor.close()

            // Agora aplica os dados no adapter
            val adapter = itemAdapter(itemArraylist)
            binding.itemLista.adapter = adapter

            // (opcional) clique nos itens
            adapter.setOnItemClickListener(object : itemAdapter.OnItemClickListener {
                override fun onItemClick(position: Int) {
                    Toast.makeText(context, "Clicou em: ${itemArraylist[position].nomeEstudante}", Toast.LENGTH_SHORT).show()
                }
            })
        }

        catch (ex: Exception){
            Toast.makeText(this, ex.message.toString(), Toast.LENGTH_SHORT).show()

        }
    }




}