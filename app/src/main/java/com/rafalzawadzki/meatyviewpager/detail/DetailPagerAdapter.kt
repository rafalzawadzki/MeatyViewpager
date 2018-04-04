package com.rafalzawadzki.meatyviewpager.detail

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.view.ViewGroup
import com.rafalzawadzki.meatyviewpager.core.model.Meat



class DetailPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

    var meats = listOf<Meat>()
    var currentDetailFragment: DetailPagerFragment? = null

    override fun getItem(position: Int): Fragment {
        return DetailPagerFragment.newInstance(meats[position])
    }

    override fun getCount(): Int {
        return meats.size
    }

    fun setItems(items: List<Meat>) {
        this.meats = items
        notifyDataSetChanged()
    }

    override fun setPrimaryItem(container: ViewGroup, position: Int, `object`: Any) {
        super.setPrimaryItem(container, position, `object`)
        currentDetailFragment = `object` as DetailPagerFragment
    }
}