package com.zomato.sushiapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import com.zomato.sushiapp.fragments.*
import kotlinx.android.synthetic.main.app_bar_main.*

class ComponentActivity : AppCompatActivity() {

    companion object {
        const val COLORS = 0
        const val TYPOGRAPHY = 1
        const val IMAGES = 2
        const val ICONS = 3
        const val BUTTONS = 4
        const val FORM_FIELDS = 5
        const val SNIPPETS = 6
        const val TAGS = 7

        fun start(context: Context, component: Int) {
            Intent(context, ComponentActivity::class.java).apply {
                putExtra("type", component)
                context.startActivity(this)
            }

        }

        private fun getFragment(component: Int): Fragment = when (component) {
            COLORS -> ColorPaletteFragment()
            TYPOGRAPHY -> TextStylesFragment()
            IMAGES -> ImageViewsFragment()
            ICONS -> ColorPaletteFragment()
            BUTTONS -> ButtonsFragment()
            FORM_FIELDS -> TextFieldsFragment()
            SNIPPETS -> ListingFragment()
            TAGS -> TagsFragment()
            else -> ColorPaletteFragment()
        }


    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_component)
        val fragment = getFragment(intent?.getIntExtra("type", COLORS) ?: COLORS)
        setupActionBar()
        supportFragmentManager.beginTransaction()
            .replace(R.id.container_main, fragment)
            .commit()
    }

    private fun setupActionBar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.getItemId()
        if (id == android.R.id.home) {
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }


}