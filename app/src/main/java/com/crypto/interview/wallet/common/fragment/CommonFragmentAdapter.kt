package com.crypto.interview.wallet.common.fragment

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.PagerAdapter
import java.util.*

class CommonFragmentAdapter(private val fragmentManager: FragmentManager, behavior: Int) :
    FragmentPagerAdapter(fragmentManager, behavior) {

    constructor(fragmentManager: FragmentManager) : this(
        fragmentManager,
        BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
    )

    var fragments: Array<Fragment>? = null
        set(value) {
            val oldField = field
            if (oldField != null) {
                val ft = fragmentManager.beginTransaction()
                for (f in oldField) {
                    ft.remove(f)
                }
                ft.commitAllowingStateLoss()
                fragmentManager.executePendingTransactions()
                field = value
                notifyDataSetChanged()
            } else {
                field = value
            }
        }

    var titles: Array<String>? = null


    override fun getItem(position: Int): Fragment {
        return fragments!![position]
    }

    override fun getCount(): Int {
        return fragments?.size ?: 0
    }

    override fun getItemPosition(`object`: Any): Int {
        return PagerAdapter.POSITION_NONE
    }

    fun setFragmentPages(fragmentList: List<Fragment>) {
        fragments = fragmentList.toTypedArray()
    }


    fun setPageTitles(pageTitles: ArrayList<String>) {
        this.titles = pageTitles.toTypedArray()
    }


    override fun getPageTitle(position: Int): CharSequence? {
        return titles?.let {
            return if (position in it.indices) {
                it[position]
            } else {
                null
            }
        } ?: super.getPageTitle(position)
    }

}
