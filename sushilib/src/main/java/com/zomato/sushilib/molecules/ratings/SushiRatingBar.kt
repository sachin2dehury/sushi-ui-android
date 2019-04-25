package com.zomato.sushilib.molecules.ratings

import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.zomato.sushilib.R

open class SushiRatingBar @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : BaseRatingView(context, attributeSet, defStyleAttr, defStyleRes) {

    override fun createRatingChild(rating: Int): View = SushiBigRatingTag(context).apply {
        val params = generateDefaultLayoutParams()
        (params as? MarginLayoutParams)?.let {
            if (rating != 5) {
                it.marginEnd = resources.getDimensionPixelSize(R.dimen.sushi_spacing_between)
            }
        }
        layoutParams = params
    }

    override fun isRatingClickable(): Boolean = true
}