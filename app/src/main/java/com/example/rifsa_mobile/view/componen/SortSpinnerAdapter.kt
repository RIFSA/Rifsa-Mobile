package com.example.rifsa_mobile.view.componen

import android.content.Context
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.rifsa_mobile.R
import java.util.Locale

class SortSpinnerAdapter(
    context : Context
): ArrayAdapter<SortFinance>(context,0,SortFinance.values()) {
    val layoutInflater : LayoutInflater = LayoutInflater.from(context)

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view : View

        if (convertView == null) {
            view = layoutInflater.inflate(R.layout.custom_dropdown, parent, false)
        } else {
            view = convertView
        }

        getItem(position)?.let { country ->
            setItemForCountry(view, country)
        }

        return view
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view: View

        if (position == 0) {
            view = layoutInflater.inflate(R.layout.header_sort, parent, false)
            view.setOnClickListener {
                val root = parent.rootView
                root.dispatchKeyEvent(KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_BACK))
                root.dispatchKeyEvent(KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_BACK))
            }
        } else {
            view = layoutInflater.inflate(R.layout.custom_dropdown, parent, false)

            getItem(position)?.let { country ->
                setItemForCountry(view, country)
            }
        }
        return view
    }

    override fun getItem(position: Int): SortFinance? {
        if (position == 0) {
            return null
        }
        return super.getItem(position - 1)
    }

    override fun getCount() = super.getCount() + 1

    override fun isEnabled(position: Int) = position != 0

    private fun setItemForCountry(view: View, country: SortFinance) {
        val tvCountry = view.findViewById<TextView>(R.id.tv_dropdown_text)
        val ivCountry = view.findViewById<ImageView>(R.id.ic_dropdown)
        tvCountry.text = country.title
        ivCountry.setBackgroundResource(country.icon)
    }

}