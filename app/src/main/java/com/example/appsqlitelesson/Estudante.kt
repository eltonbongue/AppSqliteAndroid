package com.example.appsqlitelesson

import android.content.ContentValues
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.appsqlitelesson.databinding.ActivityEstudanteBinding
import java.io.ByteArrayOutputStream
import java.io.File
import kotlin.Exception

class Estudante : AppCompatActivity() {

    private lateinit var binding: ActivityEstudanteBinding
    private var imageByteArray: ByteArray? = null
    private var db: SQLiteDatabase? = null
    private var myFile: File? = null
    private lateinit var msg: String
    private var sId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityEstudanteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        myFile = ConnectionClass.myFile
        db = SQLiteDatabase.openOrCreateDatabase(myFile!!.absolutePath, null, null)
        val i = intent
        msg = i.getStringExtra("msg").toString()
        sId = i.getIntExtra("sid", 0)

        when(msg) {
            "add" -> binding.buttonSalvarDados.text = "Inserir dados"
            "edit" -> {
                binding.buttonSalvarDados.text = "actualizar dados"
                binding.btnDel.visibility = View.VISIBLE

                showData()

            }

        }





    }

    fun uploadEstudanteImage(view: View) {

        val myFileIntent = Intent(Intent.ACTION_GET_CONTENT)
        myFileIntent.type = "image/* "
        activityResultLauncher.launch((myFileIntent))

    }

    private val activityResultLauncher = registerForActivityResult(

        ActivityResultContracts.StartActivityForResult()
    ) { result: ActivityResult ->
        if (result.resultCode == RESULT_OK) {

            val uri = result.data!!.data
            try {
                val inputStream =
                    contentResolver.openInputStream(uri!!)
                val myBitmap = BitmapFactory.decodeStream(inputStream)
                val stream = ByteArrayOutputStream()
                myBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)

                imageByteArray = stream.toByteArray()
                binding.imageViewEstudante.setImageBitmap(myBitmap)
            } catch (ex: java.lang.Exception) {
                Toast.makeText(this, ex.message.toString(), Toast.LENGTH_LONG).show()
            }
        }

    }


    private fun showData() {

        try {
            val cursor =
                db!!.rawQuery("Select * from estudante where studentId = $sId", null)

            if (cursor.count > 0) {
                while (cursor.moveToNext()) {

                    binding.editNome.setText(cursor.getString(1).toString())
                    binding.editEndereco.setText(cursor.getString(2).toString())
                    binding.editClasse.setText(cursor.getString(3).toString())
                    binding.editIdade.setText(cursor.getString(4).toString())
                    var x: ByteArray? = null
                    if (cursor.getBlob(5) != null) {

                        x = cursor.getBlob(5)
                        val bitmap = BitmapFactory.decodeByteArray(x, 0, x.size)
                        binding.imageViewEstudante.setImageBitmap(bitmap)
                        imageByteArray = x
                    }
                    cursor.close()

                }
            }

        }

        catch (ex: Exception){
            Toast.makeText(this, ex.message.toString(),Toast.LENGTH_SHORT).show()
        }

    }


    fun btnClick(view: View) {
        when(msg){
            "add" -> insertData()
            "edit" -> updateData()
        }

    }




    private fun insertData() {
        val estudanteName = binding.editNome.text.toString()
        val estudanteEndereco = binding.editEndereco.text.toString()
        val estudante_class = binding.editClasse.text.toString()
        val estudante_idade = binding.editIdade.text.toString()


        if (binding.editNome.text.toString().isEmpty()) {

          Toast.makeText(this, "digite o nome do estudante", Toast.LENGTH_SHORT).show()
            binding.editNome.requestFocus()

        }

       else if (binding.editClasse.text.toString().isEmpty()) {

            Toast.makeText(this, "digite o nome da classe", Toast.LENGTH_SHORT).show()
            binding.editClasse.requestFocus()
        }

        else{

            try{
                val  values = ContentValues()
                values.put("nomeEstudante", estudanteName)
                values.put("endereco", estudanteEndereco)
                values.put("classe", estudante_class)
                values.put("idade", estudante_idade)
                values.put("estudantePhoto", imageByteArray)
                db!!.insert("estudante", null,values)
                Toast.makeText(this,"Dado inserido:" , Toast.LENGTH_SHORT).show()
                clr()
            }

            catch(ex:Exception) {

                Toast.makeText(this, "insert: " + ex.message.toString(), Toast.LENGTH_LONG).show()
            }
        }

    }

    private fun updateData() {
        val estudanteName = binding.editNome.text.toString()
        val estudanteEndereco = binding.editEndereco.text.toString()
        val estudante_class = binding.editClasse.text.toString()
        val estudante_idade = binding.editIdade.text.toString()


        if (binding.editNome.text.toString().isEmpty()) {

            Toast.makeText(this, "digite o nome do estudante", Toast.LENGTH_SHORT).show()
            binding.editNome.requestFocus()

        }

        else if (binding.editClasse.text.toString().isEmpty()) {

            Toast.makeText(this, "digite o nome da classe", Toast.LENGTH_SHORT).show()
            binding.editClasse.requestFocus()
        }

        else{

            try{
                val  values = ContentValues()
                values.put("nomeEstudante", estudanteName)
                values.put("endereco", estudanteEndereco)
                values.put("classe", estudante_class)
                values.put("idade", estudante_idade)
                values.put("estudantePhoto", imageByteArray)
                db!!.update("estudante", values, "studentId = $sId", null)
                Toast.makeText(this,"Dado actualizado:" , Toast.LENGTH_SHORT).show()
                clr()
            }

            catch(ex:Exception) {

                Toast.makeText(this, "insert: " + ex.message.toString(), Toast.LENGTH_LONG).show()
            }
        }

    }

    fun  delData(view: View){
        val values = ContentValues()
        values.put("studentId", sId)
        db!!.delete("estudante", "studentId = $sId ", null)
        Toast.makeText(this,"Dado deletado" , Toast.LENGTH_SHORT).show()
        finish()

    }


    private fun clr() {
        binding.editNome.text.clear()
        binding.editEndereco.text.clear()
        binding.editClasse.text.clear()
        binding.editIdade.text.clear()
        binding.imageViewEstudante.setImageDrawable(null)
    }
}