package com.example.everymoment.extensions

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import android.widget.ImageView
import android.widget.PopupWindow
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import com.example.everymoment.databinding.CategoryPopupBinding
import com.example.everymoment.R
import com.example.everymoment.presentation.viewModel.DiaryViewModel

class CategoryPopup(
    private val fragmentActivity: FragmentActivity,
    private val context: Context,
    private val viewModel: DiaryViewModel
) {

    private lateinit var categoryPopup: PopupWindow
    private var categoryCount = 0
    private lateinit var popupView: CategoryPopupBinding
    private lateinit var gridLayout: GridLayout
    private lateinit var addButton: ImageView
    private val maxRows: Int = 4
    private val itemsPerRow: Int = 3

    private var listener: ((String?) -> Unit)? = null
    private lateinit var addCategoryDialog: CustomEditDialog
    private lateinit var delCategoryDialog: CustomDialog
    private lateinit var delSelectedCategory: TextView


    fun showCategoryPopup(
        anchorView: View,
        xOffset: Int = 0,
        yOffset: Int = 0,
        onCategorySelected: ((String?) -> Unit)
    ) {
        this.listener = onCategorySelected

        if (!::categoryPopup.isInitialized) {
            setCategryPopup()
            setAddCategoryDialog()
            setDelCategoryDialog()
        }

        categoryPopup = PopupWindow(
            popupView.root,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        categoryPopup.isOutsideTouchable = true
        categoryPopup.isFocusable = true

        val location = IntArray(2)
        anchorView.getLocationOnScreen(location)
        categoryPopup.showAtLocation(
            anchorView,
            Gravity.NO_GRAVITY,
            location[0] + xOffset,
            location[1] + yOffset
        )

        popupView.root.setOnTouchListener { view, event ->
            view.parent.requestDisallowInterceptTouchEvent(true)
            view.onTouchEvent(event)
            true
        }
    }

    private fun setCategryPopup() {
        popupView = CategoryPopupBinding.inflate((context as Activity).layoutInflater)
        addButton = popupView.addCategory
        gridLayout = popupView.categoryGrid

        addButton.setOnClickListener {
            if (categoryCount < maxRows * itemsPerRow) {
                addCategoryDialog.show(fragmentActivity.supportFragmentManager, "CustomEditDialog")
            }
        }

        val size = viewModel.getCategoryListSize()
        if (size != null && size != 0) {
            val categoryList = viewModel.categories.value
            if (categoryList != null) {
                for (category in categoryList) {
                    addCategoryTextView(category.categoryName)
                }
            }
        }

    }

    private fun setAddCategoryDialog() {
        addCategoryDialog = CustomEditDialog(
            context.resources.getString(R.string.category_add_dialog),
            "",
            context.resources.getString(R.string.example_category),
            context.resources.getString(R.string.category_add_dialog_instruction),
            context.resources.getString(R.string.cancel),
            context.resources.getString(R.string.save),
            onPositiveClick = {
                val category = it.trim()
                if (checkCategory(category) == -1) {
                    addCategoryDialog.setWrongInstruction(context.getString(R.string.category_less_one))
                } else if (checkCategory(category) == 1) {
                    addCategoryDialog.setWrongInstruction(context.getString(R.string.category_more_six))
                } else {
                    addCategoryTextView(category)
                    viewModel.postCategory(category)
                    addCategoryDialog.dismiss()
                }
            },
            removeEditText = true
        )
    }

    private fun setDelCategoryDialog() {
        delCategoryDialog = CustomDialog(
            context.resources.getString(R.string.del_category_dialog),
            context.resources.getString(R.string.cancel),
            context.resources.getString(R.string.delete),
            onPositiveClick = {
                delCategory(delSelectedCategory)
            }
        )
    }

    private fun delCategory(textView: TextView) {
        viewModel.getCategoryId(textView.text.toString())
            ?.let {
                viewModel.delCategory(it)
                gridLayout.removeView(textView)
                categoryCount -= 1
                moveAddButtonToNext()
            }
    }

    private fun checkCategory(userInput: String): Int {
        if (userInput.isEmpty()) return -1
        else if (userInput.length > 6) return 1
        else return 0
    }

    private fun addCategoryTextView(userInput: String) {
        val textView = TextView(context).apply {
            textSize = 16f
            layoutParams = GridLayout.LayoutParams().apply {
                width = GridLayout.LayoutParams.WRAP_CONTENT
                height = GridLayout.LayoutParams.MATCH_PARENT

                setGravity(Gravity.CENTER)
                setMargins(10, 10, 20, 20)
            }
            background = resources.getDrawable(R.drawable.category_background, null)
            setPadding(15, 10, 15, 10)
            setTextColor(Color.WHITE)
            text = resources.getString(R.string.category_text, userInput)

            setOnClickListener {
                listener?.invoke(this.text.toString())
            }

            setOnLongClickListener {
                delCategoryDialog.show(fragmentActivity.supportFragmentManager, "delCategoryDialog")
                delSelectedCategory = this
                true
            }
        }
        gridLayout.addView(textView)
        categoryCount += 1
        moveAddButtonToNext()
    }

    private fun moveAddButtonToNext() {
        gridLayout.removeView(addButton)
        gridLayout.addView(addButton)
        if (categoryCount >= maxRows * itemsPerRow) {
            addButton.visibility = View.GONE
        }
    }

}
