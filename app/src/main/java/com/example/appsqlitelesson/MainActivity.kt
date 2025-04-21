package com.example.appsqlitelesson

import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteDatabase.*
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.appsqlitelesson.databinding.ActivityMainBinding
import java.io.File

class MainActivity : AppCompatActivity() {

    private  lateinit var binding: ActivityMainBinding
    private  var  db:SQLiteDatabase? = null
    private var myFile: File? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        createDatabase()
        createTables()
    }

    private fun createDatabase(){
                    try {
                        val folder =
                            File(getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS).toString()+"/Database")
                    if (!folder.exists()){
                        folder.mkdir()
                    }
                         myFile = File(folder, "MyDB")
                        ConnectionClass.myFile = myFile
                        db = openOrCreateDatabase(myFile!!.absolutePath, null, null)
                    }

                    catch (ex:Exception){
                        Toast.makeText(this,ex.message, Toast.LENGTH_LONG).show()
                    }
            }

    private fun createTables() {

        var createTable = " CREATE TABLE IF NOT EXISTS estudante(studentId INTEGER PRIMARY KEY AUTOINCREMENT, nomeEstudante TEXT, endereco TEXT, classe TEXT, idade int, estudantePhoto BLOB)"
        db!!.execSQL(createTable)

        createTable = " CREATE TABLE IF NOT EXISTS classe(classeId INTEGER PRIMARY KEY AUTOINCREMENT, nomeClasse TEXT)"
        db!!.execSQL(createTable)

    }

    fun addData(view: View){
        val intent = Intent(this, Estudante::class.java)
        intent.putExtra("msg", "add")
        startActivity(intent)
    }

    fun editData(view: View){

        val intent = Intent(this, ListaActivity::class.java)
        startActivity(intent)
    }





}