package com.metalichecky.amonguseditor.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.metalichecky.amonguseditor.R
import com.metalichecky.amonguseditor.model.settings.Language
import kotlinx.android.synthetic.main.layout_item_language.view.*

class LanguageAdapter(
    val languages: List<Language> = listOf()
): BaseAdapter() {

    override fun getView(idx: Int, convertView: View?, parent: ViewGroup?): View {
        return if (convertView == null) {
            val inflater = LayoutInflater.from(parent?.context)
            val view = inflater.inflate(R.layout.layout_item_language, parent, false)
            val language = languages.get(idx)
            view.tvLanguage.setText(language.nameResId)
            view.ivLanguage.setImageResource(language.imageResId)
            view
        } else {
            convertView
        }
    }

    override fun getItem(idx: Int): Any {
        return languages.get(idx)
    }

    override fun getItemId(idx: Int): Long {
        return languages.get(idx).ordinal.toLong()
    }

    override fun getCount(): Int {
        return languages.size
    }
}