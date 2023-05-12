package com.example.seabattle.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.seabattle.R
import com.example.seabattle.data.model.gameobjects.Cell

class CellAdapter(
    private val cells: ArrayList<Cell>
) : RecyclerView.Adapter<CellAdapter.CellHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CellHolder {
        val frameLayout = FrameLayout(parent.context)
        return CellHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.cell, frameLayout)
        )
    }

    override fun getItemCount(): Int {
        return cells.size
    }

    override fun onBindViewHolder(holder: CellHolder, position: Int) {
        holder.bind(cells[position])
    }

    class CellHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var x: Int = 0
        private var y: Int = 0
        private val text: TextView = itemView.findViewById(R.id.cell)

        fun bind(cell: Cell) {
            x = cell.posX
            y = cell.posY
            itemView.setOnClickListener{

            }
        }

        fun changeText(text: String) {
            this.text.text = text
        }
    }
}