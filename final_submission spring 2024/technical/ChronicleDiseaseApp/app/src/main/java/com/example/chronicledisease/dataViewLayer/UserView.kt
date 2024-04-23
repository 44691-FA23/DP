package com.example.chronicledisease.dataViewLayer

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.chronicledisease.databinding.SamplecardBinding

class UserView(
    val context: Context,val array:ArrayList<Int>,
    val name:ArrayList<String>,val stateDecider: StateDecider,
    val color:ArrayList<String>
) :RecyclerView.Adapter<UserView.ViewPart>(){
    class ViewPart(val item:SamplecardBinding):RecyclerView.ViewHolder(item.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)= ViewPart(
        SamplecardBinding.inflate(LayoutInflater.from(context),parent,false)
    )

    override fun getItemCount()=array.size

    override fun onBindViewHolder(holder: ViewPart, position: Int) {
    with(holder.item) {
        try {
            root.setCardBackgroundColor(Color.parseColor(color[position]))
        }catch (e:Exception){
root.setCardBackgroundColor(Color.BLACK)
        }
        details2.text=name[position]
        images.load(array[position])
        root.setOnClickListener {
            stateDecider.selectpositions(position)
        }
    }
    }
}