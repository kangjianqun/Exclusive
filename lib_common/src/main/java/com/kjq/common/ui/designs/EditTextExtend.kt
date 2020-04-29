package com.kjq.common.ui.designs

import android.content.Context
import android.text.InputFilter
import android.util.AttributeSet
import android.widget.EditText
import androidx.appcompat.widget.AppCompatEditText
import androidx.databinding.BindingAdapter

//@InverseBindingMethods(
//        InverseBindingMethod(type = EditTextExtend::class, attribute = "commonEditTextNotInput", event = "commonEditTextNotInputChanged"))
class EditTextExtend : AppCompatEditText {

    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
    }

    companion object {
        /**
         * EditText输入文字改变的监听
         */
        @JvmStatic
        @BindingAdapter(value = ["commonEditTextNotInput"])
        fun setCommonEditTextNotInput(editText: EditText, type: Int) {
            if (type == 1) {
                setEditTextInhibitInputSpace(editText)
            }
        }

        /**
         * 禁止输入空格
         * @param editText
         */
        fun setEditTextInhibitInputSpace(editText: EditText) {
            val sInputFilter = InputFilter { source, _, _, _, _, _ ->
                if (source == " ") {
                    ""
                } else {
                    source
                }
            }
            editText.filters = arrayOf(sInputFilter)
        }

    }


}