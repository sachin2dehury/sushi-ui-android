package com.zomato.sushilib.atoms.buttons

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color.WHITE
import android.os.Build
import android.support.annotation.AttrRes
import android.support.annotation.ColorInt
import android.support.annotation.StyleRes
import android.support.design.button.MaterialButton
import android.util.AttributeSet
import android.util.TypedValue
import com.zomato.sushilib.R
import com.zomato.sushilib.annotations.ButtonDimension
import com.zomato.sushilib.annotations.ButtonType
import com.zomato.sushilib.annotations.FontWeight
import com.zomato.sushilib.utils.text.TextFormatUtils
import com.zomato.sushilib.utils.theme.ResourceThemeResolver.getThemeWrappedContext
import com.zomato.sushilib.utils.theme.ResourceThemeResolver.getThemedColorFromAttr
import com.zomato.sushilib.utils.widgets.ButtonStyleUtils
import com.zomato.sushilib.utils.widgets.DrawableSetters
import com.zomato.sushilib.utils.widgets.TextViewUtils

open class SushiButton @JvmOverloads constructor(
    ctx: Context, attrs: AttributeSet? = null,
    @AttrRes defStyleAttr: Int = R.attr.buttonStyle, @StyleRes defStyleRes: Int = 0
) : MaterialButton(
    getThemeWrappedContext(ctx, defStyleRes),
    attrs,
    defStyleAttr
), DrawableSetters {

    @ButtonDimension
    private var buttonDimension: Int = ButtonDimension.LARGE
    @ButtonType
    private var buttonType: Int = ButtonType.SOLID
    @ColorInt
    private var buttonColor: Int = getThemedColorFromAttr(context, R.attr.colorAccent)
    @ColorInt
    private var buttonTextColor: Int = WHITE
    @ColorInt
    private var customStrokeColor: Int = buttonColor
    private var buttonStrokeWidth: Int = -1

    @FontWeight
    var textFontWeight: Int = FontWeight.REGULAR
        set(value) {
            field = value
            setTextAppearance(TextFormatUtils.textFontWeightToTextAppearance(value))
        }

    init {

        context?.theme?.obtainStyledAttributes(
            attrs,
            R.styleable.SushiButton,
            defStyleAttr, 0
        )?.let {

            buttonDimension = it.getInt(R.styleable.SushiButton_buttonDimension, ButtonDimension.LARGE)
            buttonType = it.getInt(R.styleable.SushiButton_buttonType, ButtonType.SOLID)
            buttonColor = it.getColor(R.styleable.SushiButton_buttonColor, buttonColor)
            buttonTextColor = it.getColor(R.styleable.SushiButton_buttonTextColor, buttonTextColor)
            buttonStrokeWidth = it.getDimensionPixelOffset(R.styleable.SushiButton_buttonStrokeWidth, -1)
            customStrokeColor = buttonColor

            val attrTextFontWeight = it.getInt(R.styleable.SushiButton_textFontWeight, -1)
            // Only do this if someone has actually set this attr in xml
            if (attrTextFontWeight != -1) {
                textFontWeight = attrTextFontWeight
            }
            if (icon != null) {
                throw IllegalArgumentException(
                    """
                    SushiButton uses app:drawableLeft, app:drawableStart etc
                    app:icon attribute is not supported.
                """.trimIndent()
                )
            }
            reapplyStyles()
            reapplySizes()

            // If user has set a ColorStateList or Color we take that
            it.getColorStateList(R.styleable.SushiButton_strokeColor)?.let { strokeColorStateList ->
                strokeColor = strokeColorStateList
            } ?: setStrokeColor(it.getColor(R.styleable.SushiButton_strokeColor, buttonColor))

            it.recycle()

            TextViewUtils.apply {
                applyDrawables(
                    attrs, defStyleAttr,
                    currentTextColor,
                    (textSize).toInt(),
                    0.9f
                )
            }
        }
    }

    override fun setTextAppearance(resId: Int) {
        @Suppress("DEPRECATION")
        super.setTextAppearance(context, resId)
    }

    @ColorInt
    fun getButtonColor(): Int {
        return buttonColor
    }

    fun setButtonColor(@ColorInt color: Int) {
        if (color == buttonColor) return
        buttonColor = color
        customStrokeColor = color
        reapplyStyles()
    }

    @ButtonType
    fun getButtonType(): Int {
        return buttonType
    }

    fun setButtonType(@ButtonType style: Int) {
        if (style == buttonType) return
        buttonType = style
        reapplyStyles()
    }

    private fun reapplyStyles() {
        ButtonStyleUtils.apply {
            applyStrokeWidth()
            applyIconPadding()
            applyRippleColor()
            applyBackgroundTintList()
            applyIconAndTextColor()
            applyStrokeColor(customStrokeColor)
        }
    }

    @ButtonDimension
    fun getButtonDimension(): Int {
        return buttonDimension
    }

    fun setButtonDimension(@ButtonDimension size: Int) {
        if (size == buttonDimension) return
        buttonDimension = size
        reapplySizes()
    }

    override fun setCompoundDrawableTintList(tintList: ColorStateList?) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            super.setCompoundDrawableTintList(tintList)
        } else {
            compoundDrawables.forEach { d ->
                d?.setTintList(tintList)
            }
            compoundDrawablesRelative.forEach { d ->
                d?.setTintList(tintList)
            }
        }
    }

    fun getButtonStrokeWidth(): Int {
        return buttonStrokeWidth
    }

    fun getButtonTextColor(): Int {
        return buttonTextColor
    }

    private fun setStrokeColor(@ColorInt color: Int) {
        customStrokeColor = color
        ButtonStyleUtils.apply {
            applyStrokeColor(customStrokeColor)
        }
    }

    private fun reapplySizes() {
        when (buttonDimension) {
            ButtonDimension.LARGE -> {
                iconSize = resources.getDimensionPixelSize(R.dimen.sushi_iconsize_500)
                minHeight = resources.getDimensionPixelSize(R.dimen.sushi_button_large_minheight)
                setTextSize(
                    TypedValue.COMPLEX_UNIT_PX,
                    resources.getDimension(R.dimen.sushi_textsize_500)
                )
                compoundDrawablePadding = resources.getDimensionPixelSize(R.dimen.sushi_spacing_micro)
            }
            ButtonDimension.MEDIUM -> {
                iconSize = resources.getDimensionPixelSize(R.dimen.sushi_iconsize_300)
                minHeight = resources.getDimensionPixelSize(R.dimen.sushi_button_medium_minheight)
                setTextSize(
                    TypedValue.COMPLEX_UNIT_PX,
                    resources.getDimension(R.dimen.sushi_textsize_300)
                )
                compoundDrawablePadding = resources.getDimensionPixelSize(R.dimen.sushi_spacing_nano)
            }
            ButtonDimension.SMALL -> {
                iconSize = resources.getDimensionPixelSize(R.dimen.sushi_iconsize_200)
                minHeight = resources.getDimensionPixelSize(R.dimen.sushi_button_small_minheight)
                setTextSize(
                    TypedValue.COMPLEX_UNIT_PX,
                    resources.getDimension(R.dimen.sushi_textsize_200)
                )
                compoundDrawablePadding = resources.getDimensionPixelSize(R.dimen.sushi_spacing_pico)
            }
        }
        if (buttonType == ButtonType.TEXT) {
            minHeight = 0
            val picoPad = resources.getDimensionPixelSize(R.dimen.sushi_spacing_femto)
            setPadding(picoPad, picoPad, picoPad, picoPad)
        }
    }

}
