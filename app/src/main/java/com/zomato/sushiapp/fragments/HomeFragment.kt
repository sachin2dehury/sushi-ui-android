package com.zomato.sushiapp.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.res.ResourcesCompat
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.zomato.sushiapp.ComponentActivity
import com.zomato.sushiapp.R
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.android.synthetic.main.fragment_main.view.*


class HomeFragment : Fragment() {

    private val clickListener = View.OnClickListener {
        val component = when (it.id) {
            R.id.nav_text_styles -> ComponentActivity.TYPOGRAPHY
            R.id.nav_text_fields -> ComponentActivity.FORM
            R.id.nav_color_palette -> ComponentActivity.COLORS
            R.id.nav_image_views -> ComponentActivity.IMAGES
            R.id.nav_buttons -> ComponentActivity.BUTTONS
            R.id.nav_tags -> ComponentActivity.TAGS
            R.id.nav_listing -> ComponentActivity.SNIPPETS
            R.id.nav_menu_tabs -> ComponentActivity.MENU_TABS
            else -> null
        }
        component ?: return@OnClickListener
        activity?.let {

            ComponentActivity.start(it, component)

        }

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as? AppCompatActivity)?.setSupportActionBar(view.toolbar)

        collapsible_toolbar.apply {
            setExpandedTitleTypeface(ResourcesCompat.getFont(context, R.font.okra_light))
            setCollapsedTitleTypeface(ResourcesCompat.getFont(context, R.font.okra_light))
        }

        view.apply {
            findViewById<View>(R.id.nav_text_styles)?.setOnClickListener(clickListener)
            findViewById<View>(R.id.nav_text_fields)?.setOnClickListener(clickListener)
            findViewById<View>(R.id.nav_color_palette)?.setOnClickListener(clickListener)
            findViewById<View>(R.id.nav_image_views)?.setOnClickListener(clickListener)
            findViewById<View>(R.id.nav_buttons)?.setOnClickListener(clickListener)
            findViewById<View>(R.id.nav_tags)?.setOnClickListener(clickListener)
            findViewById<View>(R.id.nav_listing)?.setOnClickListener(clickListener)
            findViewById<View>(R.id.nav_menu_tabs)?.setOnClickListener(clickListener)
        }
    }

}
