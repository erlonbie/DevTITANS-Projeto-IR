package com.wax.irapp.adapter

import android.content.Context
import android.provider.CalendarContract.Colors
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.wax.irapp.R
import com.wax.irapp.models.IRCommand

class IRCommandAdapter(private val context: Context, val listener : IRCommandClickListener) : RecyclerView.Adapter<IRCommandAdapter.IRCommandViewHolder>() {

    private val irCommandList = ArrayList<IRCommand>()
    private val fullList = ArrayList<IRCommand>()

    override fun onCreateViewHolder(parent: ViewGroup,viewType: Int): IRCommandViewHolder {
        return IRCommandViewHolder(
            LayoutInflater.from(context).inflate(R.layout.list_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: IRCommandViewHolder, position: Int) {

        val currentIRCommand = irCommandList[position]
        holder.title.text = currentIRCommand.title
        holder.title.isSelected = true

        holder.date.text = currentIRCommand.date
        holder.date.isSelected = true

        holder.irCommandLayout.setCardBackgroundColor(holder.itemView.resources.getColor(R.color.grey,null))

        holder.irCommandLayout.setOnClickListener{
            listener.onItemClicked(irCommandList[holder.adapterPosition])
        }

        holder.irCommandLayout.setOnLongClickListener{
            listener.onLongItemClicked(irCommandList[holder.adapterPosition],holder.irCommandLayout)
            true
        }

        holder.button.setOnClickListener {

            Toast.makeText(context, "${currentIRCommand.title} Enviado com Sucesso", Toast.LENGTH_SHORT).show()
        }

    }

    fun updateList(newList : List<IRCommand>){

        fullList.clear()
        fullList.addAll(newList)

        irCommandList.clear()
        irCommandList.addAll(fullList)

        notifyDataSetChanged()
    }

    fun filterList(search:String){
        irCommandList.clear()

        for (item in fullList){
            if (item.title?.lowercase()?.contains(search.lowercase()) == true){
                irCommandList.add(item)
            }
        }

        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        irCommandList.size.also { return it }
    }

    class IRCommandViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val irCommandLayout = itemView.findViewById<CardView>(R.id.card_layout)
        val title = itemView.findViewById<TextView>(R.id.tv_title)
        val button = itemView.findViewById<ImageButton>(R.id.btn_send_command)
        val date = itemView.findViewById<TextView>(R.id.tv_date)
    }

    interface IRCommandClickListener{

        fun onItemClicked(irCommand: IRCommand)
        fun onLongItemClicked(irCommand: IRCommand, cardView: CardView)
    }
}