package com.example.appsqlitelesson

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView

class itemAdapter (private val itemList: ArrayList<DataSet>):

    RecyclerView.Adapter<itemAdapter.ItemHolder>(){
    private lateinit var mListener: OnItemClickListener

    interface  OnItemClickListener{

        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener){
        mListener = listener
    }

    class ItemHolder(itemView: View, listener: OnItemClickListener):RecyclerView.ViewHolder(itemView){

        val itemName: TextView = itemView.findViewById(R.id.textViewNome)
        val itemEndereco: TextView = itemView.findViewById(R.id.textViewEndereco)
        val itemClass: TextView = itemView.findViewById(R.id.textViewClasse)
        val itemIdade: TextView = itemView.findViewById(R.id.textViewIdade)
        val itemImg: ImageView = itemView.findViewById(R.id.imageViewEstudante0)


        init {
            itemView.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
       val itemView = LayoutInflater.from(parent.context).inflate(

           R.layout.item,
           parent, false

       )

        return ItemHolder(itemView, mListener)

    }

     override fun getItemCount(): Int {

        return itemList.size

    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        val currentItem = itemList[position]
        holder.itemName.text = currentItem.nomeEstudante
        holder.itemEndereco.text = currentItem.endereco
        holder.itemClass.text = currentItem.classe
        holder.itemIdade.text = currentItem.idade.toString()

        val x = currentItem.estudantePhoto

        if (x != null && x.isNotEmpty()) {
            val bitmap = BitmapFactory.decodeByteArray(x, 0, x.size)
            holder.itemImg.setImageBitmap(bitmap)
        }

    }


}