package com.zomato.sushilib.molecules.inputfields

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.support.annotation.AttrRes
import android.support.design.widget.TextInputEditText
import android.support.design.widget.TextInputLayout
import android.support.v4.content.ContextCompat
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.LayoutDirection
import android.view.MotionEvent
import com.zomato.sushilib.R
import com.zomato.sushilib.utils.widgets.TextViewUtils.applyDrawables


/**
 * created by championswimmer on 02/04/19
 * Copyright (c) 2019 Zomato Media Pvt. Ltd.
 */
open class SushiTextInputField @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null,
    @AttrRes defStyleAttr: Int = android.support.design.R.attr.textInputStyle
) : TextInputLayout(context, attrs, defStyleAttr) {

    interface EdgeDrawableClickListener {
        /**
         * method called when drawableLeft (or drawableStart
         * if RTL supported) is clicked
         */
        fun onDrawableStartClicked()

        /**
         * method called when drawableRight (or drawableEnd
         * if RTL supported) is clicked
         */
        fun onDrawableEndClicked()
    }

    interface TextValidator {
        /**
         * A function called every time the text changes, which you can
         * hook into to generate error messages
         *
         * @param text The text currenly in the EditText
         * @return An error as [String] if the text is invalid
         * or [null] if the text is valid
         */
        fun validateText(text: Editable?): String?
    }


    private var mEdgeDrawableClickListener: EdgeDrawableClickListener? = null
    private var mTextValidator: TextValidator? = null

    private var mEditText: TextInputEditText

    /**
     * Set an [EdgeDrawableClickListener]
     */
    fun setEdgeDrawableClickListener(listener: EdgeDrawableClickListener?) {
        if (mEdgeDrawableClickListener == null && listener != null) {
            prepareOnTouchListener()
        }
        mEdgeDrawableClickListener = listener
    }

    fun setTextValidator(validator: TextValidator?) {
        if (mTextValidator == null && validator != null) {
            prepareOnTextChangedListener()
        }

        mTextValidator = validator
    }

    fun setTextValidator(validator: (text: Editable?) -> String?) {
        setTextValidator(object : TextValidator {
            override fun validateText(text: Editable?): String? = validator(text)
        })
    }


    init {
        // WARNING: Never change the theme of this context
        mEditText = TextInputEditText(context)

        context?.obtainStyledAttributes(
            attrs,
            R.styleable.SushiTextInputField,
            defStyleAttr,
            0
        )?.let {
            val attrInputType = it.getInt(
                R.styleable.SushiTextInputField_android_inputType,
                -1
            )
            if (attrInputType != -1) {
                mEditText.inputType = attrInputType
            }
            mEditText.applyDrawables(
                attrs, defStyleAttr,
                ContextCompat.getColor(context, R.color.sushi_grey_400) ?: Color.GRAY,
                mEditText.textSize.toInt()
            )

            it.recycle()
        }
        addView(mEditText)
    }

    private fun prepareOnTextChangedListener() {
        mEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                error = mTextValidator?.validateText(s)
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun prepareOnTouchListener() {
        mEditText.setOnTouchListener { v, event ->
            val drRight: Drawable? =
                editText?.compoundDrawablesRelative?.get(2)
                    ?: editText?.compoundDrawables?.get(2)

            val drLeft: Drawable? =
                editText?.compoundDrawablesRelative?.get(0)
                    ?: editText?.compoundDrawables?.get(0)

            if (event.action == MotionEvent.ACTION_UP) {

                if (drLeft != null) {
                    if (layoutDirection == LayoutDirection.LTR) {
                        if (event.rawX <= (editText!!.left + editText!!.paddingLeft + drLeft.bounds.width())) {
                            mEdgeDrawableClickListener?.onDrawableStartClicked()
                            return@setOnTouchListener true
                        }
                    } else {
                        if (event.rawX >= (editText!!.right - drLeft.bounds.width())) {
                            mEdgeDrawableClickListener?.onDrawableStartClicked()
                            return@setOnTouchListener true
                        }
                    }
                }

                if (drRight != null) {
                    if (layoutDirection == LayoutDirection.LTR) {
                        if (event.rawX >= (editText!!.right - drRight.bounds.width())) {
                            mEdgeDrawableClickListener?.onDrawableEndClicked()
                            return@setOnTouchListener true
                        }
                    } else {
                        if (event.rawX <= (editText!!.left + editText!!.paddingLeft + drRight.bounds.width())) {
                            mEdgeDrawableClickListener?.onDrawableEndClicked()
                            return@setOnTouchListener true
                        }
                    }

                }

            }
            // Unless one of the above cases hit, we do not consume this event
            return@setOnTouchListener false
        }
    }
}