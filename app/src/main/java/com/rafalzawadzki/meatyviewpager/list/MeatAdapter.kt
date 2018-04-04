package com.rafalzawadzki.meatyviewpager.list

import android.support.v4.view.ViewCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.rafalzawadzki.meatyviewpager.R
import com.rafalzawadzki.meatyviewpager.core.model.Meat
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.cell_image.view.*


class MeatAdapter : RecyclerView.Adapter<MeatAdapter.MeatViewHolder>() {

    var meats = listOf<Meat>()
    var clickListener: ((Meat, View) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MeatViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.cell_image, parent, false)
        return MeatViewHolder(view)
    }

    override fun onBindViewHolder(holder: MeatViewHolder, position: Int) {
        val meat = meats[position]

        ViewCompat.setTransitionName(holder.imgBackground, meat.transitionName)
        holder.imgBackground.tag = meat.transitionName

        holder.itemView.setOnClickListener { clickListener?.invoke(meat, holder.imgBackground) }

        Picasso.with(holder.imgBackground.context)
                .load(meat.url)
                .into(holder.imgBackground)
    }

    override fun getItemCount() = meats.size

    fun setItems(items: List<Meat>) {
        meats = items
        notifyDataSetChanged()
    }

    class MeatViewHolder(view: View): RecyclerView.ViewHolder(view) {

        val imgBackground = view.imgBackground as ImageView

    }
}